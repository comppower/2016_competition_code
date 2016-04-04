package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Portcullis {

	private CANTalon CAN;
	private Relay light;
	private boolean move;
	private Timer timer;
	
	public Portcullis(CANTalon CAN, Relay light)
	{
		this.CAN = CAN;
		this.light = light;
		move = true;
		timer = new Timer();
	}
	
	/*public void liftCom(int val)
	{
		if(val==0 && pos != 0 && !move)
    	{
    		CAN.set(.7);
    		pos = 0;
    		move = true;
    	}
		else if(val==180 && pos != 1 && !move)
    	{
    		CAN.set(-.7);
    		pos = 1;
    		move = true;
    		timer.start();
    	}
		
		if(timer.get() >= 2)
		{
			CAN.set(0);
    		move = false;
    		timer.stop();
    		timer.reset();
		}
		if(LTS.get() && pos == 0)
		{
			CAN.set(0);
    		move = false;
    	}
		
		if(pos == 0)
			light.set(Relay.Value.kForward);
		else
			light.set(Relay.Value.kOff);
	}*/
	
	public void lift(int val)
	{
		//check negative and positive values
		//reverse +1 and -2 to make them reversed
		
		if(move)
		{
			timer.start();
			move = false;
		}
		if(val == 0)
		{
			CAN.set(.5);
		}
		else if (val == 180)
		{
			CAN.set(-.5);
		}
		else if(true)
		{
			CAN.set(0);
			timer.stop();
			timer.reset();
			move = true;
		}
	}
	public void lift(double val)
	{
		CAN.set(val);
	}

}
