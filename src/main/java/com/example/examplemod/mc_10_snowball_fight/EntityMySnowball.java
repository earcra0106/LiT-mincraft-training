package com.example.examplemod.mc_10_snowball_fight;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityMySnowball extends Snowball {
    // Entityに当たったときのダメージ量
    private static final float DAMAGE = 2.0F;

    public EntityMySnowball(EntityType<? extends Snowball> entityTypeIn, Level level) {
        super(entityTypeIn, level);
    }

    public EntityMySnowball(Level level, LivingEntity thowerIn) {
        super(level, thowerIn);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        // ブロックに当たった なら
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) result;
            switch (blockHitResult.getDirection()) {
                case EAST, WEST, NORTH, SOUTH -> {
                    Block block = level.getBlockState(blockHitResult.getBlockPos()).getBlock();
                    if (block == Blocks.SNOW_BLOCK) {
                        level.setBlockAndUpdate(blockHitResult.getBlockPos(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        // エンティティに当たった なら
        } else if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityRayTraceResult = (EntityHitResult) result;
            entityRayTraceResult.getEntity().hurt(DamageSource.thrown(this, getOwner()), DAMAGE);
        }

        // 着弾すると爆発する
        level.explode(this, getX(), getY(), getZ(), 1.0F, false, Explosion.BlockInteraction.NONE);
    }
}
