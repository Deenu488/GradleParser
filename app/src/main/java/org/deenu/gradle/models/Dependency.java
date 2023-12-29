package org.deenu.gradle.models;

import java.util.HashMap;
import java.util.Map;

public class Dependency {

	private String configuration;
	private String group;
	private String name;
	private String version;

	public Dependency(String group, String name, String version) {
		this.group = group;
		this.name = name;
		this.version = version;
	}

	public Dependency() {
	}

	public static Dependency fromString(String dependency) {
		String[] values = dependency.split(":");
		return new Dependency(values[0], values[1], values[2]);
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getConfiguration() {
		return ((configuration != null) ? configuration : "configuration");
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return ((configuration != null) ? configuration : "configuration") + ":" + group + ":" + name + ":" + version;
	}

}