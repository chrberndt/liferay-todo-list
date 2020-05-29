package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.exception.NoSuchTaskException;
import com.chberndt.liferay.todo.list.service.TaskLocalService;
import com.chberndt.liferay.todo.list.web.constants.ToDoListPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

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
		long entryId = ParamUtil.getLong(actionRequest, "entryId");

		long[] deleteTaskIds = null;

		if (entryId > 0) {
			deleteTaskIds = new long[] {entryId};
		}
		else {
			deleteTaskIds = ParamUtil.getLongValues(
				actionRequest, "rowIdsAnnouncementsTask");
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
		//		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
		//			WebKeys.THEME_DISPLAY);

		long entryId = ParamUtil.getLong(actionRequest, "entryId");

		//		String[] distributionScopeParts = StringUtil.split(
		//			ParamUtil.getString(actionRequest, "distributionScope"));

		//		long classNameId = 0;
		//		long classPK = 0;

		//		if (distributionScopeParts.length == 2) {
		//			classNameId = GetterUtil.getLong(distributionScopeParts[0]);
		//
		//			if (classNameId > 0) {
		//				classPK = GetterUtil.getLong(distributionScopeParts[1]);
		//			}
		//		}

		//		String title = ParamUtil.getString(actionRequest, "title");
		//		String content = ParamUtil.getString(actionRequest, "content");
		//		String url = ParamUtil.getString(actionRequest, "url");
		//		String type = ParamUtil.getString(actionRequest, "type");
		//
		//		Date dueDate = new Date();

		boolean displayImmediately = ParamUtil.getBoolean(
			actionRequest, "displayImmediately");

		if (!displayImmediately) {
			//			int dueDateMonth = ParamUtil.getInteger(
			//				actionRequest, "dueDateMonth");
			//			int dueDateDay = ParamUtil.getInteger(actionRequest, "dueDateDay");
			//			int dueDateYear = ParamUtil.getInteger(
			//				actionRequest, "dueDateYear");
			//			int dueDateHour = ParamUtil.getInteger(
			//				actionRequest, "dueDateHour");
			//			int dueDateMinute = ParamUtil.getInteger(
			//				actionRequest, "dueDateMinute");
			//			int dueDateAmPm = ParamUtil.getInteger(
			//				actionRequest, "dueDateAmPm");
			//
			//			if (dueDateAmPm == Calendar.PM) {
			//				dueDateHour += 12;
			//			}

			//			dueDate = _portal.getDate(
			//				dueDateMonth, dueDateDay, dueDateYear,
			//				dueDateHour, dueDateMinute, themeDisplay.getTimeZone(),
			//				TaskDueDateExceptionClass.class);
		}

		//		int priority = ParamUtil.getInteger(actionRequest, "priority");

		if (entryId <= 0) {
			//			boolean alert = ParamUtil.getBoolean(actionRequest, "alert");

			// TODO

			//_taskLocalService.addTask();
		}
		else {

			// TODO
			// _taskLocalService.updateTask();

		}
	}

	// TODO: use remote service

	@Reference
	private Portal _portal;

	@Reference
	private TaskLocalService _taskLocalService;

}