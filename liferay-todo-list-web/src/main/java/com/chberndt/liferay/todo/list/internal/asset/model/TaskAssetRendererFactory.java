package com.chberndt.liferay.todo.list.internal.asset.model;

import com.chberndt.liferay.todo.list.constants.TodoListActionKeys;
import com.chberndt.liferay.todo.list.constants.TodoListConstants;
import com.chberndt.liferay.todo.list.constants.TodoListPortletKeys;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalService;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleLoaderUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + TodoListPortletKeys.TODO_LIST,
	service = AssetRendererFactory.class
)
public class TaskAssetRendererFactory extends BaseAssetRendererFactory<Task> {

	public static final String TYPE = "task";

	public TaskAssetRendererFactory() {
		setClassName(Task.class.getName());
		setLinkable(true);
		setPortletId(TodoListPortletKeys.TODO_LIST);
		setSearchable(true);
	}

	@Override
	public AssetRenderer<Task> getAssetRenderer(long classPK, int type)
		throws PortalException {

		TaskAssetRenderer taskAssetRenderer = new TaskAssetRenderer(
			_taskLocalService.getTask(classPK),
			ResourceBundleLoaderUtil.
				getResourceBundleLoaderByBundleSymbolicName(
					"com.chberndt.liferay.todo.list.web"));

		taskAssetRenderer.setAssetDisplayPageFriendlyURLProvider(
			_assetDisplayPageFriendlyURLProvider);
		taskAssetRenderer.setAssetRendererType(type);
		taskAssetRenderer.setServletContext(_servletContext);

		return taskAssetRenderer;
	}

	@Override
	public String getClassName() {
		return Task.class.getName();
	}

	@Override
	public String getIconCssClass() {
		return "task";
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public PortletURL getURLAdd(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, long classTypeId) {

		PortletURL portletURL = _portal.getControlPanelPortletURL(
			liferayPortletRequest, getGroup(liferayPortletRequest),
			TodoListPortletKeys.TODO_LIST, 0, 0, PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcRenderCommandName", "/edit_task");

		return portletURL;
	}

	@Override
	public PortletURL getURLView(
		LiferayPortletResponse liferayPortletResponse,
		WindowState windowState) {

		LiferayPortletURL liferayPortletURL =
			liferayPortletResponse.createLiferayPortletURL(
				TodoListPortletKeys.TODO_LIST, PortletRequest.RENDER_PHASE);

		try {
			liferayPortletURL.setWindowState(windowState);
		}
		catch (WindowStateException windowStateException) {
		}

		return liferayPortletURL;
	}

	@Override
	public boolean hasAddPermission(
		PermissionChecker permissionChecker, long groupId, long classTypeId) {

		return _portletResourcePermission.contains(
			permissionChecker, groupId, TodoListActionKeys.ADD_TASK);
	}

	@Override
	public boolean hasPermission(
			PermissionChecker permissionChecker, long classPK, String actionId)
		throws Exception {

		return _taskModelResourcePermission.contains(
			permissionChecker, classPK, actionId);
	}

	@Reference
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(resource.name=" + TodoListConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

	@Reference(
		target = "(osgi.web.symbolicname=com.chberndt.liferay.todo.list.web)"
	)
	private ServletContext _servletContext;

	@Reference
	private TaskLocalService _taskLocalService;

	@Reference(
		target = "(model.class.name=com.chberndt.liferay.todo.list.model.Task)"
	)
	private ModelResourcePermission<Task> _taskModelResourcePermission;

}