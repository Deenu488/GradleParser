package org.deenu.gradle.models;

import java.util.HashMap;
import java.util.Map;

public class Repository {
  private String repositoryName;
  private String repositoryUrl;
  private Map<String, String> repositories;

  public Repository(String repositoryName, String repositoryUrl) {
    this.repositoryName = repositoryName;
    this.repositoryUrl = repositoryUrl;
    this.repositories = new HashMap<>();
    this.repositories.put(repositoryName, repositoryUrl);
  }

  public Repository(Map<String, String> repositories) {
    this.repositories = new HashMap<>(repositories);
  }

  public Map<String, String> getRepositories() {
    return repositories;
  }

  public String getRepositoryName() {
    return repositoryName;
  }

  public String getRepositoryUrl() {
    return repositoryUrl;
  }

  @Override
  public String toString() {
    return repositoryName + ":" + repositoryUrl;
  }
}
