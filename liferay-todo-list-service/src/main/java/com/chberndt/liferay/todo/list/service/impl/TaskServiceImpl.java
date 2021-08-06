
package com.chberndt.liferay.todo.list.service.impl;

import com.chberndt.liferay.todo.list.constants.TodoListActionKeys;
import com.chberndt.liferay.todo.list.constants.TodoListConstants;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.base.TaskServiceBaseImpl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * The implementation of the task remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * <code>com.chberndt.liferay.todo.list.service.TaskService</code> interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @see TaskServiceBaseImpl
 */
@Component(
	property = {
		"json.web.service.context.name=custom",
		"json.web.service.context.path=Task"
	},
	service = AopService.class
)
public class TaskServiceImpl extends TaskServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use
	 * <code>com.chberndt.liferay.todo.list.service.TaskServiceUtil</code> to access
	 * the task remote service.
	 */
	public Task addTask(
			String title, String description, boolean completed, Date dueDate,
			ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			TodoListActionKeys.ADD_TASK);

		return taskLocalService.addTask(
			getUserId(), title, description, completed, dueDate,
			serviceContext);
	}

	public void deleteTask(long taskId) throws PortalException {
		_taskModelResourcePermission.check(
			getPermissionChecker(), taskId, ActionKeys.DELETE);

		taskLocalService.deleteTask(taskId);
	}

	@Override
	public List<Task> getGroupTasks(long groupId, int status, int max) {
		return getGroupTasks(groupId, status, 0, max);
	}

	@Override
	public List<Task> getGroupTasks(
		long groupId, int status, int start, int end) {

		if (status == WorkflowConstants.STATUS_ANY) {
			return taskPersistence.filterFindByG_NotS(
				groupId, WorkflowConstants.STATUS_IN_TRASH, start, end);
		}

		return taskPersistence.filterFindByG_S(groupId, status, start, end);
	}

	@Override
	public List<Task> getGroupTasks(
		long groupId, int status, int start, int end,
		OrderByComparator<Task> orderByComparator) {

		if (status == WorkflowConstants.STATUS_ANY) {
			return taskPersistence.filterFindByG_NotS(
				groupId, WorkflowConstants.STATUS_IN_TRASH, start, end,
				orderByComparator);
		}

		return taskPersistence.filterFindByG_S(
			groupId, status, start, end, orderByComparator);
	}

	@Override
	public int getGroupTasksCount(long groupId, int status) {
		if (status == WorkflowConstants.STATUS_ANY) {
			return taskPersistence.filterCountByG_NotS(
				groupId, WorkflowConstants.STATUS_IN_TRASH);
		}

		return taskPersistence.filterCountByG_S(groupId, status);
	}

	public Task getTask(long taskId) throws PortalException {
		_taskModelResourcePermission.check(
			getPermissionChecker(), taskId, ActionKeys.VIEW);

		return taskLocalService.getTask(taskId);
	}

	public List<Task> getTasksByKeywords(
		long groupId, String keywords, int start, int end,
		OrderByComparator<Task> orderByComparator) {

		return taskLocalService.getTasksByKeywords(
			groupId, keywords, start, end, orderByComparator);
	}

	public long getTasksCountByKeywords(long groupId, String keywords) {
		return taskLocalService.getTasksCountByKeywords(groupId, keywords);
	}

	@Override
	public Task moveTaskToTrash(long taskId) throws PortalException {
		_taskModelResourcePermission.check(
			getPermissionChecker(), taskId, ActionKeys.DELETE);

		return taskLocalService.moveTaskToTrash(getUserId(), taskId);
	}

	@Override
	public void restoreTaskFromTrash(long taskId) throws PortalException {
		_taskModelResourcePermission.check(
			getPermissionChecker(), taskId, ActionKeys.DELETE);

		taskLocalService.restoreTaskFromTrash(getUserId(), taskId);
	}

	public Task updateTask(
			long taskId, String title, String description, boolean completed,
			Date dueDate, ServiceContext serviceContext)
		throws PortalException {

		_taskModelResourcePermission.check(
			getPermissionChecker(), taskId, ActionKeys.UPDATE);

		return taskLocalService.updateTask(
			getUserId(), taskId, title, description, completed, dueDate,
			serviceContext);
	}

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(resource.name=" + TodoListConstants.RESOURCE_NAME + ")"
	)
	private volatile PortletResourcePermission _portletResourcePermission;

	@Reference(
		target = "(model.class.name=com.chberndt.liferay.todo.list.model.Task)"
	)
	private ModelResourcePermission<Task> _taskModelResourcePermission;

}