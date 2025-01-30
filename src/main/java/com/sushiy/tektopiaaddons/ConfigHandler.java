package com.sushiy.tektopiaaddons;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static float MODDED_FOOD_HUNGER_MULTIPLIER = 1.5f;
    public static float MODDED_FOOD_HAPPINESS_MULTIPLIER = 1.5f;

    public static String[] MODDED_FOOD_CUSTOM_STATS_DEFAULT = {"harvestcraft:pepperoniitem, 5, -1", "minecraft:stone, 1,-10"};
    public static String[] MODDED_FOOD_CUSTOM_STATS;

    public static String[] MODDED_METAL_SMITH_PRIORITY_DEFAULT = {"Copper:1", "Bronze:3", "Silver:4", "Steel:5"};
    public static String[] MODDED_METAL_SMITH_PRIORITY;


    public static String[] MODDED_ORE_REGROW_CHANCE_DEFAULT = {"Stone:30","Coal:30", "Iron:25", "Gold:5", "Redstone:12", "Lapis:3", "Diamond:3", "Copper:30", "Silver:15", "Tin:10"};
    public static String[] MODDED_ORE_REGROW_CHANCE;


    public static int VILLAGER_SKILLRATE = 100;
    public static int VILLAGE_RADIUS = 100;
    public static int VILLAGE_ANIMALPEN_SIZE_PERCENTAGE_MULTIPLIER = 100;
    public static boolean NEW_PLAYERS_RECEIVE_STARTERBOOK = false;

    public static void init(File file)
    {
        config = new Configuration(file);

        String category;
        category = "Base Mod Config";
        config.addCustomCategoryComment(category, "Settings related to the base mod");
        //VILLAGER_SKILLRATE = config.getInt("Rate at which villagers gain skills", category, 100, 0, 10000, "default 100");
        //VILLAGE_RADIUS = config.getInt("Radius of village ", category, 100, 0, 300, "!!Change with caution!!.default 100");
        VILLAGE_ANIMALPEN_SIZE_PERCENTAGE_MULTIPLIER = Math.round(config.getFloat("Multiplier for animals in a pen", category, 1, 0, 10, "default 1")* 100);
        NEW_PLAYERS_RECEIVE_STARTERBOOK = config.getBoolean("Should new players get a starterbook", category, false, "");


        category = "Food";
        config.addCustomCategoryComment(category, "Settings related to food consumption");
        MODDED_FOOD_HUNGER_MULTIPLIER = config.getFloat("Modded food hunger multiplier", category, 1.5f, 1.0f, 5.0f, "Multiplier for modded food hunger value");
        MODDED_FOOD_HAPPINESS_MULTIPLIER = config.getFloat("Modded food happiness multiplier", category, 1.0f, 1.0f, 5.0f, "Multiplier for modded food happiness value");
        MODDED_FOOD_CUSTOM_STATS = config.getStringList("Modded food custom stats", category, MODDED_FOOD_CUSTOM_STATS_DEFAULT, "List of custom stats for fooditems. you can also add additional food items here that didn't get picked up automatically. minecraft values are:" +
                "Items.APPLE, 12, -1\n" +
                "Items.BAKED_POTATO, 35, 1\n" +
                "Items.BEETROOT, 7, -1\n" +
                "Items.BEETROOT_SOUP, 50, 6, returnBowl\n" +
                "Items.BREAD, 55, 4\n" +
                "Items.CAKE, 7, 25\n" +
                "Items.CARROT, 12, -1\n" +
                "Items.COOKED_BEEF, 70, 14\n" +
                "Items.COOKED_CHICKEN, 60, 6\n" +
                "Items.COOKED_MUTTON, 66, 4\n" +
                "Items.COOKED_PORKCHOP, 70, 14\n" +
                "Items.COOKIE, 5, 16\n" +
                "Items.GOLDEN_CARROT, 70, 20\n" +
                "Items.MELON, 6, 3\n" +
                "Items.MUSHROOM_STEW, 50, 4, returnBowl\n" +
                "Items.POTATO, 7, -1\n" +
                "Items.PUMPKIN_PIE, 35, 18");


        category = "Smithing";
        MODDED_METAL_SMITH_PRIORITY = config.getStringList("Modded metal ingot priority", category, MODDED_METAL_SMITH_PRIORITY_DEFAULT, "Format: \"<Metalname>:<integer value>\" Metal should be capitalized. Higher numbers are crafted first by smith. Iron is priority 3. ");

        category = "Mining";
        MODDED_ORE_REGROW_CHANCE = config.getStringList("Modded ores regrow chance", category, MODDED_ORE_REGROW_CHANCE_DEFAULT, "Format: \"<oreName>:<integer value>\". Higher number is more likely to be found");

        config.save();
    }

    public static void registerConfig(FMLPreInitializationEvent event)
    {
        TektopiaAddons.config = new File(event.getModConfigurationDirectory() + "/" + TektopiaAddons.MODID);
        TektopiaAddons.config.mkdirs();
        init(new File(TektopiaAddons.config.getPath(), TektopiaAddons.MODID + ".cfg"));
    }
}
