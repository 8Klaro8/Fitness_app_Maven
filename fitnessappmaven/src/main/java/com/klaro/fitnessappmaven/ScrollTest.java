package com.klaro.fitnessappmaven;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * ScrollTest
 */
// TODO - make horizotal scrolling vanish
public class ScrollTest extends JFrame implements ActionListener {
    // init. button(s) and panel(s)
    JButton button, back, test, workoutButton, addWorkout, removeWorkout;
    JPanel panelTop, panelBottom, panelRight, panelCenter, panelScroll;

    // paths
    public final String WORKOUT_PIC_1 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\yoga.png";
    public final String WORKOUT_PIC_2 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\workout.png";
    public final String WORKOUT_PIC_3 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\fitness.png";

    public ScrollTest() {
        // init. buttons
        button = new JButton("Love");
        back = new JButton("<Back");
        addWorkout = new JButton("Add Workout");
        removeWorkout = new JButton("Remove Workout");
        // initialize panels
        panelTop = new JPanel();
        panelBottom = new JPanel();
        panelRight = new JPanel();
        panelCenter = new JPanel();
        panelScroll = create_scroll_panel();
        panelTop.setPreferredSize(new Dimension(200, 20));
        // functions
        setup(); // call setup - sets up the basics for this.JFrame
        // set_panel_color(); // set color of panels
        set_center_panel();
        set_top_panel(); // set panelTop: add back button to it and align it to right
        set_size_location(); // set size and locations of elements
        addActionEvent(); // adds action(s) to buttons
        SwingUtilities.updateComponentTreeUI(this); // force refresh page to make everything visible right away.
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
        panelTop.add(back);
        panelTop.setPreferredSize(new Dimension(100, 30));
    }

    public JPanel create_scroll_panel() {
        JPanel panelScroll = new JPanel();
        // panelScroll.setBorder(new EmptyBorder(new Insets(10,10,10,20)));
        panelScroll.setBorder(new EmptyBorder(10, 10, 10, 25));
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
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
                { "Workout 3", "Rep", "Kcal", WORKOUT_PIC_1 },
        };
        panelScroll.setLayout(new GridLayout(workoutExamples.length, 1, 10, 10));
        for (int i = 0; i < workoutExamples.length; i++) {
            workoutButton = new JButton();
            workoutButton = setWorkoutButtonIcon(workoutExamples[i][3], workoutButton);
            workoutButton.setText(workoutExamples[i][0]);
            panelScroll.add(workoutButton);
        }
        return panelScroll;
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

    public void set_size_location() {
        this.add(BorderLayout.NORTH, panelTop);
        this.add(BorderLayout.SOUTH, panelBottom);
        this.add(BorderLayout.EAST, panelRight);
        this.add(BorderLayout.CENTER, panelCenter);
        this.add(BorderLayout.WEST, new JScrollPane(panelScroll));
        this.add(BorderLayout.NORTH, panelTop);
    }

    public void set_panel_color() {
        panelTop.setBackground(Color.GREEN);
        panelBottom.setBackground(Color.BLACK);
        panelRight.setBackground(Color.RED);
        panelCenter.setBackground(Color.PINK);
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

    // add action event
    public void addActionEvent() {
        back.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            try {
                go_back_to_homesite();// leads to home page
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
    }

}