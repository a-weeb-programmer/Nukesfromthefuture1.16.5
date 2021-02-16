package net.nukesfromthefuture.interfaces;

public interface IConsumer {
void setPower(long i);
	
	long getPower();
	long getPowerScaled(long i);
	boolean hasPower();
	int getPowerRequired();
	long getMaxPower();
}
