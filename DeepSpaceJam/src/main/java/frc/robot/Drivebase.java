package frc.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Drivebase{
     private static VictorSP left1,left2,left3,right1,right2,right3;
     private static SpeedControllerGroup left,right;
    private static boolean selfDriving = false;
    public static void init(){
        left1 = new VictorSP(0);
        left2 = new VictorSP(1);
        left3 = new VictorSP(2);
        right1 = new VictorSP(3);
        right2 = new VictorSP(4);
        right3 = new VictorSP(5);
        left = new SpeedControllerGroup(left1,left2,left3);
        right = new SpeedControllerGroup(right1,right2,right3);
        left.setInverted(true);
    }

    private static double prev_left = 0;
    private static double prev_right = 0;
    public static void drive(double l, double r){
        //write/copy in drive code smoothing and safety methods
        l = smooth(l, 0.02,0.9);
        r = smooth(r,0.02,0.9);
        l = safety(l, prev_left, 0.3);
        r = safety(r, prev_right, 0.3);
        left.set(l* Robot.speed);
        right.set(r* Robot.speed);
        prev_left = l;
        prev_right = r;

    }
    private static double safety(double cmdVal, double prevVal, double maxChange) {
		double diff = cmdVal - prevVal;
		if (Math.abs(diff) < maxChange) {
			return cmdVal;
		} else {
			if (diff > 0) {
				return prevVal + maxChange;
			} else {
				return prevVal - maxChange;
			}
		
        }
    }
    private static double smooth(double value, double deadBand, double max) {
		double aValue = Math.abs(value);
		if (aValue > max)
			return (value / aValue);
		else if (aValue < deadBand)
			return 0;
		else
			return aValue * aValue * (value / aValue);
    }
    public static boolean cameraControlled(){
        return selfDriving;
    }
    public static void selfDrive(){
        selfDriving = true;
    }
    public static void returnControl(){
        selfDriving = false;
    }
}