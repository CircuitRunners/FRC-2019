/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//import edu.wpi.first.wpilibj.Talon;
/**
 * Add your docs here.
 * 
 */
public class Elevator {
    private static TalonSRX elevator;
    public static void init(){
        elevator = new TalonSRX(9);
        elevator.clearStickyFaults();
    }
    public static void run(){
        
    }
    int elevTimeoutMs = 10;
    int elevPIDLoopIdx = 0;
    int elevSlotIdx = 0;
    int elevCV = 15000;
    int maxElevCV = 18000;
    int elevCA = 15000;
    int maxElevCA = 18000;
    public void talonConfig(TalonSRX thisTalon, boolean inverted) {
		/* first choose the sensor */
		thisTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, elevPIDLoopIdx,
				elevTimeoutMs);
		thisTalon.setSensorPhase(true);
		thisTalon.setInverted(false);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, elevTimeoutMs);
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, elevTimeoutMs);
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, elevTimeoutMs);
		/* set the peak and nominal outputs */
		thisTalon.configNominalOutputForward(0, elevTimeoutMs);
		thisTalon.configNominalOutputReverse(0, elevTimeoutMs);
		thisTalon.configPeakOutputForward(1, elevTimeoutMs);
		thisTalon.configPeakOutputReverse(-1, elevTimeoutMs);

		/* set closed loop gains in slot0 - see documentation */
		thisTalon.selectProfileSlot(elevSlotIdx, elevPIDLoopIdx);
		thisTalon.config_kF(0, 0.287, elevTimeoutMs);
		thisTalon.config_kP(0, 0.4, elevTimeoutMs);
		thisTalon.config_kI(0, 0, elevTimeoutMs);
		thisTalon.config_kD(0, 0, elevTimeoutMs);
		/* set acceleration and vcruise velocity - see documentation */
		thisTalon.configMotionCruiseVelocity(elevCV, elevTimeoutMs);
		thisTalon.configMotionAcceleration(elevCV, elevTimeoutMs);
		/* zero the sensor */
		thisTalon.setSelectedSensorPosition(0, elevPIDLoopIdx, elevTimeoutMs);

		//thisTalon.configMotionCruiseVelocity((int) RobotData.elevCruiseVel, elevTimeoutMs);
		//thisTalon.configMotionAcceleration((int) RobotData.elevCruiseAccel, elevTimeoutMs);

	}
}
