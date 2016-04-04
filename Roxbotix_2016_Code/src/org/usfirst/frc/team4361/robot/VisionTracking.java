package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.RGBImage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ParticleFilterCriteria2;

public class VisionTracking {
	
	private final String CAMHOST = "10.43.61.11";

	private final int VIEW = 10;//temp, Might not be neccesary
	private final int MINSIZE = 10;
	
	private final int HORZPOS = 0;
	private final int VERTPOS = 0;
	
	private ParticleFilterCriteria2[] cc;
	private ParticleAnalysisReport[] report;
	
	private int bestTarg;
	
	private  static AxisCamera cam;
	
	Digit digit = new Digit();
	
	public VisionTracking()
	{
		
		cam = new AxisCamera(CAMHOST);
        cam.writeBrightness(100);
        cam.writeColorLevel(50);
        cam.writeResolution(AxisCamera.Resolution.k320x240);
        cam.writeMaxFPS(16);
        
        cc = new ParticleFilterCriteria2[1];
        cc[0] = new ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, MINSIZE, 700, 0, 0);
        
        bestTarg = -1;     
	}
	
	public int tryTracking()
	{
		
		try
		{

			BinaryImage filteredImage = takePic();
			
			digit.print("wrong   ");
			
			report = filteredImage.getOrderedParticleAnalysisReports();//Code is causing Vision Exception. No known reason.
			
			if(report.length == 0)
				return -1;
			
			if(report.length != 3 || true)
			{
				for(int i = 0; i < report.length; i++)
				{
					if(report[i].boundingRectLeft==-1)
						return 1;
					if(report[i].boundingRectLeft+report[i].imageWidth==1)
						return 2;
				}
			}
			
			for(int i = 0; i < report.length; i++)
			{
				if(report[bestTarg].imageWidth<report[i].imageWidth)
				{
					bestTarg = i;
				}
			}
			
			if(report[bestTarg].center_mass_x_normalized<HORZPOS-.03)
			{
				return 2;
			}
			if(report[bestTarg].center_mass_x_normalized>HORZPOS+.03)
			{
				return 1;
			}
			
			if(report[bestTarg].center_mass_y_normalized<VERTPOS-.03)
			{
				return 4;
			}
			if(report[bestTarg].center_mass_y_normalized>VERTPOS+.03)
			{
				return 3;
			}
			else
			{
				return 0;
			}
			
		}
		catch(Exception ace)
		{
			SmartDashboard.putString("Exception2", ace.getMessage());
			//System.out.print(ace.getMessage());
			return 5;
		}		
		
	}
	
	public BinaryImage takePic()
	{
		BinaryImage thresholdImage;
		BinaryImage filteredImage;
		try
		{
			ColorImage image = new RGBImage();
			cam.getImage(image);
			
			thresholdImage = image.thresholdRGB(0,255,0,255,0,255);//(235, 245, 235, 245, 235, 245);
			
			filteredImage = thresholdImage.particleFilter(cc);//Code is causing Vision Exception. No known reason.
			return filteredImage;
		}
		catch(Exception ace)
		{
			SmartDashboard.putString("Exception1", ace.getMessage());
			//System.out.print(ace.getMessage());
			ace.printStackTrace();
			return null;
		}
			
	}
}
