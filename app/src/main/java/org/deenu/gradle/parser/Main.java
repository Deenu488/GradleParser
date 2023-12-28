package org.deenu.gradle.parser;

import java.io.File;
import org.deenu.gradle.parser.exception.ParserFailedException;
import org.deenu.gradle.script.GradleBuildScript;
import org.deenu.gradle.script.GradleSettingsScript;

public class Main {

  public static void main(String[] args) {
    try {

      File buildGradle = new File("/storage/emulated/0/test/build.gradle");
      GradleBuildScript gradleBuildScript = new GradleBuildScript(buildGradle);
      System.out.println("Plugins: " + gradleBuildScript.getPlugins());
      System.out.println("Repositories: " + gradleBuildScript.getRepositories());
      System.out.println(
          "BuildScriptRepositories: " + gradleBuildScript.getBuildScriptRepositories());
      System.out.println(
          "AllProjectsRepositories: " + gradleBuildScript.getAllProjectsRepositories());

      buildGradle = new File("/storage/emulated/0/test/settings.gradle");

      GradleSettingsScript gradleSettingsScript = new GradleSettingsScript(buildGradle);
      System.out.println("rootProjectName: " + gradleSettingsScript.getRootProjectName());
      System.out.println("Includes: " + gradleSettingsScript.getIncludes());

    } catch (Exception e) {
      System.out.println(new ParserFailedException(e.getMessage()));
    }
  }
}
