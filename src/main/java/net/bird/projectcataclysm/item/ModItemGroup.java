package net.bird.projectcataclysm.item;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    // This group also needs to be identified in en_us.json
    public static final ItemGroup PROJECT_CATACLYSM =
            FabricItemGroupBuilder.build(new Identifier(ProjectCataclysmMod.MOD_ID,
            "project_cataclysm_group"), () -> new ItemStack(ModItems.SILVER_INGOT));
}
