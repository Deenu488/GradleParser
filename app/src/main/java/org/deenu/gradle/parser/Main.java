package org.deenu.gradle.parser;

import java.io.File;
import org.deenu.gradle.parser.exception.ParserFailedException;
import org.deenu.gradle.script.GradleBuildScript;
import org.deenu.gradle.script.GradleSettingsScript;

public class Main {

	public static void main(String[] args) {
		try {
			// Parse from build.gradle
			File buildGradle = new File("/storage/emulated/0/test/build.gradle");
			GradleBuildScript gradleBuildScript = new GradleBuildScript(buildGradle);
			System.out.println(gradleBuildScript.getPlugins());

			// Parse from build.gradle.kts
			File buildGradleKts = new File("/storage/emulated/0/test/build.gradle.kts");
			GradleBuildScript gradleBuildScriptKts = new GradleBuildScript(buildGradleKts);
			System.out.println(gradleBuildScriptKts.getPlugins());

			// Parse from settings.gradle
			File settingsGradle = new File("/storage/emulated/0/test/settings.gradle");
			GradleSettingsScript gradleSettingsScript = new GradleSettingsScript(settingsGradle);
			System.out.println(gradleSettingsScript.getIncludes());

			// Parse from settings.gradle.kts
			File settingsGradleKts = new File("/storage/emulated/0/test/settings.gradle.kts");
			GradleSettingsScript gradleSettingsScriptKts = new GradleSettingsScript(settingsGradleKts);
			System.out.println(gradleSettingsScriptKts.getIncludes());
		} catch (Exception e) {
			System.out.println(new ParserFailedException(e.getMessage()));
		}
	}
}