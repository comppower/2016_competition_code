package org.usfirst.frc.team4361.robot;
import com.kauailabs.navx.frc.*;

public class turnControl {
	
	double CAL;
	double angle;
	AHRS navX;

	public turnControl(AHRS navX) 
	{
		CAL=-navX.getAngle();
		angle=CAL+navX.getAngle();
		this.navX=navX;
	}
	public void reset()
	{
		CAL=-navX.getAngle();
		angle=CAL+navX.getAngle();
	}
	public void rotate(double target)
	{
		angle=CAL+navX.getAngle();
		if(angle<-360||angle>360)
		{
			angle = 0;
		}
	}

}
