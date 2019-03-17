/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class Wrist {
    private static TalonSRX wrist;
	private static TalonSRX wristSlave;
	private static int position = 0;
    public static void init(){
        wrist = new TalonSRX(12);
        wristSlave = new TalonSRX(13);
        wristSlave.follow(wrist);
        talonConfig(wrist,false);
        talonConfig(wristSlave,false);
		wrist.setSelectedSensorPosition(0);
		//wrist.clearStickyFaults();
	}
	private static int desiredPosition = 0;
	private static int unary = 0;
	public static void run(double rightJoyInput, double rightTriggerInput){
		if(Math.abs(rightJoyInput) > 0.1){
			desiredPosition += rightJoyInput * 30;
		} else if(Math.abs(rightTriggerInput) > 0.01){
			unary = (int) (rightTriggerInput /Math.abs(rightTriggerInput));
			rightTriggerInput /= 2;
			rightTriggerInput = rightTriggerInput * rightTriggerInput * unary;
			desiredPosition = (int) (rightTriggerInput * 12000) + 40;
		}
		if(desiredPosition > 40){desiredPosition = 30;}
		if(desiredPosition < -9900){desiredPosition = -9900;}
		if(Elevator.wristOut){
			desiredPosition = (desiredPosition >= -600)? -600:desiredPosition;
		}
		move(desiredPosition);
	}
	private static void move(int filteredPosition){
		if(position != desiredPosition){
			wrist.set(ControlMode.MotionMagic, filteredPosition);
		}
	}
    
	public static void up(){
		desiredPosition = -20;
	}
	public static void out(){
		desiredPosition = -2800;
	}
    public static void talonConfig(TalonSRX thisTalon, boolean inverted) {
		/* first choose the sensor */
		thisTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Elevator.elevPIDLoopIdx,
				Elevator.elevTimeoutMs);
		thisTalon.setSensorPhase(inverted);
		thisTalon.setInverted(inverted);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Elevator.elevTimeoutMs);
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Elevator.elevTimeoutMs);
		thisTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, Elevator.elevCA);
		/* set the peak and nominal outputs */
		thisTalon.configNominalOutputForward(0, Elevator.elevTimeoutMs);
		thisTalon.configNominalOutputReverse(0, Elevator.elevTimeoutMs);
		thisTalon.configPeakOutputForward(1, Elevator.elevTimeoutMs);
		thisTalon.configPeakOutputReverse(-1, Elevator.elevTimeoutMs);

		/* set closed loop gains in slot0 - see documentation */
		thisTalon.selectProfileSlot(Elevator.elevSlotIdx, Elevator.elevPIDLoopIdx);
		thisTalon.config_kF(0, 0.287, Elevator.elevTimeoutMs);
		thisTalon.config_kP(0, 0.4, Elevator.elevTimeoutMs);
		thisTalon.config_kI(0, 0, Elevator.elevTimeoutMs);
		thisTalon.config_kD(0, 0, Elevator.elevTimeoutMs);
		/* set acceleration and vcruise velocity - see documentation */
		thisTalon.configMotionCruiseVelocity(Elevator.elevCV, Elevator.elevTimeoutMs);
		thisTalon.configMotionAcceleration(Elevator.elevCA-3000, Elevator.elevTimeoutMs);
		/* zero the sensor */
		thisTalon.setSelectedSensorPosition(0, Elevator.elevPIDLoopIdx, Elevator.elevTimeoutMs);
        thisTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,LimitSwitchNormal.NormallyOpen);
        thisTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,LimitSwitchNormal.NormallyOpen);
		//thisTalon.configMotionCruiseVelocity((int) RobotData.elevCruiseVel, elevTimeoutMs);
		//thisTalon.configMotionAcceleration((int) RobotData.elevCruiseAccel, elevTimeoutMs);

	}
	
	public static void display(){
		SmartDashboard.putNumber("Wrist Clicks",wrist.getSelectedSensorPosition());
		SmartDashboard.putNumber("Wrist Motor Controller output %",wrist.getMotorOutputPercent());
	}
}
