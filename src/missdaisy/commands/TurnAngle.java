package missdaisy.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.loops.SynchronousPID;
import missdaisy.subsystems.Drive;
import missdaisy.utilities.DaisyMath;

/**
 *
 */
public class TurnAngle extends Command {
  
    private Drive mDrive;
    private double setpoint;
    private int count;
    private SynchronousPID mTurnPID, mDrivePID;
    private double mGoalAngle = 0.0, mCurrentAngle = 0.0;
  
    public TurnAngle(double angle) {
        // Use requires() here to declare subsystem dependencies
        super("DriveDistance");
        
        mDrive = Drive.getInstance();
        mTurnPID = new SynchronousPID();
        mTurnPID.setContinuous(true);
        mTurnPID.setInputRange(-180.0, 180);
        mTurnPID.setOutputRange(-0.5, 0.5);
        //1.0/180.0, 0.0, 0.2
        mTurnPID.setPID(1.0/100.0, 0.0, 0.0);
        setpoint = angle;
        count = 0;
        
        mDrive.resetEncoders();
        
        requires(mDrive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {      
      mTurnPID.reset();
      mGoalAngle = mDrive.getGyroAngle();
      mDrive.resetEncoders();
      count = 0;
      mTurnPID.setSetpoint(mGoalAngle);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      // what angle you're at right now
      mCurrentAngle = mDrive.getGyroAngle();
      // how far you've traveled since you've set your goal
      
      double error = DaisyMath.boundAngleNeg180to180Degrees(setpoint - mCurrentAngle);
      double turn = error * mTurnPID.getP();
      double dir = 1.0;
      if(error < 0.0){
        dir = -1.0;
      }
      turn = dir * Math.max(Math.abs(turn), .3);
      mDrive.setSpeedTurn(0.0, turn);
      
      if(Math.abs(DaisyMath.boundAngleNeg180to180Degrees(setpoint - mCurrentAngle)) <
          Constants.Properties.PID_DRIVE_ANGLE_TOLERANCE) {
        count++;
      } else {
        count = 0;
      }
      
      SmartDashboard.putNumber("TurnAngle Turn", turn);

      // checks to see if we are on target
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return count > 5;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      mDrive.setSpeedTurn(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      mDrive.setSpeedTurn(0.0, 0.0);
    }
}
