package com.sushiy.tektopiaaddons.mixin;

import com.sushiy.tektopiaaddons.ITradeableStructure;
import com.sushiy.tektopiaaddons.TektopiaAddons;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.structures.VillageStructure;
import net.tangotek.tektopia.structures.VillageStructureMerchantStall;
import net.tangotek.tektopia.structures.VillageStructureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VillageStructureMerchantStall.class)
public class VillageStructureMerchantStallMixin extends VillageStructure implements ITradeableStructure {
    protected VillageStructureMerchantStallMixin(World world, Village v, EntityItemFrame itemFrame, VillageStructureType t, String name) {
        super(world, v, itemFrame, t, name);
    }

    protected BlockPos deliveryChestPos = null;
    protected TileEntityChest deliveryChest = null;

    public BlockPos tektopiaAddons$getDeliveryChestPos()
    {
        return deliveryChestPos;
    }

    @Override
    public TileEntityChest tektopiaAddons$getDeliveryChest() {
        return deliveryChest;
    }

    @Inject(method = "doFloorScan", at = @At("TAIL"), remap = false)
    protected void doFloorScanInject(CallbackInfo ci) {
        BlockPos belowFrame = framePos.down();
        scanSpecialBlock(belowFrame, this.world.getBlockState(belowFrame).getBlock());
    }

    @Override
    protected void scanSpecialBlock(BlockPos pos, Block block) {
        TileEntity te = this.world.getTileEntity(pos);
        if (te instanceof TileEntityChest) {
            deliveryChest = (TileEntityChest)te;
            deliveryChestPos = pos;
            addSpecialBlock(block, pos);
            this.specialAdded = true;
            TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " found a delivery chest in the merchant stall at " + pos);
        }
        else
        {
            if(deliveryChest != null)
            {
                deliveryChest = null;
                deliveryChestPos = null;
                TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " removed a delivery chest in the merchant stall at " + pos);

            }
        }
    }
}
