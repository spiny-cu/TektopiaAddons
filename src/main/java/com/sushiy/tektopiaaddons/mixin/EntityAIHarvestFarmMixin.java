package com.sushiy.tektopiaaddons.mixin;

import com.sushiy.tektopiaaddons.TektopiaAddons;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.tangotek.tektopia.entities.EntityVillageNavigator;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import net.tangotek.tektopia.entities.ai.EntityAIHarvestFarm;
import net.tangotek.tektopia.entities.ai.EntityAIMoveToBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityAIHarvestFarm.class)
public abstract class EntityAIHarvestFarmMixin extends EntityAIMoveToBlock
{
    @Shadow(remap = false)
    protected @Final EntityVillagerTek villager;

    public EntityAIHarvestFarmMixin(EntityVillageNavigator v) {
        super(v);
    }

    /**
     * @author Sushiy
     * @reason more control over modded crop targetting
     */
    protected BlockPos getDestinationBlock() {
        if (this.villager.getVillage() != null) {
            BlockPos cropPos = this.villager.getVillage().requestMaxAgeCrop();
            if (cropPos != null)
            {
                ResourceLocation r = this.villager.world.getBlockState(cropPos).getBlock().getRegistryName();
                if(this.villager.isAIFilterEnabled(TektopiaAddons.getHarvestAIFilterName(this.villager.world.getBlockState(cropPos).getBlock()))){
                    //this is a minecraft crop and it is inactive
                    return cropPos;
                }
            }
        }
        return null;
    }
}
