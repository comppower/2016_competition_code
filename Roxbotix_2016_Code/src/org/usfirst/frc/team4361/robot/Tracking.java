package org.usfirst.frc.team4361.robot;

public class Tracking {
	public double corX, corY, corA;
	public double filter;
	//intialize the class with all of the calibration values
	public Tracking(double inX, double inY)
	{
		//to make this work, just parse the exact values that are prin
		corX=inY;
		corY=inX;
		filter =.1;
	}
	//set the current X,Y and area values (reverse them when they are inputed like so
	public String track(double y, double x)
	{
		//the image is rotated 270 degrees, so the parameters must be passed in terms of -y,x
		//print the current conditions
		System.out.println(x+ "," + y);
		//using an if statement for the different conditions
		//we need to ensure that moving back makes the center Y value lower, and moving forward makes it higher
		//The left and right statements must be reversed, becasue the x adjustments change the y axis
		//the back and forward statements are as they should be, becuase the x cooridnate is 0 when the robot
		//is as far forward as possible to the target
		if(Math.abs(corX-x)/corX>filter)
		{
			if(x>corX)
			{
				System.out.println("Rotate Right");
				return "right";
			}
			if(x<corX)
			{
				System.out.println("Rotate Left");
				return "left";
			}
		}
		if(Math.abs(y-corY)/corY>filter)
		{
			if(y<corY)
			{
				System.out.println("Move Forward");
				return "forward";
			}
			if(y>corY)
			{
				System.out.println("Move Back");
				return "back";
			}
		}
		if(Math.abs(corY-y)/corY<filter && Math.abs(corX-x)/corX<filter)
		{
			System.out.println("shoot");
			return "shoot";
		}
		return "null";
	}

}
