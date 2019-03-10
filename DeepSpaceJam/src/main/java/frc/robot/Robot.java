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
  static XboxController operator = new XboxController(1);
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
    HabClimber.init();
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
      SmartDashboard.putNumber("correction angle", Camera.xAngle);
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
    Elevator.run(-operator.getY(Hand.kLeft));
    Intake.run();
    Wrist.run(-operator.getY(Hand.kRight));
    getControllers();
    }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Elevator.run(-operator.getY(Hand.kLeft));
    Intake.run();
    Wrist.run(-operator.getY(Hand.kRight));
    getControllers();
    //Accelerometer.run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    Accelerometer.run();
    controlHab();

  }
  static boolean wasIntaking = true;
  static boolean flopped = false;
  public static double speed = 1.0;
  private void getControllers(){
    //tracking controls
    if(driver.getRawButtonReleased(Logitech.BTN_A) || Drivebase.cameraControlled()){
      Camera.startTracking();
    } else if(driver.getRawButtonReleased(Logitech.BTN_A) && Camera.isTracking()){
      Camera.stopTracking();
    }
    //drive controls
   if(Camera.isTracking() && Drivebase.cameraControlled()){
      Drivebase.drive(.25+(Camera.xAngle/30), .25-(Camera.xAngle/30));
   } else {
     Drivebase.drive(driver.getRawAxis(Xbox.LOGITECH_LEFTY),driver.getRawAxis(Xbox.LOGITECH_RIGHTY));
   }
   if(driver.getRawButton(Logitech.BTN_RIGHT_TRIGGER)){
     speed = 0.6;
   } else {
     speed = 1.0;
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
   if(operator.getAButton()){
     Wrist.out();
     Elevator.goToLvl1();
     Wrist.out();
   } else if (operator.getBButton()){
     Wrist.out();
     Elevator.goToLvl2();
     Wrist.up();
   } else if(operator.getYButton()){
     Wrist.out();
     Elevator.goToLvl3();
     Wrist.up();
   }
   if(operator.getXButtonReleased()){
    if(flopped){
      flopped = !flopped;
      Wrist.up();
    } else {
      flopped = !flopped;
      Wrist.out();
    }
   }

   //intake controls
   
   if(operator.getBumper(Hand.kRight)){
     Intake.out();
     wasIntaking = false;
   } else if(operator.getBumper(Hand.kLeft)){
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
