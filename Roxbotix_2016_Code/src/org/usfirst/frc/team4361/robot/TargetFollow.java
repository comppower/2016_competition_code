package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TargetFollow {
	
	//GRIP 
    NetworkTable table;
	final double[] defaultVal={0.0};
	Tracking track;
    WeightedAverage ave;
    double[] deafultVal;
	double[] centerX; 
	double[] centerY; 
	double[] width; 
	double[] length;
	double[] area;
	String dir = "";
	double speed=.15;
    
    Drive left, right;
    Shooter shoot;
    
	public TargetFollow(Drive left, Drive right, Shooter shoot, double xCal, double yCal) {
		//set up corrected centers for the robot
        table = NetworkTable.getTable("GRIP/myContoursReport");
        track = new Tracking(xCal, yCal); //133, 105
        ave = new WeightedAverage(10,700);
        
        this.left = left;
        this.right = right;
        this.shoot = shoot;
        
	     //tracking portion
	     deafultVal = new double[0];
	   	 centerX = table.getNumberArray("centerX", deafultVal);
	   	 centerY = table.getNumberArray("centerY", deafultVal);
	   	 width = table.getNumberArray("width",deafultVal);
	   	 length = table.getNumberArray("height", deafultVal);
	   	 area = table.getNumberArray("area",deafultVal);
	   	 dir = "";
	   	 speed=.15;
	}
	
	public void track()
	{
		deafultVal = new double[0];
	   	 centerX = table.getNumberArray("centerX", deafultVal);
	   	 centerY = table.getNumberArray("centerY", deafultVal);
	   	 width = table.getNumberArray("width",deafultVal);
	   	 length = table.getNumberArray("height", deafultVal);
	   	 area = table.getNumberArray("area",deafultVal);
	   	 dir = "";
	   	 speed=.25;
		if(centerX.length>0)
		{
			if(!ave.cal)
			{
				cal(length, width, centerX, centerY);
				System.out.println("Calibrating");
			}
			else
			{
				double[] values = input(length, width, centerX, centerY, area);
				 dir = track.track(values[0], values[1]);
				 //filter = values[2];
				 System.out.println(dir);
			}
			
			if(dir.equals("left"))
			{
				//1.5 to correct for slower turn
				left.drive(-speed);
				right.drive(-speed);
	
			}
			if(dir.equals("right"))
			{
				left.drive(speed);
				right.drive(speed);
			}
			if(dir.equals("back"))
			{
				left.drive(-speed);
				right.drive(speed);
			}
			if(dir.equals("forward"))
			{
				left.drive(speed);
				right.drive(-speed);
			}
			if(dir.equals("shoot"))
			{
				shoot.shoot(false, true);
			}
		}
	}
	
	public void cal(double[] length, double[] width, double[] centerX, double[] centerY)
    {
    	//check to see if the array is full already
    	ave.cal=true;
		for(int i=0; i<ave.centerX.length; i++)
		{
			if(ave.centerX[i]==-1)
			{
				ave.cal=false;
			}
		}
		//intializes values to look for the best hit
    	int index =-1;
		double score =0;
		//calibrate this corScore value
		double corScore=1;
		//replace this loop with a loop to look through length and make score
		//length[i]/width[i]
		for(int i=0; i<length.length; i++)
		{
			score = length[i]/width[i];
			if(Math.abs(score-1.4)<Math.abs(corScore-1.4))
			{
				corScore=score;
				index = i;
			}
		}
		//do nothing if the index isn't changed
		if(index ==-1)
		{

		}
		else
		{
			//System.out.println(" 	"+corScore + " was closest at "+index);
			//put in centerX[index] here instead of corScore
			ave.xIn(centerX[index]);
			ave.yIn(centerY[index]);
		}
    }
    public double[] input(double[] length, double width[], double[] x, double[] y, double[] a)
    {
    	int index =-1;
		double score =0;
		//calibrate this corScore value
		double corScore=1;
		//replace this loop with a loop to look through length and make score
		//length[i]/width[i]
		for(int i=0; i<length.length; i++)
		{
			score = length[i]/width[i];
			if(Math.abs(score-1.4)<Math.abs(corScore-1.4))
			{
				corScore=score;
				index = i;
			}
		}
		//do nothing if the index isn't changed
		if(index ==-1)
		{

		}
		else
		{
			//System.out.println(" 	"+corScore + " was closest at "+index);
			//put in centerX[index] here instead of corScore
			ave.xIn(x[index]);
			ave.yIn(y[index]);
			ave.areaIn(a[index]);
		}
    	double[] def = {ave.getAverage("x"),ave.getAverage("y"), ave.getAverage("area")};
    	return def;
    }
    
    public void printUI(double yCur, double xCur)
    {
    	//supply correct x and y vals for calibration
    	double xCal = 156;
    	double yCal = 31;
    	SmartDashboard.putDouble("ROTATION", (xCal-xCur)/xCal);
    	SmartDashboard.putDouble("ELEVATION", (yCal-yCur)/yCal);
    	SmartDashboard.putBoolean("Alignment: X", Math.abs(xCal-xCur)/xCal<.05);
    	SmartDashboard.putBoolean("Alignment: Y", Math.abs(yCal-yCur)/yCal<.05);
    }
    public void printUI()
    {
    	 //Prints out UI (untested code)
        if(centerX.length>0&&centerY.length>0)
        {
        	printUI(centerX[0], centerY[0]);
        }
        else
        {
        	printUI(-1,-1);
        }
    }
}
