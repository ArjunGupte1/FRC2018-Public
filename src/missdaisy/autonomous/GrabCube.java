package missdaisy.autonomous;

import missdaisy.subsystems.Drive;
import missdaisy.subsystems.Intake;

public class GrabCube extends TimeoutState{
  private Intake mIntake;
  private Drive mDrive;
  private double mIntakeSpeed = 0.0;

  // todo: Set timeout to be a parameter
  public GrabCube(double intakeSpeed) {
    super("RunIntake", 1000);
    mIntake = Intake.getInstance();
    mDrive = Drive.getInstance();
    mIntakeSpeed = intakeSpeed;
  }

  /**
   * Sets the drive base's current controller to be the drive distance controller
   */
  @Override
  public void enter() {
    super.enter();
    mIntake.setSpeed(mIntakeSpeed);
    mDrive.setSpeed(0.25, 0.25);
  }

  @Override
  public void running() {
    
  }

  /**
   * Ensures the robot's drive base is in an expected state.
   */
  @Override
  public void exit() {
    mIntake.setSpeed(0.0);
    mDrive.setSpeed(0.0, 0.0);
  }

  @Override
  public boolean isDone() {
    return mIntake.hasCube();
  }
}