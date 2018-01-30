package com.axellience.vuegwt.eclipse.templatewatcher;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public class VueTemplateInfo {
	private final IFile htmlFile;
	private final IFile javaFile;
	private final IPath javaSourcePath;
	private final IPath javaOutputPath;

	public VueTemplateInfo(IFile htmlFile, IFile javaFile, IPath javaSourcePath, IPath javaOutputPath) {
		this.htmlFile = htmlFile;
		this.javaFile = javaFile;
		this.javaSourcePath = javaSourcePath;
		this.javaOutputPath = javaOutputPath;
	}

	public IFile getHtmlFile() {
		return htmlFile;
	}

	public IFile getJavaFile() {
		return javaFile;
	}

	public IPath getJavaSourcePath() {
		return javaSourcePath;
	}

	public IPath getJavaOutputPath() {
		return javaOutputPath;
	}
}
