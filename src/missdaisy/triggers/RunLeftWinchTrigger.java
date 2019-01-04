package missdaisy.triggers;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.OI;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Drive;

public class RunLeftWinchTrigger extends Button {
    
  public boolean get() {
    return (Drive.getInstance().getLeftPTOState() && OI.getInstance().mDriveLeftTrigger);
  }   

}
