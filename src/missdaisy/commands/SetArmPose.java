package missdaisy.commands;

import org.opencv.core.TickMeter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Drive;
import missdaisy.subsystems.Wrist;
import missdaisy.utilities.DaisyMath;

public class SetArmPose extends Command{
   
  private double shoulderTargetAngle;
  private double wristTargetAngle;
  private double startTime;
  private Arm mArm;
  private Wrist mWrist;
  private Drive mDrive;
  private boolean isTipped;
  
  public SetArmPose(double shoulderAngle, double wristAngle) {
    // Use requires() here to declare subsystem dependencies
    super("GoToShoulderAngle");
    requires(Robot.mArm);
    shoulderTargetAngle = shoulderAngle;
    wristTargetAngle = wristAngle;
    mArm = Arm.getInstance();
    mWrist = Wrist.getInstance();
    mDrive = Drive.getInstance();
    isTipped = false;
  }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    //mArm.limitSpeed();
    mArm.useFirstPIDS();
    mArm.setAngle(shoulderTargetAngle);
    mWrist.useSecondPIDS();
    mWrist.setDesiredWristAngle(wristTargetAngle);
    SmartDashboard.putBoolean("SetArmPose Running", false);
    startTime = System.currentTimeMillis();
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //mArm.setAngle(targetAngle);
    mWrist.maintainExtension();
    SmartDashboard.putBoolean("SetArmPose Running", true);
    //if(mArm.getLowerLimitSwitch() && mArm.getEncoderRate() < 0.0) {
      //mArm.setSpeed(0.0);
    //}
    if (!isTipped && Math.abs(mDrive.getGyroPitch()) > Constants.Properties.UPPER_TIP_THRESHOLD){
      mArm.setAngle(Constants.ArmPose.LOAD_CUBE);
    } else if ( isTipped && Math.abs(mDrive.getGyroPitch()) < Constants.Properties.LOWER_TIP_THRESHOLD){
      mArm.setAngle(shoulderTargetAngle);
    }
  }
  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
      return (Math.abs(DaisyMath.boundAngleNeg180to180Degrees(shoulderTargetAngle - mArm.getShoulderAngleInDegrees()))
          < Constants.Arm.ARM_TOLERANCE 
          &&
          Math.abs(DaisyMath.boundAngleNeg180to180Degrees(wristTargetAngle - mWrist.getWristAngleInDegrees()))
          < Constants.Arm.WRIST_TOLERANCE)
          || System.currentTimeMillis() - startTime > 3250;
  }
  
  // Called once after isFinished returns true
  @Override
  protected void end() {
    mArm.setSpeed(0.0);
    //mArm.delimitSpeed();
    SmartDashboard.putBoolean("SetArmPose Running", false);
    mArm.useSecondPIDS();
    //mWrist.useFirstPIDS();
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
