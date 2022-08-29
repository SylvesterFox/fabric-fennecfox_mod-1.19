package net.sylvesternotcute.fennecmod.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.sylvesternotcute.fennecmod.FennecMod;

public class ModItemGroup {
    public static final ItemGroup FOXXOGROUP = FabricItemGroupBuilder.build(new Identifier(FennecMod.MOD_ID, "foxxogroup"),
            () -> new ItemStack(Items.BARREL));
}
