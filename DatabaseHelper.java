
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package bus.ticketing.system;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

/**
 *
 * @author Harvey
 */
public class DatabaseHelper {
    public Connection        connection;
    public ResultSet         resultSet;
    public ResultSetMetaData metaData;
    public int               numberOfRows;
    public Statement         statement;
    public boolean           connectedToDatabase;
    public ResultSet         result;
    public ResultSetMetaData metadata;

    public DatabaseHelper() throws SQLException {
        this.numberOfRows        = 0;
        this.statement           = null;
        this.connectedToDatabase = false;
        this.resultSet           = null;
        this.metaData            = null;
        this.connection          = null;
        createDB();    // create the database if it does not exist

        // update database connection status
        connectedToDatabase = true;
    }

    public final void setQuery(String query) throws SQLException, IllegalStateException {

        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // specify query and execute it WATCH OUT: statement.executequery doesnot
//      run on queries that return number of rows such as inserts, so I use statement.execute()
        statement.execute(query);
        resultSet = statement.getResultSet();

        if (resultSet != null) {    // a resultset is null if it is an update, count, and I think insert

            // determine number of rows in ResultSet
            resultSet.last();                     // move to last row
            numberOfRows = resultSet.getRow();    // get row number
            metaData     = resultSet.getMetaData();
        } else {
            numberOfRows = 0;
        }
    }    // end method setQuery

    public int getColumnCount() throws IllegalStateException {

        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // determine number of columns
        try {
            return metaData.getColumnCount();
        }                                               // end try
                catch (SQLException sqlException) {}    // end catch

        return 0;    // if problems occur above, return 0 for number of columns
    }    // end method getColumnCount

    // get name of a particular column in ResultSet
    public String getColumnName(int column) throws IllegalStateException {

        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // determine column name
        try {
            return metaData.getColumnName(column + 1);
        }    // end try
                catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }    // end catch

        return "";    // if problems, return empty string for column name
    }    // end method getColumnName

    // return number of rows in ResultSet

    public int getRowCount() throws IllegalStateException {

        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        return numberOfRows;
    }    // end method getRowCount

    public Object getValueAt(int row, int column) throws IllegalStateException {

        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // obtain a value at specified ResultSet row and column
        try {
            resultSet.absolute(row + 1);

            return resultSet.getObject(column + 1);
        }    // end try
                catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }    // end catch

        return "";    // if problems, return empty string object
    }    // end method getValueAt

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void disconnectFromDatabase() {
        if (connectedToDatabase) {

//          close Statement and Connection
            try {
                resultSet.close();
                statement.close();
                connection.close();
            }                                               // end try
                    catch (SQLException sqlException) {}    // end catch
            finally                                         // update database connection status
            {
                connectedToDatabase = false;
            }                                               // end finally
        }                                                   // end if
    }    // end method disconnectFromDatabase

    public int Query(String sql) {
        try {
            statement = connection.createStatement();

            // System.out.println(sql);
            return statement.executeUpdate(sql);
        } catch (SQLException pp) {
            System.out.println(sql);
            System.out.println(pp.toString());
        }

        return 0;
    }

    public ArrayList ExecuteQuery(String sql) throws SQLException {
        ArrayList resultSet = new ArrayList();

        try {
            if (connection != null) {
                statement = connection.createStatement();
                result    = statement.executeQuery(sql);
                metadata  = result.getMetaData();

                // String[] res=null;
                int numcolls = metadata.getColumnCount();

                while (result.next()) {
                    for (int i = 1; i <= numcolls; i++) {
                        System.out.println(i + ":" + result.getObject(i));
                        resultSet.add(result.getObject(i));
                    }
                }
            }
        } catch (SQLException pp) {
            System.out.println(pp.toString());
        }

        if (resultSet.isEmpty()) {
            resultSet.add(0);
        }

        return resultSet;
    }

    /**
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    private void createDB() {
        try {
            String userHomeDir = System.getProperty("user.home", ".");
            String systemDir   = userHomeDir + "/.Bus";

            // Set the db system directory.
            System.setProperty("derby.system.home", systemDir);

            String     url   = "jdbc:derby:bus;create=true";
            Properties props = new Properties();

            props.put("user", "bus");

            try {

                // Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                connection = DriverManager.getConnection(url, props);
                statement  = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e.toString());
            }

            String query;

            System.out.println("creating passengers table");
            query = "CREATE TABLE BUS.PASSENGERS(" + "ID INTEGER," + "NAME VARCHAR (50) NOT NULL,"
                    + "DEPARTURE VARCHAR (15) NOT NULL," + "DESTINATION VARCHAR (15) NOT NULL," + "PHONE VARCHAR (15),"
                    + "Date DATE NOT NULL," + "SeatNo VARCHAR (15)" + ")";
            statement.execute(query);
            query = "CREATE TABLE BUS.SelectTedSeats(" + "Number INTEGER," + "BusNumber VARCHAR (50) NOT NULL,"
                    + "Date VARCHAR (15) NOT NULL," + "Pass VARCHAR (15) NOT NULL," + "PHONE VARCHAR (15)" + ")";
            statement.execute(query);
            System.out.println("created passengers table");
            System.out.println("creating mail table");
            query = "CREATE TABLE BUS.MAIL(" + "SENDERID INTEGER," + "SENDERNAME VARCHAR (50) NOT NULL,"
                    + "SPHONE VARCHAR (15)," + "ITEMS VARCHAR (200) NOT NULL," + "DEPARTURE VARCHAR (15) NOT NULL,"
                    + "RECNAME VARCHAR (50) NOT NULL," + "RPHONE VARCHAR (15)," + "DESTINATION VARCHAR (15) NOT NULL,"
                    + "DATE TIMESTAMP NOT NULL)";
            statement.execute(query);
            System.out.println("created mail table");
            System.out.println("creating journey table");
            query = "CREATE TABLE BUS.JOURNEY(" + "DEPARTURE VARCHAR (15) NOT NULL,"
                    + "DESTINATION VARCHAR (15) NOT NULL," + "PRICE INTEGER NOT NULL,"
                    + "PRIMARY KEY (DEPARTURE, DESTINATION))";
            statement.execute(query);
            query = "insert into JOURNEY VAlues('Buea','Yaounde',5000)";
            statement.execute(query);
            query = "insert into JOURNEY VAlues('Douala','Yaounde',2000)";
            statement.execute(query);
            System.out.println("created journey table");
            System.out.println("creating table itinerary");
            query = "CREATE TABLE BUS.ITINERARY(" + "DEPARTURE VARCHAR (15) NOT NULL," + "DEPTERMINAL VARCHAR (25),"
                    + "DESTINATION VARCHAR (15) NOT NULL," + "ARRITERMINAL VARCHAR (15)," + "DATE TIMESTAMP" + ")";
            statement.execute(query);

            Calendar bb = Calendar.getInstance();

            query = "insert into ITINERARY values('Buea','Bue','Doual',' ss','2002-01-01 00:00:00')";
            statement.execute(query);
            System.out.println("created table itinerary");
            System.out.println("creating table terminals");
            query = "CREATE TABLE BUS.TERMINALS(" + "LOCATION VARCHAR (15) NOT NULL,"
                    + "TERMINAL VARCHAR (25) NOT NULL," + "PRIMARY KEY (LOCATION, TERMINAL))";
            statement.execute(query);
            query = "insert into TERMINALS values('Buea','Douala') ";
            statement.execute(query);
            query = "insert into TERMINALS values('Douala','Buea') ";
            statement.execute(query);
            query = "insert into TERMINALS values('Yaounde','Douala') ";
            statement.execute(query);
            System.out.println("creating buses table");
            query = "CREATE TABLE BUS.BUSES(" + "BUSNUMBER VARCHAR (10) NOT NULL," + "BUSBRAND VARCHAR (50) NOT NULL,"
                    + "NUMBEROFSEATS INTEGER NOT NULL," + "PRIMARY KEY (BUSNUMBER))";
            statement.execute(query);
            query = "insert into BUSES values('10Sd','Amour Mezam',70)";
            statement.execute(query);
            query = "insert into BUSES values('10SSS','Amour Mezam',55)";
            statement.execute(query);
            query = "insert into BUSES values('10SSSf','Amour Mezam',30)";
            statement.execute(query);
            System.out.println("created buses table");
            System.out.println("creating cashier table");
            query = "CREATE TABLE BUS.CASHIER(" + "NAME VARCHAR (50) NOT NULL," + "BRANCH VARCHAR (15) NOT NULL,"
                    + "PRIMARY KEY (BRANCH))";
            statement.execute(query);
            System.out.println("created cashier table");
        } catch (SQLException ex) {
            System.out.println("" + ex.getCause());
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
