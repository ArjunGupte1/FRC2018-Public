package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.OI;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;

/**
 *
 */
public class HoldArmPos extends Command {
  
    private OI mOI;
    private Arm mArm;
    
    public HoldArmPos() {
      super("MoveArm");
        // Use requires() here to declare subsystem dependencies
      mOI = OI.getInstance();
      mArm = Arm.getInstance();
      
      requires(Robot.mArm);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      mArm.useSecondPIDS();
      mArm.holdPosition();      
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      mArm.useFirstPIDS();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      end();
    }
    
    public boolean isInterruptible() {
      return true;
    }
}
