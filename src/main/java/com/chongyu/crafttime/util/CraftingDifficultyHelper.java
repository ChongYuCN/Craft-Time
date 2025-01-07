package com.chongyu.crafttime.util;

import com.chongyu.crafttime.config.CommonConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class CraftingDifficultyHelper {

	public static float getCraftingDifficultyFromMatrix(AbstractRecipeScreenHandler<?> handler, boolean is_craft_table, Screen screen) {
		ArrayList<Slot> slots = new ArrayList<Slot>();
		int index = is_craft_table? 10 : 5;
		for (int i = 1; i < index; i++) {
			slots.add(handler.getSlot(i));
		}

		Text text = screen.getTitle();
		float p = 1.0f;
		if(text.equals(Text.translatable("container.copper_crafting"))){
			p -= 0.1f;//铜工作台减10%
		}
		if(text.equals(Text.translatable("container.iron_crafting"))){
			p -= 0.2f;//铁工作台减20%
		}
		if(text.equals(Text.translatable("container.diamond_crafting"))){
			p -= 0.3f;//钻石工作台减30%
		}
		if(text.equals(Text.translatable("container.netherite_crafting"))){
			p -= 0.9f;//下界合金工作台减60%
		}

		return Math.max(getCraftingDifficultyFromMatrix(slots) * p, 15F);
	}


	public static float getCraftingDifficultyFromMatrix(ArrayList<Slot> slots) {
		float basic_difficulty = 5F;
		float item_difficulty = 0F;
		for (Slot s : slots) {
			Item item = s.getStack().getItem();
			if (item == Items.AIR)
				continue;
			item_difficulty += getDifficulty(item);
		}
		return basic_difficulty + item_difficulty;
	}
	
	public static ArrayList<Item> getItemFromMatrix(AbstractRecipeScreenHandler<?> handler, boolean is_craft_table) {
		ArrayList<Item> items = new ArrayList<Item>();
		int index = is_craft_table ? 10 : 5;
		for (int i = 1; i < index; i++) {
			items.add(handler.getSlot(i).getStack().getItem());
		}
		return items;
	}

	public static float getDifficulty(Item item) {
		String name =  Registries.ITEM.getId(item).toString();
		if(item==Items.IRON_BLOCK){
			return 360;
		}
		if(item==Items.IRON_INGOT){
			return 40F;
		}
		if(item==Items.GOLD_INGOT){
			return 30F;
		}
		if(item==Items.DIAMOND){
			return 60F;
		}
		if(name.contains("aliveandwell:ingot_wujin")){
			return 60F;
		}
		if(item==Items.NETHERITE_INGOT){
			return 100F;
		}
		if(name.contains("aliveandwell:ingot_mithril") ){
			return 120F;
		}
		if(name.contains("aliveandwell:ingot_adamantium")){
			return 150F;
		}

		if(CommonConfig.craftItemTimeMap.containsKey(name)){
			return CommonConfig.craftItemTimeMap.get(name);
		}

		return 20F;
	}
}
