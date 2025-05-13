package com.example.examplemod.mc_06_rainbowblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;

public class BlockRainbow extends Block {

    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 6);

    public BlockRainbow() {
        super(Block.Properties.of(Material.STONE).strength(10f));
        this.registerDefaultState(this.getStateDefinition().any().setValue(COLOR, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        int color = state.getValue(COLOR);

        color++;
        if (color >= 7) {
            color = 0;
        }

        level.setBlockAndUpdate(pos, state.setValue(COLOR, color));
    }
}
