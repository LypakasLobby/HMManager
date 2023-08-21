package com.lypaka.hmmanager;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("hmmanager")
public class HMManager {

    public static final String MOD_ID = "hmmanager";
    public static final String MOD_NAME = "HMManager";
    public static final Logger logger = LogManager.getLogger("HMManager");
    public static BasicConfigManager configManager;

    public HMManager() throws IOException, ObjectMappingException {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/hmmanager"));
        String[] files = new String[]{"hmmanager.conf"};
        configManager = new BasicConfigManager(files, dir, HMManager.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        ConfigGetters.load();

    }

}
