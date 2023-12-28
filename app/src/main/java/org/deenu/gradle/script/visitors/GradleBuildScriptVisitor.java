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
  private boolean inBuildscriptRepositories = false;
  private List<Repository> buildscriptRepositories = new ArrayList<>();

  private int buildscriptLastLineNumber = -1;
  private String buildscriptConfigurationName;
  private boolean inBuildscript = false;

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

  public int getBuildScriptRepositoriesLastLineNumber() {
    return buildscriptRepositoriesLastLineNumber;
  }

  public List<Repository> getBuildScriptRepositories() {
    return buildscriptRepositories;
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
          if (expressionText != null && inPlugins) {
            plugins.add(new Plugin(expressionText));
          }
          if (expressionText != null
              && repositoriesConfigurationName != null
              && inRepositories
              && !inPlugins
              && !inBuildscript) {
            repositories.add(new Repository(repositoriesConfigurationName, expressionText));
          }

          if (expressionText != null
              && buildscriptRepositoriesConfigurationName != null
              && inBuildscriptRepositories
              && !inPlugins) {
            buildscriptRepositories.add(
                new Repository(buildscriptRepositoriesConfigurationName, expressionText));
          }
        }
      }
    }
    super.visitArgumentlistExpression(argumentListExpression);
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) {
    if (inPlugins) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();
    } else if (inRepositories && !inPlugins && !inBuildscript) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();
    } else if (inBuildscriptRepositories && !inPlugins) {
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
      inBuildscript = false;
    }

    if (lineNumber > buildscriptRepositoriesLastLineNumber) {
      inBuildscriptRepositories = false;
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
      inBuildscript = true;
    }

    if (inBuildscript && inRepositories) {
      buildscriptRepositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inBuildscriptRepositories = true;
    }

    if (inRepositories && !inPlugins && !inBuildscript) {
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

    if (inBuildscriptRepositories && !inPlugins) {
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

    if ((inPlugins) && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      pluginsConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      pluginsConfigurationName = null;
    } else if ((inRepositories && !inPlugins && !inBuildscript)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      repositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      repositoriesConfigurationName = null;
    } else if ((inBuildscriptRepositories && !inPlugins)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      buildscriptRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      buildscriptRepositoriesConfigurationName = null;
    } else {
      super.visitMethodCallExpression(methodCallExpression);
    }
  }
}
