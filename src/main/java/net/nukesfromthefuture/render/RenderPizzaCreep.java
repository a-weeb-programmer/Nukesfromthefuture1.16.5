package net.nukesfromthefuture.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.nukesfromthefuture.entity.EntityPizzaCreeper;
import net.nukesfromthefuture.main.Nukesfromthefuture;

public class RenderPizzaCreep extends MobRenderer<EntityPizzaCreeper, CreeperModel<EntityPizzaCreeper>> {
    public static final ResourceLocation texture = new ResourceLocation(Nukesfromthefuture.mod_id, "textures/entity/pizza_creep.png");
    public RenderPizzaCreep(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new CreeperModel(), 0.6F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityPizzaCreeper entity) {
        return texture;
    }
    public static class RenderFactory implements IRenderFactory<EntityPizzaCreeper>{

        @Override
        public EntityRenderer createRenderFor(EntityRendererManager manager) {
            return new RenderPizzaCreep(manager);
        }
    }
}
