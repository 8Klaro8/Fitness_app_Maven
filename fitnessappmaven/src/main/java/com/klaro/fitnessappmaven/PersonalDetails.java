package com.klaro.fitnessappmaven;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

public class PersonalDetails extends JFrame implements java.awt.event.ActionListener {
    JLabel firstName, lastName, username, email;
    JTextField firstNameField, lastNameField, usernameField, emailField;
    JButton changePersonalDetail, back;
    JPanel centerPanel, westPanel, eastPanel, northPanel, southPanel, saveButtonPanel;
    String newFirstName, newLastName, newUsername, newEmail;

    ConnectToDB db;
    Connection conn;

    CurrentUser currUser;
    String currentUsername;
    ArrayList<String> chosenUser;

    // constr.
    public PersonalDetails() throws IOException {
        // DB
        db = new ConnectToDB();
        conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
        // user
        currUser = new CurrentUser();
        currentUsername = currUser.get_current_user();
        // setup
        setup();
        // init comp.
        initComponents();
        // set panel
        setPanels();
        // add comp to 'centerPanel'
        addToCenterPanel();
        // ad to frame
        this.add(BorderLayout.CENTER, centerPanel);
        this.add(BorderLayout.NORTH, northPanel);
        this.add(BorderLayout.WEST, westPanel);
        this.add(BorderLayout.EAST, eastPanel);
        this.add(BorderLayout.SOUTH, southPanel);
    }

    private void addToCenterPanel() {
        centerPanel.add(firstName);
        // centerPanel.add(firstNameField);
        centerPanel.add(lastName);
        // centerPanel.add(lastNameField);
        centerPanel.add(username);
        // centerPanel.add(usernameField);
        centerPanel.add(email);
        // centerPanel.add(emailField);
    }

    private void initComponents() throws IOException {
        initLabels();
        initButtons();
        initTextFields();
        initPanels();
    }

    public void setup() {
        this.setTitle("My IT app");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 500);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
    }

    public void initLabels() throws IOException {
        // get current user
        chosenUser = db.get_by_name(conn, currUser.get_current_user());
        System.out.println(chosenUser);
        firstName = new JLabel("<html>First name:&emsp;&emsp;" + chosenUser.get(4) + "</html", SwingConstants.LEFT);
        lastName = new JLabel("<html>Last name:&emsp;&emsp;" + chosenUser.get(5) + "</html", SwingConstants.LEFT);
        username = new JLabel("<html>Username:&emsp;&emsp;" + chosenUser.get(2) + "</html", SwingConstants.LEFT);
        email = new JLabel("<html>Email:&emsp;&emsp;" + chosenUser.get(chosenUser.size() - 1) + "</html",
                SwingConstants.LEFT);
    }

    public void initTextFields() {
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        usernameField = new JTextField();
        emailField = new JTextField();
    }

    public void initButtons() {
        // init. button
        changePersonalDetail = new JButton("Change Details");
        back = new JButton("<Back");
        // set action
        changePersonalDetail.addActionListener(this);
        back.addActionListener(this);
    }

    public void initPanels() {
        centerPanel = new JPanel();
        westPanel = new JPanel();
        eastPanel = new JPanel();
        northPanel = new JPanel();
        southPanel = new JPanel();
        saveButtonPanel = new JPanel();
    }

    public void setPanels() {
        // set center panel layout
        centerPanel.setLayout(new GridLayout(8, 1, 10, 10));
        centerPanel.setBorder(new EmptyBorder(new Insets(50, 0, 0, 0)));
        // set north panel layout
        setNorthPanel();
        // set pref. size
        northPanel.setPreferredSize(new Dimension(0, 50));
        southPanel.setPreferredSize(new Dimension(0, 60));
        westPanel.setPreferredSize(new Dimension(100, 0));
        eastPanel.setPreferredSize(new Dimension(30, 0));

    }

    private void setNorthPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        northPanel.setLayout(new GridBagLayout());
        // add comp. to 'northPanel'
        gbc.fill = gbc.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.weightx = 0.1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        northPanel.add(changePersonalDetail, gbc);
        // append empty JPanel
        for (int i = 0; i < 2; i++) {
            JPanel tempPanel = new JPanel();
            gbc.insets = new Insets(10, 0, 0, 0);
            gbc.weightx = 1;
            gbc.gridx = 2 + i;
            gbc.gridy = 1;
            northPanel.add(tempPanel, gbc);
        }
        // add back button
        gbc.fill = gbc.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.weightx = .8;
        gbc.gridx = 5;
        gbc.gridy = 1;
        northPanel.add(back, gbc);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == back) {
            try {
                go_to_HomeSite();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
        else if(e.getSource() == changePersonalDetail){
            try {
                go_to_Settings();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    public void go_to_HomeSite() throws IOException {
        this.dispose();
        new HomeSite();
    }

    public void go_to_Settings() throws IOException {
        this.dispose();
        new Settings();
    }
}
