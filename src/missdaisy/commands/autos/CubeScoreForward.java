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

public class CubeScoreForward extends CommandGroup {

  public  CubeScoreForward() {
    addParallel(new GoToWristAngle(Constants.WristPose.FORWARD_SCORING, true));
    addSequential(new GoToShoulderAngle(Constants.ArmPose.FORWARD_SCORING, true));
    //addSequential(new MaintainWristExtension()); // We might not really need this
    addSequential(new ScoreCube());
  }
}