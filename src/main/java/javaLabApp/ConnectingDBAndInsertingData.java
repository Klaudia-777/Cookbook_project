package javaLabApp;

import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.List;

// CREATING DATA
public class ConnectingDBAndInsertingData {

    static Connection conn = null;
    static Statement stat = null;
    static ResultSet rs = null;


    public  void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("INFO: Driver was found.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: You should download driver first:  https://bitbucket.org/xerial/sqlite-jdbc");
            System.exit(1);
        }

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:file.db");
            System.out.println("INFO: Connection was established.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------
    public  void insertData(List<String> data) {
        try {
            stat = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//           //stat.executeUpdate("drop table if exists recipes;");
//           stat.executeUpdate("create table recipes (id integer primary key autoincrement, name text unique not null, category text not null, urlAddress text not null, instructions text not null);");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("insert into recipes(name,category,urlAddress,instructions) values (?, ?, ?, ?);");
           // prep.setString(1,);
            prep.setString(1, data.get(0));
            prep.setString(2, data.get(1));
            prep.setString(3, data.get(2));
            prep.setString(4, data.get(3));

            prep.addBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            prep.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // ------------------------------------------------------------------------------------------

    public void readData(){
        try {
            stat = conn.createStatement();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            rs = stat.executeQuery("select * from recipes;");
            while (rs.next()) {
                System.out.println("id= " + rs.getString("id") + ",  "+"\n");
                System.out.println("name = " + rs.getString("name") + ",  "+"\n");
                System.out.println("category = " + rs.getString("category")+"\n");
                System.out.println("image url = " + rs.getString("urlAddress")+"\n");
                System.out.println("instructions = " + rs.getString("instructions")+"\n");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // ------------------------------------------------------------------------------------------
    public  void closeConnection() {
        try {
            conn.close();
            System.out.println("INFO: Connection was closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

