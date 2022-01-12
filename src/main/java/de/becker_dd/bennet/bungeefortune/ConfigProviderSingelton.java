package de.becker_dd.bennet.bungeefortune;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigProviderSingelton {

  private Configuration applicationConfig;
  private String path;

  private List<String> fortunes;
  private List<Favicon> icons;

  private final SecureRandom random;

  public ConfigProviderSingelton() {
    random = new SecureRandom();
  }

  private void update(){
    fortunes = new ArrayList<>();
    icons = new ArrayList<>();

    fortunes.add("");

    try {
      for (String path : ConfigProviderSingelton.getConfig().get("fortunePaths",
          List.of(""))) {
        parseFile(path);
      }
      for (String path : ConfigProviderSingelton.getConfig().get("iconPaths",
          List.of(""))) {
        walkImageDir(path);
      }
    } catch (IOException e) {
      //ignore
    }
  }
  private void parseFile(String fileName) throws IOException {
    File fortuneFile = new File(fileName);
    FileReader fr = new FileReader(fortuneFile);
    BufferedReader br = new BufferedReader(fr);

    String line;
    while((line=br.readLine()) != null) {
      if(!line.equalsIgnoreCase("%")){
        String f = fortunes.get(fortunes.size() - 1) + " " + line.replace('\t', ' ').trim();
        fortunes.set(fortunes.size() - 1, f);
      } else {
        fortunes.add("");
      }
    }
    fr.close();
  }

  @SneakyThrows
  private void walkImageDir(String dir) {
    Set<String> fileList = new HashSet<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
      for (Path path : stream) {
        if (!Files.isDirectory(path)) {
          fileList.add(path.toString());
        } else {
          walkImageDir(path.toString());
        }
      }
    }
    List<Favicon> i = fileList.stream().map(s -> {
      try {
        return Favicon.create(ImageIO.read(new File(s)));
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList());

    icons.addAll(i);
  }


  private static final ConfigProviderSingelton instance = new ConfigProviderSingelton();

  public static Configuration getConfig() {
    if (instance.applicationConfig == null) {
      throw new RuntimeException("config not initialized");
    }
    return instance.applicationConfig;
  }

  public static Configuration setConfig(Configuration config) {
    instance.applicationConfig = config;
    instance.update();
    return instance.applicationConfig;
  }

  public static void setPath(String path){
    instance.path = path;
  }

  @SneakyThrows
  public static void reload() {
    Configuration configuration = ConfigurationProvider
        .getProvider(YamlConfiguration.class)
        .load(new File(instance.path));
    setConfig(configuration);
    instance.update();
  }

  public static String getFortune() {
    int r_idx = Math.abs(instance.random.nextInt()) % instance.fortunes.size();

    while(instance.fortunes.get(r_idx).length() > (int)ConfigProviderSingelton
        .getConfig().get("maxFortuneLength", 50)) {
      r_idx = Math.abs(instance.random.nextInt()) % instance.fortunes.size();
    }

    return instance.fortunes.get(r_idx);
  }

  public static Optional<Favicon> getIcon(){
    Optional<Favicon> ret;
    if(instance.icons.size() > 0) {
      int i_idx = Math.abs(instance.random.nextInt()) % instance.icons.size();

      return Optional.ofNullable(instance.icons.get(i_idx));
    }
    return Optional.empty();
  }

}
