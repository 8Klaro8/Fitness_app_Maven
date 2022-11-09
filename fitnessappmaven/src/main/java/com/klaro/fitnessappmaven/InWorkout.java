package com.klaro.fitnessappmaven;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    String path, name, type;

    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    CurrentUser currUser = new CurrentUser();

    int elementCounter2;

    public InWorkout(String clickedButton, int elementCounter) {

        elementCounter2 = elementCounter;
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
            ArrayList<HashMap> workoutCollectionDelete;
            String jsonData = db.read_workout(conn, currUser.get_current_user());
            workoutCollectionDelete = prepare_workouts_by_selection(jsonData);

            for (HashMap hashMap : workoutCollectionDelete) {
                String formattedName = form_workout_name_from_db(hashMap); // remove '"' from 'name' value
                if (formattedName.equals(String.valueOf(clickedButton))) {
                    name = String.valueOf(hashMap.values().toArray()[0]);
                    type = String.valueOf(hashMap.values().toArray()[1]);
                    path = String.valueOf(hashMap.values().toArray()[2]);

                    name = cut_quotes(name);
                    type = cut_quotes(type);
                    path = cut_quotes(path);

                    // display 'name', 'type' and 'path'
                    workoutName = new JLabel(name, SwingConstants.CENTER);
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

                    System.out.println();
                }

            }
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

    private void add_comp_to_centerPanel(String path) {
        panelCenter.add(setWorkoutButtonIcon(path, new JButton()));
        panelCenter.add(workoutName);
        panelCenter.add(editWorkoutName);
        panelCenter.add(workoutType);
        panelCenter.add(editWorkoutType);
    }

    public String cut_quotes(String input) {
        return input.substring(1, input.length() - 1);
    }

    private String form_workout_name_from_db(HashMap hashMap) {
        String nameFromDB = String.valueOf(hashMap.values().iterator().next());
        return nameFromDB.substring(1, nameFromDB.length() - 1);
    }

    private ArrayList<HashMap> prepare_workouts_by_selection(String workouts) {
        ArrayList<String> workoutSeparations = new ArrayList<String>(); // arraylist to contain separated workouts
        String currentSubs = ""; // current substring
        workoutSeparations = separate_workout(workoutSeparations, currentSubs, workouts); // separates each workout

        // workout loop - as many times as many workouts given
        ArrayList<String> pairCollections = new ArrayList<String>();
        ArrayList<HashMap> workoutCollection = new ArrayList<HashMap>(); // collection of workout HashMaps

        HashMap<String, String> currentWorkout = new HashMap<>(); // ready key value pairs
        String currentPair = "";
        for (int i = 0; i < workoutSeparations.size(); i++) { // number of workouts
            currentPair = create_keyValue_pairs(workoutSeparations, pairCollections, currentPair, i);
            pairCollections.add(currentPair); // add current workout pair to 'pairCollections'
            currentPair = ""; // empty 'currentPair'
            create_workout_pairs(pairCollections, currentWorkout);// read workout to add to UI, then clear ArrayList
                                                                  // before add next workout.
            pairCollections.clear(); // clear pairCollections after addition
            workoutCollection.add(new HashMap<String, String>(currentWorkout)); // append currentWorkout as a new
                                                                                // example to avoid 'reference trap'
            currentWorkout.clear(); // after addition clear the 'container'
        }
        return workoutCollection;
    }

    private void create_workout_pairs(ArrayList<String> pairCollections, HashMap<String, String> currentWorkout) {
        for (String pair : pairCollections) {
            String[] entry = pair.split(":");
            currentWorkout.put(entry[0].trim(), entry[1].trim());
        }
    }

    private String create_keyValue_pairs(ArrayList<String> workoutSeparations, ArrayList<String> pairCollections,
            String currentPair, int i) {
        for (int k = 0; k < workoutSeparations.get(i).length(); k++) {
            if (workoutSeparations.get(i).charAt(k) == ',') {
                // add key, value pare to pairCollections
                pairCollections.add(currentPair);
                currentPair = "";
            } else {
                currentPair += String.valueOf(workoutSeparations.get(i).charAt(k));
            }
        }
        return currentPair;
    }

    private ArrayList separate_workout(ArrayList<String> workoutSeparations, String currentSubs, String testString) {
        /**
         * Removes the brackets from the json string and separates the workouts by
         * comma,
         * then appends the separated results to 'workoutSeparations'
         * 
         * @param ArrayList<String> workoutSeparations
         * @param String            currentSubs
         * @param String            testString
         */
        int beginIndex = 0; // begin index to of the substing
        int endIndex = 0;// end index to of the substing
        for (int i = 0; i < testString.length(); i++) {
            if (testString.charAt(i) == '{') {
                beginIndex = i + 1;
            } else if (testString.charAt(i) == '}') {
                endIndex = (i);
                currentSubs = testString.substring(beginIndex, endIndex);
                workoutSeparations.add(currentSubs);
                currentSubs = "";
            } else {
                currentSubs += String.valueOf(testString.charAt(i));
            }
        }
        return workoutSeparations;
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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == backButton) {
            go_my_workouts();
        } else if (e.getSource() == editWorkoutName) {
            System.out.println("Clicked");
            String currentWorkoutName = workoutName.getText();
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
            String currentWorkoutType = workoutType.getText();
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
            try {
                db.update_workout_name(conn, newWorkoutName, currUser.get_current_user(), workoutName.getText(), elementCounter2);

            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }

        else if (e.getSource() == done2) {
            String newWorkoutType = workoutTypeTextField.getText();
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
        }

    }

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
