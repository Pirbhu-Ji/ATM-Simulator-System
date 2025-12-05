package bank.db;

import java.sql.*;

public class DatabaseConnection{
    public Connection con;
    public Statement stmt;

    public DatabaseConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); //load MySQL JDBC Driver
            String url="jdbc:mysql://localhost:3306/bankdb";
            String user="root";
            String password="1234";
            con = DriverManager.getConnection(url,user,password);
            stmt = con.createStatement();
        }catch (Exception a){
            a.printStackTrace();
        }
    }
}
