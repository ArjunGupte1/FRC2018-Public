package missdaisy.triggers;

import edu.wpi.first.wpilibj.buttons.Button;
import missdaisy.Robot;

public class ManualWristControl extends Button {
    
  public boolean get() {
    //return SmartDashboard.getBoolean("AllowManualWristControl", false);
    return Robot.mWrist.getManualControl();
  }   

}
