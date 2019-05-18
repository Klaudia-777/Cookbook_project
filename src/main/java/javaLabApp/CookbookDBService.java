package javaLabApp;

import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.List;

public class CookbookDBService {

    static Connection conn = null;
    static Statement stat = null;
    static ResultSet rsRecipes = null;
    static ResultSet rsIngridients = null;

    public ResultSet getRs() {
        return rsRecipes;
    }

    public void createConnection() {
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

    public void dropTables(){
        try {
            stat = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stat.executeUpdate("drop table recipes ;");
            stat.executeUpdate("drop table ingridients ;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createTables() {
        try {
            stat = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stat.executeUpdate(" create table IF NOT EXISTS recipes (id integer primary key autoincrement, name text unique not null, category text not null, urlAddress text not null, instructions text not null) ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stat.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stat.executeUpdate(" create table IF NOT EXISTS ingridients (id integer primary key autoincrement, name text not null, recipes_name text not null, FOREIGN KEY (recipes_name) REFERENCES recipes(name) ON DELETE CASCADE ON UPDATE CASCADE);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDataIntoRecipesTable(List<String> data) {
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
        } catch (SQLiteException lite) {
            System.out.println(" ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDataIntoIngridientsTable(List<String> data) {
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("insert into ingridients(name,recipes_name) values (?, ?);");
            prep.setString(1, data.get(0));
            prep.setString(2, data.get(1));
            prep.addBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            prep.executeBatch();
        } catch (SQLiteException lite) {
            System.out.println(" ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readDataRecipes() {
        try {
            stat = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            rsRecipes = stat.executeQuery("select * from recipes;");
            while (rsRecipes.next()) {
                System.out.println("id= " + rsRecipes.getString("id") + ",  " + "\n");
                System.out.println("name = " + rsRecipes.getString("name") + ",  " + "\n");
                System.out.println("category = " + rsRecipes.getString("category") + "\n");
                System.out.println("image url = " + rsRecipes.getString("urlAddress") + "\n");
                System.out.println("instructions = " + rsRecipes.getString("instructions") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readDataIngridients() {
        try {
            stat = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            rsIngridients = stat.executeQuery("select * from ingridients;");
            while (rsIngridients.next()) {
                System.out.println("id= " + rsIngridients.getString("id") + ",  " + "\n");
                System.out.println("name = " + rsIngridients.getString("name") + ",  " + "\n");
                System.out.println("category = " + rsIngridients.getString("recipes_name") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnection() {
        try {
            conn.close();
            System.out.println("INFO: Connection was closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

