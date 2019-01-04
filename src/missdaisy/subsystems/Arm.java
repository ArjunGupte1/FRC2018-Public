package missdaisy.subsystems;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.commands.HoldArmPos;
import missdaisy.utilities.DaisyMath;


/**
 * Base class for a subsystem.
 *
 * A subsystem is a mechanism or cohesive function of a robot. Examples could be: Drive, Arm,
 * Shooter, etc.
 *
 * @author Jagger, Adam341
 */
public class Arm extends DaisySubsystem {

  private static Arm instance = null;
  //private static DataLogger dataLogger = null;
  private TalonSRX mShoulderMotor;
  private VictorSPX mShoulderSlave;
  private double p, i, d;
  private DigitalInput shoulderLowerLimitSwitchLeft, shoulderLowerLimitSwitchRight, shoulderUpperLimitSwitch;
  private boolean sensorIsAbsolute = true, isManualControl = false;
  private int sensorWaitCount = 0;
  private double speedDampener;
  private boolean dampenSpeed, isDone;


  /**
   * Gets the instance of the shooter. Used in order to never have more than one shooter object,
   * ever.
   *
   * @return The one and only instance of the shooter
   */
  public static Arm getInstance() {
    if (instance == null) {
      instance = new Arm();
    }
    return instance;
  }

  private Arm() {
    
    // Define the motor and controller properties for the shoulder motor
    mShoulderMotor = new TalonSRX(Constants.CAN.SHOULDER_TALONSRX_ID);
    
    mShoulderMotor.configAllowableClosedloopError(0, 80, 10);
    mShoulderMotor.configMotionAcceleration(100, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.configMotionCruiseVelocity(225, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.setInverted(false);
    //mShoulderMotor.setSensorPhase(true);
    mShoulderMotor.setSensorPhase(false);
    mShoulderMotor.setNeutralMode(NeutralMode.Brake);
    //For Moving Position
    mShoulderMotor.config_kP(0, 2.0, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.config_kI(0, 0.0, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.config_kD(0, 100.0, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.config_kF(0, 10.0, Constants.Properties.K_TIMEOUT_MS);
    //For Holding Position
    mShoulderMotor.config_kP(1, 1.0, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.config_kI(1, 0.0, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.config_kD(1, 1.0, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.config_kF(1, 0.0, Constants.Properties.K_TIMEOUT_MS);
     
    // Set the other shoulder motors to follow the main talon srx motor
    mShoulderSlave = new VictorSPX(Constants.CAN.SHOULDER_SLAVE_VICTORSPX_ID);
    mShoulderSlave.setInverted(false);
    mShoulderSlave.setNeutralMode(NeutralMode.Brake);
    mShoulderSlave.follow(mShoulderMotor);

    mShoulderMotor.configForwardSoftLimitEnable(true, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.configReverseSoftLimitEnable(true, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.configForwardSoftLimitThreshold(Constants.Arm.ARM_FORWARD_SOFT_LIMIT, Constants.Properties.K_TIMEOUT_MS);
    mShoulderMotor.configReverseSoftLimitThreshold(Constants.Arm.ARM_REVERSE_SOFT_LIMIT, Constants.Properties.K_TIMEOUT_MS);
   
    
    // This is the hardstop limit switch
    shoulderLowerLimitSwitchLeft = new DigitalInput(Constants.Arm.ARM_LOWER_LMIT_SWITCH_LEFT);
    shoulderLowerLimitSwitchRight = new DigitalInput(Constants.Arm.ARM_LOWER_LMIT_SWITCH_RIGHT);
    shoulderUpperLimitSwitch = new DigitalInput(Constants.Arm.ARM_UPPER_LIMIT_SWITCH);
    
    speedDampener = 1.0;
    dampenSpeed = false;
    isDone = false;
    
    SmartDashboard.putBoolean("Arm/DampenSpeed", dampenSpeed);
    
    //mShoulderMotor.setSensorPhase(true);
    
    // Switch to relative mode for the sensor, might have to do this later to give the sensor time to get an accurate absolute reading first
    //setAbsoluteEncoderMode();
    setRelativeEncoderMode();
    setPositionOfSensor(Constants.Arm.ARM_LOWEST_POSITION);
    
    SmartDashboard.putBoolean("Arm/InAbsoluteMode", false);
  }
  
  /*
  public void setAbsoluteEncoderMode() {
    // Switches the talon encoder to absolute mode
    sensorIsAbsolute = true;
    mShoulderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
    }
  */
  
  
  public void setRelativeEncoderMode() {
    mShoulderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    sensorIsAbsolute = false;
  }
  /*
  public void resetRelativeAngleFromAbsolute() {
    int relOffset = mShoulderMotor.getSelectedSensorPosition(0);
    if (sensorIsAbsolute){
      relOffset = (int) DaisyMath.boundEncoderNeg2048To2048(
          relOffset - Constants.Arm.SHOULDER_HORIZONTAL_ABSOLUTE_POS);
    }
      
    SmartDashboard.putNumber("Arm/relOffset", relOffset);
    
    setRelativeEncoderMode();
    
    mShoulderMotor.setSelectedSensorPosition(relOffset, 0, 10);
  }
  */
  
  public synchronized double getPosition() {
    return mShoulderMotor.getSelectedSensorPosition(0);
  }
  
  public double getShoulderAngleInRadians() {
    return DaisyMath.boundAngleNegPiToPiRadians(
        (mShoulderMotor.getSelectedSensorPosition(0) * Constants.Arm.SHOULDER_RADIANS_PER_TICK));
  }

  public double getShoulderAngleInDegrees() {
    return DaisyMath.boundAngleNeg180to180Degrees(
        (mShoulderMotor.getSelectedSensorPosition(0) * Constants.Arm.SHOULDER_DEGREES_PER_TICK));
  }
  
  
  public void setAngle(double angle) {
    
    double sensorTickValue = angle / Constants.Arm.SHOULDER_DEGREES_PER_TICK;

    //sensorTickValue = 
    //if(getLowerLowerLimitSwitch() && angle < mShoulderMotor.getSelectedSensorPosition(0)) {
      //mShoulderMotor.set(ControlMode.PercentOutput, 0.0);
    //} else {
      //mShoulderMotor.set(ControlMode.Position, sensorTickValue);
    mShoulderMotor.set(ControlMode.MotionMagic, sensorTickValue);
    //}
    SmartDashboard.putNumber("Arm_Shoulder Tick Setpoint", sensorTickValue);
    SmartDashboard.putNumber("Arm_Shoulder Angle Setpoint", angle);
    SmartDashboard.putNumber("Arm_Arm PID Error", mShoulderMotor.getClosedLoopError(0));

    //Sets position based on relative distance, not absolute location
     
  }
  
  public void holdPosition() {
    mShoulderMotor.set(ControlMode.Position, getPosition());
  }
  
  public void useFirstPIDS() {
    mShoulderMotor.selectProfileSlot(0, 0);
  }
  
  public void useSecondPIDS() {
    mShoulderMotor.selectProfileSlot(1, 0);
  }

  public void setSpeed(double speed) {
    /*
    if((getLowerLimitSwitch() )//&& speed < 0.0)
        || (getUpperLimitSwitch() && speed > 0.0)) {
      stop();
    } else {
      
    } */
    if (getLowerLimitSwitch() && speed < 0.0 ){
      speed = 0.0;
    }else if (getUpperLimitSwitch() && speed > 0.0){
      speed = 0.0;
    }
    
      mShoulderMotor.set(ControlMode.PercentOutput, speedDampener * speed);

      SmartDashboard.putNumber("ArmSpdCmd", speed);
    //}
  }
  
  public void stop() {
    //mShoulderMotor.set(ControlMode.PercentOutput, 0.0);
  }
  
  public void setManualControl(boolean control) {
    isManualControl = control;
  }
  
  public boolean getManualControl() {
    return isManualControl;
  }
  
  public boolean getLowerLimitSwitch() {
    return !shoulderLowerLimitSwitchLeft.get() || !shoulderLowerLimitSwitchRight.get();
  }
  
  public boolean resetArmByLimitSwitch(){
    return !shoulderLowerLimitSwitchLeft.get() || !shoulderLowerLimitSwitchRight.get();
  }
  
  public boolean getUpperLimitSwitch() {
    return !shoulderUpperLimitSwitch.get();
  }

  public void limitSpeed() {
    mShoulderMotor.configPeakOutputForward(.5, 10);
    mShoulderMotor.configPeakOutputReverse(-.5, 10);
  }

  public void delimitSpeed() {
    mShoulderMotor.configPeakOutputForward(1.0, 10);
    mShoulderMotor.configPeakOutputReverse(-1.0, 10);
  }
  
  public boolean getDone() {
    return isDone;
  }
  
  public void setDone(boolean d) {
    isDone = d;
  }
  
  public void resetEncoder() {
  
    //Use following to reset position of Shoulder motor
    mShoulderMotor.configSetParameter(ParamEnum.eClearPositionOnIdx, 1, 0x00, 0x00, 10);
    mShoulderMotor.configSetParameter(ParamEnum.eClearPositionOnIdx, 0, 0x00, 0x00, 10);
    mShoulderMotor.setSelectedSensorPosition(0, 0, 10);
    
  }
  
  public void setPositionOfSensorToZero() {
    mShoulderMotor.setSelectedSensorPosition(0, 0, 10);
  }
  
  public void setPositionOfSensor(int pos) {
    mShoulderMotor.setSelectedSensorPosition(pos, 0, 10);
  }
  
  public void checkForLowerLimitSwitch() {
    if(getLowerLimitSwitch()) {
      if(mShoulderMotor.getSelectedSensorVelocity(0) < 0.0) {
        //stop();
      }
      if (resetArmByLimitSwitch()){
        this.setPositionOfSensor(Constants.Arm.ARM_LOWEST_POSITION);
      }
    }
  }
  
  public void checkForUpperLimitSwitch() {
    if(getUpperLimitSwitch()) {
      if(mShoulderMotor.getSelectedSensorVelocity(0) > 0.0) {
        //stop();
      }
      //this.setPositionOfSensor(Constants.Arm.ARM_VERTICAL_POSITION);
    }
  }
  
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new HoldArmPos());
  }
  
  public void logToDashboard() {
    /*
    sensorWaitCount++;
    
    if (sensorWaitCount == 10) {
      resetRelativeAngleFromAbsolute();
      System.out.println("Auto reset to relative from absolute");
    } else if(sensorWaitCount < 10) {
      if(!sensorIsAbsolute)
        setAbsoluteEncoderMode();
    } else {
      boolean inAbsoluteMode = SmartDashboard.getBoolean("Arm/InAbsoluteMode", false);
      if (inAbsoluteMode) {
        if(!sensorIsAbsolute) {
          setAbsoluteEncoderMode();
        }
      } else {
        if(sensorIsAbsolute) {
          setRelativeEncoderMode();
        }
      }
    }
    */
    
    checkForLowerLimitSwitch();
    checkForUpperLimitSwitch();
    
    
    dampenSpeed = SmartDashboard.getBoolean("Arm/DampenSpeed", dampenSpeed);
    
    if(dampenSpeed) {
      speedDampener = 0.5;
    } else {
      speedDampener = 1.0;
    }
    
    SmartDashboard.putBoolean("Arm_DampenSpeed", dampenSpeed);  
    
    SmartDashboard.putBoolean("Arm_ManualControl", isManualControl);
    SmartDashboard.putNumber("Arm_Shoulder Relative Position", mShoulderMotor.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Arm_ShoulderAngle", getShoulderAngleInDegrees());
    SmartDashboard.putNumber("Arm_Shoulder Velocity", mShoulderMotor.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Arm_Shoulder Output Voltage", mShoulderMotor.getMotorOutputPercent());
    SmartDashboard.putBoolean("Arm_Arm Lower Limit Switch", getLowerLimitSwitch());
    SmartDashboard.putBoolean("Arm_Arm Upper Limit Switch", getUpperLimitSwitch());
    SmartDashboard.putBoolean("Arm_isDone", isDone);
  }
  
  public void setBrake()
  {
    mShoulderMotor.setNeutralMode(NeutralMode.Brake);
  }
  public void setCoast()
  {
    mShoulderMotor.setNeutralMode(NeutralMode.Coast);
  }

  public double getEncoderRate() {
    double rate = mShoulderMotor.getMotorOutputVoltage();
    if (Math.abs(rate) < 0.05) {
      rate = 0.0;
    }
    return rate;
  }
}
