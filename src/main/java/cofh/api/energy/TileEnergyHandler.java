package cofh.api.energy;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

/**
 * Reference implementation of {@link IEnergyHandler}. Use/extend this or implement your own.
 *
 * @author King Lemming
 *
 */
public class TileEnergyHandler extends TileEntity implements IEnergyHandler {

	protected EnergyStorage storage = new EnergyStorage(32000);

	public TileEnergyHandler(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {

		super.read(state, nbt);
		storage.readFromNBT(nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {

		super.write(nbt);
		storage.writeToNBT(nbt);
		return nbt;
	}

	/* IEnergyConnection */
	@Override
	public boolean canConnectEnergy(Direction from) {

		return true;
	}

	/* IEnergyReceiver */
	@Override
	public int receiveEnergy(Direction from, int maxReceive, boolean simulate) {

		return storage.receiveEnergy(maxReceive, simulate);
	}

	/* IEnergyProvider */
	@Override
	public int extractEnergy(Direction from, int maxExtract, boolean simulate) {

		return storage.extractEnergy(maxExtract, simulate);
	}

	/* IEnergyReceiver and IEnergyProvider */
	@Override
	public int getEnergyStored(Direction from) {

		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(Direction from) {

		return storage.getMaxEnergyStored();
	}

}