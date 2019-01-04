package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.commands.DrivePath;
import missdaisy.commands.GrabCube;
import missdaisy.commands.ScoreCube;
import missdaisy.commands.TimeDelay;
import missdaisy.commands.groups.CubeLoadPosition;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class CenterAuton extends CommandGroup {

  public  CenterAuton() {
    
    if(SmartDashboard.getBoolean("Wait Before Auton", false)) {
      addSequential(new TimeDelay(Constants.Autonomous.AUTO_TIME_DELAY));
    }
    
    //addParallel(new HoldArmPos());
    //addParallel(new DrivePath("Paths/CrossAutoLine", -1.0));
    
    /*
    addParallel(new DrivePath("Paths/Left/ToRightSwitchFront", -1.0));
    addSequential(new CubeSwitchScore());
    addSequential(new ScoreCube());
    //addSequential(new CubeLoadPosition());
    //addSequential(new DrivePath("Paths/Scale/RightScaleFrontToRightSwitchBack", 1.0));
    //addSequential(new GrabCube(-1.0, 0.25));
    //addParallel(new DrivePath("Paths/Switch/RightSwitchBackToRightScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
     */
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}