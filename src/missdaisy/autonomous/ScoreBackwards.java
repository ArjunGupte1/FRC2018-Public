package missdaisy.autonomous;

import missdaisy.commands.autos.CubeScoreBackward;

/**
 * Position the arm into a backwards scoring state and shoot out the cube.
 *
 * @author AJN341
 */
public class ScoreBackwards extends State {

  private CubeScoreBackward mScoreBackwards;

  public ScoreBackwards() {
    super("ScoreBackwards");
    mScoreBackwards = new CubeScoreBackward(0);
  }

  @Override
  public void enter() {
    mScoreBackwards.start();
  }

  @Override
  public void running() {
    
  }

  @Override
  public void exit() {
    
  }

  @Override
  public boolean isDone() {
    return mScoreBackwards.isCompleted();
  }
}
