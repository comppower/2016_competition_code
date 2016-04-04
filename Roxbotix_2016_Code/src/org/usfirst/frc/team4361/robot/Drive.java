package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class Drive{

	CANTalon[] CAN;
	
	public Drive(CANTalon[] CAN)
	{
		this.CAN = CAN;
	}
	
	public void drive(double val)
	{
		for(int i = 0; i < CAN.length; i++)
		{
			CAN[i].set(val);
		}
	}
	
	public void drive(boolean input, double val, Timer timer, double time)
	{
		if(input&&timer.get()==0.0)
		{
			for(int i = 0; i < CAN.length; i++)
			{
				CAN[i].set(val);
			}
			timer.start();
		}
		else if(timer.get()>=time)
		{
			for(int i = 0; i < CAN.length; i++)
			{
				CAN[i].set(0);
			}
			timer.stop();
			timer.reset();
		}
	}
}
