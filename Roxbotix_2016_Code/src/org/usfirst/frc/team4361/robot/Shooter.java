package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

public class Shooter {
	
	private Timer indTimer;

	private CANTalon[] SHT, LIFT, IND;
	
	private DigitalInput[] LTS;
	
	private Relay[] light;

	public Shooter(CANTalon[] SHT, CANTalon[] LIFT, CANTalon[] IND, Relay[] light)
	{
		this.SHT = SHT;
		this.LIFT = LIFT;
		this.IND = IND;
		indTimer = new Timer();
		
		this.light = light;
	}
	
	public Shooter(CANTalon[] SHT, CANTalon[] LIFT, CANTalon[] IND, DigitalInput[] LTS, Relay[] light)
	{
		this.SHT = SHT;
		this.LIFT = LIFT;
		this.IND = IND;
		this.LTS = LTS;
		indTimer = new Timer();
		
		this.light = light;
	}

	public void lift(double val)
	{
		if(LTS[0].get()&&val>0)
		{
			LIFT[0].set(-val);
			LIFT[1].set(val);
		}
		else if(!LTS[2].get()&&val<0)
		{
			LIFT[0].set(-val);
			LIFT[1].set(val);
		}
		else
		{
			LIFT[0].set(0);
			LIFT[1].set(0);
		}
		
		
		if(!LTS[2].get())
			light[0].set(Relay.Value.kForward);
		else
			light[0].set(Relay.Value.kOff);
		
		if(!LTS[0].get())
			light[1].set(Relay.Value.kForward);
		else
			light[1].set(Relay.Value.kOff);
	}
	
	public void liftSim(double val)
	{
		LIFT[0].set(-val);
		LIFT[1].set(val);
		
		/*if(!LTS[2].get())
			light[0].set(Relay.Value.kForward);
		else
			light[0].set(Relay.Value.kOff);
		
		if(!LTS[0].get())
			light[1].set(Relay.Value.kForward);
		else
			light[1].set(Relay.Value.kOff);*/
	}

	public void shoot(boolean in, boolean out) 
	{
		if (in) 
		{
			SHT[0].set(1);
			SHT[1].set(-1);
		}
		
		// check direction
		else if (out) 
		{
			SHT[0].set(-1);
			SHT[1].set(1);
		}
		
		else 
		{
			for (int i = 0; i < SHT.length; i++) 
			{
				SHT[i].set(0);
			}
		}
	}

	public void index(boolean in, boolean out) 
	{
		if (in)
		{
			IND[0].set(-.4);
		} 
		else if (out) 
		{
			IND[0].set(.4);
		} 
		else 
		{
			IND[0].set(0);
		}
	}
	
	public void indexAuto(boolean ind)
	{
		if(ind)
		{
			IND[0].set(-.4);
			
		}
		else if(!ind&&!LTS[1].get()&&indTimer.get()<=0)
		{
			IND[0].set(.4);
			indTimer.start();
		}
		else if(LTS[1].get()||indTimer.get()>=1)
		{
			IND[0].set(0);
			indTimer.stop();
			indTimer.reset();
		}
	}
	public boolean getShoot()
	{
		return(SHT[0].get()==-1);
	}
	private void region() {
		/*
		 * public void startIntake(boolean check) { if(check) { CAN[0].set(-.5);
		 * CAN[1].set(.5); } } public void startShooter(boolean check) {
		 * if(check) { CAN[0].set(1); CAN[1].set(-1); } }
		 * 
		 * public void lowerShooter(boolean check) { if(check) { LIFT[0].set(1);
		 * LIFT[1].set(-1); } } public void raiseShooter(boolean check) {
		 * if(check) { LIFT[0].set(-1); LIFT[1].set(1); } }
		 * 
		 * public void stopShooter(boolean check) { if(!check) { for(int i = 0;
		 * i < CAN.length; i++) { CAN[i].set(0); } } } public void
		 * stopLift(boolean check) { if(!check) { for(int i = 0; i <
		 * LIFT.length; i++) { LIFT[i].set(0); } } }
		 */}

}
