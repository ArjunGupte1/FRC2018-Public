package missdaisy.autonomous;

import missdaisy.commands.autos.CubeScoreForward;
import missdaisy.commands.autos.CubeSwitchScore;

/**
 * Position the arm into a backwards scoring state and shoot out the cube.
 *
 * @author AJN341
 */
public class ScoreForward extends State {

  private CubeScoreForward mScoreForward;

  public ScoreForward() {
    super("ScoreBackwards");
    mScoreForward = new CubeScoreForward();
  }

  @Override
  public void enter() {
    mScoreForward.start();
  }

  @Override
  public void running() {
    
  }

  @Override
  public void exit() {
    
  }

  @Override
  public boolean isDone() {
    return mScoreForward.isCompleted();
  }
}
