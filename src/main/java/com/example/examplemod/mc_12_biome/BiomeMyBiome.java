package com.example.examplemod.mc_12_biome;

import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeMyBiome {
    public static Biome makeMyBiome() {
        BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder =
                new BiomeGenerationSettings.Builder();

        // 花の設定
        PlacedFeature flowerSettings =
                BiomeUtil.makeFlowerSpawnSetting(
                        new BiomeUtil.FlowerData[]{
                                // 花の種類と確率
                                new BiomeUtil.FlowerData(Blocks.POPPY, 1),
                                new BiomeUtil.FlowerData(Blocks.DANDELION, 2),
                        },
                        // 花の数
                        "my_biome_flower", 10
                );
        biomeGenerationSettingsBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, flowerSettings);

        // 鉱石の設定
        PlacedFeature oreSettings =
                BiomeUtil.makeOreSpawnSetting(
                        "my_biome_ore",
                        // 鉱石の種類、最大生成数、生成確率、最小Y座標、最大Y座標
                        new BiomeUtil.OreData(Blocks.DIAMOND_BLOCK, 12, 256, 50, 63)
                );
        biomeGenerationSettingsBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, oreSettings);

        // 木の設定
        PlacedFeature myBiomeTree =
                BiomeUtil.makeTreeSetting(
                        "my_biome_tree",
                        Blocks.OAK_LOG, // 木の幹
                        Blocks.OAK_LEAVES, // 葉
                        Blocks.OAK_SAPLING // 苗木
                );
        biomeGenerationSettingsBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, myBiomeTree);

        // モブの設定
        MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();

        // クリーチャー
        mobSpawnSettingsBuilder.addSpawn(
                MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 1, 100, 100)
        );

        // モンスター
        mobSpawnSettingsBuilder.addSpawn(
                MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 100, 100)
        );

        // バイオーム生成
        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .biomeCategory(Biome.BiomeCategory.ICY)
                .temperature(0.0f)
                .downfall(1.0f)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0xffffff)
                        .waterFogColor(0x000000)
                        .fogColor(0xc0d8ff)
                        .skyColor(0x55ffff)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY))
                        .build())
                .mobSpawnSettings(mobSpawnSettingsBuilder.build())
                .generationSettings(biomeGenerationSettingsBuilder.build())
                .build();
    }
}
