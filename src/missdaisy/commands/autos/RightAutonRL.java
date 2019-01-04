package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.commands.DrivePath;
import missdaisy.commands.ScoreCube;
import missdaisy.commands.groups.CubeLoadPosition;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class RightAutonRL extends CommandGroup {

  public  RightAutonRL() {
    
    addParallel(new DrivePath("Paths/Right/ToLeftScaleFront", -1.0));
    addSequential(new CubeScoreBackward(1000));
    addSequential(new ScoreCube());
    addSequential(new CubeLoadPosition());
    
    /*
    addParallel(new DrivePath("Paths/Right/RightSwitchSide", -1.0));
    addSequential(new CubeScoreBackward(1000));
    addSequential(new ScoreCube());
    addSequential(new CubeLoadPosition());
    //addSequential(new DrivePath("Paths/Scale/LeftScaleFrontToLeftSwitchBack", 1.0));
    //addSequential(new GrabCube(-1.0, 0.25));
    //addParallel(new DrivePath("Paths/Switch/LeftSwitchBackToLeftScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
    */
    
    /*
    addSequential(new DrivePath("Paths/Right/ToRightSwitchSide", -1.0));
    addSequential(new TurnAngle(90.0));
    addParallel(new SetWristAngle(Constants.WristPose.SWITCH_SPIT));
    addParallel(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.SWITCH_SPIT));
    addSequential(new DrivePath("Paths/Right/ShortDash", 1.0));
    addSequential(new ScoreCube());
    */
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}