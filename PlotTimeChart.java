package project1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class PlotTimeChart {

	public static void main(String[] args) throws IOException { 		
        try { // be careful: this is not object oriented    
            DateFormat format = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
            DataContainer off = new DataContainer("office.csv");
            Date[] dates = off.getDates();
            String[] time = off.getTimeStrings();
            
            
            double[] Toffice = off.getData("Toffice");   
            
            double[] Tcorridor = off.getData("Tcorridor");
            
            int n = off.getNumberOfSamples();
            //Date[] dates = new Date[n];
            
              
                TimeSeries tempOffice = new TimeSeries("Toffice");
                TimeSeries tempCorridor = new TimeSeries("Tcorridor");
                for(int i=0; i<n; i++){
                    tempCorridor.add(new Hour(dates[i]),Tcorridor[i]);
                    tempOffice.add(new Hour(dates[i]),Toffice[i]);
                }
                TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
                timeSeriesCollection.addSeries(tempOffice);
                timeSeriesCollection.addSeries(tempCorridor);
                
                JPanel chartPanel = new ChartPanel(ChartFactory.createTimeSeriesChart("Temperatures","Time","ylabel",timeSeriesCollection,true, true, false));
                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(chartPanel);
                frame.pack();
                frame.setVisible(true);
               
                
                

        } catch (ParseException ex) {
			Logger.getLogger(PlotTimeChart.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
