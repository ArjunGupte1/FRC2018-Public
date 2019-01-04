package missdaisy.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.commands.PincerClose;

public class Intake extends DaisySubsystem {
  
  public static Intake intakeInstance;
  public static Solenoid mPincer;
  
  private VictorSPX mLeftIntake, mRightIntake;
  private DigitalInput mCubeLimitSwitch;
  private Solenoid mStatusLight;
  
  public static Intake getInstance() {
    if(intakeInstance == null) {
      intakeInstance = new Intake();
    }
    return intakeInstance;
  }
  
  public Intake(){
    mLeftIntake = new VictorSPX(Constants.CAN.INTAKE_VICTORSPX_PORT_LEFT);
    mLeftIntake.setNeutralMode(NeutralMode.Brake);
    mLeftIntake.setInverted(false);
    mRightIntake = new VictorSPX(Constants.CAN.INTAKE_VICTORSPX_PORT_RIGHT);
    mRightIntake.setNeutralMode(NeutralMode.Brake);
    mRightIntake.setInverted(true);
    
    mPincer = new Solenoid(Constants.Solenoids.INTAKE);
    mStatusLight = new Solenoid(Constants.Solenoids.STATUS_LIGHT);
    
    mCubeLimitSwitch = new DigitalInput(Constants.Intake.INTAKE_LIMIT_SWITCH);
    
  }
  
  public void setSpeed(double speed) {
    mLeftIntake.set(ControlMode.PercentOutput, 0.75 * speed);
    mRightIntake.set(ControlMode.PercentOutput, 0.75 * speed);
  }
  
  public boolean hasCube() {
    return mCubeLimitSwitch.get();
  }
  
  public void setLight(boolean value) {
    mStatusLight.set(value);
  }
  
  public void openPincer() {
    mPincer.set(false);
  }
  
  public void closePincer() {
    mPincer.set(true);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new PincerClose());
    
  }
  
  public void logToDashboard() {
    SmartDashboard.putNumber("Intake Speed", mLeftIntake.getMotorOutputPercent());
    SmartDashboard.putBoolean("Pincer Solenoid Closed", mPincer.get());
    SmartDashboard.putBoolean("Pincer Solenoid Open", !mPincer.get());
    SmartDashboard.putBoolean("Intake HasCube", mCubeLimitSwitch.get());
  }

}
