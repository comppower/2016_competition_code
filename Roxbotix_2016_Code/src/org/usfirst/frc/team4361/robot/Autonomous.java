package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.Timer;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.*;


public class Autonomous{
	
	double diameter;
	double circumference;
	double distanceNeeded;
	
	boolean isEnc, hasRun;
	int runNum, lEncNum, rEncNum, large;
	Timer timer, timerSpeed;
	Digit digit;
	
	Drive left, right;
	Shooter shoot;
	
	TargetFollow follow;
	
	Encoder lEnc, rEnc;
	
	int minDist, maxDist;
	
	Portcullis port;
	
	AHRS navx;
	
	public Autonomous(Drive left, Drive right, Shooter shoot, Portcullis port)
	{
		diameter = 8.4;
		circumference = diameter * Math.PI;
		
		isEnc = false;
		lEncNum = 0;
		rEncNum = 0;
		large = 0;
		hasRun = false;
		runNum = 0;
		timer = new Timer();
		timerSpeed = new Timer();
		digit = new Digit();
		
		this.left = left;
		this.right = right;
		this.shoot = shoot;
		this.port = port;
		
		follow = new TargetFollow(left, right, shoot);
		
		navx = new AHRS(SerialPort.Port.kMXP); 
	}
	public Autonomous(Drive left, Drive right, Shooter shoot, Portcullis port, Encoder lEnc, Encoder rEnc)
	{
		this(left, right, shoot, port);
		this.lEnc = lEnc;
		this.rEnc = rEnc;
		runNum = 0;
	}
	
	public void defaultGoToObstacle()
	{
		if(runNum == 0)
			goDistance(54, .3);
	}
	//untested code
	public void lowBar()
	{
		if(runNum == 0)
		{
			goDistance(10, .3);
			port.lift(0);
			//shoot.lift(-.4);
		}
		if(runNum == 1)
		{
			goDistance(44, .3);
			port.lift(-1);
			//shoot.lift(-.4);
		}
		if(runNum == 2)
		{
			goDistance(50, .4);
			//shoot.lift(0);
		}
		if(runNum == 3)
			runNum = -1;
	}
	
	public void portcullis()
	{
		if(runNum == 0)
			goDistance(30, .3);
		if(runNum == 1)
		{
			goDistance(24, .3);
			port.lift(0);
			//shoot.liftSim(-.5);
		}
		if(runNum == 2)
		{
			goDistance(50, .4);
			port.lift(-1);
			//shoot.lift(0);
		}
		if(runNum == 3)
		{
			goDistance(5, -.3);			
		}
		if(runNum == 4)
			runNum = -1;
	}
	
	public void chevelDeFrise()
	{
		if(runNum == 0)
			goDistance(56, .3);
		if(runNum == 1)
		{
			wait(0.0);
			port.lift(0);
			//shoot.lift(.5);
		}
		if(runNum == 2)
		{
			goDistance(100, .6);
			port.lift(-1);
			//shoot.lift(0);
		}
		if(runNum == 3)
			goDistance(5, -.4);
		if(runNum == 4)
			runNum = -1;
	}
	//end untested code
	
	public void moat()
	{
		//calibrate values
		if(runNum == 0)
			goDistance(54, .90);
		if(runNum == 1)
		{
			goDistance(65, 1);
			port.lift(180);
			//shoot.liftSim(.7);
		}
		if(runNum == 2)
		{
			goDistance(5, -.6);
			port.lift(-1);
			//shoot.lift(0);
		}
		if(runNum == 3)
			runNum = -1;
		
		//end calibration
	}
	
	public void ramparts()
	{
		if(runNum == 0)
			goDistance(54, .3);
		if(runNum == 1)
			goDistance(65, .8);
		if(runNum == 2)
			runNum = -1;
	}
	
	public void drawbridge()
	{
		if(runNum == 0)
			goDistance(54, .3);
	}
	
	public void sallyPort()
	{
		if(runNum == 0)
			goDistance(54, .3);
	}
	
	public void rockWall()
	{
		if(runNum == 0)
			goDistance(54, 1);
		if(runNum == 1)
		{
			goDistance(200, 1);
			port.lift(180);
			//shoot.lift(.7);
		}
		if(runNum == 2)
		{
			goDistance(5, -.8);
			port.lift(-1);
			//shoot.lift(0);
		}
		if(runNum == 3)
			runNum = -1;
	}
	
	public void roughTerrain()
	{
		if(runNum == 0)
			goDistance(54, .3);
		if(runNum == 1)
		{
			goDistance(80, 1);
			port.lift(180);
			//shoot.lift(.7);
		}
		if(runNum == 2)
		{
			goDistance(5, -.4);
			port.lift(-1);
			//shoot.lift(0);
		}
		if(runNum == 3)
			runNum = -1;
	}
	
	//Merge
	//runNum = -100 mean stop all Autonomous Function
	//hts
	public void high(int pos, String def)
	{
		if(runNum == -1 && !hasRun)
		{
			if(def.equals("lowbar"))
			{
				minDist = 0;
				maxDist = 0;
			}
			else if(def.equals("roughTerrain"))
			{
				minDist = 0;
				maxDist = 0;
			}
			else if(def.equals("rockWall"))
			{
				minDist = 0;
				maxDist = 0;
			}
			else if(def.equals("moat"))
			{
				minDist = 0;
				maxDist = 0;
			}
			else if(def.equals("portcullis"))
			{
				minDist = 0;
				maxDist = 0;
			}
			else
			{
				runNum = -100;
				return;
			}
			hasRun = true;
		}
		
		if(runNum == -1)
		{
			if(pos == 1)//LowBar
			{
				goDistance(10.2*12-maxDist, .3);
			}
			else if(pos >= 2 && pos <=5)
			{
				goDistance(6*12-maxDist, .3);
			}
			else
			{
				runNum = -100;
				return;
			}
		}
		if(runNum == -2)
		{
			if(pos == 1)//LowBar
			{
				turn(60);
			}
			else if(pos == 2)
			{
				turn(60);
			}
			else if(pos == 3)
			{
				turn(0);
			}
			else if(pos == 4)
			{
				turn(-60);
			}
			else if(pos == 5)
			{
				turn(-45);
			}
		}
		if(runNum == -3)
		{
			wait(.3);
			shoot.liftSim(-1);
		}
		if(runNum == -4)
		{
			shoot.liftSim(0);
			follow.track();
			if(follow.dir.equals("shoot"))
			{
				wait(.75);
			}
		}
		if(runNum == -5)
		{
			wait(.5);
			shoot.indexAuto(true);
		}
		if(runNum == -6)
			shoot.indexAuto(false);
	}
	
	private void goDistance(double dist, double speed)
	{
		if(!hasRun)
		{
			right.drive(-speed);
			left.drive(speed);
		}
		
		if(isEnc)
		{
			if(!hasRun)
			{
				lEnc.reset();
				rEnc.reset();
				hasRun = true;
			}
			
			large = Math.abs(Math.max(lEnc.getRaw(), rEnc.getRaw()));
			
			if(large*circumference>dist)
			{
				right.drive(0);
				left.drive(0);
				
				hasRun = false;
				if(runNum>=0)
					runNum++;
				else
					runNum--;
			}
		}
		
		//For when the encoders break
		else if(!isEnc)
		{
			double timeWarm = .5;
			int dist2 = 0;
			double timeNeeded = timeWarm + ((dist / circumference) / ((speed * 5310) / (60 * 12.75)));
			if(!hasRun)
			{
				timer.start();
				hasRun = true;
			}
			
			if(timer.get()>timeNeeded)
			{
				right.drive(0);
				left.drive(0);
				
				hasRun = false;
				if(runNum>=0)
					runNum++;
				else
					runNum--;
				timeNeeded = 0;
				
				timer.stop();
				timer.reset();
			}
		}
	}
	
	//Untested
	private void wait(double time)
	{
		if(!hasRun)
		{
			timer.start();
			hasRun = true;
		}
		if(timer.get()== time && hasRun)
		{
			timer.stop();
			timer.reset();
			hasRun = false;
			if(runNum>=0)
				runNum++;
			else
				runNum--;
		}
	}
	
	private void turn(double angle)
	{
		double percent = Math.abs(angle)/360;
		if(!hasRun)
		{
			lEnc.reset();
			rEnc.reset();
		}
		if(!hasRun&&angle<0)
		{
			right.drive(.3);
			left.drive(.3);
			hasRun = true;
		}
		else if(!hasRun&&angle>0)
		{
			right.drive(-.3);
			left.drive(-.3);
			hasRun = true;
		}
		else if(!hasRun&&angle==0)
			hasRun=true;
		
		large = Math.abs(Math.max(lEnc.getRaw(), rEnc.getRaw()));
		double radius = ((27+3/8)*Math.PI);
		if(large*circumference>=radius*percent)
		{
			right.drive(0);
			left.drive(0);
			
			hasRun = false;
			if(runNum>=0)
				runNum++;
			else
				runNum--;
		}
	}
	
	/*private void turn(int angle)
	{
		
	}*/
}
