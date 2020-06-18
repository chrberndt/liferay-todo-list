package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalServiceUtil;

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

			// TODO: Use remote service

			task = TaskLocalServiceUtil.getTask(taskId);
		}

		// TODO: Add trash support

		//		if ((task != null) && task.isInTrash()) {
		//			throw new NoSuchTaskException("{taskId=" + taskId + "}");
		//		}

		return task;
	}

}