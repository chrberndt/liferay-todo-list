package com.chberndt.liferay.todo.list.internal.security.permission.resource;

import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(immediate = true, service = TaskPermission.class)
public class TaskPermission {

	public static boolean contains(
			PermissionChecker permissionChecker, long taskId, String actionId)
		throws PortalException {

		return _taskModelResourcePermission.contains(
			permissionChecker, taskId, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, Task task, String actionId)
		throws PortalException {

		return _taskModelResourcePermission.contains(
			permissionChecker, task, actionId);
	}

	@Reference(
		target = "(model.class.name=com.chberndt.liferay.todo.list.model.Task)",
		unbind = "-"
	)
	protected void setEntryModelPermission(
		ModelResourcePermission<Task> modelResourcePermission) {

		_taskModelResourcePermission = modelResourcePermission;
	}

	private static ModelResourcePermission<Task> _taskModelResourcePermission;

}