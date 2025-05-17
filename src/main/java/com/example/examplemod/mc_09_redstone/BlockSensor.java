package com.example.examplemod.mc_09_redstone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSensor extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final double RADIUS = 5.0D;

    public BlockSensor() {
        super(Block.Properties.of(Material.STONE).strength(30f));
        this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockState.getValue(POWERED) == true) {
            return 15;
        } else {
            return 0;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        // 0.25秒後にtickメソッドを呼び出すようにスケジュール
        pLevel.scheduleTick(pPos, this, 5);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        super.tick(state, level, pos, rand);

        // 軸に沿った範囲を取得
        AABB aabb = new AABB(
                pos.getX() - RADIUS,
                pos.getY() - RADIUS,
                pos.getZ() - RADIUS,
                pos.getX() + RADIUS,
                pos.getY() + RADIUS,
                pos.getZ() + RADIUS
        );

        boolean isFound = false;

        // 範囲内のエンティティを取得
        List<Entity> entities = level.getEntities(null, aabb);

        // エンティティの中にCreeperがいるか確認
        for (Entity entity : entities) {
            if (entity.getType() == EntityType.CREEPER) {
                isFound = true;
                break;
            }
        }

        // このブロックのstateを更新
        level.setBlockAndUpdate(pos, state.setValue(POWERED, isFound));

        // 0.25秒後にtickメソッドを呼び出すようにスケジュール
        level.scheduleTick(pos, this, 5);
    }
}
