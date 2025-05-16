package com.example.examplemod.mc_07_soundblock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import static com.example.examplemod.ExampleMod.MODID;

public class BlockSound extends Block {
    private static final IntegerProperty SOUND = IntegerProperty.create("sound", 0, 2);

    private static final SoundEvent soundEvent[] = {
            new SoundEvent(new ResourceLocation(MODID, "sound1")),
            new SoundEvent(new ResourceLocation(MODID, "sound2")),
            new SoundEvent(new ResourceLocation(MODID, "sound3")),
    };

    public BlockSound() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(10f));
        this.registerDefaultState(this.getStateDefinition().any().setValue(SOUND, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SOUND);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            return;
        }

        int soundId = state.getValue(SOUND);
        level.playSound(player, pos, soundEvent[soundId], SoundSource.BLOCKS, 1.0F, 1.0F);
        String soundNumber = String.valueOf(soundId + 1);
        player.sendMessage(new TextComponent(soundNumber + "番目の曲を再生中です"), player.getUUID());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        int soundId = state.getValue(SOUND);

        if (player.getItemInHand(handIn).getItem() == Items.STICK) {
            soundId = soundId > 0 ? soundId - 1 : 2;
        } else {
            soundId = soundId < 2 ? soundId + 1 : 0;
        }

        level.setBlockAndUpdate(pos, state.setValue(SOUND, soundId));

        if (!level.isClientSide) {
            String soundNumber = String.valueOf(soundId + 1);
            player.sendMessage(new TextComponent(soundNumber + "番目の曲に変更しました"), player.getUUID());
        }

        return InteractionResult.SUCCESS;
    }
}
