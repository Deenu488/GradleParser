package org.deenu.gradle.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.deenu.gradle.models.Include;
import org.deenu.gradle.script.visitors.GradleSettingsScriptVisitor;

public class GradleSettingsScript {

  private File settingsGradle;
  private AstBuilder astBuilder;
  private List<ASTNode> aSTNodes;
  private String scriptContents;

  private GradleSettingsScriptVisitor gradleSettingsScriptVisitor;

  public GradleSettingsScript(File settingsGradle) throws Exception, FileNotFoundException {
    this.settingsGradle = settingsGradle;
    if (settingsGradle == null || !settingsGradle.exists()) {
      throw new FileNotFoundException(
          "Failed to get 'settings.gradle' or 'settings.gradle.kts' file.");
    }
    this.scriptContents = new String(Files.readAllBytes(settingsGradle.toPath()));
    init(scriptContents);
  }

  private void init(String script) throws Exception {
    if (script == null || script.isEmpty()) {
      throw new Exception(
          "Failed to get gradle script from file: " + settingsGradle.getAbsolutePath());
    }
    this.astBuilder = new AstBuilder();
    this.aSTNodes = getASTNodes();
    this.gradleSettingsScriptVisitor = new GradleSettingsScriptVisitor();
  }

  public File getGradleSettingsFile() {
    return settingsGradle;
  }

  private List<ASTNode> getASTNodes() throws Exception {
    return this.astBuilder.buildFromString(scriptContents);
  }

  public List<Include> getIncludes() {
    walkScript(gradleSettingsScriptVisitor);
    return this.gradleSettingsScriptVisitor.getIncludes();
  }

  private void walkScript(GroovyCodeVisitor visitor) {
    for (ASTNode astNode : aSTNodes) {
      astNode.visit(visitor);
    }
  }
}
