package de.becker_dd.bennet.bungeefortune;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {
  @EventHandler
  public void onProxyPing(ProxyPingEvent event) {
    if((boolean)ConfigProviderSingelton.getConfig().get("appendFortune", true)) {
      event.getResponse().getDescriptionComponent().addExtra("\n" + ConfigProviderSingelton.getFortune());
    } else {
      event.getResponse().setDescriptionComponent(new TextComponent(ConfigProviderSingelton.getFortune()));
    }

    if(ConfigProviderSingelton.getIcon().isPresent()) {
      event.getResponse().setFavicon(ConfigProviderSingelton.getIcon().get());
    }
  }
}
