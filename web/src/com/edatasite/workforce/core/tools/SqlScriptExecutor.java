package com.edatasite.workforce.core.tools;


import com.edatasite.workforce.core.UpdateLogger;
import com.edatasite.workforce.core.tools.sqlpatcher.RunnableSqlScript;
import com.edatasite.workforce.core.tools.sqlpatcher.ScriptItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * SqlScriptExecutor - executes sql statements by file contents.
 * Formats different schema names for KPI
 * "0", "public"|none ==> public, "anv" ==> all companies
 */
public class SqlScriptExecutor {
  private static final Logger log = LoggerFactory.getLogger(SqlScriptExecutor.class);

  public static final int OS_UNKNOWN = 0;
  public static final int OS_WIN = 1;
  public static final int OS_MAC = 2;
  public static final int OS_UNIX = 3;

  //Such Called Modes
  public static final short BEFORE_SCHEMA_UPDATE = 1;//Scripts which must be run before the schema structure update
  public static final short AFTER_SCHEMA_UPDATE = 2;//Scripts which must be run after the schema structure update
  public static final short SCHEMA_UPDATE = 3; // this mode should not version in "schema_version" table

  private static final int DATABASE_ID_MULTISCHEMA_FREE = 1;
  private static final int DATABASE_ID_MULTISCHEMA_PAID = 2;
  private static final int DATABASE_ID_MULTISCHEMA_PAID1 = 3;
  private static final int DATABASE_ID_GLOBALAUTH = 4;

  public static final String DB_ID_FREE = "free";
  public static final String DB_ID_GLOBALAUTH = "globalauth";
  public static final String PUBLIC_SCHEMA = "public";

  public static final String PROPERTY_HIBERNATE_FREE_JDBC_URL = "tenant.datasource.master.FREE.url";
  public static final String PROPERTY_HIBERNATE_PAID1_JDBC_URL = "tenant.datasource.master.PAID1.url";
  public static final String PROPERTY_HIBERNATE_GLOBALAUTH_JDBC_URL = "globalauth.datasource.url";

  public static final String PROPERTY_HIBERNATE_JDBC_USERNAME = "tenant.datasource.username";
  public static final String PROPERTY_HIBERNATE_JDBC_PASSWD = "tenant.datasource.pass" + "word";

  public static final String PROPERTY_HIBERNATE_GLOBALAUTH_JDBC_USERNAME = "globalauth.datasource.username";
  public static final String PROPERTY_HIBERNATE_GLOBALAUTH_JDBC_PASSWD = "globalauth.datasource.pass" + "word";

  public static final String PROPERTY_PATCH_DIRECTORY_MULTISCHEMA = "patch.directory.multischema";
  public static final String PROPERTY_PATCH_DIRECTORY_GLOBALAUTH = "patch.directory.globalauth";
  public static final String PROPERTY_PRODUCTION_PATCH_DIRECTORY = "sqlpatch.production.path";

  public static final String SCRIPT_DELIMITER = "__"; // double underscore
  public static final String PATCH_FILE_EXTENSION = "sql";
  public static final String SCRIPT_RESULT_SUCCESS_MSG = "SUCCESS";
  public static final String SCRIPT_RESULT_FAIL_MSG = "FAIL";
  public static final int RESULT_SUCCESS = 0;
  public static final int RESULT_FAIL = 1;

  protected Properties properties;
  protected String jdbcUrl;
  private String username;
  private String password;
  private Connection connection;
  protected Connection logConnection;
  protected String patchDir;
  private static int currentOS = OS_UNKNOWN;
  protected short runMode;
  protected boolean initialized;

  private static CompletionService<Void> completionService;
  private static ExecutorService exec;

  static {
    String OS = System.getProperty("os.name").toLowerCase();
    if (OS.indexOf("win") >= 0) {
      currentOS = OS_WIN;
    } else if (OS.indexOf("mac") >= 0) {
      currentOS = OS_MAC;
    } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") >= 0) {
      currentOS = OS_UNIX;
    } else {
      currentOS = OS_UNKNOWN;
    }

    exec = new ThreadPoolExecutor(1, 10,
            30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
    completionService = new ExecutorCompletionService<>(exec);
  }


  public static int getOS() {
    return currentOS;
  }

  public SqlScriptExecutor(Properties properties) {
    this.properties = properties;
  }

  public SqlScriptExecutor(Properties properties, short runMode) {
    this.properties = properties;
    this.runMode = runMode;
  }

  /**
   * Initialize connections to given cluster
   *
   * @param clusterTypeOrDatabaseName
   */
  protected void init(String clusterTypeOrDatabaseName) {
    if (clusterTypeOrDatabaseName != null) {
      clusterTypeOrDatabaseName = clusterTypeOrDatabaseName.toLowerCase().trim();
    }

    switch (clusterTypeOrDatabaseName) {
      case DB_ID_GLOBALAUTH -> {
        // GLOBALAUTH
        jdbcUrl = properties.getProperty(PROPERTY_HIBERNATE_GLOBALAUTH_JDBC_URL);
        username = properties.getProperty(PROPERTY_HIBERNATE_GLOBALAUTH_JDBC_USERNAME);
        password = properties.getProperty(PROPERTY_HIBERNATE_GLOBALAUTH_JDBC_PASSWD);
        patchDir = properties.getProperty(PROPERTY_PATCH_DIRECTORY_GLOBALAUTH);
      }
      case DB_ID_FREE -> {
        // FREE
        jdbcUrl = properties.getProperty(PROPERTY_HIBERNATE_FREE_JDBC_URL);
        username = properties.getProperty(PROPERTY_HIBERNATE_JDBC_USERNAME);
        password = properties.getProperty(PROPERTY_HIBERNATE_JDBC_PASSWD);
        patchDir = properties.getProperty(PROPERTY_PATCH_DIRECTORY_MULTISCHEMA);
      }
      default -> {
        // PAID, PAID1, PAID2, ....
        // get NUMBER
        String number = clusterTypeOrDatabaseName.replace("paid", ""); // paid1 ==> 1
        int num = 0;
        if (!number.isEmpty()) {
          try {
            num = Integer.parseInt(number);
          } catch (NumberFormatException e) {
            log.error("Unknown clusterType: " + clusterTypeOrDatabaseName);
            return;
          }
        }

        String urlProperty = "tenant.datasource.master.PAID.url";
        if (num > 0) {
          // PAID1, ...
          urlProperty = "tenant.datasource.master.PAID" + num + ".url";
        }
        jdbcUrl = properties.getProperty(urlProperty);
        username = properties.getProperty(PROPERTY_HIBERNATE_JDBC_USERNAME);
        password = properties.getProperty(PROPERTY_HIBERNATE_JDBC_PASSWD);
        patchDir = properties.getProperty(PROPERTY_PATCH_DIRECTORY_MULTISCHEMA);
      }
    }

    try {
      connection = DriverManager.getConnection(jdbcUrl, username, password);
      connection.setAutoCommit(false);

      logConnection = DriverManager.getConnection(jdbcUrl, username, password);

      initialized = true;
    } catch (SQLException e) {
      UpdateLogger.log(ConsoleColors.YELLOW, jdbcUrl, ConsoleColors.RED, " connection refused!");
      log.error("Connection error. ", e);
      initialized = false;
    } catch (Exception e) {
      UpdateLogger.log(ConsoleColors.YELLOW, jdbcUrl, ConsoleColors.RED, " connection refused!");
      log.error("Error: ", e);
      initialized = false;
    }

  }

  /**
   * Runs Sql Scripts to the given Cluster
   *
   * @param clusterType_
   * @param args
   */
  public void execute(String clusterType_, String[] args) {
    UpdateLogger.log(ConsoleColors.PURPLE, "Running Sql Patches for ", ConsoleColors.GREEN_BRIGHT, (DB_ID_GLOBALAUTH.equalsIgnoreCase(clusterType_) ? "GLOBAL_AUTH" : clusterType_ + (runMode == BEFORE_SCHEMA_UPDATE ? " BEFORE_UPDATE" : " AFTER_UPDATE")));

    // initialize connection to given cluster
    init(clusterType_);

    if (initialized) {
      List<ScriptItem> allScriptList = listScripts();

      //run scripts to public schema of predefined cluster
      RunnableSqlScript sqlScript = new RunnableSqlScript(connection, logConnection, getNotExecutedScripts(clusterType_, PUBLIC_SCHEMA, allScriptList), PUBLIC_SCHEMA, false);
      sqlScript.run();

      /*

       * run scripts to PRIVATE SCHEMA of predefined cluster
       */
      if (!DB_ID_GLOBALAUTH.equalsIgnoreCase(clusterType_)) {
        boolean forAllSchema = Arrays.stream(args).anyMatch("all"::equalsIgnoreCase);
        List<String> schemaList = forAllSchema ? getPrivateSchemas() : Arrays.asList(args);

        for (String schema : schemaList) {
          completionService.submit(new RunnableSqlScript(connection, logConnection, getNotExecutedScripts(clusterType_, schema, allScriptList), schema, false));
        }

        wait_till_to_complete_all_submitted_tasks:
        for (int task = 0; task < schemaList.size(); task++) {
          try {
            completionService.take();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
    UpdateLogger.log(ConsoleColors.GREEN, "FINESHED Sql Patches for ", ConsoleColors.GREEN_BRIGHT, (DB_ID_GLOBALAUTH.equalsIgnoreCase(clusterType_) ? "GLOBAL_AUTH" : clusterType_ + (runMode == BEFORE_SCHEMA_UPDATE ? " BEFORE_UPDATE" : " AFTER_UPDATE")));
  }

  /**
   * Only used for SchemaUpdate with SchemaUpdate generated SQL statements
   *
   * @param scriptItem
   * @return
   */
  public void execute(ScriptItem scriptItem, boolean patchForPublic) {

    // Run for selected clusterType
    String clusterType = scriptItem.getClusterType();
    init(clusterType);

    if (initialized) {
      scriptItem.setClusterType(clusterType);
      //run SCHEMA UPDATE scripts for public schema of the predefined cluster
      if (patchForPublic) {
        RunnableSqlScript sqlScript = new RunnableSqlScript(connection, logConnection, List.of(scriptItem), PUBLIC_SCHEMA, true);
        sqlScript.run();
      }

      //run SCHEMA UPDATE scripts for PRIVATE SCHEMA of the predefined cluster
      RunnableSqlScript privateSqlScript = new RunnableSqlScript(connection, logConnection, List.of(scriptItem), scriptItem.getSchema(), true);
      privateSqlScript.run();
    }
  }

  public static void shutdownExecutorService() {
    exec.shutdown();
  }

  public void stop() {
    // close mainConnection
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      log.warn("Connection close failed. " + e.toString());
    }

    // close logConnection
    try {
      if (logConnection != null) {
        logConnection.close();
      }
    } catch (SQLException e) {
      log.warn("LogConnection close failed. " + e.toString());
    }
  }

  /**
   * Lists new(not exeuted) scripts by given cluster and schema
   *
   * @param clusterType_
   * @param schema
   * @return
   */
  List<ScriptItem> getNotExecutedScripts(String clusterType_, String schema, List<ScriptItem> allScripts) {
    List<String> executedScripts = getExecutedScriptFilenames(schema);
    List<ScriptItem> notExecutedScripts = allScripts.stream().filter(item -> !executedScripts.contains(item.getFilename()))
            .peek(item -> item.setClusterType(clusterType_))
            .collect(Collectors.toList());
    return notExecutedScripts;
  }

  /**
   * Lists executed scripts by given schema
   *
   * @param schema
   * @return
   */
  List<String> getExecutedScriptFilenames(String schema) {
    return getExecutedScriptFilenames(schema, 0);
  }

  List<String> getExecutedScriptFilenames(String schema, int attempt) {
    List<String> result = new ArrayList<>();
    String sql = "SELECT script_name FROM \"" + schema + "\".schema_version " + (!jdbcUrl.contains(DB_ID_GLOBALAUTH) ? "WHERE run_mode=?" : "") + " ORDER BY version_id DESC";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {

      if (!jdbcUrl.contains(DB_ID_GLOBALAUTH)) {
        stmt.setShort(1, runMode);
      }
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          result.add(rs.getString("script_name"));
        }
      }
      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
      if (attempt < 3) {
        try {
          TimeUnit.MILLISECONDS.sleep(100L * (attempt + 1));
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
        return getExecutedScriptFilenames(schema, ++attempt);
      }
    }
    return result;
  }

  /**
   * Lists all the private schemas in the cluster(which was difined in the init method)
   *
   * @return
   */
  List<String> getPrivateSchemas() {
    List<String> result = new ArrayList<>();
    final String sql = "SELECT ns.nspname FROM pg_namespace ns WHERE nspname ~ '^[0-9]+$' " +
            "or ns.nspname like 'template_%'" +
            "or ns.nspname = '0_template'" +
            "or ns.nspname like 'gym_%'" +
            "or ns.nspname = '0_gym'" +
            "ORDER BY nspname";
    try (Statement stmt = connection.createStatement()) {
      try (ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
          result.add(rs.getString("nspname"));
        }
      }
      connection.commit();
    } catch (SQLException e) {
      log.error("FAILED: get-private-schemas ", e);
      try {
        connection.rollback();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }

  /**
   * All scripts from HD
   *
   * @return
   */
  List<ScriptItem> listScripts() {
    List<File> list = new ArrayList<>(getFilesList());

    list.sort((o1, o2) -> {
        String a = o1.getPath();
        String b = o2.getPath();
        return a.compareToIgnoreCase(b);
    });

    List<ScriptItem> result = new ArrayList<>();
    for (Iterator<File> iter = list.iterator(); iter.hasNext(); ) {
      File file = iter.next();
      String name = file.getName();
      String filename = file.getName();

      name = name.substring(0, name.lastIndexOf("."));

      // ==== PARSING FILENAME ====
      // Parse: version_id, author, description, file_name, BEFOREUPDATE/BEFORE_UPDATE/BEFORE
      // Sample: 20170121101112__author__some-description__BEFOREUPDATE.sql
      // Sample: 20170121121342__author__some-description.sql
      String[] parse = name.split(SCRIPT_DELIMITER);
      String versionStr = "", author = "", description = "";

      try {
        versionStr = parse[0];
        author = parse[1];
        description = parse[2];
      } catch (ArrayIndexOutOfBoundsException e) {
        log.warn("Splitting Filename:{}, exceiption:{}", name, e);
      }

      try {
        long v = Long.parseLong(versionStr);
        result.add(new ScriptItem(v, filename, author, description, file, runMode == BEFORE_SCHEMA_UPDATE ? BEFORE_SCHEMA_UPDATE : AFTER_SCHEMA_UPDATE));
      } catch (NumberFormatException e) {
        log.error("Parsing version of File:{}, RunMode: {}, exception: ", filename, runMode == BEFORE_SCHEMA_UPDATE ? "Before Update Schema" : "After Update Schema", e);
      }
    }
    return result;
  }

  /**
   * Lists script files from Hard Drive by filtered run-mode which was set up in the constructor
   *
   * @return
   */
  Collection<File> getFilesList() {
    String fileSuffix = "." + PATCH_FILE_EXTENSION;

    AndFileFilter fileFilter = new AndFileFilter();
    fileFilter.addFileFilter(new SuffixFileFilter(fileSuffix));

    if (!jdbcUrl.contains(DB_ID_GLOBALAUTH)) {

      if (runMode == BEFORE_SCHEMA_UPDATE) {
        fileFilter.addFileFilter(new WildcardFileFilter("*__BEFOREUPDATE*", IOCase.INSENSITIVE));
      } else {
        fileFilter.addFileFilter(new NotFileFilter(new WildcardFileFilter("*__BEFOREUPDATE*", IOCase.INSENSITIVE)));
      }
    }
    return FileUtils.listFiles(
            new File(patchDir),
            fileFilter,
            DirectoryFileFilter.INSTANCE
    );
  }
}


