package missdaisy.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.OI;
import missdaisy.Robot;

/**
 *
 */
public class ShowStatusLight extends Command {
    
    private int count;
    public ShowStatusLight() {
        // Use requires() here to declare subsystem dependencies
      super("ShowStatusLight");
      count = -1;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      Robot.mIntake.setLight(true);
      count = 0;
      OI.getInstance().mOperatorController.rumble(0.75, 0.75);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      if (Robot.mIntake.hasCube()){
        count = 0;
      } else {
        count++;
      }
      SmartDashboard.putNumber("ShowStatusLightCount", count);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return count > 6; //Robot.mIntake.hasCube();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
      Robot.mIntake.setLight(false);
      OI.getInstance().mOperatorController.rumble(0.0,  0.0);
    }

    public boolean isInterruptable(){
      return false;
    }
}
