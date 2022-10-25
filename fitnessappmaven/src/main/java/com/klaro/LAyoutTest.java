package com.klaro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

public class LAyoutTest extends JFrame {
    // border number
    final int BORDER_NUMBER = 10;
    // Paths
    public final String LOGO_PIC_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\Logo\\lgo.png";
    public final String WORKOUT_PIC_1 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\yoga.png";
    public final String WORKOUT_PIC_2 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\workout.png";
    public final String WORKOUT_PIC_3 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\fitness.png";
    // panels
    JPanel topBar;
    JPanel leftScrollBar;
    JPanel centerbar;
    // buttons
    JButton backButton;
    JButton workoutButton;

    public LAyoutTest() {

        // panels
        topBar = createTopBar();
        leftScrollBar = createLeftScrollBar();
        centerbar = createCenterbar();
        // buttons
        backButton = new JButton("< Back");
        workoutButton = new JButton();
        // Set layout & content to 'topBar'
        topBar.setLayout(new BorderLayout());
        topBar.add(BorderLayout.EAST, backButton);
        // Set scrollable pane
        leftScrollBar.add(new JScrollPane(contentForScrollPane()));
        // set icon to app
        setIconToApp(); 
        // set up this frame
        setUpBasics();
        // add panels to this
        addCompontentToThis();
    }

    private void setUpBasics() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBounds(10, 10, 370, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void setIconToApp() {
        ImageIcon image = new ImageIcon(LOGO_PIC_PATH);
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.WHITE);
    }

    public JPanel createTopBar() {
        JPanel topBar = new JPanel();
        topBar.setBackground(Color.BLUE);
        return topBar;
    }

    public JPanel createLeftScrollBar() {
        JPanel leftScrollBar = new JPanel();
        leftScrollBar.setBackground(Color.GREEN);
        return leftScrollBar;
    }

    public JPanel createCenterbar() {
        JPanel centerBar = new JPanel();
        centerBar.setBackground(Color.BLACK);
        return centerBar;
    }

    public void addCompontentToThis() {
        this.add(BorderLayout.NORTH, topBar);
        this.add(BorderLayout.WEST, leftScrollBar);
        this.add(BorderLayout.CENTER, centerbar);
    }

    // scrollable workout panel
    public JPanel contentForScrollPane() {
        // TODO: Display prof. pics in 3 columns
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(BORDER_NUMBER, BORDER_NUMBER, BORDER_NUMBER, BORDER_NUMBER));
        String[][] workoutExamples = new String[][] {
                { "Workout 1", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 2", "Rep", "Kcal", WORKOUT_PIC_2 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_3 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
        };
        panel.setLayout(new GridLayout(workoutExamples.length,1,1,5));

        for (int j = 0; j < workoutExamples.length; j++) {
            workoutButton = new JButton();
            // Set the current image(icon) to a JButton and adds it to panel
            workoutButton = setWorkoutButtonIcon(workoutExamples[j][3], workoutButton);
            // adds current button to the panel
            workoutButton.setText(workoutExamples[j][0]);
            panel.add(workoutButton);
        }
        return panel;
    }

    // set the icon of the workout buttons
    public JButton setWorkoutButtonIcon(String picPath, JButton button) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage, imageIcon.getDescription());
        button.setIcon(imageIcon);
        return button;
    }
}
