// Auditory
package xyz.kohara.adjcore.mixins.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.kohara.adjcore.registry.AuditoryTags;
import xyz.kohara.adjcore.registry.ADJSoundEvents;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(Block.class)
public class BlockSoundsMixin {

	@Inject(method = "fallOn", at = @At("RETURN"))
	public void auditory_fallSound(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
		if (entity instanceof LivingEntity && !entity.isShiftKeyDown()) {
			if (!state.isAir() && !state.is(BlockTags.FIRE) && !state.is(BlockTags.PORTALS) && (!(state.getBlock() instanceof LiquidBlock))) {
				SoundType soundType = state.getSoundType();
				world.playSound(null, pos, soundType.getStepSound(), SoundSource.BLOCKS, soundType.getVolume() * 0.15F, soundType.getPitch());
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "getSoundType", cancellable = true)
	private void auditory_alterSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
		SoundType sound = adj$getAuditorySoundType(state);
		if (sound != null) {
			cir.setReturnValue(sound);
		}
	}

	@Unique
	private static final Map<Block, SoundType> AUDITORY_SOUNDS = new IdentityHashMap<>();

	@Unique
	private static SoundType adj$getAuditorySoundType(BlockState state) {
		Block block = state.getBlock();

		SoundType cached = AUDITORY_SOUNDS.get(block);
		if (cached != null) {
			return cached;
		}

		SoundType result = null;

		if (state.is(AuditoryTags.BASALT_SOUNDS)) {
			result = SoundType.BASALT;
		} else if (state.is(AuditoryTags.CLAY_BRICK_SOUNDS)) {
			result = ADJSoundEvents.CLAY_BRICKS;
		} else if (state.is(AuditoryTags.DIRT_SOUNDS)) {
			result = SoundType.ROOTED_DIRT;
		} else if (state.is(AuditoryTags.GOLD_SOUNDS)) {
			result = ADJSoundEvents.GOLD;
		} else if (state.is(AuditoryTags.LEAF_SOUNDS)) {
			result = SoundType.AZALEA_LEAVES;
		} else if (state.is(AuditoryTags.LILY_PAD_SOUNDS)) {
			result = ADJSoundEvents.LILY_PAD;
		} else if (state.is(AuditoryTags.METAL_SOUNDS)) {
			result = ADJSoundEvents.METAL;
		} else if (state.is(AuditoryTags.NETHERRACK_SOUNDS)) {
			result = SoundType.NETHERRACK;
		} else if (state.is(AuditoryTags.OBSIDIAN_SOUNDS)) {
			result = ADJSoundEvents.OBSIDIAN;
		} else if (state.is(AuditoryTags.PLANT_SOUNDS)) {
			result = SoundType.HANGING_ROOTS;
		} else if (state.is(AuditoryTags.RAW_ORE_BLOCK_SOUNDS)) {
			result = SoundType.NETHER_GOLD_ORE;
		} else if (state.is(AuditoryTags.SAND_SOUNDS)) {
			result = SoundType.SAND;
		} else if (state.is(AuditoryTags.SHULKER_BOX_SOUNDS)) {
			result = ADJSoundEvents.SHULKER_BOX;
		} else if (state.is(AuditoryTags.SMALL_OBJECT_SOUNDS)) {
			result = ADJSoundEvents.SMALL_OBJECT;
		} else if (state.is(AuditoryTags.SPAWNER_SOUNDS)) {
			result = ADJSoundEvents.SPAWNER;
		} else if (state.is(AuditoryTags.STONE_BRICK_SOUNDS)) {
			result = ADJSoundEvents.STONE_BRICKS;
		} else if (state.is(AuditoryTags.STONE_ORE_SOUNDS)) {
			result = ADJSoundEvents.STONE_ORE;
		} else if (state.is(AuditoryTags.STRING_SOUNDS)) {
			result = SoundType.VINE;
		} else if (state.is(AuditoryTags.TERRACOTTA_SOUNDS)) {
			result = ADJSoundEvents.TERRACOTTA;
		} else if (state.is(AuditoryTags.WOOD_SOUNDS)) {
			result = SoundType.WOOD;
		} else if (state.is(AuditoryTags.MUSHROOM_SOUNDS)) {
			result = SoundType.WART_BLOCK;
		} else if (state.is(AuditoryTags.MUSHROOM_STEM_SOUNDS)) {
			result = SoundType.STEM;
		} else if (state.is(AuditoryTags.PURPUR_SOUNDS)) {
			result = ADJSoundEvents.PURPUR;
		} else if (state.is(AuditoryTags.CHORUS_PLANT_SOUNDS)) {
			result = ADJSoundEvents.CHORUS_PLANT;
		} else if (state.is(AuditoryTags.ICE_SOUNDS)) {
			result = ADJSoundEvents.ICE;
		} else if (state.is(AuditoryTags.GOURD_SOUNDS)) {
			result = ADJSoundEvents.GOURD;
		} else if (state.is(AuditoryTags.POT_SOUNDS)) {
			result = ADJSoundEvents.SMALL_OBJECT;
		} else if (state.is(AuditoryTags.BOOKSHELF_SOUNDS)) {
			result = SoundType.CHISELED_BOOKSHELF;
		}

		if (result != null) {
			AUDITORY_SOUNDS.put(block, result);
		}

		return result;
	}
}
