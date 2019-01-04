package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.DrivePath;
import missdaisy.commands.GoToWristAngle;
import missdaisy.commands.GrabCube;
import missdaisy.commands.PincerOpen;
import missdaisy.commands.ScoreCube;
import missdaisy.commands.SetArmPose;
import missdaisy.commands.groups.CubeLoadPosition;

/**
 * Creates a CommandGroup that will move the arm & wrist into cube loading position 
 * 
 * @author AJN
 */

public class LeftAutonRL extends CommandGroup {

  public  LeftAutonRL() {
    
    
    addParallel(new DrivePath("Paths/Left/ToLeftScaleFront", -1.0));
    addSequential(new CubeScoreBackward(2000));
    addSequential(new ScoreCube(0.4));
    addSequential(new CubeLoadPosition());
    
    //Attempt Second Cube Straight
    addParallel(new PincerOpen());
    addSequential(new DrivePath("Paths/Scale/LeftScaleFrontToLeftSwitchBack", 1.0));
    addSequential(new GrabCube(-1.0, 0.25));
    addParallel(new DrivePath("Paths/Switch/LeftSwitchBackToLeftScaleFront", -1.0));
    addSequential(new CubeScoreBackward(100));
    addSequential(new ScoreCube(0.4));
    addSequential(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.STOW_ANGLE_AT_LOAD));
    /*
    addParallel(new DrivePath("Paths/Scale/LeftScaleFrontToLeftSwitchSide", 1.0));
    addSequential(new StowPosition());
    */
    /*
    addParallel(new TurnAngle(-90.0));
    addSequential(new StowPosition());
    addSequential(new DrivePath("Paths/Left/LongDash", -1.0));
    addSequential(new TurnAngle(-180.0));
    addSequential(new DrivePath("Paths/Left/LongDash", -1.0));
    */
    /*
    addSequential(new DrivePath("Paths/Scale/LeftScaleFrontToLeftSwitchBack", 1.0));
    addSequential(new GrabCube(-1.0, 0.25));
    addParallel(new DrivePath("Paths/Switch/LeftSwitchBackToLeftScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
    */
     
    
    System.out.println("Running The Auton!");
    
    addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}