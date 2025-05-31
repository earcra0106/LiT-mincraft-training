package com.example.examplemod.mc_14_bull_fighting;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderBull extends MobRenderer<EntityBull, CowModel<EntityBull>> {
    private static final ResourceLocation COW_LOCATION =
            new ResourceLocation("textures/entity/cow/cow.png");

    public RenderBull(EntityRendererProvider.Context context) {
        super(context, new CowModel(context.bakeLayer(ModelLayers.COW)), 0.7f); // 0.7fは影半径
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBull entity) {
        return COW_LOCATION; // 牛のテクスチャを使用
    }
}
