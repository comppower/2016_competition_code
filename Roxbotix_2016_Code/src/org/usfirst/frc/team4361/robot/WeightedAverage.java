package org.usfirst.frc.team4361.robot;


public class WeightedAverage {
	public double[] centerX;
	public double[] centerY;
	public double[] area;
	private double minA, maxA;
	public boolean cal;
	
	public WeightedAverage(double minA,double maxA)
	{
		centerX = new double[8];
		centerY = new double[8];
		area = new double[8];
		//minA and maxA are used to get three ranges for three motor settings
		this.minA=minA;
		this.maxA=maxA;

		for(int i =0; i<area.length; i++)
		{
			centerX[i]=-1;
			centerY[i]=-1;
			area[i]=-1;
		}
		cal = false;
	}
	//this is just averaging the raw data, which is then put into the tracking method where it is corrected
	public void yIn(double y)
	{
		for(int i=1; i<centerY.length; i++)
		{
			centerY[i-1]=centerY[i];
		}
		centerY[centerY.length-1]=y;
	}
	//)
	public void xIn(double x)
	{
		for(int i=1; i<centerX.length; i++)
		{
			centerX[i-1]=centerX[i];
		}
		centerX[centerX.length-1]=x;
	}
	//inputs area
	public void areaIn(double a)
	{
		for(int i=1; i<area.length; i++)
		{
			area[i-1]=area[i];
		}
		area[area.length-1]=a;
	}
	public int getLength()
	{
		return area.length;
	}
	//averages the array return the filtered value
	//h
	public double getAverage(String val)
	{
		double average =0;
		if(val.toUpperCase().equals("AREA"))
		{
			double areaInc = (maxA-minA)/3;
			for(int i=0; i<area.length; i++)
			{
				if(area.length/2<i)
				{
					average+=area[i];
				}
				else 
				{
					average += area[i]*2;
				}
			}
			average/=12;
			//determines required motor speed based of distance bracket
			//this uses the areaInc to create the intervals for the bracket
			if(average>minA+areaInc*2)
			{
				return.25;
			}
			else if(average>minA+areaInc)
			{
				return.2;
			}
			else
			{
				return.15;
			}
		}
		if(val.toUpperCase().equals("X"))
		{
			for(int i=0; i<centerX.length; i++)
			{
				if(centerX.length/2<i)
				{
					average+=centerX[i];
				}
				else 
				{
					average += centerX[i]*2;
				}
			}
			return average/12;
		}
		if(val.toUpperCase().equals("Y"))
		{
			for(int i=0; i<centerY.length; i++)
			{
				if(centerY.length/2<i)
				{
					average+=centerY[i];
				}
				else 
				{
					average += centerY[i]*2;
				}
			}
			return average/12;
		}
		return -1;

	}


}
