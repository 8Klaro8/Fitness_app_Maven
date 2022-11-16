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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOError;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class InWorkout extends JFrame implements ActionListener, java.awt.event.ActionListener, MouseInputListener {
    JPanel panelTop, panelRight, panelBottom, panelLeft, panelCenter;
    JButton backButton, editWorkoutName, editWorkoutType, done, done2;
    JLabel workoutName, workoutType;
    JTextField workoutNameTextField, workoutTypeTextField;
    String path, name, type, currentWorkoutName, currentWorkoutType;

    ArrayList<String> chosenArrayList;

    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    CurrentUser currUser = new CurrentUser();

    public InWorkout(String clickedButton) {
        backButton = new JButton();
        editWorkoutName = new JButton("Edit Workout title");
        editWorkoutName
                .setFont(new Font(editWorkoutName.getFont().getName(), editWorkoutName.getFont().getStyle(), 20));
        editWorkoutType = new JButton("Edit Workout type");
        editWorkoutType
                .setFont(new Font(editWorkoutType.getFont().getName(), editWorkoutType.getFont().getStyle(), 20));
        done = new JButton("Done!");
        done.setFont(new Font(done.getFont().getName(), done.getFont().getStyle(), 20));
        done2 = new JButton("Done!");
        done2.setFont(new Font(done2.getFont().getName(), done2.getFont().getStyle(), 20));

        // add actionlistener
        editWorkoutName.addActionListener(this);
        editWorkoutType.addActionListener(this);
        done.addActionListener(this);
        done2.addActionListener(this);

        backButton.setText("<Back");
        backButton.addActionListener(this);

        panelTop = new JPanel();
        panelRight = new JPanel();
        panelRight.setPreferredSize(new Dimension(70, 0));
        panelBottom = new JPanel();
        panelLeft = new JPanel();
        panelLeft.setPreferredSize(new Dimension(70, 0));
        panelCenter = new JPanel();
        setCenterPanel(clickedButton);
        setTopPanel(panelTop);

        // Set center panel
        setup();
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
        panelCenter.setLayout(new GridLayout(5, 3, 10, 10)); // set center panel's layout
        // GridBagConstraints gbc = new GridBagConstraints();
        // gbc.fill = GridBagConstraints.BOTH;
        // gbc.weighty = 1.0;
        // gbc.anchor = GridBagConstraints.PAGE_START;
        // gbc.insets = new Insets(10, 0, 0, 0);
        // gbc.gridx = 1;

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

    private void add_comp_to_centerPanel(String path) {
        panelCenter.add(setWorkoutButtonIcon(path, new JButton()));
        panelCenter.add(workoutName);
        panelCenter.add(editWorkoutName);
        panelCenter.add(workoutType);
        panelCenter.add(editWorkoutType);
    }

    public JButton setWorkoutButtonIcon(String picPath, JButton button) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
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

            // append all components
            panelCenter.add(setWorkoutButtonIcon(path, new JButton()));
            panelCenter.add(workoutNameTextField);
            panelCenter.add(done);
            panelCenter.add(workoutType);
            panelCenter.add(editWorkoutType);

            panelCenter.revalidate();
            panelCenter.repaint();
        }

        else if (e.getSource() == editWorkoutType) {
            currentWorkoutType = workoutType.getText();
            workoutTypeTextField = new JTextField(currentWorkoutType);
            // Remove comps.
            panelCenter.removeAll();
            // append all components
            panelCenter.add(setWorkoutButtonIcon(path, new JButton()));
            panelCenter.add(workoutName);
            panelCenter.add(editWorkoutName);
            panelCenter.add(workoutTypeTextField);
            panelCenter.add(done2);

            panelCenter.revalidate();
            panelCenter.repaint();
        } else if (e.getSource() == done) {
            String newWorkoutName = workoutNameTextField.getText();
            workoutName.setText(newWorkoutName);

            panelCenter.removeAll();
            // append all components
            panelCenter.add(setWorkoutButtonIcon(path, new JButton()));
            panelCenter.add(workoutName);
            panelCenter.add(editWorkoutName);
            panelCenter.add(workoutType);
            panelCenter.add(editWorkoutType);

            panelCenter.revalidate();
            panelCenter.repaint();

            // TODO update workout name in db
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

            panelCenter.removeAll();
            // append all components
            panelCenter.add(setWorkoutButtonIcon(path, new JButton()));
            panelCenter.add(workoutName);
            panelCenter.add(editWorkoutName);
            panelCenter.add(workoutType);
            panelCenter.add(editWorkoutType);

            panelCenter.revalidate();
            panelCenter.repaint();

            try {
                update_data_in_db(newWorkoutType, currentWorkoutType, 2);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
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
        // use switch to switch between type and name
        switch (indexNum) {
            case 1:
                chosenArrayList = new ArrayList<>(allWName);
                update_old_data(newWorkoutData, currentWorkoutData, allWName, allWType, allWPath, 1);
                break;
            case 2:
                chosenArrayList = new ArrayList<>(allWType);
                update_old_data(newWorkoutData, currentWorkoutData, allWName, allWType, allWPath, 2);
                break;
            default:
                break;
        }
    }

    private void update_old_data(String newWorkoutData, String currentWorkoutData, ArrayList<String> allWName,
            ArrayList<String> allWType, ArrayList<String> allWPath, int indexNum) throws IOException {
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
                    break;
                case 2:
                    db.add_workout_name(conn, allWName.get(i), currUser.get_current_user());
                    db.add_workout_type(conn, chosenArrayList.get(i), currUser.get_current_user());
                    db.add_workout_path(conn, allWPath.get(i), currUser.get_current_user());
                    break;
                default:
                    break;
            }

        }
    }

    // TODO wrote it for edit type as well
    private void update_data_in_db_name(String newWorkoutName) throws IOException {
        ArrayList<String> allWName = separate_collect_workout_datas(
                db.read_all_workout_name(conn, currUser.get_current_user()));
        ArrayList<String> allWType = separate_collect_workout_datas(
                db.read_all_workout_type(conn, currUser.get_current_user()));
        ArrayList<String> allWPath = separate_collect_workout_datas(
                db.read_all_workout_path(conn, currUser.get_current_user()));

        for (int i = 0; i < allWName.size(); i++) {
            if (allWName.get(i).equalsIgnoreCase(currentWorkoutName)) {
                allWName.remove(i);
                allWName.add(i, newWorkoutName);
            }
        }
        db.remove_all_workout_data(conn, currUser.get_current_user());
        for (int i = 0; i < allWName.size(); i++) {
            db.add_workout_name(conn, allWName.get(i), currUser.get_current_user());
            db.add_workout_type(conn, allWType.get(i), currUser.get_current_user());
            db.add_workout_path(conn, allWPath.get(i), currUser.get_current_user());
        }
    }

    private void update_data_in_db_type(String newWorkoutType) throws IOException {
        ArrayList<String> allWName = separate_collect_workout_datas(
                db.read_all_workout_name(conn, currUser.get_current_user()));
        ArrayList<String> allWType = separate_collect_workout_datas(
                db.read_all_workout_type(conn, currUser.get_current_user()));
        ArrayList<String> allWPath = separate_collect_workout_datas(
                db.read_all_workout_path(conn, currUser.get_current_user()));

        for (int i = 0; i < allWType.size(); i++) {
            if (allWType.get(i).equalsIgnoreCase(currentWorkoutType)) {
                allWType.remove(i);
                allWType.add(i, newWorkoutType);
            }
        }
        db.remove_all_workout_data(conn, currUser.get_current_user());
        for (int i = 0; i < allWName.size(); i++) {
            db.add_workout_name(conn, allWName.get(i), currUser.get_current_user());
            db.add_workout_type(conn, allWType.get(i), currUser.get_current_user());
            db.add_workout_path(conn, allWPath.get(i), currUser.get_current_user());
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
