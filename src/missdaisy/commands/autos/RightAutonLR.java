package missdaisy.commands.autos;

import edu.wpi.first.wpilibj.command.CommandGroup;
import missdaisy.Constants;
import missdaisy.commands.DrivePath;
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

public class RightAutonLR extends CommandGroup {

  public  RightAutonLR() {
    
    
    addParallel(new DrivePath("Paths/Right/ToRightScaleFront", -1.0));
    addSequential(new CubeScoreBackward(1500));
    addSequential(new ScoreCube(0.4));
    addSequential(new CubeLoadPosition());
    
    //Attempt Second Cube Straight
    addParallel(new PincerOpen());
    addSequential(new DrivePath("Paths/Scale/RightScaleFrontToRightSwitchBack", 1.0));
    addSequential(new GrabCube(-1.0, 0.25));
    addParallel(new DrivePath("Paths/Switch/RightSwitchBackToRightScaleFront", -1.0));
    addSequential(new CubeScoreBackward(100));
    addSequential(new ScoreCube(0.4));
    addSequential(new SetArmPose(Constants.ArmPose.LOAD_CUBE, Constants.WristPose.STOW_ANGLE_AT_LOAD));
    //addParallel(new DrivePath("Paths/Scale/RightScaleFrontToRightSwitchSide", 1.0));
    //addSequential(new StowPosition());
    /*
    addParallel(new TurnAngle(90.0));
    addSequential(new StowPosition());
    addSequential(new DrivePath("Paths/Right/ShortDash", -1.0));
    addSequential(new TurnAngle(-90.0));
    addParallel(new DrivePath("Paths/Right/ShortDash", -1.0));
    */
    /*
    addSequential(new DrivePath("Paths/Scale/RightScaleFrontToRightSwitchBack", 1.0));
    addSequential(new GrabCube(-1.0, 0.25));
    addParallel(new DrivePath("Paths/Switch/RightSwitchBackToRightScaleFront", -1.0));
    //addSequential(new CubeScoreBackward(100));
    */
    
    
    
    System.out.println("Running The Auton!");
    
    //addSequential(new GoToWristAngle(Constants.WristPose.STOW_ANGLE_AT_LOAD, true));
  }
}