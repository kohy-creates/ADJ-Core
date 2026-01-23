package xyz.kohara.adjcore.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.kohara.adjcore.ADJCore;
import xyz.kohara.adjcore.misc.LangGenerator;
import xyz.kohara.adjcore.registry.entities.CollectibleEntity;
import xyz.kohara.adjcore.registry.entities.Heart;
import xyz.kohara.adjcore.registry.entities.ManaStar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ADJEntities {

    public static final List<Object> COLLECTIBLES = new ArrayList<>();

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ADJCore.MOD_ID);

    public static final RegistryObject<EntityType<ManaStar>> MANA_STAR = registerCollectible(
            "mana_star",
            "Mana Star",
            ManaStar::new
    );

    public static final RegistryObject<EntityType<Heart>> HEART = registerCollectible(
            "heart",
            "Heart",
            Heart::new
    );

    private static <T extends CollectibleEntity> RegistryObject<EntityType<T>> registerCollectible(String id, String name, EntityType.EntityFactory<T> factory) {
        var entityType = register(id, name, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .fireImmune()
                .sized(0.25F, 0.25F)
                .build(id));
        COLLECTIBLES.add(entityType);
        return entityType;
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, String name, Supplier<EntityType<T>> factory) {
        LangGenerator.addItemTranslation(id, name);
        return ENTITY_TYPES.register(id, factory);
    }

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }

}
