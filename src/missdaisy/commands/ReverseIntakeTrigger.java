package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import missdaisy.Robot;

/**
 *
 */
public class ReverseIntakeTrigger extends Command {
    
    private double speed = 0.0;
    public ReverseIntakeTrigger() {
        // Use requires() here to declare subsystem dependencies
      super("RunIntake");
      speed = -1.0;
      requires(Robot.mIntake);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      Robot.mIntake.openPincer();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      speed = Robot.mOI.mOperatorLeftTrigger;
      
      Robot.mIntake.setSpeed(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
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
