package net.nukesfromthefuture.interfaces;


import net.minecraft.item.Item;
import net.nukesfromthefuture.main.FluidHandler;

/**I'm sorry, I know your only like 3 days old, but time to YEET you into da trash if I actually got off my chair*/
@Deprecated
public interface IFluidTankItem {
    /**gets the type of the fluid container
     * @return the fluid type that the fluid container item is bound to*/
    @Deprecated
    FluidHandler.FluidType getType();
    /**The value of the amount of fluid each container has in it
     * @return the amount of fluid a container has*/
    @Deprecated
    int fillAmount();
    /**The tank when full*/
    @Deprecated
    Item getFullContainer();
    /**What the container is like when no fluid*/
    @Deprecated
    Item getEmptyContainer();
}
