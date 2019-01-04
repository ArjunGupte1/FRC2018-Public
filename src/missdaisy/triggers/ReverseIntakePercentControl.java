package missdaisy.triggers;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import missdaisy.Constants;
import missdaisy.OI;
import missdaisy.Robot;
import missdaisy.subsystems.Arm;
import missdaisy.subsystems.Drive;

public class ReverseIntakePercentControl extends Button {
    
  
  /*
   * I Know we don't want to use try...catch, but it's only temporary
   * I want it to at least maybe work (I haven't had a chance to test it)
   * But if it doesn't, just comment out the try...catch and uncomment the return false
   */
  public boolean get() {
    try {
      return (Robot.mOI.isOperatorLeftTrigger());
    } catch(NullPointerException e) {
      return false;
    }
    //return false
  }   

}
