package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Wrist;
import missdaisy.utilities.DaisyMath;

public class MaintainWristExtension extends Command{
   
  private Wrist mWrist;
  
  public MaintainWristExtension() {
    // Use requires() here to declare subsystem dependencies
    super("GoToWristAngle");
    
    mWrist = Wrist.getInstance();
    requires(mWrist);
      
  }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
      mWrist.useSecondPIDS();
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {    
    // Determine what the forward extension of the arm would
    mWrist.maintainExtension();
  }
  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
      return false;
  }
  
  // Called once after isFinished returns true
  @Override
  protected void end() {
    mWrist.setSpeed(0.0);
    mWrist.useFirstPIDS();
    //mArm.delimitSpeed();
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
