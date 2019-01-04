package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Robot;
import missdaisy.loops.Navigation;
import missdaisy.subsystems.Drive;

/**
 *
 */
public class DriveTime extends Command {
    private final Drive mDrive;
    private final Navigation mNav;
    private final double kP = 0.03;
    private double mSpeed;
    private double mStartYaw;
    private long mDelay, mStartTime;
    
	public DriveTime(double speed, int timeout) {
	    super("DriveTime");
	    mSpeed = speed;
	    mDrive = Drive.getInstance();
	    mNav = Navigation.getInstance();
	    mDelay = timeout;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	    mStartYaw = mNav.getHeadingInDegrees();
	    mDrive.useAlphaFilter(false);
	    mStartTime = System.currentTimeMillis();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
	    //double turn = getYawError() * kP;
	    mDrive.setSpeedTurn(mSpeed, 0.0);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
	    return System.currentTimeMillis() > mDelay + mStartTime;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	  mDrive.setSpeedTurn(0.0, 0.0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
