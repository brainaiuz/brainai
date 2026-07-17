package com.edatasite.workforce.core.tools;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.edatasite.workforce.core.UpdateLogger;
import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.tools.sqlpatcher.ScriptItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.google.gson.Gson;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.internal.ExceptionHandlerCollectingImpl;
import org.hibernate.tool.schema.internal.ExceptionHandlerHaltImpl;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToWriter;
import org.hibernate.tool.schema.spi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.StopWatch;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 24.11.2009
 * Time: 0:22:04
 * To change this template use File | Settings | File Templates.
 */

public class EdsSchemaUpdater extends UpdateLogger {
    static final Map<String, StackTraceElement[]> errorCompaniesList = new Hashtable<>();
    private static final Logger log = LoggerFactory.getLogger(EdsSchemaUpdater.class);
    private static final List<Exception> exceptions = new ArrayList<>();
    public static Properties props;
    static boolean haltOnError = false;
    private static String dbUrl;
    public static String patchDirectory;
    private static List<String> doneList = new ArrayList<>();
    private static Map<String, String> failedPatches = new ConcurrentHashMap<>();

    private static final Set<String> SCHEMA_VERSION_COPY_EXCLUSIONS = Set.of(
            TemplateSchema.TEMPLATE.getTemplate()
    );

    static {
        //try retrieve data from file]
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

    public static void updateSchema(String schemaName) {
        updateSchema(schemaName, TenantContextHolder.FREE_DB);
    }

    public static void updateSchema(String schemaName, String cluster) {
        log.info("Starting schema: - " + schemaName + " update");
        String slashedSchema = "\"" + schemaName + "\"";

        exceptions.clear();

        StringWriter stringWriter = new StringWriter();
        List<String> statements = new ArrayList<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("metadata");
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .applySettings(getProperties(slashedSchema))
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Metadata metadata = metadataSources.buildMetadata();
        stopWatch.stop();
        log.info(stopWatch.getLastTaskName() + " took: " + stopWatch.getLastTaskTimeMillis() + "ms");

        Map<String, Object> config = new HashMap<>(serviceRegistry.getService(ConfigurationService.class).getSettings());

        final SchemaManagementTool tool = serviceRegistry.getService(SchemaManagementTool.class);

        final ExceptionHandler exceptionHandler = haltOnError
                ? ExceptionHandlerHaltImpl.INSTANCE
                : new ExceptionHandlerCollectingImpl();

        final ExecutionOptions executionOptions = SchemaManagementToolCoordinator.buildExecutionOptions(
                config,
                exceptionHandler
        );

        final TargetDescriptor targetDescriptor = new TargetDescriptor() {
            @Override
            public EnumSet<TargetType> getTargetTypes() {
                return EnumSet.of(TargetType.SCRIPT);
            }

            @Override
            public ScriptTargetOutput getScriptTargetOutput() {
                return new ScriptTargetOutputToWriter(stringWriter);
            }
        };

        try {
            stopWatch.start("migration");

            tool.getSchemaMigrator(config).doMigration(metadata, executionOptions, targetDescriptor);
            String sqlString = stringWriter.toString();

            stopWatch.stop();
            log.info(stopWatch.getLastTaskName() + " took: " + stopWatch.getLastTaskTimeMillis() + "ms");

            List<String> list = StringUtils.isNotBlank(sqlString) ? Arrays.asList(sqlString.split(";")) : Collections.emptyList();
            for (String sql : list) {
                sql = sql.replace(slashedSchema, "\"anv\"") + ";";

                // 1. CREATE TABLE IF NOT EXISTS
                sql = sql.replace("create table", "create table if not exists");

                // 2. ADD COLUMN IF NOT EXISTS
                sql = sql.replace("add column", "add column if not exists");

                // 3. ADD CONSTRAINT ....
                if (sql.contains("add constraint")) {
                    StringBuilder sb = new StringBuilder("DO $$ BEGIN BEGIN ");
                    sb.append(sql);
                    sb.append("EXCEPTION WHEN others THEN RAISE NOTICE 'NOOP'; END; END $$;");
                    sql = sb.toString();
                }

                // 4. CREATE INDEX IF NOT EXISTS ....
                sql = sql.replace("create index", "create index if not exists");

                statements.add(sql);
            }
        } finally {
            if (exceptionHandler instanceof ExceptionHandlerCollectingImpl) {
                exceptions.addAll(((ExceptionHandlerCollectingImpl) exceptionHandler).getExceptions());
            }
            serviceRegistry.close();
        }

        log.info("scripts count: " + statements.size());

        SqlScriptExecutor executor = new SqlScriptExecutor(props);
        ScriptItem scriptItem = new ScriptItem(SqlScriptExecutor.SCHEMA_UPDATE);
        scriptItem.setSchema(schemaName);
        scriptItem.setStatements(statements);
        scriptItem.setClusterType(cluster);
        executor.execute(scriptItem, true);
        executor.stop();

        log.info("All " + stopWatch.getTaskCount() + " tasks took: " + stopWatch.getTotalTimeMillis() + "ms");
        log.debug("Execution Completed for schema: - " + schemaName);
    }

    public static void executeSqlStatements(List<String> statements) {
        if (dbUrl == null) {
            dbUrl = props.getProperty("tenant.datasource.master.FREE.url");
        }
        // SQL statementlarni ketma-ket bajarish
        for (String sql : statements) {
            try (Connection connection = DriverManager.getConnection(dbUrl,  props.getProperty("tenant.datasource.username"), props.getProperty("tenant.datasource.password"));
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                System.err.println("Error executing SQL: " + sql);
//                e.printStackTrace();
                // Xatoni ignore qilib, keyingi SQL statementga o'tish
            }
        }
    }

    public static void createAsSchema(String name, String defaultSchema) {
        log.debug("Starting schema: - " + name + " update");
        String slashedName = "\"" + name + "\"";
        String sourceSchema = "\"" + defaultSchema + "\"";

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .applySettings(getProperties(slashedName))
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Metadata metadata = metadataSources.buildMetadata();


        List<String> scripts = new ArrayList<>();
        scripts.add("CREATE SCHEMA \"" + name + "\" AUTHORIZATION " + getDBUserName() + ";");
        for (Table table : metadata.collectTableMappings()) {
            if (table.isPhysicalTable() && (table.getSchema() == null || !table.getSchema().equals("public"))) {
                scripts.add(
                        sqlCreateAsString(
                                metadata.getDatabase().getDialect(),
                                table,
                                props.getProperty(Environment.DEFAULT_CATALOG),
                                slashedName,
                                sourceSchema
                        )
                );
            }
        }
        for (Table table : metadata.collectTableMappings()) {
            if (table.isPhysicalTable() && table.hasPrimaryKey() && (table.getSchema() == null || !table.getSchema().equals("public"))) {
                scripts.add(sqlAddPrimaryKeyString(metadata.getDatabase().getDialect(), table,
                        props.getProperty(Environment.DEFAULT_CATALOG), slashedName));
            }
        }

        scripts.add(generateNotNullAndDefaultConstraints(name));

        if (!SCHEMA_VERSION_COPY_EXCLUSIONS.contains(defaultSchema)) {
            scripts.add("create table " + name + ".schema_version AS select * from " + sourceSchema + ".schema_version;");
        }

        SessionFactory sessionFactory = metadata.buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            for (String script : scripts) {
                if (script.contains("anv.")) {
                    script = script.replaceAll("anv.", slashedName + ".");
                }
                session.createNativeQuery(script).executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            serviceRegistry.close();
        }

        log.debug("Execution Clone Completed for schema: - " + name);
    }

    public static String sqlCreateAsString(Dialect dialect, Table table, String defaultCatalog, String defaultSchema, String sourceSchema) {
        String res;
        String qualifiedName = getQualifiedName(table, defaultCatalog, defaultSchema);
        String sourceSchemaName = qualify(defaultCatalog, sourceSchema, table.getName());
        res = dialect.getCreateTableString() + " " + qualifiedName + " AS ";
        res += "select * from " + sourceSchemaName;
        return res;
    }

    public static String sqlAddPrimaryKeyString(Dialect dialect, Table table, String defaultCatalog, String defaultSchema) {
        StringBuilder buf = new StringBuilder();
        String tableName = getQualifiedName(table, defaultCatalog, defaultSchema);
        String seqName = tableName + "_id_seq";
        if (table.hasPrimaryKey()) {
            String primaryKey = table.getPrimaryKey().getColumns().get(0).getName();
            buf.append("alter table ").append(tableName).append(" add ").append(table.getPrimaryKey().sqlConstraintString(dialect)).append(";");
            if ("id".equals(primaryKey)) {
                buf.append(" CREATE SEQUENCE " + seqName + " OWNED BY " + tableName + "." + primaryKey + ";");
                buf.append(" DO $$ BEGIN PERFORM setval('" + seqName + "', COALESCE((SELECT MAX(" + primaryKey + ") FROM " + tableName + "), 0) + 1, false);  END $$;");
                buf.append(" ALTER TABLE " + tableName + " ALTER COLUMN " + primaryKey + " SET DEFAULT nextval('" + seqName + "');");
            }
        }

        return buf.toString();
    }
    /**hozircha faqat shular yediryatgani uchun static qo`shib qo`yildi*/
    public static String generateNotNullAndDefaultConstraints(String schemaName) {
        StringBuilder query = new StringBuilder();
        //modelfield
        query.append("ALTER TABLE ").append(schemaName).append(".modelfield\n");
        query.append("    ALTER COLUMN gridX SET NOT NULL, ALTER COLUMN gridX SET DEFAULT '0',\n");
        query.append("    ALTER COLUMN gridY SET NOT NULL, ALTER COLUMN gridY SET DEFAULT '0',\n");
        query.append("    ALTER COLUMN gridWidth SET NOT NULL, ALTER COLUMN gridWidth SET DEFAULT '4',\n");
        query.append("    ALTER COLUMN customizableTable SET NOT NULL, ALTER COLUMN customizableTable SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN isWorkflowAttribute SET NOT NULL, ALTER COLUMN isWorkflowAttribute SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN systemDisable SET NOT NULL, ALTER COLUMN systemDisable SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN hideInCustomizeForm SET NOT NULL, ALTER COLUMN hideInCustomizeForm SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN usableByWorkflow SET NOT NULL, ALTER COLUMN usableByWorkflow SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN disableUpdate SET NOT NULL, ALTER COLUMN disableUpdate SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN isEntityField SET NOT NULL, ALTER COLUMN isEntityField SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN fullWidth SET NOT NULL, ALTER COLUMN fullWidth SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN hide SET NOT NULL, ALTER COLUMN hide SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN isCustomField SET NOT NULL, ALTER COLUMN isCustomField SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN systemmandatory SET NOT NULL, ALTER COLUMN systemmandatory SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN mandatory SET NOT NULL, ALTER COLUMN mandatory SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN split SET NOT NULL, ALTER COLUMN split SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN gridHeight SET NOT NULL, ALTER COLUMN gridHeight SET DEFAULT '1';\n");
        //reference
        query.append("ALTER TABLE ").append(schemaName).append(".reference\n");
        query.append("    ALTER COLUMN deleted SET NOT NULL, ALTER COLUMN deleted SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN hasProrata SET NOT NULL, ALTER COLUMN hasProrata SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN isActive SET NOT NULL, ALTER COLUMN isActive SET DEFAULT 'true',\n");
        query.append("    ALTER COLUMN requiredComment SET NOT NULL, ALTER COLUMN requiredComment SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN shared SET NOT NULL, ALTER COLUMN shared SET DEFAULT 'true',\n");
        query.append("    ALTER COLUMN changed SET NOT NULL, ALTER COLUMN changed SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN isSystemReference SET NOT NULL, ALTER COLUMN isSystemReference SET DEFAULT 'false',\n");
        query.append("    ALTER COLUMN isRemovable SET NOT NULL, ALTER COLUMN isRemovable SET DEFAULT 'true',\n");
        query.append("    ALTER COLUMN isCustomButton SET NOT NULL, ALTER COLUMN isCustomButton SET DEFAULT 'false';\n");
        return query.toString();
    }

    public static String getQualifiedName(Table table, String defaultCatalog, String defaultSchema) {
        if (table.getSubselect() != null) {
            return "( " + table.getSubselect() + " )";
        } else {
            String usedSchema = table.getSchema() == null ? defaultSchema : table.getSchema();
            String usedCatalog = table.getCatalog() == null ? defaultCatalog : table.getCatalog();
            return qualify(usedCatalog, usedSchema, table.getName());
        }
    }

    public static String getQuotedName(Dialect dialect, String name) {
        return dialect.openQuote() + name + dialect.closeQuote();
    }

    public static String qualify(String catalog, String schema, String table) {
        StringBuilder qualifiedName = new StringBuilder();
        if (catalog != null) {
            qualifiedName.append(catalog).append('.');
        }

        if (schema != null) {
            qualifiedName.append(schema).append('.');
        }

        return qualifiedName.append(table).toString();
    }

    public static void updateTemplateSchemaDates(String schema, String cluster, Integer interval) {
        log.info("Starting schema: - " + schema + " date update");
        String slashedName = "\"" + schema + "\"";

//        SchemaUpdate schemaUpdate = new SchemaUpdate(getConfiguration(slashedName), cluster);
//        schemaUpdate.executeDateUpdateByIntervalUpdate(interval);

        log.info("Execution [Date Update] Completed for schema: - " + schema);
    }

    public static Properties getProperties(String slashedName) {
        Properties properties = new Properties();
        if (dbUrl == null) {
            dbUrl = props.getProperty("tenant.datasource.master.FREE.url");
        }
        properties.setProperty(AvailableSettings.DEFAULT_SCHEMA, slashedName);
        properties.setProperty(AvailableSettings.URL, dbUrl);
        properties.setProperty(AvailableSettings.USER, props.getProperty("tenant.datasource.username"));
        properties.setProperty(AvailableSettings.PASS, props.getProperty("tenant.datasource.password"));
        properties.setProperty(AvailableSettings.DRIVER, "org.postgresql.Driver");
        properties.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.setProperty(AvailableSettings.ISOLATION, "1");
        properties.setProperty(AvailableSettings.TRANSACTION_COORDINATOR_STRATEGY, "jdbc");
        properties.setProperty(AvailableSettings.FLUSH_BEFORE_COMPLETION, "true");
        properties.setProperty(AvailableSettings.AUTO_CLOSE_SESSION, "true");
        properties.setProperty(AvailableSettings.USE_GET_GENERATED_KEYS, "true");
        properties.setProperty(AvailableSettings.GENERATE_STATISTICS, "false");
        properties.setProperty(AvailableSettings.STATEMENT_BATCH_SIZE, "100");
        properties.setProperty(AvailableSettings.STATEMENT_FETCH_SIZE, "100");
        properties.setProperty(AvailableSettings.CFG_XML_FILE, "hibernate.cfg.xml");
        properties.setProperty(AvailableSettings.UNIQUE_CONSTRAINT_SCHEMA_UPDATE_STRATEGY, "SKIP");
        properties.setProperty(AvailableSettings.SHOW_SQL, "false");
        properties.setProperty(AvailableSettings.FORMAT_SQL, "false");
        properties.setProperty(AvailableSettings.C3P0_MIN_SIZE, "10");
        properties.setProperty(AvailableSettings.C3P0_MAX_SIZE, "30");
        properties.setProperty(AvailableSettings.C3P0_TIMEOUT, "1000");
        properties.setProperty(AvailableSettings.C3P0_MAX_STATEMENTS, "200");

        return properties;
    }

    /**
     * In some reason the SchemaUpdate class did not update schema in particular for
     * EdsTransaction d.o. Sequence Generator(even with hibernate.id.new_generator_mappings = true)
     * And I am going to fix it as A quick temp fix!!!!
     */
    public static void manualUpdateWhatWasNotUpdatedByCallingMethod(String slashedName) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String checkSql_transaction = "SELECT * FROM " + slashedName + ".transaction_id_man_seq";
        try (Connection conn = WfmGetConnection.getConnection(getClusterUrl("free"))) {
            ResultSet rs = null;
            try (PreparedStatement ps = conn.prepareStatement(checkSql_transaction)) {   //if there is no any sequence(the schema update did not create it in some reason) called transaction_id_man_seq, Then create one
                rs = ps.executeQuery();
                rs.next();  //check if exists
            } catch (SQLException e) {
                createTransactionSecuence(conn, slashedName, "transaction", "transaction_id_man_seq");
            } finally {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            }
            alterSequence(conn, slashedName, "transaction", "transaction_id_man_seq");//update the sequence and column
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        stopWatch.stop();
        System.out.println("manualUpdateWhatWasNotUpdatedByCallingMethod s = " + stopWatch.getTotalTimeMillis());
    }

    private static void createTransactionSecuence(Connection conn, String slashedName, String tableName, String sequenceName) throws SQLException {
        long lastID = 1;
        try {
            StringBuilder query = new StringBuilder();
            query.append("select max(id) from ").append(slashedName).append(".").append(tableName);
            PreparedStatement lastId = conn.prepareStatement(query.toString());
            if (lastId.executeQuery().next()) {
                lastID = lastId.getResultSet().getLong(1) + 1;
            }
        } catch (SQLException e) {
            lastID = 1;
        }
        String createSequence = "CREATE SEQUENCE " + slashedName + "." + sequenceName + "\n" +
                "  INCREMENT 1\n" +
                "  MINVALUE 1\n" +
                "  MAXVALUE 9223372036854775807\n" +
                "  START " + lastID + "\n" +
                "  CACHE 1;";

        try (PreparedStatement createSqStatement = conn.prepareStatement(createSequence)) {
            createSqStatement.execute();
        }
    }

    private static void alterSequence(Connection conn, String slashedName, String tableName, String sequenceName)
            throws SQLException {
        String alterRole = "ALTER TABLE " + slashedName + "." + sequenceName + " OWNER TO " + getDBUserName() + ";";
        String assignToId = "ALTER TABLE " + slashedName + "." + tableName
                + " ALTER COLUMN id SET DEFAULT nextval('" + slashedName + "." + sequenceName + "'::regclass);";
        try (PreparedStatement alterRoleStatement = conn.prepareStatement(alterRole)) {
            alterRoleStatement.execute();
        }
        try (PreparedStatement assignToIdStatement = conn.prepareStatement(assignToId)) {
            assignToIdStatement.execute();
        }
    }

    public static void createSchema(String name, DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "CREATE SCHEMA \"" + name + "\" AUTHORIZATION " + getDBUserName() + ";")) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSchemaList() throws SQLException {
        List<String> schemaList = new ArrayList<>();

        try (Connection conc = WfmGetConnection.getConnection(dbUrl)) {
            ResultSet rs = null;
            try (Statement st = conc.createStatement()) {
                rs = st.executeQuery("SELECT ns.nspname as id FROM pg_namespace ns  " +
                        " WHERE ns.nspname ~ '^[0-9]+$' or ns.nspname like 'template%'  or  ns.nspname = '0_template' " +
                        " or ns.nspname like 'gym%'  or  ns.nspname = '0_gym' " +
                        " ORDER BY ns.nspname DESC");
                while (rs.next()) {
                    String schema = rs.getString(1);
                    schemaList.add(schema);
                }
            } finally {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schemaList;
    }

    protected static void runHibernateSchemaUpdate(List<String> schemas, String cluster) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // Read database
        String previousdbUrl = dbUrl;
        dbUrl = getClusterUrl(cluster);

        // Run schema updating
        process(schemas, cluster);

        dbUrl = previousdbUrl;

        stopWatch.stop();
        log("SchemaUpdate elapsed: " + stopWatch.getTotalTimeMillis());
    }

    /*protected static void runSqlPatcherForSchemaUpdate(String defaultSchema, String cluster) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        exceptions.clear();

        StringWriter stringWriter = new StringWriter();
        List<String> statements = new ArrayList<>();

        String oldDbUrl = dbUrl; // take value
        dbUrl = getClusterUrl(cluster);

        String slashedSchema = "\"" + defaultSchema + "\"";

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .applySettings(getProperties(slashedSchema))
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Metadata metadata = metadataSources.buildMetadata();

        Map config = new HashMap(serviceRegistry.getService(ConfigurationService.class).getSettings());

        final SchemaManagementTool tool = serviceRegistry.getService(SchemaManagementTool.class);

        final ExceptionHandlerCollectingImpl exceptionHandler = new ExceptionHandlerCollectingImpl();

        final ExecutionOptions executionOptions = SchemaManagementToolCoordinator.buildExecutionOptions(
                config,
                exceptionHandler
        );

        final TargetDescriptor targetDescriptor = new TargetDescriptor() {
            @Override
            public EnumSet<TargetType> getTargetTypes() {
                return EnumSet.of(TargetType.SCRIPT);
            }

            @Override
            public ScriptTargetOutput getScriptTargetOutput() {
                return new ScriptTargetOutputToWriter(stringWriter);
            }
        };

        try {
            tool.getSchemaMigrator(config).doMigration(metadata, executionOptions, targetDescriptor);
        } finally {
            if (exceptionHandler instanceof ExceptionHandlerCollectingImpl) {
                exceptions.addAll(exceptionHandler.getExceptions());
            }
        }


//        SchemaUpdate schemaUpdate = new SchemaUpdate(getConfiguration(defaultSchema), cluster);
        String sqlString = stringWriter.toString();
        List<String> list = StringUtils.isNotBlank(sqlString) ? Arrays.asList(sqlString.split(";")) : null;
        for (String sql : list) {
            sql = sql.replace(slashedSchema, "\"anv\"") + ";";

            // 1. CREATE TABLE IF NOT EXISTS
            sql = sql.replace("create table", "create table if not exists");

            // 2. ADD COLUMN IF NOT EXISTS
            sql = sql.replace("add column", "add column if not exists");

            // 3. ADD CONSTRAINT ....
            if (sql.indexOf("add constraint") > 0) {
                StringBuilder sb = new StringBuilder("DO $$ BEGIN BEGIN ");
                sb.append(sql);
                sb.append(";EXCEPTION WHEN others THEN RAISE NOTICE 'NOOP'; END;END $$; ");
                sql = sb.toString();
            }

            // 4. CREATE INDEX IF NOT EXISTS ....
            sql = sql.replace("create index", "create index if not exists");

            statements.add(sql);
        }

        log.debug(statements);

        SqlScriptExecutor executor = new SqlScriptExecutor(props);
        ScriptItem scriptItem = new ScriptItem(SqlScriptExecutor.SCHEMA_UPDATE);
        scriptItem.setStatements(statements);
        scriptItem.setClusterType(cluster);
        executor.execute(scriptItem);
        executor.stop();

        dbUrl = oldDbUrl; // put back value

        stopWatch.stop();
        log(ConsoleColors.BLUE, "SchemaUpdate elapsed: " + stopWatch.getTotalTimeMillis());
    }*/

    public static void main(String[] args) {
        /**
         * Configuration logger to update schema logs
         */
        configureLogger();

        if (args.length == 0) {
            log.error("Arguments must be specified in a form of '1, 3737, ..., n' or 'all' ");
            System.exit(0);
        }
        /**
         * Read a root patch direcotry
         */
        patchDirectory = readPatchDirectory();

        /**
         * This one is a timer, that will calculate how much time the process took to finish
         */
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        /**
         * List clusters e.x[FREE, PAID, PAID1,...]
         */
        List<String> clusterList = new Gson().fromJson(props.getProperty("tenant.datasource.cluster.update", ""), List.class);

        /**
         * Fist of all run the GlobalAuth's sql patches before update schemas
         */
        runGlobalSQLPatcher();

        /**
         * Run sql patches were marked as BEFORE_UPDATE to the each of clusters' schemas
         * these kind of patched must be run before updating schemas{Sample: 20170121101112__author__some-description__BEFOREUPDATE.sql}
         */
        for (String cluster : clusterList) {
            runSqlPatcher(SqlScriptExecutor.BEFORE_SCHEMA_UPDATE, cluster, args);
        }

        // Choose one of ways: schema update via SqlPatcher or Hibernate
        boolean forAllSchema = Arrays.stream(args).anyMatch("all"::equalsIgnoreCase);

        for (String cluster : clusterList) {
            log(ConsoleColors.GREEN_BRIGHT, "SCHEMA_UPDATE for " + cluster + " is started by generated SQL statements...");
            if (forAllSchema) {
//                List<String> schemaList = null;
//                try {
                    setDbUrl(getClusterUrl(cluster));
//                    schemaList = getSchemaList();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
                runSqlPatcherForSchemaUpdate(getDefaultSchema(args), cluster);
            } else {
//                log(ConsoleColors.GREEN_BRIGHT, "SCHEMA_UPDATE for " + cluster + " is started by Hibernate default statements...");
                runHibernateSchemaUpdate(Arrays.asList(args), cluster);
            }
            runSqlPatcher(SqlScriptExecutor.AFTER_SCHEMA_UPDATE, cluster, args);
        }

        stopWatch.stop();
        System.out.println("=== SchemaUpdate: failed schemas: " + errorCompaniesList);
        System.out.println("=== Failed patches: TODO [filename] ==> failed schemas");//TODO
        System.out.println("=== Overall execution time: " + stopWatch.getTotalTimeMillis());

        UpdateLogger.conclude();
        SqlScriptExecutor.shutdownExecutorService();
    }

    public static void runSqlPatcherForSchemaUpdate(String schemaName, String cluster) {
        String previousdbUrl = dbUrl;
        dbUrl = getClusterUrl(cluster);

        // Run schema updating
        log.info("Starting schema: - " + schemaName + " update");
        String slashedSchema = "\"" + schemaName + "\"";

        exceptions.clear();

        StringWriter stringWriter = new StringWriter();
        List<String> statements = new ArrayList<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("metadata");
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .applySettings(getProperties(slashedSchema))
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Metadata metadata = metadataSources.buildMetadata();
        stopWatch.stop();
        log.info(stopWatch.getLastTaskName() + " took: " + stopWatch.getLastTaskTimeMillis() + "ms");

        Map config = new HashMap(serviceRegistry.getService(ConfigurationService.class).getSettings());

        final SchemaManagementTool tool = serviceRegistry.getService(SchemaManagementTool.class);

        final ExceptionHandler exceptionHandler = haltOnError
                ? ExceptionHandlerHaltImpl.INSTANCE
                : new ExceptionHandlerCollectingImpl();

        final ExecutionOptions executionOptions = SchemaManagementToolCoordinator.buildExecutionOptions(
                config,
                exceptionHandler
        );

        final TargetDescriptor targetDescriptor = new TargetDescriptor() {
            @Override
            public EnumSet<TargetType> getTargetTypes() {
                return EnumSet.of(TargetType.SCRIPT);
            }

            @Override
            public ScriptTargetOutput getScriptTargetOutput() {
                return new ScriptTargetOutputToWriter(stringWriter);
            }
        };

        try {
            stopWatch.start("migration");

            tool.getSchemaMigrator(config).doMigration(metadata, executionOptions, targetDescriptor);
            String sqlString = stringWriter.toString();

            stopWatch.stop();
            log.info(stopWatch.getLastTaskName() + " took: " + stopWatch.getLastTaskTimeMillis() + "ms");

            List<String> list = StringUtils.isNotBlank(sqlString) ? Arrays.asList(sqlString.split(";")) : null;
            for (String sql : list) {
                sql = sql.replace(slashedSchema, "\"anv\"") + ";";

                // 1. CREATE TABLE IF NOT EXISTS
                sql = sql.replace("create table", "create table if not exists");

                // 2. ADD COLUMN IF NOT EXISTS
                sql = sql.replace("add column", "add column if not exists");

                // 3. ADD CONSTRAINT ....
                if (sql.indexOf("add constraint") > 0) {
                    StringBuilder sb = new StringBuilder("DO $$ BEGIN BEGIN ");
                    sb.append(sql);
                    sb.append("EXCEPTION WHEN others THEN RAISE NOTICE 'NOOP'; END;END $$;");
                    sql = sb.toString();
                }

                // 4. CREATE INDEX IF NOT EXISTS ....
                sql = sql.replace("create index", "create index if not exists");

                statements.add(sql);
            }
        } finally {
            if (exceptionHandler instanceof ExceptionHandlerCollectingImpl) {
                exceptions.addAll(((ExceptionHandlerCollectingImpl) exceptionHandler).getExceptions());
            }
            serviceRegistry.close();
        }

//        log.debug(statements);
        log.info("scripts count: " + statements.size());


        try {
            List<String> schemaList = getSchemaList();
            final boolean[] patchForPublic = {true};
            schemaList.forEach(schema -> {
                SqlScriptExecutor executor = new SqlScriptExecutor(props);
                ScriptItem scriptItem = new ScriptItem(SqlScriptExecutor.SCHEMA_UPDATE);
                scriptItem.setSchema(schema);
                scriptItem.setStatements(statements);
                scriptItem.setClusterType(cluster);
                executor.execute(scriptItem, patchForPublic[0]);
                patchForPublic[0] = false;
//                manualUpdateWhatWasNotUpdatedByCallingMethod(slashedSchema);
                executor.stop();

            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        log.info("All " + stopWatch.getTaskCount() + " tasks took: " + stopWatch.getTotalTimeMillis() + "ms");

        log.debug("Execution Completed for schema: - " + schemaName);

        dbUrl = previousdbUrl;

    }

    /**
     * Run SqlPatcher, per database: globalauth, multischemafree
     *
     * @param runMode      SqlScriptExecutor.BEFORE_SCHEMA_UPDATE / .AFTER_SCHEMA_UPDATE
     * @param clusterType_
     */
    protected static void runGlobalSQLPatcher() {
        String path = SqlScriptExecutor.getOS() == SqlScriptExecutor.OS_WIN ? patchDirectory + "\\globalauth" : patchDirectory + "/globalauth";
        props.setProperty("patch.directory.globalauth", path);
        SqlScriptExecutor executor = new SqlScriptExecutor(props);
        executor.execute(SqlScriptExecutor.DB_ID_GLOBALAUTH, null);
        executor.stop();
    }

    protected static void runSqlPatcher(short runMode, String clusterType_, String[] args) {
        String path = SqlScriptExecutor.getOS() == SqlScriptExecutor.OS_WIN ? patchDirectory + "\\multischema" : patchDirectory + "/multischema";
        props.setProperty("patch.directory.multischema", path);
        SqlScriptExecutor executor = new SqlScriptExecutor(props, runMode);
        executor.execute(clusterType_, args);
        executor.stop();
    }

    protected static String readPatchDirectory() {
        // Get where the "Updater" runs
        // If it is local developer machine ("out/production/web") take "../../web/docs/DatabasePatches"
        // or give path choose dialog

        final File f = new File(EdsSchemaUpdater.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String runPath = f.getPath();

        String tail;
        String result;
        final int os = SqlScriptExecutor.getOS();
        if (os == SqlScriptExecutor.OS_WIN) {
            tail = "\\out\\production\\web";
            Path path = Paths.get("docs\\DatabasePatches");
            result = path.toAbsolutePath().toString();
        } else {
            tail = "/out/production/web";
            result = "docs/DatabasePatches";
        }

        if (runPath.toLowerCase().endsWith(tail.toLowerCase())) { // bad checking
            // Developer Workspace mode
            UpdateLogger.log(ConsoleColors.YELLOW_BRIGHT, "Developer Workspace found");
            UpdateLogger.log(ConsoleColors.GREEN, "Patch Root Direcotry: ", runPath);
            if (SqlScriptExecutor.getOS() == SqlScriptExecutor.OS_UNIX) {
                return Paths.get("web/docs/DatabasePatches").toAbsolutePath().toString();
            } else {
                return result;
            }
        } else {
            // Production Workspace mode
            UpdateLogger.log(ConsoleColors.YELLOW_BRIGHT, "Production Workspace found");
            final String productionPatchDir = "/mnt/webapps/projects/app.workforcetrack.com/ROOT/WEB-INF/classes/DatabasePatches";
            result = props.getProperty(SqlScriptExecutor.PROPERTY_PRODUCTION_PATCH_DIRECTORY, productionPatchDir);
            UpdateLogger.log(ConsoleColors.GREEN, "Patch Root Direcotry: ", result);
        }
        return result;
    }

    public static void process(List<String> schemas, String cluster) {
        for (final String schema : schemas) {
            try {
                doneList.add(schema);
                updateSchema(schema, cluster);
                doneList.remove(schema);
                doneSchema();
            } catch (Throwable e) {
                doneList.remove(schema);
                errorCompaniesList.put(schema, e.getStackTrace());
                e.printStackTrace();
                doneSchema();
            }
        }
    }

    protected static void doneSchema() {
        if (doneList.isEmpty()) {
            System.out.println("___________________________________________________________");
            System.out.println("Finished all threads");
            System.out.println("Failed companies count = " + errorCompaniesList.size());
            for (Map.Entry<String, StackTraceElement[]> entry : errorCompaniesList.entrySet()) {
                System.out.println("Schema name is " + entry.getKey() + "  ");
                System.out.println("____________________");
            }
        }

    }

    public static void setDbUrl(String dbUrl) {
        EdsSchemaUpdater.dbUrl = dbUrl;
    }

    public static void setHaltOnError(boolean haltOnError) {
        EdsSchemaUpdater.haltOnError = haltOnError;
    }

    public static String getDBUserName() {
        return props.getProperty("tenant.datasource.username");

    }

    public static String getDBUserPassword() {
        return props.getProperty("tenant.datasource.password");

    }

    public static String getDefaultSchema(String[] args) {
        log(ConsoleColors.PURPLE, "Extracting Default (ZERO) schema from args[]");
        Options options = new Options();
        Option output = new Option("d", "default", true, "default schema");
        output.setRequired(false);
        options.addOption(output);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        String defaultSchema = null;
        try {
            cmd = parser.parse(options, args);
            defaultSchema = cmd.getOptionValue("default");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("EdsSchemaUpdater", options);
        }
        if (defaultSchema == null) {
            defaultSchema = "0";
        }
        log(ConsoleColors.BLUE, "DEFAULT SCHEMA: " + defaultSchema);
        return defaultSchema;
    }

    public static String getClusterUrl(String cluster) {
        if (StringUtils.isEmpty(cluster)) {
            return props.getProperty("tenant.datasource.master.FREE.url");
        }
        cluster = cluster.toLowerCase();
        return switch (cluster) {
            case "paid" -> props.getProperty("tenant.datasource.master.PAID.url");
            case "paid1" -> props.getProperty("tenant.datasource.master.PAID1.url");
            default -> props.getProperty("tenant.datasource.master.FREE.url");
        };
    }

    static void configureLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        try (InputStream configStream = FileUtils.openInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("logback-update-schema.xml").toURI()))) {
            configurator.setContext(loggerContext);
            configurator.doConfigure(configStream); // loads logback file
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (JoranException e) {
            throw new RuntimeException(e);
        }
    }
}
