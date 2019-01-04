package missdaisy.triggers;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.OI;
import missdaisy.subsystems.Arm;

public class ManualArmControl extends Button {
    
  public boolean get() {
    return Arm.getInstance().getManualControl();
  }   

}
