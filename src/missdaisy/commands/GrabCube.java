package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Robot;
import missdaisy.subsystems.Drive;
import missdaisy.subsystems.Intake;

/**
 *
 */
public class GrabCube extends Command {
    private Intake mIntake = Intake.getInstance();
    private Drive mDrive = Drive.getInstance();
    private double mIntakeSpeed, mDriveSpeed;
    private int mCount;
    
	public GrabCube(double intakeSpeed, double driveSpeed) {
		// Use requires() here to declare subsystem dependencies
	    super("RunIntake", 1000);
	    mIntake = Intake.getInstance();
	    mDrive = Drive.getInstance();
	    mIntakeSpeed = intakeSpeed;
	    mDriveSpeed = driveSpeed;
	    mCount = 0;
		requires(Robot.exampleSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	  mIntake.setSpeed(mIntakeSpeed);
	  mIntake.openPincer();
	  mDrive.setSpeedTurn(mDriveSpeed, 0.0);
	  mCount = 0;
	  mDrive.resetGyro();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
	  mIntake.setSpeed(mIntakeSpeed);
	  if(mIntake.hasCube()) {
	    mDrive.setSpeed(0.0, 0.0);
	    mCount++;
	  } else {
	      mDrive.setSpeedTurn(mDriveSpeed, 0.0);
	  }
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
	    return mCount > 35;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	    mIntake.setSpeed(0.0);
	    mDrive.setSpeed(0.0, 0.0);
	    mIntake.closePincer();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
