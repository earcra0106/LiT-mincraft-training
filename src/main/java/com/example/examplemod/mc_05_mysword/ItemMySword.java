package com.example.examplemod.mc_05_mysword;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ItemMySword extends SwordItem {
    public static final int ANVIL_HEIGHT = 10;

    public ItemMySword() {
        super(Tiers.IRON,
                3,
                -2.4F,
                (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // if (attacker instanceof Player) {
        //     target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0));
        // }
        // return super.hurtEnemy(stack, target, attacker);

        if (!target.level.isClientSide && attacker instanceof Player) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0));

            for (int y = 0; y < 2; y++) {
                for (int x : new int[]{-2, 2}) {
                    for (int z = -1; z < 2; z++) {
                        BlockPos pos = new BlockPos(target.getX() + x, target.getY() + y, target.getZ() + z);
                        target.level.setBlockAndUpdate(pos, Blocks.GLASS.defaultBlockState());
                        pos = new BlockPos(target.getX() + z, target.getY() + y, target.getZ() + x);
                        target.level.setBlockAndUpdate(pos, Blocks.GLASS.defaultBlockState());
                    }
                }
            }

            BlockPos pos = new BlockPos(target.getX() + 1, target.getY() + ANVIL_HEIGHT, target.getZ());
            target.level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState());
            pos = new BlockPos(target.getX() - 1, target.getY() + ANVIL_HEIGHT, target.getZ());
            target.level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState());
            pos = new BlockPos(target.getX(), target.getY() + ANVIL_HEIGHT, target.getZ() - 1);
            target.level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState());
            pos = new BlockPos(target.getX(), target.getY() + ANVIL_HEIGHT, target.getZ() + 1);
            target.level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState());
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ArrowItem itemArrow = new ArrowItem(new Item.Properties());
        if (!level.isClientSide) {
            Arrow arrow = (Arrow) itemArrow.createArrow(level, playerIn.getMainHandItem(), playerIn);

            arrow.shootFromRotation(playerIn, playerIn.xRotO, playerIn.yRotO, 0.0F, 1.5F, 1.0F);

            level.addFreshEntity(arrow);
        }

        return super.use(level, playerIn, handIn);
    }
}
