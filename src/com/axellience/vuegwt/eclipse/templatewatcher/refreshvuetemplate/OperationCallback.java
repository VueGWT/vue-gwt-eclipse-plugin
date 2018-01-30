package com.axellience.vuegwt.eclipse.templatewatcher.refreshvuetemplate;

import org.eclipse.core.runtime.IProgressMonitor;

@FunctionalInterface
public interface OperationCallback extends IProgressMonitor {
	@Override
	default void beginTask(String name, int totalWork) {
	}

	@Override
	default void internalWorked(double work) {
	}

	@Override
	default boolean isCanceled() {
		return false;
	}

	@Override
	default void setCanceled(boolean value) {
	}

	@Override
	default void setTaskName(String name) {
	}

	@Override
	default void subTask(String name) {
	}

	@Override
	default void worked(int work) {
	}
}
