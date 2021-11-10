package com.cenes.adhanpeek;

import com.batoulapps.adhan.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // Validate input
        if (args.length != 4) {
            System.out.println("Exactly 4 parameters must be provided: " +
                    "Coordinate latitude, Coordinate longitude, Calculation method or path, Configuration path");
            System.exit(1);
        }

        // Parameters
        int spawnX, spawnY;
        int windowWidth, windowHeight;
        int backgroundColorR, backgroundColorG, backgroundColorB;
        int labelColorR, labelColorG, labelColorB;
        int fontSize;
        int timeoutMilliseconds;
        int maxNegativeRemaining;

        List<String> configurations = Files.readAllLines(Paths.get(args[3]));
        // Clear comments
        for (int i = configurations.size()-1; i >= 0; i--) {
            if (configurations.get(i).contains("#")) {
                configurations.set(i, configurations.get(i).split("#")[0].trim());
            } 
            if (configurations.get(i).length() == 0) {
                configurations.remove(i);
            }
        }
        spawnX = Integer.parseInt(configurations.get(0));
        spawnY = Integer.parseInt(configurations.get(1));
        windowWidth = Integer.parseInt(configurations.get(2));
        windowHeight = Integer.parseInt(configurations.get(3));
        backgroundColorR = Integer.parseInt(configurations.get(4));
        backgroundColorG = Integer.parseInt(configurations.get(5));
        backgroundColorB = Integer.parseInt(configurations.get(6));
        labelColorR = Integer.parseInt(configurations.get(7));
        labelColorG = Integer.parseInt(configurations.get(8));
        labelColorB = Integer.parseInt(configurations.get(9));
        fontSize = Integer.parseInt(configurations.get(10));
        timeoutMilliseconds = Integer.parseInt(configurations.get(11));
        maxNegativeRemaining = Integer.parseInt(configurations.get(12));


//        final Coordinates coordinates = new Coordinates(40.68962877,30.24520703);
        final Coordinates coordinates = new Coordinates(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
        RemainingCalculator remainingCalculator = new RemainingCalculator(args[2], coordinates, maxNegativeRemaining);

        JFrame frame = new JFrame();
        frame.setSize(windowWidth, windowHeight);
        frame.getContentPane().setBackground(new Color(backgroundColorR, backgroundColorG, backgroundColorB));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setLocation(spawnX, spawnY);

        JLabel label = new JLabel("" + remainingCalculator.calculateRemainingTime(), JLabel.CENTER);
        label.setFont(label.getFont().deriveFont((float)fontSize).deriveFont(Font.BOLD));
        label.setForeground(new Color(labelColorR, labelColorG, labelColorB));
        label.setBounds(0, 0, windowWidth, windowHeight);
        frame.add(label);

        JLabel label2 = new JLabel("" + remainingCalculator.calculateRemainingTime(), JLabel.CENTER);
        label2.setFont(label2.getFont().deriveFont((float)fontSize).deriveFont(Font.BOLD));
        label2.setForeground(new Color(0, 0, 0));
        label2.setBounds(5, 5, windowWidth+5, windowHeight+5);
        frame.add(label2);

        frame.setVisible(true);

        new Timer(timeoutMilliseconds, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        }).start();

    }
}
