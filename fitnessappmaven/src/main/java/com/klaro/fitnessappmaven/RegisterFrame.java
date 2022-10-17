package com.klaro.fitnessappmaven;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;

public class RegisterFrame extends JFrame implements ActionListener, LoginFormInterFace {
    // initialize container
    Container container;
    HashPassword hashPW;

    // labels & buttons
    JLabel userLabel;
    JLabel passwordLabel;
    JLabel passwordLabelRep;
    JTextField userTextfield;
    JPasswordField passwordTextfield;
    JPasswordField passwordTextfieldRep;
    JButton registerButton;
    JButton backToLogin;
    JCheckBox showPassword;
    final String BASE_PROF_PIC = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\ProfilePics\\basic_prif_pic.png";
    public final String LOGO_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\Logo\\lgo.png";
    CurrentUser currUserMethod = new CurrentUser();

    RegisterFrame() {
        hashPW = new HashPassword();
        container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));

        // this.frame settngs
        this.setTitle("My IT app");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 500);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // labels & buttons
        userLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        passwordLabelRep = new JLabel("Repeat Password");
        userTextfield = new JTextField();
        passwordTextfield = new JPasswordField();
        passwordTextfieldRep = new JPasswordField();
        registerButton = new JButton("Register");
        backToLogin = new JButton("Back to Login");
        showPassword = new JCheckBox("Show Password");

        // set icon
        ImageIcon image = new ImageIcon(LOGO_PIC_PATH);
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.WHITE);
        RegisterFrame();
    }

    public void RegisterFrame() {
        setLayoutManager();
        this.setLocationAndSize();
        this.addComponentsToContainer();
        this.addActionEvent();
    }

    // sets the layout to null - NOT USED CURRENLTY
    public void setLayoutManager() {
        this.container.setLayout(null);
    }

    // Sets the components size and location
    public void setLocationAndSize() {
        userLabel.setSize(new Dimension(100, 50));
        userLabel.setLocation(145, 30);
        passwordLabel.setSize(new Dimension(100, 50));
        passwordLabel.setLocation(145, 90);
        passwordLabelRep.setSize(new Dimension(200, 50));
        passwordLabelRep.setLocation(122, 150);
        userTextfield.setSize(new Dimension(150, 30));
        userTextfield.setLocation(100, 70);
        passwordTextfield.setSize(new Dimension(150, 30));
        passwordTextfield.setLocation(100, 130);
        passwordTextfieldRep.setSize(new Dimension(150, 30));
        passwordTextfieldRep.setLocation(100, 190);
        showPassword.setSize(new Dimension(150, 30));
        showPassword.setLocation(95, 230);
        registerButton.setSize(new Dimension(100, 30));
        registerButton.setLocation(125, 280);
        backToLogin.setSize(new Dimension(150, 30));
        backToLogin.setLocation(100, 320);

    }

    // Adds all components to 'container'
    public void addComponentsToContainer() {
        this.container.add(userLabel);
        this.container.add(passwordLabel);
        this.container.add(passwordLabelRep);
        this.container.add(userTextfield);
        this.container.add(passwordTextfield);
        this.container.add(passwordTextfieldRep);
        this.container.add(registerButton);
        this.container.add(backToLogin);
        this.container.add(showPassword);
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
                        db.add_user(conn, username, hashedPW, "null", "null", BASE_PROF_PIC);
                        // Login freshly registered user
                        currUserMethod.set_current_user(username);
                        go_back_to_homesite();
                        // TODO - show message only if user added indeed.
                        JOptionPane.showMessageDialog(this, "User: " + username + " has been registered!");
                    }
                    else{
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
