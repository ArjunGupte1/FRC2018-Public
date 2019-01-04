package missdaisy.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.Robot;
import missdaisy.commands.MoveWrist;
import missdaisy.utilities.DaisyMath;


/**
 * Class for the wrist subsystem of the 2018 robot.
 *
 * A subsystem is a mechanism or cohesive function of a robot. Examples could be: Drive, Arm,
 * Shooter, etc.
 *
 * @author Adam341
 */
public class Wrist extends DaisySubsystem {

  private static Wrist instance = null;
  //private static DataLogger dataLogger = null;
  private TalonSRX mWristMotor;
  private double desiredWristAngle = 0.0;
  private boolean sensorIsAbsolute = true, isManualControl = false;
  private int sensorWaitCount = 0;
  private boolean wristUpTrigger, wristNeutralTrigger, wristDownTrigger;
  private double mWristOffset;
  private double speedDampener;
  private boolean dampenSpeed;
  private double mForwardExtension = 0.0;
  private DigitalInput mWristLimitSwitch;

  /**
   * Gets the instance of the wrist. Used in order to never have more than one wrist object,
   * ever.
   *
   * @return The one and only instance of the shooter
   */
  public static Wrist getInstance() {
    if (instance == null) {
      instance = new Wrist();
    }
    return instance;
  }

  private Wrist() {
    mWristMotor = new TalonSRX(Constants.CAN.WRIST_TALONSRX_ID);
    //mWristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
    //mWristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    mWristMotor.configAllowableClosedloopError(0, 80, 10);
    mWristMotor.configMotionAcceleration(960, 10);
    mWristMotor.configMotionCruiseVelocity(2400, 10);
    mWristMotor.setInverted(true);
    mWristMotor.setSensorPhase(true);
    mWristMotor.setNeutralMode(NeutralMode.Brake);
    mWristMotor.config_kP(0, 1.0, Constants.Properties.K_TIMEOUT_MS);
    mWristMotor.config_kI(0, 0.0, Constants.Properties.K_TIMEOUT_MS);
    mWristMotor.config_kD(0, 1.5, Constants.Properties.K_TIMEOUT_MS);
    mWristMotor.config_kF(0, 0.0, Constants.Properties.K_TIMEOUT_MS);
    mWristMotor.configForwardSoftLimitEnable(true, Constants.Properties.K_TIMEOUT_MS);
    mWristMotor.configReverseSoftLimitEnable(true, Constants.Properties.K_TIMEOUT_MS);
    mWristMotor.configForwardSoftLimitThreshold(Constants.Arm.WRIST_FORWARD_SOFT_LIMIT, Constants.Properties.K_TIMEOUT_MS);
    mWristMotor.configReverseSoftLimitThreshold(Constants.Arm.WRIST_REVERSE_SOFT_LIMIT, Constants.Properties.K_TIMEOUT_MS);
    
    mWristLimitSwitch = new DigitalInput(Constants.Arm.WRIST_LIMIT_SWITCH);
    this.setDesiredWristAngle(getWristAngleInDegrees());
    
    //setAbsoluteEncoderMode();
    setRelativeEncoderMode();
    mWristMotor.setSelectedSensorPosition(3250, 0, 10);
    
    // Switch to relative mode for the sensor, might have to do this later to give the sensor time to get an accurate absolute reading first
    ///setRelativeEncoderMode();
    
    // Init required Smartdashboard controls
    speedDampener = 1.0;
    dampenSpeed = false;
    
    SmartDashboard.putBoolean("Wrist/DampenSpeed", dampenSpeed);
    SmartDashboard.putBoolean("Wrist/AllowManualWristControl", false);
    SmartDashboard.putBoolean("Wrist/InAbsoluteMode", true);
  }

  public void setAbsoluteEncoderMode() {
    // Switches the talon encoder to absolute mode
    sensorIsAbsolute = true;
    mWristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
  }

  public void setRelativeEncoderMode() {
    // Switches the talon encoder to relative mode and tries to populate the correct relative offset from zero given the current absolute position
    //int relOffset = (int) DaisyMath.boundEncoderNeg2048To2048(
    //    mWristMotor.getSelectedSensorPosition(0) - Constants.Arm.WRIST_HORIZONTAL_ABSOLUTE_POS);

    mWristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

    //mWristMotor.setSelectedSensorPosition(relOffset, 0, 10);
    sensorIsAbsolute = false;

    SmartDashboard.putBoolean("Wrist/InAbsoluteMode", false);
  }
  
  public void resetRelativeAngleFromAbsoulte() {
 // Switches the talon encoder to relative mode and tries to populate the correct relative offset from zero given the current absolute position
    int relOffset = (int) DaisyMath.boundEncoderNeg2048To2048(
        mWristMotor.getSelectedSensorPosition(0) - Constants.Arm.WRIST_HORIZONTAL_ABSOLUTE_POS);
    
    SmartDashboard.putNumber("Wrist/relOffset", relOffset);

    setRelativeEncoderMode();

    mWristMotor.setSelectedSensorPosition(relOffset, 0, 10);
  }

  public void setAngle(double angle) {
    double sensorTickValue = angle / Constants.Arm.WRIST_DEGREES_PER_TICK;

    mWristMotor.set(ControlMode.MotionMagic, sensorTickValue);

    SmartDashboard.putNumber("Wrist/Wrist Tick Setpoint", sensorTickValue);
    SmartDashboard.putNumber("Wrist/Wrist PID Error", mWristMotor.getClosedLoopError(0));
    System.out.println("Running Wrist setAngle");
  }
  
  public void setAnglePosition(double angle) {
    double sensorTickValue = 2.0 * angle / Constants.Arm.WRIST_DEGREES_PER_TICK - getPosition();

    mWristMotor.set(ControlMode.Position, sensorTickValue);

    SmartDashboard.putNumber("Wrist/Wrist Tick Setpoint", sensorTickValue);
    SmartDashboard.putNumber("Wrist/Wrist PID Error", mWristMotor.getClosedLoopError(0));
  }
  
  public void maintainExtension() {
    double angle = 0.0;
    
    mForwardExtension = Constants.Arm.WRIST_LENGTH * Math.cos(DaisyMath.boundAngleNegPiToPiRadians(Robot.mArm.getShoulderAngleInRadians() + getDesiredWristAngle())) + Constants.Arm.FOREARM_LENGTH * Math.cos(Robot.mArm.getShoulderAngleInRadians());
    if (mForwardExtension > (Constants.Arm.MAX_FORWARD_EXTENSION - 0.5)) {
      
      // Update the wrist target angle to keep it within the 16" extension
      double shoulderAngle = Robot.mArm.getShoulderAngleInRadians();
      double forearmReach = Constants.Arm.FOREARM_LENGTH*Math.cos(shoulderAngle);
      double deltaToBoundary = Constants.Arm.MAX_FORWARD_EXTENSION - forearmReach;
      double wristAngleRobotFrame = Math.acos(deltaToBoundary / Constants.Arm.WRIST_LENGTH);
      // convert the wrist angle to the reference frame of the forearm
      angle = (DaisyMath.boundAngleNegPiToPiRadians(wristAngleRobotFrame - shoulderAngle)) * Constants.UnitConv.RAD_TO_DEG;
      //mWrist.setDesiredWristAngle(targetAngle);
      SmartDashboard.putNumber("Forearm Reach", forearmReach);
      SmartDashboard.putNumber("WristAngleRobotFrame", wristAngleRobotFrame * Constants.UnitConv.RAD_TO_DEG);
      SmartDashboard.putNumber("ShoulderAngle", shoulderAngle * Constants.UnitConv.RAD_TO_DEG);
      SmartDashboard.putNumber("DesiredAngle", getDesiredWristAngle());
      SmartDashboard.putBoolean("Wrist/ForwardExtensionLimited", true);
      /*
      angle = DaisyMath.boundAngleNeg180to180Degrees( 
          Constants.WristPose.STOW_ANGLE_IN_WORLD_FRAME - Robot.mArm.getShoulderAngleInDegrees());
      if(angle > Constants.WristPose.STOW_ANGLE_AT_LOAD) {
        angle = Constants.WristPose.STOW_ANGLE_AT_LOAD;
        
      }
      */
    } else {
      if(Robot.mArm.getShoulderAngleInDegrees() < 0.0 && getDesiredWristAngle() < Constants.WristPose.LOAD_CUBE) {
        angle = Constants.WristPose.LOAD_CUBE;
      } else {
        angle = getDesiredWristAngle();
      }
      SmartDashboard.putBoolean("Wrist/ForwardExtensionLimited", true);
    }
    
    SmartDashboard.putNumber("mForwardExtension", mForwardExtension);
    SmartDashboard.putNumber("Wrist Extension Angle Setpoint", angle);
    
    setAnglePosition(angle);
  }

  public synchronized double getPosition() {
    return mWristMotor.getSelectedSensorPosition(0);
  }

  public void setSpeed(double speed) {
    if (getLimitSwitch() && speed > 0.0){
      speed = 0.0;
    }
    mWristMotor.set(ControlMode.PercentOutput, speed); //speedDampener * speed);
  }
  
  public void setWristUpTrigger(boolean trigger)
  {
    wristUpTrigger = trigger;
  }
  
  public boolean getWristUpTrigger()
  {
    return wristUpTrigger;
  }
  
  public void setWristNeutralTrigger(boolean trigger)
  {
    wristNeutralTrigger = trigger;
  }
  
  public boolean getWristNeutralTrigger()
  {
    return wristNeutralTrigger;
  }
  
  public void setWristDownTrigger(boolean trigger)
  {
    wristDownTrigger = trigger;
  }
  
  public boolean getWristDownTrigger()
  {
    return wristDownTrigger;
  }
  
  public void setWristOffset(double offset)
  {
    mWristOffset = offset;
  }

  public double getWristAngleInRadians() {
    return DaisyMath.boundAngleNegPiToPiRadians(
        (mWristMotor.getSelectedSensorPosition(0) * Constants.Arm.WRIST_RADIANS_PER_TICK));
  }

  public double getWristAngleInDegrees() {
    return DaisyMath.boundAngleNeg180to180Degrees(
        (mWristMotor.getSelectedSensorPosition(0) * Constants.Arm.WRIST_DEGREES_PER_TICK));
  }

  /*
  public void limitSpeed() {
    mWristMotor.configPeakOutputForward(.75, 10);
    mWristMotor.configPeakOutputReverse(-.75, 10);
  }

  public void delimitSpeed() {
    mWristMotor.configPeakOutputForward(1.0, 10);
    mWristMotor.configPeakOutputReverse(-1.0, 10);
  }
  */

  public double getDesiredWristAngle() {
    return desiredWristAngle;
  }

  public void setDesiredWristAngle(double desiredWristAngle) {
    this.desiredWristAngle = desiredWristAngle;
  }
  
  public void resetWristEncoder() {
    //mWristMotor.configSetParameter(ParamEnum.eClearPositionOnIdx, 1, 0x00, 0x00, 10);
    //mWristMotor.configSetParameter(ParamEnum.eClearPositionOnIdx, 0, 0x00, 0x00, 10);
    mWristMotor.setSelectedSensorPosition(0, 0, 10); 
  }
  
  public void useFirstPIDS() {
    mWristMotor.selectProfileSlot(0, 0);
  }
  
  public void useSecondPIDS() {
    mWristMotor.selectProfileSlot(0, 0);
  }

  @Override
  public void initDefaultCommand() {
    //setDefaultCommand(new MaintainWristExtension());
    setDefaultCommand(new MoveWrist());
    //setDefaultCommand(new StowWrist());
  }
  
  public boolean getLimitSwitch() {
    return !mWristLimitSwitch.get();
  }
  
  public void checkForLimitSwitch() {
    if(getLimitSwitch()) {
      mWristMotor.setSelectedSensorPosition(3250, 0, 10);
      if(mWristMotor.getSelectedSensorVelocity(0) > 0.0) {
        mWristMotor.set(ControlMode.PercentOutput, 0.0);
      }
    }
  }
  
  public void logToDashboard() {
    /*
    sensorWaitCount++;
    if (sensorWaitCount == 10) {
      resetRelativeAngleFromAbsoulte();
      System.out.println("Auto reset to relative from absolute");
    }
    boolean inAbsoluteMode = SmartDashboard.getBoolean("Wrist/InAbsoluteMode", false);
    if (inAbsoluteMode) {
      setAbsoluteEncoderMode();
    } else {
       setRelativeEncoderMode();
    }
    */
    dampenSpeed = SmartDashboard.getBoolean("Wrist/DampenSpeed", dampenSpeed);
    
    if(dampenSpeed) {
      speedDampener = 0.5;
    } else {
      speedDampener = 1.0;
    }
    
    checkForLimitSwitch();   
    
    //SmartDashboard.putBoolean("Wrist/InAbsoluteMode", inAbsoluteMode);
    SmartDashboard.putBoolean("Wrist_DampenSpeed", dampenSpeed);  
    SmartDashboard.putNumber("Wrist_Wrist Relative Position", getPosition());
    SmartDashboard.putNumber("Wrist_Wrist Angle", getWristAngleInDegrees());
    SmartDashboard.putNumber("Wrist_Wrist Velocity", mWristMotor.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Wrist_Wrist Output Voltage", mWristMotor.getMotorOutputPercent());
    SmartDashboard.putBoolean("Wrist_Wrist Limit Switch", getLimitSwitch());
    
    //SmartDashboard.putBoolean("Wrist/AllowManualWristControl", isManualControl);
  }

  public void setManualControl(boolean control) {
    isManualControl = control;
  }
  
  public boolean getManualControl() {
    return isManualControl;
  }

  public double getForwardExtension() {
    return mForwardExtension;
  }
}
