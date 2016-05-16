package org.usfirst.frc.team4361.robot;

public class TurnControl {
	double Cal;
	double angle;
	double targetAngle;
	double speed;
	double ratio;
	double cur;
	double prev;
	
	public TurnControl(double angle)
	{
		speed = 0;
		Cal=angle;
		this.angle=angle-Cal;
		ratio=0;
		cur=1;
		prev=1;
	}
	public double turnAngle(double current, double target)
	{
		//get angle and ensure its 0<angle<=360
		//set motor speed with calIn from the flap
		angle =current-Cal;
		resetAngle();
		angle =current-Cal;

		//circumvents divide by 0 error
		if(target==0)
		{
			target =.1;
		}
		if(target<0)
		{
			target+=360;
		}
		ratio = (angle-target)/target;
		
		//print statements for testing
		System.out.print("Cal is " + Cal+ " ");
		System.out.print("angle is "+ angle + " ");
		System.out.print("ratio is " + ratio + " ");
		
		//set ratio and ensure speed is between .15 and .65		
		if(Math.abs(ratio)<.08)
		{
			speed = 0;
		}
		/*else if(Math.abs(ratio)<.22)
		{
			speed=.24;
		}
		else if(Math.abs(ratio)>.45)
		{
			speed =.45;
		}
		else
		{
			speed = Math.abs(ratio);
		}*/
		speed = .2*Math.pow(Math.E, Math.abs(ratio));
		if(speed>1)
			speed=1;
		if(Math.abs(angle-target)<.01)
		{
			speed = 0;
		}
		//make sure speed is set properly for negative and positive
		if(ratio<0)
		{
			speed*=-1;
			cur=1;
		}
		else
		{
			cur=-1;
		}
		if(cur != prev)
		{
			speed=0;
		}
		return speed;
		
	}
	//get angle and ensure its 0<angle<=360
	public void resetAngle()
	{
		if(angle>=370)
		{
			Cal+=360;
		}
		if(angle<=-370)
		{
			Cal-=360;
		}
	}

}
