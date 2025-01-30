package com.sushiy.tektopiaaddons.mixin;

import com.sushiy.tektopiaaddons.ConfigHandler;
import com.sushiy.tektopiaaddons.TektopiaAddons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.tangotek.tektopia.ModEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModEntities.class)
public abstract class ModEntititiesMixin {

    @Shadow(remap = false)
    private static void givePlayerStarterBook(EntityPlayer player) {
    }

    @Redirect(method = "onPlayerLoggedIn", at = @At(value = "INVOKE", target = "Lnet/tangotek/tektopia/ModEntities;givePlayerStarterBook(Lnet/minecraft/entity/player/EntityPlayer;)V"), remap = false)
    private static void givePlayerStarterBookRedirect(EntityPlayer e)
    {
        if(ConfigHandler.NEW_PLAYERS_RECEIVE_STARTERBOOK)
        {
            givePlayerStarterBook(e);
        }
    }
}
