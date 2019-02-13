package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class HabClimber{
        private static Solenoid front;
        private static Solenoid back;
        public static final int FRONT = 1;
        public static final int BACK = 2;
        public static final int BOTH = 3;
        public static void init(){
            front = new Solenoid(0);
            back = new Solenoid(1);
        }
        public static void raise(int pos){
            if(pos == FRONT){
            front.set(true);
            } else if(pos == BACK){
                back.set(true);
            } else if(pos == BOTH){
                front.set(true);
                back.set(true);
            }
        }
        public static void lower(int pos){
            if(pos == FRONT){
            front.set(false);
            } else if(pos == BACK){
                back.set(false);
            } else if(pos == BOTH){
                front.set(false);
                back.set(false); 
            }
        }
}