package com.sushiy.tektopiaaddons.magistuarmory6;

import com.magistuarmory.client.renderer.PatternsHandler;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class PatternHandlerWrapper
{
    PatternsHandler.Cache cache;

    public PatternHandlerWrapper()
    {
        this.cache = new PatternsHandler.Cache("textures/models/armor", "surcoat", "surcoat");
    }

    public ResourceLocation getBannerPattern(String id, List<BannerPattern> patternList, List<EnumDyeColor> colorList) {
        return cache.getResourcePattern(id, patternList,colorList);
    }
}
