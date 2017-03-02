/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static project1.PlotFactory.plots;


/**
 *
 * @author Simen
 */
public class Mainframe extends JFrame implements ActionListener{
    DataContainer off;
    JPanel centerPlot = new JPanel();
    ArrayList <JCheckBox> variableCheckBoxes = new ArrayList <JCheckBox>();
    JButton plotButton;
    int plotCounter = 0;
    int nTotalPlots;
    
    public Mainframe() throws IOException{
        off = new DataContainer("office.csv");
        
        OccupancyEstimator office = new OccupancyEstimator(off);
        
        off.addData("Occupancy_est_power_cons", office.powerConsumption());
        off.addData("Occupancy_motion", office.motionDetected());
        String[] variables = off.getAvailableVariables();
        nTotalPlots = off.getNumberOfVariables();
        JFrame f = new JFrame("Temperatures");
        f.setLayout(new BorderLayout(10,10));
        JPanel westPanel = new JPanel();
        
        
        f.add(westPanel, BorderLayout.CENTER);
        westPanel.setLayout(new GridLayout(nTotalPlots + 1, 1));
        for (int i = 0; i < nTotalPlots; i++) {
            JCheckBox a = new JCheckBox(variables[i]);
            variableCheckBoxes.add(a);
            westPanel.add(a);
            //variableCheckBoxes.add();
            
        }
        plotButton = new JButton("Plot");
        plotButton.addActionListener(this);
        westPanel.add(plotButton);
        f.pack();
        f.setVisible(true);
        
        
    }
    
    
    public void actionPerformed(ActionEvent e) {
        ArrayList<String> plotList = new ArrayList<String>();
        for( int i=0; i<variableCheckBoxes.size(); i++ ) {
            JCheckBox checkBox = variableCheckBoxes.get( i );
            if( checkBox.isSelected() ) {
                String option = checkBox.getText();
                plotList.add(option);
            }
        }
        try {
            centerPlot = plots(plotList, off);
            
        } catch (IOException ex) {
            Logger.getLogger(Mainframe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Mainframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFrame f2 = new JFrame("Temperatures");
        f2.setLayout(new BorderLayout(10,10));
        f2.add(centerPlot, BorderLayout.CENTER);
        f2.pack();
        f2.setVisible(true);
        
           
        }
    }