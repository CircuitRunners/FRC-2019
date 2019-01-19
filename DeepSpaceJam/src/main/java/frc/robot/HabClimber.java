package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HabClimber{
    DoubleSolenoid frontLeft;
    DoubleSolenoid frontRight;
    DoubleSolenoid backLeft;
    DoubleSolenoid backRight;

    public void init(){
        frontLeft = new DoubleSolenoid(1, 2);
        frontRight = new DoubleSolenoid(3,4);
        backLeft = new DoubleSolenoid(5,6);
        backRight = new DoubleSolenoid(7,8);

    }
}