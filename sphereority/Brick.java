package sphereority;

public class Brick extends Actor {  
  protected int verticalX;

  public Brick(Stage stage) {
    super(stage);
    setSpriteNames( new String[] {"brick.gif"});
  }

  public int getVerticalX() { return verticalX; }
  public void setVerticalX(int i) {verticalX = i; }
}
