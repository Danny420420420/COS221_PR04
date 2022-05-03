
package za.ac.up.cs.cos221;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

/*import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner;*/


public class connection {
    private static Connection con;
    public connection()throws SQLException{
        System.out.println("Connecting to database...");
        String SAKILA_DB_HOST = "";
        String SAKILA_DB_PORT = "";
        String SAKILA_DB_NAME = "u21669831_sakila";
        String SAKILA_DB_USERNAME = "root";
        String SAKILA_DB_PASSWORD = "";
        String SAKILA_DB_PROTO = "jdbc:mariadb://" + SAKILA_DB_HOST + ":" + SAKILA_DB_PORT + "/" + SAKILA_DB_NAME;
        con = DriverManager.getConnection(SAKILA_DB_PROTO, SAKILA_DB_USERNAME, SAKILA_DB_PASSWORD);
        System.out.println("Connection valid: " + con.isValid(5));
    }
    
    public Connection openConnection()throws SQLException{
        return con;
    }
    
    public static void closeCon() throws SQLException{
        System.out.println("Closing DB conection");
        con.close();
    }
    
}
