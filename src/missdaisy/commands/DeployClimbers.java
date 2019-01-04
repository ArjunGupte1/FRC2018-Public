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
public class DeployClimbers extends Command {
  
    private Drive mDrive;
    private int count;
  
    public DeployClimbers() {
        // Use requires() here to declare subsystem dependencies
        super("EngageLeftPTO");
        
        mDrive = Drive.getInstance();
        requires(mDrive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      mDrive.deployClimber();
      count = 0;
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
