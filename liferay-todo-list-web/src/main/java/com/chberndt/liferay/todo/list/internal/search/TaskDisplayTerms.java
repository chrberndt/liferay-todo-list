package com.chberndt.liferay.todo.list.internal.search;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletRequest;

/**
 * @author Christian Berndt
 */
public class TaskDisplayTerms extends DisplayTerms {

	public static final String DESCRIPTION = "description";

	public static final String TITLE = "title";

	public TaskDisplayTerms(PortletRequest portletRequest) {
		super(portletRequest);

		title = ParamUtil.getString(portletRequest, TITLE);
		description = ParamUtil.getString(portletRequest, DESCRIPTION);
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	protected String description;
	protected String title;

}