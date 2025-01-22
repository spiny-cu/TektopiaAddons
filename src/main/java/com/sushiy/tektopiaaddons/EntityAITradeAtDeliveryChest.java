package com.sushiy.tektopiaaddons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.tangotek.tektopia.entities.EntityMerchant;
import net.tangotek.tektopia.structures.VillageStructure;
import net.tangotek.tektopia.structures.VillageStructureType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntityAITradeAtDeliveryChest extends EntityAIMoveToBlockCopy
{
    protected final EntityMerchant merchant;
    protected VillageStructure merchantStall;
    TileEntityChest merchantStallChest;
    private boolean active = false;
    private ItemStack itemCarryingCopy;
    MerchantRecipe currentTradeDeal = null;
    int targetTradeSlot = -1;
    int targetResultSlot = -1;

    public EntityAITradeAtDeliveryChest(EntityMerchant entityIn) {
        super(entityIn);
        this.merchant = entityIn;
    }

    @Override
    protected BlockPos getDestinationBlock()
    {
        if(merchant == null || merchant.getVillage() == null) return null;
        //Gather all relevant stalls
        List<VillageStructure> validStalls = this.merchant.getVillage().getStructures(VillageStructureType.MERCHANT_STALL).stream().filter(x -> ((ITradeableStructure) x).tektopiaAddons$getDeliveryChestPos() != null).collect(Collectors.toList());
        Collections.shuffle(validStalls);
        if (!validStalls.isEmpty()) {
            this.merchantStall = validStalls.get(0);
            return ((ITradeableStructure)merchantStall).tektopiaAddons$getDeliveryChestPos();
        }
        return null;
    }

    protected void onArrival() {
        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] onArrival");
        super.onArrival();
        startDelivery();
        this.openChest();
    }

    public boolean shouldContinueExecuting() {
        return this.active;
    }


    private void startDelivery() {
        tradingTime = 200;
        ITradeableStructure tradeable = (ITradeableStructure)merchantStall;
        merchantStall.occupySpecialBlock(tradeable.tektopiaAddons$getDeliveryChestPos());
        //this.merchant.playServerAnimation("villager_pickup");
        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] startDelivery");

        TileEntityChest chest = tradeable.tektopiaAddons$getDeliveryChest();
    }

    int tradingTime = -1;

    public void updateTask() {
        super.updateTask();
        if (this.tradingTime > 0) {
            --this.tradingTime;
            if(this.tradingTime % 20 == 0)
            {
                PerformTrade();
            }
        }else
        {
            active = false;
            this.closeChest();
        }
    }

    private void openChest() {
        TileEntity te = this.merchant.world.getTileEntity(this.destinationPos);
        if (te instanceof TileEntityChest) {
            TileEntityChest tileEntityChest = (TileEntityChest)te;
            EntityPlayer p = this.merchant.world.getClosestPlayer((double)this.destinationPos.getX(), (double)this.destinationPos.getY(), (double)this.destinationPos.getZ(), (double)-1.0F, EntitySelectors.NOT_SPECTATING);
            if (p != null) {
                tileEntityChest.openInventory(p);
            }
        }

    }

    private void closeChest() {
        TileEntity te = this.merchant.world.getTileEntity(this.destinationPos);
        if (te instanceof TileEntityChest) {
            TileEntityChest tileEntityChest = (TileEntityChest)te;
            EntityPlayer p = this.merchant.world.getClosestPlayer((double)this.destinationPos.getX(), (double)this.destinationPos.getY(), (double)this.destinationPos.getZ(), (double)-1.0F, EntitySelectors.NOT_SPECTATING);
            if (p != null) {
                tileEntityChest.closeInventory(p);
            }
        }

    }

    TileEntityChest getChest()
    {
        if(merchantStall == null)
            this.getDestinationBlock();
        if(merchantStallChest == null && merchantStall != null)
        {
            ITradeableStructure tradeable = (ITradeableStructure)merchantStall;
            merchantStallChest = tradeable.tektopiaAddons$getDeliveryChest();
        }

        return merchantStallChest;
    }

    public void PerformTrade()
    {
        ItemStack inChest = getChest().getStackInSlot(targetTradeSlot);
        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Merchant attempts trade with slot " + targetTradeSlot + "=" + inChest.getItem().getRegistryName());

        boolean hasTraded = false;

        if(currentTradeDeal != null)
        {
            this.itemCarryingCopy = currentTradeDeal.getItemToBuy().copy();
            this.merchant.equipActionItem(this.itemCarryingCopy);
            int tradeCount = inChest.getCount() /currentTradeDeal.getItemToBuy().getCount();
            for(int x = 0; x < tradeCount; x++)
            {
                if(currentTradeDeal.isRecipeDisabled()) break;
                currentTradeDeal.incrementToolUses();
                //get items from chest
                ItemStack fromChest = getChest().removeStackFromSlot(targetTradeSlot);
                //adjust count
                fromChest.setCount(fromChest.getCount() - currentTradeDeal.getItemToBuy().getCount());
                //return the rest
                getChest().setInventorySlotContents(targetTradeSlot, fromChest);
                //Add the sold items
                getChest().setInventorySlotContents(targetResultSlot, currentTradeDeal.getItemToSell());
                hasTraded = true;
                TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Merchant has bought " + currentTradeDeal.getItemToBuy().toString() + " from the deliverybox for " + currentTradeDeal.getItemToSell().toString());
            }
        }
        if(hasTraded)
        {
            merchant.livingSoundTime = -merchant.getTalkInterval();
            merchant.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0f,
                    merchant.isChild() ? (merchant.getRNG().nextFloat() - merchant.getRNG().nextFloat()) * 0.2F + 1.5F : (merchant.getRNG().nextFloat() - merchant.getRNG().nextFloat()) * 0.2F + 1.0F);

        }
        GetValidTradeDeal();
    }

    protected int getFirstFreeOrMatchingChestSlot(TileEntityChest chest, ItemStack stack)
    {
        for(int i = 0; i < chest.getSizeInventory(); i++) {
            ItemStack is = chest.getStackInSlot(i);
            if(is.isEmpty())
            {
                return i;
            }
            else
            {
                if(ItemStack.areItemsEqual(is, stack))
                {
                    int stackUntilMax = is.getMaxStackSize() - is.getCount();
                    if(stackUntilMax > stack.getCount())
                    {
                        return i;
                    }

                }
            }
        }
        return -1;
    }

    MerchantRecipe GetValidTradeDeal()
    {
        MerchantRecipeList recipes = merchant.getRecipes(null);
        if(recipes==null)
        {
            TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] GetValidTradeDeal: no recipes");
            return null;
        }
        for (MerchantRecipe tradeDeal : recipes) {
            if (tradeDeal.isRecipeDisabled()) continue;
            for (int j = 0; j < getChest().getSizeInventory(); j++) {
                ItemStack stack = getChest().getStackInSlot(j);
                if (stack.isEmpty()) continue;
                if (ItemStack.areItemsEqual(tradeDeal.getItemToBuy(), stack)) {
                    int resultSlot = getFirstFreeOrMatchingChestSlot(getChest(), tradeDeal.getItemToSell());
                    if (resultSlot == -1) {
                        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] GetValidTradeDeal: no resultSlot for: " + tradeDeal.getItemToSell().toString());
                        continue;
                    }
                    targetTradeSlot = j;
                    targetResultSlot = resultSlot;
                    return tradeDeal;
                }
            }

        }
        return null;
    }

    protected MerchantRecipe getTradeDeal(ItemStack itemStack){
        for(MerchantRecipe recipe : merchant.getRecipes(null))
        {
            if(ItemStack.areItemsEqual(recipe.getItemToBuy(), itemStack))
            {
                return recipe;
            }
        }
        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] found no trade deal that buys " + itemStack.getItem().getRegistryName());
        return null;
    }
    private void stopDelivery() {

        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] stopDelivery");
        //merchant.stopServerAnimation("villager_pickup");
    }

    public boolean isInterruptible() {
        return this.tradingTime > 30 || this.tradingTime <= 0;
    }
    protected void onStuck() {
        this.active = false;
        super.onStuck();
    }

    protected BlockPos findWalkPos() {
        BlockPos result = super.findWalkPos();
        if (result == null) {
            BlockPos pos = this.destinationPos;
            BlockPos testPos = pos.west(2);
            if (this.isWalkable(testPos, this.navigator)) {
                return testPos;
            }

            testPos = pos.east(2);
            if (this.isWalkable(testPos, this.navigator)) {
                return testPos;
            }

            testPos = pos.north(2);
            if (this.isWalkable(testPos, this.navigator)) {
                return testPos;
            }

            testPos = pos.south(2);
            if (this.isWalkable(testPos, this.navigator)) {
                return testPos;
            }
        }

        return result;
    }

    public boolean shouldExecute() {

        if (this.merchant.isAITick() && this.merchant.hasVillage() )
        {
            if(getChest() == null)
            {
                TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] should Execute Delivery false no chest");
                return false;
            }

            currentTradeDeal = GetValidTradeDeal();
            if(currentTradeDeal != null)
            {
                TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] should Execute Delivery true");
                return super.shouldExecute();
            }
            else
            {
                TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] should Execute Delivery false no tradeDeal");
            }
        }
        return false;
    }

    public void startExecuting() {
        active = true;
        this.merchant.setStall(1);
        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] startExecuting Delivery");
        super.startExecuting();
        itemCarryingCopy = currentTradeDeal.getItemToBuy().copy();
        if (this.itemCarryingCopy != null) {
            this.merchant.equipActionItem(this.itemCarryingCopy);
        }
    }

    @Override
    protected void updateMovementMode() {
        this.merchant.setMovementMode(this.merchant.getDefaultMovement());
    }
    public void resetTask() {
        TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + "[" + getClass().getName() + "] resetTask");
        this.stopDelivery();
        tradingTime = -1;
        currentTradeDeal = null;
        targetResultSlot = -1;
        targetTradeSlot = -1;
        super.resetTask();
        if (this.itemCarryingCopy != null) {
            this.merchant.unequipActionItem(this.itemCarryingCopy);
            this.itemCarryingCopy = null;
            this.merchant.setStoragePriority();
        }
    }
}
