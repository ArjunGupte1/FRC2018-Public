package missdaisy.autonomous;

import edu.wpi.first.wpilibj.Timer;
import missdaisy.loops.Navigation;
import missdaisy.loops.controllers.DriveTurnController;
import missdaisy.subsystems.Drive;
import missdaisy.utilities.DaisyMath;

/**
 * Turns the robot to an angle relative to the robot's current angular position.
 *
 * @author Joshua Sizer
 */
public class Turn extends State {

  private Drive mDrive;
  private Navigation mNavigation;
  private DriveTurnController mDriveController;
  private double mAngle;
  private double mStateStartTime;

  /**
   * @param angle The angle to turn to.
   */
  public Turn(double angle) {
    super("Turn");
    mDrive = Drive.getInstance();
    mNavigation = Navigation.getInstance();
    mDriveController = DriveTurnController.getInstance();
    mAngle = angle;
  }

  @Override
  public void enter() {
    mDriveController
        .setGoal(DaisyMath.boundAngle0to360Degrees(mNavigation.getHeadingInDegrees() + mAngle));
    mDrive.setCurrentController(mDriveController);
    mStateStartTime = Timer.getFPGATimestamp();
  }

  /**
   * The drive turn controller runs in it's own thread
   */
  @Override
  public void running() {}

  /**
   * Puts the robot back into a known state.
   */
  @Override
  public void exit() {
    mDrive.setOpenLoop();
  }


  @Override
  public boolean isDone() {
    return (Timer.getFPGATimestamp() - mStateStartTime > 1.5) || mDriveController.onTarget();
  }

}
