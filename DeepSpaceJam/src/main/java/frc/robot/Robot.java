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
import edu.wpi.first.wpilibj.Timer;
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
  static XboxController driver = new XboxController(0);
  static XboxController operator = new XboxController(1);
  //Compressor c = new Compressor(0);
  //Joystick jumpBtn = new Joystick(1);
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    MyCamera.debug = true;
    MyCamera.init();
    Drivebase.init();
    //Elevator.init();
    //Wrist.init();
    Intake.init();
    HabClimber.init();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("correction angle", MyCamera.xAngle);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    getControllers();
    //Elevator.run(operator.getTriggerAxis(Hand.kLeft), operator.getTriggerAxis(Hand.kRight));
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    getControllers();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
  static boolean flopped = false;
  private void getControllers(){
    //tracking controls
    if(driver.getAButton() || !Drivebase.operatorControlled()){
      MyCamera.startTracking();
    } else {
      MyCamera.stopTracking();
    }
    //drive controls
   if(MyCamera.isTracking() && !Drivebase.operatorControlled()){
      Drivebase.drive(.25+(MyCamera.xAngle/30), .25-(MyCamera.xAngle/30));
   } else {
     Drivebase.drive(driver.getRawAxis(Xbox.AXIS_LEFTY),driver.getRawAxis(Xbox.AXIS_RIGHTY));
   }
   //HabClimber Controls
   if(operator.getBumper(Hand.kLeft) && operator.getBumper(Hand.kRight) && driver.getBumper(Hand.kLeft) && driver.getBumper(Hand.kRight)){
     Autonomous.beginClimb();
   }
   //wrist controls
   if(operator.getXButton() && !flopped){
     Wrist.flop();
   } else if(operator.getXButton() && flopped){
     Wrist.unflop();
   }
   //Elevator controls
   if(operator.getAButton()){
     Elevator.goToLvl1();
   }
  }
}
