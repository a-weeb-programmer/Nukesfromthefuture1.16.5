package net.nukesfromthefuture.util;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIConfig implements IModPlugin {
    public JEIConfig(){

    }
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("nff", "jei_compatibility");
    }

}
