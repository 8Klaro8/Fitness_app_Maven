package com.klaro.fitnessappmaven;

import java.awt.Color;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.plaf.synth.SynthSpinnerUI;
import javax.swing.text.Style;
import netscape.javascript.JSObject;

public final class Main {
    private Main() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws IOException{
        HashMap<String, String> work_dict = new HashMap<String, String>(); 
        work_dict.put("name", "pull-up");
        work_dict.put("icon", "tempWorkoutIcons/workout.png");
        work_dict.put("rep", "8");
        work_dict.put("set", "12");
        work_dict.put("time", "60");

        // // HashMap to JSON
        // GSon myGson = new Gson();

        // GetCurrentUser getUser = new GetCurrentUser();
        // String current_user = getUser.get_current_user();

        // new MyWokrouts();
        // new ChangeProfile();
        new MyFrame();
        // new HomeSite();
        // ConnectToDB db = new ConnectToDB();
        // Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
        // db.add_workout(conn, "my_users", work_dict, current_user);

        // System.getenv("PASSWORD"));
        // db.create_account_table(conn, "my_users");
        // db.insert_row(conn, "employee", "employee_2", "Jani street 40.");
        // db.get_by_name(conn, "employee", "employee_3");
        // db.delete_row(conn, "employee", 2);
        // db.read_data(conn, "employee");
        // db.update_name(conn, "employee", "employee_1", "employee_8");
    }
}
