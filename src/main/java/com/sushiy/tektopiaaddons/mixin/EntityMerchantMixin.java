package com.sushiy.tektopiaaddons.mixin;

import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.sushiy.tektopiaaddons.EntityAITradeAtDeliveryChest;
import com.sushiy.tektopiaaddons.TektopiaAddons;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tangotek.tektopia.ProfessionType;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.entities.EntityMerchant;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import net.tangotek.tektopia.entities.ai.EntityAIGenericMove;
import net.tangotek.tektopia.entities.ai.EntityAIVisitMerchantStall;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityMerchant.class, remap = false)
public abstract class EntityMerchantMixin extends EntityVillagerTek {
    @Shadow protected static AnimationHandler animHandler;
    public EntityMerchantMixin(World worldIn, ProfessionType profType, int roleMask) {
        super(worldIn, profType, roleMask);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void checkStuck() {
        if (this.firstCheck.distanceSq(this.village.getCenter()) > (double)40.0f && this.firstCheck.distanceSq(this.getPos()) < (double)20.0F) {
            this.debugOut("Merchant failed to find a way to the village.");
            this.setDead();
        }

    }

    @Shadow
    @Override
    public AnimationHandler getAnimationHandler() {
       return null;
    }

    @Shadow private BlockPos firstCheck;

    @Shadow public abstract BlockPos getPos();

    @Inject(method = "initEntityAI", at = @At("TAIL"))
    void initEntityAIInject(CallbackInfo ci)
    {
        this.addTask(50, new EntityAITradeAtDeliveryChest((EntityMerchant) (Object)this));
    }
}
