package xyz.kohara.adjcore.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.kohara.adjcore.ADJCore;
import xyz.kohara.adjcore.misc.LangGenerator;
import xyz.kohara.adjcore.registry.effects.*;

import java.util.function.Supplier;

public class ADJEffects {
	private static final DeferredRegister<MobEffect> MOB_EFFECTS =
			DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ADJCore.MOD_ID);
	private static final DeferredRegister<Potion> POTIONS =
			DeferredRegister.create(ForgeRegistries.POTIONS, ADJCore.MOD_ID);

	public static final RegistryObject<MobEffect> COZY_CAMPFIRE = register(
			"cozy_campfire",
			new CozyCampfireEffect(),
			"Cozy Campfire",
			"Slowly regenerates lost life"
	);

	public static final RegistryObject<MobEffect> LESSER_INSTANT_HEALTH = register(
			"lesser_instant_health",
			new LesserInstantHealthEffect(),
			"Lesser Instant Health",
			"Instantly replenishes a small amount of lost health"
	);

	public static final RegistryObject<MobEffect> INSTANT_MANA = register(
			"instant_mana",
			new InstantManaEffect(),
			"Instant Mana",
			"Instantly replenishes some mana"
	);

	public static final RegistryObject<MobEffect> HEART_LANTERN = register(
			"heart_lantern",
			new HeartLanternEffect(),
			"Heart Lantern",
			"Life regeneration increased"
	);

	public static final RegistryObject<MobEffect> FALLEN_KANADE = register(
			"fallen_kanade",
			new FallenKanadeEffect(),
			"Fallen Kanade",
			"Increased life regeneration and reduced negative effect duration"
	);

	public static final RegistryObject<MobEffect> POTION_SICKNESS = register(
			"potion_sickness",
			new PotionSicknessEffect(),
			"Potion Sickness",
			"Cannot consume any more healing items"
	);

	public static final RegistryObject<MobEffect> ARCHERY = register(
			"archery",
			new ArcheryEffect(),
			"Archery",
			"10% increased arrow damage and 20% increased arrow velocity"
	);

	public static final RegistryObject<MobEffect> BUILDER = register(
			"builder",
			new BuilderEffect(),
			"Builder",
			"Increased block reach"
	);

	public static final RegistryObject<MobEffect> IRON_SKIN = register(
			"iron_skin",
			new ArcheryEffect(),
			"Ironskin",
			"Increase defense by 6"
	);

	public static final RegistryObject<MobEffect> MAGIC_POWER = register(
			"magic_power",
			new MagicPowerEffect(),
			"Magic Power",
			"15% increased magic damage"
	);

	public static final RegistryObject<MobEffect> THORNS = register(
			"thorns",
			new ThornsEffect(),
			"Thorns",
			"Attacks also take damage"
	);


	private static RegistryObject<MobEffect> register(String id, MobEffect effect, String name, String description) {
		LangGenerator.addEffectTranslation(id, name, description);
		return MOB_EFFECTS.register(id, () -> effect);
	}

	static {
		Potions.register();
	}

	public static void register(IEventBus eventBus) {
		MOB_EFFECTS.register(eventBus);
		POTIONS.register(eventBus);
	}

	public static class Potions {

		public static void register() {
		}

		public static RegistryObject<Potion> LESSER_MANA_POTION = register(
				"lesser_mana",
				"instant_mana",
				() -> new MobEffectInstance(INSTANT_MANA.get(), 1, 0),
				"Lesser Mana"
		);

		public static RegistryObject<Potion> MANA_POTION = register(
				"mana",
				"instant_mana",
				() -> new MobEffectInstance(INSTANT_MANA.get(), 1, 1),
				"Mana"
		);

		public static RegistryObject<Potion> GREATER_MANA_POTION = register(
				"greater_mana",
				"instant_mana",
				() -> new MobEffectInstance(INSTANT_MANA.get(), 1, 2),
				"Greater Mana"
		);

		public static RegistryObject<Potion> ENDURANCE = register(
				"endurance",
				"resistance",
				() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 4800, 0),
				"Endurance"
		);

		public static RegistryObject<Potion> DECAY = register(
				"decay",
				"wither",
				() -> new MobEffectInstance(MobEffects.WITHER, 600, 0),
				"Decay"
		);

		public static RegistryObject<Potion> IRON_SKIN = register(
				"iron_skin",
				"iron_skin",
				() -> new MobEffectInstance(ADJEffects.IRON_SKIN.get(), 9600, 0),
				"Ironskin"
		);

		public static RegistryObject<Potion> ARCHERY = register(
				"archery",
				"archery",
				() -> new MobEffectInstance(ADJEffects.ARCHERY.get(), 9600, 0),
				"Archery"
		);

		public static RegistryObject<Potion> MAGIC_POWER = register(
				"magic_power",
				"magic_power",
				() -> new MobEffectInstance(ADJEffects.MAGIC_POWER.get(), 4800, 0),
				"Magic Power"
		);

		public static RegistryObject<Potion> BUILDER = register(
				"builder",
				"builder",
				() -> new MobEffectInstance(ADJEffects.BUILDER.get(), 54000, 0),
				"Builder"
		);

		public static RegistryObject<Potion> THORNS = register(
				"thorns",
				"thorns",
				() -> new MobEffectInstance(ADJEffects.THORNS.get(), 9600, 0),
				"Thorns"
		);

		public static RegistryObject<Potion> HEALING_POTION = register(
				"healing_potion",
				"healing_potion",
				() -> new MobEffectInstance(MobEffects.HEAL, 1, 1),
				"Healing"
		);

		public static RegistryObject<Potion> GREATER_HEALING_POTION = register(
				"greater_healing_potion",
				"greater_healing_potion",
				() -> new MobEffectInstance(MobEffects.HEAL, 1, 2),
				"Greater Healing"
		);

		public static RegistryObject<Potion> SUPREME_HEALING_POTION = register(
				"supreme_healing_potion",
				"supreme_healing_potion",
				() -> new MobEffectInstance(MobEffects.HEAL, 1, 3),
				"Supreme Healing"
		);

		public static RegistryObject<Potion> OMEGA_HEALING_POTION = register(
				"omega_healing_potion",
				"omega_healing_potion",
				() -> new MobEffectInstance(MobEffects.HEAL, 1, 4),
				"Omega Healing"
		);

		private static RegistryObject<Potion> register(String id, String langName, Supplier<MobEffectInstance> effect, String name) {
			LangGenerator.addItemTranslation("item.minecraft.potion." + LangGenerator.constructId("effect", langName), "Potion of " + name);
			LangGenerator.addItemTranslation("item.minecraft.splash_potion." + LangGenerator.constructId("effect", langName), "Splash Potion of" + name);
			LangGenerator.addItemTranslation("item.minecraft.lingering_potion." + LangGenerator.constructId("effect", langName), "Lingering Potion of" + name);
			LangGenerator.addItemTranslation("item.minecraft.tipped_arrow." + LangGenerator.constructId("effect", langName), "Arrow of " + name);
			return POTIONS.register(id, () -> new Potion(effect.get()));
		}
	}
}
