/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

/**
 *
 * @author Simen
 */
public class OccupancyEstimator {
    
    DataContainer office;
    static int volume = 55;
    static int period = 3600;
    static int s_breath = 4;
    static int CO2_conc_out = 395;
    
    public OccupancyEstimator(DataContainer dataset){
        office = dataset;
    }
    
    public double[] powerConsumption(){
        double[] LapTop1 = office.getData("power_laptop1_zone1");
        double[] LapTop2 = office.getData("power_laptop1_zone2");
        double[] LapTop3 = office.getData("power_laptop2_zone2");
        double[] LapTop4 = office.getData("power_laptop3_zone2");
        double[] NumOfPersons = new double[office.getNumberOfSamples()];
        for (int i = 0; i < office.getNumberOfSamples() ; i++) {
            if (LapTop1[i] > 15){
                NumOfPersons[i] += 1;
            }
            if (LapTop2[i] > 15){
                NumOfPersons[i] += 1;
            }
            if (LapTop3[i] > 15){
                NumOfPersons[i] += 1;
            }
            if (LapTop4[i] > 15){
                NumOfPersons[i] += 1;
            }   
        }
        return NumOfPersons;     
    }
    
    public double calculateError(double c){
        double[] detected_motions = office.getData("detected_motions");
        double[] NumOfPersons = powerConsumption();
        double error = 0;
        for (int i = 0; i < detected_motions.length; i++) {
            error += abs((NumOfPersons[i] - (c * detected_motions[i])));
        }
        //System.out.println("Error: " + error);
        return error;
    }  
    
    public double find_c(){
        double l = 0;
        double u = 23;
        double c = 0;
        for (int i = 0; i < office.getNumberOfSamples(); i++) {
            c = (u + l)/2;
            if (calculateError(l) < calculateError(u) && calculateError(c) < calculateError(u)){
                u = c;
            }
            else if (calculateError(c) < calculateError(l) && calculateError(u) < calculateError(l)){
                l = c;
            }
            else{
                break;
            }
        }
        return c;   
    }   
    
    public double[] motionDetected(){
        double[] detected_motions = office.getData("detected_motions");
        double c = find_c();
        for (int i = 0; i < detected_motions.length; i++) {
            detected_motions[i] = detected_motions[i] * c;
        }
        return detected_motions;
    } 
   
    
    public double[] estimated_num_of_people(double[] c){
        double[] CO2_Office = office.getData("office_CO2_concentration");
        double[] CO2_Corridor = office.getData("corridor_CO2_concentration");
        double[] window_open = office.getData("window_opening");
        double[] door_open = office.getData("door_opening");
        double[] num_estim = new double[window_open.length];
        double[] Qwind_out = new double[window_open.length];
        double[] Qdoor_corr = new double[window_open.length];
        double[] Qout = new double[window_open.length];
        double[] Qcorr = new double[window_open.length];
        double[] alpha = new double[window_open.length];
        double[] beta_out = new double[window_open.length];
        double[] beta_corr = new double[window_open.length];
        double[] beta_n = new double[window_open.length];
        for (int i = 0; i < window_open.length-1; i++) {
            Qwind_out[i] = c[1] * window_open[i];
            Qout[i] = c[0] + Qwind_out[i];
            Qdoor_corr[i] = c[3] * door_open[i];
            Qcorr[i] = c[2] + Qdoor_corr[i];
            alpha[i] = exp((-(Qout[i]+Qcorr[i])*period)/volume);
            beta_out[i] = ((1-alpha[i])*Qout[i])/(Qout[i] + Qcorr[i]);
            beta_corr[i] = ((1-alpha[i])*Qcorr[i])/(Qout[i]+Qcorr[i]);
            beta_n[i] = ((1-alpha[i])*s_breath)/(Qout[i]+Qcorr[i]);
            num_estim[i] = (CO2_Office[i+1] - alpha[i]*CO2_Office[i] - beta_out[i]*CO2_conc_out - beta_corr[i]*CO2_Corridor[i])/beta_n[i];
        }
         
        return num_estim;
        
    }
    
    public double calculateErrorCO2(double[] c){
        
        double[] NumOfPersons = powerConsumption();
        double error = 0;
        for (int i = 0; i < NumOfPersons.length; i++) {
            error += abs((NumOfPersons[i] - estimated_num_of_people(c)[i]));
        }
        //System.out.println("Error: " + error);
        return error;
    }
    
    
    
    
    public double[] people_CO2(double Q0Out,double QwOut, double Q0corr, double QdCorr){
       /* double[] NumOfPersons = new double[office.getNumberOfSamples()];
        double[]  = office.getData("detected_motions");
        double[] NumOfPersons = powerConsumption();
        double[] detected_motions = office.getData("detected_motions");
        double[] NumOfPersons = powerConsumption();
        */
    
        double[] c = {Q0Out, QwOut, Q0corr, QdCorr};r 
       
    }
}



