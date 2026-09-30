package xyz.kohara.adjcore.mixins.client;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Options.class)
public abstract class OptionsMixin {

	@Shadow
	@Final
	@Mutable
	private OptionInstance<Integer> fov = new OptionInstance<>(
			"options.fov",
			OptionInstance.noTooltip(),
			(arg, integer) -> switch (integer) {
				case 30 -> Options.genericValueLabel(arg, Component.literal("Just use a Spyglass bro..."));
				case 70 -> Options.genericValueLabel(arg, Component.literal("Pretty Screenshots"));
				case 90 -> Options.genericValueLabel(arg, Component.literal("Normal"));
				case 110 -> Options.genericValueLabel(arg, Component.literal("Quake Pro"));
				default -> Options.genericValueLabel(arg, integer);
			},
			new OptionInstance.IntRange(30, 110),
			Codec.DOUBLE.xmap(d -> (int) (d * 40.0 + 70.0), i -> (i - 70.0) / 40.0),
			70,
			integer -> Minecraft.getInstance().levelRenderer.needsUpdate()
	);
}
