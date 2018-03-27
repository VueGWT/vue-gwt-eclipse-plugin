package com.axellience.vuegwt.eclipse.templatewatcher;

import static com.axellience.vuegwt.eclipse.Startup.PLUGIN_ID;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.axellience.vuegwt.eclipse.vueproject.VueProjectsManager;

public class VueTemplateWatcher implements IResourceChangeListener, IResourceDeltaVisitor {
	private final ILog LOGGER = Platform.getLog(Platform.getBundle(PLUGIN_ID));

	private final Set<IFile> javaFilesToRefresh;
	private final VueProjectsManager vueProjectsManager;

	public VueTemplateWatcher() {
		javaFilesToRefresh = new HashSet<>();
		vueProjectsManager = new VueProjectsManager();
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() != IResourceChangeEvent.POST_CHANGE)
			return;

		try {
			javaFilesToRefresh.clear();
			event.getDelta().accept(this);
			new RefreshVueTemplateJob(javaFilesToRefresh).schedule();
		} catch (CoreException e) {
			LOGGER.log(new Status(IStatus.WARNING, PLUGIN_ID, "Error while processing change notification", e));
		}
	}

	public boolean visit(IResourceDelta delta) {
		if (!isContentChange(delta))
			return true;
		IResource resource = delta.getResource();
		if (!isHtmlFile(resource))
			return true;

		IFile htmlFile = (IFile) resource;
		IFile componentJavaFile = getSibblingJavaFile(htmlFile);
		if (!componentJavaFile.exists())
			return true;

		// Filter out files that are in the project target
		if (!isInProjectClassPath(resource))
			return true;

		javaFilesToRefresh.add(componentJavaFile);
		return true;
	}

	private boolean isInProjectClassPath(IResource resource) {
		return vueProjectsManager.getResourceSourcePath(resource).isPresent();
	}

	private IFile getSibblingJavaFile(IResource resource) {
		String javaFileName = resource.getName().substring(0, resource.getName().length() - 4) + "java";
		return resource.getParent().getFile(new Path(javaFileName));
	}

	public boolean isContentChange(IResourceDelta delta) {
		return delta.getKind() == IResourceDelta.CHANGED && (delta.getFlags() & IResourceDelta.CONTENT) != 0;
	}

	public boolean isHtmlFile(IResource resource) {
		return resource.getType() == IResource.FILE && "html".equalsIgnoreCase(resource.getFileExtension());
	}
}
