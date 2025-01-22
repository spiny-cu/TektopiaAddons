package com.sushiy.tektopiaaddons.mixin;

import com.sushiy.tektopiaaddons.ConfigHandler;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.tangotek.tektopia.TekVillager;
import net.tangotek.tektopia.commands.VillageCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TekVillager.class, remap = false)
public class TekVillagerMixin {

    @Inject(method = "onServerStarting", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onServerStartingInject(FMLServerStartingEvent evt, CallbackInfo ci, VillageCommands vc, World world) {
        //world.getGameRules().addGameRule("villagerItems", ConfigHandler., GameRules.ValueType.BOOLEAN_VALUE);
        //world.getGameRules().addGameRule("villagerSkillRate", String.valueOf(ConfigHandler.VILLAGER_SKILLRATE), GameRules.ValueType.NUMERICAL_VALUE);
        //world.getGameRules().addGameRule("villageRadius", String.valueOf(ConfigHandler.VILLAGE_RADIUS), GameRules.ValueType.NUMERICAL_VALUE);
        world.getGameRules().addGameRule("villagerPenPercent", String.valueOf(ConfigHandler.VILLAGE_ANIMALPEN_SIZE_PERCENTAGE_MULTIPLIER), GameRules.ValueType.NUMERICAL_VALUE);
    }
}
