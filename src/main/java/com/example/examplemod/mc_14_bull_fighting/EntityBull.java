package com.example.examplemod.mc_14_bull_fighting;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EntityBull extends Animal {
    public EntityBull(EntityType<? extends Animal> entityTypeIn, Level level) {
        super(entityTypeIn, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // 牛の攻撃目標を追加
        // 1.0dは移動速度、trueは攻撃時に走るかどうか
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0d, true));
        // プレイヤーを見つめる目標を追加
        // 6.0fは見つめる距離
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0f));
        // 水を避けてランダムに移動する目標を追加
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0d));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        // プレイヤーを攻撃する目標を追加
        // NearestAttackableTargetGoalは、最も近い攻撃可能なターゲットを選択する
        // Player.classはプレイヤーをターゲット
        // trueはプレイヤーを攻撃する際に、プレイヤーが近くにいる場合のみ攻撃する
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2f) // 移動速度
                .add(Attributes.FOLLOW_RANGE, 35.0d) // フォロー範囲
                .add(Attributes.MAX_HEALTH, 20.0d) // 最大体力
                .add(Attributes.ARMOR, 2.0d) // 防御力
                .add(Attributes.ATTACK_DAMAGE, 2.0d); // 攻撃力
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getTarget() == null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f); // ターゲットがいないときの移動速度
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.6f); // ターゲットがいるときの移動速度
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    } // 子どもを産まないようにする

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    } // 牛の鳴き声

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.COW_HURT;
    } // 牛がダメージを受けたときの音

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    } // 牛が死んだときの音

    @Override
    protected  void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F); // 牛が歩いたときの音
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F; // 音量
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return this.isBaby() ? pSize.height * 0.95F : 13.0F; // 目の高さ
    }
}
