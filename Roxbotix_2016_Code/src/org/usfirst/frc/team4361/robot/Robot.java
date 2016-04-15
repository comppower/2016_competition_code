
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
    private SendableChooser chooser, sec;
	
    //Roxbotix Variables
    private Autonomous auto;
    private Portcullis port;
    
    private Joystick[] stick;
    
    private double stick0X, stick0Y, stick1X, stick1Y;
    
    private CANTalon[] CAN;
    
    private Drive left, right;
    
    private Shooter shooter;
    
    private TargetFollow track;
    
    private DigitalInput[] limitSwitch;
    
    private Relay[] light;
    
    double drive;
    boolean change;
    
    private Encoder lEnc, rEnc;
    
    //USB Camera
    int session;
    Image frame;
    
    //Calibration for VT
    double xCal;
    double yCal;
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
    		limitSwitch[i] = new DigitalInput(i);
    	}
        
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
        
        CANTalon[] leftDrive = {CAN[9], CAN[1]};
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
    	
    	xCal=94;
    	yCal=97;
    	track = new TargetFollow(left, right, shooter,xCal,yCal);
    	
    	stick0X = stick[0].getAxis(Joystick.AxisType.kX);
    	stick0Y = stick[0].getAxis(Joystick.AxisType.kY);
    	stick1X = stick[1].getAxis(Joystick.AxisType.kX);
    	stick1Y = stick[1].getAxis(Joystick.AxisType.kY);
    	
    	change = true;
        
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
        
        sec = new SendableChooser();
        sec.addDefault("None", "none");
        sec.addDefault("High Goal", "high");
        
        SmartDashboard.putData("Auto choices", chooser);
        SmartDashboard.putData("Secondary", sec);
        SmartDashboard.putNumber("PosGet", 0);
        
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
	 *t
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
    	secSelected = (String) sec.getSelected();//Merge
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     * 
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
    	
    	//Merge
    	switch(secSelected){
    	case "high":
    	auto.high((int) SmartDashboard.getNumber("posGet"), autoSelected);
    		break;
    	
    	default:
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
		
		//light[0].set(Relay.Value.kForward);
		//light[1].set(Relay.Value.kForward);
		
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
		
    	
    	else if(stick[1].getRawButton(1))
    	{
    		track.track();
    	
    	}
		
    	else if(stick[1].getRawButton(3))
    	{
    		//light[1].set(Relay.Value.kForward);
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
		if(stick[2].getIsXbox()&&!stick[1].getRawButton(1))
		{
			shooter.liftSim(stick[2].getRawAxis(5)*.9);
			shooter.shoot(stick[2].getRawButton(5), stick[2].getRawButton(6));
			port.lift(-stick[2].getRawAxis(1)*.9);
		}
		else if(!stick[2].getIsXbox()&&!stick[1].getRawButton(1))
		{
			shooter.liftSim(stick[2].getAxis(Joystick.AxisType.kY));
			shooter.shoot(stick[2].getRawButton(5), stick[2].getRawButton(3));
		   	port.lift(stick[2].getPOV());
		}

    	
    	//Drives indexer
    	shooter.indexAuto(stick[0].getRawButton(1));
    	//shooter.index(stick[0].getRawButton(1), stick[1].getRawButton(1));	
    	NetworkTable table =  NetworkTable.getTable("GRIP/myContoursReport");
    	double[] deafultVal = {0};
    	double[] centerX = table.getNumberArray("centerX", deafultVal);
	   	double[] centerY = table.getNumberArray("centerY", deafultVal);
	   	if(centerX.length>0&&centerY.length>0)
        {
	   		printUI(centerX[0], centerY[0]);
        }
	   	else
	   	{
	   		printUI(-1,-1);
	   	}

	   	SmartDashboard.putNumber("motor", CAN[0].get());
	   	SmartDashboard.putString("dir",track.dir);
	   	SmartDashboard.putNumber("Average x", track.ave.getAverage("x"));
	   	SmartDashboard.putNumber("Average y", track.ave.getAverage("y"));
	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void printUI(double yCur, double xCur)
    {
		//supply correct x and y vals for calibration
		SmartDashboard.putNumber("ROTATION", (xCal-xCur)/xCal);
		SmartDashboard.putNumber("ELEVATION", (yCal-yCur)/yCal);
		SmartDashboard.putBoolean("Alignment: X", Math.abs(xCal-xCur)/xCal<.05);
		SmartDashboard.putBoolean("Alignment: Y", Math.abs(yCal-yCur)/yCal<.05);
    }
    public void cameraUI()
    {
    	try
    	{
			// Starts USB camera
			NIVision.IMAQdxStartAcquisition(session);
			// Instantiate rectangles (fix the location of the rectangles
			NIVision.Rect rightRect = new NIVision.Rect(99, 440, 176, 13);
			NIVision.Rect midRect = new NIVision.Rect(135, 132, 25, 25);
			// Draw rectangles on screen
			NIVision.IMAQdxGrab(session, frame, 1);
			NIVision.imaqDrawShapeOnImage(frame, frame, rightRect, DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 200f);
			NIVision.imaqDrawShapeOnImage(frame, frame, midRect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 200f);
			// display the new image
			CameraServer.getInstance().setImage(frame);
		}
    	catch(Exception ace)
    	{
    		System.out.println(ace.getMessage());
    	}
    }
    public void testPeriodic() 
    {
    	
    }
    
    
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
