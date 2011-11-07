package com.fitzgerald.simulator.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

public class UI implements Runnable {
    
    protected MainWindow mainWindow;
    protected boolean initialised = false;
    
    public UI() {
        SwingUtilities.invokeLater(this);
        
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Called by Swing to allow the application
     * thread to make changes to the GUI
     */
    public void run() {
        System.out.println("Invoked!");
        if (!initialised) {
            mainWindow = new MainWindow();
            initialised = true;
        }
    }
}
