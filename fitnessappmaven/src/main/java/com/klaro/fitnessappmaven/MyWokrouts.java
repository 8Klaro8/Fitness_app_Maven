package com.klaro.fitnessappmaven;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.synth.SynthSpinnerUI;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyWokrouts extends JFrame implements ActionListener {
    static final int COLUMNS = 3;
    JButton workoutButton;
    JButton backToHome;
    JButton addWorkout;
    JPanel scrollablePanel;
    JPanel topBar;
    JPanel centerPanel;
    JPanel scrollBar;
    final int BORDER_NUMBER = 10;
    public final String LOGO_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\Logo\\lgo.png";
    public final String WORKOUT_PIC_1 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\yoga.png";
    public final String WORKOUT_PIC_2 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\workout.png";
    public final String WORKOUT_PIC_3 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\fitness.png";

    public MyWokrouts() throws IOException {

        // set back button
        backToHome = new JButton("< Back");
        backToHome.setLayout(null);
        // this.scrollBar = createScrollPanel();
        this.setLayout(new BorderLayout()); // set the default layout to borderlayout

        // create panel bars
        this.topBar = createTopBar(); // create top bar which includes a back button
        this.centerPanel = createCentralPanel();
        this.scrollBar = createScrollPanel();

        // this.scrollBar.add(new JScrollPane(contentForScrollPane()));

        // set frame basics
        this.setTitle("MY IT app");
        this.setBounds(10, 10, 370, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        // set icon
        ImageIcon image = new ImageIcon(LOGO_PIC_PATH);
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.WHITE);

        MyWorkoutsFrame();
    }

    // top bar
    public JPanel createTopBar() {
        JPanel topBar = new JPanel();
        topBar.setBackground(Color.BLACK);
        return topBar;
    }

    // create central
    public JPanel createCentralPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.GREEN);
        return centerPanel;
    }

    // create scroll bar(left panel)
    public JPanel createScrollPanel() {
        JPanel scrollBar = new JPanel();
        scrollBar.setBackground(Color.BLACK);
        return scrollBar;
    }

    // scrollable workout panel
    public JPanel contentForScrollPane() {
        // TODO: Display prof. pics in 3 columns
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(BORDER_NUMBER, BORDER_NUMBER, BORDER_NUMBER, 25));
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
        };
        panel.setLayout(new GridLayout(workoutExamples.length, 1, 10, 10));

        for (int j = 0; j < workoutExamples.length; j++) {
            workoutButton = new JButton();
            // Set the current image(icon) to a JButton and adds it to panel
            workoutButton = setWorkoutButtonIcon(workoutExamples[j][3], workoutButton);
            // adds current button to the panel
            workoutButton.setText(workoutExamples[j][0]);
            panel.add(workoutButton);
        }
        return panel;
    }

    // setup button
    public void MyWorkoutsFrame() {
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    // leads to home page
    public void go_back_to_homesite() throws IOException {
        this.dispose();
        new HomeSite();
    }

    public JButton setWorkoutButtonIcon(String picPath, JButton button) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage, imageIcon.getDescription());
        button.setIcon(imageIcon);
        return button;
    }

    // Adds all components to 'container'
    public void addComponentsToContainer() {
        topBar.add(backToHome);
        centerPanel.add(addWorkout);
        // add JPanels to .this
        this.add(BorderLayout.NORTH, topBar);
        this.add(BorderLayout.EAST, centerPanel);
        this.add(BorderLayout.WEST, scrollBar);
    }

    // add action event
    public void addActionEvent() {
        backToHome.addActionListener(this);
    }

    // Sets the components size and location
    public void setLocationAndSize() {
        // backToHome.setSize(new Dimension(100, 50));
        // backToHome.setAlignmentX(topBar.RIGHT_ALIGNMENT);
        // addWorkout.setSize(new Dimension(10, 10));
        // addWorkout.setAlignmentX(centerPanel.RIGHT_ALIGNMENT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToHome) {
            try {
                go_back_to_homesite();
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
    }
}