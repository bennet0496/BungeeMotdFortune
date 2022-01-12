package de.becker_dd.bennet.bungeefortune;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfig {
  @Getter @Setter
  private String[] fortunePaths;

  @Getter @Setter
  private Boolean appendFortune;

  @Getter @Setter
  private Integer maxFortuneLength;

  @Getter @Setter
  private String[] iconPaths;

}
