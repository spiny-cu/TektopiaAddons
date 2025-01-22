package com.sushiy.tektopiaaddons.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.tangotek.tektopia.ItemTagType;
import net.tangotek.tektopia.ModItems;
import net.tangotek.tektopia.ProfessionType;
import net.tangotek.tektopia.entities.EntityFarmer;
import net.tangotek.tektopia.entities.EntityGuard;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.function.Function;

@Mixin(value = EntityGuard.class, remap = false)
public abstract class EntityGuardMixin extends EntityVillagerTek
{
    @Mutable
    @Unique
    private static @Final DataParameter<Boolean> EQUIP_AUTOCHANGE_ARMOR;
    @Unique
    @Mutable
    private static @Final DataParameter<Boolean> EQUIP_AUTOCHANGE_WEAPON;

    public EntityGuardMixin(World worldIn, ProfessionType profType, int roleMask) {
        super(worldIn, profType, roleMask);
    }

    @Inject(method = "entityInit", at = @At("TAIL"))
    protected void entityInit(CallbackInfo ci) {
        this.registerAIFilter("equip_autochange.armor", EQUIP_AUTOCHANGE_ARMOR);
        this.registerAIFilter("equip_autochange.weapon", EQUIP_AUTOCHANGE_WEAPON);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void staticBlock(CallbackInfo ci) {
        EQUIP_AUTOCHANGE_ARMOR = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
        EQUIP_AUTOCHANGE_WEAPON = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);

    }

    /**
     * @author Sushiy
     * @reason needs to take into account the autoequip filters
     */
    @Overwrite
    public void equipBestGear() {

        EntityGuard guard = (EntityGuard) (Object)this;
        if(isAIFilterEnabled("equip_autochange.armor"))
        {
            this.equipBestGear(EntityEquipmentSlot.CHEST, getBestArmor(guard, EntityEquipmentSlot.CHEST));
            this.equipBestGear(EntityEquipmentSlot.LEGS, getBestArmor(guard, EntityEquipmentSlot.LEGS));
            this.equipBestGear(EntityEquipmentSlot.FEET, getBestArmor(guard, EntityEquipmentSlot.FEET));
            this.equipBestGear(EntityEquipmentSlot.HEAD, getBestArmor(guard, EntityEquipmentSlot.HEAD));
        }
        if(isAIFilterEnabled("equip_autochange.weapon")) {
            this.equipBestGear(EntityEquipmentSlot.MAINHAND, getBestWeapon(guard));
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        EntityGuard guard = (EntityGuard) (Object)this;
        if(itemStack.getItem() instanceof ItemArmor)
        {
            ItemArmor armor = (ItemArmor) itemStack.getItem();
            ItemStack equippedArmor = guard.getItemStackFromSlot(armor.getEquipmentSlot());
            player.setHeldItem(hand, ItemStack.EMPTY);
            setItemStackToSlot(armor.getEquipmentSlot(), itemStack);
            if(!equippedArmor.isEmpty())
            {
                player.setHeldItem(hand, equippedArmor);
            }
            setAIFilter("equip_autochange.armor", false);
            return true;
        }
        if(itemStack.getItem() instanceof ItemSword)
        {
            ItemSword newWeapon = (ItemSword) itemStack.getItem();
            ItemStack oldWeapon = guard.getHeldItemMainhand();
            player.setHeldItem(hand, ItemStack.EMPTY);
            setHeldItem(EnumHand.MAIN_HAND, itemStack);
            if(!oldWeapon.isEmpty())
            {
                player.setHeldItem(hand, oldWeapon);
            }
            setAIFilter("equip_autochange.weapon", false);
            return true;
        }
        return super.processInteract(player, hand);
    }

    /**
     * @author Sushiy
     * @reason always prefer current item if autochange equipment is blocked
     */
    @Overwrite
    public static Function<ItemStack, Integer> getBestWeapon(EntityGuard guard) {
        return (p) -> {
            if(!guard.isAIFilterEnabled("equip_autochange.weapon"))
                return p == guard.getHeldItemMainhand() ? 100 : -1;

            if (p.getItem() instanceof ItemSword) {
                ItemSword sword = (ItemSword)p.getItem();
                if (p.isItemEnchanted() && !guard.isAIFilterEnabled("equip_enchanted_sword")) {
                    return -1;
                } else if (sword.getToolMaterialName().equals(Item.ToolMaterial.DIAMOND.name()) && !guard.isAIFilterEnabled("equip_diamond_sword")) {
                    return -1;
                } else if (sword.getToolMaterialName().equals(Item.ToolMaterial.IRON.name()) && !guard.isAIFilterEnabled("equip_iron_sword")) {
                    return -1;
                } else {
                    int score = (int)sword.getAttackDamage();
                    score = (int)((float)score + EnchantmentHelper.getModifierForCreature(p, EnumCreatureAttribute.UNDEFINED));
                    ++score;
                    score *= 10;
                    if (ModItems.isTaggedItem(p, ItemTagType.VILLAGER)) {
                        ++score;
                    }

                    return score;
                }
            } else {
                return -1;
            }
        };
    }

    /**
     * @author Sushiy
     * @reason always prefer current item if autochange equipment is blocked
     */
    @Overwrite
    public static Function<ItemStack, Integer> getBestArmor(EntityGuard guard, EntityEquipmentSlot slot) {
        return (p) -> {
            if(!guard.isAIFilterEnabled("equip_autochange.armor"))
                return p == guard.getItemStackFromSlot(slot) ? 100 : -1;
            if (p.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor)p.getItem();
                if (armor.armorType == slot) {
                    if (p.isItemEnchanted() && !guard.isAIFilterEnabled("equip_enchanted_armor")) {
                        return -1;
                    }

                    if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.DIAMOND && !guard.isAIFilterEnabled("equip_diamond_armor")) {
                        return -1;
                    }

                    if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.IRON && !guard.isAIFilterEnabled("equip_iron_armor")) {
                        return -1;
                    }

                    if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && !guard.isAIFilterEnabled("equip_leather_armor")) {
                        return -1;
                    }

                    int score = armor.getArmorMaterial().getDamageReductionAmount(armor.armorType);
                    score += EnchantmentHelper.getEnchantmentModifierDamage(Arrays.asList(p), DamageSource.GENERIC);
                    return score;
                }
            }

            return -1;
        };
    }
}
