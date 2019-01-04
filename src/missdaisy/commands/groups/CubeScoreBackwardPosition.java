package missdaisy.commands.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.GoToWristAngle;
import missdaisy.commands.SetArmPose;
import missdaisy.commands.SetWristAngle;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class CubeScoreBackwardPosition extends CommandGroup {

  public  CubeScoreBackwardPosition() {
    //addParallel(new GoToWristAngle(Constants.WristPose.REAR_SCORING, true));
    //ddSequential(new GoToShoulderAngle(Constants.ArmPose.REAR_SCORING, true));
    //addSequential(new MaintainWristExtension()); // We might not really need this
    
    addSequential(new SetWristAngle(Constants.WristPose.REAR_SCORING));
    //addParallel(new StowWrist());
    //addSequential(new SetArmPose(Constants.ArmPose.REAR_SCORING, Constants.WristPose.HIGH_REAR_SCORING));
    addSequential(new SetArmPose(Constants.ArmPose.REAR_SCORING, 0.0));
    addSequential(new GoToWristAngle(Constants.WristPose.HIGH_REAR_SCORING, true));
  }
}