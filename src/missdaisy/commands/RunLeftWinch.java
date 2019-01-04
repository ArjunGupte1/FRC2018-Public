package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.OI;
import missdaisy.subsystems.Drive;

/**
 *
 */
public class RunLeftWinch extends Command {
  
    private Drive mDrive;
  
    public RunLeftWinch() {
        // Use requires() here to declare subsystem dependencies
        super("RunLeftWinch");
        
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
      //mDrive.setSpeed(-0.5, 0.0);
      System.out.println("Running Left Winch!");
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      mDrive.setSpeed(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      new DisengagePoppers();
      end();
    }
}
