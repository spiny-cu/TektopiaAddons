package com.sushiy.tektopiaaddons;

import com.google.common.collect.Maps;
import com.leviathanstudio.craftstudio.client.animation.ClientAnimationHandler;
import com.leviathanstudio.craftstudio.client.model.CSModelBox;
import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;

import java.util.Map;

import com.sushiy.tektopiaaddons.magistuarmory6.PatternHandlerWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.tangotek.tektopia.entities.EntityGuard;
import net.tangotek.tektopia.entities.EntityVillagerTek;

public class LayerVillagerArmorFixed implements LayerRenderer<EntityLivingBase>{
    private final RenderLivingBase<?> renderer;
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();
    private float alpha = 1.0F;
    private float colorR = 1.0F;
    private float colorG = 1.0F;
    private float colorB = 1.0F;

    protected ModelCraftStudio modelHead;
    protected ModelCraftStudio modelChest;
    protected ModelCraftStudio modelLegs;
    protected ModelCraftStudio modelFeet;
    protected ModelCraftStudio modelSurcoat;

    boolean noMagistuarmory6 = false;
    private PatternHandlerWrapper p = null;
    public LayerVillagerArmorFixed(RenderLivingBase<?> rendererIn) {
        this.renderer = rendererIn;
        modelHead = new ModelCraftStudio("tektopiaaddons", "armor_head", 64, 32);
        modelChest = new ModelCraftStudio("tektopiaaddons", "armor_chest", 64, 32);
        modelLegs = new ModelCraftStudio("tektopiaaddons", "armor_leg", 64, 32);
        modelFeet = new ModelCraftStudio("tektopiaaddons", "armor_feet", 64, 32);
        modelSurcoat = new ModelCraftStudio("tektopiaaddons", "armor_surcoat", 64, 32);
        FixArmTexturing();
        if(Loader.isModLoaded("magistuarmory"))
        {
            try {
                Class<?> clazz = Class.forName("com.magistuarmory.client.renderer.PatternsHandler");
            }
            catch (ClassNotFoundException e)
            {
                noMagistuarmory6 = true;
                TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " detected magistuarmory version without patternhandler");
                return;
            }

            p = new PatternHandlerWrapper();
        }
    }

    private void FixArmTexturing()
    {
        float textureWidth = 64;
        float textureHeight = 32;
        float f = 0.0F / textureWidth;
        float f1 = 0.0F / textureHeight;

        float modelScale = 0.0625F;
        CSModelRenderer rightUpperArm = null;
        CSModelRenderer leftUpperArm = null;
        CSModelRenderer rightLowerArm = null;
        CSModelRenderer leftLowerArm = null;
        for(CSModelRenderer block : modelChest.getParentBlocks())
        {
            rightUpperArm = FindSubmodelWithName("ArmorChestIronArmRight", block);
            leftUpperArm = FindSubmodelWithName("ArmorChestIronArmLeft", block);
            rightLowerArm = FindSubmodelWithName("ArmorChestIronArmRightLower", block);
            leftLowerArm = FindSubmodelWithName("ArmorChestIronArmLeftLower", block);
        }
        if(leftUpperArm!= null)
        {
            int texcoordU1 = 40;
            int texcoordU2 = 44;
            int texcoordV1 = 16;
            int texcoordV2 = 20;
            CSModelBox leftUpperArmBox = leftUpperArm.getCubeCSList().get(0);
            leftUpperArmBox.getQuadList()[2].vertexPositions[0].texturePositionX = (float)texcoordU2 / textureWidth - f;
            leftUpperArmBox.getQuadList()[2].vertexPositions[0].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            leftUpperArmBox.getQuadList()[2].vertexPositions[1].texturePositionX = (float)texcoordU1 / textureWidth + f;
            leftUpperArmBox.getQuadList()[2].vertexPositions[1].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            leftUpperArmBox.getQuadList()[2].vertexPositions[2].texturePositionX = (float)texcoordU1 / textureWidth + f;
            leftUpperArmBox.getQuadList()[2].vertexPositions[2].texturePositionY = (float)texcoordV2 / textureHeight - f1;
            leftUpperArmBox.getQuadList()[2].vertexPositions[3].texturePositionX = (float)texcoordU2 / textureWidth - f;
            leftUpperArmBox.getQuadList()[2].vertexPositions[3].texturePositionY = (float)texcoordV2 / textureHeight - f1;
        }
        if(rightUpperArm!= null)
        {
            int texcoordU1 = 40;
            int texcoordU2 = 44;
            int texcoordV1 = 16;
            int texcoordV2 = 20;
            CSModelBox rightUpperArmBox = rightUpperArm.getCubeCSList().get(0);
            rightUpperArmBox.getQuadList()[2].vertexPositions[0].texturePositionX = (float)texcoordU2 / textureWidth - f;
            rightUpperArmBox.getQuadList()[2].vertexPositions[0].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            rightUpperArmBox.getQuadList()[2].vertexPositions[1].texturePositionX = (float)texcoordU1 / textureWidth + f;
            rightUpperArmBox.getQuadList()[2].vertexPositions[1].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            rightUpperArmBox.getQuadList()[2].vertexPositions[2].texturePositionX = (float)texcoordU1 / textureWidth + f;
            rightUpperArmBox.getQuadList()[2].vertexPositions[2].texturePositionY = (float)texcoordV2 / textureHeight - f1;
            rightUpperArmBox.getQuadList()[2].vertexPositions[3].texturePositionX = (float)texcoordU2 / textureWidth - f;
            rightUpperArmBox.getQuadList()[2].vertexPositions[3].texturePositionY = (float)texcoordV2 / textureHeight - f1;
        }

        if(rightLowerArm != null)
        {
            int id = 3;
            CSModelBox rightLowerArmBox = rightLowerArm.getCubeCSList().get(0);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Found rightLower Arm for UV modification. Quads: " + rightLowerArmBox.getQuadList().length);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Right UV1 " + rightLowerArmBox.getQuadList()[id].vertexPositions[0].texturePositionX * textureWidth + "," + rightLowerArmBox.getQuadList()[id].vertexPositions[0].texturePositionY * textureHeight);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Right UV2 " + rightLowerArmBox.getQuadList()[id].vertexPositions[1].texturePositionX * textureWidth + "," + rightLowerArmBox.getQuadList()[id].vertexPositions[1].texturePositionY * textureHeight);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Right UV3 " + rightLowerArmBox.getQuadList()[id].vertexPositions[2].texturePositionX * textureWidth + "," + rightLowerArmBox.getQuadList()[id].vertexPositions[2].texturePositionY * textureHeight);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Right UV4 " + rightLowerArmBox.getQuadList()[id].vertexPositions[3].texturePositionX * textureWidth + "," + rightLowerArmBox.getQuadList()[id].vertexPositions[3].texturePositionY * textureHeight);

            int texcoordU1 = 48;
            int texcoordU2 = 52;
            int texcoordV1 = 16;
            int texcoordV2 = 20;
            rightLowerArmBox.getQuadList()[2].vertexPositions[0].texturePositionX = (float)texcoordU2 / textureWidth - f;
            rightLowerArmBox.getQuadList()[2].vertexPositions[0].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            rightLowerArmBox.getQuadList()[2].vertexPositions[1].texturePositionX = (float)texcoordU1 / textureWidth + f;
            rightLowerArmBox.getQuadList()[2].vertexPositions[1].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            rightLowerArmBox.getQuadList()[2].vertexPositions[2].texturePositionX = (float)texcoordU1 / textureWidth + f;
            rightLowerArmBox.getQuadList()[2].vertexPositions[2].texturePositionY = (float)texcoordV2 / textureHeight - f1;
            rightLowerArmBox.getQuadList()[2].vertexPositions[3].texturePositionX = (float)texcoordU2 / textureWidth - f;
            rightLowerArmBox.getQuadList()[2].vertexPositions[3].texturePositionY = (float)texcoordV2 / textureHeight - f1;


            texcoordU1 = 40;
            texcoordU2 = 44;
            texcoordV1 = 16;
            texcoordV2 = 20;
            rightLowerArmBox.getQuadList()[3].vertexPositions[0].texturePositionX = (float)texcoordU2 / textureWidth - f;
            rightLowerArmBox.getQuadList()[3].vertexPositions[0].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            rightLowerArmBox.getQuadList()[3].vertexPositions[1].texturePositionX = (float)texcoordU1 / textureWidth + f;
            rightLowerArmBox.getQuadList()[3].vertexPositions[1].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            rightLowerArmBox.getQuadList()[3].vertexPositions[2].texturePositionX = (float)texcoordU1 / textureWidth + f;
            rightLowerArmBox.getQuadList()[3].vertexPositions[2].texturePositionY = (float)texcoordV2 / textureHeight - f1;
            rightLowerArmBox.getQuadList()[3].vertexPositions[3].texturePositionX = (float)texcoordU2 / textureWidth - f;
            rightLowerArmBox.getQuadList()[3].vertexPositions[3].texturePositionY = (float)texcoordV2 / textureHeight - f1;
        }
        else
        {
            TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Not Found right lower Arm for UV modification");
        }
        if(leftLowerArm != null)
        {
            int id = 2;
            CSModelBox leftLowerArmBox = leftLowerArm.getCubeCSList().get(0);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Found left lower Arm for UV modification. Quads: " + leftLowerArmBox.getQuadList().length);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Left UV1 " + leftLowerArmBox.getQuadList()[id].vertexPositions[0].texturePositionX * textureWidth + "," + leftLowerArmBox.getQuadList()[id].vertexPositions[0].texturePositionY * textureHeight);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Left UV2 " + leftLowerArmBox.getQuadList()[id].vertexPositions[1].texturePositionX * textureWidth + "," + leftLowerArmBox.getQuadList()[id].vertexPositions[1].texturePositionY * textureHeight);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Left UV3 " + leftLowerArmBox.getQuadList()[id].vertexPositions[2].texturePositionX * textureWidth + "," + leftLowerArmBox.getQuadList()[id].vertexPositions[2].texturePositionY * textureHeight);
            //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Left UV4 " + leftLowerArmBox.getQuadList()[id].vertexPositions[3].texturePositionX * textureWidth + "," + leftLowerArmBox.getQuadList()[id].vertexPositions[3].texturePositionY * textureHeight);


            int texcoordU1 = 48;
            int texcoordU2 = 52;
            int texcoordV1 = 16;
            int texcoordV2 = 20;
            leftLowerArmBox.getQuadList()[2].vertexPositions[0].texturePositionX = (float)texcoordU2 / textureWidth - f;
            leftLowerArmBox.getQuadList()[2].vertexPositions[0].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            leftLowerArmBox.getQuadList()[2].vertexPositions[1].texturePositionX = (float)texcoordU1 / textureWidth + f;
            leftLowerArmBox.getQuadList()[2].vertexPositions[1].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            leftLowerArmBox.getQuadList()[2].vertexPositions[2].texturePositionX = (float)texcoordU1 / textureWidth + f;
            leftLowerArmBox.getQuadList()[2].vertexPositions[2].texturePositionY = (float)texcoordV2 / textureHeight - f1;
            leftLowerArmBox.getQuadList()[2].vertexPositions[3].texturePositionX = (float)texcoordU2 / textureWidth - f;
            leftLowerArmBox.getQuadList()[2].vertexPositions[3].texturePositionY = (float)texcoordV2 / textureHeight - f1;

            texcoordU1 = 40;
            texcoordU2 = 44;
            texcoordV1 = 16;
            texcoordV2 = 20;
            leftLowerArmBox.getQuadList()[3].vertexPositions[0].texturePositionX = (float)texcoordU2 / textureWidth - f;
            leftLowerArmBox.getQuadList()[3].vertexPositions[0].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            leftLowerArmBox.getQuadList()[3].vertexPositions[1].texturePositionX = (float)texcoordU1 / textureWidth + f;
            leftLowerArmBox.getQuadList()[3].vertexPositions[1].texturePositionY = (float)texcoordV1 / textureHeight + f1;
            leftLowerArmBox.getQuadList()[3].vertexPositions[2].texturePositionX = (float)texcoordU1 / textureWidth + f;
            leftLowerArmBox.getQuadList()[3].vertexPositions[2].texturePositionY = (float)texcoordV2 / textureHeight - f1;
            leftLowerArmBox.getQuadList()[3].vertexPositions[3].texturePositionX = (float)texcoordU2 / textureWidth - f;
            leftLowerArmBox.getQuadList()[3].vertexPositions[3].texturePositionY = (float)texcoordV2 / textureHeight - f1;
        }
        else
        {
            TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Not Found left lower Arm for UV modification");
        }
    }

    public CSModelRenderer FindSubmodelWithName(String name, CSModelRenderer block)
    {
        //TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " Checking block " + block.boxName);
        CSModelRenderer result = null;
        if(block.boxName.equals(name))
            result = block;
        else if(block.childModels != null)
        {
            for(ModelRenderer child : block.childModels)
            {
                if(child instanceof CSModelRenderer)
                    result =  FindSubmodelWithName(name, (CSModelRenderer) child);
                if(result != null)
                    break;
            }
        }
        return result;
    }

    public void doRenderLayer(EntityLivingBase entityVillager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        boolean showArmor = true;
        EntityVillagerTek tekVillager;
        try
        {
            tekVillager = (EntityVillagerTek)entityVillager;
            showArmor = !((EntityVillagerTek)entityVillager).isSleeping();
        }catch(ClassCastException e)
        {
            throw new RuntimeException(e);
        }
        if (showArmor) {
            renderArmorSlot(modelHead, (EntityGuard) tekVillager, EntityEquipmentSlot.HEAD, ageInTicks);
            renderArmorSlot(modelChest, (EntityGuard) tekVillager, EntityEquipmentSlot.CHEST, ageInTicks);
            renderArmorSlot(modelLegs, (EntityGuard) tekVillager, EntityEquipmentSlot.LEGS, ageInTicks);
            renderArmorSlot(modelFeet, (EntityGuard) tekVillager, EntityEquipmentSlot.FEET, ageInTicks);
            RenderEpicKnightSurcoat(modelSurcoat, (EntityGuard) tekVillager, ageInTicks);

        }

    }

    private final TileEntityBanner banner = new TileEntityBanner();
    void RenderEpicKnightSurcoat(ModelCraftStudio model, EntityGuard entityGuard, float ageInTicks)
    {
        if (!Loader.isModLoaded("magistuarmory") || noMagistuarmory6)
        {
            return;
        }
        ItemStack itemStack = entityGuard.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if(itemStack.isEmpty())
        {
            return;
        }
        if (itemStack.getItem() instanceof ItemArmor) {
            ItemArmor itemArmor = (ItemArmor) itemStack.getItem();

            if (itemStack.getSubCompound("BlockEntityTag") != null)
            {
                model.setModelAttributes(this.renderer.getMainModel());
                ClientAnimationHandler.performAnimationInModel(model.getParentBlocks(), entityGuard);
                this.banner.setItemValues(itemStack, true);
                this.renderer.bindTexture(p.getBannerPattern(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
                model.render();
            }
        }
    }

    private void renderArmorSlot(ModelCraftStudio model, EntityGuard entityGuard, EntityEquipmentSlot slot, float ageInTicks)
    {
        ItemStack itemStack = entityGuard.getItemStackFromSlot(slot);
        if(itemStack.isEmpty())
        {
            return;
        }
        if (itemStack.getItem() instanceof ItemArmor)
        {
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            if (itemArmor.getEquipmentSlot() == slot)
            {
                model.setModelAttributes(this.renderer.getMainModel());
                ClientAnimationHandler.performAnimationInModel(model.getParentBlocks(), entityGuard);
                this.renderer.bindTexture(this.getArmorResource(entityGuard, itemStack, slot, null));
                if (itemArmor.hasOverlay(itemStack)) // Allow this for anything, not only cloth
                {
                    int i = itemArmor.getColor(itemStack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float f1 = (float)(i >> 8 & 255) / 255.0F;
                    float f2 = (float)(i & 255) / 255.0F;
                    GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                    model.render();
                    this.renderer.bindTexture(this.getArmorResource(entityGuard, itemStack, slot, "overlay"));
                }
                {
                    GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                    //this.renderer.bindTexture(this.getDefaultArmorResource(slot));
                    model.render();
                }
                if (itemStack.hasEffect())
                {
                    renderEnchantedGlint(this.renderer, entityGuard, model, ageInTicks);
                }
            }

        }

    }

    public static void renderEnchantedGlint(RenderLivingBase<?> renderer, EntityLivingBase entity, ModelCraftStudio model, float ageInTicks)
    {
        float f = (float)entity.ticksExisted + ageInTicks;
        renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f1 = 0.5F;
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
            float f2 = 0.76F;
            GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334F;
            GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
            GlStateManager.rotate(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            model.render();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
    }


    //This gets the mesh of the armor item instead. won't work for us probably.
    protected ModelBiped getArmorModelHook(net.minecraft.entity.EntityLivingBase entity, net.minecraft.item.ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model)
    {
        return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    private ResourceLocation getDefaultArmorResource(EntityEquipmentSlot slot) {
        if(isLegSlot(slot))
            return new ResourceLocation("tektopiaaddons", "textures/entity/chainmail_layer_2.png");
        return new ResourceLocation("tektopiaaddons", "textures/entity/chainmail_layer_1.png");
    }
    private ResourceLocation getDefaultArmorResourceHead() {

        return new ResourceLocation("tektopiaaddons", "textures/entity/knight_layer_1.png");
    }

    public ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type)
    {
        ItemArmor item = (ItemArmor)stack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1)
        {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (isLegSlot(slot) ? 2 : 1), type == null ? "" : String.format("_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null)
        {
            TektopiaAddons.LOGGER.info(TektopiaAddons.MODID + " register armor texture " + s1);
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }

        return resourcelocation;
    }
    private boolean isLegSlot(EntityEquipmentSlot slotIn)
    {
        return slotIn == EntityEquipmentSlot.LEGS;
    }
}

