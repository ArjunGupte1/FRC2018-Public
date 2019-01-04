package missdaisy.loops.controllers;

import missdaisy.Constants;
import missdaisy.loops.Navigation;
import missdaisy.subsystems.Drive;
import missdaisy.utilities.Trajectory;
import missdaisy.utilities.TrajectoryFollower;
import missdaisy.utilities.TrajectoryGenerator;

/**
 * Turns to a heading using trajectories for the left and right drive. By turning in opposite directions, we achieve a rotation
 * about the center of our robot. 
 * 
 * @author Joshua Sizer
 *
 */
public class TurnTrajectoryController implements Controller {
  
  private static TurnTrajectoryController mInstance = new TurnTrajectoryController();
  
  private Trajectory mLeftTraj;
  private Trajectory mRightTraj;
  private TrajectoryFollower mLeftTrajFollower;
  private TrajectoryFollower mRightTrajFollower;
  private Navigation mNav;
  private Drive mDrive;
  private double mLeftEncStartPos;
  private double mRightEncStartPos;
  private double mSetpoint;
  
  private static double kP = 1.0;
  private static double kI = 0.0;
  private static double kD = 10.0;
  
  // these are not physical maximums, but the trajectory maximums
  private static final double kLowGearMaximumVelocity = 60.0; // inches/s
  private static final double kHighGearMaximumVelocity = 60.0; // inches/s
  
  private static final double kLowGearMaximumAcceleration = 120.0;
  private static final double kHighGearMaximumAcceleration = 120.0;

  private static final double kMaxJerk = 500;
  
  // feet are converted to inches, the native unit of the encoders
  private static double kHighGearV = 1.0 / 192.0; // (16 ft/s) guess
  private static double kLowGearV = 1.0 / 72.0; // ( 6 ft/s) guess
  private static double kHighGearA = 1.0 / 384.0; // (32 ft/s/s) guess
  private static double kLowGearA = 1.0 / 528.0; // (44 ft/s/s) guess
  
  private static final double kWheelBaseWidth = 36.0; //inches
  
  public static TurnTrajectoryController getInstance() {
    return mInstance;
  }
  
  private TurnTrajectoryController() {
    mLeftTrajFollower = new TrajectoryFollower("Left Side Turn Trajectory Follower");
    mRightTrajFollower = new TrajectoryFollower("Right Side Turn Trajectory Follower");
    mNav = Navigation.getInstance();
    mDrive = Drive.getInstance();
  }
  
  /**
   * The absolute heading to turn to. To turn an angle relative to the robot's current angular position,
   * input Navigation.getCurrentHeadingInDegrees() + angle. Set should ideally be called once per move.
   * 
   * @param setpoint The absolute angle (in degrees) to turn to
   */
  public synchronized void set(double setpoint, boolean isHighGear) {
    mSetpoint = setpoint;
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = 0.005;
    config.max_jerk = kMaxJerk;
    
    if (isHighGear) {
      mLeftTrajFollower.configure(kP, kI, kD, kHighGearV, kHighGearA);
      mRightTrajFollower.configure(kP, kI, kD, kHighGearV, kHighGearA);
      config.max_acc = kHighGearMaximumAcceleration;
      config.max_vel = kHighGearMaximumVelocity;
    } else {
      mLeftTrajFollower.configure(kP, kI, kD, kLowGearV, kLowGearA);
      mRightTrajFollower.configure(kP, kI, kD, kLowGearV, kLowGearA);
      config.max_acc = kLowGearMaximumAcceleration;
      config.max_vel = kLowGearMaximumVelocity;
    }
    
    // negative error indicates we need to turn left
    // so the left profiles needs a negative distance
    // however, the generator does not support negative distances,
    // so we'll deal with this in the following part
   
    double error = calculateError(setpoint - mNav.getHeadingInDegrees(), 0, 360);
    double linearDistance = (error / 360.0) * Math.PI * kWheelBaseWidth;
    
    mLeftTraj = TrajectoryGenerator.generate(config, // the velocity, acceleration, and jerk limits
        TrajectoryGenerator.TrapezoidalStrategy, // type of motion profile
        mNav.getLeftEncoderRate(), // start velocity
        mNav.getHeadingInDegrees(), // start heading
        linearDistance, // distance
        0.0, // end velocity
        setpoint); // goal heading
    
    mRightTraj = TrajectoryGenerator.generate(config, // the velocity, acceleration, and jerk limits
        TrajectoryGenerator.TrapezoidalStrategy, // type of motino profile
        mNav.getRightEncoderRate(), // start velocity
        mNav.getHeadingInDegrees(), // start heading
        linearDistance, // distance
        0.0, // end velocity
        setpoint); // goal heading
    
    mLeftTrajFollower.setTrajectory(mLeftTraj);
    mRightTrajFollower.setTrajectory(mRightTraj);
    
    mLeftEncStartPos = mNav.getLeftEncoderDistance();
    mRightEncStartPos = mNav.getRightEncoderDistance();
  }

  @Override
  public void run() { 
    double leftOutput;
    double rightOutput;
    
    // if set is called while this loop is running, we want our current output to be calculated and sent to
    // the drive before we change our trajectories
    synchronized (this) {
      double error = mSetpoint - mNav.getHeadingInDegrees();

      // negative indicates left wheel needs to run in reverse
      if (error < 0) {
        leftOutput = -1.0 * mLeftTrajFollower.calculate(Math.abs(mNav.getLeftEncoderDistance() - mLeftEncStartPos));
        rightOutput = mRightTrajFollower.calculate(Math.abs(mNav.getRightEncoderDistance() - mRightEncStartPos));
      } else {
        leftOutput = -1.0 * mLeftTrajFollower.calculate(Math.abs(mNav.getLeftEncoderDistance() - mLeftEncStartPos));
        rightOutput = mRightTrajFollower.calculate(Math.abs(mNav.getRightEncoderDistance() - mRightEncStartPos));
      }
    }
    
    if (onTarget()) {
      mDrive.setSpeedTurn(0.0, 0.0);
    } else {
      mDrive.setSpeedTurn(leftOutput, rightOutput);
    }
  }
  
  /**
   * This will calculate the smaller error assuming the output is continuous
   * 
   * @param error The error was we normally calulate it
   * @param minimumInput The smallest input possible
   * @param maximumInput The largest input possible 
   * @return
   */
  private double calculateError(double error, double minimumInput, double maximumInput) {
    if (Math.abs(error) > (maximumInput - minimumInput) / 2) {
      if (error > 0) {
        error = error - maximumInput + minimumInput;
      } else {
        error = error + maximumInput - minimumInput;
      }
    }
    return error;
  }

  @Override
  public void reset() {
    mLeftEncStartPos = mNav.getLeftEncoderDistance();
    mRightEncStartPos = mNav.getRightEncoderDistance();
  }

  @Override
  public boolean onTarget() {
    // TODO Auto-generated method stub
    return mLeftTrajFollower.isFinishedTrajectory() && mRightTrajFollower.isFinishedTrajectory();
  }

  @Override
  public void loadProperties() {
    // TODO Auto-generated method stub

  }

}
