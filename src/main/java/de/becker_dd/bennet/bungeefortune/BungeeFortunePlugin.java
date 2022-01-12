package de.becker_dd.bennet.bungeefortune;

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeFortunePlugin extends Plugin {
  @Override
  public void onEnable() {
    // You should not put an enable message in your plugin.
    // BungeeCord already does so
    getLogger().info("Yay! It loads!");

    Configuration configuration = null;
    try {
      configuration = ConfigurationProvider
          .getProvider(YamlConfiguration.class)
          .load(new File(getDataFolder(), "config.yml"));
    } catch (IOException e) {
      configuration = new Configuration();

      configuration.set("fortunePaths", List.of("/usr/share/games/fortunes/fortunes"));
      configuration.set("appendFortune", true);
      configuration.set("maxFortuneLength", 50);
      configuration.set("iconPaths", List.of(getDataFolder().getAbsolutePath() + "/images"));

      try {
        if (!getDataFolder().exists()) {
          //noinspection ResultOfMethodCallIgnored
          getDataFolder().mkdir();
        }
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      //e.printStackTrace();
    }

    ConfigProviderSingelton.setConfig(configuration);
    ConfigProviderSingelton.setPath(getDataFolder() + "/config.yml");

    //System.out.println("Application config info " + configuration.toString());

    getProxy().getPluginManager().registerListener(this, new PingListener());
    getProxy().getPluginManager().registerCommand(this, new FortuneCommand("fortune"));
  }
}
