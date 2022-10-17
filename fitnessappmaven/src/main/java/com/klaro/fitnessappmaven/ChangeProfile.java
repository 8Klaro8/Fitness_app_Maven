package com.klaro.fitnessappmaven;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeProfile extends JFrame implements ActionListener {
    static final int COLUMNS = 3;
    JButton profPicButton;
    public final String USER_FILE_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\current_user\\current_user.txt";
    public final String LOGO_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\Logo\\lgo.png";
    public static final String PROF_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\ProfilePics";
    JPanel panel;

    public ChangeProfile() throws IOException {

        this.setTitle("MY IT app");
        this.setLayout(new BorderLayout());
        this.panel = createPanel();
        this.add(BorderLayout.CENTER, new JScrollPane(panel));
        this.setBounds(10, 10, 370, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        // set icon
        ImageIcon image = new ImageIcon(LOGO_PIC_PATH);
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.WHITE);
    }

    public JPanel createPanel() {
        // TODO: Display prof. pics in 3 columns
        JPanel panel = new JPanel();
        // getting all prof. pic path
        ArrayList<String> all_prof_path = get_all_prof_pic_path();
        Object[] objAllPathArray = all_prof_path.toArray();
        String[] strAllPathArray = new String[objAllPathArray.length];
        int numOfPics = strAllPathArray.length;
        for (int i = 0; i < objAllPathArray.length; i++) {
            strAllPathArray[i] = String.valueOf(objAllPathArray[i]);
        }
        panel.setLayout(new GridLayout(numOfPics, COLUMNS, 10, 10));
        for (int i = 0; i < numOfPics; i++) {
            for (int j = 0; j < numOfPics; j++) {
                profPicButton = new JButton();
                String lastElement = strAllPathArray[strAllPathArray.length - 1];
                String currElement = strAllPathArray[j];
                if (lastElement.equals(currElement)) {
                    return panel;
                }
                // Set the current image(icon) to a JButton and adds it to panel
                profPicButton = set_prof_pic_images(strAllPathArray[j], profPicButton);
                // sets icons path as action command (just like a parameter)
                profPicButton.setActionCommand("" + profPicButton.getIcon());
                // adds action listener
                profPicButton.addActionListener(numberAction);
                // adds current button to the panel
                panel.add(profPicButton);
            }
        }
        return panel;
    }

    // sets the right image to the button as icon
    public JButton set_prof_pic_images(String picPath, JButton button) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage, imageIcon.getDescription());
        button.setIcon(imageIcon);
        return button;
    }

    // Loop thru profile pics and gets their paths and returns it
    public static ArrayList<String> get_all_prof_pic_path() {
        ArrayList<String> allProfilePicPath = new ArrayList<String>();
        File dir = new File(PROF_PIC_PATH);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                allProfilePicPath.add(child.getPath());
            }
            return allProfilePicPath;
        } else {
            return allProfilePicPath;
        }
    }

    // inherited this function because must inherit but it is not used. Instead userd new action created: 'numberAction'
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    // gets the currnetly logged in user
    public String get_current_user() throws IOException {
        Path fileName = Path.of(USER_FILE_PATH);
        return Files.readString(fileName);
    }

    Action numberAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // gets profile pics path from set actioncommand
            String profPicPath = e.getActionCommand();
            ConnectToDB db = new ConnectToDB();
            Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
            try {
                db.set_prof_pic_path(conn, "my_users", profPicPath, get_current_user());
                go_back_to_homesite();
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
    };

    // leads to home page
    public void go_back_to_homesite() throws IOException {
        this.dispose();
        new HomeSite();
    }

}
