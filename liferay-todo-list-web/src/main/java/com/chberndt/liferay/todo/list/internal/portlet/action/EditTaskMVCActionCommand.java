package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.constants.ToDoListPortletKeys;
import com.chberndt.liferay.todo.list.exception.NoSuchTaskException;
import com.chberndt.liferay.todo.list.exception.TaskDueDateException;
import com.chberndt.liferay.todo.list.internal.bulk.selection.TaskBulkSelectionFactory;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskService;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.TrashedModel;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.trash.service.TrashEntryService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				_updateTask(actionRequest);
			}
			else if (cmd.equals(Constants.DELETE)) {
				_deleteTasks(actionRequest, false);
			}
			else if (cmd.equals(Constants.MOVE_TO_TRASH)) {
				_deleteTasks(actionRequest, true);
			}
			else if (cmd.equals(Constants.RESTORE)) {
				_restoreTrashEntries(actionRequest);
			}
		}
		catch (NoSuchTaskException | PrincipalException exception) {
			SessionErrors.add(actionRequest, exception.getClass());
		}
	}

	//	private void _deleteTask(
	//		Task task, boolean moveToTrash, List<TrashedModel> trashedModels) {
	//
	//		try {
	//			if (moveToTrash) {
	//				trashedModels.add(
	//					_taskService.moveTaskToTrash(task.getTaskId()));
	//			}
	//			else {
	//				_taskService.deleteTask(task.getTaskId());
	//			}
	//		}
	//		catch (PortalException portalException) {
	//			ReflectionUtil.throwException(portalException);
	//		}
	//	}

	private void _deleteTasks(ActionRequest actionRequest, boolean moveToTrash)
		throws Exception {

		long[] deleteTaskIds = null;

		long taskId = ParamUtil.getLong(actionRequest, "taskId");

		if (taskId > 0) {
			deleteTaskIds = new long[] {taskId};
		}
		else {
			deleteTaskIds = ParamUtil.getLongValues(actionRequest, "rowIds");
		}

		List<TrashedModel> trashedModels = new ArrayList<>();

		for (long deleteTaskId : deleteTaskIds) {
			try {
				if (moveToTrash) {
					trashedModels.add(
						_taskService.moveTaskToTrash(deleteTaskId));
				}
				else {
					_taskService.deleteTask(deleteTaskId);
				}
			}
			catch (PortalException portalException) {
				ReflectionUtil.throwException(portalException);
			}
		}

		if (moveToTrash && (deleteTaskIds.length > 0)) {
			addDeleteSuccessData(
				actionRequest,
				HashMapBuilder.<String, Object>put(
					"trashedModels", trashedModels
				).build());
		}
	}

	private Map<String, String[]> _getParameterMap(ActionRequest actionRequest)
		throws Exception {

		Map<String, String[]> parameterMap = new HashMap<>(
			actionRequest.getParameterMap());

		parameterMap.put(
			"groupId",
			new String[] {
				String.valueOf(_portal.getScopeGroupId(actionRequest))
			});

		return parameterMap;
	}

	private void _restoreTrashEntries(ActionRequest actionRequest)
		throws Exception {

		long[] restoreTrashEntryIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "restoreTrashEntryIds"), 0L);

		for (long restoreTrashEntryId : restoreTrashEntryIds) {
			_trashEntryService.restoreEntry(restoreTrashEntryId);
		}
	}

	private Task _updateTask(ActionRequest actionRequest) throws Exception {
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
			return _taskService.addTask(
				title, description, completed, dueDate, serviceContext);
		}

		return _taskService.updateTask(
			taskId, title, description, completed, dueDate, serviceContext);
	}

	@Reference
	private Portal _portal;

	@Reference
	private TaskBulkSelectionFactory _taskBulkSelectionFactory;

	@Reference
	private TaskService _taskService;

	@Reference
	private TrashEntryService _trashEntryService;

}