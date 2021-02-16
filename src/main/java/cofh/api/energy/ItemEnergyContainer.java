package cofh.api.energy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Reference implementation of {@link IEnergyContainerItem}. Use/extend this or implement your own.
 * 
 * @author King Lemming
 * 
 */
public class ItemEnergyContainer extends Item implements IEnergyContainerItem {

	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public ItemEnergyContainer(Item.Properties prop) {
		super(prop);
	}

	public ItemEnergyContainer(Item.Properties prop, int capacity) {

		this(prop, capacity, capacity, capacity);
	}

	public ItemEnergyContainer(Item.Properties prop, int capacity, int maxTransfer) {

		this(prop, capacity, maxTransfer, maxTransfer);
	}

	public ItemEnergyContainer(Item.Properties prop, int capacity, int maxReceive, int maxExtract) {
		super(prop);

		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public ItemEnergyContainer setCapacity(int capacity) {

		this.capacity = capacity;
		return this;
	}

	public void setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
	}

	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

		if (container.getTag() == null) {
			container.setTag(new CompoundNBT());
		}
		int energy = container.getTag().getInt("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
			container.getTag().putInt("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

		if (container.getTag() == null || !container.getTag().contains("Energy")) {
			return 0;
		}
		int energy = container.getTag().getInt("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			container.getTag().putInt("Energy", energy);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {

		if (container.getTag() == null || !container.getTag().contains("Energy")) {
			return 0;
		}
		return container.getTag().getInt("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {

		return capacity;
	}

}