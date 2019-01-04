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
public class EngageLeftPTO extends Command {
  
    private Drive mDrive;
    private int count;
  
    public EngageLeftPTO() {
        // Use requires() here to declare subsystem dependencies
        super("EngageLeftPTO");
        
        mDrive = Drive.getInstance();
        mDrive.resetEncoders();
        count = 0;
        
        requires(mDrive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {           
      mDrive.resetEncoders();
      mDrive.engageLeftPopper();
      mDrive.engageLeftPTO();
      count = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      if(count > 5) {
        mDrive.setSpeed(-1.0, 0.0);
      }
      count++;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;//count >= 10;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      //mDrive.disengagePoppers();
      mDrive.setSpeed(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      end();
    }
    
    public boolean isInterruptible() {
      return true;
    }
}
