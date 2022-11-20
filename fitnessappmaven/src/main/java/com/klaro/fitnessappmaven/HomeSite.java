package com.klaro.fitnessappmaven;

import java.awt.Color;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.OptionPaneUI;
import javax.swing.plaf.metal.OceanTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.awt.image.BufferedImage;

public class HomeSite extends JFrame implements ActionListener, MouseInputListener {
    
    // initialize container
    Container container;
    JLabel profPicLabel, labelImage, settings;
    JButton logout, changeProfilePic, myWorkouts;
    SetProfileImage setProf = new SetProfileImage();
    public final String USER_FILE_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\current_user\\current_user.txt";
    public final String LOGO_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\Logo\\lgo.png";
    CurrentUser getCurrUser = new CurrentUser();
    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    String currentUser = getCurrUser.get_current_user();

    HomeSite() throws IOException {
        SwingUtilities.updateComponentTreeUI(this); // refresh page
        container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));

        // this.frame settngs
        setupFrame();

        // labels & buttons
        initAndSetLabelsAnButtons();
        String baseProfPic = db.get_prof_pic_path(conn, "my_users", currentUser);
        profPicLabel = setProf.setBasicProfPic(profPicLabel, baseProfPic);

        // set icon
        ImageIcon image = new ImageIcon(LOGO_PIC_PATH);
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.WHITE);

        HomeSiteFrame();
        SwingUtilities.updateComponentTreeUI(this); // refresh page
    }

    private void initAndSetLabelsAnButtons() {
        profPicLabel = new JLabel();
        settings = new JLabel();
        settings.setText("Settings");
        profPicLabel.setName("profPicLabel");
        profPicLabel.addMouseListener(this);
        logout = new JButton("Logout");
        changeProfilePic = new JButton("Change Profile");
        myWorkouts = new JButton("My Wokrouts");
    }

    private void setupFrame() {
        this.setTitle("My IT app");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 500);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    // Sets up login screen/ frame
    public void HomeSiteFrame() {
        this.setLayoutManager();
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
        profPicLabel.setSize(new Dimension(100, 100));
        profPicLabel.setLocation(135, 30);
        logout.setSize(new Dimension(80, 30));
        logout.setLocation(250, 20);
        changeProfilePic.setSize(new Dimension(120, 30));
        changeProfilePic.setLocation(125, 130);
        SwingUtilities.updateComponentTreeUI(this);
        myWorkouts.setSize(new Dimension(120, 30));
        myWorkouts.setLocation(125, 170);
    }

    // Adds all components to 'container'
    public void addComponentsToContainer() {
        this.container.add(profPicLabel);
        this.container.add(logout);
        this.container.add(changeProfilePic);
        this.container.add(myWorkouts);
    }

    public void logut_from_app() throws IOException {
        // re-writing current user to "" & logout
        LogoutFunction logoutFun = new LogoutFunction();
        logoutFun.logout_from_app(this);
    }

    // add action event
    public void addActionEvent() {
        logout.addActionListener(this);
        changeProfilePic.addActionListener(this);
        myWorkouts.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logout) {
            try {
                logut_from_app();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == changeProfilePic) {
            this.dispose();
            try {
                go_to_ChangeProfile();

            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == myWorkouts) {
            try {
                go_to_workouts();

            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
    }

    public void go_to_ChangeProfile() throws IOException {
        this.dispose();
        new ChangeProfile();
    }

    public void go_to_workouts() throws IOException {
        this.dispose();
        new MyWokrouts();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (e.getComponent().getName() == profPicLabel.getName()) {
                try {
                    go_to_ChangeProfile();
                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
}

/* Refresh window -> */
// SwingUtilities.updateComponentTreeUI(this);
