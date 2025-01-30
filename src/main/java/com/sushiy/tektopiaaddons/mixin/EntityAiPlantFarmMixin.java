package com.sushiy.tektopiaaddons.mixin;

import com.sushiy.tektopiaaddons.TektopiaAddons;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.tangotek.tektopia.entities.EntityFarmer;
import net.tangotek.tektopia.entities.EntityVillageNavigator;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import net.tangotek.tektopia.entities.ai.EntityAIMoveToBlock;
import net.tangotek.tektopia.entities.ai.EntityAIPlantFarm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Mixin(value = EntityAIPlantFarm.class)
public abstract class EntityAiPlantFarmMixin  extends EntityAIMoveToBlock {
    public EntityAiPlantFarmMixin(EntityVillageNavigator v) {
        super(v);
    }


    @Shadow(remap = false)
    private IBlockState plantState = null;
    @Shadow(remap = false)
    protected @Final EntityVillagerTek villager;
    @Shadow(remap = false)
    private Predicate<BlockPos> isPlantable() {return null;}

    /**
     * @author Sushiy
     * @reason fix detection of modded crops
     */
    protected BlockPos getDestinationBlock() {
        BlockPos farmPos = this.villager.getVillage().requestFarmland(this.isPlantable());
        if (farmPos != null) {
            BlockPos cropPos = farmPos.up();
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Found PlantFarm Destination");
            this.plantState = this.checkNearbyCrops(cropPos);
            if (this.plantState != null)
            {
                String aiFilter = TektopiaAddons.getPlantAIFilterName(plantState.getBlock());
                //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Testing for " + aiFilter);
                if(this.villager.isAIFilterEnabled(aiFilter))
                {
                    Item seedItem = EntityFarmer.getSeed(this.plantState);
                    Predicate<ItemStack> seedPred = (i) -> i.getItem() == seedItem;
                    if (this.villager.getInventory().getItemCount(seedPred) >= 1) {
                        return cropPos;
                    }

                    this.villager.setItemThought(seedItem);
                }

            }
        }
        else
        {
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Found No PlantFarm Destination");
        }

        return null;
    }

    private IBlockState checkNearbyCrops(BlockPos pos) {
        IBlockState bestState = null;

        HashMap<Block, Integer> blocks = new HashMap<>();
        Block west = this.villager.world.getBlockState(pos.west()).getBlock();
        Block east = this.villager.world.getBlockState(pos.east()).getBlock();
        Block north = this.villager.world.getBlockState(pos.north()).getBlock();
        Block south = this.villager.world.getBlockState(pos.south()).getBlock();

        if(TektopiaAddons.isCropBlock(west))
            blocks.merge(west, 1, Integer::sum);
        if(TektopiaAddons.isCropBlock(east))
            blocks.merge(east, 1, Integer::sum);
        if(TektopiaAddons.isCropBlock(north))
            blocks.merge(north, 1, Integer::sum);
        if(TektopiaAddons.isCropBlock(south))
            blocks.merge(south, 1, Integer::sum);

        Map.Entry<Block, Integer> maxEntry = null;

        for (Map.Entry<Block, Integer> entry : blocks.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        if(maxEntry != null)
            return maxEntry.getKey().getDefaultState();
        else
            return null;
    }
}
