package com.chberndt.liferay.todo.list.internal.search;

import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

/**
 * @
 */
public class TaskSearch extends SearchContainer<Task> {

	public static final String EMPTY_RESULTS_MESSAGE = "there-are-no-results";

	public static List<String> headerNames = new ArrayList<String>() {
		{
			add("title");
			add("description");
		}
	};
	public static Map<String, String> orderableHeaders =
		new HashMap<String, String>() {
			{
				put("description", "description");
				put("title", "title");
			}
		};

	public TaskSearch(PortletRequest portletRequest, PortletURL iteratorURL) {
		this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
	}

	public TaskSearch(
		PortletRequest portletRequest, String curParam,
		PortletURL iteratorURL) {

		super(
			portletRequest, new TaskDisplayTerms(portletRequest),
			new TaskSearchTerms(portletRequest), curParam, DEFAULT_DELTA,
			iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		TaskDisplayTerms displayTerms = (TaskDisplayTerms)getDisplayTerms();

		String portletId = PortletProviderUtil.getPortletId(
			Task.class.getName(), PortletProvider.Action.VIEW);

		iteratorURL.setParameter(
			TaskDisplayTerms.TITLE, displayTerms.getTitle());
		iteratorURL.setParameter(
			TaskDisplayTerms.DESCRIPTION, displayTerms.getDescription());

		try {
			PortalPreferences preferences =
				PortletPreferencesFactoryUtil.getPortalPreferences(
					portletRequest);

			String orderByCol = ParamUtil.getString(
				portletRequest, "orderByCol");
			String orderByType = ParamUtil.getString(
				portletRequest, "orderByType");

			if (Validator.isNotNull(orderByCol) &&
				Validator.isNotNull(orderByType)) {

				preferences.setValue(
					portletId, "tasks-order-by-col", orderByCol);
				preferences.setValue(
					portletId, "tasks-order-by-type", orderByType);
			}
			else {
				orderByCol = preferences.getValue(
					portletId, "tasks-order-by-col", "title");
				orderByType = preferences.getValue(
					portletId, "tasks-order-by-type", "asc");
			}

			// Create comparator

			OrderByComparator<Task> orderByComparator =
				OrderByComparatorFactoryUtil.create(
					"Task", orderByCol, !"asc".equals(orderByType));

			setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);
			setOrderByComparator(orderByComparator);
		}
		catch (Exception exception) {
			_log.error("Unable to initialize task search", exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(TaskSearch.class);

}