/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.awt.geom.Ellipse2D.Double;

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
    private static double power = 0;
    public static void run(){
        intake.set(power);
    }
    public static void in(){
        
        power = 0.6;
    }
    public static void in(int t){
        
        power = 1;
    }
    public static void out(){
        
        power = -0.6;
    } 
    public static void out(int t){
        
        power = -1;
    }
    public static void off(){
        power = 0;
    }
    public static void idle(){
        power = 0.3;
    }

}
