package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Robot;

/**
 *
 */
public class TimeDelay extends Command {
    private long mDelay, mStartTime;
	public TimeDelay(long delay) {
		// Use requires() here to declare subsystem dependencies
		//requires(Robot.exampleSubsystem);
	  mDelay = delay;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	    mStartTime = System.currentTimeMillis();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return System.currentTimeMillis() > mDelay + mStartTime;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
