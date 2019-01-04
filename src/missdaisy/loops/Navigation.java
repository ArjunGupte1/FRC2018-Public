package missdaisy.loops;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.subsystems.Drive;
import missdaisy.utilities.DaisyMath;

/**
 * The representation of the location, heading (angle), speed, and distance (traveled) of the robot
 * 
 * @author jrussell
 */
public class Navigation {
  private static Navigation navigationInstance;
  // Sensors
  private Encoder mLeftDriveEncoder;
  private Encoder mRightDriveEncoder;

  // Navigational state
  private double x = 0.0; // positive from driver facing center of the field
  private double y = 0.0; // positive from driver looking left
  private double theta0 = 0.0; // anti-clockwise from center of field to left
  private double thetaLast = 0.0;
  private double leftEncoderLast = 0.0;
  private double rightEncoderLast = 0.0;
  private double mApproxTargetAngle = 0.0;

  public static Navigation getInstance() {
    if (navigationInstance == null)
      navigationInstance = new Navigation();
    return navigationInstance;
  }

  private Navigation() {
    mLeftDriveEncoder = Drive.getInstance().getLeftEncoder();
    mRightDriveEncoder = Drive.getInstance().getRightEncoder();
  }

  /**
   * Reset your current location to specified coordinates
   * 
   * @param x desired x coordinate
   * @param y desired y coordinate
   * @param theta desired robot heading (angle)
   */
  public synchronized void resetRobotPosition(double x, double y, double theta) {
    mApproxTargetAngle =
        DaisyMath.boundAngle0to360Degrees((mApproxTargetAngle - getHeadingInDegrees()) + theta);
    this.x = x;
    this.y = y;
    theta0 = theta;
    thetaLast = theta;
    mLeftDriveEncoder.reset();
    mRightDriveEncoder.reset();
    Drive.getInstance().resetGyro();
  }

  public void resetEncoders() {
    mLeftDriveEncoder.reset();
    mRightDriveEncoder.reset();
  }

  public synchronized double getApproxTargetAngle() {
    return mApproxTargetAngle;
  }

  public synchronized void setApproxTargetAngle(double newAngle) {
    mApproxTargetAngle = DaisyMath.boundAngle0to360Degrees(newAngle);
  }

  /**
   * @return the current x coordinate of the robot
   */
  public synchronized double getXinInches() {
    return x;
  }

  /**
   * @return the current y coordinate of the robot
   */
  public synchronized double getYinInches() {
    return y;
  }

  /**
   * @return the current angle of the robot
   */
  public double getHeadingInDegrees() {
    return DaisyMath.boundAngle0to360Degrees(thetaLast + theta0);
  }

  public synchronized void run() {
    // Read sensors
    double left = mLeftDriveEncoder.getDistance();
    double right = mRightDriveEncoder.getDistance();
    // getRoll = the robots pitch
    // getPitch = the robots roll
    double yaw = DaisyMath.boundAngle0to360Degrees(Drive.getInstance().getGyroAngle() + theta0);

    // the distance the drive has gone since last cycle
    double distance = ((left + right) - (leftEncoderLast + rightEncoderLast)) / 2.0;
    /*
     * if( (left-leftEncoderLast == 0.0) && (right-rightEncoderLast == 0.0) ) { // Fuse encoders
     * with gyro to prevent drift gyro.resetYaw(); theta0 = DaisyMath.boundAngle0to360Degrees(theta0
     * + thetaLast); theta = thetaLast = 0.0; }
     */
    double thetaRad = Math.toRadians(yaw);
    // calculate the current x + y coordinates, based on the distance traveled the angle turned
    x += distance * Math.cos(thetaRad);
    y += distance * Math.sin(thetaRad);

    leftEncoderLast = left;
    rightEncoderLast = right;
    thetaLast = yaw;

    SmartDashboard.putNumber("heading", yaw);
  }

  public double getLeftEncoderDistance() {
    return mLeftDriveEncoder.getDistance();
  }

  public double getRightEncoderDistance() {
    return -1.0 * mRightDriveEncoder.getDistance();
  }

  public double getLeftEncoderRate() {
    return mLeftDriveEncoder.getRate();
  }

  public double getRightEncoderRate() {
    return mRightDriveEncoder.getRate();
  }

  /**
   * Uses the distance the robot moves per one shaft rotation to calculate the speed of the robot
   * 
   * @return the speed in units/sec, defined by the setDistancePerpulse.
   */
  public double getAverageEncoderRate() {
    double average = (getLeftEncoderRate() + getRightEncoderRate()) / 2;
    return average;
  }

  /**
   * Uses the distance the robot moves per one shaft rotation and the number of rotations to
   * calculate distance traveled.
   * 
   * @return The distance traveled in units, defined by the setDistancePerPulse
   */
  public double getAverageEncoderDistance() {
    double average = (getLeftEncoderDistance() + getRightEncoderDistance()) / 2;
    return average;
  }

  public void logToDashboard() {
    SmartDashboard.putNumber("LeftEncoderRate", getLeftEncoderRate());
    SmartDashboard.putNumber("RightEncoderRate", getRightEncoderRate());
    SmartDashboard.putNumber("Distance", getAverageEncoderDistance());
  }
}