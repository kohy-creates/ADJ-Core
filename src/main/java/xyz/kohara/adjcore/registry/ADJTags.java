package xyz.kohara.adjcore.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import xyz.kohara.adjcore.ADJCore;

public class ADJTags {

	public static class Items {
		public static final TagKey<Item> CURIOS_DROPPED_ON_DEATH = create(Registries.ITEM, "curios_dropped_on_death");
	}

	public static class EntityTypes {
		public static final TagKey<EntityType<?>> PETS_THAT_IGNORE_DEATH_MESSAGES = create(Registries.ENTITY_TYPE, "pets_that_ignore_death_messages");
	}

	public static class DamageTypes {
		public static final TagKey<DamageType> MELEE = create(Registries.DAMAGE_TYPE, "melee");
		public static final TagKey<DamageType> PLAYER_MELEE = create(Registries.DAMAGE_TYPE, "player_melee");
		public static final TagKey<DamageType> MOB_MELEE = create(Registries.DAMAGE_TYPE, "mob_melee");
		public static final TagKey<DamageType> DAMAGE_OVER_TIME = create(Registries.DAMAGE_TYPE, "dot");

		public static final TagKey<DamageType> IGNORES_COOLDOWN = create(Registries.DAMAGE_TYPE, "bypasses_cooldown");

		public static final TagKey<DamageType> IS_ENVIRONMENTAL = create(Registries.DAMAGE_TYPE, "is_environmental");
		public static final TagKey<DamageType> IS_PHYSICAL = create(Registries.DAMAGE_TYPE, "is_physical");

		public static final TagKey<DamageType> NO_HURT_BOB = create(Registries.DAMAGE_TYPE, "no_hurt_bob");
	}

	public static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> key, String path) {
		return TagKey.create(key, ADJCore.of(path));
	}
}
