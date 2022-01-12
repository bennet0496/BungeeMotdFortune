package de.becker_dd.bennet.bungeefortune;

import java.io.File;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

class FortuneCommand extends Command {
  public FortuneCommand(String name) {
    super(name);
  }

  public FortuneCommand(String name, String permission, String... aliases) {
    super(name, permission, aliases);
  }

  @Override
  public void execute(CommandSender commandSender, String[] strings) {
    if(commandSender.hasPermission("fortune.reload")){
      if(strings[0].equalsIgnoreCase("reload")) {
        commandSender.sendMessage(new TextComponent("Reloading Fortune Config"));
        ConfigProviderSingelton.reload();
      }
    }
  }
}
