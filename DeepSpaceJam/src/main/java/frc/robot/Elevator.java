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

//import edu.wpi.first.wpilibj.Talon;
/**
 * Add your docs here.
 * 
 */
public class Elevator {
    private static TalonSRX elevator;
    private static int currentPosition = 5893;
    private static int desiredPosition = 5893;
    public static void init() {
        elevator = new TalonSRX(11);
        elevator.clearStickyFaults();
        talonConfig(elevator, true);
        elevator.setInverted(true);
        elevator.setSelectedSensorPosition(5893);
    }

    public static void run(double leftJoyInput) {
        
        if(Math.abs(leftJoyInput) > 0.1) {
            desiredPosition += (leftJoyInput * 300);
        }
        if(desiredPosition > maxHeight){
            desiredPosition = maxHeight;
        }
        move((desiredPosition < 0)? 0:desiredPosition);
        if(desiredPosition < 0){
            desiredPosition = 0;
        }
        currentPosition = elevator.getSelectedSensorPosition(elevPIDLoopIdx);
    }

    static int elevTimeoutMs = 10;
    static int elevPIDLoopIdx = 0;
    static int elevSlotIdx = 0;
    static int elevCV = 90000; //was 36000
    //static int maxElevCV = 18000;
    static int elevCA = 90000; //was 45000
    //static int maxElevCA = 18000;

    public static void talonConfig(TalonSRX thisTalon, boolean inverted) {
        /* first choose the sensor */
        thisTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, elevPIDLoopIdx, elevTimeoutMs);
        thisTalon.setSensorPhase(!inverted);
        thisTalon.setInverted(inverted);

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
        thisTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        thisTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        // thisTalon.configMotionCruiseVelocity((int) RobotData.elevCruiseVel,
        // elevTimeoutMs);
        // thisTalon.configMotionAcceleration((int) RobotData.elevCruiseAccel,
        // elevTimeoutMs);

    }

    private static int maxHeight = 92500;
public static boolean wristOut = false;
    private static void move(double position) {
        if (position != currentPosition) {
           elevator.set(ControlMode.MotionMagic, position);
        }
        if(position > 30000 && position < 53000){
            wristOut = true;
        } else {
            wristOut = false;
        }
    }


     public static void goToLvl1() {
        desiredPosition = 8200;
    }
    public static void goToLvl2() {
        desiredPosition = 52400;
    }
    public static void goToLvl3() {
        desiredPosition = maxHeight;
    }
    public static void display(){
       SmartDashboard.putNumber("Elevator Clicks",elevator.getSelectedSensorPosition());
    }
}
