package missdaisy.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.loops.SynchronousPID;
import missdaisy.subsystems.Drive;

/**
 *
 */
public class DisengagePoppers extends Command {
  
    private Drive mDrive;
  
    public DisengagePoppers() {
        // Use requires() here to declare subsystem dependencies
        super("DisengagePTOs");
        
        mDrive = Drive.getInstance();        
        requires(mDrive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      mDrive.disengagePoppers();
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
      end();
    }
}
