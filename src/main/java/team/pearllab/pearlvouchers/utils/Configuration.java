package team.pearllab.pearlvouchers.utils;

import org.simpleyaml.configuration.file.YamlFile;
import team.pearllab.pearlvouchers.PearlVouchers;
import team.pearllab.pearlvouchers.lang.Language;

public class Configuration {

    private PearlVouchers plugin;
    public Language language;

    private YamlFile configFile;

    public Configuration(PearlVouchers plugin ) {
        this.plugin = plugin;

        loadConfigFile();

    }

    /**
     * The loadConfigFile function loads the configuration file from the plugin's data folder.
     * If it does not exist, it will create a new one using the default config file in src/main/resources.
     * If it does exist, it will load the file.
     */
    private void loadConfigFile() {
        configFile = new YamlFile("plugins/PearlVouchers/configuration.yml");
        try {
            if(!configFile.exists()) {
                plugin.saveResource("configuration.yml", false);
            }
            configFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.language = Language.withValue(configFile.getString("language"));
    }

}
