package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HabClimber{
    static DoubleSolenoid frontLeft;
    static DoubleSolenoid frontRight;
    static DoubleSolenoid backLeft;
    static DoubleSolenoid backRight;

    public static void init(){
        frontLeft = new DoubleSolenoid(1, 2);
        frontRight = new DoubleSolenoid(3,4);
        backLeft = new DoubleSolenoid(5,6);
        backRight = new DoubleSolenoid(7,8);

    }
    public static void raise(boolean endGame){
        if(endGame){
            frontLeft.set(DoubleSolenoid.Value.kForward);
            backLeft.set(DoubleSolenoid.Value.kForward);
            frontRight.set(DoubleSolenoid.Value.kForward);
            backRight.set(DoubleSolenoid.Value.kForward);
        } else {
            System.out.println("Not Endgame Yet!!!");
        }
    }
    public static void lower(){
        frontLeft.set(DoubleSolenoid.Value.kReverse);
        backLeft.set(DoubleSolenoid.Value.kReverse);
        frontRight.set(DoubleSolenoid.Value.kReverse);
        backRight.set(DoubleSolenoid.Value.kReverse); 
    }
}