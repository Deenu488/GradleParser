package org.deenu.gradle.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.deenu.gradle.models.Plugin;
import org.deenu.gradle.models.Repository;
import org.deenu.gradle.script.visitors.GradleBuildScriptVisitor;

public class GradleBuildScript {

  private File buildGradle;
  private AstBuilder astBuilder;
  private List<ASTNode> aSTNodes;
  private String scriptContents;
  private GradleBuildScriptVisitor gradleBuildScriptVisitor;

  public GradleBuildScript(File buildGradle) throws Exception, FileNotFoundException {
    this.buildGradle = buildGradle;
    if (buildGradle == null || !buildGradle.exists()) {
      throw new FileNotFoundException("Failed to get 'build.gradle' or 'build.gradle.kts' file.");
    }
    this.scriptContents = new String(Files.readAllBytes(buildGradle.toPath()));
    init(scriptContents);
  }

  private void init(String script) throws Exception {
    if (script == null || script.isEmpty()) {
      throw new Exception(
          "Failed to get gradle script from file: " + buildGradle.getAbsolutePath());
    }
    this.astBuilder = new AstBuilder();
    this.aSTNodes = getASTNodes();
    this.gradleBuildScriptVisitor = new GradleBuildScriptVisitor();
  }

  public File getGradleBuildFile() {
    return buildGradle;
  }

  private List<ASTNode> getASTNodes() throws Exception {
    return this.astBuilder.buildFromString(scriptContents);
  }

  public List<Plugin> getPlugins() {
    walkScript(gradleBuildScriptVisitor);
    return this.gradleBuildScriptVisitor.getPlugins();
  }

  public List<Repository> getRepositories() {
    walkScript(gradleBuildScriptVisitor);
    return this.gradleBuildScriptVisitor.getRepositories();
  }

  public List<Repository> getBuildScriptRepositories() {
    walkScript(gradleBuildScriptVisitor);
    return this.gradleBuildScriptVisitor.getBuildScriptRepositories();
  }

  private void walkScript(GroovyCodeVisitor visitor) {
    for (ASTNode astNode : aSTNodes) {
      astNode.visit(visitor);
    }
  }
}
