package org.openstreetmap.josm.plugins.tofix;


import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.josm.Main;

public class TofixDialog extends JPanel {
	
    private JOptionPane optionPane;
    private JCheckBox delete;
    private JComboBox portCombo;

	    public TofixDialog() {
        GridBagConstraints c = new GridBagConstraints();
        JButton refreshBtn, configBtn;

        setLayout(new GridBagLayout());

        portCombo = new JComboBox();

        refreshPorts();
        c.insets = new Insets(4,4,4,4);
        c.gridwidth = 1;
        c.weightx = 0.8;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel(tr("Port:")), c);

        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.5;
        add(portCombo, c);

        refreshBtn = new JButton(tr("Refresh"));
        refreshBtn.addActionListener(new ActionListener(){
                @Override
				public void actionPerformed(java.awt.event.ActionEvent e){
                    refreshPorts();
                }
            });
        refreshBtn.setToolTipText(tr("refresh the port list"));
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        add(refreshBtn, c);

        configBtn = new JButton(tr("Configure"));
        configBtn.addActionListener(new ActionListener(){
                @Override
				public void actionPerformed(java.awt.event.ActionEvent e){
                    System.out.println("configuring the device");
                    try{





                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(Main.parent, tr("Connection Error.") + " " + ex.toString());
                    }
                    System.out.println("configuring the device finished");
                }
            });
        configBtn.setToolTipText(tr("configure the connected DG100"));
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        add(configBtn, c);


        delete = new JCheckBox(tr("delete data after import"));
        delete.setSelected(Main.pref.getBoolean("globalsat.deleteAfterDownload", false));

        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        add(delete, c);
    }

}