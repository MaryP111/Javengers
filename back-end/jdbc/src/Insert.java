
import java.sql.*;


public class Insert {

    public static void main(String[] args) {

        try {


            // Get a connection to the database

            String url = "jdbc:mysql://localhost:3306/web_app";
            String user = "%%%";
            String password = "%%%%%%%";

            Connection myConn = DriverManager.getConnection(url, user, password);


            // Create a statement

            Statement myStmt = myConn.createStatement();

            // Query to insert values into the database

            String sql = "insert into user (first_name, last_name, user_name, password, email, phone_number)" +
                    " VALUES ('Bob', 'Tucker', 'bobTucker', '12345789', 'bobTucker@ntua.gr', 1234567894)";

            // Execute the query, i.e. insert the entry into the table

            myStmt.executeUpdate(sql);

            // Verify that the insert was successful

            System.out.println("Insert was successful");

        }

        catch (Exception exc) {

            exc.printStackTrace();
        }
    }
}
