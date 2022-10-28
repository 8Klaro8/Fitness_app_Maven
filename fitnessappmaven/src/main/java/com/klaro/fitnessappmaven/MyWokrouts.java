package com.klaro.fitnessappmaven;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.*;

import com.github.underscore.Json;
import com.github.underscore.Json.JsonObject;
import com.github.underscore.Json.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MyWokrouts
 */
// TODO - make horizotal scrolling vanish

public class MyWokrouts extends JFrame implements ActionListener {
    // init. button(s) and panel(s)
    JButton button, back, test, workoutButton, addWorkout, removeWorkout;
    JPanel panelTop, panelBottom, panelRight, panelCenter, panelScroll;

    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    CurrentUser currUser = new CurrentUser();

    // paths
    public final String WORKOUT_PIC_1 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\yoga.png";
    public final String WORKOUT_PIC_2 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\workout.png";
    public final String WORKOUT_PIC_3 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\fitness.png";

    public MyWokrouts() throws IOException {
        // init. buttons
        button = new JButton("Love");
        back = new JButton("<Back");
        addWorkout = new JButton("Add Workout");
        removeWorkout = new JButton("Remove Workout");
        // initialize panels
        panelTop = new JPanel();
        panelBottom = new JPanel();
        panelRight = new JPanel();
        panelCenter = new JPanel();
        panelScroll = create_scroll_panel();
        panelTop.setPreferredSize(new Dimension(200, 20));
        // functions
        setup(); // call setup - sets up the basics for this.JFrame
        // set_panel_color(); // set color of panels
        set_center_panel();
        set_top_panel(); // set panelTop: add back button to it and align it to right
        set_location(); // set size and locations of elements
        addActionEvent(); // adds action(s) to buttons
        SwingUtilities.updateComponentTreeUI(this); // force refresh page to make everything visible right away.
    }

    public void set_center_panel() {
        JPanel adjustPanel = new JPanel();
        adjustPanel.setLayout(new GridLayout(8, 1, 10, 10));
        adjustPanel.add(new JPanel());
        adjustPanel.add(addWorkout);
        adjustPanel.add(removeWorkout);
        panelCenter.add(BorderLayout.CENTER, adjustPanel);
    }

    private void set_top_panel() {
        panelTop.setLayout(new GridLayout(1, 3, 10, 10));
        for (int i = 0; i < 3; i++) {
            JPanel pan = new JPanel();
            panelTop.add(pan);
        }
        panelTop.add(back);
        panelTop.setPreferredSize(new Dimension(100, 30));
    }

    public JPanel create_scroll_panel() throws IOException {
        JPanel panelScroll = new JPanel();
        // panelScroll.setBorder(new EmptyBorder(new Insets(10,10,10,20)));
        panelScroll.setBorder(new EmptyBorder(10, 10, 10, 25));

        // Read workout form DB based on current user
        String workouts = db.read_workout(conn, currUser.get_current_user()); // get workouts from DB
        int workoutsLength = workouts.length(); // length of workouts
        String[][] workoutArray = new String[workoutsLength][3]; // initialize array to contain workouts with the length
                                                                 // of workouts and fixed size of 3
        // append workouts to String array
        workouts = workouts.substring(1, (workouts.length() - 1)); // remove brackets
        // split each worouts separately

        ArrayList<String> workoutSeparations = new ArrayList<String>(); // arraylist to contain separated workouts
        String currentSubs = ""; // current substring

        // String testString = "{\"suti\": \"27\"}, {\"hal\": \"14\"}, {\"malac\": \"64\"}";
        workoutSeparations = separate_workout(workoutSeparations, currentSubs, workouts);
        System.out.println("WOKROUTSEP: " + workoutSeparations);

        // workout loop - as many times as many workouts given
        ArrayList<String> pairCollections = new ArrayList<String>();
        ArrayList<HashMap> workoutCollection = new ArrayList<HashMap>(); // collection of workout HashMaps
        HashMap<String, String> currentWorkout = new HashMap<>(); // ready key value pairs
        String currentPair = "";
        for (int i = 0; i < workoutSeparations.size(); i++) { // number of workouts
            currentPair = create_keyValue_pairs(workoutSeparations, pairCollections, currentPair, i);
            pairCollections.add(currentPair);
            currentPair = "";
            // read workout to add to UI, then clear ArrayList before add next workout.
            for (String pair : pairCollections) {
                String[] entry = pair.split(":");
                currentWorkout.put(entry[0].trim(), entry[1].trim());
            }
            System.out.println("currentWorkout Map: " + currentWorkout);
            workoutCollection.add(currentWorkout);
        }
        // loop thru workout HashMaps and get out: path


        String[][] workoutExamples = new String[][] {
                { "Workout 1", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 2", "Rep", "Kcal", WORKOUT_PIC_2 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_3 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
        };
        panelScroll.setLayout(new GridLayout(workoutExamples.length, 1, 10, 10));
        for (int i = 0; i < workoutExamples.length; i++) {
            workoutButton = new JButton();
            workoutButton = setWorkoutButtonIcon(workoutExamples[i][3], workoutButton);
            workoutButton.setText(workoutExamples[i][0]);
            panelScroll.add(workoutButton);
        }
        return panelScroll;
    }

    private String create_keyValue_pairs(ArrayList<String> workoutSeparations, ArrayList<String> pairCollections, String currentPair, int i) {
        for (int k = 0; k < workoutSeparations.get(i).length(); k++) {
            if (workoutSeparations.get(i).charAt(k) == ',') {
                // add key, value pare to pairCollections
                pairCollections.add(currentPair);
                currentPair = "";
            }
            else{
                currentPair += String.valueOf(workoutSeparations.get(i).charAt(k));
            }
        }
        return currentPair;
    }

    private ArrayList separate_workout(ArrayList<String> workoutSeparations, String currentSubs, String testString) {
        int beginIndex = 0; // begin index to of the substing
        int endIndex = 0;// end index to of the substing
        for (int i = 0; i < testString.length(); i++) {
            if (testString.charAt(i) == '{') {
                beginIndex = i + 1;
            } else if (testString.charAt(i) == '}') {
                endIndex = (i);
                currentSubs = testString.substring(beginIndex, endIndex);
                workoutSeparations.add(currentSubs);
                currentSubs = "";
            } else {
                currentSubs += String.valueOf(testString.charAt(i));
            }
        }
        return workoutSeparations;
    }

    public void setup() {
        // this Frame
        this.setTitle("My IT App");
        this.setLayout(new BorderLayout());
        this.setBounds(10, 10, 370, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void set_location() {
        this.add(BorderLayout.NORTH, panelTop);
        this.add(BorderLayout.SOUTH, panelBottom);
        this.add(BorderLayout.EAST, panelRight);
        this.add(BorderLayout.CENTER, panelCenter);
        this.add(BorderLayout.WEST, new JScrollPane(panelScroll));
        this.add(BorderLayout.NORTH, panelTop);
    }

    public void set_panel_color() {
        panelTop.setBackground(Color.GREEN);
        panelBottom.setBackground(Color.BLACK);
        panelRight.setBackground(Color.RED);
        panelCenter.setBackground(Color.PINK);
    }

    public JButton setWorkoutButtonIcon(String picPath, JButton button) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage, imageIcon.getDescription());
        button.setIcon(imageIcon);
        return button;
    }

    public void go_back_to_homesite() throws IOException {
        this.dispose();
        new HomeSite();
    }

    public void go_to_addworkout() {
        this.dispose();
        new AddWorkout();
    }

    // add action event
    public void addActionEvent() {
        back.addActionListener(this);
        addWorkout.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            try {
                go_back_to_homesite();// leads to home page
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == addWorkout) {
            go_to_addworkout();
        }
    }
}