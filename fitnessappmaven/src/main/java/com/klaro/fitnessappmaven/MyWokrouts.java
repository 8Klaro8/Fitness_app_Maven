package com.klaro.fitnessappmaven;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MyWokrouts
 */
// TODO - make horizotal scrolling vanish

public class MyWokrouts extends JFrame implements ActionListener, MouseInputListener {
    String selectedButton = "";// temp solution for preserving selected workout - for deletion
    float allCalorieBurnedLastWeek, allCalorieBurnedToday;
    // init. button(s), label(s) and panel(s)
    JButton button, backButton, test, workoutButton, addWorkout, removeWorkout;
    JPanel panelTop, panelBottom, panelRight, panelCenter, panelScroll;
    JLabel caloriesBurnedLastWeek, caloriesBurnedLastWeekNum, caloriesBurnedToday, caloriesBurnedTodayNum;
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
        // init. labels
        init_labels();
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

    public void init_labels() {
        caloriesBurnedLastWeek = new JLabel();
        caloriesBurnedLastWeekNum = new JLabel();
        caloriesBurnedToday = new JLabel();
        caloriesBurnedTodayNum = new JLabel();
    }

    private void if_no_workout_then_redirect() throws IOException {
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
        // set 'caloriesBurnedLastWeek' text
        caloriesBurnedLastWeek.setText("Calories Burned Last 7 Days:");
        caloriesBurnedToday.setText("Calories burned today:");
        caloriesBurnedLastWeek.setHorizontalAlignment(SwingConstants.CENTER);
        caloriesBurnedToday.setHorizontalAlignment(SwingConstants.CENTER);
        caloriesBurnedLastWeek.setBorder(new EmptyBorder(new Insets(30,0,0,0)));
        // set/ get 'caloriesBurnedLastWeekNum'
        get_last_week_calorie();
        get_todays_calorie();
        // add calories burned label
        adjustPanel.add(caloriesBurnedLastWeek);
        adjustPanel.add(caloriesBurnedLastWeekNum);
        adjustPanel.add(caloriesBurnedToday);
        adjustPanel.add(caloriesBurnedTodayNum);
        panelCenter.add(BorderLayout.CENTER, adjustPanel);
    }

    public void get_last_week_calorie() {
        // get 'todaysDate'
        String todaysDateString = get_todays_date();
        // inti 'todaysDate'
        Date todaysDate = dateToSimpleDate(todaysDateString);
        // init 'allCalorieBurnedLastWeek'
        allCalorieBurnedLastWeek = 0;
        try {
            String allWorkoutDate = db.read_get_all_date(conn, currUser.get_current_user());
            String allWorkoutBurnedCalorie = db.read_all_workout_burned_calorie(conn, currUser.get_current_user());
            ArrayList<String> allWorkoutDateSep = separate_collect_workout_datas(allWorkoutDate);
            ArrayList<String> allWorkoutBurnedCalorieSep = separate_collect_workout_datas(allWorkoutBurnedCalorie);
            for (int i = 0; i < allWorkoutDateSep.size(); i++) {
                // get current date
                Date currDate = dateToSimpleDate(allWorkoutDateSep.get(i));
                // get timedifference in miliseconds
                long timeDiff = todaysDate.getTime() - currDate.getTime();
                // transform miliseconds to days
                long dayDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
                // checking if date is smaller than today but not older than a week
                if (dayDiff < 7) { // if workouts date is no longer than 7 days then get calorie
                    // Read other saved datas and get calorie by index
                    allCalorieBurnedLastWeek += Float.valueOf(allWorkoutBurnedCalorieSep.get(i));
                }
            }
            // set caloriedBuned font properties
            setCaloriesBurnedText();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void get_todays_calorie() {
        // get 'todaysDate'
        String todaysDateString = get_todays_date();
        // inti 'todaysDate'
        Date todaysDate = dateToSimpleDate(todaysDateString);
        // init 'allCalorieBurnedLastWeek'
        allCalorieBurnedToday = 0;
        try {
            String allWorkoutDate = db.read_get_all_date(conn, currUser.get_current_user());
            String allWorkoutBurnedCalorie = db.read_all_workout_burned_calorie(conn, currUser.get_current_user());
            ArrayList<String> allWorkoutDateSep = separate_collect_workout_datas(allWorkoutDate);
            ArrayList<String> allWorkoutBurnedCalorieSep = separate_collect_workout_datas(allWorkoutBurnedCalorie);
            for (int i = 0; i < allWorkoutDateSep.size(); i++) {
                // get current date
                Date currDate = dateToSimpleDate(allWorkoutDateSep.get(i));
                // get timedifference in miliseconds
                long timeDiff = todaysDate.getTime() - currDate.getTime();
                // transform miliseconds to days
                long dayDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
                // checking if date is smaller than today but not older than a week
                if (!(dayDiff > 0)) { // if workouts date is no longer than 7 days then get calorie
                    // Read other saved datas and get calorie by index
                    allCalorieBurnedToday += Float.valueOf(allWorkoutBurnedCalorieSep.get(i));
                }
            }
            // set caloriedBuned font properties
            setCaloriesBurnedTodayText();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setCaloriesBurnedTodayText() {
        caloriesBurnedTodayNum.setText(String.valueOf(allCalorieBurnedToday));
        caloriesBurnedTodayNum.setHorizontalAlignment(SwingConstants.CENTER);
        caloriesBurnedTodayNum.setFont(new Font(caloriesBurnedTodayNum.getFont().getName(), caloriesBurnedTodayNum.getFont().getStyle(), 20));
    }

    private void setCaloriesBurnedText() {
        caloriesBurnedLastWeekNum.setText(String.valueOf(allCalorieBurnedLastWeek));
        caloriesBurnedLastWeekNum.setHorizontalAlignment(SwingConstants.CENTER);
        caloriesBurnedLastWeekNum.setFont(new Font(caloriesBurnedLastWeekNum.getFont().getName(), caloriesBurnedLastWeekNum.getFont().getStyle(), 20));
    }

    public Date dateToSimpleDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int getTimeDifference(Date date1, Date date2) {
        return (int) (date1.getTime() - date2.getTime());
    }

    public String get_todays_date() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public Instant stringDateToDate(String inputDate) {
        return Instant.parse(inputDate);
    }

    public void get_time_stamp_from_db() {

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
        ArrayList<String> collectedWorkoutNames = separate_collect_workout_datas(
                db.read_all_workout_name(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutTypes = separate_collect_workout_datas(
                db.read_all_workout_type(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutPaths = separate_collect_workout_datas(
                db.read_all_workout_path(conn, currUser.get_current_user()));
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
            currWorkoutButton.addMouseListener(this);
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

    public void go_to_addworkout() throws IOException {
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
        // new InWorkout(selectedButton);
        new TestInWorkout(selectedButton);
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
            try {
                go_to_addworkout();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if (e.getSource() == removeWorkout) {
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
        ArrayList<String> collectedWorkoutNames = separate_collect_workout_datas(
                db.read_all_workout_name(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutTypes = separate_collect_workout_datas(
                db.read_all_workout_type(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutPaths = separate_collect_workout_datas(
                db.read_all_workout_path(conn, currUser.get_current_user()));
        ArrayList<String> collectedWorkoutdurations = separate_collect_workout_datas(
                db.read_all_workout_duration(conn, currUser.get_current_user()));

        for (int i = 0; i < collectedWorkoutNames.size(); i++) {
            if (collectedWorkoutNames.get(i).equals(selectedButton)) {
                System.out.println("Index found!");
                // get workout type and path from db by index
                String typeToDelete = collectedWorkoutTypes.get(i);
                String pathToDelete = collectedWorkoutPaths.get(i);
                String durationToDelete = collectedWorkoutdurations.get(i);
                // delete items from ArrayList
                for (int j = 0; j < collectedWorkoutNames.size(); j++) {
                    if (collectedWorkoutNames.get(i).equals(selectedButton)) {
                        collectedWorkoutNames.remove(i);
                        collectedWorkoutTypes.remove(i);
                        collectedWorkoutPaths.remove(i);
                        collectedWorkoutdurations.remove(i);
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
                    db.add_workout_duration(conn, collectedWorkoutdurations.get(j), currUser.get_current_user());
                }

            }
        }
    }

    Action buttonAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedButton = e.getActionCommand(); // set 'buttonToDelete' equal to the clicked button's name
        }
    };

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 2) {
            System.out.println(e.getClickCount());
            go_to_clicked_workout();
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