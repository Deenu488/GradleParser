package org.deenu.gradle.models;

import java.util.ArrayList;
import java.util.List;

public class Plugin {

	private String plugin;
	private List<String> plugins;

	public Plugin(String plugin) {
		this.plugin = plugin;
		this.plugins = new ArrayList<>();
		this.plugins.add(plugin);
	}

	public Plugin(List<String> plugins) {
		this.plugins = new ArrayList<>(plugins);
	}

	public List<String> getPlugins() {
		return plugins;
	}

	@Override
	public String toString() {
		return plugin;
	}

}
