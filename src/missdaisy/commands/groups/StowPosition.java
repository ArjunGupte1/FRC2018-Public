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

public class StowPosition extends CommandGroup {

  public  StowPosition() {
    //addParallel(new GoToWristAngle(Constants.WristPose.LOAD_CUBE, true));
    //addSequential(new GoToShoulderAngle(Constants.ArmPose.LOAD_CUBE, true));
    //addSequential(new MaintainWristExtension()); // We might not really need this
    
    addSequential(new SetWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD));
    //addParallel(new StowWrist());
    addSequential(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.STOW_ANGLE_AT_LOAD));
    //addSequential(new GoToWristAngle(Constants.WristPose.LOAD_CUBE, true));
  }
}