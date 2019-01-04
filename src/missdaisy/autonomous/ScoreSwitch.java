package missdaisy.autonomous;

import missdaisy.commands.autos.CubeSwitchScore;
import missdaisy.commands.groups.CubeScoreBackwardPosition;

/**
 * Position the arm into a backwards scoring state and shoot out the cube.
 *
 * @author AJN341
 */
public class ScoreSwitch extends State {

  private CubeSwitchScore mSwitchSpit;

  public ScoreSwitch() {
    super("ScoreBackwards");
    mSwitchSpit = new CubeSwitchScore();
  }

  @Override
  public void enter() {
    mSwitchSpit.start();
  }

  @Override
  public void running() {
    
  }

  @Override
  public void exit() {
    
  }

  @Override
  public boolean isDone() {
    return mSwitchSpit.isCompleted();
  }
}
