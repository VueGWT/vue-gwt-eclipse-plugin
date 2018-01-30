package com.axellience.vuegwt.eclipse.templatewatcher.refreshvuetemplate;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class TouchFileUtil {
	public static void touchFile(IFile javaFile) {
		new File(javaFile.getFullPath().toString()).setLastModified(System.currentTimeMillis());
		try {
			javaFile.touch(null);
		} catch (CoreException e) {
		}
	}
}
