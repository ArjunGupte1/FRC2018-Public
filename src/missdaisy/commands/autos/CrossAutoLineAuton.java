package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.DrivePath;
import missdaisy.commands.GoToWristAngle;
import missdaisy.commands.HoldArmPos;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class CrossAutoLineAuton extends CommandGroup {

  public  CrossAutoLineAuton() {
    addParallel(new HoldArmPos());
    //addParallel(new DrivePath("Paths/CrossAutoLine", 1.0));
    addParallel(new DrivePath("Paths/TuneTurn", 1.0));
    
    System.out.println("Running The Auton!");
    
    addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}