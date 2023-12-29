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

  private int pluginManagementLastLineNumber = -1;
  private String pluginManagementConfigurationName;
  private boolean inPluginManagement = false;

  private int pluginManagementRepositoriesLastLineNumber = -1;
  private String pluginManagementRepositoriesConfigurationName;
  private boolean inPluginManagementRepositories = false;
  private List<Repository> pluginManagementRepositories = new ArrayList<>();

private int dependencyResolutionManagementLastLineNumber = -1;
  private String dependencyResolutionManagementConfigurationName;
  private boolean inDependencyResolutionManagement = false;

  private int dependencyResolutionManagementRepositoriesLastLineNumber = -1;
  private String dependencyResolutionManagementRepositoriesConfigurationName;
  private boolean inDependencyResolutionManagementRepositories = false;
  private List<Repository> dependencyResolutionManagementRepositories = new ArrayList<>();


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

  public int getPluginManagementLastLineNumber() {
    return pluginManagementLastLineNumber;
  }

  public int getPluginManagementRepositoriesLastLineNumber() {
    return pluginManagementRepositoriesLastLineNumber;
  }

  public List<Repository> getPluginManagementRepositories() {
    return pluginManagementRepositories;
  }
  
  public int getDependencyResolutionManagementLastLineNumber() {
    return dependencyResolutionManagementLastLineNumber;
  }

  public int getDependencyResolutionManagementRepositoriesLastLineNumber() {
    return dependencyResolutionManagementRepositoriesLastLineNumber;
  }

  public List<Repository> getDependencyResolutionManagementRepositories() {
    return dependencyResolutionManagementRepositories;
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
			 && !inPluginManagement
			 && !inDependencyResolutionManagement              
			 ) {
            plugins.add(new Plugin(expressionText));
          }

          if (expressionText != null
              && repositoriesConfigurationName != null
              && inRepositories
          //    && !inPlugins
              && !inBuildScript
             && !inAllProjects
			  	  && !inPluginManagement
			  && !inDependencyResolutionManagement
              && !inFlatDir
			  ) {
            repositories.add(new Repository(repositoriesConfigurationName, expressionText));
          }
		  
		//  System.out.println(expressionText + " " + repositoriesConfigurationName + " " + inBuildScriptRepositories);
		  		  
          if (expressionText != null
              && repositoriesConfigurationName != null
              && inBuildScriptRepositories
              && !inAllProjectsRepositories
			  && !inPluginManagementRepositories
			  && !inDependencyResolutionManagementRepositories
        //      && !inPlugins
             && !inFlatDir
		 ) {		
			// System.out.println(expressionText);		  
            buildscriptRepositories.add(
                new Repository(repositoriesConfigurationName, expressionText));
          }

          if (expressionText != null
              && allprojectsRepositoriesConfigurationName != null
              && inAllProjectsRepositories
              && !inBuildScriptRepositories
			  && !inPluginManagementRepositories
			  && !inDependencyResolutionManagementRepositories
              && !inPlugins
              && !inFlatDir) {
            allprojectsRepositories.add(
                new Repository(allprojectsRepositoriesConfigurationName, expressionText));
          }

          if (expressionText != null
              && pluginManagementRepositoriesConfigurationName != null
              && inPluginManagementRepositories
              && !inBuildScriptRepositories
              && !inAllProjectsRepositories			
			  && !inDependencyResolutionManagementRepositories
              && !inPlugins
              && !inFlatDir) {
            pluginManagementRepositories.add(
                new Repository(pluginManagementRepositoriesConfigurationName, expressionText));
          }
		  
		  if (expressionText != null
              && dependencyResolutionManagementRepositoriesConfigurationName != null
              && inDependencyResolutionManagementRepositories
              && !inBuildScriptRepositories
              && !inAllProjectsRepositories			
			  && !inPluginManagementRepositories
              && !inPlugins
              && !inFlatDir) {
            dependencyResolutionManagementRepositories.add(
                new Repository(dependencyResolutionManagementRepositoriesConfigurationName, expressionText));
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

    if (inPlugins /* && !inBuildScript && !inAllProjects && !inPluginManagement && !inDependencyResolutionManagement && !inFlatDir */) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inRepositories /* && !inPlugins && !inBuildScript && !inAllProjects && !inPluginManagement && !inDependencyResolutionManagement && !inFlatDir*/ ) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();
	  
} else if (inBuildScriptRepositories
       /* && !inAllProjectsRepositories
		&& !inPluginManagementRepositories
		&& !inDependencyResolutionManagementRepositories
        && !inPlugins
        && !inFlatDir*/
		) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inAllProjectsRepositories
        && !inBuildScriptRepositories
		&& !inPluginManagementRepositories
		&& !inDependencyResolutionManagementRepositories
        && !inPlugins
        && !inFlatDir) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inAllProjectsRepositories
        && inAllProjectsRepositoriesFlatDirDirs
        && !inBuildScriptRepositories
		&& !inPluginManagementRepositories
		&& !inDependencyResolutionManagementRepositories
        && !inPlugins) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();

    } else if (inPluginManagementRepositories
        && !inBuildScriptRepositories
        && !inAllProjectsRepositories	
		&& !inDependencyResolutionManagementRepositories
        && !inPlugins
        && !inFlatDir) {
      blockStatementStack.push(true);
      super.visitBlockStatement(blockStatement);
      blockStatementStack.pop();
    
	 } else if (inDependencyResolutionManagementRepositories
        && !inBuildScriptRepositories
        && !inAllProjectsRepositories	
		&& !inPluginManagementRepositories
        && !inPlugins
        && !inFlatDir) {
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
	   	
	if (lineNumber > pluginManagementLastLineNumber) {
      inPluginManagement = false;
    }
	
	 if (lineNumber > dependencyResolutionManagementLastLineNumber) {
      inDependencyResolutionManagement = false;
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
//	  System.out.println(lineNumber +  " " + methodName + " " + inBuildScript);	  
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

    if (methodName.equals("pluginManagement")) {
      pluginManagementLastLineNumber = methodCallExpression.getLastLineNumber();
      inPluginManagement = true;
    }
	
	if (methodName.equals("dependencyResolutionManagement")) {
      dependencyResolutionManagementLastLineNumber = methodCallExpression.getLastLineNumber();
      inDependencyResolutionManagement = true;
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

    if (inPluginManagement && inRepositories) {
      pluginManagementRepositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inPluginManagementRepositories = true;
    }
	
	if (inDependencyResolutionManagement && inRepositories) {
      dependencyResolutionManagementRepositoriesLastLineNumber = methodCallExpression.getLastLineNumber();
      inDependencyResolutionManagementRepositories = true;	  
    }

    if (inRepositories
     //   && !inPlugins
        && !inBuildScript
        && !inAllProjects
        && !inPluginManagement
		&& !inDependencyResolutionManagement
        && !inFlatDir
		) {			
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

    if (inBuildScriptRepositories
      //  && !inAllProjectsRepositories
    //    && !inPluginManagementRepositories
	//	&& !inDependencyResolutionManagementRepositories
   //     && !inPlugins
        && !inFlatDir
		) {
		//	System.out.println(lineNumber +  " " + methodName + " " + inBuildScript + " " + inBuildScriptRepositories);	  
			
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

    if (inAllProjectsRepositories
        && !inBuildScriptRepositories
        && !inPluginManagementRepositories
		&& !inDependencyResolutionManagementRepositories
        && !inPlugins
        && !inFlatDir) {
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

    if (inPluginManagementRepositories
        && !inBuildScriptRepositories
        && !inAllProjectsRepositories
		&& !inDependencyResolutionManagementRepositories
        && !inPlugins
        && !inFlatDir) {
      if (methodName.equals("google")) {
        pluginManagementRepositories.add(new Repository(methodName, "https://maven.google.com/"));
      }
      if (methodName.equals("mavenLocal")) {
        pluginManagementRepositories.add(new Repository(methodName, ".m2/repository"));
      }
      if (methodName.equals("mavenCentral")) {
        pluginManagementRepositories.add(
            new Repository(methodName, "https://repo.maven.apache.org/maven2/"));
      }
      if (methodName.equals("gradlePluginPortal")) {
        pluginManagementRepositories.add(
            new Repository(methodName, "https://plugins.gradle.org/m2/"));
      }
    }
	
	if (inDependencyResolutionManagementRepositories
       // && !inBuildScriptRepositories
   //     && !inAllProjectsRepositories
	//	&& !inPluginManagementRepositories
	&& !inInclude
        && !inPlugins
        && !inFlatDir) {			
      if (methodName.equals("google")) {
        dependencyResolutionManagementRepositories.add(new Repository(methodName, "https://maven.google.com/"));
      }
      if (methodName.equals("mavenLocal")) {
        dependencyResolutionManagementRepositories.add(new Repository(methodName, ".m2/repository"));
      }
      if (methodName.equals("mavenCentral")) {
        dependencyResolutionManagementRepositories.add(
            new Repository(methodName, "https://repo.maven.apache.org/maven2/"));
      }
      if (methodName.equals("gradlePluginPortal")) {
        dependencyResolutionManagementRepositories.add(
            new Repository(methodName, "https://plugins.gradle.org/m2/"));
      }
    }

    if ((inAllProjectsRepositories
            && inAllProjectsRepositoriesFlatDirDirs
            && !inBuildScriptRepositories
            && !inPluginManagementRepositories
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

    if (inPlugins
       //     && !inBuildScript
       //     && !inAllProjects
      //      && !inPluginManagement
	//		&& !inDependencyResolutionManagement
       //     && !inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      pluginsConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      pluginsConfigurationName = null;

    } else if (inRepositories
         //   && !inPlugins
       //     && !inBuildScript
      //      && !inAllProjects			
       //      && !inPluginManagement
	//		&& !inDependencyResolutionManagement
      //      && !inFlatDir
			
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      repositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      repositoriesConfigurationName = null;

    } else if (inBuildScriptRepositories
          /*  && !inAllProjectsRepositories
            && !inPluginManagementRepositories
			&& !inDependencyResolutionManagementRepositories
            && !inPlugins
            && inFlatDir
		*/	
        && !(blockStatementStack.isEmpty() ? false : blockStatementStack.peek())
					
					) {
						
      buildscriptRepositoriesConfigurationName = methodName;
	 // System.out.println(lineNumber +  " " + methodName + " " + inBuildScript + " " + inBuildScriptRepositories  + " " + repositoriesConfigurationName);
      super.visitMethodCallExpression(methodCallExpression);
      buildscriptRepositoriesConfigurationName = null;

    } else if ((inAllProjectsRepositories
            && !inBuildScriptRepositories
            && !inPluginManagementRepositories
			&& !inDependencyResolutionManagementRepositories
            && !inPlugins
            && !inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      allprojectsRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      allprojectsRepositoriesConfigurationName = null;

    } else if ((inPluginManagementRepositories
            && !inBuildScriptRepositories
            && !inAllProjectsRepositories
			&& !inDependencyResolutionManagementRepositories
            && !inPlugins
            && !inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      pluginManagementRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      pluginManagementRepositoriesConfigurationName = null;
	  
	} else if ((inDependencyResolutionManagementRepositories
            && !inBuildScriptRepositories
            && !inAllProjectsRepositories
			&& !inPluginManagementRepositories
            && !inPlugins
            && !inFlatDir)
        && (blockStatementStack.isEmpty() ? false : blockStatementStack.peek())) {
      dependencyResolutionManagementRepositoriesConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      dependencyResolutionManagementRepositoriesConfigurationName = null;

    } else if (inInclude) {
      includeConfigurationName = methodName;
      super.visitMethodCallExpression(methodCallExpression);
      includeConfigurationName = null;

    } else {
      super.visitMethodCallExpression(methodCallExpression);
    }
  }
}
