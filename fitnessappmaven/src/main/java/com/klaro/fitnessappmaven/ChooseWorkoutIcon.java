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

public class ChooseWorkoutIcon extends JFrame implements ActionListener {
    static final int COLUMNS = 3;
    JButton profPicButton;
    public final String USER_FILE_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\current_user\\current_user.txt";
    public final String LOGO_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\Logo\\lgo.png";
    public static final String PROF_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons";
    JPanel panel;
    String iconPathToReplace, workoutName;
    // DB
    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));

    CurrentUser currUser = new CurrentUser();
    String currentUsername = currUser.get_current_user();

    public ChooseWorkoutIcon(String workoutName) throws IOException {
        this.workoutName = workoutName;
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

                // Set the current image(icon) to a JButton and adds it to panel
                profPicButton = set_prof_pic_images(strAllPathArray[j], profPicButton);
                // sets icons path as action command (just like a parameter)
                profPicButton.setActionCommand("" + profPicButton.getIcon());
                // adds action listener
                profPicButton.addActionListener(numberAction);
                // adds current button to the panel
                panel.add(profPicButton);
                if (lastElement.equals(currElement)) {
                    return panel;
                }
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
            String iconPath = e.getActionCommand();
            try {
                // change the icon path of workout
                ArrayList<String> allWorkoutIconPathList = replace_icon_path_in_arraylist(iconPath); // replace icon path
                db.remove_all_icon_path(conn, currentUsername); // remove all data from path
                for (int j = 0; j < allWorkoutIconPathList.size(); j++) {// add all data from 'allWorkoutPathList'
                    db.add_workout_path(conn, allWorkoutIconPathList.get(j), currentUsername);
                }
                go_back_to_in_workout();
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }

        private ArrayList<String> replace_icon_path_in_arraylist(String iconPath) {
            String allWorkoutName = db.read_all_workout_name(conn, currentUsername);
            String allWorkoutIconPath = db.read_all_workout_path(conn, currentUsername);
            ArrayList<String> allWorkoutNameList = separate_collect_workout_datas(allWorkoutName);
            ArrayList<String> allWorkoutIconPathList = separate_collect_workout_datas(allWorkoutIconPath);
            for (int i = 0; i < allWorkoutNameList.size(); i++) {
                if (allWorkoutNameList.get(i).equals(workoutName)) {
                    // remove index workout icon path
                    allWorkoutIconPathList.remove(allWorkoutIconPathList.get(i));
                    // add new icon path TODO
                    allWorkoutIconPathList.add(iconPath);
                }
            }
            return allWorkoutIconPathList;
        }
    };

    // leads to home page
    public void go_back_to_homesite() throws IOException {
        this.dispose();
        new HomeSite();
    }

    public void go_back_to_in_workout() throws IOException {
        System.out.println(workoutName);
        this.dispose();
        new TestInWorkout(workoutName);
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

}
