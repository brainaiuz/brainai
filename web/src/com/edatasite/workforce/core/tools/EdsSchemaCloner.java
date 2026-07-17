package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.springframework.core.env.AbstractEnvironment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class EdsSchemaCloner {

    public static Properties props;

    static {
        props = new Properties();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        if (StringUtil.isEmpty(profile)) {
            System.out.println("--------------------------");
            System.out.println("Please set spring.profiles.active!!!");
            System.out.println("VM options add this -Dspring.profiles.active=local or app");
            System.out.println("--------------------------");
            System.exit(0);
        }
        InputStream stream0 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/hibernate.properties");
        InputStream stream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/application.properties");
        if (stream0 != null) {
            try {
                props.load(stream0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (stream1 != null) {
            try {
                props.load(stream1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Clone a Postgres schema by:
     * 1) pg_dump (custom) of template schema
     * 2) pg_restore to SQL on stdout
     * 3) rewrite schema identifiers from templateSchema -> newSchema
     * 4) pipe into psql --single-transaction
     * <p>
     * Retry behavior: DROP SCHEMA newSchema CASCADE; CREATE SCHEMA newSchema;
     * Concurrency: per-newSchema advisory lock, fail-fast (no waiting).
     */
    public static void cloneSchema(String templateSchema, String newSchema) throws Exception {
        int port = 5432;
        String host = props.getProperty("tenant.datasource.master.FREE.host");
        String dbName = props.getProperty("tenant.datasource.master.FREE.db");
        String dbUserName = props.getProperty("tenant.datasource.username");
        String dbPassword = props.getProperty("tenant.datasource.password");

        if (StringUtil.isEmpty(templateSchema) || StringUtil.isEmpty(newSchema)) {
            throw new IllegalArgumentException("templateSchema and newSchema must be non-empty");
        }

        String from = quoteIdent(templateSchema);
        String to = quoteIdent(newSchema);

        Path dumpFile = Files.createTempFile("schema-" + templateSchema + "-", ".dump");
        try {
            runProcess(
                    Map.of("PGPASSWORD", dbPassword),
                    List.of(
                            "pg_dump",
                            "-h", host,
                            "-p", String.valueOf(port),
                            "-U", dbUserName,
                            "-d", dbName,
                            "-F", "c",
                            "--schema=" + templateSchema,
                            "--no-owner",
                            "--no-acl",
                            "--quote-all-identifiers",
                            "-f", dumpFile.toString()
                    )
            );

            Process restore = new ProcessBuilder(
                    "pg_restore",
                    "--no-owner",
                    "--no-acl",
                    "--schema=" + templateSchema,
                    "-f", "-",
                    dumpFile.toString()
            ).redirectErrorStream(true).start();

            ProcessBuilder psqlPb = new ProcessBuilder(
                    "psql",
                    "-h", host,
                    "-p", String.valueOf(port),
                    "-U", dbUserName,
                    "-d", dbName,
                    "-v", "ON_ERROR_STOP=1",
                    "--single-transaction"
            ).redirectErrorStream(true);

            psqlPb.environment().put("PGPASSWORD", dbPassword);
            Process psql = psqlPb.start();

            ExecutorService es = Executors.newFixedThreadPool(2);

            Future<?> pipe = es.submit(() -> {
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(restore.getInputStream(), StandardCharsets.UTF_8));
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(psql.getOutputStream(), StandardCharsets.UTF_8))
                ) {
                    String lockKeyExpr = "hashtext('tenant_schema_provisioning:" + escapeSqlLiteral(newSchema) + "')";

                    out.write("DO $$ BEGIN "
                            + "IF NOT pg_try_advisory_lock(" + lockKeyExpr + ") THEN "
                            + "RAISE EXCEPTION 'Provisioning in progress for schema " + escapeSqlLiteral(newSchema) + " (advisory lock busy)'; "
                            + "END IF; "
                            + "END $$;");
                    out.newLine();

                    out.write("DROP SCHEMA IF EXISTS " + to + " CASCADE;");
                    out.newLine();
                    out.write("CREATE SCHEMA " + to + ";");
                    out.newLine();

                    String line;
                    while ((line = in.readLine()) != null) {
                        line = line.replace("SET search_path = " + from + ";", "SET search_path = " + to + ";");
                        line = line.replace("SET search_path = " + from + ", pg_catalog;", "SET search_path = " + to + ", pg_catalog;");
                        line = line.replace("SET search_path = " + from + ", public;", "SET search_path = " + to + ", public;");

                        line = line.replace(from + ".", to + ".");
                        line = line.replace("SCHEMA " + from, "SCHEMA " + to);

                        out.write(line);
                        out.newLine();
                    }

                    out.write("SELECT pg_advisory_unlock(" + lockKeyExpr + ");");
                    out.newLine();

                    out.flush();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });

            Future<String> psqlLog = es.submit(() -> readAll(psql.getInputStream()));

            int restoreExit = restore.waitFor();

            try {
                pipe.get();
            } finally {
                try {
                    psql.getOutputStream().close();
                } catch (IOException ignored) {
                }
            }

            int psqlExit = psql.waitFor();
            es.shutdownNow();

            String psqlOut;
            try {
                psqlOut = psqlLog.get(2, TimeUnit.SECONDS);
            } catch (Exception e) {
                psqlOut = "(unable to read psql output: " + e.getMessage() + ")";
            }

            if (restoreExit != 0 || psqlExit != 0) {
                throw new RuntimeException(
                        "Schema clone failed. pg_restore exit=" + restoreExit + ", psql exit=" + psqlExit +
                                "\npsql output:\n" + psqlOut
                );
            }

        } finally {
            try {
                Files.deleteIfExists(dumpFile);
            } catch (IOException ignored) {
            }
        }
    }

    private static void runProcess(Map<String, String> extraEnv, List<String> cmd) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd).redirectErrorStream(true);
        pb.environment().putAll(extraEnv);

        Process p = pb.start();
        p.getOutputStream().close();

        String out = readAll(p.getInputStream());
        int code = p.waitFor();
        if (code != 0) {
            throw new RuntimeException("Command failed (" + code + "): " + String.join(" ", cmd) + "\n" + out);
        }
    }

    private static String quoteIdent(String ident) {
        return "\"" + ident.replace("\"", "\"\"") + "\"";
    }

    private static String escapeSqlLiteral(String s) {
        return s.replace("'", "''");
    }

    private static String readAll(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
}
