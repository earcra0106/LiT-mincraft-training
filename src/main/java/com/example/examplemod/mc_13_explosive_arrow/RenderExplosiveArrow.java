package com.example.examplemod.mc_13_explosive_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderExplosiveArrow extends ArrowRenderer<EntityExplosiveArrow> {
    private static final ResourceLocation TEXTURE_EXPLOSIVE_ARROW =
            new ResourceLocation("examplemod", "textures/entity/explosive_arrow.png");

    public RenderExplosiveArrow(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityExplosiveArrow enitity) {
        return TEXTURE_EXPLOSIVE_ARROW;
    }
}
