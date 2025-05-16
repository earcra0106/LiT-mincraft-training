package com.example.examplemod.mc_05_mysword;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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

        if (attacker instanceof Player) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0));
        }

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

        BlockPos pos = new BlockPos(target.getX(), target.getY() + ANVIL_HEIGHT, target.getZ());
        target.level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState());

        return super.hurtEnemy(stack, target, attacker);
    }
}
