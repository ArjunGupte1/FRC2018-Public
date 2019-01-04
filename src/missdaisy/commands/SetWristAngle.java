package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.subsystems.Wrist;

/**
 * Creates a Command that will set the desired wrist angle that will be achieved
 * when we are not trying to keep the arm within the 16" extension. 
 * 
 * @author AJN
 */

public class SetWristAngle extends Command{
  
  private Wrist mWrist;
  private double targetAngle;
  
  public SetWristAngle(double angle) {
    // Use requires() here to declare subsystem dependencies
    super("SetWristAngle");
    
    mWrist = Wrist.getInstance();
    requires(mWrist);
    
    targetAngle = angle;
  }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    mWrist.setDesiredWristAngle(targetAngle);
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
  
  public boolean isInterruptible() {
    return true;
  }

}
