package missdaisy.autonomous;

import java.io.File;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;
import missdaisy.Constants;
import missdaisy.loops.controllers.EncoderFollower;
import missdaisy.subsystems.Drive;
import missdaisy.utilities.DaisyMath;

/**
 * Drive the specified segment of the trajectory
 * 
 */

public class DriveSegment2 extends State {
  
  private Drive mDrive;
  private TankModifier mModifier;
  private EncoderFollower mLeftPath, mRightPath;
  private String mFile;
  private boolean mDirection;
  private double initial_heading;
  /**
   * 
   * @param filename The relative path of the folder containing the trajectory's .csv and .txt file. 
   *    Example: "Red/MiddleGear" (without quotes)
   * @param direction Whether to run this trajectory in forward or reverse. 
   * @param useTurnPID Should we use our own PID on the heading error?
   */
  public DriveSegment2(String filename, double direction) {
    super("DriveSegment2");
    System.out.println("Creating DriveSegment2");
    mDrive = Drive.getInstance();
        
    mLeftPath = new EncoderFollower();
    mRightPath = new EncoderFollower();
    
    mFile = "/home/lvuser/trajectories/" + filename + ".csv";
    mDirection = direction < 0.0;
    
    initial_heading = 0.0;
  }

  /**
   * Sets the drive base's current controller to be the drive distance controller
   */
  @Override
  public void enter() {
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
    
    
    mLeftPath.configurePIDVA(0.1, 0.0, 0.0, 1.0 /
        (Constants.Drive.DRIVE_MAX_VELOCITY * Constants.UnitConv.FPS_TO_MPS ), 0.0);
    mLeftPath.configureEncoder(-1 * mDrive.getLeftEncoder().get(), 256, 4.0 * Constants.UnitConv.IN_TO_M);
    
    mRightPath.configurePIDVA(0.1, 0.0, 0.0, 1.0 /
        (Constants.Drive.DRIVE_MAX_VELOCITY * Constants.UnitConv.FPS_TO_MPS ), 0.0);
    mRightPath.configureEncoder(mDrive.getRightEncoder().get(), 256, 4.0 * Constants.UnitConv.IN_TO_M);
  }

  @Override
  public void running() {
    
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
    
    if(mDirection) {
      angleDifference *= -1.0;
    }
    
    double turn = DaisyMath.minmax(0.8 * (1.0 / 120.0) * angleDifference, -0.8, 0.8);
    
    //turn = 0;
    
    mDrive.setSpeed(lSpeed + turn, rSpeed - turn);
    
    if(Constants.DEBUG_MODE) {
      
      SmartDashboard.putNumber("Pathfinder_lSpeed", lSpeed);
      SmartDashboard.putNumber("Pathfinder_lError", mLeftPath.getLastError() * Constants.UnitConv.M_TO_IN);
      SmartDashboard.putNumber("Pathfinder_segHeading", Math.toDegrees(mLeftPath.getHeading()));
      //SmartDashboard.putNumber("Pathfinder_lPos", mLeftPath.getSegment().position * Constants.UnitConv.M_TO_IN);
      //SmartDashboard.putNumber("Pathfinder_lVel", mLeftPath.getSegment().velocity);
      //SmartDashboard.putNumber("Pathfinder_rVel", mRightPath.getSegment().velocity);
      //SmartDashboard.putNumber("Pathfinder_rPos", mRightPath.getSegment().position * Constants.UnitConv.M_TO_IN);
      SmartDashboard.putNumber("Pathfinder_lSegY", mLeftPath.getSegment().y * Constants.UnitConv.M_TO_IN);
      SmartDashboard.putNumber("Pathfinder_rSegY", mRightPath.getSegment().y * Constants.UnitConv.M_TO_IN);
      SmartDashboard.putNumber("Pathfinder_rSpeed", rSpeed);
      SmartDashboard.putNumber("Pathfinder_rError", mRightPath.getLastError() * Constants.UnitConv.M_TO_IN);
      SmartDashboard.putNumber("Pathfinder_turn", turn);
      SmartDashboard.putNumber("Pathfinder_angleDifference", angleDifference);
      mDrive.logToDashboard();
    }
  }

  /**
   * Ensures the robot's drive base is in an expected state.
   */
  @Override
  public void exit() {
    mDrive.setSpeed(0.0, 0.0);    
  }

  /**
   * This state is considered done if the drive distance controller is on target
   */
  @Override
  public boolean isDone() {
    return mLeftPath.isFinished() && mRightPath.isFinished();
  }
}
