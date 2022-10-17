package com.klaro.fitnessappmaven;

import java.awt.*;
import java.net.URL;
import java.awt.image.*;
import javax.imageio.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.LabelUI;

public class SetProfileImage {
    public JLabel setBasicProfPic(JLabel label, String picPath) {
        ImageIcon imageIcon = new ImageIcon(picPath);
        Image resizedImage = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage, imageIcon.getDescription());
        label.setIcon(imageIcon);
        return label;
    }
}