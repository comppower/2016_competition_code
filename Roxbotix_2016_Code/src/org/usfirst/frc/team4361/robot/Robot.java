
package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private final String defaultAuto = "Drive to Obstacle";
    private String autoSelected, secSelected;
    private SendableChooser chooser;
	
    //Roxbotix Variables
    private Autonomous auto;
    private Portcullis port;
    
    private Joystick[] stick;
    
    private double stick0X, stick0Y, stick1X, stick1Y;
    
    private CANTalon[] CAN;
    
    private Drive left, right;
    
    private Shooter shooter;
    
    private Digit digit;
    
    private DigitalInput[] limitSwitch;
    
    private Relay[] light;
    
    double drive;
    boolean change;
    
    //GRIP 
    NetworkTable table;
	final double[] defaultVal={0.0};
	Tracking track;
    WeightedAverage ave; 
    private Encoder lEnc, rEnc;
    
    //USB Camera
    int session;
    Image frame;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		
    	lEnc = new Encoder(8, 9, true);
    	rEnc = new Encoder(6, 7, true);
    	
    	drive = 1.0;
    	
    	light = new Relay[2];
    	for(int i = 0; i < light.length; i++)
    	{
    		light[i] = new Relay(i);
    	}
    	
    	limitSwitch = new DigitalInput[2];
    	for(int i = 1; i < limitSwitch.length; i++)
    	{
    		limitSwitch[1] = new DigitalInput(1);
    	}
     	
    	//Roxbotix Variables
        digit = new Digit();
        
        stick = new Joystick[3];
        for(int i = 0; i < stick.length; i++)
        {
        	stick[i] = new Joystick(i);
        }
        
        CAN = new CANTalon[12];
        for(int i = 0; i < CAN.length; i++)
        {
        	CAN[i] = new CANTalon(i);
        }
        
        CANTalon[] leftDrive = {CAN[0], CAN[1]};
    	left = new Drive(leftDrive);
    	
    	CANTalon[] rightDrive = {CAN[2], CAN[3]};
    	right = new Drive(rightDrive);
    	
    	CANTalon[] shoot = { CAN[4], CAN[5]};
    	CANTalon[] shooterLift = {CAN[6], CAN[7]};
    	CANTalon[] index = {CAN[8]};
    	shooter = new Shooter(shoot, shooterLift, index, limitSwitch, light);
    	
    	CANTalon[] ChevelLift = {CAN[11]};
    	
    	//lEnc = new Encoder(0, 1, true, EncodingType.k1X); rEnc = new Encoder(2, 3, true, EncodingType.k1X);
    	
    	port = new Portcullis(CAN[10], light[1]);
    	
    	//auto = new Autonomous(left, right, shooter);
    	auto = new Autonomous(left, right, shooter, port, lEnc, rEnc);
    	
    	stick0X = stick[0].getAxis(Joystick.AxisType.kX);
    	stick0Y = stick[0].getAxis(Joystick.AxisType.kY);
    	stick1X = stick[1].getAxis(Joystick.AxisType.kX);
    	stick1Y = stick[1].getAxis(Joystick.AxisType.kY);
    	
    	digit.print("4361");
    	change = true;
    	
    	
    	//GRIP
    	

        //set up corrected centers for the robot
        table = NetworkTable.getTable("GRIP/myContoursReport");
        track = new Tracking(133,105);
        ave = new WeightedAverage(10,700);
        
    	//Default Variables
    	chooser = new SendableChooser();
        chooser.addDefault("Drive to Obstacle", "default");
        chooser.addObject("Low Bar", "lowbar");
        chooser.addObject("Portcullis", "portcullis");
        chooser.addObject("Chevel De Frise", "chevelDeFrise");
        chooser.addObject("Moat", "moat");
        chooser.addObject("Ramparts", "ramparts");
        chooser.addObject("Drawbridge", "drawbridge");
        chooser.addObject("Sally Port", "sallyPort");
        chooser.addObject("Rock Wall", "rockWall");
        chooser.addObject("Rough Terrain", "roughTerrain");
        
        SmartDashboard.putData("Auto choices", chooser);
        
        //USB CAM INIT
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam1",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *h
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case "portcullis":
    	auto.portcullis();
            break;
    	case "chevelDeFrise":
    	auto.chevelDeFrise();
            break;
    	case "moat":
    	auto.moat();
            break;
    	case "ramparts":
    	auto.ramparts();
            break;
    	case "drawbridge":
    	auto.drawbridge();
            break;
    	case "sallyPort":
    	auto.sallyPort();
            break;
    	case "rockWall":
    	auto.rockWall();
            break;
    	case "roughTerrain":
    	auto.roughTerrain();
            break;
    	case "lowbar":
        auto.lowBar();
        	break;
    	
    	case defaultAuto:
    	default:
    	auto.defaultGoToObstacle();
            break;
    	}
    	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
    	cameraUI();
		// Sets joystick values
		stick0X = stick[0].getAxis(Joystick.AxisType.kX);
		stick0Y = stick[0].getAxis(Joystick.AxisType.kY);
		stick1X = stick[1].getAxis(Joystick.AxisType.kX);
		stick1Y = stick[1].getAxis(Joystick.AxisType.kY);
		
		//sets vision tracking vals

    	
        //adjusts gear box
    	SmartDashboard.putDouble("gear", drive);
    	if(drive == 1 && stick[0].getRawButton(3)&&change)
    	{
    		drive = .75;
    		change = false;
    	}
    	else if(drive == .75 && stick[0].getRawButton(3)&&change)
    	{
    		drive = 1;
    		change = false;
    	}
    	
    	if(!stick[0].getRawButton(3))
    		change = true;
    	
    	//tracking data
    	double[] deafultVal = new double[0];
    	double[] centerX = table.getNumberArray("centerX", deafultVal);
    	double[] centerY = table.getNumberArray("centerY", deafultVal);
    	double[] width = table.getNumberArray("width",deafultVal);
    	double[] length = table.getNumberArray("height", deafultVal);
    	double[] area = table.getNumberArray("area",deafultVal);
    	String dir = "";
    	double speed=.15;
    	//perfect rotation and drive code
		if (stick[0].getRawButton(4)) 
		{
			if (stick0Y < -.07 || stick0Y > .07) 
			{
				right.drive(stick0Y * drive);
				left.drive(-stick0Y * drive);
			} 
			else 
			{
				right.drive(0);
				left.drive(0);
			}
		}
		//tracking portion
        
    	
    	else if(centerX.length>0&&stick[1].getRawButton(1))
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
    		if(dir.equals("forward"))
    		{
    			left.drive(-speed*1.25);
    			right.drive(speed);
    		}
    		if(dir.equals("back"))
    		{
    			left.drive(speed*1.25);
    			right.drive(-speed*1.25);
    		}
    	}
		
		//Perfect forward drive
		else if (stick[0].getRawButton(6)) 
		{
			if (stick0X < -.07 || stick0X > .07) 
			{
				right.drive(stick0X * drive);
				left.drive(stick0X * drive);
			} 
			else 
			{
				right.drive(0);
				left.drive(0);
			}
		} 
		//standard drive
		else 
		{
			// Drives Chassis
			if (stick0Y < -.07 || stick0Y > .07)
				right.drive(stick0Y * drive);
			else
				right.drive(0);
			if (stick1Y < -.07 || stick1Y > .07)
				left.drive(-stick1Y * drive);
			else
				left.drive(0);
		}
    	//end untested code
	    	
	    //Drives shooter
	    //shooter.lift(stick[2].getAxis(Joystick.AxisType.kY));
		if(stick[2].getIsXbox())
		{
			shooter.liftSim(stick[2].getRawAxis(1));
			shooter.shoot(stick[2].getRawButton(5), stick[2].getRawButton(6));
			port.lift(stick[2].getRawAxis(5));
		}
		else
		{
			shooter.liftSim(stick[2].getAxis(Joystick.AxisType.kY));
			shooter.shoot(stick[2].getRawButton(5), stick[2].getRawButton(3));
		   	port.lift(stick[2].getPOV());
		}

    	
    	//Drives indexer
    	shooter.indexAuto(stick[0].getRawButton(1));
    	//shooter.index(stick[0].getRawButton(1), stick[1].getRawButton(1));
    	
    	//Untested
 
    	       
        //Prints out UI (untested code)
        if(centerX.length>0&&centerY.length>0)
        {
        	printUI(centerX[0], centerY[0]);
        }
        else
        {
        	printUI(-1,-1);
        }
        //end untested code
       


    }
    
    /**
     * This function is called periodically during test mode
     */
    
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
    public void cameraUI()
    {
    	//Starts USB camera
    	NIVision.IMAQdxStartAcquisition(session);
    	//Instantiate rectangles (fix the location of the rectangles
    	NIVision.Rect rightRect = new NIVision.Rect(99,440,176,13);
    	NIVision.Rect midRect = new NIVision.Rect(135,132,25,25);
    	//Draw rectangles on screen
    	NIVision.IMAQdxGrab(session, frame, 1);
    	NIVision.imaqDrawShapeOnImage(frame, frame, rightRect,
    			DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 200f);
    	NIVision.imaqDrawShapeOnImage(frame, frame, midRect,
    				DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 200f);
    	//display the new image
    	CameraServer.getInstance().setImage(frame);

    }
    public void testPeriodic() 
    {
    	
    }
    
    //Prints out relevant data on the smartdashboard
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
    
    //not working
  
    
    public void autoAlign(double yCur, double xCur, double areaCur)
    {
    	/*
    	//use if statements so the index is updated with every move, and it is more effecient
    	 check to see if the robot is out of alignment
    	I will need to determine the expected percent error
    	 from testing
    	
    	//Calibration values
    	double xCal = 156;
    	double yCal = 31;
    	double areaCal=10;
    	if(Math.abs(xCur-xCal)/xCal>.05)
    	{
    		//set the talons to (centerX-CORRECTED_X)/CORRECTED_X) with the proper negative/positives
    		if(xCur-xCal>0)
    		{
    			right.drive(-.2);
    			left.drive(-.2);
    			SmartDashboard.putString("vision", "left");
    		}
    		else if(xCur-xCal<0)
    		{
    			right.drive(.2);
    			left.drive(.2);
    			SmartDashboard.putString("vision", "right");
    		}
    	}
    	else
    	{
    		right.drive(0);
    		left.drive(0);
    	}
    	if((yCur-yCal)>0)
    	{
    		//set the talons to ((centerY-CORRECTED_Y)/(Math.abs(centerY-CORRECTED_Y))), or if statements
    		// becuase this value needs to be about 1
    		
    		//find where (0,0) is 
    		right.drive(-.3);
    		left.drive(.3);
    		SmartDashboard.putString("vision", "forward");
    	}
    	else if(yCur-yCal<0again, check (0,0) spot)	
    	{
    		right.drive(.3);
    		left.drive(-.3);
    		SmartDashboard.putString("vision", "back");
    	}
    	else
    	{
    		right.drive(0);
    		left.drive(0);
    	}
    	if((Math.abs(xCur-xCal)/xCal)<.05 && Math.abs((yCur-yCal)/yCal)<.05)
    	{
    		if(areaCur>areaCal)//only fires when lined up, otherwise it will keep the ball, to make it more effecient for the driver (long story)
    		// possibly put or statement for timer, it depends whether keeping the ball or firing is more important
    		{
    			//fire the bloody thing
    			SmartDashboard.putString("vision", "fire");
    			right.drive(0);
    			left.drive(0);
    			shooter.shoot(false,true);
    		}
    		else
    		{
    			SmartDashboard.putString("vision", "go forward");
    			right.drive(-.2);
    			left.drive(.2);
    		}
    	}
    }*/
    }
}
