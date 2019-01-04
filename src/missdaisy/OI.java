package missdaisy;


import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.commands.AdjustWrist;
import missdaisy.commands.DeployClimbers;
import missdaisy.commands.EngageLeftPTO;
import missdaisy.commands.HoldClimber;
import missdaisy.commands.MoveArm;
import missdaisy.commands.MoveWrist;
import missdaisy.commands.ReEngageClimber;
import missdaisy.commands.ResetArmEncoder;
import missdaisy.commands.ResetDriveEncoders;
import missdaisy.commands.ResetGyro;
import missdaisy.commands.ResetWristEncoder;
import missdaisy.commands.ReverseIntake;
import missdaisy.commands.ReverseIntakeTrigger;
import missdaisy.commands.RunIntake;
import missdaisy.commands.RunIntakeTrigger;
import missdaisy.commands.ShowStatusLight;
import missdaisy.commands.Unwinch;
import missdaisy.commands.groups.CubeLoadPosition;
import missdaisy.commands.groups.CubeScoreBackwardPosition;
import missdaisy.commands.groups.CubeScoreForwardPosition;
import missdaisy.commands.groups.SwitchSpit;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Drive;
import missdaisy.subsystems.Intake;
import missdaisy.subsystems.Wrist;
import missdaisy.triggers.CubeAcquired;
import missdaisy.triggers.ManualArmControl;
import missdaisy.triggers.ManualWristControl;
import missdaisy.triggers.ReverseIntakePercentControl;
import missdaisy.triggers.RunIntakePercentControl;
import missdaisy.triggers.RunLeftWinchTrigger;
import missdaisy.triggers.RunRightWinchTrigger;
import missdaisy.triggers.WristDownTrigger;
import missdaisy.triggers.WristNeutralTrigger;
import missdaisy.triggers.WristUpTrigger;
import missdaisy.utilities.XboxController;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
    private static OI oiInstance;
    private Drive mDrive = Drive.getInstance();
    private Arm mArm = Arm.getInstance();
    private Intake mIntake = Intake.getInstance();
    private Wrist mWrist = Wrist.getInstance();
    XboxController mDriverController;
    public XboxController mOperatorController;
    private double targetAngle;
    
    public double mDriveLeftStickX = 0.0, mDriveLeftStickY = 0.0;
    public double mDriveRightStickX = 0.0, mDriveRightStickY = 0.0;
    public boolean mDriveLeftTrigger = false, mDriveRightTrigger = false;
    
    public double mOperatorLeftStickX = 0.0, mOperatorLeftStickY = 0.0;
    public double mOperatorRightStickX = 0.0, mOperatorRightStickY = 0.0;
    public double mOperatorLeftTrigger = 0.0, mOperatorRightTrigger = 0.0;
    
    // Declare our triggers
    public Button mCubeTrigger = new CubeAcquired();
    public Button mManualWristTrigger = new ManualWristControl();
    public Button mManualArmTrigger = new ManualArmControl();
    public Button mWristUpTrigger = new WristUpTrigger();
    public Button mWristNeutralTrigger = new WristNeutralTrigger();
    public Button mWristDownTrigger = new WristDownTrigger();
    public Button mRunLeftWinch = new RunLeftWinchTrigger();
    public Button mRunRightWinch = new RunRightWinchTrigger();
    public Button mReverseIntakePercentControl = new ReverseIntakePercentControl();
    public Button mRunIntakePercentControl = new RunIntakePercentControl();
    
    public static OI getInstance() {
      if(oiInstance == null) {
        oiInstance = new OI();
      }
      return oiInstance;
    }
    
    public OI(){
      
      // Create the Driver & Operator Joysticks
      mDriverController = new XboxController(Constants.XboxController.DRIVER_PORT);
      mOperatorController = new XboxController(Constants.XboxController.OPERATOR_PORT);
      
      // Commands triggered by the Driver
      mDriverController.BumperLeft.whileHeld(new EngageLeftPTO());
      //mDriverController.BumperRight.whileHeld(new EngageLeftPopper());
      mDriverController.ButtonStart.whenPressed(new ResetDriveEncoders());
      mDriverController.ButtonBack.whenPressed(new ResetGyro());
      mDriverController.ButtonY.whenPressed(new DeployClimbers());
      mDriverController.ButtonA.whileHeld(new Unwinch());
      mDriverController.ButtonB.whileHeld(new HoldClimber());
      mDriverController.ButtonX.whenPressed(new ReEngageClimber());
      
      // Commands triggered by the Operator
      
      /* 
      // Controls to help tune the base arm controls
      mOperatorController.ButtonA.whileHeld(new GoToWristAngle(0.0, true));
      mOperatorController.ButtonB.whileHeld(new GoToShoulderAngle(0.0, true));
      mOperatorController.ButtonX.whileHeld(new GoToShoulderAngle(0.0, false));
      mOperatorController.ButtonY.whileHeld(new GoToWristAngle(0.0, false));
      */
      
      mOperatorController.ButtonA.whenPressed(new CubeLoadPosition());
      mOperatorController.ButtonB.whenPressed(new CubeScoreForwardPosition());
      //mOperatorController.ButtonA.whileHeld(new GoToWristAngle(Constants.WristPose.LOAD_CUBE, true));
      //mOperatorController.ButtonB.whileHeld(new GoToWristAngle(Constants.WristPose.FORWARD_SCORING, true));
      mOperatorController.ButtonX.whenPressed(new CubeScoreBackwardPosition());
      //mOperatorController.ButtonY.whileHeld(new MaintainWristExtension());
      //mOperatorController.ButtonY.whenPressed(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
      mOperatorController.ButtonY.whenPressed(new SwitchSpit());
      mOperatorController.ButtonStart.whileHeld(new ResetArmEncoder());
      mOperatorController.ButtonBack.whenPressed(new ResetWristEncoder());
      mOperatorController.BumperLeft.whileHeld(new RunIntake()); //Outtakes
      mOperatorController.BumperRight.whileHeld(new ReverseIntake()); //Intakes

      
      // Define the commands based on robot triggers
      mManualWristTrigger.whileHeld(new MoveWrist());   // When the smartdashboard boolean is checked, allows manual control, otherwise will stow the wrist
      mCubeTrigger.whenPressed(new ShowStatusLight());
      mManualArmTrigger.whileHeld(new MoveArm());
      mWristUpTrigger.whenPressed(new AdjustWrist(10.0));
      mWristNeutralTrigger.whenPressed(new AdjustWrist(0.0));
      mWristDownTrigger.whenPressed(new AdjustWrist(-10.0));
      //mRunLeftWinch.whileHeld(new RunLeftWinch());
      //mRunRightWinch.whileHeld(new RunRightWinch());
      mReverseIntakePercentControl.whileHeld(new ReverseIntakeTrigger());
      mRunIntakePercentControl.whileHeld(new RunIntakeTrigger());
    }
    
    public void run() {      
      mDriveLeftStickX = mDriverController.getDeadbandedLeftXAxis(Constants.XboxController.DEAD_BAND);
      mDriveLeftStickY = -1.0 * mDriverController.getDeadbandedLeftYAxis(Constants.XboxController.DEAD_BAND);
      mDriveRightStickX = 1.0 * mDriverController.getDeadbandedRightXAxis(Constants.XboxController.DEAD_BAND);
      mDriveRightStickY = -1.0 * mDriverController.getDeadbandedRightYAxis(Constants.XboxController.DEAD_BAND);
      mDriveLeftTrigger = mDriverController.getLeftTrigger();
      mDriveRightTrigger = mDriverController.getRightTrigger();
      
      mOperatorLeftStickX = mOperatorController.getDeadbandedLeftXAxis(Constants.XboxController.DEAD_BAND);
      mOperatorLeftStickY = -1.0 * mOperatorController.getDeadbandedLeftYAxis(Constants.XboxController.DEAD_BAND);
      mOperatorRightStickX = mOperatorController.getDeadbandedRightXAxis(Constants.XboxController.DEAD_BAND);
      mOperatorRightStickY = -1.0 * mOperatorController.getDeadbandedRightYAxis(Constants.XboxController.DEAD_BAND);
      mOperatorLeftTrigger = mOperatorController.getRawAxis(Constants.XboxController.LEFT_TRIGGER);
      mOperatorRightTrigger = mOperatorController.getRawAxis(Constants.XboxController.RIGHT_TRIGGER);
      
      SmartDashboard.putNumber("DPad", mOperatorController.getDPadAxis());
      
      if((Math.abs(mDriveLeftStickY) > 0 || Math.abs(mDriveRightStickX) > 0)
          && (mDrive.getLeftPTOState() || mDrive.getLeftPopperState())) {
        mDrive.disengagePoppers();
      }
      
      if(Math.abs(mOperatorLeftStickY) > 0.0) {
        mArm.setManualControl(true);
      } else {
        mArm.setManualControl(false);
      }
      
      //if(SmartDashboard.getBoolean("AllowManualWristControl", false))
      {
        if(Math.abs(mOperatorRightStickY) > 0.0) {
          mWrist.setManualControl(true);
        } else {
          mWrist.setManualControl(false);
        }
      }
      //else
      /*{
        double xVal = mOperatorController.getDeadbandedRightXAxis(0.75);
        double yVal = -1.0 * mOperatorController.getDeadbandedRightYAxis(0.75);
        
         mWrist.setWristUpTrigger(yVal > 0.0);
         mWrist.setWristDownTrigger(yVal < 0.0);
         mWrist.setWristNeutralTrigger(xVal > 0.0);
      }*/
      
      SmartDashboard.putBoolean("Showing Status Light", mCubeTrigger.get());
      SmartDashboard.putBoolean("Wrist/AllowManualWristControl", mManualWristTrigger.get());

      
      //mArm.setSpeed(mOperatorLeftStickY);
      //mWrist.setSpeed(0.5 * mOperatorRightStickY);
    }
    
    public boolean isOperatorLeftTrigger() {
        return mOperatorLeftTrigger > 0.1;
    }

    public boolean isOperatorRightTrigger() {
      return mOperatorRightTrigger > 0.1;
    }
}
