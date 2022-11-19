package com.klaro.fitnessappmaven;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class TestInWorkout extends JFrame implements ActionListener, java.awt.event.ActionListener, MouseInputListener {
    JPanel panelTop, panelRight, panelBottom, panelLeft, panelCenter, centerPanelSeparator, centerTopPanel,
            centerBotPanel, caloriePanel, timeWorkedOutPanel;
    JButton backButton, editWorkoutName, editWorkoutType, done, done2;
    JLabel workoutName, workoutType, burnedCaloriesLabel, burnedCaloriesLabelNumber, timeWorkedOut, timeWorkedOutNum;
    JTextField workoutNameTextField, workoutTypeTextField;
    String path, name, type, currentWorkoutName, currentWorkoutType, clickedButton, calorie;
    ArrayList<String> chosenArrayList;
    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    CurrentUser currUser = new CurrentUser();

    public TestInWorkout(String clickedButton) {
        this.clickedButton = clickedButton;
        // init button and set font
        initButtons();
        // init labels
        timeWorkedOut = new JLabel();
        timeWorkedOutNum = new JLabel();
        burnedCaloriesLabel = new JLabel("Burned Calories", SwingConstants.CENTER);
        burnedCaloriesLabelNumber = new JLabel();

        // set fonts
        setFonts();
        // add actionlistener
        editWorkoutName.addActionListener(this);
        editWorkoutType.addActionListener(this);
        done.addActionListener(this);
        done2.addActionListener(this);
        // back button
        backButton.setText("<Back");
        backButton.addActionListener(this);
        // init panels
        initPanels();
        setCenterPanel(clickedButton);
        setTopPanel(panelTop);
        // set preffered size
        panelLeft.setPreferredSize(new Dimension(70, 0));
        panelRight.setPreferredSize(new Dimension(70, 0));
        // set layout
        centerPanelSeparator.setLayout(new GridLayout(2, 1, 10, 10));
        centerTopPanel.setLayout(new GridLayout(4, 1, 10, 10));
        centerBotPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Set center panel
        setup();
    }

    private void setFonts() {
        editWorkoutName
                .setFont(new Font(editWorkoutName.getFont().getName(), editWorkoutName.getFont().getStyle(), 20));
        editWorkoutType
                .setFont(new Font(editWorkoutType.getFont().getName(), editWorkoutType.getFont().getStyle(), 20));
        done.setFont(new Font(done.getFont().getName(), done.getFont().getStyle(), 20));
        done2.setFont(new Font(done2.getFont().getName(), done2.getFont().getStyle(), 20));
    }

    private void initButtons() {
        backButton = new JButton();
        done = new JButton("Done!");
        done2 = new JButton("Done!");
        editWorkoutName = new JButton("Edit Workout title");
        editWorkoutType = new JButton("Edit Workout type");
    }

    private void initPanels() {
        centerPanelSeparator = new JPanel();
        panelTop = new JPanel();
        panelRight = new JPanel();
        panelBottom = new JPanel();
        panelLeft = new JPanel();
        panelCenter = new JPanel();
        centerTopPanel = new JPanel();
        centerBotPanel = new JPanel();
        caloriePanel = new JPanel();
        timeWorkedOutPanel = new JPanel();
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
        setPanelsPosition();
    }

    public void setPanelsPosition() {
        this.add(BorderLayout.NORTH, panelTop);
        this.add(BorderLayout.EAST, panelRight);
        this.add(BorderLayout.SOUTH, panelBottom);
        this.add(BorderLayout.WEST, panelLeft);
        this.add(BorderLayout.CENTER, panelCenter);
    }

    public void setTopPanel(JPanel panelTop) {
        panelTop.setPreferredSize(new Dimension(150, 30));
        panelTop.setLayout(new GridLayout(1, 5, 10, 10));
        for (int i = 0; i < 3; i++) {
            panelTop.add(new JPanel());
        }
        panelTop.add(backButton);
    }

    public void setCenterPanel(String clickedButton) {
        panelCenter.setLayout(new GridLayout(2, 1, 10, 10)); // set center panel's layout
        // Get current workout by workout's name
        try {
            ArrayList<String> allWName = separate_collect_workout_datas(
                    db.read_all_workout_name(conn, currUser.get_current_user()));
            ArrayList<String> allWType = separate_collect_workout_datas(
                    db.read_all_workout_type(conn, currUser.get_current_user()));
            ArrayList<String> allWPath = separate_collect_workout_datas(
                    db.read_all_workout_path(conn, currUser.get_current_user()));
            for (int i = 0; i < allWName.size(); i++) {
                if (allWName.get(i).equalsIgnoreCase(clickedButton)) {
                    type = allWType.get(i);
                    path = allWPath.get(i);
                    // display 'name', 'type' and 'path'
                    workoutName = new JLabel(clickedButton, SwingConstants.CENTER);
                    workoutType = new JLabel(type, SwingConstants.CENTER);
                    workoutName
                            .setFont(new Font(workoutName.getFont().getName(), workoutName.getFont().getStyle(), 30));
                    workoutType
                            .setFont(new Font(workoutType.getFont().getName(), workoutType.getFont().getStyle(), 30));
                    // set name
                    workoutName.setName("workoutLabel");
                    // add mouse listener
                    workoutName.addMouseListener(this);

                    // when clicked, make it editable
                    add_comp_to_centerPanel(path);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void add_comp_to_centerPanel(String path) throws IOException {

        // add button
        // read user's weight
        String usersWeight = db.read_users_weight(conn, currUser.get_current_user());
        String currentDuration = null;
        // get duration of workout
        String stringAllWorkoutName = db.read_all_workout_name(conn, currUser.get_current_user());
        String stringAllWorkoutDuration = db.read_all_workout_duration(conn, currUser.get_current_user());
        ArrayList<String> arrayListAllWorkoutName = separate_collect_workout_datas(stringAllWorkoutName);
        ArrayList<String> arrayListAllWorkoutDuration = separate_collect_workout_datas(stringAllWorkoutDuration);

        for (int i = 0; i < arrayListAllWorkoutName.size(); i++) {
            if (arrayListAllWorkoutName.get(i).equals(clickedButton)) {
                currentDuration = arrayListAllWorkoutDuration.get(i);
            }
        }
        // read API
        try {
            HttpRequest response = getResponse(usersWeight, currentDuration);
            calorie = getCalorie(response);
            burnedCaloriesLabelNumber.setText(calorie);
            burnedCaloriesLabelNumber.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // add comp. to 'centerTopPanel'
        addCompToCenterTopPanel();
        // add calorie components to calorie panel
        addToCaloriePanel();
        // add to timeWorkedOutLabel
        addToTimeWorkedOutPanel();
        // add comp. to bot panel
        addToCenterBotPanel(path);
        // add sub panels to 'panelCenter'
        addToPanelCenter();
    }

    private void addToPanelCenter() {
        panelCenter.add(centerTopPanel);
        panelCenter.add(centerBotPanel);
    }

    private void addToTimeWorkedOutPanel() {
        timeWorkedOutPanel.setLayout(new GridLayout(1,2,10,10));
        timeWorkedOut.setText("Duration:");
        timeWorkedOut.setHorizontalAlignment(SwingConstants.CENTER);
        // get/ set workout duration
        timeWorkedOutNum.setText(getWorkoutDuration() + " min");
        timeWorkedOutNum.setHorizontalAlignment(SwingConstants.CENTER);
        timeWorkedOutPanel.add(timeWorkedOut);
        timeWorkedOutPanel.add(timeWorkedOutNum);
    }

    private String getWorkoutDuration() {
        try {
            String readWorkoutName = db.read_all_workout_name(conn, currUser.get_current_user());
            ArrayList<String> allWorkoutNameList = separate_collect_workout_datas(readWorkoutName);
            String readWorkoutDuration = db.read_all_workout_duration(conn, currUser.get_current_user());
            ArrayList<String> allWorkoutDurationList = separate_collect_workout_datas(readWorkoutDuration);
            for (int i = 0; i < allWorkoutNameList.size(); i++) {
                if (allWorkoutNameList.get(i).equals(workoutName.getText())) {
                    return allWorkoutDurationList.get(i);
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void addToCenterBotPanel(String path) {
        centerBotPanel.add(setWorkoutButtonIcon(path, new JButton()));
        centerBotPanel.add(caloriePanel);
        // add time worked out
        centerBotPanel.add(timeWorkedOutPanel);
    }

    private void addToCaloriePanel() {
        caloriePanel.setLayout(new GridLayout(1, 2, 10, 10));
        caloriePanel.add(burnedCaloriesLabel);
        caloriePanel.add(burnedCaloriesLabelNumber);
    }

    private void addCompToCenterTopPanel() {
        centerTopPanel.add(workoutName);
        centerTopPanel.add(editWorkoutName);
        centerTopPanel.add(workoutType);
        centerTopPanel.add(editWorkoutType);
    }

    private HttpRequest getResponse(String usersWeight, String currentDuration) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        String.format(
                                "https://fitness-calculator.p.rapidapi.com/burnedcalorie?activityid=co_2&activitymin=%s&weight=%s",
                                currentDuration, usersWeight)))
                .header("X-RapidAPI-Key", "b4b40d284amshacf7b676b928e88p1a5c77jsned0db45910bf")
                .header("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return request;
    }

    private String getCalorie(HttpRequest request) throws ParseException {
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser parser = new JSONParser();

            JSONObject json = (JSONObject) parser.parse(String.valueOf(response.body()));
            Object calorieJson = json.values().toArray()[1];
            JSONObject calorie = (JSONObject) parser.parse(String.valueOf(calorieJson));
            return String.valueOf(calorie.get("burnedCalorie"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public JButton setWorkoutButtonIcon(String picPath, JButton button) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage, imageIcon.getDescription());
        button.setIcon(imageIcon);
        return button;
    }

    @Override
    public void processAction(ActionEvent arg0) throws AbortProcessingException {
        // TODO Auto-generated method stub

    }

    public void go_my_workouts() {
        try {
            this.dispose();
            new MyWokrouts();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == backButton) {
            go_my_workouts();
        } else if (e.getSource() == editWorkoutName) {
            currentWorkoutName = workoutName.getText(); // get workout name
            workoutNameTextField = new JTextField(currentWorkoutName);
            // Remove comps.
            panelCenter.removeAll();
            centerBotPanel.removeAll();
            centerTopPanel.removeAll();

            // append top panel components
            centerTopPanel.add(workoutNameTextField);
            centerTopPanel.add(done);
            centerTopPanel.add(workoutType);
            centerTopPanel.add(editWorkoutType);
            // append bot panel components
            centerBotPanel.add(setWorkoutButtonIcon(path, new JButton()));
            centerBotPanel.add(caloriePanel);
            centerBotPanel.add(timeWorkedOutPanel);
            // add to 'panelCenter'
            panelCenter.add(centerTopPanel);
            panelCenter.add(centerBotPanel);
            refreshScreen();
        }

        else if (e.getSource() == editWorkoutType) {
            currentWorkoutType = workoutType.getText();
            workoutTypeTextField = new JTextField(currentWorkoutType);
            // Remove comps.
            panelCenter.removeAll();
            centerBotPanel.removeAll();
            centerTopPanel.removeAll();
            // append top panel components
            centerTopPanel.add(workoutName);
            centerTopPanel.add(editWorkoutName);
            centerTopPanel.add(workoutTypeTextField);
            centerTopPanel.add(done2);
            // append bot panel components
            centerBotPanel.add(setWorkoutButtonIcon(path, new JButton()));
            centerBotPanel.add(caloriePanel);
            centerBotPanel.add(timeWorkedOutPanel);
            // add to 'panelCenter'
            panelCenter.add(centerTopPanel);
            panelCenter.add(centerBotPanel);

            refreshScreen();
        } else if (e.getSource() == done) {
            String newWorkoutName = workoutNameTextField.getText();
            workoutName.setText(newWorkoutName);
            // Remove comps.
            panelCenter.removeAll();
            centerBotPanel.removeAll();
            centerTopPanel.removeAll();

            // append top panel components
            centerTopPanel.add(workoutName);
            centerTopPanel.add(editWorkoutName);
            centerTopPanel.add(workoutType);
            centerTopPanel.add(editWorkoutType);
            // append bot panel components
            centerBotPanel.add(setWorkoutButtonIcon(path, new JButton()));
            centerBotPanel.add(caloriePanel);
            centerBotPanel.add(timeWorkedOutPanel);
            // add to 'panelCenter'
            panelCenter.add(centerTopPanel);
            panelCenter.add(centerBotPanel);

            refreshScreen();
            try {
                update_data_in_db(newWorkoutName, currentWorkoutName, 1);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }

        else if (e.getSource() == done2) {
            String newWorkoutType = workoutTypeTextField.getText();
            // checking if new workout type is correct
            if (!(is_correct_type(newWorkoutType))) {
                return;
            }
            newWorkoutType = makeFirstLetterBig(newWorkoutType);
            workoutType.setText(newWorkoutType);
            // Remove comps.
            panelCenter.removeAll();
            centerBotPanel.removeAll();
            centerTopPanel.removeAll();
            // append top panel components
            centerTopPanel.add(workoutName);
            centerTopPanel.add(editWorkoutName);
            centerTopPanel.add(workoutType);
            centerTopPanel.add(editWorkoutType);
            // append bot panel components
            centerBotPanel.add(setWorkoutButtonIcon(path, new JButton()));
            centerBotPanel.add(caloriePanel);
            centerBotPanel.add(timeWorkedOutPanel);
            // add to 'panelCenter'
            panelCenter.add(centerTopPanel);
            panelCenter.add(centerBotPanel);

            refreshScreen();

            try {
                update_data_in_db(newWorkoutType, currentWorkoutType, 2);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private void refreshScreen() {
        panelCenter.revalidate();
        panelCenter.repaint();
    }

    private String makeFirstLetterBig(String newWorkoutType) {
        String bigLetter = newWorkoutType.substring(0, 1).toUpperCase();
        String rest = newWorkoutType.substring(1, newWorkoutType.length());
        newWorkoutType = bigLetter + rest;
        return newWorkoutType;
    }

    public boolean is_correct_type(String newWorkoutType) {
        if (!(newWorkoutType.equalsIgnoreCase("cardio") ||
                newWorkoutType.equalsIgnoreCase("gym") ||
                newWorkoutType.equalsIgnoreCase("street"))) {
            JOptionPane.showMessageDialog(this, "Please choose either:\n\t-Street\n\t-Gym\n\t-Cardio");
            return false;
        }
        return true;
    }

    public void update_data_in_db(String newWorkoutData, String currentWorkoutData, int indexNum) throws IOException {
        // Collect workout datas
        ArrayList<String> allWName = separate_collect_workout_datas(
                db.read_all_workout_name(conn, currUser.get_current_user()));
        ArrayList<String> allWType = separate_collect_workout_datas(
                db.read_all_workout_type(conn, currUser.get_current_user()));
        ArrayList<String> allWPath = separate_collect_workout_datas(
                db.read_all_workout_path(conn, currUser.get_current_user()));
        ArrayList<String> allDuration = separate_collect_workout_datas(
                db.read_all_workout_duration(conn, currUser.get_current_user()));
        // use switch to switch between type and name
        switch (indexNum) {
            case 1:
                chosenArrayList = new ArrayList<>(allWName);
                update_old_data(newWorkoutData, currentWorkoutData, allWName, allWType, allWPath, allDuration, 1);
                break;
            case 2:
                chosenArrayList = new ArrayList<>(allWType);
                update_old_data(newWorkoutData, currentWorkoutData, allWName, allWType, allWPath, allDuration, 2);
                break;
            default:
                break;
        }
    }

    private void update_old_data(String newWorkoutData, String currentWorkoutData, ArrayList<String> allWName,
            ArrayList<String> allWType, ArrayList<String> allWPath, ArrayList<String> allDuration, int indexNum) throws IOException {
        // If program finds the data we want to update, then it removes the old data and
        // appends the updated data.
        for (int i = 0; i < chosenArrayList.size(); i++) {
            if (chosenArrayList.get(i).equalsIgnoreCase(currentWorkoutData)) {
                chosenArrayList.remove(i);
                chosenArrayList.add(i, newWorkoutData);
            }
        }
        // Removes everything from db
        db.remove_all_workout_data(conn, currUser.get_current_user());
        // Re-appends everything to db with the updated ArrayLists
        for (int i = 0; i < chosenArrayList.size(); i++) {
            // based on the indexNum we choose the appropriate 'append'
            // structure.(Append/relace type or name)
            switch (indexNum) {
                case 1:
                    db.add_workout_name(conn, chosenArrayList.get(i), currUser.get_current_user());
                    db.add_workout_type(conn, allWType.get(i), currUser.get_current_user());
                    db.add_workout_path(conn, allWPath.get(i), currUser.get_current_user());
                    db.add_workout_duration(conn, allDuration.get(i), currUser.get_current_user());
                    break;
                case 2:
                    db.add_workout_name(conn, allWName.get(i), currUser.get_current_user());
                    db.add_workout_type(conn, chosenArrayList.get(i), currUser.get_current_user());
                    db.add_workout_path(conn, allWPath.get(i), currUser.get_current_user());
                    db.add_workout_duration(conn, allDuration.get(i), currUser.get_current_user());
                    break;
                default:
                    break;
            }

        }
    }

    // currently not used
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (e.getComponent().getName() == workoutName.getName()) {
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
