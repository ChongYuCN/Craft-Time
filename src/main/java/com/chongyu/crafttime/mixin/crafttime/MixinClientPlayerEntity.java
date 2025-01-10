package com.chongyu.crafttime.mixin.crafttime;

import com.chongyu.crafttime.ITimeCraftPlayer;
import com.chongyu.crafttime.sound.SoundEventRegistry;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements ITimeCraftPlayer {

	@Unique
	public boolean is_crafting = false;
	@Unique
	public float craft_time = 0;
	@Unique
	public float craft_period = 0;

	public MixinClientPlayerEntity(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
		super(world, profile, publicKey);
	}


	@Override
	public void craftTime$setCrafting(boolean is_crafting) {
		this.is_crafting = is_crafting;
	}

	@Override
	public boolean craftTime$isCrafting() {
		return this.is_crafting;
	}

	@Override
	public void craftTime$setCraftTime(float craft_time) {
		this.craft_time = craft_time;
	}

	@Override
	public float craftTime$getCraftTime() {
		return this.craft_time;
	}

	@Override
	public void craftTime$setCraftPeriod(float craft_period) {
		this.craft_period = craft_period;
	}

	@Override
	public float craftTime$getCraftPeriod() {
		return this.craft_period;
	}
	
	@Override
	public void craftTime$stopCraft() {
		this.is_crafting = false;
		this.craft_time = 0F;
	}

	@Override
	public void craftTime$startCraftWithNewPeriod(float craft_period) {
		this.craft_time = 0;
		this.craft_period = craft_period;
		this.is_crafting = true;

//		if (craft_period >= 10F) {
//			MinecraftClient.getInstance().getSoundManager().play(new CraftingTickableSound(Random.create(),this, this.getBlockPos()));
//		}
	}
	
	@Override
	public boolean craftTime$tick(ItemStack resultStack) {

		if (this.craftTime$isCrafting()) {
//			ItemStack cursorStack = this.getInventory().player.currentScreenHandler.getCursorStack();
//			if (cursorStack.getItem() != Items.AIR) {
//				if (!cursorStack.isItemEqual(resultStack)
//						|| cursorStack.getCount() + resultStack.getCount() > cursorStack.getMaxCount()) {
//					return false;
//				}
//			}
			if(resultStack.isEmpty()){
				return false;
			}

			if (this.craftTime$getCraftTime() < this.craftTime$getCraftPeriod()) {
				this.craft_time += getCraftingSpeed(this);
			} else if (this.craftTime$getCraftTime() >= this.craftTime$getCraftPeriod()) {
				this.playSound(SoundEventRegistry.finishSound, SoundCategory.PLAYERS, 0.1F, 1f);
				this.craftTime$startCraftWithNewPeriod(craft_period);
				return true;
			}
		}
		return false;
	}

	@Unique
	public float getCraftingSpeed(PlayerEntity player) {
		float speed = 1F;
		speed += 0.02F * Math.min(200, player.experienceLevel);
		return speed;
	}
}
