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

//import edu.wpi.first.wpilibj.Talon;
/**
 * Add your docs here.
 * 
 */
public class Elevator {
    private static TalonSRX elevator;
    private static int currentPosition = 0;
    private static int desiredPosition = 0;

    public static void init() {
        elevator = new TalonSRX(9);
        elevator.clearStickyFaults();
        talonConfig(elevator, false);
    }

    public static void run(double leftTriggerInfo, double rightTriggerInfo) {
        if (Math.abs(leftTriggerInfo) > 0.1) {
            desiredPosition += 10 * leftTriggerInfo;
        } else if (Math.abs(rightTriggerInfo) > 0.1) {
            desiredPosition -= 10 * rightTriggerInfo;
        } else if (Math.abs(rightTriggerInfo) > 0.1 && Math.abs(leftTriggerInfo) > 0.1) {
        }

        move(desiredPosition);

        currentPosition = elevator.getSelectedSensorPosition(elevPIDLoopIdx);
    }

    static int elevTimeoutMs = 10;
    static int elevPIDLoopIdx = 0;
    static int elevSlotIdx = 0;
    static int elevCV = 15000;
    static int maxElevCV = 18000;
    static int elevCA = 15000;
    static int maxElevCA = 18000;

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

    private static int maxHeight = 80; // inches

    private static void move(double position) {
        if (position != currentPosition) {
            if (position > toClicks(maxHeight)) {
                elevator.set(ControlMode.MotionMagic, toClicks(maxHeight));
            } else {
                elevator.set(ControlMode.MotionMagic, desiredPosition);
            }

        }
    }

    private static double toInches(int i) {
        return (i / 990.0);
    }

    private static double toClicks(int j) {
        return (j * 990.0);
    }

    public static void goToLvl1() {
        desiredPosition = 120;
    }

    public static void goToLvl2() {
        desiredPosition = 240;
    }

    public static void goToLvl3() {
        desiredPosition = 360;
    }
}
