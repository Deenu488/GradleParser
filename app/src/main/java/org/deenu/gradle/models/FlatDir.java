package org.deenu.gradle.models;

import java.util.ArrayList;
import java.util.List;

public class FlatDir {

  private String flatdir;
  private List<String> flatdirs;

  public FlatDir(String flatdir) {
    this.flatdir = flatdir;
    this.flatdirs = new ArrayList<>();
    this.flatdirs.add(flatdir);
  }

  public FlatDir(List<String> flatdirs) {
    this.flatdirs = new ArrayList<>(flatdirs);
  }

  public List<String> getFlatDirs() {
    return flatdirs;
  }

  @Override
  public String toString() {
    return flatdir;
  }
}
