package com.axellience.vuegwt.eclipse.templatewatcher.refreshvuetemplate;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.axellience.vuegwt.eclipse.templatewatcher.VueTemplateInfo;

public class RefreshVueTemplateJob extends WorkspaceJob {
	private final Set<VueTemplateInfo> templatesToRefresh;

	public RefreshVueTemplateJob(Set<VueTemplateInfo> templatesToRefresh) {
		super("Vue GWT Refresh");
		this.templatesToRefresh = new HashSet<>(templatesToRefresh);
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		this.templatesToRefresh.stream().forEach(this::processTemplate);

		return Status.OK_STATUS;
	}

	private void processTemplate(VueTemplateInfo vueTemplateInfo) {
		IFile htmlFile = vueTemplateInfo.getHtmlFile();
		IPath htmlFilePathInSource = htmlFile.getFullPath().makeRelativeTo(vueTemplateInfo.getJavaSourcePath());

		IFile targetFile = htmlFile.getProject().getFile(vueTemplateInfo.getJavaOutputPath()
				.append(htmlFilePathInSource).makeRelativeTo(htmlFile.getProject().getFullPath()));

		try {
			if (targetFile.exists()) {
				targetFile.setContents(htmlFile.getContents(), IResource.FORCE,
						(OperationCallback) () -> TouchFileUtil.touchFile(vueTemplateInfo.getJavaFile()));
			} else {
				htmlFile.copy(targetFile.getProjectRelativePath(), IResource.FORCE,
						(OperationCallback) () -> TouchFileUtil.touchFile(vueTemplateInfo.getJavaFile()));
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
