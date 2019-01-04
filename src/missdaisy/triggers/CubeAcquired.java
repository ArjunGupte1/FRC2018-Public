package missdaisy.triggers;

import edu.wpi.first.wpilibj.buttons.Button;
import missdaisy.OI;
import missdaisy.subsystems.Intake;

public class CubeAcquired extends Button {
  private Intake mIntake = Intake.getInstance();
  
  public boolean get() {
    return mIntake.hasCube();
  }   

}
