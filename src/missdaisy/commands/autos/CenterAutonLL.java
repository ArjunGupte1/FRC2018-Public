package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.commands.DrivePath;
import missdaisy.commands.DriveTime;
import missdaisy.commands.GoToWristAngle;
import missdaisy.commands.GrabCube;
import missdaisy.commands.HoldArmPos;
import missdaisy.commands.NullArm;
import missdaisy.commands.ScoreCube;
import missdaisy.commands.SetArmPose;
import missdaisy.commands.SetWristAngle;
import missdaisy.commands.TurnAngle;
import missdaisy.commands.groups.CubeLoadPosition;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class CenterAutonLL extends CommandGroup {

  public  CenterAutonLL() {
    
    if(SmartDashboard.getBoolean("Wait Before Auton", false)) {
      //addSequential(new TimeDelay(Constants.Autonomous.AUTO_TIME_DELAY));
    }
    
    //addParallel(new HoldArmPos());
    //addParallel(new DrivePath("Paths/ToLeftSwitchFront", -1.0));
    
    addParallel(new NullArm());
    //addSequential(new DrivePath("Paths/Center/ToLeftSwitchFront", 1.0));
    //addSequential(new SetWristAngle(Constants.WristPose.SWITCH_SPIT));
    addParallel(new DrivePath("Paths/Center/ToLeftSwitchFront", 1.0));
    //addSequential(new DrivePath("Paths/Center/ToLeftSwitchFront", 1.0));
    addSequential(new CubeSwitchScore());
    addSequential(new SetArmPose(Constants.ArmPose.SWITCH_SPIT, Constants.WristPose.SWITCH_SPIT));
    addSequential(new ScoreCube());
   
    // Go back and grab a second cube
    addSequential(new DrivePath("Paths/Switch/LeftSwitchFrontToCenter", -1.0));
    addSequential(new CubeLoadPosition());
    //addSequential(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.LOAD_CUBE_AUTO));
    addSequential(new GrabCube(-1.0, 0.35));

    /*
    // Go score it on the switch
    addSequential(new DriveTime(-0.5, 1500));
    addParallel(new DrivePath("Paths/Center/ToLeftSwitchFront", 1.0));
    addSequential(new SetArmPose(Constants.ArmPose.SWITCH_SPIT, Constants.WristPose.SWITCH_SPIT));
    addSequential(new ScoreCube());
    */
    
    // Go score the cube on the scale??
    addSequential(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.STOW_ANGLE_AT_LOAD));
    addSequential(new TurnAngle(90.0));
    addParallel(new DrivePath("Paths/Center/ToLeftScaleFront", -1.0));
    
    
    addSequential(new CubeScoreBackward(2500));
    addSequential(new ScoreCube(0.3));
    addParallel(new CubeLoadPosition());
    
    //addSequential(new CubeLoadPosition());
    //addSequential(new DrivePath("Paths/Scale/RightScaleFrontToRightSwitchBack", 1.0));
    //addSequential(new GrabCube(-1.0, 0.25));
    //addParallel(new DrivePath("Paths/Switch/RightSwitchBackToRightScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
     
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}