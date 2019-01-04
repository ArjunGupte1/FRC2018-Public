package missdaisy.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.commands.DriveTankMode;
import missdaisy.utilities.DaisyMath;

public class Drive extends DaisySubsystem {
  
  private static Drive driveInstance;
  private PWMTalonSRX mLeftDriveMotorA, mLeftDriveMotorB, mLeftDriveMotorC;
  private PWMTalonSRX mRightDriveMotorA, mRightDriveMotorB, mRightDriveMotorC;
  private Encoder mLeftDriveEncoder;
  private Encoder mRightDriveEncoder;
  private AHRS mGyro;
  private DoubleSolenoid mLeftPTO, mLeftPopper;
  private Solenoid mClimber;
  private boolean isLeftPTO, isLeftPopper, climberDeployed;
  private double mGyroOffset, mPitchOffset;
  private double p, i, d;
  
  public static Drive getInstance() {
    if(driveInstance == null) {
      driveInstance = new Drive();
    }
    return driveInstance;
  }
  
  public Drive()
  {
    mLeftDriveMotorA = new PWMTalonSRX(Constants.Drive.LEFT_MOTOR_A_PORT);
    mLeftDriveMotorB = new PWMTalonSRX(Constants.Drive.LEFT_MOTOR_B_PORT);
    mLeftDriveMotorC = new PWMTalonSRX(Constants.Drive.LEFT_MOTOR_C_PORT);
    
    mRightDriveMotorA = new PWMTalonSRX(Constants.Drive.RIGHT_MOTOR_A_PORT);
    mRightDriveMotorB = new PWMTalonSRX(Constants.Drive.RIGHT_MOTOR_B_PORT);
    mRightDriveMotorC = new PWMTalonSRX(Constants.Drive.RIGHT_MOTOR_C_PORT);
    mRightDriveMotorA.setInverted(true);
    mRightDriveMotorB.setInverted(true);
    mRightDriveMotorC.setInverted(true);
        
    
    mLeftPTO = new DoubleSolenoid(Constants.Solenoids.LEFT_PTO_A, Constants.Solenoids.LEFT_PTO_B);
    //mLeftPTO.set(Value.kReverse);
    mLeftPopper = new DoubleSolenoid(Constants.Solenoids.RIGHT_POPPER_A, Constants.Solenoids.RIGHT_POPPER_B);
    disengagePoppers();
    
    isLeftPTO = false;
    isLeftPopper = false;

    mClimber = new Solenoid(Constants.Solenoids.CLIMBER_RELEASE);
    engageClimber();
    climberDeployed = false;
    
    mLeftDriveEncoder = new Encoder(Constants.Drive.LEFT_ENCODER_A_PORT, Constants.Drive.LEFT_ENCODER_B_PORT,
        false, Encoder.EncodingType.k4X);
    mRightDriveEncoder = new Encoder(Constants.Drive.RIGHT_ENCODER_A_PORT, Constants.Drive.RIGHT_ENCODER_B_PORT,
        false, Encoder.EncodingType.k4X);
    
    mLeftDriveEncoder.reset();
    mRightDriveEncoder.reset();
        
    mLeftDriveEncoder.setDistancePerPulse(Constants.Drive.DISTANCE_PER_ENCODER_PULSE);
    mRightDriveEncoder.setDistancePerPulse(Constants.Drive.DISTANCE_PER_ENCODER_PULSE);
    
    p = 0;
    i = 0;
    d = 0;
    
    try {
      /* Communicate w/navX MXP via the MXP SPI Bus. */
      /* Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB */
      /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
      mGyro = new AHRS(SPI.Port.kMXP);

    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    }
    
    mGyroOffset = 0.0;
    
    SmartDashboard.putNumber("Drive Percentage Input", 0.0);
  }
  
  public void setSpeedTurn( double speed, double turn) {
    
    mLeftDriveMotorA.set(speed + turn);
    mLeftDriveMotorB.set(speed + turn);
    mLeftDriveMotorC.set(speed + turn);
    
    mRightDriveMotorA.set(speed - turn);
    mRightDriveMotorB.set(speed - turn);
    mRightDriveMotorC.set(speed - turn);
  }
  
  public void setSpeed( double lSpeed, double rSpeed) {
    
    mLeftDriveMotorA.set(lSpeed);
    mLeftDriveMotorB.set(lSpeed);
    mLeftDriveMotorC.set(lSpeed);
    
    mRightDriveMotorA.set(rSpeed);
    mRightDriveMotorB.set(rSpeed);
    mRightDriveMotorC.set(rSpeed);
  }
  
  public void deployClimber() {
    //if (DriverStation.getInstance().getMatchTime() > Constants.Timing.ENDGAME_TIME_START){
      mClimber.set(true);
      climberDeployed = true;
    //}
  }
  
  public void holdClimber() {
    setSpeed(-0.1, 0.0);
  }
  
  public void unSpoolClimber() {
    setSpeed(0.9, 0.0);
  }
  
  public void restClimber() {
    setSpeed(0.0, 0.0);
  }
  
  public void engageClimber() {
    
    mClimber.set(false);
    climberDeployed = false;
    disengagePoppers();
  }
  
  public void engageLeftPTO() {
    if (climberDeployed){
      //mLeftPopper.set(Value.kReverse);
      //isLeftPopper = false;
      mLeftPTO.set(Value.kReverse);
      isLeftPTO = true;
    }
  }
  
  public void engageLeftPopper() {
    
      //mLeftPTO.set(Value.kReverse);
      //isLeftPTO = false;
      mLeftPopper.set(Value.kForward);
      isLeftPopper = true;
  }
  
  public void disengagePoppers() {
    
    mLeftPopper.set(Value.kReverse);
    isLeftPopper = false;
    mLeftPTO.set(Value.kForward);
    isLeftPTO = false;
  
    System.out.println("Both Poppers Disengaged!");
  }
  
  public boolean getLeftPTOState() {
    return isLeftPTO;
  }
  
  public boolean getLeftPopperState() {
    return isLeftPopper;
  }
  
  public boolean getClimberDeployedState() {
    return climberDeployed;
  }
  
  public void stop() {
    mLeftDriveMotorA.stopMotor();
    mLeftDriveMotorB.stopMotor();
    mLeftDriveMotorC.stopMotor();
    mRightDriveMotorA.stopMotor();
    mRightDriveMotorB.stopMotor();
    mRightDriveMotorC.stopMotor();
  }
  
  public void resetEncoders() {
    mLeftDriveEncoder.reset();
    mRightDriveEncoder.reset();
  }
  
  public double getP() {
    return p;
  }
  
  public double getI() {
    return i;
  }
  
  public double getD() {
    return d;
  }
  
  public void resetGyro() {
    mGyroOffset = mGyro.getAngle();
    mPitchOffset = mGyro.getPitch();
  }
  
  public double getGyroAngle() {
    return DaisyMath.boundAngleNeg180to180Degrees(mGyro.getAngle() - mGyroOffset);
  }
  
  public double getGyroPitch() {
    //return DaisyMath.boundAngleNeg180to180Degrees(mGyro.getPitch() - mPitchOffset);
    return mGyro.getRoll();
  }
  
  public void resetSpeed() {
    mLeftDriveMotorA.setSpeed(0.0);
    mLeftDriveMotorB.setSpeed(0.0);
    mLeftDriveMotorC.setSpeed(0.0);
    mRightDriveMotorA.setSpeed(0.0);
    mRightDriveMotorB.setSpeed(0.0);
    mRightDriveMotorC.setSpeed(0.0);
  }

  
  public double getLeftDist() {
    //return mLeftDriveEncoder.get();
    return -1.0 * mLeftDriveEncoder.getDistance();
  }
  
  public double getRightDist() {
    return mRightDriveEncoder.getDistance();
  }
  
  public double getLeftVelocity() {
    return -1.0 * mLeftDriveEncoder.getRate();
  }
  
  public double getRightVelocity() {
    return mRightDriveEncoder.getRate();
  }
  
  public Encoder getLeftEncoder() {
    return mLeftDriveEncoder;
  }
  
  public Encoder getRightEncoder() {
    return mRightDriveEncoder;
  }
  
  //public AHRS getGyro() {
    //return mGyro;
  //}
  
  public void logToDashboard()
  {
    SmartDashboard.putNumber("Left Encoder Distance", getLeftDist());
    SmartDashboard.putNumber("Right Encoder Distance", getRightDist());
    SmartDashboard.putNumber("Left Encoder Speed", getLeftVelocity());
    SmartDashboard.putNumber("Right Encoder Speed", getRightVelocity());
    SmartDashboard.putNumber("GryoYaw", getGyroAngle());
    SmartDashboard.putNumber("GryoPitch", getGyroPitch());
    SmartDashboard.putBoolean("LeftPTO", getLeftPTOState());
    SmartDashboard.putBoolean("LeftPopper", getLeftPopperState());
    SmartDashboard.putBoolean("ClimberDeployed", getClimberDeployedState());
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveTankMode());
    
  }
  
  public void useAlphaFilter(boolean bool) {
    
  }
}
