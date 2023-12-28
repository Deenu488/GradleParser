package org.deenu.gradle.models;

import java.util.ArrayList;
import java.util.List;

public class Include {

  private String include;
  private List<String> includes;

  public Include(String include) {
    this.include = include;
    this.includes = new ArrayList<>();
    this.includes.add(include);
  }

  public Include(List<String> includes) {
    this.includes = new ArrayList<>(includes);
  }

  public List<String> getIncludes() {
    return includes;
  }

  @Override
  public String toString() {
    return include;
  }
}
