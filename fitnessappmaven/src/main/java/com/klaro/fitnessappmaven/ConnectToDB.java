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

    public void add_workout(Connection conn, String value, String current_user) {
        /**
         * Adds workout with icon, name, rep, set, time(intervalum)
         * 
         * @param Connection conn
         * @param String     work_dict
         * @param String current_user
         * 
         */
        // String query = String.format("UPDATE my_users SET json_workouts = (CASE WHEN json_workouts IS NULL THEN '[]'::JSONB ELSE json_workouts END) || '%s'::JSONB WHERE username='%s';", value, current_user);
        // String query = String.format("UPDATE my_users SET json_workouts = '%s'::JSON WHERE username='%s';", value, current_user);
        // String query = String.format("UPDATE my_users SET json_workouts = json_workouts::jsonb || '%s' WHERE username = '%s';", value, current_user);
        // String query = String.format("UPDATE my_users SET json_workouts = COALESCE(json_workouts, '[]'::JSONB) || '%s'::JSONB WHERE username = '%s';", value, current_user);
        String query = String.format("UPDATE my_users SET json_workouts = COALESCE(json_workouts, '[]'::JSONB) || '%s' WHERE username = '%s';", value, current_user);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete_workout(Connection conn, String workout_name, String current_user) {
        String query = String.format("UPDATE my_users SET json_workouts = json_workouts - '%s' WHERE username = '%s';", workout_name, current_user);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            // System.out.println(result.getString("json_workouts"));
            // if (result.next()) {
            //     System.out.println(result.getString(1));

            // }
            // while (result.next()) {
            //     // System.out.println(result.getString(2));
            //     System.out.println(result.getString("json_workouts"));
            // }
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

// Select Json data:------------------------------------------------
// SELECT json_workouts
// FROM my_users, jsonb_array_elements(my_users.json_workouts) AS wk WHERE wk->>'name' = '1' AND username = 'b';

// Select Json data 2:------------------------------------------------  
// SELECT 
//   jsonb_agg(j) FILTER (WHERE j->>'name' = '2')
// FROM my_users t, jsonb_array_elements(json_workouts) j 
// WHERE username = 'b' GROUP BY t.json_workouts;

// Add json data-------------------------------------------------
//     UPDATE jsontesting SET jsondata = (
//     CASE
//         WHEN jsondata IS NULL THEN '[]'::JSONB
//         ELSE jsondata
//     END
// ) || '["newString"]'::JSONB WHERE id = 7;

    public boolean username_exists(Connection conn, String table_name, String username) {
        String query = String.format("SELECT username IN %s WHERE username='%s'", table_name, username); 
        try {
            Statement statement = conn.createStatement();
            statement.executeQuery(query);
            return true;
        } catch (Exception e) {
            return false;
            // System.out.println(e.getMessage());
        }
    }

    public void add_column(Connection conn, String column_name, String value_to_add) {
        String query = String.format("ALTER TABLE my_users ADD %s %s;", column_name, value_to_add);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Column successfuly added.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove_column(Connection conn, String column_name) {
        String query = String.format("ALTER TABLE my_users DROP %s;", column_name);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Column successfuly dropped.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String read_workout(Connection conn, String username) {
        String query = String.format("select json_workouts from my_users where username='%s'", username);
        try {
            Statement statement = conn.createStatement();
            ResultSet resSet = statement.executeQuery(query);
            while (resSet.next()) {
                return resSet.getString("json_workouts");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public void insert_workout(Connection conn, String value, String current_user) {
        String query = String.format("INSERT INTO my_uers (json_workouts) VALUES '%s' WHERE username = '%s';", value, current_user);
        try {
            Statement statement = conn.createStatement();
            statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void clear_json_column(Connection conn, String current_user) {
        String query = String.format("update my_users set json_workouts = null where username='%s';", current_user);
        try {
            Statement statmeent = conn.createStatement();
            statmeent.executeQuery(query);
            System.out.println("Json column has been cleared");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
