/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Add your docs here.
 */
public class Pistons {
    private static Solenoid hatch;
    private static Solenoid frontClimb;
    private static Solenoid backClimb;

    public static final int HATCH = 1;
    public static final int FRONTCLIMB = 2;
    public static final int BACKCLIMB = 3;
    public static void init(){
        hatch = new Solenoid(0);
        frontClimb = new Solenoid(1);
        backClimb = new Solenoid(2);
    }
    public static void extend(int solenoid){
        switch(solenoid){
            case HATCH:
                hatch.set(true);
                break;
            case FRONTCLIMB:
                frontClimb.set(true);
                break;
            case BACKCLIMB:
                backClimb.set(true);
                break;
            default:
                break;
        }
    }
    public static void retract(int solenoid){
        switch(solenoid){
            case HATCH:
                hatch.set(false);
                break;
            case FRONTCLIMB:
                frontClimb.set(false);
                break;
            case BACKCLIMB:
                backClimb.set(false);
                break;
            default:
                break;
        }
    }
}
