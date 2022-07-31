package com.spoilers.arcanearrays.blocks.tiles.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.spoilers.arcanearrays.blocks.tiles.ArrayTile;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class ArrayCoreRenderer implements BlockEntityRenderer<ArrayTile> {
    
    public static final Material CIRCLE_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("block/array_circle"));
    //private final BookModel bookModel;
    
    public ArrayCoreRenderer(BlockEntityRendererProvider.Context pContext) {
        
    }
    
    public void render(ArrayTile pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
    }
}
