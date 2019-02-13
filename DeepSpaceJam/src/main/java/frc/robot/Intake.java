/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * Add your docs here.
 */
public class Intake {
    private static VictorSP intake;
    private static double time = Timer.getFPGATimestamp();
    private static double endTime = 0;
    public static void init(){
        intake = new VictorSP(6);
    }
    private static int power = 0;
    public static void run(){
        time = Timer.getFPGATimestamp();
        if(endTime > time){
            intake.set(power);
        } else {
            intake.stopMotor();
        }
    }
    public static void in(){
        endTime = time++;
        power = 1;
    }
    public static void in(int t){
        endTime = time + t;
        power = 1;
    }
    public static void out(){
        endTime = time++;
        power = -1;
    } 
    public static void out(int t){
        endTime = time + t;
        power = -1;
    }
    public static void off(){
        power = 0;
    }


}
