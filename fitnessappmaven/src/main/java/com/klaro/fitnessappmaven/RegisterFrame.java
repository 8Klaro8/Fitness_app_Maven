package com.klaro.fitnessappmaven;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
// TODO DONT let register with username that already exists
public class RegisterFrame extends JFrame implements ActionListener, LoginFormInterFace {
    // initialize container
    Container container;
    HashPassword hashPW;
    // labels & buttons
    JLabel userLabel, passwordLabel, passwordLabelRep, weightLabel, heightLabel;
    JPanel mainPanel, registerDataPanel, perosnalInfoPanel, north, south, east, west, showPassPanel, buttonsPanel;
    JTextField userTextfield, weight, height;
    JPasswordField passwordTextfield, passwordTextfieldRep;
    JButton registerButton, backToLogin;
    JCheckBox showPassword;
    final String BASE_PROF_PIC = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\ProfilePics\\basic_prif_pic.png";
    public final String LOGO_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\Logo\\lgo.png";
    CurrentUser currUserMethod = new CurrentUser();

    RegisterFrame() {
        hashPW = new HashPassword();
        RegisterFrame();
        setupFrame();
        // set icon
        setIcon();
    }

    private void addComponents() {
        registerDataPanel.add(userTextfield);
        registerDataPanel.add(passwordLabel);
        registerDataPanel.add(passwordTextfield);
        registerDataPanel.add(passwordLabelRep);
        registerDataPanel.add(passwordTextfieldRep);
        // add show pass panel
        showPassPanel.add(showPassword);
        this.add(showPassPanel);
        // add to personal panel
        perosnalInfoPanel.add(heightLabel);
        perosnalInfoPanel.add(height);
        perosnalInfoPanel.add(weightLabel);
        perosnalInfoPanel.add(weight);
        // ad to buttons panel
        buttonsPanel.add(registerButton);
        buttonsPanel.add(backToLogin);
        // add to 'mainPanel'
        mainPanel.add(registerDataPanel);
        mainPanel.add(showPassword);
        mainPanel.add(perosnalInfoPanel);
        mainPanel.add(buttonsPanel);

        this.add(BorderLayout.EAST, east);
        this.add(BorderLayout.WEST, west);
        this.add(BorderLayout.SOUTH, south);
        this.add(BorderLayout.NORTH, north);
        this.add(BorderLayout.CENTER, mainPanel);
    }

    private void setLayouts() {
        this.setLayout(new BorderLayout());
        mainPanel.setLayout(new GridLayout(4, 1));
        registerDataPanel.setLayout(new GridLayout(3, 2, 10, 10));
        perosnalInfoPanel.setLayout(new GridLayout(3, 2, 10, 10));
        showPassPanel.setLayout(new GridLayout(1, 3, 10, 10));
    }

    private void setIcon() {
        ImageIcon image = new ImageIcon(LOGO_PIC_PATH);
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.WHITE);
    }

    private void initComponents() {
        userLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        passwordLabelRep = new JLabel("Repeat Password");
        userTextfield = new JTextField();
        passwordTextfield = new JPasswordField();
        passwordTextfieldRep = new JPasswordField();
        registerButton = new JButton("Register");
        backToLogin = new JButton("Back to Login");
        showPassword = new JCheckBox("Show Password");
        weight = new JTextField();
        height = new JTextField();
        heightLabel = new JLabel("Height");
        weightLabel = new JLabel("Weight");
        mainPanel = new JPanel();
        registerDataPanel = new JPanel();
        perosnalInfoPanel = new JPanel();
        north = new JPanel();
        south = new JPanel();
        east = new JPanel();
        west = new JPanel();
        // set panels size
        east.setPreferredSize(new Dimension(70, 0));
        west.setPreferredSize(new Dimension(70, 0));
        showPassPanel = new JPanel();
        buttonsPanel = new JPanel();

    }

    private void setupFrame() {
        this.setTitle("My IT app");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 500);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    public void RegisterFrame() {
        // init buttons
        initComponents();
        // set layout
        setLayouts();
        // add to register panel
        registerDataPanel.add(userLabel);
        addComponents();
        this.addActionEvent();
    }

    // add action event
    public void addActionEvent() {
        registerButton.addActionListener(this);
        showPassword.addActionListener(this);
        backToLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            char[] pwd_1 = passwordTextfield.getPassword();
            char[] pwd_2 = passwordTextfieldRep.getPassword();
            String pwd_string_1 = String.valueOf(pwd_1);
            String pwd_string_2 = String.valueOf(pwd_2);
            if (!(pwd_string_1.equals(pwd_string_2))) {
                JOptionPane.showMessageDialog(this, "Password does not match!");
                passwordTextfield.setText("");
                passwordTextfieldRep.setText("");
            } else {
                String username = userTextfield.getText();
                if (username.isEmpty() || pwd_string_1.isEmpty() || pwd_string_2.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username and Password fields can't be empty.");
                    return;
                }
                if (height.getText().isEmpty() || weight.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please give a height and weight.");
                    return;
                }
                // Hash password
                try {
                    // Basic profile image
                    String hashedPW = hashPW.generateStorngPasswordHash(pwd_string_1);
                    // Store user
                    ConnectToDB db = new ConnectToDB();
                    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
                    // control if username exists in DB
                    boolean usernameExists = db.username_exists(conn, "my_users", username);
                    if (!(usernameExists)) {
                        db.add_user(conn, username, hashedPW, ", ", ", ", BASE_PROF_PIC, height.getText(), weight.getText(), ", ");

                        // Login freshly registered user
                        currUserMethod.set_current_user(username);
                        // Add start value to new user
                        db.insert_basic_values(conn, currUserMethod.get_current_user());
                        go_back_to_homesite();
                        // TODO - show message only if user added indeed.
                        JOptionPane.showMessageDialog(this, "User: " + username + " has been registered!");
                    } else {
                        JOptionPane.showMessageDialog(this, "The username: " + username + " already exists.");
                    }

                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
        } else if (e.getSource() == backToLogin) {
            go_to_login_page();
        } else if (e.getSource() == showPassword) {
            ShowPassword(showPassword);
        }
    }

    // Show password
    @Override
    public void ShowPassword(JCheckBox showPassword) {
        if (showPassword.isSelected()) {
            passwordTextfield.setEchoChar((char) 0);
            passwordTextfieldRep.setEchoChar((char) 0);
        } else {
            passwordTextfield.setEchoChar('*');
            passwordTextfieldRep.setEchoChar('*');
        }
    }
    // Page changing
    public void go_to_login_page() {
        this.dispose();
        new MyFrame();
    }

    public void go_back_to_homesite() throws IOException {
        this.dispose();
        new HomeSite();
    }
}
