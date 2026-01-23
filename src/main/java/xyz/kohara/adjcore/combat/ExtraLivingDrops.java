package xyz.kohara.adjcore.combat;

import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.kohara.adjcore.ADJCore;
import xyz.kohara.adjcore.ADJData;
import xyz.kohara.adjcore.registry.entities.Heart;
import xyz.kohara.adjcore.registry.entities.ManaStar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtraLivingDrops {

    public static RandomSource random = RandomSource.create();

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player) {
            ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
            itemStack.getOrCreateTag()
                    .putString("SkullOwner", player.getName().getString());

            Component deathMessage = Component.literal(
                            ADJCore.toSmallUnicode(
                                    ADJCore.deathMessageToFirstPerson(player, entity.getCombatTracker().getDeathMessage())
                            )
                    )
                    .withStyle(Style.EMPTY
                            .withColor(TextColor.parseColor("#737373"))
                            .withItalic(true)
                    );

            Component time = Component.literal(
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            ).withStyle(Style.EMPTY
                    .withColor(TextColor.parseColor("#737373"))
                    .withItalic(true)
            );

            ListTag loreList = new ListTag();
            loreList.add(StringTag.valueOf(Component.Serializer.toJson(deathMessage)));
            loreList.add(StringTag.valueOf(Component.Serializer.toJson(time)));

            itemStack.getOrCreateTagElement("display").put("Lore", loreList);

            player.drop(itemStack, true, true);
        } else {
            Level level = entity.level();
            Vec3 pos = entity.position();

            Player player = ADJCore.getNearestPlayerWithinRadius(entity, 40);
            if (player == null) return;
            int currentMana = (int) ManaUtil.getCurrentMana(player);
            int maxMana = ManaUtil.getMaxMana(player);
            if (currentMana != maxMana) {
                if (random.nextFloat() <= (13f / 24f)) {
                    level.addFreshEntity(new ManaStar(level, pos.x(), pos.y(), pos.z()));
                }
            }

            ResourceLocation entityKey = entity.getType().builtInRegistryHolder().key().location();
            if (ADJData.heartDropRules.containsKey(entityKey)) {
                ADJData.HeartDropRule rule = ADJData.heartDropRules.get(entityKey);

                for (int i = 0; i < rule.getDropAmount(); i++) {
                    if (random.nextFloat() <= rule.getChance()) {
                        level.addFreshEntity(new Heart(level, pos.x(), pos.y(), pos.z()));
                    }
                }

            } else {
                float currentHP = player.getHealth();
                float maxHP = player.getMaxHealth();
                if (currentHP != maxHP) {
                    if (random.nextFloat() <= (1f / 12f)) {
                        level.addFreshEntity(new Heart(level, pos.x(), pos.y(), pos.z()));
                    }
                }
            }
        }
    }
}
