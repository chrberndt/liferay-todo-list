package com.chberndt.liferay.todo.list.internal.application.list;

import com.chberndt.liferay.todo.list.constants.ToDoListPortletKeys;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=200",
		"panel.category.key=" + PanelCategoryKeys.SITE_ADMINISTRATION_CONTENT
	},
	service = PanelApp.class
)
public class ToDoListPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return ToDoListPortletKeys.TODO_LIST;
	}

}