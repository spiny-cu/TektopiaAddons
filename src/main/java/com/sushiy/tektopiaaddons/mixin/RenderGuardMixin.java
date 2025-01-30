package com.sushiy.tektopiaaddons.mixin;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
import com.sushiy.tektopiaaddons.LayerVillagerArmorFixed;
import com.sushiy.tektopiaaddons.TektopiaAddons;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.tangotek.tektopia.client.RenderGuard;
import net.tangotek.tektopia.client.RenderVillager;
import net.tangotek.tektopia.entities.EntityGuard;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderGuard.class)
public abstract class RenderGuardMixin<T extends EntityGuard> extends RenderVillagerMixin<T> {

    public RenderGuardMixin(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Mutable
    @Unique
    protected @Final ModelCraftStudio tektopiaAddons$maleModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructorTail(RenderManager manager, CallbackInfo ci)
    {
        tektopiaAddons$maleModel = new ModelCraftStudio("tektopiaaddons", "guard_body", 128, 64);
        addLayer(new LayerVillagerArmorFixed(this));
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        mainModel = tektopiaAddons$maleModel;
        super.tektopiaAddons$doRender(entity, x, y, z, entityYaw, partialTicks, false);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private void updateArmor(CSModelRenderer modelRenderer, EntityGuard entityGuard)
    {
        if (modelRenderer.boxName.startsWith("Capt"))
        {
            modelRenderer.showModel = entityGuard.isCaptain() && !entityGuard.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
        }

        if (modelRenderer.childModels != null) {
            for(ModelRenderer child : modelRenderer.childModels) {
                this.updateArmor((CSModelRenderer)child, entityGuard);
            }
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    protected void preRenderCallback(EntityGuard entityGuard, float partialTickTime) {
        //this.mainModel = tektopiaAddons$maleModel;
        ModelCraftStudio model = (ModelCraftStudio)this.getMainModel();

        for(CSModelRenderer parent : model.getParentBlocks()) {
            this.updateArmor(parent, entityGuard);
        }

    }
}
