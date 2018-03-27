package com.axellience.vuegwt.eclipse;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IStartup;
import com.axellience.vuegwt.eclipse.templatewatcher.VueTemplateWatcher;

public class Startup implements IStartup {
	public static String PLUGIN_ID = "com.axellience.vuegwt.eclipse";
	private VueTemplateWatcher vueTemplateWatcher;

	@Override
	public void earlyStartup() {
		this.vueTemplateWatcher = new VueTemplateWatcher();
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(vueTemplateWatcher, IResourceChangeEvent.POST_CHANGE);
	}
}
