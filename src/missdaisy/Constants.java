package missdaisy;

public class Constants {
  
  public static final boolean DEBUG_MODE = true;
  
  public static class XboxController {
    
    public static final int DRIVER_PORT = 0;
    public static final int OPERATOR_PORT = 1;
    public static final double DEAD_BAND = 0.1; //0.2
    
    public static final int B_BUTTON = 2;
    public static final int LEFT_TRIGGER = 2;
    public static final int RIGHT_TRIGGER = 3;
    
  };
  
  public static class Solenoids{
    public static final int LEFT_PTO_A = 0;
    public static final int LEFT_PTO_B = 1;
    public static final int CLIMBER_RELEASE = 2;
    public static final int INTAKE = 3;
    public static final int RIGHT_POPPER_A = 4;
    public static final int RIGHT_POPPER_B = 5;
    public static final int STATUS_LIGHT = 7;
    
  }
  
  public static class Drive{
    public static final int LEFT_ENCODER_A_PORT = 0;
    public static final int LEFT_ENCODER_B_PORT = 1;
    public static final int RIGHT_ENCODER_A_PORT = 2;
    public static final int RIGHT_ENCODER_B_PORT = 3;
    public static final double DISTANCE_PER_ENCODER_PULSE = (double) (4.0 * Math.PI / 256.0);
    public static final double DRIVE_MAX_VELOCITY = 11.0;
    public static final double PID_DRIVE_DISTANCE_TOLERANCE = .2;
    
    public static final int LEFT_MOTOR_A_PORT = 0;
    public static final int LEFT_MOTOR_B_PORT = 1;
    public static final int LEFT_MOTOR_C_PORT = 2;
    public static final int RIGHT_MOTOR_A_PORT = 3;
    public static final int RIGHT_MOTOR_B_PORT = 4;
    public static final int RIGHT_MOTOR_C_PORT = 6;
  };
  
  public static class CAN {
    public static final int SHOULDER_TALONSRX_ID = 1;
    public static final int SHOULDER_SLAVE_VICTORSPX_ID = 2;
    public static final int WRIST_TALONSRX_ID = 3;
    
    public static final int INTAKE_VICTORSPX_PORT_LEFT = 4;
    public static final int INTAKE_VICTORSPX_PORT_RIGHT = 5;
  };
  
  public static class Autonomous{
    
     static final String[] AUTONS = {
        "/home/lvuser/trajectories/CrossAutoLine.txt",
        "/home/lvuser/trajectories/CenterRightSwitch.txt",
        "/home/lvuser/trajectories/DemoLeft.txt",
        "/home/lvuser/trajectories/DoNothing.txt"
        };
     
     static final String[] STARTINGPOSITIONS = {
         "Left", "Center", "Right", "CrossAutoLine", "DoNothing"
         };

    public static final long AUTO_TIME_DELAY = 10;
  }
  
  public static class Arm {
    // Inputs
    public static final int ARM_LOWER_LMIT_SWITCH_LEFT = 6;
    public static final int ARM_LOWER_LMIT_SWITCH_RIGHT = 7;
    public static final int WRIST_LIMIT_SWITCH = 8;
    public static final int ARM_UPPER_LIMIT_SWITCH = 9;
    
    // Physical Parameters
    public static final double MAST_LENGTH = 42.0;
    public static final double FOREARM_LENGTH = 44.0;
    public static final double WRIST_LENGTH = 17.0;
    public static final double MAX_FORWARD_EXTENSION = 40.0;
    
    //???
    public static final double MIN_ARM_HEIGHT = -(10/360);
    public static final double MAX_ARM_HEIGHT = (135/360);
    public static final double ARM_GEAR_RATIO = (15 / 28);
    public static final double ARM_TOLERANCE = 6.0; //Degrees
    public static final double WRIST_TOLERANCE = 3.0; //Degrees
    
    
    
    // Shoulder Encoder
    public static final double SHOULDER_DEGREES_PER_TICK = (double)((360.0)/4096.0);
    public static final double SHOULDER_ABSOLUTE_DEGREES_PER_TICK = (double)((360.0)/1024.0);
    public static final double SHOULDER_RADIANS_PER_TICK = (double)((2.0*Math.PI)/4096.0);
    public static final double SHOULDER_HORIZONTAL_ABSOLUTE_POS = 1415;
    public static final double SHOULDER_MAX_TICKS = 0.0;//?
    
    // Wrist Encoder
    public static final double WRIST_DEGREES_PER_TICK = (double)((360.0 * 1.0/2.0)/4096);
    public static final double WRIST_ABSOLUTE_DEGREES_PER_TICK = (double)((360.0 * 1.0/2.0)/1024.0);
    public static final double WRIST_RADIANS_PER_TICK = (double)((2.0*Math.PI * 1.0/2.0)/4096);
    public static final double WRIST_HORIZONTAL_ABSOLUTE_POS = -2300; //4073;
    public static final double WRIST_MAX_TICKS = 0.0;//?
    public static final double WRIST_MIN_TICKS = 0.0;//?

    public static final int WRIST_FORWARD_SOFT_LIMIT = 3300;

    public static final int WRIST_REVERSE_SOFT_LIMIT = -2300;

    public static final int ARM_FORWARD_SOFT_LIMIT = 1100;

    public static final int ARM_REVERSE_SOFT_LIMIT = -800;

    public static final int ARM_LOWEST_POSITION = -585;

    public static final int ARM_VERTICAL_POSITION = 990;
  }
  
  public static class ArmPose {
    public static final double LOAD_CUBE = -47.0;
    public static final double FORWARD_SCORING = 75.0;
    public static final double REAR_SCORING = 87.0;
    public static final double SWITCH_SPIT = -20.0;
  }
  
  public static class WristPose {
    public static final double STOW_ANGLE_IN_WORLD_FRAME = 110.0;
    public static final double STOW_ANGLE_AT_LOAD = 140.0;
    public static final double LOAD_CUBE = 62.0;
    public static final double LOAD_CUBE_AUTO = 57.0;
    public static final double FORWARD_SCORING = -50.0;
    public static final double REAR_SCORING = 85.0;
    public static final double HIGH_REAR_SCORING = 38.0;
    public static final double SWITCH_SPIT = 120.0;
  }
  
  public static class Intake {
    public static final int INTAKE_LIMIT_SWITCH = 5;
  }
  
  public static class Timing {
    public static final double ENDGAME_TIME_START = 105.0; // 1:45 min
  }
  
  public static class Properties {
    public static final long FAST_LOOP_TIMER_PERIOD = 10L;
    public static final double PID_DRIVE_ANGLE_TOLERANCE = 2.0;
    public static final double PID_DRIVE_TURN_MIN_OUTPUT = 0.3;
    public static final double PID_DRIVE_TURN_MAX_OUTPUT = 0.9;
    public static final double DRIVE_WIDTH = 27.5;//23.7; //47.7;
    public static final int K_TIMEOUT_MS = 0;
    public static final double UPPER_TIP_THRESHOLD = 15.0;
    public static final double LOWER_TIP_THRESHOLD = 10.0;
    
  }

  public static class UnitConv {
    public static final double M_TO_IN = 39.3701;
    public static final double IN_TO_M = 1.0 / M_TO_IN;
    public static final double MPS_TO_FPS = 3.28084;
    public static final double MPS_TO_INPS = MPS_TO_FPS * 12;
    public static final double RAD_TO_DEG = 180.0 / Math.PI;
    public static final double DEG_TO_RAD = Math.PI / 180.0;
    public static final double FPS_TO_MPS = 1.0 / MPS_TO_FPS;
    
  }

}
