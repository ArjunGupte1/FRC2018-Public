package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.commands.DrivePath;
import missdaisy.commands.GrabCube;
import missdaisy.commands.PincerOpen;
import missdaisy.commands.ScoreCube;
import missdaisy.commands.TimeDelay;
import missdaisy.commands.groups.CubeLoadPosition;
import missdaisy.commands.groups.CubeStowPosition;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class LeftAutonLR extends CommandGroup {

  public  LeftAutonLR() {
    
    // Delay to let 2040 get out the way
    addParallel(new CubeStowPosition());
    addSequential(new TimeDelay(3000));
    
    addParallel(new DrivePath("Paths/Left/ToRightScaleFront", -1.0));
    addSequential(new CubeScoreBackward(5000));
    addSequential(new ScoreCube(0.5));
    addSequential(new CubeLoadPosition());
    
    addParallel(new PincerOpen());
    addSequential(new DrivePath("Paths/Scale/RightScaleFrontToRightSwitchBack", 1.0));
    addSequential(new GrabCube(-1.0, 0.25));
    addParallel(new DrivePath("Paths/Switch/RightSwitchBackToRightScaleFront", -1.0));
    addSequential(new CubeScoreBackward(100));
    
    /*
    addParallel(new DrivePath("Paths/Left/ToLeftSwitchSide", -1.0));
    addSequential(new CubeScoreBackward(1000));
    addSequential(new ScoreCube());
    //addSequential(new CubeLoadPosition());
    //addSequential(new DrivePath("Paths/Scale/LeftScaleFrontToLeftSwitchBack", 1.0));
    //addSequential(new GrabCube(-1.0, 0.25));
    //addParallel(new DrivePath("Paths/Switch/LeftSwitchBackToLeftScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
    */
    /*
    addSequential(new DrivePath("Paths/Left/ToLeftSwitchSide", -1.0));
    addSequential(new TurnAngle(-90.0));
    addParallel(new SetWristAngle(Constants.WristPose.SWITCH_SPIT));
    addParallel(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.SWITCH_SPIT));
    addSequential(new DrivePath("Paths/Left/ShortDash", 1.0));
    addSequential(new ScoreCube());
    */
    
    
    //addSequential(new DrivePath("Paths/CrossAutoLine", -1.0));
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}