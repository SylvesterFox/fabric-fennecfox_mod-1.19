package net.sylvesternotcute.fennecmod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.custom.ChineseDragonEntity;
import net.sylvesternotcute.fennecmod.entity.custom.FennecEntity;

public class ModEntities {
   public static final EntityType<FennecEntity> FENNEC = Registry.register(
         Registry.ENTITY_TYPE, new Identifier(FennecMod.MOD_ID, "fennec"),
           FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FennecEntity::new)
                   .dimensions(EntityDimensions.fixed(0.9f, 0.7f)).build());

   public static final EntityType<ChineseDragonEntity> CHINESE_DRAGON = Registry.register(
           Registry.ENTITY_TYPE, new Identifier(FennecMod.MOD_ID, "chinese_dragon"),
           FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ChineseDragonEntity::new)
                   .dimensions(EntityDimensions.fixed(2.0f, 1.2f)).build());
}
