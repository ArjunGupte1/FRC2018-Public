package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;
import missdaisy.utilities.DaisyMath;

public class GoToShoulderAngle extends Command{
   
  private double targetAngle;
  private Arm mArm;
  private boolean goToAngle;
  
  public GoToShoulderAngle(double angle, boolean goToAngle) {
    // Use requires() here to declare subsystem dependencies
    super("GoToShoulderAngle");
    requires(Robot.mArm);
    targetAngle = angle;
    this.goToAngle = goToAngle;
    mArm = Arm.getInstance();
  }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    //mArm.limitSpeed();
    mArm.useFirstPIDS();
    if(goToAngle) {
        mArm.setAngle(targetAngle);
    } else {
        mArm.setAngle(SmartDashboard.getNumber("Set Shoulder Angle", 45.0));
    }
    SmartDashboard.putBoolean("GoToShoulderAngle Running", false);
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //mArm.setAngle(targetAngle);
    SmartDashboard.putBoolean("GoToShoulderAngle Running", true);
  }
  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    boolean finished = Math.abs(DaisyMath.boundAngleNeg180to180Degrees(targetAngle - mArm.getShoulderAngleInDegrees()))
                          < Constants.Arm.ARM_TOLERANCE ;
    mArm.setDone(finished);
    return finished;
  }
  
  // Called once after isFinished returns true
  @Override
  protected void end() {
    mArm.setSpeed(0.0);
    //mArm.delimitSpeed();
    SmartDashboard.putBoolean("GoToShoulderAngle Running", false);
    mArm.useSecondPIDS();
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
