/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;

/**
 * Add your docs here.
 */
public class Intake {
    private static VictorSP intake;
    public static void init(){
        intake = new VictorSP(6);
    }
    private static int power = 0;
    public static void run(){
        intake.set(power);
    }
    public static void in(){
        power = 1;
    }
    public static void out(){
        power = -1;
    } 
    public static void off(){
        power = 0;
    }


}
