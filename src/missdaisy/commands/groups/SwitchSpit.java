package missdaisy.commands.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.SetArmPose;
import missdaisy.commands.SetWristAngle;
import missdaisy.commands.StowWrist;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class SwitchSpit extends CommandGroup {

  public SwitchSpit() {
    
    
    //addParallel(new GoToWristAngle(Constants.WristPose.SWITCH_SPIT, true));
    //addSequential(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.SWITCH_SPIT));
    
    addParallel(new StowWrist());
    addSequential(new SetWristAngle(Constants.WristPose.SWITCH_SPIT));
    addSequential(new SetArmPose(Constants.ArmPose.SWITCH_SPIT, Constants.WristPose.SWITCH_SPIT));
    //addSequential(new GoToWristAngle(Constants.WristPose.SWITCH_SPIT, true));
  }
}