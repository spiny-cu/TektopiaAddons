package com.sushiy.tektopiaaddons.mixin;

import com.sushiy.tektopiaaddons.TektopiaAddons;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import net.tangotek.tektopia.structures.VillageStructure;
import net.tangotek.tektopia.structures.VillageStructureMineshaft;
import net.tangotek.tektopia.structures.VillageStructureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = VillageStructureMineshaft.class)
public abstract class VillageStructureMineshaftMixin extends VillageStructure {
    protected VillageStructureMineshaftMixin(World world, Village v, EntityItemFrame itemFrame, VillageStructureType t, String name) {
        super(world, v, itemFrame, t, name);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static boolean isOre(World world, BlockPos pos) {
        Block b = world.getBlockState(pos).getBlock();
        return TektopiaAddons.isOreBlock(b);
    }

    /**
     * @author Sushiy
     * @reason Includes all modded ores that we have previously scanned
     */
    @Overwrite(remap = false)
    public static Block getRegrowBlock(EntityVillagerTek villager) {
        int oreRoll = villager.getRNG().nextInt(100);
        int count = 0;
        for(Block ore : TektopiaAddons.oreWeights.keySet())
        {
            count += TektopiaAddons.oreWeights.get(ore) * (TektopiaAddons.totalOreWeight/100);
            if(count >= oreRoll)
            {
                return ore;
            }
        }
        throw new RuntimeException(TektopiaAddons.MODID + " No random value found. This should be impossible.");
    }
}
