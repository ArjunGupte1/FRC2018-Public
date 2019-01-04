package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.OI;
import missdaisy.subsystems.Drive;

/**
 *
 */
public class DriveTemporary extends Command {
  
    private Drive mDrive;
  
    public DriveTemporary() {
        // Use requires() here to declare subsystem dependencies
        super("DriveTankMode");
        
        mDrive = Drive.getInstance();
        
        requires(mDrive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      mDrive.setSpeedTurn(SmartDashboard.getNumber("Drive Percentage Input", 0.0), 0.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
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
