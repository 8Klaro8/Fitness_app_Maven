package com.klaro.fitnessappmaven;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.*;
import javax.swing.event.MouseInputListener;

import org.json.simple.JSONObject;
import org.w3c.dom.events.MouseEvent;

import com.github.underscore.Json;
import com.github.underscore.Json.JsonObject;
import com.github.underscore.Json.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * MyWokrouts
 */
// TODO - make horizotal scrolling vanish

public class MyWokrouts extends JFrame implements ActionListener, MouseInputListener {
    String buttonToDelete = "";// temp solution for preserving selected workout - for deletion
    // init. button(s) and panel(s)
    JButton button, backButton, test, workoutButton, addWorkout, removeWorkout;
    JPanel panelTop, panelBottom, panelRight, panelCenter, panelScroll;
    // connect and get user
    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    CurrentUser currUser = new CurrentUser();
    // paths
    public final String WORKOUT_PIC_1 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\yoga.png";
    public final String WORKOUT_PIC_2 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\workout.png";
    public final String WORKOUT_PIC_3 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\fitness.png";
    String allWorkoutName = db.read_all_workout_name(conn, currUser.get_current_user());
    Gson myGson = new Gson();// Gson

    public MyWokrouts() throws IOException {
        // init. buttons
        init_buttons();
        // add action listener
        removeWorkout.addActionListener(this);
        // initialize panels
        init_panels();
        panelTop.setPreferredSize(new Dimension(200, 20));
        // functions
        setup(); // call setup - sets up the basics for this.JFrame
        if_no_workout_then_redirect();// checking if there is any workout
        // set_panel_color(); // set color of panels
        set_center_panel();
        set_top_panel(); // set panelTop: add back button to it and align it to right
        set_location(); // set size and locations of elements
        addActionEvent(); // adds action(s) to buttons
        refresh();
    }

    private void if_no_workout_then_redirect() {
        if (allWorkoutName.length() < 3) {
            JOptionPane.showMessageDialog(this, "No workout added yet");
            go_to_addworkout();
        }
    }

    private void init_panels() throws IOException {
        panelTop = new JPanel();
        panelBottom = new JPanel();
        panelRight = new JPanel();
        panelCenter = new JPanel();
        panelScroll = create_scroll_panel();
    }

    private void init_buttons() {
        button = new JButton("Love");
        backButton = new JButton("<Back");
        addWorkout = new JButton("Add Workout");
        removeWorkout = new JButton("Remove Workout");
    }

    public void set_center_panel() {
        JPanel adjustPanel = new JPanel();
        adjustPanel.setLayout(new GridLayout(8, 1, 10, 10));
        adjustPanel.add(new JPanel());
        adjustPanel.add(addWorkout);
        adjustPanel.add(removeWorkout);
        panelCenter.add(BorderLayout.CENTER, adjustPanel);
    }

    private void set_top_panel() {
        panelTop.setLayout(new GridLayout(1, 3, 10, 10));
        for (int i = 0; i < 3; i++) {
            JPanel pan = new JPanel();
            panelTop.add(pan);
        }
        panelTop.add(backButton);
        panelTop.setPreferredSize(new Dimension(100, 30));
    }

    public JPanel create_scroll_panel() throws IOException {
        JPanel panelScroll = new JPanel();
        // set layout
        panelScroll.setLayout(new GridLayout(allWorkoutName.length(), 1, 10, 10));
        // read workout datas - name,type,path
        ArrayList<String> collectedWorkoutNames = separate_collect_workout_datas(db.read_all_workout_name(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutTypes = separate_collect_workout_datas(db.read_all_workout_type(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutPaths = separate_collect_workout_datas(db.read_all_workout_path(conn, currUser.get_current_user()));
        JButton currWorkoutButton;

        create_workout_buttons(panelScroll, collectedWorkoutNames, collectedWorkoutPaths);
        return panelScroll;
    }

    private void create_workout_buttons(JPanel panelScroll, ArrayList<String> collectedWorkoutNames,
            ArrayList<String> collectedWorkoutPaths) {
        JButton currWorkoutButton;
        for (int i = 0; i < collectedWorkoutNames.size(); i++) {
            currWorkoutButton = new JButton(collectedWorkoutNames.get(i));
            currWorkoutButton = setWorkoutButtonIcon(collectedWorkoutPaths.get(i), currWorkoutButton);
            currWorkoutButton.addActionListener(buttonAction);
            panelScroll.add(currWorkoutButton);
        }
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

    public void setup() {
        // this Frame
        this.setTitle("My IT App");
        this.setLayout(new BorderLayout());
        this.setBounds(10, 10, 370, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void set_location() {
        this.add(BorderLayout.NORTH, panelTop);
        this.add(BorderLayout.SOUTH, panelBottom);
        this.add(BorderLayout.EAST, panelRight);
        this.add(BorderLayout.CENTER, panelCenter);
        this.add(BorderLayout.WEST, new JScrollPane(panelScroll));
        this.add(BorderLayout.NORTH, panelTop);
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

    public void go_to_addworkout() {
        this.dispose();
        new AddWorkout();
    }

    public void refresh() throws IOException {
        this.revalidate();
        this.repaint();
    }

    // add action event
    public void addActionEvent() {
        backButton.addActionListener(this);
        addWorkout.addActionListener(this);
    }

    public void go_to_clicked_workout() {
        this.dispose();
        new InWorkout(buttonToDelete);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            try {
                go_back_to_homesite();// leads to home page
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == addWorkout) {
            go_to_addworkout();
        }else if (e.getSource() == removeWorkout) {
            try {
                delete_selected_workout();
                this.dispose();
                new MyWokrouts();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    private void delete_selected_workout() throws IOException {
        // get the index of the chosen workout by it's name
        ArrayList<String> collectedWorkoutNames = separate_collect_workout_datas(db.read_all_workout_name(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutTypes = separate_collect_workout_datas(db.read_all_workout_type(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutPaths = separate_collect_workout_datas(db.read_all_workout_path(conn, currUser.get_current_user()));
        for (int i = 0; i < collectedWorkoutNames.size(); i++) {
            if (collectedWorkoutNames.get(i).equals(buttonToDelete)) {
                System.out.println("Index found!");
                // get workout type and path from db by index
                String typeToDelete = collectedWorkoutTypes.get(i);
                String pathToDelete = collectedWorkoutPaths.get(i);
                // delete items from ArrayList
                for (int j = 0; j < collectedWorkoutNames.size(); j++) {
                    if (collectedWorkoutNames.get(i).equals(buttonToDelete)) {
                        collectedWorkoutNames.remove(i);
                        collectedWorkoutTypes.remove(i);
                        collectedWorkoutPaths.remove(i);
                    }
                    break;
                }
                // delete all from db before appending new value
                db.remove_all_workout_data(conn, currUser.get_current_user());
                // append values back to db
                for (int j = 0; j < collectedWorkoutNames.size(); j++) {
                    db.add_workout_name(conn, collectedWorkoutNames.get(j), currUser.get_current_user());
                    db.add_workout_type(conn, collectedWorkoutTypes.get(j), currUser.get_current_user());
                    db.add_workout_path(conn, collectedWorkoutPaths.get(j), currUser.get_current_user());
                }

            }
        }
    }

    Action buttonAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonToDelete = e.getActionCommand(); // set 'buttonToDelete' equal to the clicked button's name
            System.out.println("Button clikced: " + buttonToDelete);
            go_to_clicked_workout();
        }
    };

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 2) {
            
        }

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }
}