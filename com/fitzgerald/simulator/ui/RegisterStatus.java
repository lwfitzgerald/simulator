package com.fitzgerald.simulator.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fitzgerald.simulator.processor.Util;

public class RegisterStatus extends JPanel {
    
    protected JTextField textField;
    
    public RegisterStatus(String registerName) {
        super();
        
        JLabel label = new JLabel(registerName);
        textField = new JTextField();
        textField.setEnabled(false);
        textField.setDisabledTextColor(new Color(0));
        textField.setMinimumSize(new Dimension(82, 19));
        textField.setMaximumSize(new Dimension(82, 19));
        textField.setPreferredSize(new Dimension(82, 19));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        
        GroupLayout layoutMgr = new GroupLayout(this);
        layoutMgr.setHorizontalGroup(layoutMgr.createSequentialGroup()
                .addComponent(label)
                .addComponent(textField)
        );
        
        setVisible(true);
    }
    
    /**
     * Set the value of this register status box
     * @param value Value to set to
     */
    public void setValue(byte[] value) {
        textField.setText(String.valueOf(Util.bytesToInt(value)));
    }
    
}
