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
	int cur=0;
	int prev=0;

	double speed=.15;
	boolean half=false;
	
	double xCal;
	double yCal;
    
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
	   	 half=false;
	   	 
	   	 this.xCal = xCal;
	   	 this.yCal = yCal;
	}
	
	public void track(double calIn)
	{
		deafultVal = new double[0];
	   	 centerX = table.getNumberArray("centerX", deafultVal);
	   	 centerY = table.getNumberArray("centerY", deafultVal);
	   	 width = table.getNumberArray("width",deafultVal);
	   	 length = table.getNumberArray("height", deafultVal);
	   	 area = table.getNumberArray("area",deafultVal);
	   	 dir = "";

	   	 if(half)
	   	 {
		   	 speed=1.5*.1*-calIn+.15*.5;
	   	 }
	   	 else if(!half)
	   	 {
		   	 speed=1.5*.1*-calIn+.15;
	   	 }

		if(centerX.length>0)
		{
			if(!ave.cal)
			{
				cal(length, width, centerX, centerY);
				System.out.println("Calibrating");
			}
			else
			{
				//double[] values = input(length, width, centerX, centerY, area);
				 dir = track.track(centerX[0], centerY[0]);
				 //filter = values[2];

				 System.out.println("speed: "+speed+"half: " + half);
			}
			if(checkForward())
			{
				dir="forward";
				System.out.println("checkem");
			}
			 System.out.println(dir);

			if(dir.equals("left"))
			{
				//1.5 to correct for slower turn
				left.drive(-speed*1.25);
				right.drive(-speed*1.25);
				shoot.shoot(false, false);
				cur=1;
			}
			if(dir.equals("right"))
			{
				left.drive(speed*1.25);
				right.drive(speed*1.25);
				shoot.shoot(false, false);
				cur=2;
			}
			if(dir.equals("back"))
			{
				left.drive(-speed);
				right.drive(speed);
				shoot.shoot(false, false);
				cur=3;
			}
			if(dir.equals("forward"))
			{
				left.drive(speed);
				right.drive(-speed);
				shoot.shoot(false, false);
				cur=4;

			}
			if(dir.equals("shoot"))
			{
				shoot.shoot(false, true);
				left.drive(0);
				right.drive(0);
			}
			half=checkRep();
			prev=cur;

			

		}
	}
	public boolean checkForward()
	{
		if(cur==1&&prev==2||cur==2&&prev==1)
		{
			return true;
		}
		return false;
	}
	public boolean checkRep()
	{
		if(cur==1&&prev==2)
		{
			return true;
		}
		if(cur==2&&prev==1)
		{
			return true;
		}
		if(cur==1&&prev==2)
		{
			return true;
		}
		if(cur==3&&prev==4)
		{
			return true;
		}
		if(cur==4&&prev==3)
		{
			return true;
		}
		if(prev==3&&cur==3)
		{
			return true;
		}
		return false;
	}
	public void cal(double[] length, double[] width, double[] centerX, double[] centerY)
    {
    	//check to see if the array is full already
    	ave.cal=true;
		for(int i=0; i<ave.centerX.length; i++)
		{
			ave.xIn(centerX[0]);
			ave.yIn(centerY[0]);
		}
    	for(int i=0; i<ave.centerX.length; i++)
		{
			if(ave.centerX[i]==-1)
			{
				ave.cal=false;
			}
		}
		//intializes values to look for the best hit
    	/*int index =-1;
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

		}*/

    }
    public double[] input(double[] length, double width[], double[] x, double[] y, double[] a)
    {
    	int index =-1;
		double score =0;
		//calibrate this corScore value
		double corScore=.1;
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
    
 
}
