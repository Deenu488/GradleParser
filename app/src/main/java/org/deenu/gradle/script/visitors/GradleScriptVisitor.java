package org.deenu.gradle.script.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.deenu.gradle.models.FlatDir;
import org.deenu.gradle.models.Include;
import org.deenu.gradle.models.Plugin;
import org.deenu.gradle.models.Repository;

public class GradleScriptVisitor extends CodeVisitorSupport {

  private Stack<Boolean> blockStatementStack = new Stack<>();

  private int pluginsLastLineNumber = -1;
  private String pluginsConfigurationName;
  private boolean inPlugins = false;
  private List<Plugin> plugins = new ArrayList<>();

  private int repositoriesLastLineNumber = -1;
  private String repositoriesConfigurationName;
  private boolean inRepositories = false;
  private List<Repository> repositories = new ArrayList<>();

  private int buildscriptRepositoriesLastLineNumber = -1;
  private String buildscriptRepositoriesConfigurationName;
  private boolean inBuildScriptRepositories = false;
  private List<Repository> buildscriptRepositories = new ArrayList<>();

  private int buildscriptLastLineNumber = -1;
  private String buildscriptConfigurationName;
  private boolean inBuildScript = false;

  private int allprojectsLastLineNumber = -1;
  private String allprojectsConfigurationName;
  private boolean inAllProjects = false;

  private int allprojectsRepositoriesLastLineNumber = -1;
  private String allprojectsRepositoriesConfigurationName;
  private boolean inAllProjectsRepositories = false;
  private List<Repository> allprojectsRepositories = new ArrayList<>();

  private String rootProjectName;
  private int includeLastLineNumber = -1;
  private String includeConfigurationName;
  private boolean inInclude = false;
  private List<Include> includes = new ArrayList<>();

  private int flatDirLastLineNumber = -1;
  private String flatDirConfigurationName;
  private boolean inFlatDir = false;

  private int allprojectsrepositoriesflatDirLastLineNumber = -1;
  private String allprojectsrepositoriesflatDirConfigurationName;
  private boolean inAllProjectsRepositoriesFlatDir = false;

  private int flatDirdirsLastLineNumber = -1;
  private String flatDirdirsConfigurationName;
  private boolean inFlatDirDirs = false;

  private int allprojectsrepositoriesflatDirdirsLastLineNumber = -1;
  private String allprojectsrepositoriesflatDirdirsConfigurationName;
  private boolean inAllProjectsRepositoriesFlatDirDirs = false;
  private List<FlatDir> allprojectsrepositoriesflatDirDirs = new ArrayList<>();

  public int getPluginsLastLineNumber() {
    return pluginsLastLineNumber;
  }

  public List<Plugin> getPlugins() {
    return plugins;
  }

  public int getRepositoriesLastLineNumber() {
    return repositoriesLastLineNumber;
  }

  public List<Repository> getRepositories() {
    return repositories;
  }

  public int getBuildScriptLastLineNumber() {
    return buildscriptLastLineNumber;
  }

  public int getBuildScriptRepositoriesLastLineNumber() {
    return buildscriptRepositoriesLastLineNumber;
  }

  public List<Repository> getBuildScriptRepositories() {
    return buildscriptRepositories;
  }

  public int getAllProjectsLastLineNumber() {
    return allprojectsLastLineNumber;
  }

  public int getAllProjectsRepositoriesLastLineNumber() {
    return allprojectsRepositoriesLastLineNumber;
  }

  public List<Repository> getAllProjectsRepositories() {
    return allprojectsRepositories;
  }

  public int getFlatDirLastLineNumber() {
    return flatDirLastLineNumber;
  }

  public int getFlatDirDirsLastLineNumber() {
    return flatDirdirsLastLineNumber;
  }

  public int getAllProjectsRepositoriesFlatDirLastLineNumber() {
    return allprojectsrepositoriesflatDirLastLineNumber;
  }

  public int getAllProjectsRepositoriesFlatDirDirsLastLineNumber() {
    return allprojectsrepositoriesflatDirdirsLastLineNumber;
  }

  public List<FlatDir> getAllProjectsRepositoriesFlatDirDirs() {
    return allprojectsrepositoriesflatDirDirs;
  }

  public String getRootProjectName() {
    return rootProjectName;
  }

  public int getIncludesLastLineNumber() {
    return includeLastLineNumber;
  }

  public List<Include> getIncludes() {
    return includes;
  }

  @Override
  public void visitArgumentlistExpression(ArgumentListExpression argumentListExpression) {

    List<Expression> expressions = argumentListExpression.getExpressions();
    if (expressions != null) {

      if ((expressions.size() == 1) && (expressions.get(0) instanceof ConstantExpression)) {
        ConstantExpression constantExpression = (ConstantExpression) expressions.get(0);

        if (constantExpression != null) {
          int lineNumber = constantExpression.getLineNumber();
          String expressionText = constantExpression.getText();
          if (expressionText != null
              && inPlugins
              && !inBuildScript
              && !inAllProjects
              && !inFlatDir) {
            plugins.add(new Plugin(expressionText));
          }

          if (expressionText != null
              && repositoriesConfigurationName != null
              && inRepositories
              && !inPlugins
              && !inBuildScript
              && !inAllProjects
              && !inFlatDir) {
            repositories.add(new Repository(repositoriesConfigurationName, expressionText));
          }

          if (expressionText != null
              && buildscriptRepositoriesConfigurationName != null
              && inBuildScriptRepositories
              && !inAllProjectsRepositories
              && !inPlugins
              && !inFlatDir) {
            buildscriptRepositories.add(
                new Repository(buildscriptRepositoriesConfigurationName, expressionText));
          }

          if (expressionText != null
              && allprojectsRepositoriesConfigurationName != null
              && inAllProjectsRepositories
              && !inBuildScriptRepositories
              && !inPlugins
              && !inFlatDir) {
            allprojectsRepositories.add(
                new Repository(allprojectsRepositoriesConfigurationName, expressionText));
          }

          if (expressionText != null && inInclude) {
            includes.add(new Include(expressionText));
          }
        }
      }
    }
    super.visitArgumentlistExpression(argumentListExpression);
  }

  @Override
  public void visitBinaryExpression(BinaryExpression binaryExpression) {
    Expression leftExpression = binaryExpression.getLeftExpression();
    Expression rightExpression = binaryExpression.getRightExpression();

    if (leftExpression != null && rightExpression != null) {
      String leftExpressionText = leftExpression.getText();
      String rightExpressionText = rightExpression.getText();
      if (leftExpressionText != null && rightExpressionText != null) {
        if (leftExpressionText.equals("rootProject.name")) {
          rootProjectName = rightExpressionText;
        }
      }
    }

    super.visitBinaryExpression(binaryExpression);
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) {

    if (inPlugins && !inBuildScript && !inAllProjects && !inFlatDir) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inRepositories && !inPlugins && !inBuildScript && !inAllProjects && !inFlatDir) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inBuildScriptRepositories
        && !inAllProjectsRepositories
        && !inPlugins
        && !inFlatDir) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inAllProjectsRepositories
        && !inBuildScriptRepositories
        && !inPlugins
        && !inFlatDir) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inAllProjectsRepositories
        && inAllProjectsRepositoriesFlatDirDirs
        && !inBuildScriptRepositories
        && !inPlugins) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else {
      super.visitBlockStatement(blockStatement);
    }
  }

  @Override
  public void visitMethodCallExpression(MethodCallExpression methodCallExpression) {
    int lineNumber = methodCallExpression.getLineNumber();
    String methodName = methodCallExpression.getMethodAsString();

    if (lineNumber > pluginsLastLineNumber) {
      inPlugins = false;
    }

    if (lineNumber > repositoriesLastLineNumber) {
      inRepositories = false;
    }

    if (lineNumber > buildscriptLastLineNumber) {
      inBuildScript = false;
    }

    if (lineNumber > buildscriptRepositoriesLastLineNumber) {
      inBuildScriptRepositories = false;
    }

    if (lineNumber > allprojectsLastLineNumber) {
      inAllProjects = false;
    }

    if (lineNumber > allprojectsRepositoriesLastLineNumber) {
      inAllProjectsRepositories = false;
    }

    if (lineNumber > includeLastLineNumber) {
      inInclude = false;
    }

    if (lineNumber > flatDirLastLineNumber) {
      inFlatDir = false;
    }

    if (lineNumber > allprojectsrepositoriesflatDirLastLineNumber) {
      inAllProjectsRepositoriesFlatDir = false;
    }

    if (lineNumber > flatDirdirsLastLineNumber) {
      inFlatDirDirs = false;
    }

    if (lineNumber > allprojectsrepositoriesflatDirdirsLastLineNumber) {
      inAllProjectsRepositoriesFlatDirDirs = false;
    }

    if (methodName.equals("plugins")) {
      pluginsLastLineNumber = methodCallExpression.getLastLineNumber();
      inPlugins = true;
    }

    if (methodName.equals("repositories")) {
      repositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inRepositories = true;
    }

    if (methodName.equals("buildscript")) {
      buildscriptLastLineNumber = methodCallExpression.getLastLineNumber();
      inBuildScript = true;
    }

    if (methodName.equals("allprojects")) {
      allprojectsLastLineNumber = methodCallExpression.getLastLineNumber();
      inAllProjects = true;
    }

    if (methodName.equals("include")) {
      includeLastLineNumber = methodCallExpression.getLastLineNumber();
      inInclude = true;
    }

    if (methodName.equals("flatDir")) {
      flatDirLastLineNumber = methodCallExpression.getLastLineNumber();
      inFlatDir = true;
    }

    if (methodName.equals("dirs")) {
      flatDirdirsLastLineNumber = methodCallExpression.getLastLineNumber();
      inFlatDirDirs = true;
    }

    if (inBuildScript && inRepositories) {
      buildscriptRepositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inBuildScriptRepositories = true;
    }

    if (inAllProjects && inRepositories) {
      allprojectsRepositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inAllProjectsRepositories = true;
    }

    if (inAllProjects && inRepositories && inFlatDir) {
      allprojectsrepositoriesflatDirLastLineNumber = methodCallExpression.getLastLineNumber();
      inAllProjectsRepositoriesFlatDir = true;
    }

    if (inAllProjects && inRepositories && inFlatDir && inFlatDirDirs) {
      allprojectsrepositoriesflatDirdirsLastLineNumber = methodCallExpression.getLastLineNumber();
      inAllProjectsRepositoriesFlatDirDirs = true;
    }

    if (inRepositories && !inPlugins && !inBuildScript && !inAllProjects && !inFlatDir) {
      if (methodName.equals("google")) {
        repositories.add(new Repository(methodName, "https://maven.google.com/"));
      }
      if (methodName.equals("mavenLocal")) {
        repositories.add(new Repository(methodName, ".m2/repository"));
      }
      if (methodName.equals("mavenCentral")) {
        repositories.add(new Repository(methodName, "https://repo.maven.apache.org/maven2/"));
      }
      if (methodName.equals("gradlePluginPortal")) {
        repositories.add(new Repository(methodName, "https://plugins.gradle.org/m2/"));
      }
    }

    if (inBuildScriptRepositories && !inAllProjectsRepositories && !inPlugins && !inFlatDir) {
      if (methodName.equals("google")) {
        buildscriptRepositories.add(new Repository(methodName, "https://maven.google.com/"));
      }
      if (methodName.equals("mavenLocal")) {
        buildscriptRepositories.add(new Repository(methodName, ".m2/repository"));
      }
      if (methodName.equals("mavenCentral")) {
        buildscriptRepositories.add(
            new Repository(methodName, "https://repo.maven.apache.org/maven2/"));
      }
      if (methodName.equals("gradlePluginPortal")) {
        buildscriptRepositories.add(new Repository(methodName, "https://plugins.gradle.org/m2/"));
      }
    }

    if (inAllProjectsRepositories && !inBuildScriptRepositories && !inPlugins && !inFlatDir) {
      if (methodName.equals("google")) {
        allprojectsRepositories.add(new Repository(methodName, "https://maven.google.com/"));
      }
      if (methodName.equals("mavenLocal")) {
        allprojectsRepositories.add(new Repository(methodName, ".m2/repository"));
      }
      if (methodName.equals("mavenCentral")) {
        allprojectsRepositories.add(
            new Repository(methodName, "https://repo.maven.apache.org/maven2/"));
      }
      if (methodName.equals("gradlePluginPortal")) {
        allprojectsRepositories.add(new Repository(methodName, "https://plugins.gradle.org/m2/"));
      }
    }

    if ((inAllProjectsRepositories
            && inAllProjectsRepositoriesFlatDirDirs
            && !inBuildScriptRepositories
            && !inPlugins)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      allprojectsrepositoriesflatDirdirsConfigurationName = methodName;

      Expression expression = methodCallExpression.getArguments();

      if (expression != null && expression instanceof TupleExpression) {
        TupleExpression tupleExpression = (TupleExpression) expression;
        if (tupleExpression != null) {
          for (Expression expr : tupleExpression.getExpressions()) {
            if (expr != null && expr instanceof ConstantExpression) {
              ConstantExpression constantExpression = (ConstantExpression) expr;
              if (constantExpression != null) {
                String constantExpressionText = constantExpression.getText();
                if (constantExpressionText != null) {
                  allprojectsrepositoriesflatDirDirs.add(new FlatDir(constantExpressionText));
                }
              }
            }
          }
        }
      }
    }

    if ((inPlugins && !inBuildScript && !inAllProjects && !inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      pluginsConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      pluginsConfigurationName = null;

    } else if ((inRepositories && !inPlugins && !inBuildScript && !inAllProjects && !inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      repositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      repositoriesConfigurationName = null;

    } else if ((inBuildScriptRepositories && !inAllProjectsRepositories && !inPlugins && inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      buildscriptRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      buildscriptRepositoriesConfigurationName = null;

    } else if ((inAllProjectsRepositories && !inBuildScriptRepositories && !inPlugins && !inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      allprojectsRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      allprojectsRepositoriesConfigurationName = null;

    } else if (inInclude) {
      includeConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      includeConfigurationName = null;

    } else {
      super.visitMethodCallExpression(methodCallExpression);
    }
  }
}
