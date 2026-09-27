package xyz.kohara.adjcore;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ADJData {

	private static String path(String name) {
		return path(name, "txt");
	}

	private static String path(String name, String ending) {
		return "config/" + ADJCore.MOD_ID + "/" + name + "." + ending;
	}

	private static final String DEATH_TEXTS_FILE = path("death_text");
	private static final String STRUCTURES_IGNORE_MIN_DISTANCE_FILE = path("structures_ignore_min_distance");
	private static final String POTION_NAME_OVERRIDES_FILE = path("potion_name_overrides");
	private static final String WINDOW_TITLES_FILE = path("window_titles");
	private static final String ATTRIBUTES_TOOLTIP_ORDER_FILE = path("attributes_tooltip_order");
	private static final String EXTRA_HEART_DROP_RULES = path("extra_heart_drop_rules");
	private static final String CURIO_SLOTS_TO_KEEP = path("curio_slots_to_keep");
	private static final String HEALING_EFFECTS = path("healing_effects");

	private static final List<String> deathTexts = new ArrayList<>();
	public static final List<String> structuresIgnoreMinDistance = new ArrayList<>();
	public static final Map<String, String> potionNameOverrides = new HashMap<>();
	public static final List<String> windowTitles = new ArrayList<>();
	public static final List<ResourceLocation> attributesTooltipOrder = new ArrayList<>();
	public static final Map<ResourceLocation, HeartDropRule> heartDropRules = new HashMap<>();
	public static final List<String> curioSlotsToKeep = new ArrayList<>();
	public static final List<ResourceLocation> healingEffects = new ArrayList<>();

	static {
		reloadEverythingReloadable();
	}

	public static void reloadEverythingReloadable() {
		deathTexts.clear();
		structuresIgnoreMinDistance.clear();
		potionNameOverrides.clear();
		windowTitles.clear();
		attributesTooltipOrder.clear();

		deathTexts.addAll(readLines(DEATH_TEXTS_FILE));
		structuresIgnoreMinDistance.addAll(readLines(STRUCTURES_IGNORE_MIN_DISTANCE_FILE));
		potionNameOverrides.putAll(readMap(POTION_NAME_OVERRIDES_FILE, ":"));
		windowTitles.addAll(readLines(WINDOW_TITLES_FILE));

		readLines(ATTRIBUTES_TOOLTIP_ORDER_FILE).stream()
				.map(String::trim)
				.filter(s -> !s.isEmpty() && !s.startsWith("#"))
				.map(ResourceLocation::parse)
				.forEach(attributesTooltipOrder::add);

		readLines(EXTRA_HEART_DROP_RULES).stream()
				.map(String::trim)
				.filter(s -> !s.isEmpty() && !s.startsWith("#"))
				.forEach(s -> {
					String[] data = s.split("/");
					ResourceLocation loc = ResourceLocation.parse(data[0]);
					double chance = Double.parseDouble(data[2]);
					if (data[1].contains("-")) {
						String[] bounds = data[1].split("-");
						int min = Integer.parseInt(bounds[0]);
						int max = Integer.parseInt(bounds[1]);
						heartDropRules.put(loc, new HeartDropRule(min, max, chance));
					} else {
						int amount = Integer.parseInt(data[1]);
						heartDropRules.put(loc, new HeartDropRule(amount, chance));
					}

				});

		curioSlotsToKeep.addAll(readLines(CURIO_SLOTS_TO_KEEP));

		readLines(HEALING_EFFECTS)
				.stream()
				.map(ResourceLocation::parse)
				.forEach(healingEffects::add);
	}

	private static List<String> readLines(String path) {
		try {
			Path file = Paths.get(path);
			Files.createDirectories(file.getParent());

			if (Files.notExists(file)) {
				Files.createFile(file);
			}

			return Files.readAllLines(file);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read config: " + path, e);
		}
	}

	private static Map<String, String> readMap(String path, String separator) {
		Map<String, String> map = new HashMap<>();

		for (String line : readLines(path)) {
			if (line.isBlank() || !line.contains(separator)) continue;

			String[] parts = line.split(separator, 2);
			map.put(parts[0].trim(), parts[1].trim());
		}

		return map;
	}


	public static String getRandomDeathText() {
		if (deathTexts.isEmpty())
			return "\"\"";

		return "\"" + deathTexts.get(new Random().nextInt(deathTexts.size())) + "\"";
	}

	public static Comparator<Attribute> attributeComparator() {

		return (a, b) -> {
			ResourceLocation aId = ForgeRegistries.ATTRIBUTES.getKey(a);
			ResourceLocation bId = ForgeRegistries.ATTRIBUTES.getKey(b);

			int aIndex = attributesTooltipOrder.indexOf(aId);
			int bIndex = attributesTooltipOrder.indexOf(bId);

			// both explicitly ordered
			if (aIndex != -1 && bIndex != -1)
				return Integer.compare(aIndex, bIndex);

			// one explicitly ordered
			if (aIndex != -1) return -1;
			if (bIndex != -1) return 1;

			// fallback alphabetical
			return aId.toString().compareTo(bId.toString());
		};
	}

	public static class HeartDropRule {

		private final int min;
		private final int max;
		private final double chance;

		private HeartDropRule(int amount, double chance) {
			this.min = amount;
			this.max = amount;
			this.chance = chance;
		}

		private HeartDropRule(int min, int max, double chance) {
			this.min = min;
			this.max = max;
			this.chance = chance;
		}

		public int getDropAmount() {
			if (this.min == this.max) {
				return this.max;
			}
			return new Random().nextInt(this.max - this.min + 1) + this.min;
		}

		public double getChance() {
			return this.chance;
		}
	}
}
