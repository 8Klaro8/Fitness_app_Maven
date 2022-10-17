package com.klaro.fitnessappmaven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CurrentUser {
    public final String USER_FILE_PATH = "fitnessappmaven\\src\\main\\java\\com\\klaro\\fitnessappmaven\\current_user\\current_user.txt";
    

    public String get_current_user() throws IOException {
        // read current user from txt file
        Path fileName = Path.of(USER_FILE_PATH);
        return Files.readString(fileName);
    }

    public void set_current_user(String userText) throws IOException {
        // read current user from txt file
        Path fileName = Path.of(USER_FILE_PATH);
        Files.writeString(fileName, userText);
    }

}
