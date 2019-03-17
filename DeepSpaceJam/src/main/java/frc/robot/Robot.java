/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  static Joystick driver = new Joystick(0);
  static Joystick operator = new Joystick(1);
  Compressor c = new Compressor(0);
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    Camera.debug = true;
    Camera.init();
    Drivebase.init();
    Elevator.init();
    Wrist.init();
    Intake.init();
    Pistons.init();
    c.clearAllPCMStickyFaults();
    c.setClosedLoopControl(false);
    
  }
boolean debug = true;
  /**
   * This function is called periodically while the robot is powered on.
   */
  @Override
  public void robotPeriodic() {
    if(debug){
      Elevator.display();
      Wrist.display();
      Camera.display();
      }

  }

  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Elevator.run(-operator.getRawAxis(Logitech.AXIS_LEFTY));
    Intake.run();
    Wrist.run(-operator.getRawAxis(Logitech.AXIS_RIGHTY),-operator.getRawAxis(Logitech.AXIS_RTRIGGER));
    getControllers();
    }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Elevator.run(-operator.getRawAxis(Logitech.AXIS_LEFTY));
    Intake.run();
    Wrist.run(-operator.getRawAxis(Logitech.AXIS_RIGHTY),-operator.getRawAxis(Logitech.AXIS_RTRIGGER));
    getControllers();
  }
  boolean tr = false;
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    
  }

  static boolean wasIntaking = true;
  static boolean flopped = false;
  public static double speed = 0.6;

  private void getControllers(){
    //tracking controls
    if(driver.getRawButtonPressed(Logitech.BTN_A) || Drivebase.cameraControlled()){
      Camera.startTracking();
    } else if(driver.getRawButtonPressed(Logitech.BTN_B)){
      Camera.stopTracking();
    }
    //drive controls
   if(Camera.isTracking()){
      Drivebase.drive(-0.5 + (Camera.xAngle/160), -0.5 - (Camera.xAngle/160));
   } else {
     Drivebase.drive(driver.getRawAxis(Logitech.AXIS_LEFTY),driver.getRawAxis(Logitech.AXIS_RIGHTY));
   }
   //Right bumper slows robot to 30%.
   if(driver.getRawButton(Logitech.BTN_RIGHT_BUMPER)){
     speed = 0.3;
   } 
   //Left bumper press means speed is 100%.
   else if (driver.getRawButton(Logitech.BTN_LEFT_BUMPER)){
        speed = 1.0;
    }
    //No bumper press means speed is 60%.
    else {
     speed = 0.6;
    }
   //HabClimber Controls
   /*
   if(operator.getBackButton()){
    HabClimber.move(HabClimber.BOTH,HabClimber.RETRACT);
   } else if(operator.getStartButton()){
     HabClimber.correctedRaise();
   }
 */
   //Elevator/wrist controls (human control is in teleOp/auton loop)
   if(operator.getRawButton(Logitech.BTN_A)){
     Wrist.out();
     Elevator.goToLvl1();
     Wrist.out();
   } else if (operator.getRawButton(Logitech.BTN_B)){
     Wrist.out();
     Elevator.goToLvl2();
     Wrist.up();//!!!Not waiting on elevator command
   } else if(operator.getRawButton(Logitech.BTN_Y)){
     Wrist.out();
     Elevator.goToLvl3();
     Wrist.up();
   } else if(operator.getRawButton(Logitech.BTN_LEFT_STICK)){
     Elevator.setToCurrentPosition();
   }
   if(operator.getRawButtonReleased(Logitech.BTN_X)){
    if(flopped){
      flopped = !flopped;
      Wrist.up();
    } else {
      flopped = !flopped;
      Wrist.out();
    }
   }

   //intake controls
   
   if(operator.getRawButton(Logitech.BTN_RIGHT_BUMPER)){
     Intake.out();
     wasIntaking = false;
   } else if(operator.getRawButton(Logitech.BTN_LEFT_BUMPER)){
     Intake.in();
     wasIntaking = true;
   }  else if (!wasIntaking){
      Intake.off();
   } else {
     Intake.idle();
   }
  }
  public static void controlHab(){
    /*if(operator.getStartButton()){
      HabClimber.correctedRaise();
    }*/
  }
}
