package net.starfall.tcrm.util;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.starfall.tcrm.TCRankingMod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigManager {
    private static String getConfigFilePath() {
        return FabricLoader.getInstance().getConfigDir().toString() + "\\" + TCRankingMod.MOD_ID + ".json";
    }

    public static File getConfigFile() {
        TCRankingMod.LOGGER.info("Reading config file from path " + getConfigFilePath());
        return new File(getConfigFilePath());
    }

    public static void readConfigIntoMemory() {
        // If config doesn't exist, create it with default values
        Gson gson = new Gson();

        File configFile = getConfigFile();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                Files.writeString(configFile.toPath(), gson.toJson(new Config())); // new Config() = hardcoded default values

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        configFile = getConfigFile();
        // Read json
        String json = "";
        try {
            json = Files.readString(configFile.toPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TCRankingMod.LOGGER.info("Read config file: " + json);

        // Parse json
        TCRankingMod.config = gson.fromJson(json, Config.class);
    }

    public static void saveConfig() {
        // Save config
        File configFile = getConfigFile();
        Gson gson = new Gson();
        String json = gson.toJson(TCRankingMod.config);
        try {
            Files.writeString(configFile.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
            // Log IO Error
            TCRankingMod.LOGGER.error("Error while saving config file: " + e.getMessage());
        }

        // Log
        TCRankingMod.LOGGER.info("Saved config file: " + json);
    }

    public static void resetConfig() {
        // Reset config
        TCRankingMod.config = new Config();
        saveConfig();
    }
}
