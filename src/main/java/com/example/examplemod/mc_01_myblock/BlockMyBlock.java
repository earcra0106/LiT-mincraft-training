package com.example.examplemod.mc_01_myblock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockMyBlock extends Block{
    public BlockMyBlock() {
        super(Block.Properties.of(Material.DIRT));
    }

    @Override
    public void attack(BlockState pState, Level level, BlockPos pPos, Player pPlayer) {
        super.attack(pState, level, pPos, pPlayer);

        LivingEntity entityMob = new Pig(EntityType.PIG, level);

        entityMob.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
        if (!level.isClientSide) {
            ((ServerLevel) level).tryAddFreshEntityWithPassengers(entityMob);
        }
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        super.stepOn(pLevel, pPos, pState, pEntity);

        if (!pLevel.isClientSide) {
            float explpsionRadius = 3.0f;

            pLevel.explode(null, pPos.getX(), pPos.getY(), pPos.getZ(), explpsionRadius, Explosion.BlockInteraction.BREAK);
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);

        if (pLevel.isClientSide) return;

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = pPos.offset(x, y, z);
                    if (y == -1) {
                        pLevel.setBlockAndUpdate(pos, Blocks.DIAMOND_BLOCK.defaultBlockState());
                    } else {
                        if (x == 0 && y == 0 && z == 0) continue;
                        pLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }
}
