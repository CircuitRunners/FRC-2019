package org.usfirst.frc.team1002.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Drivebase {
	static TalonSRX masterLeft, masterRight;
	static VictorSPX slaveLeft1, slaveLeft2, slaveRight1, slaveRight2;
	public static void init() {
		masterLeft = new TalonSRX(20);
		masterRight = new TalonSRX(21);
		slaveLeft1 = new VictorSPX(22);
		slaveLeft2 = new VictorSPX(23);
		slaveRight1 = new VictorSPX(24);
		
	}
	
	public void config(TalonSRX thisTalon, boolean isLeft) {
		thisTalon.clearStickyFaults();
		final int driveTimeoutMs = 10;
		thisTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
				driveTimeoutMs);
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, driveTimeoutMs);
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, driveTimeoutMs);
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, driveTimeoutMs);
		/* set the peak and nominal outputs */
		thisTalon.configNominalOutputForward(0, driveTimeoutMs);
		thisTalon.configNominalOutputReverse(0, driveTimeoutMs);
		thisTalon.configPeakOutputForward(1, driveTimeoutMs);
		thisTalon.configPeakOutputReverse(-1, driveTimeoutMs);

		/* set closed loop gains in slot0 - see documentation */
		thisTalon.selectProfileSlot(0, 0);
		thisTalon.config_kF(0, 0.287, driveTimeoutMs);
		thisTalon.config_kP(0, 0.4, driveTimeoutMs);
		thisTalon.config_kI(0, 0, driveTimeoutMs);
		thisTalon.config_kD(0, 0, driveTimeoutMs);
		/* set acceleration and cruise velocity - see documentation */
		thisTalon.configMotionCruiseVelocity(10000, driveTimeoutMs);
		thisTalon.configMotionAcceleration(10000, driveTimeoutMs);
		/* zero the sensor */
		thisTalon.setSelectedSensorPosition(0, 0, driveTimeoutMs);

		//thisTalon.configMotionCruiseVelocity((int) .elevCruiseVel, driveTimeoutMs);
		//thisTalon.configMotionAcceleration((int) .elevCruiseAccel, driveTimeoutMs);
	}
	
	public static void drive() {
		
	}
}
