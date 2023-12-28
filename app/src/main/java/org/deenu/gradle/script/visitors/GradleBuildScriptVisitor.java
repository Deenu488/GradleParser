package org.deenu.gradle.script.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.deenu.gradle.models.Plugin;
import org.deenu.gradle.models.Repository;

public class GradleBuildScriptVisitor extends CodeVisitorSupport {

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

  @Override
  public void visitArgumentlistExpression(ArgumentListExpression argumentListExpression) {

    List<Expression> expressions = argumentListExpression.getExpressions();
    if (expressions != null) {
      if ((expressions.size() == 1) && (expressions.get(0) instanceof ConstantExpression)) {
        ConstantExpression constantExpression = (ConstantExpression) expressions.get(0);

        if (constantExpression != null) {
          int lineNumber = constantExpression.getLineNumber();
          String expressionText = constantExpression.getText();

          if (expressionText != null && inPlugins && !inBuildScript && !inAllProjects) {
            plugins.add(new Plugin(expressionText));
          }

          if (expressionText != null
              && repositoriesConfigurationName != null
              && inRepositories
              && !inPlugins
              && !inBuildScript
              && !inAllProjects) {
            repositories.add(new Repository(repositoriesConfigurationName, expressionText));
          }

          if (expressionText != null
              && buildscriptRepositoriesConfigurationName != null
              && inBuildScriptRepositories
              && !inAllProjectsRepositories
              && !inPlugins) {
            buildscriptRepositories.add(
                new Repository(buildscriptRepositoriesConfigurationName, expressionText));
          }

          if (expressionText != null
              && allprojectsRepositoriesConfigurationName != null
              && inAllProjectsRepositories
              && !inBuildScriptRepositories
              && !inPlugins) {
            allprojectsRepositories.add(
                new Repository(allprojectsRepositoriesConfigurationName, expressionText));
          }
        }
      }
    }
    super.visitArgumentlistExpression(argumentListExpression);
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) {

    if (inPlugins && !inBuildScript && !inAllProjects) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inRepositories && !inPlugins && !inBuildScript && !inAllProjects) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inBuildScriptRepositories && !inAllProjectsRepositories && !inPlugins) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inAllProjectsRepositories && !inBuildScriptRepositories && !inPlugins) {
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

    if (inBuildScript && inRepositories) {
      buildscriptRepositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inBuildScriptRepositories = true;
    }

    if (methodName.equals("allprojects")) {
      allprojectsLastLineNumber = methodCallExpression.getLastLineNumber();
      inAllProjects = true;
    }

    if (inAllProjects && inRepositories) {
      allprojectsRepositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inAllProjectsRepositories = true;
    }

    if (inRepositories && !inPlugins && !inBuildScript && !inAllProjects) {
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

    if (inBuildScriptRepositories && !inAllProjectsRepositories && !inPlugins) {
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

    if (inAllProjectsRepositories && !inBuildScriptRepositories && !inPlugins) {
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

    if ((inPlugins && !inBuildScript && !inAllProjects)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      pluginsConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      pluginsConfigurationName = null;

    } else if ((inRepositories && !inPlugins && !inBuildScript && !inAllProjects)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      repositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      repositoriesConfigurationName = null;

    } else if ((inBuildScriptRepositories && !inAllProjectsRepositories && !inPlugins)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      buildscriptRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      buildscriptRepositoriesConfigurationName = null;

    } else if ((inAllProjectsRepositories && !inBuildScriptRepositories && !inPlugins)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      allprojectsRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      allprojectsRepositoriesConfigurationName = null;

    } else {
      super.visitMethodCallExpression(methodCallExpression);
    }
  }
}
