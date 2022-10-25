package com.klaro.fitnessappmaven;

import javax.swing.border.*;
import javax.swing.plaf.metal.OceanTheme;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.*;
import javax.swing.*;
import com.google.gson.*;

/**
 * AddWorkout
 */
public class AddWorkout extends JFrame implements ItemListener, ActionListener {
    JLabel workoutTitleLabel;
    JTextField workoutTitleInput;
    JPanel centerPanel, north, west, east, south, workoutScrollPanel, topPanel, midPanel, emptyPanel, bottomPanel;
    JButton button, button2, button3, addWorkout;
    WorkoutList workoList;
    JComboBox jCombo;
    Object selectedWorkoutType;
    HashMap<String, String> workoutHash = new HashMap<String, String>();
    // estabilish connection to DB
    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    CurrentUser currentUser = new CurrentUser();

    public AddWorkout() {
        workoList = new WorkoutList(); // workout icons
        // init. label(s)
        workoutTitleLabel = new JLabel("Workout's title", SwingConstants.CENTER);
        // init. entry(es)
        workoutTitleInput = new JTextField();
        workoutTitleInput.setPreferredSize(new Dimension(150, 30));
        // init. buttons

        // init. panels
        workoutScrollPanel = create_workoutScrollPanel();
        centerPanel = create_center_panel();
        north = new JPanel();
        west = new JPanel();
        east = new JPanel();
        south = new JPanel();

        north.setPreferredSize(new Dimension(50, 0));
        west.setPreferredSize(new Dimension(100, 0));
        east.setPreferredSize(new Dimension(100, 0));
        south.setPreferredSize(new Dimension(50, 0));

        // functions
        setup(); // call setup - sets up the basics for this.JFrame
        create_center_panel();
        add_comp_to_main_frame();
        SwingUtilities.updateComponentTreeUI(this); // force refresh page to make everything visible right away.
    }

    public JPanel create_workoutScrollPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 5, 5));
        button = new JButton();
        button = setWorkoutButtonIcon(workoList.WORKOUT_PIC_1, button);

        panel.add(button);

        button2 = new JButton();
        button2 = setWorkoutButtonIcon(workoList.WORKOUT_PIC_2, button2);
        panel.add(button2);

        button3 = new JButton();
        button3 = setWorkoutButtonIcon(workoList.WORKOUT_PIC_3, button3);
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

    public void go_back_to_homesite() throws IOException {
        this.dispose();
        new HomeSite();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == jCombo) {
            if (jCombo.getSelectedItem().equals("Please select")) {
            } else {
                selectedWorkoutType = jCombo.getSelectedItem();
                System.out.println(selectedWorkoutType);
                workoutHash.put("type", String.valueOf(selectedWorkoutType));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            workoutHash.put("path", String.valueOf(button.getIcon()));
        }
        if (e.getSource() == button2) {
            workoutHash.put("path", String.valueOf(button2.getIcon()));
        }
        if (e.getSource() == button3) {
            workoutHash.put("path", String.valueOf(button3.getIcon()));
        }
        if (e.getSource() == addWorkout) {
            String workoutName = workoutTitleInput.getText();
            workoutHash.put("name", workoutName);

            String myGson = to_gson(workoutHash);
            System.out.println(myGson);
            try {
                db.add_workout(conn, myGson, currentUser.get_current_user());
                JOptionPane.showMessageDialog(this, "Workout added successfuly!");
                go_back_to_homesite();
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }

    }

    public String to_gson(HashMap myHash) {
        Gson gson = new Gson();
        return gson.toJson(myHash);
    }
}
