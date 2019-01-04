package missdaisy.commands;

import java.io.File;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;
import missdaisy.Constants;
import missdaisy.Robot;
import missdaisy.loops.controllers.EncoderFollower;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Drive;
import missdaisy.subsystems.Intake;
import missdaisy.utilities.DaisyMath;

/**
 *
 */
public class DrivePath extends Command {
    private Drive mDrive;
    private TankModifier mModifier;
    private EncoderFollower mLeftPath, mRightPath;
    private String mFile;
    private boolean mDirection, flipAngle;
    private double initial_heading;
    private int segment_count;
    
    public DrivePath(String filename, double direction) {
      super("DriveSegment2");
      System.out.println("Creating DriveSegment2");
      mDrive = Drive.getInstance();
          
      mLeftPath = new EncoderFollower();
      mRightPath = new EncoderFollower();
      
      mFile = "/home/lvuser/trajectories/" + filename + ".csv";
      mDirection = direction < 0.0;
      flipAngle = false;
      
      initial_heading = 0.0;
      segment_count = 0;
      // Use requires() here to declare subsystem dependencies
      requires(mDrive);
  }

    
	public DrivePath(String filename, double direction, boolean flipAng) {
	    this(filename, direction);
	    flipAngle = flipAng;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	    Intake.getInstance().closePincer();
	    
	    segment_count = 0;
	  
	    System.out.println("Following " + mFile);
	    
	    initial_heading = mDrive.getGyroAngle();
	    
	    mLeftPath.reset();
	    mRightPath.reset();
	    mDrive.resetEncoders();
	    
	    File myFile = new File(mFile);
	    Trajectory trajectory = Pathfinder.readFromCSV(myFile);
	    
	    mModifier = new TankModifier(trajectory);
	    mModifier.modify(Constants.Properties.DRIVE_WIDTH * Constants.UnitConv.IN_TO_M);
	    
	    if(mDirection) {
	      mLeftPath.setTrajectory(mModifier.getLeftTrajectory());
	      mRightPath.setTrajectory(mModifier.getRightTrajectory());
	    } else {
	      mLeftPath.setTrajectory(mModifier.getRightTrajectory());
	      mRightPath.setTrajectory(mModifier.getLeftTrajectory());
	    }
	    mLeftPath.configurePIDVA(0.4, 0.0, 0.0, 1.0 /
	        (Constants.Drive.DRIVE_MAX_VELOCITY * Constants.UnitConv.FPS_TO_MPS ), 0.1);
	    mLeftPath.configureEncoder(-1 * mDrive.getLeftEncoder().get(), 256, 4.0 * Constants.UnitConv.IN_TO_M);
	    
	    mRightPath.configurePIDVA(0.4, 0.0, 0.0, 1.0 /
	        (Constants.Drive.DRIVE_MAX_VELOCITY * Constants.UnitConv.FPS_TO_MPS ), 0.1);
	    mRightPath.configureEncoder(mDrive.getRightEncoder().get(), 256, 4.0 * Constants.UnitConv.IN_TO_M);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
	  double lSpeed = 0.0;
	    double rSpeed = 0.0;
	    
	    if(mDirection) { //Backwards
	      lSpeed = -1.0 * mLeftPath.calculate(mDrive.getLeftEncoder().get());
	      rSpeed = -1.0 * mRightPath.calculate(-1 * mDrive.getRightEncoder().get());
	    } else { //Forwards
	      lSpeed = mLeftPath.calculate(-1 * mDrive.getLeftEncoder().get());
	      rSpeed = mRightPath.calculate(mDrive.getRightEncoder().get());
	    }
	    
	    double angleDifference = Math.toDegrees(DaisyMath.boundAngleNegPiToPiRadians(mLeftPath.getHeading()
	        - Math.toRadians(mDrive.getGyroAngle() - initial_heading)));
	    
	    if(flipAngle) {
	      angleDifference *= -1.0;
	    }
	    
	    double turn = DaisyMath.minmax(0.8 * (1.0 / 80.0) * angleDifference, -0.8, 0.8);
	    
	    //turn *= 0.175;
	    
	    if(segment_count < 50) {
	      double dir = 1.0;
	      if (mDirection){
	        dir = -1.0;
	      }
	      lSpeed = lSpeed + dir * 0.2 * ((50 - segment_count) / 50.0);
	      rSpeed = rSpeed + dir * 0.2 * ((50 - segment_count) / 50.0);
	    }
	    
	    mDrive.setSpeed(lSpeed + turn, rSpeed - turn);
	    
	    if(Constants.DEBUG_MODE) {
	      
	      SmartDashboard.putNumber("Pathfinder_lSpeed", lSpeed);
	      SmartDashboard.putNumber("Pathfinder_lError", mLeftPath.getLastError() * Constants.UnitConv.M_TO_IN);
	      SmartDashboard.putNumber("Pathfinder_segHeading", Math.toDegrees(mLeftPath.getHeading()));
	      //SmartDashboard.putNumber("Pathfinder_lPos", mLeftPath.getSegment().position * Constants.UnitConv.M_TO_IN);
	      //SmartDashboard.putNumber("Pathfinder_lVel", mLeftPath.getSegment().velocity);
	      //SmartDashboard.putNumber("Pathfinder_rVel", mRightPath.getSegment().velocity);
	      //SmartDashboard.putNumber("Pathfinder_rPos", mRightPath.getSegment().position * Constants.UnitConv.M_TO_IN);
	      //SmartDashboard.putNumber("Pathfinder_lSegY", mLeftPath.getSegment().y * Constants.UnitConv.M_TO_IN);
	      //SmartDashboard.putNumber("Pathfinder_rSegY", mRightPath.getSegment().y * Constants.UnitConv.M_TO_IN);
	      SmartDashboard.putNumber("Pathfinder_rSpeed", rSpeed);
	      SmartDashboard.putNumber("Pathfinder_rError", mRightPath.getLastError() * Constants.UnitConv.M_TO_IN);
	      SmartDashboard.putNumber("Pathfinder_turn", turn);
	      SmartDashboard.putNumber("Pathfinder_angleDifference", angleDifference);
	      mDrive.logToDashboard();
	    }
	    segment_count++;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
	    return mLeftPath.isFinished() && mRightPath.isFinished();
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
	}
}
