
package com.chberndt.liferay.todo.list.service.impl;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.base.TaskServiceBaseImpl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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

		//				_portletResourcePermission.check(
		//					getPermissionChecker(), serviceContext.getScopeGroupId(),
		//					"ADD_TASK");

		return taskLocalService.addTask(
			getUserId(), title, description, completed, dueDate,
			serviceContext);
	}

	public void deleteTask(long taskId) throws PortalException {
		_taskModelResourcePermission.check(
			getPermissionChecker(), taskId, ActionKeys.DELETE);

		taskLocalService.deleteTask(taskId);
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

	// TODO: Fix portletResourcePermission check

	//	@Reference(target = "(resource.name=" + ToDoListPortletKeys.TODO_LIST + ")")
	//	private PortletResourcePermission _portletResourcePermission;

	@Reference(
		target = "(model.class.name=com.chberndt.liferay.todo.list.model.Task)"
	)
	private ModelResourcePermission<Task> _taskModelResourcePermission;

}