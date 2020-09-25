package com.ai.phaticdialogue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String message;
        try {
            message = bis.readLine();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
