/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Add your docs here.
 */
public class Autonomous {
    public static final int MODE_CLIMB = 100;
    public static final int MODE_HATCH = 200;
    public static final int MODE_BALL = 300;
    private static int mode = 0;
    private double x = MyCamera.xAngle;
    public static void run(){
        switch(mode){
            case MODE_CLIMB:
            climb();
            break;
            case MODE_BALL:
            break;
            case MODE_HATCH:
            break;
            default:
            break;
        }  
    }
    public static void beginClimb(){
        mode = MODE_CLIMB;
    }
    private static int step = 0;
    private static void climb(){
        switch(step){
          case 1:
          HabClimber.raise(HabClimber.BOTH);
          step++;
          break;  
          case 2:
          Wrist.flop();
          step++;
          break;
          case 3:
          Intake.in(3);
          step++;
          case 4:

          break;
        }

    }   
}
