package com.chberndt.liferay.todo.list.internal.asset.model;

import com.chberndt.liferay.todo.list.constants.ToDoListPortletKeys;
import com.chberndt.liferay.todo.list.internal.security.permission.resource.TaskPermission;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.web.constants.ToDoListWebKeys;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.asset.util.AssetHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;
import com.liferay.portal.kernel.portlet.PortletLayoutFinderRegistryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.trash.TrashRenderer;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Christian Berndt
 */
public class TaskAssetRenderer
	extends BaseJSPAssetRenderer<Task> implements TrashRenderer {

	public TaskAssetRenderer(
		Task task, ResourceBundleLoader resourceBundleLoader) {

		_task = task;
	}

	@Override
	public Task getAssetObject() {
		return _task;
	}

	@Override
	public String getClassName() {
		return Task.class.getName();
	}

	@Override
	public long getClassPK() {
		return _task.getTaskId();
	}

	@Override
	public long getGroupId() {
		return _task.getGroupId();
	}

	@Override
	public String getJspPath(
		HttpServletRequest httpServletRequest, String template) {

		if (template.equals(TEMPLATE_ABSTRACT) ||
			template.equals(TEMPLATE_FULL_CONTENT)) {

			return "/asset/" + template + ".jsp";
		}

		return null;
	}

	@Override
	public String getPortletId() {
		AssetRendererFactory<Task> assetRendererFactory =
			getAssetRendererFactory();

		return assetRendererFactory.getPortletId();
	}

	@Override
	public int getStatus() {
		return _task.getStatus();
	}

	@Override
	public String getSummary(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		int abstractLength = AssetHelper.ASSET_ENTRY_ABSTRACT_LENGTH;

		if (portletRequest != null) {
			abstractLength = GetterUtil.getInteger(
				portletRequest.getAttribute(
					WebKeys.ASSET_ENTRY_ABSTRACT_LENGTH),
				AssetHelper.ASSET_ENTRY_ABSTRACT_LENGTH);
		}

		String summary = HtmlUtil.escape(_task.getDescription());

		if (Validator.isNull(summary)) {
			summary = StringUtil.shorten(
				HtmlUtil.stripHtml(_task.getDescription()), abstractLength);
		}

		return summary;
	}

	@Override
	public String getTitle(Locale locale) {
		//		ResourceBundle resourceBundle =
		//			_resourceBundleLoader.loadResourceBundle(locale);

		return _task.getTitle();
	}

	@Override
	public String getType() {
		return TaskAssetRendererFactory.TYPE;
	}

	@Override
	public PortletURL getURLEdit(HttpServletRequest httpServletRequest) {
		Group group = GroupLocalServiceUtil.fetchGroup(_task.getGroupId());

		if (group.isCompany()) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			group = themeDisplay.getScopeGroup();
		}

		PortletURL portletURL = PortalUtil.getControlPanelPortletURL(
			httpServletRequest, group, ToDoListPortletKeys.TODO_LIST, 0, 0,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcRenderCommandName", "/edit_task");
		portletURL.setParameter("taskId", String.valueOf(_task.getTaskId()));

		return portletURL;
	}

	@Override
	public PortletURL getURLEdit(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		return getURLEdit(
			PortalUtil.getHttpServletRequest(liferayPortletRequest));
	}

	@Override
	public String getUrlTitle() {

		// TODO

		return null;

		// return _task.getUrlTitle();

	}

	@Override
	public String getURLView(
			LiferayPortletResponse liferayPortletResponse,
			WindowState windowState)
		throws Exception {

		AssetRendererFactory<Task> assetRendererFactory =
			getAssetRendererFactory();

		PortletURL portletURL = assetRendererFactory.getURLView(
			liferayPortletResponse, windowState);

		portletURL.setParameter("mvcRenderCommandName", "/view_task");
		portletURL.setParameter("taskId", String.valueOf(_task.getTaskId()));
		portletURL.setWindowState(windowState);

		return portletURL.toString();
	}

	@Override
	public String getURLViewInContext(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			String noSuchTaskRedirect)
		throws PortalException {

		if (_assetDisplayPageFriendlyURLProvider != null) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)liferayPortletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			String friendlyURL =
				_assetDisplayPageFriendlyURLProvider.getFriendlyURL(
					getClassName(), getClassPK(), themeDisplay);

			if (Validator.isNotNull(friendlyURL)) {
				return friendlyURL;
			}
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)liferayPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!_hasViewInContextGroupLayout(_task.getGroupId(), themeDisplay)) {
			return null;
		}

		return getURLViewInContext(
			liferayPortletRequest, noSuchTaskRedirect, "/find_task", "taskId",
			_task.getTaskId());
	}

	@Override
	public long getUserId() {
		return _task.getUserId();
	}

	@Override
	public String getUserName() {
		return _task.getUserName();
	}

	@Override
	public String getUuid() {
		return _task.getUuid();
	}

	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return TaskPermission.contains(
			permissionChecker, _task, ActionKeys.UPDATE);
	}

	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return TaskPermission.contains(
			permissionChecker, _task, ActionKeys.VIEW);
	}

	@Override
	public boolean include(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String template)
		throws Exception {

		httpServletRequest.setAttribute(ToDoListWebKeys.TASK, _task);

		return super.include(httpServletRequest, httpServletResponse, template);
	}

	@Override
	public boolean isPrintable() {
		return true;
	}

	public void setAssetDisplayPageFriendlyURLProvider(
		AssetDisplayPageFriendlyURLProvider
			assetDisplayPageFriendlyURLProvider) {

		_assetDisplayPageFriendlyURLProvider =
			assetDisplayPageFriendlyURLProvider;
	}

	private boolean _hasViewInContextGroupLayout(
		long groupId, ThemeDisplay themeDisplay) {

		try {
			PortletLayoutFinder portletLayoutFinder =
				PortletLayoutFinderRegistryUtil.getPortletLayoutFinder(
					Task.class.getName());

			PortletLayoutFinder.Result result = portletLayoutFinder.find(
				themeDisplay, groupId);

			if ((result == null) || Validator.isNull(result.getPortletId())) {
				return false;
			}

			return true;
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException, portalException);
			}

			return false;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TaskAssetRenderer.class);

	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;
	private final Task _task;

}