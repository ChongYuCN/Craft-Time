package com.chongyu.crafttime;

import com.chongyu.crafttime.config.Config;
import com.chongyu.crafttime.register.BlockEnityRegistry;
import com.chongyu.crafttime.register.BlockInit;
import com.chongyu.crafttime.register.UseBlock;
import com.chongyu.crafttime.sound.SoundEventRegistry;
import com.chongyu.crafttime.worklevel.CraftingIngredients;
import com.chongyu.crafttime.worklevel.FurnaceIngredients;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class CraftTime implements ModInitializer {
    public static final String MOD_ID = "crafttime";
    public static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

    public static Config config;
    @Override
    public void onInitialize() {
        config = new Config();
        config.load();

        BlockInit.registerBlocks();
        BlockInit.registerBlockItems();
        BlockInit.registerFuels();

        BlockEnityRegistry.init();
        CraftingIngredients.init();
        FurnaceIngredients.initFuel();
        FurnaceIngredients.initItem();

        UseBlock.init();
        Registry.register(Registry.SOUND_EVENT, SoundEventRegistry.finishSoundID, SoundEventRegistry.finishSound);
    }
}
