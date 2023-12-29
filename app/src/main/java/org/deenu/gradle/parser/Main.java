package org.deenu.gradle.parser;

import java.io.File;
import org.deenu.gradle.parser.exception.ParserFailedException;
import org.deenu.gradle.script.GradleScript;

public class Main {

  public static void main(String[] args) {
    try {

      File buildGradle = new File("/storage/emulated/0/test/build.gradle");
      GradleScript gradleScript = new GradleScript(buildGradle);

      System.out.println();
      System.out.println("GradleFile: " + gradleScript.getGradleFile());
      System.out.println();
      System.out.println("GradleFileName: " + gradleScript.getGradleFileName());
      System.out.println();
      System.out.println("IsGradleBuildFile: " + gradleScript.isGradleBuildFile());
      System.out.println();
      System.out.println("IsGradleSettingsFile: " + gradleScript.isGradleSettingsFile());
      System.out.println();
      System.out.println("Plugins: " + gradleScript.getPlugins());
      System.out.println();
      System.out.println("Repositories: " + gradleScript.getRepositories());
      System.out.println();
      System.out.println("BuildScriptRepositories: " + gradleScript.getBuildScriptRepositories());
      System.out.println();
      System.out.println("AllProjectsRepositories: " + gradleScript.getAllProjectsRepositories());
      System.out.println();
      System.out.println(
          "AllProjectsRepositoriesFlatDirDirs: "
              + gradleScript.getAllProjectsRepositoriesFlatDirDirs());
      System.out.println();
      System.out.println(
          "PluginManagementRepositories: " + gradleScript.getPluginManagementRepositories());
      System.out.println();
      System.out.println(
          "DependencyResolutionManagementRepositories: "
              + gradleScript.getDependencyResolutionManagementRepositories());
      System.out.println();
      System.out.println(
          "DependencyResolutionManagementRepositoriesFlatDirDirs: "
              + gradleScript.getDependencyResolutionManagementRepositoriesFlatDirDirs());
      System.out.println();
      System.out.println("rootProjectName: " + gradleScript.getRootProjectName());
      System.out.println();
      System.out.println("Include: " + gradleScript.getIncludes());
      System.out.println();
      System.out.println("Dependencies: " + gradleScript.getDependencies());
      System.out.println();
      System.out.println("BuildScriptDependencies: " + gradleScript.getBuildScriptDependencies());
      System.out.println();

    } catch (Exception e) {
      System.out.println(new ParserFailedException(e.getMessage()));
    }
  }
}
