package missdaisy.commands.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.SetArmPose;
import missdaisy.commands.SetWristAngle;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class CubeScoreForwardPosition extends CommandGroup {

  public  CubeScoreForwardPosition() {
    //addSequential(new GoToShoulderAngle(Constants.ArmPose.FORWARD_SCORING, true));
    //addParallel(new GoToWristAngle(Constants.WristPose.FORWARD_SCORING, true));
    
    addSequential(new SetWristAngle(Constants.WristPose.FORWARD_SCORING));
    //addParallel(new StowWrist());
    addSequential(new SetArmPose(Constants.ArmPose.FORWARD_SCORING, Constants.WristPose.FORWARD_SCORING));
    //addSequential(new GoToWristAngle(Constants.WristPose.FORWARD_SCORING, true));
  }
}