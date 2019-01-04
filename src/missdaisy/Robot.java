
package missdaisy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.autonomous.StateMachine;
import missdaisy.commands.DriveArcadeMode;
import missdaisy.commands.ExampleCommand;
import missdaisy.fileio.PropertyReader;
import missdaisy.fileio.PropertySet;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Drive;
import missdaisy.subsystems.ExampleSubsystem;
import missdaisy.subsystems.Intake;
import missdaisy.subsystems.Wrist;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

    private String kPropertiesFilePath = "/home/lvuser/trajectories/";
    
    boolean mLastDriverAButtonState = false;
    boolean mLastDriverBButtonState = false;
    boolean mLastDriverXButtonState = false;
    boolean mLastDriverYButtonState = false;
    boolean mLastOperatorAButtonState = false;
    boolean mLastOperatorBButtonState = false;
    boolean mLastOperatorXButtonState = false;
    boolean mLastOperatorYButtonState = false;

    
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI mOI;
	public static Drive mDrive = Drive.getInstance();
	public static Arm mArm = Arm.getInstance();
	public static Wrist mWrist = Wrist.getInstance();
	public static Intake mIntake = Intake.getInstance();
	private PropertyReader mPropertyReader;
	private StateMachine mStateMachine;
	private String[] autonList, startingPositions;
	private int fileIndex = 0;
	public static PropertySet mPropertySet = PropertySet.getInstance();
	private String mStartingPlacement = "";
	private String mGameMessage = "";
	public static boolean waitBeforeAuton = false;
	private int autonCount = 0;
	

	Command autonomousCommand;
	Class<?> autonClass;
	Constructor<?> autonConstructor;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
	    mOI = OI.getInstance();
	    chooser.addDefault("Default Auto", new ExampleCommand());
	    mDrive.resetGyro();
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		// Start sending the camera image to the dashboard
		new Thread(() -> {
		  UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		  camera.setResolution(320,240);
		  camera.setFPS(15);
		}).start();
		//CameraServer.getInstance().startAutomaticCapture();
		mPropertyReader = new PropertyReader();
		mPropertyReader.parseFile(kPropertiesFilePath);
		autonList = Constants.Autonomous.AUTONS;
		startingPositions = Constants.Autonomous.STARTINGPOSITIONS;
		//chooseAutoMode();
		
		logToDashboard();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
	  
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		
		listenForAutoSelectionAButton();
		chooseAutoModeFromInfo();
		
		SmartDashboard.putNumber("DPad:", mOI.mOperatorController.getDPadAxis());
        
		logToDashboard();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
//		autonomousCommand = chooser.getSelected();
	  chooseAutoModeFromInfo();
	  autonCount = 0;
	  //runAutoCommand();
	  
	  /*
	  try {
        autonomousCommand = (Command) autonConstructor.newInstance();
        System.out.println("Made Command!");
      } catch (InstantiationException | IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      };
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
		*/
		
//		mStateMachine = new StateMachine(
//	        new AutonomousParser().parseStates(true));
		
		logToDashboard();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if(autonCount <= 3) {
		  chooseAutoModeFromInfo();
		} else if(autonCount == 4){
	      runAutoCommand();
		} else {
	      Scheduler.getInstance().run();
//		  mStateMachine.run();
	      logToDashboard();
		}
		autonCount++;
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		Scheduler.getInstance().add(new DriveArcadeMode());
		
		
		SmartDashboard.putNumber("Set Shoulder Angle", 10.0);
		SmartDashboard.putNumber("Set Wrist Angle", 15.0);

		logToDashboard();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
	  if (autonomousCommand != null)
        autonomousCommand.cancel();
	  
	  Scheduler.getInstance().run();
	  mOI.run();
	  logToDashboard();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	public void logToDashboard() {
	  mDrive.logToDashboard();
	  mArm.logToDashboard();
	  mWrist.logToDashboard();
	  mIntake.logToDashboard();
	  SmartDashboard.putNumber("Autonomous File", SmartDashboard.getNumber("Autonomous File", 0));
	  if(autonConstructor != null) {
	    SmartDashboard.putString("Auton Class", autonConstructor.toString());
	  }
	  waitBeforeAuton = SmartDashboard.getBoolean("Wait Before Auton", false);
	  SmartDashboard.putBoolean("Wait Before Auton", waitBeforeAuton);
	  //SmartDashboard.putBoolean("isSwitchLeft", gameInfo[0]);
      //SmartDashboard.putBoolean("isScaleLeft", gameInfo[1]);
	}
	/*
	private void chooseAutoMode() {
	   
	    String mAutoMode;
	    //fileIndex = (int) SmartDashboard.getNumber("Autonomous File", 0);
	    
	    if (autonList != null && fileIndex < autonList.length){
	      mPropertyReader.parseAutonomousFile(autonList[fileIndex]);
	      mAutoMode = autonList[fileIndex];
	    } else {
	      mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/DriveStraight.txt");
	      mAutoMode = "/home/lvuser/trajectories/DriveStraight.txt";
	    }
	    
	    SmartDashboard.putString("Autonomous Mode:", mAutoMode);
	  }
	
	private void listenForAutoChanges() {
	    if (mOI.mDriverController.getAButton() && !mLastDriverAButtonState) {
	      fileIndex++;
	      if (autonList != null && fileIndex >= autonList.length){
	        fileIndex = 0;
	      }
	      chooseAutoMode();
	    }
	    mLastDriverAButtonState = mOI.mDriverController.getAButton(); 
	  }

	*/
	/*
	public void chooseAutoMode() {
	  mStartingPlacement = SmartDashboard.getString("Starting Placement", "Center");
	  
	  gameInfo = AutoChooser.ChooseAuto();
	  
	  gameInfo[0] = SmartDashboard.getBoolean("isSwitchLeft", false);
	  gameInfo[1] = SmartDashboard.getBoolean("isScaleLeft", false);
	  
	  boolean mIsSwitchLeft = gameInfo[0];
	  boolean mIsScaleLeft = gameInfo[1];
	  	        
	  if( mStartingPlacement.equalsIgnoreCase("Left")) { //Checks for Left Placement
	      if(mIsScaleLeft) {
	         if(mIsSwitchLeft) {
	            mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/LeftScaleLeftScale.txt");
                
              } else {
                //mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/.txt");
              }
	      }
	      else if(!mIsScaleLeft) {
	          if(mIsSwitchLeft) {
                mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/LeftScaleRightScale.txt");
              } else {
                //mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/.txt");
              }
	      }
	      else {
	          mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/DoNothing.txt");
	      }
	  }
	  else if( mStartingPlacement.equalsIgnoreCase("Right") ) { //Checks for Right Placement
	      if(mIsScaleLeft) {
	          if(mIsSwitchLeft) {
                //mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/.txt");
              } else {
                mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/RightScaleLeftScale.txt");
	          }
	      }
	      else if(!mIsScaleLeft) {
	          if(mIsSwitchLeft) {
                //mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/.txt");
              } else {
                mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/RightScaleRightScale.txt");
              }
	      }
	      else {
	          mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/DoNothing.txt");
	      }
	  }
	  else if( mStartingPlacement.equalsIgnoreCase("Center") ) { //Checks for Center Placement
	      if(mIsSwitchLeft) {
	          mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/CenterLeftSwitch.txt");
	      }
	      else if(!mIsSwitchLeft) {
	          mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/CenterRightSwitch.txt");
	      }
	      else {
	          mPropertyReader.parseAutonomousFile("/home/lvuser/trajectories/DoNothing.txt");
	      }
	  }
	  else { //Checks for incorrect StartingPlacement input
	      //Invalid Starting Placement
	  }
	}
	*/
	
	private void chooseAutoModeFromInfo() {
	  int side = 0;
	  
	  if(DriverStation.getInstance().getAlliance().equals(Alliance.Red)) //Checks side
	    side = 2;
	  
	  if( !mGameMessage.equals(DriverStation.getInstance().getGameSpecificMessage()) 
          || mStartingPlacement != startingPositions[fileIndex])
        {
	        String className = "";
	        mStartingPlacement = startingPositions[fileIndex];
            mGameMessage = DriverStation.getInstance().getGameSpecificMessage();
            
            if(startingPositions[fileIndex].equals("CrossAutoLine")) {
              className = this.getClass().getPackage().getName() + ".commands.autos.CrossAutoLineAuton";
            } else if(startingPositions[fileIndex].equals("DoNothing")){
              className = this.getClass().getPackage().getName() +
                  ".commands.autos.DoNothingAuton";
            } else if(mGameMessage.length() > 0) {
              className = this.getClass().getPackage().getName() +
                  ".commands.autos." + mStartingPlacement + "Auton" +
                  mGameMessage.charAt(side) + mGameMessage.charAt(1);
            } else {
              className = this.getClass().getPackage().getName() + ".commands.autos.CrossAutoLineAuton";
            }
            
            try {
              autonClass = Class.forName(className);
              autonConstructor = autonClass.getConstructors()[0];
            } catch (ClassNotFoundException e) {
              e.printStackTrace();
            } catch (SecurityException e) {
              e.printStackTrace();
            }
            
            //System.out.println(autonConstructor.getName());
            System.out.println(mStartingPlacement);
        }
	}
	
	private void listenForAutoSelectionAButton() {
      if (mOI.mDriverController.getAButton() && !mLastDriverAButtonState) {
        fileIndex++;
        if (startingPositions != null && fileIndex >= startingPositions.length){
          fileIndex = 0;
        }
      }
      SmartDashboard.putString("Starting Placement", startingPositions[fileIndex]);
      SmartDashboard.putNumber("File Index", fileIndex);
      SmartDashboard.putNumber("Starting Positions", startingPositions.length);
      SmartDashboard.putString("Game Message", mGameMessage);
      mLastDriverAButtonState = mOI.mDriverController.getAButton(); 
    }
	
	private void runAutoCommand() {
	  try {
        autonomousCommand = (Command) autonConstructor.newInstance();
        System.out.println("Made Command!");
      } catch (InstantiationException | IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      };
        /*
         * String autoSelected = SmartDashboard.getString("Auto Selector",
         * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
         * = new MyAutoCommand(); break; case "Default Auto": default:
         * autonomousCommand = new ExampleCommand(); break; }
         */

        // schedule the autonomous command (example)
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }
        
        //if(autonomousCommand.isCompleted())
	}
	
}