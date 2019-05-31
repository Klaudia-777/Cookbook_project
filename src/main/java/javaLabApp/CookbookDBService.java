package javaLabApp;

import org.sqlite.SQLiteException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CookbookDBService {
    private Util service = new Util();
    private JButton chooseRecipe;
    private static Connection conn = null;
    private static Statement stat = null;
    private static ResultSet rsRecipes = null;

    /**
     * CREATE CONNECTION WITH DATABASE
     */

    void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
//            System.out.println("INFO: Driver was found.");
        } catch (ClassNotFoundException e) {
//            System.err.println("ERROR: You should download driver first:  https://bitbucket.org/xerial/sqlite-jdbc");
            System.exit(1);
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:file.db");
//            System.out.println("INFO: Connection was established.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *  DELETING TABLES
     */

    public void dropTables() {
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

    /**
     * DROPPING ROWS
     * FOR TESTS MOSTLY
     */

    public  void dropRow() {
        try {
            stat = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stat.executeUpdate("delete from recipes where name like 'Picatta%';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * CREATING TABLES
     */

    void createTables() {
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


    /**
     * INSERTING DATA INTO RECIPES TABLE
     */

    void insertDataIntoRecipesTable(List<String> data) {
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

    /**
     *  INSERTING DATA INTO INGRIDIENTS TABLE
     */

    void insertDataIntoIngridientsTable(List<String> data) {
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

    /**
     *  READING DATA (FOR TESTS MOSTLY)
     */

//    void readDataRecipes() {
//        try {
//            stat = conn.createStatement();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            rsRecipes = stat.executeQuery("select * from recipes;");
//            while (rsRecipes.next()) {
//                System.out.println("id= " + rsRecipes.getString("id") + ",  " + "\n");
//                System.out.println("name = " + rsRecipes.getString("name") + ",  " + "\n");
//                System.out.println("category = " + rsRecipes.getString("category") + "\n");
//                System.out.println("image url = " + rsRecipes.getString("urlAddress") + "\n");
//                System.out.println("instructions = " + rsRecipes.getString("instructions") + "\n");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//  void readDataIngridients() {
//        try {
//            stat = conn.createStatement();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            rsIngridients = stat.executeQuery("select * from ingridients;");
//            while (rsIngridients.next()) {
//                System.out.println("id= " + rsIngridients.getString("id") + ",  " + "\n");
//                System.out.println("name = " + rsIngridients.getString("name") + ",  " + "\n");
//                System.out.println("category = " + rsIngridients.getString("recipes_name") + "\n");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     *  FILTERING RECIPES BY CATEGORY
     */

    void filterRecipesByCategory(String categoryCriteria) {
        JFrame jf = new JFrame("Choose a dish");
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.yellow);
        try {
            rsRecipes = stat.executeQuery("select count(*)as 'count'" +
                    "from recipes " +
                    "where category = '" + categoryCriteria + "';");

            GridLayout gridLayout = new GridLayout(Integer.parseInt(rsRecipes.getString("count")), 1);
            jPanel.setLayout(gridLayout);

            rsRecipes = stat.executeQuery("select *" +
                    "from recipes " +
                    "where category = '" + categoryCriteria + "';");

            while (rsRecipes.next()) {
                chooseRecipe=new JButton(rsRecipes.getString("name"));
                chooseRecipe.addActionListener(new FinalRecipeViewActionListener());
                jPanel.add(chooseRecipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        service.setJFrame(jf,true,true,0,0,
                800,350,true,Arrays.asList(new JComponent[]{jPanel}));
    }

    /**
     *  FILTERING RECIPES BY CATEGORY AND ALSO INGRIDIENT
     */

    void filterByCategoriesAndIngridients(String categoryCriteria, String ingridientCriteria) {
        JFrame jf = new JFrame("Choose a dish");
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.yellow);

        try {
            rsRecipes = stat.executeQuery("select count(*) as 'count' " +
                    "from recipes " +
                    "inner join ingridients " +
                    "on recipes.name=ingridients.recipes_name " +
                    "where ingridients.name " +
                    "LIKE '%" + ingridientCriteria + "%'" +
                    "and recipes.category = '" + categoryCriteria + "';");

            GridLayout gridLayout = new GridLayout(Integer.parseInt(rsRecipes.getString("count")), 1);
            jPanel.setLayout(gridLayout);


            rsRecipes = stat.executeQuery("select *" +
                    "from recipes " +
                    "inner join ingridients " +
                    "on recipes.name=ingridients.recipes_name " +
                    "where ingridients.name " +
                    "LIKE '%" + ingridientCriteria + "%'" +
                    "and recipes.category = '" + categoryCriteria + "';");


            while (rsRecipes.next()) {
                chooseRecipe=new JButton(rsRecipes.getString("name"));
                chooseRecipe.addActionListener(new FinalRecipeViewActionListener());
                jPanel.add(chooseRecipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        service.setJFrame(jf,true,true,400,500,
                800,350,true, Arrays.asList(new JComponent[]{jPanel}));
    }


    /**
     *  FILTERING RECIPES BY INGRIDIENT ONLY (FOR TESTS)
     */


//    void filterRecipesByIngridients(String ingridientCriteria) {
//        JFrame jf = new JFrame("Choose a dish");
//
//        JPanel jPanel = new JPanel();
//
//        try {
//            rsRecipes = stat.executeQuery("select recipes.name " +
//                    "from recipes " +
//                    "inner join ingridients " +
//                    "on recipes.name=ingridients.recipes_name " +
//                    "where ingridients.name " +
//                    "LIKE '%" + ingridientCriteria + "%';");
//
//            while (rsRecipes.next()) {
//                chooseRecipe=new JButton(rsRecipes.getString("name"));
//                chooseRecipe.addActionListener(new FinalRecipeViewActionListener());
//                jPanel.add(chooseRecipe);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        jf.getContentPane().add(jPanel, BorderLayout.CENTER);
//        service.setJFrame(jf);
//        jf.setSize(500, 500);
//        jf.setLocation(1300,0);
//    }




    /**
     *  FILTERING RECIPES BY NAME
     *  (PARSING RECIPE FROM CONNECTED BUTTON)
     *  -> FinalRecipeViewActionListener
     */


    List<String> filterByName(String name){
        List<String> dataPreparedToFinalFrame=new ArrayList<>();
        try {
            rsRecipes = stat.executeQuery("select * " +
                    "from recipes " +
                    "where name =  '" + name + "';");

            dataPreparedToFinalFrame.add(rsRecipes.getString("name"));
            dataPreparedToFinalFrame.add(rsRecipes.getString("category"));
            dataPreparedToFinalFrame.add(rsRecipes.getString("urlAddress"));
            dataPreparedToFinalFrame.add(rsRecipes.getString("instructions"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rsIngridients = stat.executeQuery("select * " +
                    "from ingridients " +
                    "where recipes_name =  '" + name + "';");
            while (rsRecipes.next()) {
                dataPreparedToFinalFrame.add(rsIngridients.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataPreparedToFinalFrame;
    }

    /**
     *  CLOSING CONNECTION
     */


    void closeConnection() {
        try {
            conn.close();
//            System.out.println("INFO: Connection was closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

