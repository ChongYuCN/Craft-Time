package com.chongyu.crafttime.register;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

public class UseBlock {
    public static void init(){

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//            ItemStack itemStack = player.getStackInHand(hand);
//            Item item = itemStack.getItem();
//            String name = Registry.ITEM.getId(item).toString();//获取物品注册名字========Test==========

            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();

            if (!player.world.isClient){
                if(!player.isCreative()){
//                    player.sendMessage(Text.of(name));
                    //无法使用原版工作台
                    if(block == Blocks.CRAFTING_TABLE){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }

                    //禁用“你要去的生物群落”工作台
                    if(Registry.BLOCK.getId(block).toString().contains("byg:") && Registry.BLOCK.getId(block).toString().contains("crafting_table")){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }

                    //合成蓝图
                    if(Registry.BLOCK.getId(block).toString().contains("create:") && Registry.BLOCK.getId(block).toString().contains("crafting_blueprint")){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
