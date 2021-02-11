package com.chberndt.liferay.todo.list.internal.search;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;

import javax.portlet.PortletRequest;

public class TaskSearchTerms extends TaskDisplayTerms {

	public TaskSearchTerms(PortletRequest portletRequest) {
		super(portletRequest);

		title = DAOParamUtil.getString(portletRequest, TITLE);
		description = DAOParamUtil.getString(portletRequest, description);
	}

}