package com.axellience.vuegwt.eclipse.templatewatcher;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class VueTemplateWatcher implements IResourceChangeListener, IResourceDeltaVisitor {
	private final Set<IFile> javaFilesToRefresh;

	public VueTemplateWatcher() {
		javaFilesToRefresh = new HashSet<>();
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() != IResourceChangeEvent.POST_CHANGE)
			return;

		try {
			javaFilesToRefresh.clear();
			event.getDelta().accept(this);
			new RefreshVueTemplateJob(javaFilesToRefresh).schedule();
		} catch (CoreException e) {
			e.printStackTrace();
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

		javaFilesToRefresh.add(componentJavaFile);
		return true;
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
