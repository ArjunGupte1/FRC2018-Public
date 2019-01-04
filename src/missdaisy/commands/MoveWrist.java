package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Constants;
import missdaisy.OI;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Wrist;

/**
 *
 */
public class MoveWrist extends Command {
  
    private Wrist mWrist;
    
    public MoveWrist() {
      super("MoveWrist");
        // Use requires() here to declare subsystem dependencies
      mWrist = Wrist.getInstance();
      
      requires(Robot.mWrist);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {      
      if(Robot.mArm.getShoulderAngleInDegrees() > Constants.Arm.ARM_TOLERANCE
         || Math.abs(mWrist.getWristAngleInDegrees() - Constants.WristPose.LOAD_CUBE) > Constants.Arm.WRIST_TOLERANCE 
         || (OI.getInstance().mOperatorRightStickY > 0.0 && !mWrist.getLimitSwitch())){
        mWrist.setSpeed(0.75 * OI.getInstance().mOperatorRightStickY);
      } else {
        mWrist.setSpeed(0.0);
      }
      mWrist.setDesiredWristAngle(mWrist.getWristAngleInDegrees());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;//mWrist.getForwardExtension() > Constants.Arm.MAX_FORWARD_EXTENSION - 0.5;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      mWrist.setSpeed(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      end();
    }
}
