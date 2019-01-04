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
public class DriveDistance extends Command {
  
    private Drive mDrive;
    private double dist;
    private SynchronousPID mTurnPID, mDrivePID;
    private double mGoalAngle = 0.0, mCurrentAngle = 0.0;
  
    public DriveDistance(double distance) {
        // Use requires() here to declare subsystem dependencies
        super("DriveDistance");
        
        mDrive = Drive.getInstance();
        mTurnPID = new SynchronousPID();
        mTurnPID.setContinuous(true);
        mTurnPID.setInputRange(0, 360);
        mTurnPID.setOutputRange(-0.2, 0.2);
        //1.0/180.0, 0.0, 0.2
        mTurnPID.setPID(1.0/180.0, 0.0, 0.5);
        mDrivePID = new SynchronousPID();
        mDrivePID.setContinuous(true);
        mDrivePID.setInputRange(-650, 650);
        mDrivePID.setOutputRange(-Constants.Drive.DRIVE_MAX_VELOCITY, Constants.Drive.DRIVE_MAX_VELOCITY);
        mDrivePID.setPID(mDrive.getP(), mDrive.getI(), mDrive.getD());
        dist = distance;
        
        mDrive.resetEncoders();
        
        requires(mDrive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {      
      mTurnPID.reset();
      mDrivePID.reset();
      mGoalAngle = mDrive.getGyroAngle();
      mDrive.resetEncoders();
      
      mDrivePID.setSetpoint(dist);
      mTurnPID.setSetpoint(mGoalAngle);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      // what angle you're at right now
      mCurrentAngle = mDrive.getGyroAngle();
      // how far you've traveled since you've set your goal
      
      double speed = mDrivePID.calculate((mDrive.getLeftDist() + mDrive.getRightDist()) / 2);
      double turn = mCurrentAngle * mTurnPID.getP();
      mDrive.setSpeedTurn(speed, turn);


      // checks to see if we are on target
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return mDrivePID.onTarget(Constants.Drive.PID_DRIVE_DISTANCE_TOLERANCE)
            || mDrive.getLeftDist() >= dist - Constants.Drive.PID_DRIVE_DISTANCE_TOLERANCE;
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
