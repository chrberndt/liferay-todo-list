package com.chberndt.liferay.todo.list.internal.display.context;

import com.chberndt.liferay.todo.list.constants.ToDoListActionKeys;
import com.chberndt.liferay.todo.list.internal.security.permission.resource.ToDoListPermission;
import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Christian Berndt
 */
public class TasksManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public TasksManagementToolbarDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		HttpServletRequest httpServletRequest,
		SearchContainer<Task> searchContainer, String displayStyle) {

		super(
			liferayPortletRequest, liferayPortletResponse, httpServletRequest,
			searchContainer);

		// TODO: Add TrashHelper support

		_displayStyle = displayStyle;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData("action", "deleteTasks");

						// TODO: Add trashHelper support

						boolean trashEnabled = false;
						//						boolean trashEnabled = _trashHelper.isTrashEnabled(
						//							_themeDisplay.getScopeGroupId());

						dropdownItem.setIcon(
							trashEnabled ? "trash" : "times-circle");

						String label = "delete";

						if (trashEnabled) {
							label = "move-to-recycle-bin";
						}

						dropdownItem.setLabel(LanguageUtil.get(request, label));

						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	@Override
	public String getClearResultsURL() {
		PortletURL clearResultsURL = getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);

		return clearResultsURL.toString();
	}

	@Override
	public CreationMenu getCreationMenu() {
		if (!ToDoListPermission.contains(
				_themeDisplay.getPermissionChecker(),
				_themeDisplay.getScopeGroupId(), ToDoListActionKeys.ADD_TASK)) {

			return null;
		}

		return CreationMenuBuilder.addDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref(
					liferayPortletResponse.createRenderURL(),
					"mvcRenderCommandName", "/edit_task", "redirect",
					currentURLObj.toString());
				dropdownItem.setLabel(LanguageUtil.get(request, "add-task"));
			}
		).build();
	}

	// TODO:

	//	public List<LabelItem> getFilterLabelItems() {
	//		return null;
	//	}

	// TODO: fix deprecated methods

	@Override
	public String getSearchActionURL() {
		PortletURL searchURL = liferayPortletResponse.createRenderURL();

		searchURL.setParameter("mvcRenderCommandName", "/view");

		String navigation = ParamUtil.getString(
			request, "navigation", "entries");

		searchURL.setParameter("navigation", navigation);

		searchURL.setParameter("orderByCol", getOrderByCol());
		searchURL.setParameter("orderByType", getOrderByType());

		return searchURL.toString();
	}

	@Override
	public List<ViewTypeItem> getViewTypeItems() {
		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setParameter("mvcRenderCommandName", "/view");

		if (searchContainer.getDelta() > 0) {
			portletURL.setParameter(
				"delta", String.valueOf(searchContainer.getDelta()));
		}

		portletURL.setParameter("orderBycol", searchContainer.getOrderByCol());
		portletURL.setParameter(
			"orderByType", searchContainer.getOrderByType());

		portletURL.setParameter("entriesNavigation", getNavigation());

		if (searchContainer.getCur() > 0) {
			portletURL.setParameter(
				"cur", String.valueOf(searchContainer.getCur()));
		}

		return new ViewTypeItemList(portletURL, _displayStyle) {
			{
				addCardViewTypeItem();

				addListViewTypeItem();

				addTableViewTypeItem();
			}
		};
	}

	// TODO

	//	protected List<DropdownItem> getOrderByDropdownItems() {
	//		return null;
	//	}

	// TODO: fix deprecated methods

	// TODO: enable sorting

	//	private PortletURL _getCurrentSortingURL() {
	//		PortletURL sortingURL = getPortletURL();

	//
	//		sortingURL.setParameter("mvcRenderCommandName", "view");
	//
	//		sortingURL.setParameter(SearchContainer.DEFAULT_CUR_PARAM, "0");
	//
	//		String keywords = ParamUtil.getString(request, "keywords");
	//
	//		if (Validator.isNotNull(keywords)) {
	//			sortingURL.setParameter("keywords", keywords);
	//		}
	//
	//		return sortingURL;
	//	}

	private final String _displayStyle;
	private final ThemeDisplay _themeDisplay;

}