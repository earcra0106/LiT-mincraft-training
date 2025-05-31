package com.example.examplemod.mc_15_tobisuke;

import com.example.examplemod.ExampleMod;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class EntityTobisuke extends TamableAnimal {
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID
            = SynchedEntityData.defineId(EntityTobisuke.class, EntityDataSerializers.FLOAT);

    public EntityTobisuke(EntityType<? extends TamableAnimal> entityTypeIn, Level level) {
        super(entityTypeIn, level);
        this.setTame(false); // 初期状態ではテイムされていない
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new FloatGoal(this)); // 水中で浮くための目標を追加
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this)); // 座る目標を追加
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4f)); // ターゲットに飛びかかる目標を追加
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0d, 10.0f, 2.0f, false)); // 飼い主を追いかける目標を追加
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0d)); // 繁殖する目標を追加
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0d)); // 水を避けながらランダムに歩く目標を追加
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0f)); // プレイヤーを見つめる目標を追加
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this)); // ランダムに周囲を見る目標を追加

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this)); // 飼い主が攻撃されたときに敵を攻撃する目標を追加
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this)); // 飼い主が攻撃されたときに敵を追跡する目標を追加
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers()); // 他の敵から攻撃されたときに敵を攻撃する目標を追加
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3d) // 移動速度
                .add(Attributes.MAX_HEALTH, 8.0d) // 最大体力
                .add(Attributes.ATTACK_DAMAGE, 2.0d);
    }

    @Override
    protected void customServerAiStep() {
        this.entityData.set(DATA_HEALTH_ID, this.getHealth());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HEALTH_ID, getHealth());
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
        EntityTobisuke entityTobisuke = ExampleMod.ENTITY_TOBISUKE.create(level);
        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            entityTobisuke.setOwnerUUID(uuid);
            entityTobisuke.setTame(true); // 子供もテイムされている
        }
        return entityTobisuke;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.APPLE;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTame() && tickCount > 2400; // テイムされていない場合、2400ティック（2分）後に削除
    }

    @Override
    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        if (!isFood(playerIn.getMainHandItem())) { // 食べ物を持っていない場合
            return InteractionResult.PASS;
        }

        if (this.level.isClientSide) {
            return InteractionResult.PASS;
        }

        if (isTame()) {
            this.setOrderedToSit(!this.isOrderedToSit());
        } else {
            if (!playerIn.isCreative()) {
                playerIn.getMainHandItem().setCount(playerIn.getMainHandItem().getCount() - 1); // 食べ物を消費
            }
            this.setTame(true);
            this.setOrderedToSit(false);
            this.setHealth(20.0f); // テイム時に体力を全回復
            this.setOwnerUUID(playerIn.getUUID()); // プレイヤーを飼い主として設定
            this.spawnTamingParticles(true); // テイム成功のパーティクルを表示
            this.level.broadcastEntityEvent(this, (byte) 7); // テイム成功のサウンドを再生
        }
        return InteractionResult.SUCCESS;
    }
}
