package com.chberndt.liferay.todo.list.internal.display.context;

import com.chberndt.liferay.todo.list.constants.ToDoListPortletKeys;
import com.chberndt.liferay.todo.list.internal.security.permission.resource.TaskPermission;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalServiceUtil;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Christian Berndt
 */
public class TasksDisplayContext {

	public TasksDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;

		// TODO: Add TrashHelper support
		// _trashHelper = trashHelper;

		_portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			liferayPortletRequest);

		_httpServletRequest = _liferayPortletRequest.getHttpServletRequest();
	}

	public List<String> getAvailableActions(Task task) throws PortalException {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (TaskPermission.contains(
				themeDisplay.getPermissionChecker(), task, ActionKeys.DELETE)) {

			return Collections.singletonList("deleteTasks");
		}

		return Collections.emptyList();
	}

	public Map<String, Object> getComponentContext() throws PortalException {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return new HashMap<String, Object>() {
			{
				put("trashEnabled", false);

				// TODO

				//					_trashHelper.isTrashEnabled(
				//						themeDisplay.getScopeGroupId()));
			}
		};
	}

	public String getDisplayStyle() {
		String displayStyle = ParamUtil.getString(
			_httpServletRequest, "displayStyle");

		if (Validator.isNull(displayStyle)) {
			displayStyle = _portalPreferences.getValue(
				ToDoListPortletKeys.TODO_LIST, "tasks-display-style", "icon");
		}
		else {
			_portalPreferences.setValue(
				ToDoListPortletKeys.TODO_LIST, "tasks-display-style",
				displayStyle);

			_httpServletRequest.setAttribute(
				WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
		}

		return displayStyle;
	}

	public SearchContainer<Task> getSearchContainer()
		throws PortalException, PortletException {

		PortletURL portletURL = _liferayPortletResponse.createRenderURL();

		portletURL.setParameter("mvcRenderCommandName", "/view");

		String tasksNavigation = ParamUtil.getString(
			_httpServletRequest, "tasksNavigation");

		portletURL.setParameter("tasksNavigation", tasksNavigation);

		SearchContainer<Task> tasksSearchContainer = new SearchContainer<>(
			_liferayPortletRequest,
			PortletURLUtil.clone(portletURL, _liferayPortletResponse), null,
			"no-tasks-were-found");

		String orderByCol = ParamUtil.getString(
			_httpServletRequest, "orderByCol", "title");

		tasksSearchContainer.setOrderByCol(orderByCol);

		String orderByType = ParamUtil.getString(
			_httpServletRequest, "orderByType", "asc");

		tasksSearchContainer.setOrderByType(orderByType);

		// TODO

		//			tasksSearchContainer.setOrderByComparator(
		//				BlogsUtil.getOrderByComparator(
		//					tasksSearchContainer.getOrderByCol(),
		//					tasksSearchContainer.getOrderByType()));

		tasksSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_liferayPortletResponse));

		_populateResults(tasksSearchContainer);

		return tasksSearchContainer;
	}

	private int _getStatus() {
		if (_status != null) {
			return _status;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay) _httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();

		if (permissionChecker.isContentReviewer(themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId())) {

			_status = WorkflowConstants.STATUS_ANY;
		} else {
			_status = WorkflowConstants.STATUS_APPROVED;
		}

		return _status;
	}

	private void _populateResults(SearchContainer<Task> searchContainer)
		throws PortalException {

		List<Task> tasksResults = null;

		// TODO: filter tasks by keyword

		// TODO: filter tasks by assetCategoryId and / or assetTagName

		// TODO: Manage index based search

		searchContainer.setTotal(TaskLocalServiceUtil.getTasksCount());

		tasksResults = TaskLocalServiceUtil.getTasks(
			searchContainer.getStart(), searchContainer.getEnd());

		searchContainer.setResults(tasksResults);
	}

	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final PortalPreferences _portalPreferences;

	 private Integer _status;
		// TODO
	// private final TrashHelper _trashHelper;

}