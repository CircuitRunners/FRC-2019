/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

/**
 * Add your docs here.
 */
public class Accelerometer {
    private static BuiltInAccelerometer bia;
    private static double pitch = 0;
    private static double roll = 0;

    private static double yAccel = 0.0;
    private static double xAccel = 0.0;
    private static double zAccel = 0.0;

    public static void init(){
        BuiltInAccelerometer bia = new BuiltInAccelerometer();
    }
    public static void run(){

        if(HabClimber.isRaising()){
            yAccel = bia.getY();
            xAccel = bia.getX();
            zAccel = bia.getZ();
        roll = Math.atan(yAccel/
            (Math.sqrt((xAccel * xAccel) + (zAccel * zAccel))));
        pitch = Math.atan(xAccel/
		    (Math.sqrt((yAccel * yAccel) + (zAccel * zAccel))));
        }
    }
    public static double getRoll(){
        return roll;
    }
    public static double getPitch(){
        return pitch;
    }

}
