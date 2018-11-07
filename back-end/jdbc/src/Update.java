
import java.sql.*;


public class Update {

    public static void main(String[] args) {

        try {


            // Get a connection to the database

            String url = "jdbc:mysql://localhost:3306/web_app";
            String user = "%%%%";
            String password = "%%%%%%%%%";

            Connection myConn = DriverManager.getConnection(url, user, password);


            // Create a statement

            Statement myStmt = myConn.createStatement();

            // Query to update values into the database

            String sql = "update user set first_name='Leo' where id=3";

            // Execute the query, i.e. insert the entry into the table

            myStmt.executeUpdate(sql);

            // Verify that the update was successful

            System.out.println("Update complete");

        }

        catch (Exception exc) {

            exc.printStackTrace();
        }
    }
}
