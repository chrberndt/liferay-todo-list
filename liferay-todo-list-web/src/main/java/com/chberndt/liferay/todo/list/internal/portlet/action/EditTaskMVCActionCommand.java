package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.constants.ToDoListPortletKeys;
import com.chberndt.liferay.todo.list.exception.NoSuchTaskException;
import com.chberndt.liferay.todo.list.exception.TaskDueDateException;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalService;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Calendar;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	property = {
		"javax.portlet.name=" + ToDoListPortletKeys.TODO_LIST,
		"mvc.command.name=/edit_task"
	},
	service = MVCActionCommand.class
)
public class EditTaskMVCActionCommand extends BaseMVCActionCommand {

	protected void deleteTask(ActionRequest actionRequest) throws Exception {
		long taskId = ParamUtil.getLong(actionRequest, "taskId");

		long[] deleteTaskIds = null;

		if (taskId > 0) {
			deleteTaskIds = new long[] {taskId};
		}
		else {
			deleteTaskIds = ParamUtil.getLongValues(actionRequest, "rowIds");
		}

		for (long deleteTaskId : deleteTaskIds) {
			_taskLocalService.deleteTask(deleteTaskId);
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateTask(actionRequest);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteTask(actionRequest);
			}
		}

		// TODO: catch TaskDueDateException | TaskTitleException

		catch (NoSuchTaskException | PrincipalException e) {
			SessionErrors.add(actionRequest, e.getClass());
		}
	}

	protected void updateTask(ActionRequest actionRequest) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long taskId = ParamUtil.getLong(actionRequest, "taskId");

		String title = ParamUtil.getString(actionRequest, "title");
		String description = ParamUtil.getString(actionRequest, "description");
		boolean completed = ParamUtil.getBoolean(actionRequest, "completed");

		Date dueDate = new Date();

		int dueDateMonth = ParamUtil.getInteger(actionRequest, "dueDateMonth");
		int dueDateDay = ParamUtil.getInteger(actionRequest, "dueDateDay");
		int dueDateYear = ParamUtil.getInteger(actionRequest, "dueDateYear");
		int dueDateHour = ParamUtil.getInteger(actionRequest, "dueDateHour");
		int dueDateMinute = ParamUtil.getInteger(
			actionRequest, "dueDateMinute");
		int dueDateAmPm = ParamUtil.getInteger(actionRequest, "dueDateAmPm");

		if (dueDateAmPm == Calendar.PM) {
			dueDateHour += 12;
		}

		dueDate = _portal.getDate(
			dueDateMonth, dueDateDay, dueDateYear, dueDateHour, dueDateMinute,
			themeDisplay.getTimeZone(), TaskDueDateException.class);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			Task.class.getName(), actionRequest);

		if (taskId <= 0) {

			// TODO: use remote service

			_taskLocalService.addTask(
				serviceContext.getGuestOrUserId(), title, description,
				completed, dueDate, serviceContext);
		}
		else {

			// TODO: use remote service

			_taskLocalService.updateTask(
				serviceContext.getGuestOrUserId(), taskId, title, description,
				dueDate, serviceContext);
		}
	}

	@Reference
	private Portal _portal;

	// TODO: use remote service

	@Reference
	private TaskLocalService _taskLocalService;

}