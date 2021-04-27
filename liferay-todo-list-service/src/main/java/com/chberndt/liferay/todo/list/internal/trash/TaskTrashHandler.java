package com.chberndt.liferay.todo.list.internal.trash;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.trash.BaseTrashHandler;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.trash.constants.TrashActionKeys;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	property = "model.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = TrashHandler.class
)
public class TaskTrashHandler extends BaseTrashHandler {

	@Override
	public void deleteTrashEntry(long classPK) throws PortalException {
		_taskLocalService.deleteTask(classPK);
	}

	@Override
	public String getClassName() {
		return Task.class.getName();
	}

	@Override
	public String getRestoreContainedModelLink(
			PortletRequest portletRequest, long classPK)
		throws PortalException {

		PortletURL portletURL = getRestoreURL(portletRequest, classPK, false);

		Task task = _taskLocalService.getTask(classPK);

		portletURL.setParameter("taskId", String.valueOf(task.getTaskId()));

		// portletURL.setParameter("urlTitle", task.getUrlTitle());

		return portletURL.toString();
	}

	@Override
	public String getRestoreContainerModelLink(
			PortletRequest portletRequest, long classPK)
		throws PortalException {

		PortletURL portletURL = getRestoreURL(portletRequest, classPK, true);

		return portletURL.toString();
	}

	@Override
	public String getRestoreMessage(
		PortletRequest portletRequest, long classPK) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay.translate("todo-list");
	}

	@Override
	public boolean isInTrash(long classPK) throws PortalException {
		Task task = _taskLocalService.getTask(classPK);

		return task.isInTrash();
	}

	@Override
	public boolean isRestorable(long classPK) throws PortalException {
		Task task = _taskLocalService.getTask(classPK);

		if (!hasTrashPermission(
				PermissionThreadLocal.getPermissionChecker(), task.getGroupId(),
				classPK, TrashActionKeys.RESTORE)) {

			return false;
		}

		return !task.isInTrashContainer();
	}

	@Override
	public void restoreTrashEntry(long userId, long classPK)
		throws PortalException {

		_taskLocalService.restoreTaskFromTrash(userId, classPK);
	}

	protected PortletURL getRestoreURL(
			PortletRequest portletRequest, long classPK, boolean containerModel)
		throws PortalException {

		PortletURL portletURL = null;

		Task task = _taskLocalService.getTask(classPK);
		String portletId = PortletProviderUtil.getPortletId(
			Task.class.getName(), PortletProvider.Action.VIEW);

		long plid = _portal.getPlidFromPortletId(task.getGroupId(), portletId);

		if (plid == LayoutConstants.DEFAULT_PLID) {
			portletId = PortletProviderUtil.getPortletId(
				Task.class.getName(), PortletProvider.Action.MANAGE);

			portletURL = _portal.getControlPanelPortletURL(
				portletRequest, portletId, PortletRequest.RENDER_PHASE);
		}
		else {
			portletURL = PortletURLFactoryUtil.create(
				portletRequest, portletId, plid, PortletRequest.RENDER_PHASE);
		}

		if (!containerModel) {
			portletURL.setParameter("mvcRenderCommandName", "/edit_task");
		}

		return portletURL;
	}

	@Override
	protected boolean hasPermission(
			PermissionChecker permissionChecker, long classPK, String actionId)
		throws PortalException {

		return _taskModelResourcePermission.contains(
			permissionChecker, classPK, actionId);
	}

	@Reference
	private Portal _portal;

	@Reference
	private TaskLocalService _taskLocalService;

	@Reference(
		target = "(model.class.name=com.chberndt.liferay.todo.list.model.Task)"
	)
	private ModelResourcePermission<Task> _taskModelResourcePermission;

}