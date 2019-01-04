package missdaisy.triggers;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Robot;

public class WristUpTrigger extends Button {
    
  public boolean get() {
    return SmartDashboard.getBoolean("AllowManualWristControl", false) == false && Robot.mWrist.getWristUpTrigger();
    //return Robot.mWrist.getManualControl();
  }   

}
