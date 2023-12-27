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

public class GradleBuildScriptVisitor extends CodeVisitorSupport {

  private int pluginsLastLineNumber = -1;
  private boolean inPlugins = false;
  private List<Plugin> plugins = new ArrayList<>();
  private String pluginsConfigurationName;
  private Stack<Boolean> blockStatementStack = new Stack<>();

  public List<Plugin> getPlugins() {
    return plugins;
  }

  public int getPluginsLastLineNumber() {
    return pluginsLastLineNumber;
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
            Plugin plugin = new Plugin(expressionText);
            plugins.add(plugin);
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
    } else {
      super.visitBlockStatement(blockStatement);
    }
  }

  @Override
  public void visitMethodCallExpression(MethodCallExpression methodCallExpression) {
    int lineNumber = methodCallExpression.getLineNumber();
    if (lineNumber > pluginsLastLineNumber) {
      inPlugins = false;
    }
    String methodName = methodCallExpression.getMethodAsString();
    if (methodName.equals("plugins")) {
      pluginsLastLineNumber = methodCallExpression.getLastLineNumber();
      inPlugins = true;
    }
    if ((inPlugins) && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      pluginsConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      pluginsConfigurationName = null;
    } else {
      super.visitMethodCallExpression(methodCallExpression);
    }
  }
}
