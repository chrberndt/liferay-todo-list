package com.chberndt.liferay.todo.list.internal.application.list;

import com.chberndt.liferay.todo.list.constants.TodoListPortletKeys;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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
		return TodoListPortletKeys.TODO_LIST;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + TodoListPortletKeys.TODO_LIST + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}