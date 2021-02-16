package net.nukesfromthefuture.interfaces;

import net.nukesfromthefuture.containers.FluidTank;
import net.nukesfromthefuture.main.FluidHandler;

import java.util.List;


public interface IFluidContainer {
	//Args: fill, what the fill should be set to; index, index for array if there are multiple tanks
		public void setFillstate(int fill, int index);

		//Args: fill: what the fill should be set to; type, what type the tank in question has
		void setFluidFill(int fill, FluidHandler.FluidType type);
		
		//Args: type, what the type should be set to; index, index for array if there are multiple tanks
		public void setType(FluidHandler.FluidType type, int index);
		
		public List<FluidTank> getTanks();
		
		//Args: type, what type the tank in question has
		int getFluidFill(FluidHandler.FluidType type);
}
