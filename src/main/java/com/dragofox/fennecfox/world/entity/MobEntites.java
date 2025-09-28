package com.dragofox.fennecfox.world.entity;

import com.dragofox.fennecfox.world.entity.custom.FennecEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.dragofox.fennecfox.FennecMod.MODID;


public class MobEntites {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.createEntities(MODID);

    public static final Supplier<EntityType<FennecEntity>> FENNEC_ENTITY = ENTITIES
            .register("fennec_fox",
                    () -> EntityType.Builder.of(
                            FennecEntity::new,
                            MobCategory.CREATURE
                        )
                            .sized(0.9f, 0.7f)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.fromNamespaceAndPath("fennecmod", "fennec")
                            ))
                );
}
