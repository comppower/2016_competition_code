package org.usfirst.frc.team4361.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

public class Digit 
{
	//I2C address of the digit board is 0x70
	I2C i2c = new I2C(Port.kMXP, 0x70);
	
	// Buttons A and B are keyed to digital inputs 19 and 20	
	//DigitalInput buttonA = new DigitalInput(19);
	//DigitalInput buttonB = new DigitalInput(20);
	
	// The potentiometer is keyed to AI 3	
	//AnalogInput pot = new AnalogInput(3);
		
	byte[] byte1;
	byte[][] charreg;
	byte[][] loadingCircle;
		
	public Digit() 
	{
		// set up the board - turn on, set blinking and brightness   
    	byte[] osc = new byte[1];
    	byte[] blink = new byte[1];
    	byte[] bright = new byte[1];
    	osc[0] = (byte)0x21;
    	blink[0] = (byte)0x81;
    	bright[0] = (byte)0xEF;

		i2c.writeBulk(osc);
		Timer.delay(.01);
		i2c.writeBulk(bright);
		Timer.delay(.01);
		i2c.writeBulk(blink);
		Timer.delay(.01);
		
		byte1 = new byte[10];
		charreg = new byte[101][2];
	   	 //charreg is short for character registry
	   	charreg[0][0] = (byte)0b00000110; charreg[0][1] = (byte)0b00000000; //1
	   	charreg[1][0] = (byte)0b11011011; charreg[1][1] = (byte)0b00000000; //2
	   	charreg[2][0] = (byte)0b11001111; charreg[2][1] = (byte)0b00000000; //3
	   	charreg[3][0] = (byte)0b11100110; charreg[3][1] = (byte)0b00000000; //4
	   	charreg[4][0] = (byte)0b11101101; charreg[4][1] = (byte)0b00000000; //5
	   	charreg[5][0] = (byte)0b11111101; charreg[5][1] = (byte)0b00000000; //6
	   	charreg[6][0] = (byte)0b00000111; charreg[6][1] = (byte)0b00000000; //7
	   	charreg[7][0] = (byte)0b11111111; charreg[7][1] = (byte)0b00000000; //8
	   	charreg[8][0] = (byte)0b11101111; charreg[8][1] = (byte)0b00000000; //9
	   	charreg[9][0] = (byte)0b00111111; charreg[9][1] = (byte)0b00000000; //0
	   	charreg[10][0] = (byte)0b11110111; charreg[10][1] = (byte)0b00000000; //A
	   	charreg[11][0] = (byte)0b10001111; charreg[11][1] = (byte)0b00010010; //B
	   	charreg[12][0] = (byte)0b00111001; charreg[12][1] = (byte)0b00000000; //C
	   	charreg[13][0] = (byte)0b00001111; charreg[13][1] = (byte)0b00010010; //D
	   	charreg[14][0] = (byte)0b11111001; charreg[14][1] = (byte)0b00000000; //E
	   	charreg[15][0] = (byte)0b11110001; charreg[15][1] = (byte)0b00000000; //F
	   	charreg[16][0] = (byte)0b10111101; charreg[16][1] = (byte)0b00000000; //G
	   	charreg[17][0] = (byte)0b11110110; charreg[17][1] = (byte)0b00000000; //H
	   	charreg[18][0] = (byte)0b00001001; charreg[18][1] = (byte)0b00010010; //I
	   	charreg[19][0] = (byte)0b00011110; charreg[19][1] = (byte)0b00000000; //J
	   	charreg[20][0] = (byte)0b01110000; charreg[20][1] = (byte)0b00001100; //K
	   	charreg[21][0] = (byte)0b00111000; charreg[21][1] = (byte)0b00000000; //L
	   	charreg[22][0] = (byte)0b00110110; charreg[22][1] = (byte)0b00000101; //M
	   	charreg[23][0] = (byte)0b00110110; charreg[23][1] = (byte)0b00001001; //N
	   	charreg[24][0] = (byte)0b00111111; charreg[24][1] = (byte)0b00000000; //O
	   	charreg[25][0] = (byte)0b11110011; charreg[25][1] = (byte)0b00000000; //P
	   	charreg[26][0] = (byte)0b00111111; charreg[26][1] = (byte)0b00001000; //Q
	   	charreg[27][0] = (byte)0b11110011; charreg[27][1] = (byte)0b00001000; //R
	   	charreg[28][0] = (byte)0b10001101; charreg[28][1] = (byte)0b00000001; //S
	   	charreg[29][0] = (byte)0b00000001; charreg[29][1] = (byte)0b00010010; //T
	   	charreg[30][0] = (byte)0b00111110; charreg[30][1] = (byte)0b00000000; //U
	   	charreg[31][0] = (byte)0b00110000; charreg[31][1] = (byte)0b00100100; //V
	   	charreg[32][0] = (byte)0b00110110; charreg[32][1] = (byte)0b00101000; //W
	   	charreg[33][0] = (byte)0b00000000; charreg[33][1] = (byte)0b00101101; //X
	   	charreg[34][0] = (byte)0b00000000; charreg[34][1] = (byte)0b00010101; //Y
	   	charreg[35][0] = (byte)0b00001001; charreg[35][1] = (byte)0b00100100; //Z
	   	charreg[36][0] = (byte)0b00000000; charreg[36][1] = (byte)0b10000000; //Space
	   	charreg[100][0] = (byte)0b11111111; charreg[100][1] = (byte)0b01111111; //Error
	   	
	   	loadingCircle = new byte[15][2];
	   	
	   	loadingCircle[0][0] = (byte)0b00111111; loadingCircle[0][1] = (byte)0b00000001;
	   	loadingCircle[1][0] = (byte)0b00111111; loadingCircle[1][1] = (byte)0b00000010;
	   	loadingCircle[2][0] = (byte)0b00111111; loadingCircle[2][1] = (byte)0b00000100;
	   	loadingCircle[3][0] = (byte)0b10111111; loadingCircle[3][1] = (byte)0b00000000;
	   	loadingCircle[4][0] = (byte)0b00111111; loadingCircle[4][1] = (byte)0b00001000;
	   	loadingCircle[5][0] = (byte)0b00111111; loadingCircle[5][1] = (byte)0b00010000;
	   	loadingCircle[6][0] = (byte)0b00111111; loadingCircle[6][1] = (byte)0b00100000;
	   	loadingCircle[7][0] = (byte)0b01111111; loadingCircle[7][1] = (byte)0b00000000;
	}
	
	protected void print(String output)
	{
		output = output.toUpperCase();
		if(output.length()%4!=0)
			output = output + "    ";
		output = output.substring(0, output.length() - output.length()%4);
		
		
		int[] integerString = convertToInts(output);
		
		for(int c = 0; c < 10; c++)
    	{
    		byte1[c] = (byte)(0b00000000) & 0xFF;
    	}
		
		for(int c = 0; c<integerString.length/4; c++)
		{
			byte1[0] = (byte)(0b0000111100001111);
			
    		byte1[2] = charreg[integerString[4*c+3]][0];
    		byte1[3] = charreg[integerString[4*c+3]][1];
    		byte1[4] = charreg[integerString[4*c+2]][0];
    		byte1[5] = charreg[integerString[4*c+2]][1];
    		byte1[6] = charreg[integerString[4*c+1]][0];
    		byte1[7] = charreg[integerString[4*c+1]][1];
    		byte1[8] = charreg[integerString[4*c]][0];
    		byte1[9] = charreg[integerString[4*c]][1];
    		
    		// send the array to the board
    		i2c.writeBulk(byte1);
    		
    		Timer.delay(1);
		}
	}
	protected void printScroll(String output)
	{
		output = output.toUpperCase();
		if(output.length()%4!=0)
			output = output + "    ";
		output = output.substring(0, output.length() - output.length()%4);
		
		
		int[] integerString = convertToInts(output);
		
		for(int c = 0; c < 10; c++)
    	{
    		byte1[c] = (byte)(0b00000000) & 0xFF;
    	}
		
		for(int c = 0; c<integerString.length-4; c++)
		{
			byte1[0] = (byte)(0b0000111100001111);
			
    		byte1[2] = charreg[integerString[c+3]][0];
    		byte1[3] = charreg[integerString[c+3]][1];
    		byte1[4] = charreg[integerString[c+2]][0];
    		byte1[5] = charreg[integerString[c+2]][1];
    		byte1[6] = charreg[integerString[c+1]][0];
    		byte1[7] = charreg[integerString[c+1]][1];
    		byte1[8] = charreg[integerString[c]][0];
    		byte1[9] = charreg[integerString[c]][1];
    		
    		// send the array to the board
    		i2c.writeBulk(byte1);
    		
    		Timer.delay(.5);
		}
	}
	
    public void testLoading(boolean check)
    {
    	if(check)
    	{    	
    		for(int c = 0; c < 10; c++)
        	{
        		byte1[c] = (byte)(0b00000000) & 0xFF;
        	}
    		
        	for(int i = 0; i < loadingCircle.length; i++)
        	{
        		byte1[0] = (byte)(0b0000111100001111);
        		
        		byte1[8] = loadingCircle[i][0];
        		byte1[9] = loadingCircle[i][1];
        		
        		byte1[2] = loadingCircle[i][0];
        		byte1[3] = loadingCircle[i][1];
        		
        		i2c.writeBulk(byte1);
        		
        		Timer.delay(.2);
        	}
    	}
    }
    
    private int[] convertToInts(String a)
	{
		int[] temp = new int[a.length()];
		for(int i = 0; i < a.length(); i++)
		{
			if(a.substring(i, i+1).equals("1"))
			{
				temp[i] = 0;
			}
			else if(a.substring(i, i+1).equals("2"))
			{
				temp[i] = 1;
			}
			else if(a.substring(i, i+1).equals("3"))
			{
				temp[i] = 2;
			}
			else if(a.substring(i, i+1).equals("4"))
			{
				temp[i] = 3;
			}
			else if(a.substring(i, i+1).equals("5"))
			{
				temp[i] = 4;
			}
			else if(a.substring(i, i+1).equals("6"))
			{
				temp[i] = 5;
			}
			else if(a.substring(i, i+1).equals("7"))
			{
				temp[i] = 6;
			}
			else if(a.substring(i, i+1).equals("8"))
			{
				temp[i] = 7;
			}
			else if(a.substring(i, i+1).equals("9"))
			{
				temp[i] = 8;
			}
			else if(a.substring(i, i+1).equals("0"))
			{
				temp[i] = 9;
			}
			else if(a.substring(i, i+1).equals("A"))
			{
				temp[i] = 10;
			}
			else if(a.substring(i, i+1).equals("B"))
			{
				temp[i] = 11;
			}
			else if(a.substring(i, i+1).equals("C"))
			{
				temp[i] = 12;
			}
			else if(a.substring(i, i+1).equals("D"))
			{
				temp[i] = 13;
			}
			else if(a.substring(i, i+1).equals("E"))
			{
				temp[i] = 14;
			}
			else if(a.substring(i, i+1).equals("F"))
			{
				temp[i] = 15;
			}
			else if(a.substring(i, i+1).equals("G"))
			{
				temp[i] = 16;
			}
			else if(a.substring(i, i+1).equals("H"))
			{
				temp[i] = 17;
			}
			else if(a.substring(i, i+1).equals("I"))
			{
				temp[i] = 18;
			}
			else if(a.substring(i, i+1).equals("J"))
			{
				temp[i] = 19;
			}
			else if(a.substring(i, i+1).equals("K"))
			{
				temp[i] = 20;
			}
			else if(a.substring(i, i+1).equals("L"))
			{
				temp[i] = 21;
			}
			else if(a.substring(i, i+1).equals("M"))
			{
				temp[i] = 22;
			}
			else if(a.substring(i, i+1).equals("N"))
			{
				temp[i] = 23;
			}
			else if(a.substring(i, i+1).equals("O"))
			{
				temp[i] = 24;
			}
			else if(a.substring(i, i+1).equals("P"))
			{
				temp[i] = 25;
			}
			else if(a.substring(i, i+1).equals("Q"))
			{
				temp[i] = 26;
			}
			else if(a.substring(i, i+1).equals("R"))
			{
				temp[i] = 27;
			}
			else if(a.substring(i, i+1).equals("S"))
			{
				temp[i] = 28;
			}
			else if(a.substring(i, i+1).equals("T"))
			{
				temp[i] = 29;
			}
			else if(a.substring(i, i+1).equals("U"))
			{
				temp[i] = 30;
			}
			else if(a.substring(i, i+1).equals("V"))
			{
				temp[i] = 31;
			}
			else if(a.substring(i, i+1).equals("W"))
			{
				temp[i] = 32;
			}
			else if(a.substring(i, i+1).equals("X"))
			{
				temp[i] = 33;
			}
			else if(a.substring(i, i+1).equals("Y"))
			{
				temp[i] = 34;
			}
			else if(a.substring(i, i+1).equals("Z"))
			{
				temp[i] = 35;
			}
			else if(a.substring(i, i+1).equals(" "))
			{
				temp[i] = 36;
			}
			else
			{
				temp[i] = 100;
			}
		}
		
		return temp;
	}

}
