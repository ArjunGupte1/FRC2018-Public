package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.subsystems.Drive;

/**
 *
 */
public class ResetGyro extends Command {
  
    private Drive mDrive;
  
    public ResetGyro() {
        // Use requires() here to declare subsystem dependencies
        super("ResetArmEncoder");
        
        mDrive = Drive.getInstance();
        
        //requires(mArm);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      mDrive.resetGyro();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
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
