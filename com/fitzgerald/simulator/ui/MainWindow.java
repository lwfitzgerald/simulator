package com.fitzgerald.simulator.ui;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
    public MainWindow() {
        super();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        
        add(new RegisterTable());
        
        pack();
        setVisible(true);
    }
}
