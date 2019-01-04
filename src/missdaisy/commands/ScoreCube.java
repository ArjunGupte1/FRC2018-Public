package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Robot;

/**
 *
 */
public class ScoreCube extends Command {
  
    private int mCounter = 0;
    private double speed = 0.0;
    public ScoreCube() {
        // Use requires() here to declare subsystem dependencies
      super("RunIntake");
      speed = 1.0;
      requires(Robot.mIntake);
    }
    
    public ScoreCube(double sp) {
      // Use requires() here to declare subsystem dependencies
    super("RunIntake");
    speed = sp;
    requires(Robot.mIntake);
  }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      Robot.mIntake.openPincer();
      mCounter = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      System.out.println("SCORING CUBE");
      Robot.mIntake.setSpeed(speed);
      mCounter++;
      System.out.println("Running ScoreCube");
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
      return mCounter > 40;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      Robot.mIntake.setSpeed(0.0);
      Robot.mIntake.closePincer();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      Robot.mIntake.setSpeed(0.0);
    }
}
