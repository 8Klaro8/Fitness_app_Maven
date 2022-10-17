package com.klaro.fitnessappmaven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.IIOException;
import javax.swing.JFrame;

public class LogoutFunction {
    public void logout_from_app(JFrame thisFrame) throws IOException {
        thisFrame.dispose();
        new MyFrame();
        Path fileName = Path.of("fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\current_user\\current_user.txt");
        Files.writeString(fileName, "");
    }
}
