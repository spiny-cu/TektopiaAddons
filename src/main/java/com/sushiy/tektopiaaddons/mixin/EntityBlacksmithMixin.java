package com.sushiy.tektopiaaddons.mixin;

import com.sushiy.tektopiaaddons.TektopiaAddons;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.tangotek.tektopia.ProfessionType;
import net.tangotek.tektopia.entities.EntityBlacksmith;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import net.tangotek.tektopia.entities.crafting.Recipe;
import net.tangotek.tektopia.storage.ItemDesire;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(value = EntityBlacksmith.class, remap = false)
public abstract class EntityBlacksmithMixin extends EntityVillagerTek{


    public EntityBlacksmithMixin(World worldIn, ProfessionType profType, int roleMask) {
        super(worldIn, profType, roleMask);
    }

    @Shadow
    private static List<Recipe> craftSetAnvil;

    @Shadow protected abstract boolean canVillagerPickupItem(ItemStack itemIn);

    @Mutable
    @Unique
    private static @Final DataParameter<Boolean> SMELT_MODDED;

    @Inject(method = "entityInit", at = @At("TAIL"))
    protected void entityInit(CallbackInfo ci) {
        this.registerAIFilter("smelt_modded", SMELT_MODDED);
    }
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void staticBlock(CallbackInfo ci) {
        SMELT_MODDED = EntityDataManager.createKey(EntityBlacksmith.class, DataSerializers.BOOLEAN);
    }

    @Inject(method = "initEntityAI", at = @At("TAIL"))
    protected void initEntityAI(CallbackInfo ci)
    {
        for(ItemStack input : FurnaceRecipes.instance().getSmeltingList().keySet())
        {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
            if(!result.isEmpty())
            {
                //Check if it is either an ore or an ingot
                if((TektopiaAddons.oreItems.contains(input.getItem()) || TektopiaAddons.ingotItems.contains(input.getItem())))
                {
                    this.getDesireSet().addItemDesire(new ItemDesire(input.getItem(), 0, 8, 16, (x) -> x.isAIFilterEnabled("smelt_modded")));
                    TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " added itemdesire for blacksmith " + input.getItem().getRegistryName() + "->" + result.getItem().getRegistryName());
                }
            }
        }
    }

    /**
     * @author Sushiy
     * @reason adding a second filter would have been equally viable, but more tasks makes it less clear what the AI is doing
     */
    @Overwrite
    private static Function<ItemStack, Integer> bestSmeltable(EntityVillagerTek villager) {
        return (p) -> {
            if(villager.isAIFilterEnabled("smelt_modded"))
            {
                //Check if there is a smelting recipe for this
                if(FurnaceRecipes.instance().getSmeltingList().containsKey(p))
                {
                    ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(p);
                    if(!stack.isEmpty())
                    {
                        //Check if the result is an ingot and itself is either an ore or an ingot
                        if((TektopiaAddons.oreItems.contains(p.getItem()) || TektopiaAddons.ingotItems.contains(p.getItem())))
                        {
                            if(TektopiaAddons.smithIngotPriority.containsKey(stack.getItem()))
                            {
                                return TektopiaAddons.smithIngotPriority.get(stack.getItem());
                            }
                            return 1;
                        }
                    }
                    return 0;
                }
            }
            if (p.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE) && villager.isAIFilterEnabled("smelt_iron")) {
                return 3;
            } else {
                return p.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE) && villager.isAIFilterEnabled("smelt_gold") ? 2 : 0;
            }
        };
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    protected Predicate<ItemStack> isDeliverable() {
        return (p) -> craftSetAnvil.stream().anyMatch((e) -> ItemStack.areItemsEqual(e.getProduct(), p) || TektopiaAddons.ingotItems.contains(p.getItem()));
    }
}
