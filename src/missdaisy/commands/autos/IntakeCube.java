package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.GoToShoulderAngle;
import missdaisy.commands.GoToWristAngle;
import missdaisy.commands.RunIntake;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class IntakeCube extends CommandGroup {

  public  IntakeCube() {
    addParallel(new GoToWristAngle(Constants.WristPose.LOAD_CUBE, true));
    addSequential(new GoToShoulderAngle(Constants.ArmPose.LOAD_CUBE, true));
    //addSequential(new MaintainWristExtension()); // We might not really need this
    addSequential(new RunIntake());
  }
}