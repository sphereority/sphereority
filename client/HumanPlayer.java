package client;

public class HumanPlayer extends LocalPlayer
{
    protected InputListener inputDevice;
    
    public HumanPlayer(InputListener input)
    {
        inputDevice = input;
    }
    
    public HumanPlayer(InputListener input, byte playerID, String name)
    {
        super(playerID, name);
        inputDevice = input;
    }
    
    public boolean animate(float dTime, float currentTime)
    {
        if (inputDevice != null)
        {
            if (inputDevice.isLeftKeyPressed())  accelerate(-PLAYER_ACCELERATION*dTime, 0);
            if (inputDevice.isRightKeyPressed()) accelerate(PLAYER_ACCELERATION*dTime, 0);
            if (inputDevice.isUpKeyPressed())    accelerate(0, -PLAYER_ACCELERATION*dTime);
            if (inputDevice.isDownKeyPressed())  accelerate(0, PLAYER_ACCELERATION*dTime);
            if (inputDevice.isButtonFiring())    fire();
        }
        
        return super.animate(dTime, currentTime);
    }
}
