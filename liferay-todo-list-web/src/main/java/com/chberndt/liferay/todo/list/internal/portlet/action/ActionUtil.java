package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.exception.NoSuchTaskException;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletRequest;

/**
 * @author Christian Berndt
 */
public class ActionUtil {

	public static Task getTask(PortletRequest portletRequest) throws Exception {
		long taskId = ParamUtil.getLong(portletRequest, "taskId");

		Task task = null;

		if (taskId > 0) {
			task = TaskServiceUtil.getTask(taskId);
		}

		if ((task != null) && task.isInTrash()) {
			throw new NoSuchTaskException("{taskId=" + taskId + "}");
		}

		return task;
	}

}