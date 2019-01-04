package missdaisy.autonomous;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.File;

public class AutoChooser {
	
	private static String mGameMessage;
	private static boolean[] info;
	
	public static boolean[] ChooseAuto() {
		mGameMessage = DriverStation.getInstance().getGameSpecificMessage(); //Game Message with sides of switches and scale
		info = new boolean[2];
		
		if( mGameMessage.charAt(0) == 'L' ) { //Checks our side of our switch
			info[0] = true;
		}
		else {
			info[0] = false;
		}
		
		if( mGameMessage.charAt(1) == 'L' ) { //Checks our side of the scale
			info[1] = true;
		}
		else {
			info[1] = false;
		}
		
		return info;
	}
}