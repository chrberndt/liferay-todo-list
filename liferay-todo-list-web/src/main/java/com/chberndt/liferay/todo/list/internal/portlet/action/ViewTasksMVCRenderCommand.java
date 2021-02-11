package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.constants.ToDoListPortletKeys;
import com.chberndt.liferay.todo.list.internal.display.context.TasksDisplayContext;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskService;
import com.chberndt.liferay.todo.list.web.constants.ToDoListWebKeys;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ToDoListPortletKeys.TODO_LIST,
		"mvc.command.name=/", "mvc.command.name=/view"
	},
	service = MVCRenderCommand.class
)
public class ViewTasksMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		renderRequest.setAttribute(
			ToDoListWebKeys.TASKS_DISPLAY_CONTEXT,
			new TasksDisplayContext(renderRequest, renderResponse));

		// Add task list related attributes.

		addTaskListAttributes(renderRequest);

		//		// Add Clay management toolbar related attributes.
		//
		//		addManagementToolbarAttributes(renderRequest, renderResponse);

		return "/view.jsp";
	}

	/**
	 * Adds assigment list related attributes to the request.
	 *
	 * @param renderRequest
	 */
	private void addTaskListAttributes(RenderRequest renderRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		// Resolve start and end for the search.

		int currentPage = ParamUtil.getInteger(
			renderRequest, SearchContainer.DEFAULT_CUR_PARAM,
			SearchContainer.DEFAULT_CUR);

		int delta = ParamUtil.getInteger(
			renderRequest, SearchContainer.DEFAULT_DELTA_PARAM,
			SearchContainer.DEFAULT_DELTA);

		int start = ((currentPage > 0) ? (currentPage - 1) : 0) * delta;
		int end = start + delta;

		// Get sorting options.
		// Notice that this doesn't really sort on title because the field is
		// stored in XML. In real world this search would be integrated to the
		// search engine  to get localized sort options.

		String orderByCol = ParamUtil.getString(
			renderRequest, "orderByCol", "title");
		String orderByType = ParamUtil.getString(
			renderRequest, "orderByType", "asc");

		// Create comparator

		OrderByComparator<Task> comparator =
			OrderByComparatorFactoryUtil.create(
				"Task", orderByCol, !"asc".equals(orderByType));

		// Get keywords.
		// Notice that cleaning keywords is not implemented.

		String keywords = ParamUtil.getString(renderRequest, "keywords");

		// Call the service to get the list of tasks.

		List<Task> tasks = _taskService.getTasksByKeywords(
			themeDisplay.getScopeGroupId(), keywords, start, end, comparator);

		// Set request attributes.

		renderRequest.setAttribute(
			"taskCount",
			_taskService.getTasksCountByKeywords(
				themeDisplay.getScopeGroupId(), keywords));
		renderRequest.setAttribute("tasks", tasks);
	}

	@Reference
	private Portal _portal;

	@Reference
	private TaskService _taskService;

}