package productsearch;

import java.util.*;
import java.sql.*;
import java.io.*;

public class SearchEngine {

    private Connection myConn;

    public SearchEngine() throws Exception {

        // Get db properties

        Properties props = new Properties();
        props.load(new FileInputStream("web_app.properties"));

        String user = props.getProperty("user");
        String password = props.getProperty("password");
        String dburl = props.getProperty("dburl");

        // Connect to database
        myConn = DriverManager.getConnection(dburl, user, password);

        System.out.println("DB connection successful to " + dburl);
    }

    public List<Product> getAllProducts() throws Exception {

        List<Product> list = new ArrayList<>();

        Statement myStmt = null;
        ResultSet myRs = null;

        try {

            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery("select * from product");

            while (myRs.next()) {

                Product tempProduct = convertRowToProduct(myRs);

                list.add(tempProduct);

            }

            return list;
        }

        finally {

            close(myStmt, myRs);
        }
    }

    public List<Product> searchProducts(String keyWord) throws Exception {

        List<Product> list = new ArrayList<>();

        Statement myStmt =  null;
        ResultSet myRs = null;

        String name, company, category;
        int id;
        float stars;

        /*
         * Define how narrow the search will be
         * using parameter T. Return to the user
         * everything within distance T.
         */

        /*
         * T parameter should not be static
         */

        int len = keyWord.length();
        int T;

        if (len < 4) T = 1;
        else if (len < 7) T = 2;
        else T = 3;

        try {

            myStmt = myConn.createStatement();

            String sql = "select * from product";

            myRs = myStmt.executeQuery(sql);

            while (myRs.next()) {

                name = myRs.getString("name");
                company = myRs.getString("company");
                category = myRs.getString("category");

                int disName = distance(keyWord, name);
                int disCompany = distance(keyWord, company);
                int disCategory = distance(keyWord, category);

                /*
                 * If our keyword matches in some degree
                 * with the characteristics of a product
                 * then add it to the list and display it
                 * to the user
                 */

                if (disName <= T || disCompany <= T || disCategory <= T) {

                    id = myRs.getInt("id");
                    stars = myRs.getFloat("stars");

                    Product tempProduct = new Product(id, name, company, category, stars);
                    list.add(tempProduct);
                }
            }

            return list;
        }
        finally {

            close(myStmt, myRs);
        }

    }

    private Product convertRowToProduct(ResultSet myRs) throws SQLException {

        int id = myRs.getInt("id");
        String name = myRs.getString("name");
        String company = myRs.getString("company");
        String category = myRs.getString("category");
        float stars = myRs.getFloat("stars");

        Product tempProduct = new Product(id, name, company, category, stars);

        return tempProduct;
    }
    private static void close(Connection myConn, Statement myStmt, ResultSet myRs)
            throws SQLException {

        if (myRs != null) {
            myRs.close();
        }

        if (myStmt != null) {
            myStmt.close();

        }

        if (myConn != null) {
            myConn.close();
        }
    }

    private void close(Statement myStmt, ResultSet myRs) throws SQLException {
        close(null, myStmt, myRs);
    }


    /*
     * A method that calculates Levensthein
     * distance using Dynamic Programming
     */


    static int distance(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    static int min (int a, int b, int c) {

        return Math.min(Math.min(a, b),c);
    }


    /*
     * Our search engine is not case sensitive
     */

    public static int costOfSubstitution(Character a, Character b) {
        if (Character.isUpperCase(a)) a = Character.toLowerCase(a);
        if (Character.isUpperCase(b)) b = Character.toLowerCase(b);
        return a == b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {

        SearchEngine dao = new SearchEngine();
        System.out.println(dao.searchProducts("Adibs"));

        System.out.println(dao.getAllProducts());
    }
}





