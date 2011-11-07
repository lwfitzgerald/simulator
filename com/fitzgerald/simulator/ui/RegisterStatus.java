package com.fitzgerald.simulator.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fitzgerald.simulator.processor.Util;

public class RegisterStatus extends JPanel {
    
    protected JLabel label;
    protected JTextField textField;
    
    public RegisterStatus(String registerName) {
        super();
        
        label = new JLabel(registerName);
        // TODO: Make this empty + changeable via setters
        textField = new JTextField("0");
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
    
    public void setValue(byte[] value) {
        textField.setText(String.valueOf(Util.bytesToInt(value)));
    }
    
}
