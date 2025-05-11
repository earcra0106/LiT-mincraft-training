package com.example.examplemod.javaskip;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerDeathEventHandler {
    // 「骨」
    private static final Item NOT_DIE_ITEM = Items.BONE;

    // イベントハンドラ
    @SubscribeEvent
    // LivingHurtEventはあらゆるエンティティのダメージで発火する
    public void onPlayerHurt(LivingHurtEvent event) {
        // level = マイクラのワールドのこと
        // マイクラの世界構造:
        // level
        //     render : 表示 dataの変化を検知して反映する clientの世界
        //     data   : あらゆるデータ                  remoteの世界

        // LivingHurtEventはrenderとdataどちらも呼ばれる
        // ダメージを受けたら...
        // render: dataのダメージ量を参照してパーティクルを表示する
        // data: エンティティのHPを更新する

        // このイベントが発火された世界がrenderである なら
        if (event.getEntityLiving().level.isClientSide) {
            // イベント終わり
            return;
        }

        // ダメージを受けたエンティティがプレイヤー でない なら
        if (!(event.getEntityLiving() instanceof Player)) {
            // イベント終わり
            return;
        }

        // (プレイヤーの体力 - 攻撃によって与えられるダメージ が0以下)でない
        // == 攻撃でプレイヤーが死なない なら
        if (!(event.getEntityLiving().getHealth() - event.getAmount() <= 0)) {
            // イベント終わり
            return;
        }

        // プレイヤーのインベントリを取得する
        Inventory inventory = ((Player) event.getEntityLiving()).getInventory();
        // インベントリのスロット数 だけ繰り返す
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            // インベントリのi番目のアイテムを取得
            ItemStack stack = inventory.getItem(i);

            // アイテムがない なら
            if (stack == null) {
                // 次のアイテムを参照
                continue;
            }

            // アイテムが「骨」でない なら
            if (stack.getItem() != NOT_DIE_ITEM) {
                // 次のアイテムを参照
                continue;
            }

            // アイテムが一つだけ なら
            if (stack.getCount() == 1) {
                // ?アイテムを無しにする?
                inventory.removeItemNoUpdate(i);
            } else {
                // ?アイテムのスタック数を1減らす?
                stack.setCount(stack.getCount() - 1);
            }
            // ?イベントを無効化する(つまりダメージをなかったことにする)?
            event.setCanceled(true);
            return;
        }
    }
}
