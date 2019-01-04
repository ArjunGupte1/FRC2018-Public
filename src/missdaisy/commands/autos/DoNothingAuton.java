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

public class DoNothingAuton extends CommandGroup {

  public  DoNothingAuton() {
    //addParallel(new HoldArmPos());
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}