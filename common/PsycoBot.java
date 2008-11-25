package common;

public class PsycoBot extends ComputerPlayer
{
	public boolean animate(float dTime, float currentTime)
	{
		float dx = RANDOM.nextFloat()*2-1, dy = RANDOM.nextFloat()*2-1;
		accelerate(PLAYER_ACCELERATION*dx*dTime, PLAYER_ACCELERATION*dy*dTime);
		
		return super.animate(dTime, currentTime);
	}
}
