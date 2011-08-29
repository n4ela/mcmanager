package mcmanager.test.utils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.unitils.core.TestListener;
import org.unitils.dbunit.DbUnitModule;
import org.unitils.dbunit.util.DbUnitDatabaseConnection;

public class InMemoryDbUnitModule extends DbUnitModule {
    private static final String DATABASE_SCHEMA_NAME = "database.schemaNames";
    private static final String DEFAULT_SCHEMA_NAME = "PUBLIC";
    private static final String SCHEMA_DEFAULT_FILENAME = "create_database.sql";
    private static final String SCHEMA_FILENAME = "com.adeptechllc.dbunit.schemaFileName";
    private boolean isInitialized = false;

    /**
     * @return The TestListener object that implements Unitils' DbUnit support
     */

    @Override
    public TestListener getTestListener() {
        return new DbUnitListener();
    }

    public void initializeSchema() {
        try {
            String schemaName = configuration.getProperty(DATABASE_SCHEMA_NAME, DEFAULT_SCHEMA_NAME);

            DbUnitDatabaseConnection connection = getDbUnitDatabaseConnection(schemaName);
            Statement statement = connection.getConnection().createStatement();
//            for (String tableString : getTableStatements())
//            {
//                System.out.println(tableString);
//                statement.executeUpdate(tableString);
//                System.out.println("VSTAVIL");
//            }
            connection.close();
            isInitialized = true;
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    private List<String> getTableStatements() {
        List<String> results = new ArrayList<String>();

        try {
            String allTables = readFileAsString();
            StringTokenizer st = new StringTokenizer(allTables, ";", false);

            while (st.hasMoreTokens()) {
                results.add(st.nextToken());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        return results;
    }

    private String readFileAsString() throws IOException {
        InputStream input;
        String filename = configuration.getProperty(SCHEMA_FILENAME, SCHEMA_DEFAULT_FILENAME);

        input = getClass().getClassLoader().getResourceAsStream(filename);

        if (input == null) {
            System.err.println("schema filename " + filename + " not found: using default");
            input = getClass().getClassLoader().getResourceAsStream(SCHEMA_DEFAULT_FILENAME);
        }

        //		BufferedReader bufferedReader =
        //			new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE EMPLOYEES (id varchar(10));");

        //		String nextLine = bufferedReader.readLine();
        //		while (nextLine != null) {
        //			stringBuilder.append(nextLine.replaceAll("\t", ""));
        //			nextLine = bufferedReader.readLine();
        //		}
        return stringBuilder.toString();
    }

    /**
     * 
     * Test listener that is called while the test framework is running tests
     */

    protected class DbUnitListener extends org.unitils.dbunit.DbUnitModule.DbUnitListener {

        @Override
        public void beforeTestSetUp(Object testObject, Method testMethod) {
            if (!isInitialized) {
                initializeSchema();
            }
            super.beforeTestSetUp(testObject, testMethod);
        }
    }
}
