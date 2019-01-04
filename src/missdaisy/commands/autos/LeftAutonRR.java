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

public class LeftAutonRR extends CommandGroup {

  public  LeftAutonRR() {
    
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
    
    //addSequential(new DrivePath("Paths/Scale/RightScaleFrontToRightSwitchBack", 1.0));
    //addSequential(new GrabCube(-1.0, 0.25));
    //addParallel(new DrivePath("Paths/Switch/RightSwitchBackToRightScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
    
    //addSequential(new DrivePath("Paths/CrossAutoLine", -1.0));
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}