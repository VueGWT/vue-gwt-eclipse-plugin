package com.axellience.vuegwt.eclipse.vueproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;

public class VueProjectsManager {
	private final Map<IProject, VueProjectInfo> vueProjectInfoCache;
	
	public VueProjectsManager() {
		vueProjectInfoCache = new HashMap<>();
	}

	public Optional<IPath> getResourceSourcePath(IResource resource) {
		return getInfoForProject(resource.getProject())
				.flatMap(projectInfo -> projectInfo.getResourceSourcePath(resource));
	}

	public Optional<VueProjectInfo> getInfoForProject(IProject project) {
		if (vueProjectInfoCache.containsKey(project))
			return Optional.of(vueProjectInfoCache.get(project));

		if (!project.isOpen() || !isJavaProject(project))
			return Optional.empty();

		VueProjectInfo vueProjectInfo = new VueProjectInfo(JavaCore.create(project));
		vueProjectInfoCache.put(project, vueProjectInfo);
		return Optional.of(vueProjectInfo);
	}

	public boolean isJavaProject(IProject project) {
		return JavaProject.hasJavaNature(project);
	}
}