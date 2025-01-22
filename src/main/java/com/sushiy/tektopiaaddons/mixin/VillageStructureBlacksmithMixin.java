package com.sushiy.tektopiaaddons.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.tangotek.tektopia.ProfessionType;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.entities.crafting.Recipe;
import net.tangotek.tektopia.structures.VillageStructure;
import net.tangotek.tektopia.structures.VillageStructureBlacksmith;
import net.tangotek.tektopia.structures.VillageStructureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(value = VillageStructureBlacksmith.class, remap = false)
public abstract class VillageStructureBlacksmithMixin extends VillageStructure {


    protected VillageStructureBlacksmithMixin(World world, Village v, EntityItemFrame itemFrame, VillageStructureType t, String name) {
        super(world, v, itemFrame, t, name);
    }
/* NOT SURE THIS IS POSSIBLE?
    @Unique
    private static List<EntityArmorStand> tektopiaAddons$armorStands;

    @Unique
    private static List<Recipe> tektopiaAddons$armorStandRecipes;

    @Inject(method = "scanSpecialBlock", at = @At(value = "INVOKE", target = "Lnet/tangotek/tektopia/structures/VillageStructure;scanSpecialBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    protected void scanSpecialBlockInject(BlockPos pos, Block block, CallbackInfo ci)
    {
        tektopiaAddons$armorStands = this.world.getEntitiesWithinAABB(EntityArmorStand.class, aabb);
        tektopiaAddons$armorStandRecipes = tektopiaAddons$getArmorRequestedRecipes();
    }

    @Unique
    public List<Recipe> tektopiaAddons$GetArmorStandRecipes()
    {
        if(tektopiaAddons$armorStandRecipes == null)
        {
            tektopiaAddons$armorStandRecipes = tektopiaAddons$getArmorRequestedRecipes();
        }
        return tektopiaAddons$armorStandRecipes;
    }

    @Unique
    public List<Recipe> tektopiaAddons$getArmorRequestedRecipes()
    {
        HashMap<Item, Recipe> map = new HashMap<>();

        for(IRecipe recipe : ForgeRegistries.RECIPES.getValuesCollection())
        {
            //if this is not a shaped recipe or this is already in the map, skip
            if(!(recipe instanceof ShapedRecipes) || map.containsKey(recipe.getRecipeOutput().getItem()))
                continue;

            for(EntityArmorStand armorStand : tektopiaAddons$armorStands)
            {
                for(ItemStack stack : armorStand.getArmorInventoryList())
                {
                    Recipe tekRecipe = tektopiaAddons$TryGetTekRecipeFromStack(stack, recipe);
                    if(tekRecipe !=null)
                        map.put(stack.getItem(), tekRecipe);
                }

                ItemStack stack = armorStand.getHeldItemMainhand();

                Recipe tekRecipe = tektopiaAddons$TryGetTekRecipeFromStack(stack, recipe);
                if(tekRecipe !=null)
                    map.put(stack.getItem(), tekRecipe);

                stack = armorStand.getHeldItemOffhand();

                tekRecipe = tektopiaAddons$TryGetTekRecipeFromStack(stack, recipe);
                if(tekRecipe !=null)
                map.put(stack.getItem(), tekRecipe);

            }
        }
        return new ArrayList<>(map.values());
    }

    @Unique
    Recipe tektopiaAddons$TryGetTekRecipeFromStack(ItemStack itemStack, IRecipe recipe)
    {
        if(ItemStack.areItemsEqual(recipe.getRecipeOutput(), itemStack))
        {
            List<ItemStack> ingredients = new ArrayList();
            for(Ingredient i : recipe.getIngredients())
            {
                for(ItemStack matchingStack : i.getMatchingStacks())
                {
                    ingredients.add(i.getMatchingStacks()[0]);
                    break;
                }
            }
            return new Recipe(ProfessionType.BLACKSMITH, "craft_from_armorstand", 2, recipe.getRecipeOutput(), ingredients, 1, 1, (v) -> v.getSkillLerp(ProfessionType.BLACKSMITH, 6, 2), 1);
        }
        return null;
    }
 */
}
