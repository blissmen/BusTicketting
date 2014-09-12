/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bus.ticketing.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Harvey
 */
public class DatabaseHelper {

    public Connection connection;
    public ResultSet resultSet;
     public ResultSetMetaData metaData;
    public int numberOfRows;
    public Statement statement;
     public boolean connectedToDatabase;
     public ResultSet result;
     public ResultSetMetaData metadata;

    public DatabaseHelper() throws SQLException {

        this.numberOfRows = 0;
        this.statement = null;
        this.connectedToDatabase = false;
        this.resultSet = null;
        this.metaData = null;
        this.connection = null;

        createDB();//create the database if it does not exist
        // update database connection status
        connectedToDatabase = true;
    }

 
    public final void setQuery(String query)
            throws SQLException, IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // specify query and execute it WATCH OUT: statement.executequery doesnot 
//        run on queries that return number of rows such as inserts, so I use statement.execute()
        statement.execute(query);
        resultSet = statement.getResultSet();
        if (resultSet != null) {//a resultset is null if it is an update, count, and I think insert
            // determine number of rows in ResultSet
            resultSet.last(); // move to last row
            numberOfRows = resultSet.getRow(); // get row number
            metaData = resultSet.getMetaData();
        }else{
            numberOfRows = 0;
        }

    } // end method setQuery

    public int getColumnCount() throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // determine number of columns
        try {
            return metaData.getColumnCount();
        } // end try
        catch (SQLException sqlException) {
        } // end catch

        return 0; // if problems occur above, return 0 for number of columns
    } // end method getColumnCount

    // get name of a particular column in ResultSet
    public String getColumnName(int column) throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // determine column name
        try {
            return metaData.getColumnName(column + 1);
        } // end try
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch

        return ""; // if problems, return empty string for column name
    } // end method getColumnName
    // return number of rows in ResultSet

    public int getRowCount() throws IllegalStateException {
        // ensure database connection is available

        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        return numberOfRows;
    } // end method getRowCount

    public Object getValueAt(int row, int column)
            throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // obtain a value at specified ResultSet row and column
        try {
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        } // end try
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch

        return ""; // if problems, return empty string object
    } // end method getValueAt

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
// close Statement and Connection
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } // end try
            catch (SQLException sqlException) {
            } // end catch
            finally // update database connection status
            {
                connectedToDatabase = false;
            } // end finally
        } // end if
    } // end method disconnectFromDatabase
            
      public int Query(String sql)
    {
        try{
            statement = connection.createStatement();
           // System.out.println(sql);
            return  statement.executeUpdate(sql);
        
        }
        catch(SQLException pp)
        {
            System.out.println(sql);
            System.out.println(pp.toString());
        }
        return 0;
    
    
    }
    public ArrayList ExecuteQuery(String sql) throws SQLException
    {
        ArrayList resultSet = new  ArrayList();
       try
       { 
           if(connection!=null)
           {
      statement = connection.createStatement();
        result = statement.executeQuery(sql);
       metadata = result.getMetaData();
     //  String[] res=null;
       int numcolls = metadata.getColumnCount();
       while(result.next())
       {
       
       for ( int i = 1; i <= numcolls; i++ )
       {
            
             System.out.println(i+":"+result.getObject(i));  
            
             resultSet.add(result.getObject(i));
       
            
       }
       }
           }
       }
       
       
       
       catch(SQLException pp)
       {
           System.out.println(pp.toString());
       }
       
       if(resultSet.isEmpty())
           resultSet.add(0);
        return resultSet;
       }
    

    private void createDB()  {
        try {
            String userHomeDir = System.getProperty("user.home", ".");
            String systemDir = userHomeDir + "/.bam";
            // Set the db system directory.
            System.setProperty("derby.system.home", systemDir);
            
            String url = "jdbc:derby:bam;create=true";
            Properties props = new Properties();
            props.put("u", "p");
            
            try {
                //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                connection = DriverManager.getConnection(url, props);
                statement = connection.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e.toString());
            }
            
            String query;
            String qu;
       
            qu = "CREATE TABLE bam.department ( "
                + " dept_id SMALLINT NOT NULL generated always as identity(start with 1 , increment by 1)," //maybe we can use smallint here as the workers may only be bellow 10 000 in number.
                + " department VARCHAR(25) NOT NULL," //reduce the size to say 15
                + " post VARCHAR(15) NOT NULL," //reduce the size to say 10
                + " salary INT NOT NULL, " 
                + " PRIMARY KEY (dept_id))";
            //OK
             
            System.out.println(qu);
          try{  statement.execute(qu);
              System.out.println("success");
          }
          catch(Exception asd)
          {
              System.out.println(asd.getMessage());
          }
            query = "CREATE TABLE bam.staff ( "  
                    + " staff_id SMALLINT NOT NULL generated always as identity(start with 1 , increment by 1) ,"
                    + " dept_id SMALLINT NOT NULL,"
                    + " staff_idcard_num VARCHAR(15) NOT NULL," //maybe varchar(20) with reasones same as that of custumers
                    + " name VARCHAR(45) NOT NULL,"
                    + " sex CHAR(1) NOT NULL,"
                    + " address VARCHAR(45) NOT NULL,"
                    + " marital_status VARCHAR(8) NOT NULL," //marital status, think it should be varchar(8) as the maximum can be devorced
                    + " region_of_origin VARCHAR(8) NOT NULL," //varchar(10) with the max south west and north west
                    + " phone_num VARCHAR(13) NOT NULL," //int and also a check
                    + " email VARCHAR(45) NOT NULL," //needs a check here.
                    + " emp_date DATE NOT NULL,"
                    + " PRIMARY KEY (staff_id),"
                    //+  "CONSTRAINT fk_staff_payroll_des1"
                    + " FOREIGN KEY (dept_id)"
                    + " REFERENCES bam.department (dept_id) "
                    + " ON DELETE NO ACTION " //learn on update and delete, as we have to deal with the updating and deleting of staffs from the system.
                    + " ON UPDATE NO ACTION) ";

            System.out.println(query);
            statement.execute(query);
             
            query = "CREATE TABLE bam.source_destination_list ( "
                   + " sdl_id SMALLINT NOT NULL generated always as identity(start with 1 , increment by 1) ,"
                   + " source VARCHAR(15) NOT NULL, "
                   + " destination VARCHAR(15) NOT NULL, "
                   + " distance SMALLINT NOT NULL, "
                   + " time_interval VARCHAR(13) NOT NULL, " // 11 - 17
                   + " period varchar(9) , "
                   + " PRIMARY KEY (sdl_id))";

            System.out.println(query);
            statement.execute(query);
            
            String  query2= "CREATE TABLE bam.customer ("
                          + "  id_card_number VARCHAR(15) NOT NULL," 
                          + " n0_of_journeys SMALLINT NOT NULL," //small int should be used. a vehicle cannot do upto 10 000 journeys
                          + " Name VARCHAR(45) NOT NULL,"
                          + " telephone_number VARCHAR(13) NOT NULL," //need a check to validate the format. can we also add country of origin?
                          + " email VARCHAR(45) NOT NULL," //need a check here to validate email formats
                          + " PRIMARY KEY (id_card_number))";

            System.out.println(query2);
           statement.execute(query2);
           
            query = "CREATE TABLE bam.bus ("
                    + " bus_mat VARCHAR(10) NOT NULL," //char(9), example sw 192 ad
                    + " color VARCHAR(10) NOT NULL,"
                    + " working_state Boolean NOT NULL default true,"//default should be true, meaning its in a working state. this is because, u don't buy a bus in the non working state or is it possible?
                    + " travel_status Boolean NOT NULL default false,"//think default is false, not travelling as the bus when bought, has not been assigned a journey yet
                    + " num_of_seats SMALLINT NOT NULL,"
                    + " driver_id1 SMALLINT NOT NULL,"
                    + " driver_id2 SMALLINT NOT NULL,"
                    + " chasi_num VARCHAR(35) NOT NULL,"//STILL TO BE WORKED ON
                    + " date_of_aquisition DATE NOT NULL,"
                    //not so clear from here up to 
                    + " max_distance SMALLINT NOT NULL,"
                    + " accumulated_distance SMALLINT NOT NULL, "
                    + " no_of_oil_refils SMALLINT NOT NULL, "
                    //h ere
                    + " sdl_id1 SMALLINT NOT NULL,"
                    + " sdl_id2 SMALLINT NOT NULL,"
                    + " internal_view BLOB NOT NULL,"
                    + " PRIMARY KEY (bus_mat),"
                    //+  "CONSTRAINT fk_bus_staff"
                    + " FOREIGN KEY (driver_id1)"
                    + " REFERENCES bam.staff (staff_id)"
                    + " ON DELETE NO ACTION "
                    + " ON UPDATE NO ACTION, "
                    //+  "CONSTRAINT fk_bus_staff1"
                    + " FOREIGN KEY (driver_id2)"
                    + " REFERENCES bam.staff (staff_id)"
                    + " ON DELETE NO ACTION "
                    + " ON UPDATE NO ACTION, "
                    //+  "CONSTRAINT fk_bus_source_destination_list1"
                    + " FOREIGN KEY (sdl_id1)"
                    + " REFERENCES bam.source_destination_list (sdl_id)"
                    + " ON DELETE NO ACTION "
                    + " ON UPDATE NO ACTION, "
                   // +  "CONSTRAINT fk_bus_source_destination_list2"
                    + " FOREIGN KEY (sdl_id2)"
                    + "REFERENCES bam.source_destination_list (sdl_id)"
                    + " ON DELETE NO ACTION "
                    + " ON UPDATE NO ACTION) ";
            
            System.out.println(query);
            statement.execute(query);
               
                    /*
                YD010214BA 
                */
                
            query = "CREATE TABLE bam.bus_schedule ("
                + " schedule_id VARCHAR(13) NOT NULL," //how great can this number be, can a bus go up to 10 000 schedules in its life time? if no, then we can bring it to small int.
                + " sdl_id SMALLINT NOT NULL," // --foreign key -> sdt 
                + " bus_mat VARCHAR(10) NOT NULL," // -- foreign key -> bus
                    //we may be forgetting the time(date) for the bus schedule, think we add the sch_date attribut as such
                     //+ " sch_date date not NOT NULL, "
                + " schedule_date DATE NOT NULL, "
                + " PRIMARY KEY (schedule_id),"
                //+  "CONSTRAINT fk_bus_schedule_source_destination_list1"
                + " FOREIGN KEY (sdl_id)"
                + " REFERENCES bam.source_destination_list (sdl_id)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION, "
                //+  "CONSTRAINT fk_bus_schedule_bus1"
                + " FOREIGN KEY (bus_mat)"
                + " REFERENCES bam.bus (bus_mat)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION) ";
                
            statement.execute(query);
            System.out.println(query);
            
            query = "CREATE TABLE bam.payment ("
                + " pay_id VARCHAR(8) NOT NULL," // eg, RES 0010, REN 1001, DEL 0001
                + " pay_method VARCHAR(10) NOT NULL," //field too long
                + " amount INT NOT NULL, " //amt integer instead of varchar as aggregations are going to be made on this fields
                + " pay_date DATE NOT NULL, "
                + " pay_time TIME NOT NULL, "
                + " staff_id SMALLINT NOT NULL," // -- foreign key -> staff
                + " PRIMARY KEY (pay_id),"
                //+  "CONSTRAINT fk_payment_staff1"
                + " FOREIGN KEY (staff_id)"
                + " REFERENCES bam.staff (staff_id)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION) ";

            System.out.println(query);
            statement.execute(query);
            
            query = " CREATE TABLE bam.reservation ("
                + " reservation_id VARCHAR(15) NOT NULL," //THE FORMAT HAS NOT YET BEEN DECIDED. AND ALSO THE LENGTH
                + " schedule_id VARCHAR(13) NOT NULL,"
                + " pay_id VARCHAR(8) NOT NULL,"
                + " id_card_number VARCHAR(15) NOT NULL," // -- foreign key -> custumer : varchar(9) for id card number. but what if foreigners cm to buy tickets and their id card have more than the specified number of digits?
                + " PRIMARY KEY (reservation_id),"
                //+  " CONSTRAINT fk_reservation_bus_schedule1"
                + " FOREIGN KEY (schedule_id)"
                + " REFERENCES bam.bus_schedule (schedule_id)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION, "
                //+ "CONSTRAINT fk_reservation_payment1"
                + " FOREIGN KEY (pay_id)"
                + " REFERENCES bam.payment (pay_id)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION, "
                //+ "CONSTRAINT fk_reservation_customer1"
                + " FOREIGN KEY (id_card_number)"
                + " REFERENCES bam.customer (id_card_number)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION) ";
            
            System.out.println(query);
            statement.execute(query); 
               
               query = "CREATE TABLE bam.seats ("
                + " seat_number SMALLINT NOT NULL," 
                + " schedule_id VARCHAR(13) NOT NULL,"
                + " reservation_id VARCHAR(15) NOT NULL,"
                + " PRIMARY KEY (seat_number, schedule_id)," //i think the seat_number and schedule_id should be the primary key
                //+ " CONSTRAINT fk_seats_bus_schedule1"
                + " FOREIGN KEY (schedule_id)"
                + " REFERENCES bam.bus_schedule (schedule_id)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION, "
                //+ " CONSTRAINT fk_seats_reservation1"
                + " FOREIGN KEY (reservation_id)"
                + " REFERENCES bam.reservation (reservation_id)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION) ";
            
            System.out.println(query);
            statement.execute(query);
            
            query = "CREATE TABLE bam.rentals ( "
                + " rental_id VARCHAR(8) NOT NULL, "
                + " source VARCHAR(15) NOT NULL, "
                + " destination VARCHAR(15) NOT NULL, "
                + " distance SMALLINT NOT NULL, "
                + " purpose VARCHAR(45) NOT NULL, "
                + " pay_id VARCHAR(8) NOT NULL, "
                + " id_card_number VARCHAR(15) NOT NULL, "
                + " PRIMARY KEY (rental_id), "
                //+ "CONSTRAINT fk_rentals_bus1"
//                + " FOREIGN KEY (bus_bus_mat) "
//                + " REFERENCES bus (bus_mat) "
//                + " ON DELETE NO ACTION "
//                + " ON UPDATE NO ACTION, "
                //+ "CONSTRAINT fk_rentals_payment1"
                + " FOREIGN KEY (pay_id) "
                + " REFERENCES bam.payment (pay_id) "
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION, "
                //+ "CONSTRAINT fk_rentals_customer1"
                + " FOREIGN KEY (id_card_number) "
                + " REFERENCES bam.customer (id_card_number) "
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION) ";
            
            System.out.println(query);
            statement.execute(query);
            
            query = "CREATE TABLE bam.rent_has_bus  ("
                    + " rental_id VARCHAR(8) NOT NULL, "
                    + " bus_mat VARCHAR(10) NOT NULL, "
                    + " driver_id SMALLINT NOT NULL, "
                    + " PRIMARY KEY ( rental_id, bus_mat ), "
                    //+ " CONSTRAINT rentals_has_bus1"
                    + " FOREIGN KEY (rental_id)"
                    + " REFERENCES bam.rentals (rental_id)"
                    + " ON DELETE NO ACTION"
                    + " ON UPDATE NO ACTION,"
                    //+ " CONSTRAINT rentals_has_bus2"
                    + " FOREIGN KEY (bus_mat)"
                    + " REFERENCES bam.bus (bus_mat)"
                    + " ON DELETE NO ACTION"
                    + " ON UPDATE NO ACTION )";
            System.out.println(query);
            statement.execute(query);
            
            
            query = "CREATE TABLE bam.delivery ("
                + " delivery_id VARCHAR(8) NOT NULL, "
                + " receivers_name VARCHAR(25) NOT NULL,"
                + " receivers_tel__number VARCHAR(13) NOT NULL," 
                + " package_type VARCHAR(20) NOT NULL,"
             // + " password VARCHAR(25) NOT NULL," //this too might not always be needed as only the name and location of the receiver is known
                + " status Boolean NOT NULL DEFAULT false,"
                + " receivers_address VARCHAR(45) NOT NULL,"
                + " collection_Date DATE NOT NULL,"
                + " pay_id VARCHAR(8) NOT NULL,"
                + " id_card_number VARCHAR(15) NOT NULL," //foreign key -> custumer table
                + " PRIMARY KEY (delivery_id),"
                //+  "CONSTRAINT fk_delivery_payment1"
                + " FOREIGN KEY (pay_id)"
                + " REFERENCES bam.payment (pay_id)"
                + " ON DELETE NO ACTION"
                + " ON UPDATE NO ACTION,"
                //+  "CONSTRAINT fk_delivery_customer1"
                + " FOREIGN KEY (id_card_number)"
                + " REFERENCES bam.customer (id_card_number)"
                + " ON DELETE NO ACTION"
                + " ON UPDATE NO ACTION)";
             
            System.out.println(query);
            statement.execute(query);
            
            
            query = "CREATE TABLE bam.delivery_has_source_destination_list ("
                    + " delivery_id VARCHAR(8) NOT NULL,"
                    + " sdl_id SMALLINT NOT NULL,"
                    + " PRIMARY KEY (delivery_id, sdl_id),"
                    //+  "CONSTRAINT fk_delivery_has_source_destination_list_delivery1"
                    + " FOREIGN KEY (delivery_id)"
                    + " REFERENCES bam.delivery (delivery_id)"
                    + " ON DELETE NO ACTION"
                    + " ON UPDATE NO ACTION,"
                    //+  "CONSTRAINT fk_delivery_has_source_destination_list_source_destination_li1"
                    + " FOREIGN KEY (sdl_id)"
                    + " REFERENCES bam.source_destination_list (sdl_id)"
                    + " ON DELETE NO ACTION"
                    + " ON UPDATE NO ACTION)";
            
            System.out.println(query);
            statement.execute(query);
            
            query =  "CREATE TABLE bam.user_accounts ("
                //+ " owner_id SMALLINT NOT NULL generated always identity ( start with 1 , increment by 1),"
               + "	owner_id INTEGER NOT NULL GENERATED ALWAYS "
                    + " AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + " username VARCHAR(25) NOT NULL,"
                + " password VARCHAR(45) NOT NULL,"//if we are going to hash the passwords, then its ok, else the the password length is too long.
                + " staff_id SMALLINT NOT NULL,"
                + " type VARCHAR(10) NOT NULL," //varchar(5) with admin being the longest
                + " PRIMARY KEY (owner_id),"
               
                + " FOREIGN KEY (staff_id)"
                + " REFERENCES bam.staff (staff_id)"
                + " ON DELETE NO ACTION "
                + " ON UPDATE NO ACTION ) ";

            System.out.println(query);
            statement.execute(query);
        }
        catch (SQLException ex) {
            Dialogs.create().message("error in schema").showError();
            ex.printStackTrace();
        }
        
    }
}