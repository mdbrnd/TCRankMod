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

    public static void readConfig(boolean resetToDefault) throws IOException {
        // If config doesn't exist, create it with default values
        Gson gson = new Gson();

        File configFile = getConfigFile();
        if (!configFile.exists()) {
            configFile.createNewFile();
            Files.writeString(configFile.toPath(), gson.toJson(new Config())); // new Config() = hardcoded default values
        }

        // If resetToDefault is true, overwrite the config file with default values
        if (resetToDefault) {
            Files.writeString(configFile.toPath(), gson.toJson(new Config()));
        }

        configFile = getConfigFile();
        // Read json
        String json = Files.readString(configFile.toPath());
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
}
