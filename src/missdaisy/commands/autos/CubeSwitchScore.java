package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.GoToShoulderAngle;
import missdaisy.commands.GoToWristAngle;
import missdaisy.commands.ScoreCube;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class CubeSwitchScore extends CommandGroup {

  public  CubeSwitchScore() {
    addParallel(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
    addSequential(new GoToShoulderAngle(Constants.ArmPose.LOAD_CUBE, true));
    //addSequential(new MaintainWristExtension()); // We might not really need this
    addSequential(new ScoreCube());
  }
}