package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Wrist;
import missdaisy.utilities.DaisyMath;

public class StowWrist extends Command{
  
  private Arm mArm;
  private Wrist mWrist;
  private double targetAngle;
  private boolean goToAngle;
  
  public StowWrist() {
    // Use requires() here to declare subsystem dependencies
    super("StowWrist");
    
    mWrist = Wrist.getInstance();
    mArm = Arm.getInstance();
    targetAngle = 0.0;
    requires(mWrist);
      
  }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    //mWrist.limitSpeed();
    mWrist.useSecondPIDS();
    Robot.mArm.setDone(false);
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // Hold the wrist so it always stays vertical
    targetAngle = DaisyMath.boundAngleNeg180to180Degrees( 
        Constants.WristPose.STOW_ANGLE_IN_WORLD_FRAME - mArm.getShoulderAngleInDegrees());
    mWrist.setAnglePosition(targetAngle);
    SmartDashboard.putNumber("Stow Wrist Target Angle", targetAngle);
  }
  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
      return Robot.mArm.getDone();
  }
  
  // Called once after isFinished returns true
  @Override
  protected void end() {
    mWrist.setSpeed(0.0);
    //mWrist.delimitSpeed();
    mWrist.useFirstPIDS();
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
