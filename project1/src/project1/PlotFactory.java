/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author Simen
 */
public class PlotFactory {
    
    
    public static JPanel plots(ArrayList <String> plotlist, DataContainer off) throws IOException, ParseException{
        Date[] dates = off.getDates();
        ArrayList <TimeSeries> timeTemps = new ArrayList <TimeSeries>();
        ArrayList <double[]> data = new ArrayList <double[]>();
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        
        for (int i = 0; i < plotlist.size(); i++) {
            timeTemps.add(new TimeSeries((Comparable) plotlist.get(i)));
            data.add(off.getData(plotlist.get(i)));
            for (int j = 0; j < off.getNumberOfSamples(); j++) {
                timeTemps.get(i).add(new Hour(dates[j]), data.get(i)[j]);
            }
            timeSeriesCollection.addSeries(timeTemps.get(i));            
        }
        JPanel chartPanel = new ChartPanel(ChartFactory.createTimeSeriesChart("Temperatures","Time","Temperature",timeSeriesCollection,true, true, false));
        return chartPanel;
    }    
   
}
