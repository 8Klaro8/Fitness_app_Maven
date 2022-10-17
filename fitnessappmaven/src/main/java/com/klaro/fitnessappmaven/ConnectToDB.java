package com.klaro.fitnessappmaven;

import java.sql.Statement;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ConnectToDB {

    public Connection connect_to_db(String dbname, String user, String password) {

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, password);
            if (conn != null) {
                System.out.println("Connection estabilished!");
            } else {
                System.out.println("Connection failed!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return conn;
    }

    public void create_table(Connection conn, String table_name) {
        /**
         * Creates table
         * 
         * @param Connection conn
         * @param String     table_name
         */
        Statement statement;
        try {
            String query = "create table " + table_name
                    + "(empid SERIAL, name varchar(200), address varchar(200), workout_dict varchar(200), primary key(empid));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created: " + table_name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void create_account_table(Connection conn, String table_name) {
        /**
         * Creates user account table
         * 
         * @param Connection conn
         * @param String     table_name
         */
        try {
            String query = "create table " + table_name
                    + "(user_id SERIAL, prof_image varchar(200), username varchar(200), password varchar(200), firstName varchar(200), lastName varchar(200), workout_dict varchar(200), primary key(user_id));";
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created: " + table_name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert_row(Connection conn, String table_name, String name, String address) {
        /**
         * Inserts a row to the selected table
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     name
         * @param String     address
         */
        Statement statement;
        try {
            String query = String.format("insert into %s(name, address) values ('%s', '%s');", table_name, name,
                    address);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row inserted!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void read_data(Connection conn, String table_name) {
        /**
         * Reads all data from chosen table
         * 
         * @param Connection conn
         * @param String     table_name
         */
        Statement statement;
        ResultSet rs;
        try {
            String query = String.format("select * from %s", table_name);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getString("empid") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getString("address") + " ");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet get_by_name(Connection conn, String table_name, String name) {
        /**
         * Prints all feld of the searched item
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     name
         */
        Statement statement;
        ResultSet result = null;
        try {
            String query = String.format("select * from %s where username='%s'", table_name, name);
            statement = conn.createStatement();
            result = statement.executeQuery(query);
            try {
                ResultSetMetaData resultMT = result.getMetaData();
                int columnCount = resultMT.getColumnCount();
                while (result.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        String columnValue = result.getString(i);
                        System.out.println(resultMT.getColumnName(i) + ": " + columnValue);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void delete_row(Connection conn, String table_name, int id) {
        /**
         * Deletes a row by using table name and id
         * 
         * @param String table_name
         * @param int    id
         */

        String query = String.format("delete from %s where empid=%s", table_name, id);
        try {
            Statement statement = conn.createStatement();
            System.out.println("Row with " + id + " has been deleted.");
            statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void update_name(Connection conn, String table_name, String update_name, String name) {
        /**
         * Updates name column by where name equals to name
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     update_name
         * @param String     name
         * 
         */
        String query = String.format("UPDATE %s SET name='%s' WHERE name='%s';", table_name, update_name, name);
        try {
            Statement statemnt = conn.createStatement();
            statemnt.executeUpdate(query);
            System.out.println("Name updated!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void add_user(Connection conn, String username, String hashed_password, String first_name,
            String last_name, String profPic) {
        /**
         * Creates user to account db
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     username
         * @param String     hashed_password
         * @param String     first_name
         * @param String     last_name
         * 
         */
        Statement statement;
        try {
            String query = String.format(
                    "insert into my_users (username, password, firstname, lastname, prof_image) values ('%s', '%s', '%s', '%s', '%s');",
                    username,
                    hashed_password, first_name, last_name, profPic);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row inserted!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String get_hash_by_username(Connection conn, String table_name, String username) {
        /**
         * Prints all feld of the searched item
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     name
         */
        Statement statement;
        try {
            String query = String.format("select password, user_id from %s where username='%s'", table_name, username);
            statement = conn.createStatement();
            ResultSet resSet = statement.executeQuery(query);
            if (resSet.next()) {
                return resSet.getString("password");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "EMPTY";
    }

    public String get_prof_pic_path(Connection conn, String table_name, String username) {
        /**
         * Gets the proile pics path by userame
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     username
         * 
         */
        String query = String.format("select prof_image from %s where username='%s'", table_name, username);
        try {
            Statement statement = conn.createStatement();
            ResultSet resSet = statement.executeQuery(query);
            if (resSet.next()) {
                return resSet.getString("prof_image");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void set_prof_pic_path(Connection conn, String table_name, String updated_value, String username) {
        /**
         * Sets the current users profile pic by path
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     updated_value
         * @param String     username
         * 
         */
        String query = String.format("update %s set prof_image='%s' where username='%s'", table_name, updated_value,
                username);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Prof pic has been changed");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void add_workout(Connection conn, String table_name, String work_dict, String current_user) {
        /**
         * Adds workout with icon, name, rep, set, time(intervalum)
         * 
         * @param Connection conn
         * @param String     table_name
         * @param String     work_dict
         * @param String current_user
         * 
         */
        String query = String.format("insert into %s work_dict value '%s' where username='%s';", table_name, work_dict, current_user);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Workout iserted successfuly!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean username_exists(Connection conn, String table_name, String username) {
        String query = String.format("select username in %s where username='%s'", table_name, username); 
        try {
            Statement statement = conn.createStatement();
            statement.executeQuery(query);
            return true;
        } catch (Exception e) {
            return false;
            // System.out.println(e.getMessage());
        }
    }
}
