package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.subsystems.Wrist;
import missdaisy.utilities.DaisyMath;

public class AdjustWrist extends Command{
  
  private Wrist mWrist;
  private double targetAngle;
  
  public AdjustWrist(double angle) {
    // Use requires() here to declare subsystem dependencies
    super("AdjustWrist");
    
    mWrist = Wrist.getInstance();
    requires(mWrist);
    
    targetAngle = angle;
      
  }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    //mWrist.limitSpeed();
    mWrist.useFirstPIDS();
    mWrist.setWristOffset(0.0);
    targetAngle += mWrist.getWristAngleInDegrees();
    mWrist.setAngle(targetAngle);
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
   
  }
  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Math.abs(DaisyMath.boundAngleNeg180to180Degrees(targetAngle - mWrist.getWristAngleInDegrees()))
        < Constants.Arm.WRIST_TOLERANCE ;
  }
  
  // Called once after isFinished returns true
  @Override
  protected void end() {
    mWrist.setSpeed(0.0);
    //mWrist.delimitSpeed();
    mWrist.useSecondPIDS();
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
