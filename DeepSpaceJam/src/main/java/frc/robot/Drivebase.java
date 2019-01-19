package frc.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Drivebase{
     VictorSP left1,left2,left3,right1,right2,right3;
     SpeedControllerGroup left,right;

    private static final int IDLE = 100;
    private static final int AUTO = 101;
    private static final int TELEOP = 102;
    private static int JOB = IDLE;
    public void init(){
        left1 = new VictorSP(0);
        left2 = new VictorSP(1);
        left3 = new VictorSP(2);
        right1 = new VictorSP(3);
        right2 = new VictorSP(4);
        right3 = new VictorSP(5);
        left = new SpeedControllerGroup(left1,left2,left3);
        right = new SpeedControllerGroup(right1,right2,right3);
    }
    public boolean isIdle(){
        return(JOB == IDLE);
    }
    public void CheckStatus(){
        switch(JOB){
            case IDLE:
        
            break;
            case AUTO:
            trackingCheckStatus();
            break;
            case TELEOP:
            operatorCheckStatus();
            break;
        }
        
    }
    public void trackingCheckStatus(){

    }
    public void operatorCheckStatus(){

    }
    public void startTracking(){
        JOB = AUTO;
    }
    public void autoDrive(double leftPwr, double rtPwr){
        left.set(leftPwr);
        right.set(rtPwr);
    }
    double prev_left = 0;
    double prev_right = 0;
    public void drive(double left, double right){
        //write/copy in drive code smoothing and safety methods
        left = smooth(left, 0.1,0.9);
        right = smooth(right,0.1,0.9);
        left = safety(left, prev_left, 0.3);
        right = safety(right, prev_right, 0.3);
        prev_left = left;
        prev_right = right;
    }
    private double safety(double cmdVal, double prevVal, double maxChange) {
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
			return aValue * aValue * aValue * (value / aValue);
	}
}