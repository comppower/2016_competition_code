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
	int curX=0;
	int curY=0;
	int prevX=0;
	int prevY=0;

	double speedX=.15;
	double speedY=.15;
	boolean halfX;
	boolean halfY;
	
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

	   	 speedX=.15;
	   	 speedY=.15;
	   	 halfX=false;
	   	 halfY=false;
	   	 
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
				 System.out.println("speedX: "+speedX+" halfX: " + halfX);
				 System.out.println("speedY: "+speedY+" halfY: " + halfY);
			}
			System.out.println(dir);
			//sets motor speed
			setSpeed(calIn);
			//sets motors to do the corrections
			if(dir.equals("left"))
			{
				//1.5 to correct for slower turn
				left.drive(-speedX);
				right.drive(-speedX);
				shoot.shoot(false, false);
				curX=1;
			}
			if(dir.equals("right"))
			{
				left.drive(speedX);
				right.drive(speedX);
				shoot.shoot(false, false);
				curX=2;
			}
			if(dir.equals("back"))
			{
				left.drive(-speedY);
				right.drive(speedY);
				shoot.shoot(false, false);
				curY=2;
			}
			if(dir.equals("forward"))
			{
				left.drive(speedY);
				right.drive(-speedY);
				shoot.shoot(false, false);
				curY=1;
			}
			
			if(dir.equals("shoot"))
			{
				shoot.shoot(false, true);
				left.drive(0);
				right.drive(0);
			}
			halfX=checkRep(curX,prevX,halfX);
			halfY=checkRep(curY,prevY,halfY);
			prevY=curY;
			prevX=curX;

		}
	}

	public void setSpeed(double calIn) 
	{
		if (halfX) 
		{
			speedX = .2 * -calIn + .15 * .5;
		} 
		else if (!halfX) 
		{
			speedX = .2 * -calIn + .15;
		}
		if(halfY)
		{
			speedY= .15 * -calIn + .15 * .5;
		}
		else if(!halfY)
		{
			speedY= .15 * -calIn + .15;
		}
	}
	public boolean checkRep(int cur, int prev, boolean half)
	{
		if(cur==1&&prev==2)
		{
			return true;
		}
		if(cur==2&&prev==1)
		{
			return true;
		}
		if(cur==prev && half==true)
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
