import java.sql.*;


public class Connect {

    public static void main(String[] args) {

        try {

            // First of all, get a connection to the database

            /* The database can be located on a local machine,
            on a remote server, or in the cloud
             */


            String url = "jdbc:mysql://localhost:3306/web_app";

            /* In this example, we are using mysql as database management
            system and we have created a database with the name web_app,
            that corresponds to our web application. The default port used by
            mysql is 3306
            */

            /* Username and password that have been set
            in order to access the database
            */

            String user = "%%%%";
            String password = "%%%%%%%%";


            Connection myConn = DriverManager.getConnection(url, user, password);


            // Create a statement

            Statement myStmt = myConn.createStatement();

            // Executing a simple SQL query

            String sql = "select * from user";

            ResultSet myRs = myStmt.executeQuery(sql);

            // Process the result set

            /* myRs.next() method returns true if the are
            rows left to process. It also moves the cursor
            to the next entry
             */


            while (myRs.next()) {

              /* So, in this part we are able to process that was
              produced by the query
               */

                System.out.println(myRs.getString("user_name") + "," + myRs.getString("email"));
            }

            System.out.println("Query process has been completed");



        }

        catch (Exception exc) {

            // Exception handler depending on the class of the exception object

            /* First likely cause: could not establish connection */

            /* Second likely case: error in the execution of the query,
            in general that can be caused by a duplicate entry in a unique filed,
            an entry that fails to satisfy the required constraints, especially
            foreign keys constraints, etc.
             */

            /* Therefore, in this segment we handle all the bad cases,
            that are inevitable because of the user's input
             */

            // Display information regarding the exception

            exc.printStackTrace();
        }
    }
}
