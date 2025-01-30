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
import net.tangotek.tektopia.structures.VillageStructureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityMerchant.class)
public abstract class EntityMerchantMixin extends EntityVillagerTek {
    @Shadow(remap = false) protected static AnimationHandler animHandler;
    public EntityMerchantMixin(World worldIn, ProfessionType profType, int roleMask) {
        super(worldIn, profType, roleMask);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private void checkStuck()
    {
        if (this.firstCheck.distanceSq(this.village.getCenter()) > (double)100.0f && this.firstCheck.distanceSq(new BlockPos(this)) < (double)20.0F) {
            this.setDead();
        }
    }

    @Shadow(remap = false)
    @Override
    public AnimationHandler getAnimationHandler() {
       return null;
    }

    @Shadow(remap = false) private BlockPos firstCheck;

    @Inject(method = "initEntityAI", at = @At("TAIL"))
    void initEntityAIInject(CallbackInfo ci)
    {
        this.addTask(49, new EntityAITradeAtDeliveryChest((EntityMerchant) (Object)this));
    }
}
