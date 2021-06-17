package com.chberndt.liferay.todo.list.internal.search;

import com.chberndt.liferay.todo.list.internal.security.permission.resource.TaskPermission;
import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;

import javax.portlet.PortletResponse;

/**
 * @author Christian Berndt
 */
public class TaskRowChecker extends EmptyOnClickRowChecker {

	public TaskRowChecker(PortletResponse portletResponse) {
		super(portletResponse);
	}

	@Override
	public boolean isDisabled(Object object) {
		Task formInstance = (Task)object;

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			if (!TaskPermission.contains(
					permissionChecker, formInstance, ActionKeys.DELETE)) {

				return true;
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		return super.isDisabled(object);
	}

	private static final Log _log = LogFactoryUtil.getLog(TaskRowChecker.class);

}