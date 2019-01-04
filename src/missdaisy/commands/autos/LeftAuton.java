package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.commands.DrivePath;
import missdaisy.commands.GrabCube;
import missdaisy.commands.ScoreCube;
import missdaisy.commands.groups.CubeLoadPosition;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class LeftAuton extends CommandGroup {

  public  LeftAuton() {
    //addParallel(new HoldArmPos());
    //addParallel(new DrivePath("Paths/CrossAutoLine", -1.0));
    
    /*
    addParallel(new DrivePath("Paths/Left/ToLeftScaleFront", -1.0));
    addSequential(new CubeScoreBackward(1000));
    addSequential(new ScoreCube());
    addSequential(new CubeLoadPosition());
    addSequential(new DrivePath("Paths/Scale/LeftScaleFrontToLeftSwitchBack", 1.0));
    addSequential(new GrabCube(-1.0, 0.25));
    addParallel(new DrivePath("Paths/Switch/LeftSwitchBackToLeftScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
     */
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}