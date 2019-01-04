package missdaisy.triggers;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.OI;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Drive;

public class RunRightWinchTrigger extends Button {
    
  public boolean get() {
    return false;//(Drive.getInstance().getLeftPopperState() && OI.getInstance().mDriveRightTrigger);
  }   

}
