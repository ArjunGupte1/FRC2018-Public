package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.OI;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;

/**
 *
 */
public class MoveArm extends Command {
  
    private Arm mArm;
    
    public MoveArm() {
      super("MoveArm");
        // Use requires() here to declare subsystem dependencies
      mArm = Arm.getInstance();
      
      requires(Robot.mArm);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      mArm.setCoast();
      
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      mArm.setSpeed(0.5 * OI.getInstance().mOperatorLeftStickY);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      mArm.setBrake();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
