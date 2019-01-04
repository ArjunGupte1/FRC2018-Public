package missdaisy.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Constants;
import missdaisy.Robot;

/**
 *
 */
public class PincerClose extends Command {
  
    public PincerClose() {
        // Use requires() here to declare subsystem dependencies
      super("PincerClose");
      requires(Robot.mIntake);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      Robot.mIntake.setSpeed(0.0);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      Robot.mIntake.closePincer();
      Robot.mIntake.setSpeed(0.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      Robot.mIntake.openPincer();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      end();
    }
}
