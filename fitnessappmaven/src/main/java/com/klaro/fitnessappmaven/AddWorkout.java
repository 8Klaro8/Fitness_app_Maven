package com.klaro.fitnessappmaven;

import javax.swing.border.*;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.event.*;
import javax.swing.*;

import com.github.underscore.Json.ParseException;
import com.google.gson.*;

/**
 * AddWorkout
 */
public class AddWorkout extends JFrame implements ItemListener, ActionListener {
    JLabel workoutTitleLabel, timeWorkedOutLabel;
    JTextField workoutTitleInput, timeWorkedOutInput;
    JPanel centerPanel, north, west, east, south, workoutScrollPanel, topPanel, midPanel, emptyPanel, bottomPanel;
    JButton button, button2, button3, addWorkout, backButton;
    WorkoutList workoutList;
    JComboBox jCombo;
    Object selectedWorkoutType;
    HashMap<String, String> workoutHash = new HashMap<String, String>();
    // estabilish connection to DB
    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    CurrentUser currentUser = new CurrentUser();
    Gson myGson = new Gson();
    // workout datas to save
    String workoutType, workoutPath, workoutName;
    String currUsername = null;

    public AddWorkout() throws IOException {
        // init curr. user
        currUsername = currentUser.get_current_user();
        workoutList = new WorkoutList(); // workout icons
        // init. label(s)
        workoutTitleLabel = new JLabel("Workout's title", SwingConstants.CENTER);
        timeWorkedOutLabel = new JLabel("Workout duration", SwingConstants.CENTER);
        // init. entry(es)
        workoutTitleInput = new JTextField();
        workoutTitleInput.setPreferredSize(new Dimension(150, 30));
        timeWorkedOutInput = new JTextField();
        // init. buttons
        backButton = new JButton("<Back");
        // init. panels
        workoutScrollPanel = create_workoutScrollPanel();
        centerPanel = create_center_panel();
        north = new JPanel();
        west = new JPanel();
        east = new JPanel();
        south = new JPanel();

        north.setPreferredSize(new Dimension(50, 30));
        west.setPreferredSize(new Dimension(100, 0));
        east.setPreferredSize(new Dimension(100, 0));
        south.setPreferredSize(new Dimension(50, 50));

        // functions
        setup(); // call setup - sets up the basics for this.JFrame
        // create_center_panel();
        set_north_panel(north);
        add_comp_to_main_frame();
        SwingUtilities.updateComponentTreeUI(this); // force refresh page to make everything visible right away.
    }

    public void set_north_panel(JPanel north) {
        north.setLayout(new GridLayout(1, 4, 10, 10));
        for (int i = 0; i < 3; i++) {
            north.add(new JPanel());
        }
        backButton.addActionListener(this);
        north.add(backButton);
    }

    public JPanel create_workoutScrollPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 5, 10));
        button = new JButton();
        button = setWorkoutButtonIcon(workoutList.WORKOUT_PIC_1, button);

        panel.add(button);

        button2 = new JButton();
        button2 = setWorkoutButtonIcon(workoutList.WORKOUT_PIC_2, button2);
        panel.add(button2);

        button3 = new JButton();
        button3 = setWorkoutButtonIcon(workoutList.WORKOUT_PIC_3, button3);
        panel.add(button3);
        // add listeners to buttons
        button.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        return panel;
    }

    public JPanel create_center_panel() {
        // init. panels
        centerPanel = new JPanel();
        topPanel = new JPanel();
        midPanel = new JPanel();
        bottomPanel = new JPanel();
        addWorkout = new JButton("Add");
        addWorkout.addActionListener(this);
        String[] workoutTypes = { "Please select", "Gym", "Cardio", "Street" }; // types of workout to display in a
                                                                                // JComboBox
        jCombo = new JComboBox<>(workoutTypes); // init. JComboBox
        jCombo.addItemListener(this); // add itemListener to JComboBox
        // set panels layout
        centerPanel.setLayout(new GridLayout(3, 3, 10, 10));
        midPanel.setLayout(new GridLayout(1, 1, 10, 10));
        topPanel.setLayout(new GridLayout(5, 1, 10, 10));
        emptyPanel = new JPanel(); // creating a empry panel to push eleemnts downwards
        // add elements to 'topPanel'
        topPanel.add(emptyPanel);
        topPanel.add(workoutTitleLabel);
        topPanel.add(workoutTitleInput);
        topPanel.add(jCombo);
        midPanel.add(new JScrollPane(workoutScrollPanel));
        // set bottom panel layout
        bottomPanel.setLayout(new GridLayout(3,1,10,10));
        // add to bottom panel
        bottomPanel.add(timeWorkedOutLabel);
        bottomPanel.add(timeWorkedOutInput);

        bottomPanel.add(addWorkout);
        // add elements to 'centerPanel'
        centerPanel.add(topPanel);
        centerPanel.add(midPanel);
        centerPanel.add(bottomPanel);
        return centerPanel;
    }

    public void add_comp_to_main_frame() {
        this.add(BorderLayout.NORTH, north);
        this.add(BorderLayout.WEST, west);
        this.add(BorderLayout.EAST, east);
        this.add(BorderLayout.SOUTH, south);
        this.add(BorderLayout.CENTER, centerPanel);
    }

    public void setup() {
        // this Frame
        this.setLayout(new BorderLayout());
        this.setTitle("My IT App");
        this.setBounds(10, 10, 370, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public JButton setWorkoutButtonIcon(String picPath, JButton button) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage, imageIcon.getDescription());
        button.setIcon(imageIcon);
        return button;
    }

    public void go_back_to_my_workouts() throws IOException {
        this.dispose();
        new MyWokrouts();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == jCombo) {
            if (jCombo.getSelectedItem().equals("Please select")) {
                JOptionPane.showMessageDialog(this, "Please select a workout type.");
                return;
            } else {
                selectedWorkoutType = jCombo.getSelectedItem();
                workoutType = String.valueOf(selectedWorkoutType);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            workoutPath = String.valueOf(button.getIcon());
        }
        if (e.getSource() == button2) {
            workoutPath = String.valueOf(button2.getIcon());
        }
        if (e.getSource() == button3) {
            workoutPath = String.valueOf(button3.getIcon());
        }
        if (e.getSource() == addWorkout) {
            String workoutDurationText = timeWorkedOutInput.getText().strip();

            // TODO check if workout name contians forbidden characters, like: ''
            workoutName = workoutTitleInput.getText().strip(); // get workout's name/title

            // check if workout duration is numeric
            for (int i = 0; i < workoutDurationText.length(); i++) {
                if (Character.isDigit(workoutDurationText.charAt(i)) == false) {
                    JOptionPane.showMessageDialog(this, "Type only number!");
                    return;
                }
            }
            // get workout duration
            if (workoutDurationText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Type workout duration!");
                return;
            }
            // check if workout name already exists
            if (workout_name_exists()) {
                JOptionPane.showMessageDialog(this, "This workout name already exists!");
                return;
            }
            if (workoutName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please give a workout name.");
                return;
            }
            if (workoutPath == null) {
                JOptionPane.showMessageDialog(this, "Please choose a workout icon.");
                return;
            }
            if (workoutType == null) {
                JOptionPane.showMessageDialog(this, "Please choose a workout type.");
                return;
            } else {
                try {
                    db.add_workout_name(conn, workoutName, currUsername);
                    db.add_workout_type(conn, workoutType, currUsername);
                    db.add_workout_path(conn, workoutPath, currUsername);
                    db.add_workout_duration(conn, workoutDurationText, currUsername);
                    db.add_workout_date(conn, get_date_now(), currUsername);
                    // get users weight
                    String weight = db.get_weight_by_username(conn, currUsername);
                    db.add_workout_burned_calorie(conn, getCalorie(getResponse(weight, workoutDurationText)), currUsername);

                    JOptionPane.showMessageDialog(this, "Workout added Successfuly!");
                    go_back_to_my_workouts();
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
        }
        if (e.getSource() == backButton) {
            try {
                go_back_to_my_workouts();
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
    }

    public String to_gson(HashMap myHash) {
        Gson gson = new Gson();
        return gson.toJson(myHash);
    }

    private ArrayList<String> separate_collect_workout_datas(String inputed_method) {
        ArrayList<String> collectedWorkoutDatas = new ArrayList<String>();
        try {
            int begin = -1;
            int end = -1;
            int commaCounter = 0;
            String currentSubString = "";
            boolean beginAdded = false;
            for (int i = 0; i < inputed_method.length(); i++) {
                if (inputed_method.charAt(i) == ',') {
                    if (!(beginAdded)) {
                        begin = i + 1;
                        beginAdded = true;
                    }
                    commaCounter++;
                    if (commaCounter == 2) {
                        end = i;
                        currentSubString = inputed_method.substring(begin, end);
                        begin = i + 1;
                        commaCounter = 1;
                        collectedWorkoutDatas.add(currentSubString.trim());
                    }
                }
            }
            return collectedWorkoutDatas;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            return null;
        }

    }

    public boolean workout_name_exists() {
        try {
            ArrayList<String> allWorkoutName = separate_collect_workout_datas(
                    db.read_all_workout_name(conn, currUsername));
            for (String wName : allWorkoutName) {
                if (wName.equalsIgnoreCase(workoutName)) {
                    return true;
                }
            }
        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }
        return false;
    }

    private HttpRequest getResponse(String usersWeight, String currentDuration) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        String.format(
                                "https://fitness-calculator.p.rapidapi.com/burnedcalorie?activityid=co_2&activitymin=%s&weight=%s",
                                currentDuration, usersWeight)))
                .header("X-RapidAPI-Key", "b4b40d284amshacf7b676b928e88p1a5c77jsned0db45910bf")
                .header("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return request;
    }

    private String getCalorie(HttpRequest request) throws org.json.simple.parser.ParseException {
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser parser = new JSONParser();

            JSONObject json = (JSONObject) parser.parse(String.valueOf(response.body()));
            Object calorieJson = json.values().toArray()[1];
            JSONObject calorie = (JSONObject) parser.parse(String.valueOf(calorieJson));
            return String.valueOf(calorie.get("burnedCalorie"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String get_date_now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now =  LocalDateTime.now();
        return String.valueOf(dtf.format(now));
    }
}
