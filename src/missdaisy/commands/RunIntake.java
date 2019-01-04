package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Constants;
import missdaisy.Robot;

/**
 *
 */
public class RunIntake extends Command {
  
    private int count = 0;
    public RunIntake() {
        // Use requires() here to declare subsystem dependencies
      super("RunIntake");
      count = 0;
      requires(Robot.mIntake);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      if(Robot.mIntake.hasCube()) {
        count = 20;
      } else {
        Robot.mIntake.openPincer();
        count = 0;
      }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      Robot.mIntake.setSpeed(1.0);
      if(Robot.mIntake.hasCube()) {
        count++;
      }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return count >= 5;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      Robot.mIntake.setSpeed(0.0);
      if(count <= 10) {
        Robot.mIntake.closePincer();
      }
      if(Robot.mIntake.hasCube()) {
        Robot.mWrist.setAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD);
      }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      end();
    }
}
