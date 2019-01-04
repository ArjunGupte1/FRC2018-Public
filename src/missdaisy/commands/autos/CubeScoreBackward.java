package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.GoToWristAngle;
import missdaisy.commands.NullArm;
import missdaisy.commands.ScoreCube;
import missdaisy.commands.SetArmPose;
import missdaisy.commands.SetWristAngle;
import missdaisy.commands.TimeDelay;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class CubeScoreBackward extends CommandGroup {

  public  CubeScoreBackward(long delay) {
    addParallel(new NullArm());
    addSequential(new TimeDelay(delay));
    
    
    addSequential(new SetWristAngle(Constants.WristPose.REAR_SCORING));
    //addSequential(new SetArmPose(Constants.ArmPose.REAR_SCORING, Constants.WristPose.REAR_SCORING));
    //addSequential(new ScoreCube(0.25));
    addSequential(new SetArmPose(Constants.ArmPose.REAR_SCORING, 0.0));
    addSequential(new GoToWristAngle(Constants.WristPose.REAR_SCORING, true));
  }
}