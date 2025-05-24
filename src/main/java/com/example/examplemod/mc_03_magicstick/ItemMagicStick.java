package com.example.examplemod.mc_03_magicstick;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.time.ZoneId;

public class ItemMagicStick extends Item {
    public ItemMagicStick() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT));
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Level level = pTarget.level;
        BlockPos spawnPos = pTarget.blockPosition();

        // LivingEntity entity;
        // if (pTarget instanceof Villager) {
        //     entity = new Zombie(level);
        // } else {
        //     entity = new Pig(EntityType.PIG, level);
        // }
        //
        // entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        //
        // // 攻撃したエンティティを保存する
        // if (!level.isClientSide && pAttacker instanceof Player) {
        //     // エンティティのレジストリ名を取得
        //     ResourceLocation entityName = pTarget.getType().getRegistryName();
        //
        //     // エンティティのレジストリ名がnullでない場合
        //     if (entityName != null) {
        //         // タグを作成
        //         CompoundTag entityTag = new CompoundTag();
        //         // タグにエンティティのレジストリ名を保存
        //         entityTag.putString("clicked_entity", entityName.toString());
        //         // ItemMagicStickのスタックにタグを保存
        //         pStack.getOrCreateTag().put("clicked_entity", entityTag);
        //
        //         ((Player) pAttacker).displayClientMessage(new TextComponent("エンティティを保存しました: " + pTarget.getType().getRegistryName()), true);
        //     // エンティティのレジストリ名がnullの場合
        //     } else {
        //         ((Player) pAttacker).displayClientMessage(new TextComponent("無効なエンティティです。"), true);
        //     }
        // }
        //
        // if (!pAttacker.level.isClientSide) {
        //     ServerLevel serverLevel = (ServerLevel) pAttacker.level;
        //     serverLevel.tryAddFreshEntityWithPassengers(entity);
        //     serverLevel.removeEntity(pTarget);
        // }

        // 同じ敵に対して10回攻撃したらポーション効果を付与
        if (!level.isClientSide && pAttacker instanceof Player) {
            String uuid = pTarget.getStringUUID();

            CompoundTag targetUuidTag = pStack.getOrCreateTag();

            if (!targetUuidTag.contains(uuid)) {
                targetUuidTag.putInt(uuid, 1);
            } else {
                targetUuidTag.putInt(uuid, targetUuidTag.getInt(uuid) + 1);
            }

            if (targetUuidTag.getInt(uuid) >= 10) {
                pTarget.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 1));
                targetUuidTag.putInt(uuid, 0);
                ((Player) pAttacker).displayClientMessage(new TextComponent("ポーション効果を付与しました。"), true);
            } else {
                ((Player) pAttacker).displayClientMessage(new TextComponent("あと" + (10 - targetUuidTag.getInt(uuid)) + "回攻撃してください。"), true);
            }

            pStack.setTag(targetUuidTag);
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        ItemStack stack = pContext.getItemInHand();

        // クライアント側またはプレイヤーがnullの場合は何もしない
        if (level.isClientSide() || player == null) {
            return InteractionResult.SUCCESS;
        }

        // ItemMagicStickのスタックからタグを取得
        CompoundTag tag = stack.getTag();

        // タグがnullまたは"clicked_entity"が含まれていない場合は何もしない
        if (tag == null || !tag.contains("clicked_entity")) {
            return InteractionResult.SUCCESS;
        }

        // "clicked_entity"からエンティティのレジストリ名を取得
        CompoundTag entityTag = tag.getCompound("clicked_entity");
        // エンティティのレジストリ名を文字列として取得
        String entityName = entityTag.getString("clicked_entity");
        // エンティティのレジストリ名からEntityTypeを取得
        EntityType<?> entityType = EntityType.byString(entityName).orElse(null);

        if (entityType == null) {
            player.displayClientMessage(new TextComponent("無効なエンティティです。"), true);
            return InteractionResult.SUCCESS;
        }

        // エンティティを生成
        LivingEntity newEntity = (LivingEntity) entityType.create(level);

        // クリックした位置からエンティティの召喚位置を計算
        BlockPos clickedPos = pContext.getClickedPos();
        BlockPos clickedFacePos = clickedPos.relative(pContext.getClickedFace());

        // クリックした位置にエンティティを配置できるか確認
        if (!level.isEmptyBlock(clickedFacePos)) {
            player.displayClientMessage(new TextComponent("ここにはエンティティを配置できません。"), true);
            return InteractionResult.SUCCESS;
        }

        // エンティティの位置を設定
        newEntity.setPos(clickedFacePos.getX(), clickedFacePos.getY(), clickedFacePos.getZ());

        // エンティティをサーバーに追加
        ((ServerLevel) level).tryAddFreshEntityWithPassengers(newEntity);

        return InteractionResult.SUCCESS;
    }
}
