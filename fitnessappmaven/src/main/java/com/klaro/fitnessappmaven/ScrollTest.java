package com.klaro.fitnessappmaven;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.*;
import java.awt.*;

/**
 * ScrollTest
 */
// TODO - make horizotal scrolling vanish
public class ScrollTest extends JFrame {
    // init. button(s) and panel(s)
    JButton button, back, test;
    JPanel panelTop, panelBottom, panelRight, panelCenter, panelScroll;

    // paths
    public final String WORKOUT_PIC_1 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\yoga.png";
    public final String WORKOUT_PIC_2 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\workout.png";
    public final String WORKOUT_PIC_3 = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\tempWorkoutIcons\\fitness.png";

    public ScrollTest() {
        // init. buttons
        button = new JButton("Love");
        back = new JButton("<Back");
        // initialize panels
        panelTop = new JPanel();
        panelBottom = new JPanel();
        panelRight = new JPanel();
        panelCenter = new JPanel();
        panelScroll = create_scroll_panel();
        panelTop.setPreferredSize(new Dimension(200,20));

        setup(); // call setup - sets up the basics for this.JFrame
        // set_panel_color(); // set color of panels
        set_top_panel(); // set panelTop: add back button to it and align it to right
        set_size_location(); // set size and locations of elements
        SwingUtilities.updateComponentTreeUI(this); // force refresh page to make everything visible right away.
    }

    private void set_top_panel() {
        panelTop.setLayout(new GridLayout(1,3, 10,10));
        for (int i = 0; i < 3; i++) {
            JPanel pan = new JPanel();
            panelTop.add(pan);
        }
        panelTop.add(back);
        panelTop.setPreferredSize(new Dimension(100,30));
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
            panelScroll.add(new JButton(workoutExamples[i][0]));
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
}