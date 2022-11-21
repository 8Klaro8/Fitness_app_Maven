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

public class Settings extends JFrame implements java.awt.event.ActionListener {
    JLabel firstName, lastName, username, email;
    JTextField firstNameField, lastNameField, usernameField, emailField;
    JButton save, back;
    JPanel centerPanel, westPanel, eastPanel, northPanel, southPanel, saveButtonPanel;
    String newFirstName, newLastName, newUsername, newEmail;

    ConnectToDB db;
    Connection conn;

    CurrentUser currUser;
    String currentUsername;

    // constr.
    public Settings() throws IOException {
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
        centerPanel.add(firstNameField);
        centerPanel.add(lastName);
        centerPanel.add(lastNameField);
        centerPanel.add(username);
        centerPanel.add(usernameField);
        centerPanel.add(email);
        centerPanel.add(emailField);
    }

    private void initComponents() {
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

    public void initLabels() {
        firstName = new JLabel("First name", SwingConstants.CENTER);
        lastName = new JLabel("Last name", SwingConstants.CENTER);
        username = new JLabel("Username", SwingConstants.CENTER);
        email = new JLabel("Email", SwingConstants.CENTER);
    }

    public void initTextFields() {
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        usernameField = new JTextField();
        emailField = new JTextField();
    }

    public void initButtons() {
        save = new JButton("Save");
        // set action
        save.addActionListener(this);
        back = new JButton("<Back");
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
        // set 'saveButtnPanel'
        saveButtonPanel.setBorder(new EmptyBorder(new Insets(5, 0, 0, 0)));
        // add save to panel
        saveButtonPanel.add(save);
        // set center panel layout
        centerPanel.setLayout(new GridLayout(8, 1, 10, 10));
        // set north panel layout
        setNorthPanel();
        // set 'southPanel'
        southPanel.add(saveButtonPanel);
        // set pref. size
        northPanel.setPreferredSize(new Dimension(0, 30));
        southPanel.setPreferredSize(new Dimension(0, 60));
        westPanel.setPreferredSize(new Dimension(30, 0));
        eastPanel.setPreferredSize(new Dimension(30, 0));

    }

    private void setNorthPanel() {
        northPanel.setLayout(new GridLayout(1, 4, 10, 10));
        // add comp. to 'northPanel'
        for (int i = 0; i < 3; i++) {
            northPanel.add(new JPanel());
        }
        northPanel.add(back);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == save) {
            System.out.println("Save pressed");
            // get values
            newFirstName = firstNameField.getText();
            newLastName = lastNameField.getText();
            newUsername = usernameField.getText();
            newEmail = emailField.getText();
            // check if username exists
            boolean usernameExists = db.username_exists(conn, newUsername);
            if (usernameExists) {
                JOptionPane.showMessageDialog(this, "This username\nalready exists.");
                return;
            }
            // check if empty
            if (!(newUsername.isEmpty())) {
                // assign new value and save it
                try {
                    
                    db.insert_username(conn, newUsername, currUser.get_current_user());
                    currUser.set_current_user(newUsername);
                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                }
                
                
            }
            if (!(newFirstName.isEmpty())) {
                try {
                    db.insert_firstName(conn, newFirstName, currUser.get_current_user());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            if (!(newLastName.isEmpty())) {
                try {
                    db.insert_lastName(conn, newLastName, currUser.get_current_user());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            if (!(newEmail.isEmpty())) {
                try {
                    db.insert_email(conn, newEmail, currUser.get_current_user());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            try {
                go_to_PersonalDetails();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
        else if(e.getSource() == back){
            try {
                go_to_PersonalDetails();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
        
    }

    public void go_to_PersonalDetails() throws IOException {
        this.dispose();
        new PersonalDetails();
    }
}
