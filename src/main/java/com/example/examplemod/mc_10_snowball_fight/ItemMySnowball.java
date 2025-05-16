package com.example.examplemod.mc_10_snowball_fight;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.Level;

import java.util.Random;

public class ItemMySnowball extends SnowballItem {
    private static final int DIFFUSION_AMOUNT = 5;
    private static final int DIFFUSION_RADIUS = 5;

    public ItemMySnowball() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack itemStack = playerIn.getItemInHand(handIn);

        if (!playerIn.isCreative()) {
            itemStack.setCount(itemStack.getCount() - 1);
        }

        Random random = new Random();

        /* @Nullable Player pPlayer
         *  サウンドを再生するプレイヤーを指定します。
         *  nullの場合、全てのプレイヤーにサウンドが再生されます。
         *
         *  double pX, double pY, double pZ
         *  サウンドを再生する位置（X, Y, Z座標）を指定します。
         *
         *  SoundEvent pSoundEvent
         *  再生するサウンドイベントを指定します（例: SoundEvents.SNOWBALL_THROW）。
         *
         *  SoundSource pCategory
         *  サウンドのカテゴリを指定します（例: SoundSource.NEUTRAL, SoundSource.PLAYER）。
         *  float pVolume
         *
         *  サウンドの音量を指定します（例: 1.0Fで通常の音量）。
         *  float pPitch
         *
         *  サウンドのピッチ（音の高さ）を指定します（例: 1.0Fで通常の高さ）。 */
        level.playSound(
                null,
                playerIn.getX(),
                playerIn.getY(),
                playerIn.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (random.nextFloat() * 0.4F + 0.8F)
        );

//        if (!level.isClientSide()) {
//            EntityMySnowball entity = new EntityMySnowball(level, playerIn);
//            entity.shootFromRotation(
//                    playerIn,
//                    playerIn.xRotO,
//                    playerIn.yRotO,
//                    0.0F,
//                    1.5F,
//                    1.0F
//            );
//            level.addFreshEntity(entity);
//        }

        if (!level.isClientSide()) {
            for (int i = 0; i < DIFFUSION_AMOUNT; i++) {
                EntityMySnowball entity = new EntityMySnowball(level, playerIn);
                entity.shootFromRotation(
                        playerIn,
                        playerIn.xRotO,
                        playerIn.yRotO + ((float) i - (DIFFUSION_AMOUNT / 2)) * DIFFUSION_RADIUS,
                        0.0F,
                        1.5F,
                        1.0F
                );
                level.addFreshEntity(entity);
            }
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
