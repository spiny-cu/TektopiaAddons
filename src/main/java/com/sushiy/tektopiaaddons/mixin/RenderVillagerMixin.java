package com.sushiy.tektopiaaddons.mixin;

import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.tangotek.tektopia.client.RenderVillager;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import org.spongepowered.asm.mixin.*;

@Mixin(value = RenderVillager.class)
public abstract class RenderVillagerMixin<T extends EntityVillagerTek> extends RenderLiving<T> {
    @Shadow(remap = false)
    protected @Final ModelCraftStudio maleModel;
    @Shadow(remap = false)
    protected @Final ModelCraftStudio femaleModel;
    public RenderVillagerMixin(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Unique
    public void tektopiaAddons$doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks, boolean replaceMainModel) {
        if(replaceMainModel)
        {
            if (!entity.isMale() && this.femaleModel != null) {
                this.mainModel = this.femaleModel;
            } else {
                this.mainModel = this.maleModel;
            }

        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
