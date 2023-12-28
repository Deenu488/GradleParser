package org.deenu.gradle.parser;

import java.io.File;
import org.deenu.gradle.parser.exception.ParserFailedException;
import org.deenu.gradle.script.GradleScript;

public class Main {

  public static void main(String[] args) {
    try {

      File buildGradle = new File("/storage/emulated/0/test/build.gradle");
      GradleScript gradleScript = new GradleScript(buildGradle);

      System.out.println("GradleFile: " + gradleScript.getGradleFile());
      System.out.println("GradleFileName: " + gradleScript.getGradleFileName());
      System.out.println("IsGradleBuildFile: " + gradleScript.isGradleBuildFile());
      System.out.println("IsGradleSettingsFile: " + gradleScript.isGradleSettingsFile());
      System.out.println("Plugins: " + gradleScript.getPlugins());
      System.out.println("Repositories: " + gradleScript.getRepositories());
      System.out.println("BuildScriptRepositories: " + gradleScript.getBuildScriptRepositories());
      System.out.println("AllProjectsRepositories: " + gradleScript.getAllProjectsRepositories());
      System.out.println(
          "AllProjectsRepositoriesFlatDirs: " + gradleScript.getAllProjectsRepositoriesFlatDirs());
      System.out.println("rootProjectName: " + gradleScript.getRootProjectName());
      System.out.println("Include: " + gradleScript.getIncludes());

    } catch (Exception e) {
      System.out.println(new ParserFailedException(e.getMessage()));
    }
  }
}
