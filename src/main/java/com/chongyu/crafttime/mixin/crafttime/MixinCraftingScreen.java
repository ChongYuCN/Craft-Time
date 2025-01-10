package com.chongyu.crafttime.mixin.crafttime;

import com.chongyu.crafttime.ITimeCraftPlayer;
import com.chongyu.crafttime.util.CraftingDifficultyHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(CraftingScreen.class)
public abstract class MixinCraftingScreen extends HandledScreen<CraftingScreenHandler> {

	@Unique
	private ITimeCraftPlayer player;

	public MixinCraftingScreen(CraftingScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Unique
	private static final Identifier CRAFT_OVERLAY_TEXTURE = new Identifier("crafttime:textures/gui/crafting_table.png");

	@Inject(method = "drawBackground", at = @At("TAIL"))
	protected void timecraft$drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY,
			CallbackInfo info) {
		assert this.client != null;
		this.player = (ITimeCraftPlayer) this.client.player;

		RenderSystem.setShaderTexture(0,CRAFT_OVERLAY_TEXTURE);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		if (player.craftTime$isCrafting() && player.craftTime$getCraftPeriod() > 0) {
			int l = (int) ((player.craftTime$getCraftTime() * 24.0F / player.craftTime$getCraftPeriod()));
			MixinCraftingScreen.drawTexture(matrices, i + 89, j + 35, 0, 0, l + 1, 16, 24, 17);
		}
	}

	@Inject(method = "handledScreenTick", at = @At("TAIL"))
	public void timecraft$tick(CallbackInfo info) {
		assert this.client != null;
		this.player = (ITimeCraftPlayer) this.client.player;
		ItemStack resultStack = this.handler.getSlot(0).getStack();
		boolean finished = player.craftTime$tick(resultStack);

		if (finished) {
			ArrayList<Item> old_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, true);

			//---------------------------------------------------------
			super.onMouseClick(this.handler.getSlot(0), 0, 0, SlotActionType.THROW);

			ArrayList<Item> new_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, true);

			if (old_recipe.equals(new_recipe) )
				player.craftTime$setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, true,this));
			else
				player.craftTime$stopCraft();
		}
	}

	@Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType,
			CallbackInfo info) {

		if (slot != null) {
			invSlot = slot.id;
		}
		if (invSlot > 0 && invSlot < 10) {
			player.craftTime$setCraftTime(0);
			player.craftTime$setCrafting(false);
		}
		if (invSlot == 0) {
			if (!player.craftTime$isCrafting() ) {
				player.craftTime$startCraftWithNewPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, true,this));
			}
			info.cancel();
		}
	}
}
