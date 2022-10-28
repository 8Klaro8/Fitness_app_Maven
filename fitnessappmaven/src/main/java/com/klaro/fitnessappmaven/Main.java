package com.klaro.fitnessappmaven;

import com.google.gson.*;
import java.io.IOException;
import java.io.Reader;
import java.io.SerializablePermission;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class Main {
    private Main() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws IOException {

        // // // // //
        // ---------------------GSON------------------------------------------
        // HashMap<String, String> work_dict = new HashMap<String, String>();
        // work_dict.put("name", "pull-up");
        // work_dict.put("icon", "tempWorkoutIcons/workout.png");
        // work_dict.put("rep", "8");
        // work_dict.put("set", "12");
        // work_dict.put("time", "60");

        // System.out.println("Before GSON: \n" + work_dict);

        // Gson myGson = new Gson();
        // String workdict_json = myGson.toJson(work_dict);
        // System.out.println("JSON OBJ: \n" + workdict_json);

        // // JsonObject obj = new JsonObject();
        // // obj.addProperty("yes", "yes");
        // // obj.addProperty("maybe", "maybe");
        // // obj.addProperty("no", "no");
        // // System.out.println(obj);

        // HashMap fromJson = myGson.fromJson(workdict_json, HashMap.class);
        // System.out.println("\nFrom JSON to HashMap: \n" + fromJson);
        // // // // //
        // ---------------------GSON------------------------------------------

        
        // Gson myGson = new Gson();
        // GsonBuilder gsonMapBuilder = new GsonBuilder(); // HashMap to JSON
        // Gson gsonObject = gsonMapBuilder.create();

        // GetCurrentUser getUser = new GetCurrentUser();
        // String current_user = getUser.get_current_user();

        // new AddWorkout();
        new MyWokrouts();
        // new ChangeProfile();
        // new MyFrame();
        // new HomeSite();
        // ConnectToDB db = new ConnectToDB();
        // Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
        // db.add_column(conn, "json_workouts", "JSONB");

        // This is how to add and read
        // workout------------------------------------------------------
        // HashMap<String, String> value = new HashMap<String, String>();
        // value.put("1", "value1");
        // value.put("2", "value2");
        // value.put("3", "value3");

        // String toJsonValue = myGson.toJson(value);
        // db.add_workout(conn, toJsonValue, "y");
        // db.remove_column(conn, "json_workouts");

        // Read Json from DB--------------------------------------------
        // String jsonData = db.read_workout(conn, "y");
        // JsonElement jsonDataString = JsonParser.parseString(jsonData);
        // System.out.println(jsonDataString.getAsJsonArray().get(1).getAsJsonObject().get("1"));
        // This is how to add and read
        // workout------------------------------------------------------

        // db.remove_column(conn, "workouts");
        // db.add_column(conn, "workouts", "TEXT[][]");
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

// SET PATH=%PATH%:C:\Program Files\Amazon Corretto\jdk11.0.16_9\bin
// SET PATH=%PATH%:C:\Users\gergr\OneDrive\Dokumentumok\gson_jar
