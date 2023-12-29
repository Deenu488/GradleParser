package org.deenu.gradle.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.deenu.gradle.models.FlatDir;
import org.deenu.gradle.models.Include;
import org.deenu.gradle.models.Plugin;
import org.deenu.gradle.models.Repository;
import org.deenu.gradle.parser.exception.UnsupportedFileException;
import org.deenu.gradle.script.visitors.GradleScriptVisitor;

public class GradleScript {

  private File gradleFile;
  private AstBuilder astBuilder;
  private List<ASTNode> aSTNodes;
  private String scriptContents;
  private GradleScriptVisitor gradleScriptVisitor;

  public GradleScript(File gradleFile) throws Exception, FileNotFoundException {
    this.gradleFile = gradleFile;
    if (gradleFile == null || !gradleFile.exists()) {
      throw new FileNotFoundException("Failed to get '.gradle' or '.gradle.kts' file.");
    } else if (gradleFile != null) {
      String fileName = gradleFile.getName();
      String[] fileExtensions = new String[] {".gradle", ".gradle.kts"};

      if (fileName != null) {
        if (!fileName.endsWith(fileExtensions[0]) && !fileName.endsWith(fileExtensions[1])) {
          throw new UnsupportedFileException("Unsupported file: " + gradleFile.getAbsolutePath());
        }
      }
    }

    this.scriptContents = new String(Files.readAllBytes(this.gradleFile.toPath()));
    init(scriptContents, gradleFile);
  }

  private void init(String script, File gradleFile) throws Exception {
    if (script == null || script.isEmpty()) {
      throw new Exception("Failed to get gradle script from file: " + gradleFile.getAbsolutePath());
    }
    this.astBuilder = new AstBuilder();
    this.aSTNodes = getASTNodes();
  }

  public File getGradleFile() {
    return this.gradleFile;
  }

  public String getGradleFileName() {
    return this.gradleFile.getName();
  }

  public boolean isGradleBuildFile() {
    return getGradleFileName().startsWith("build.gradle");
  }

  public boolean isGradleSettingsFile() {
    return getGradleFileName().startsWith("settings.gradle");
  }

  private List<ASTNode> getASTNodes() throws Exception {
    return this.astBuilder.buildFromString(scriptContents);
  }

  public List<Plugin> getPlugins() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getPlugins();
  }

  public List<Repository> getRepositories() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getRepositories();
  }

  public List<Repository> getBuildScriptRepositories() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getBuildScriptRepositories();
  }

  public List<Repository> getAllProjectsRepositories() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getAllProjectsRepositories();
  }

  public List<FlatDir> getAllProjectsRepositoriesFlatDirDirs() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getAllProjectsRepositoriesFlatDirDirs();
  }

  public List<Repository> getPluginManagementRepositories() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getPluginManagementRepositories();
  }

  public List<Repository> getDependencyResolutionManagementRepositories() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getDependencyResolutionManagementRepositories();
  }

  public List<FlatDir> getDependencyResolutionManagementRepositoriesFlatDirDirs() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getDependencyResolutionManagementRepositoriesFlatDirDirs();
  }

  public String getRootProjectName() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getRootProjectName();
  }

  public List<Include> getIncludes() {
    this.gradleScriptVisitor = new GradleScriptVisitor();
    walkScript(gradleScriptVisitor);
    return this.gradleScriptVisitor.getIncludes();
  }

  private void walkScript(GroovyCodeVisitor visitor) {
    for (ASTNode astNode : aSTNodes) {
      astNode.visit(visitor);
    }
  }
}
