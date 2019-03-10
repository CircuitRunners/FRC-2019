package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class HabClimber{
        private static Solenoid frontLeft;
        private static Solenoid backLeft;
        private static Solenoid frontRight;
        private static Solenoid backRight;
        public static final int FRONT = 1;
        public static final int BACK = 2;
        public static final int LEFT = 3;
        public static final int RIGHT = 4;
        public static final int EXTEND = 5;
        public static final int RETRACT = 6;
        public static final int BOTH = 9;
        private static boolean isRaising = false;
        public static void init(){
            frontLeft = new Solenoid(2);
            backLeft = new Solenoid(3);
            frontRight = new Solenoid(0);
            backRight = new Solenoid(1);
        }
        public static void move(int pos, int state){
            if(state == EXTEND){
                switch(pos){
                    case FRONT:
                        frontLeft.set(true);
                        frontRight.set(true);
                        break;
                    case BACK:
                        backLeft.set(true);
                        backRight.set(true);
                        break;
                    case LEFT:
                        frontLeft.set(true);
                        backLeft.set(true);
                        break;
                    case RIGHT:
                        frontRight.set(true);
                        backRight.set(true);
                        break;
                }
            } else if(state == RETRACT){
                switch(pos){
                    case FRONT:
                        frontLeft.set(false);
                        frontRight.set(false);
                        break;
                    case BACK:
                        backLeft.set(false);
                        backRight.set(false);
                        break;
                    case LEFT:
                        frontLeft.set(false);
                        backLeft.set(false);
                        break;
                    case RIGHT:
                        frontRight.set(false);
                        backRight.set(false);
                        break;
                }
            }
        }
        
        public static void correctedRaise(){
            isRaising = true;
        }
        
        public static void run(){
            if(isRaising){
                move(BOTH,EXTEND);
                if(Accelerometer.getPitch() > 1){
                    move(BACK,RETRACT);
                } else if(Accelerometer.getPitch() < -1){
                    move(FRONT,RETRACT);
                }
                if(Accelerometer.getRoll() > 1){
                    move(LEFT,RETRACT);
                } else if(Accelerometer.getRoll() < -1){
                    move(RIGHT,RETRACT);
                }
            }
        }
        public static boolean isRaising(){
            return isRaising;
        }
}