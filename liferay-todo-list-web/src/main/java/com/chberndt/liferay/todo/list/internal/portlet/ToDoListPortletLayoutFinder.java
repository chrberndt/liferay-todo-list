package com.chberndt.liferay.todo.list.internal.portlet;

import com.chberndt.liferay.todo.list.constants.TodoListPortletKeys;

import com.liferay.portal.kernel.portlet.BasePortletLayoutFinder;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "model.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = PortletLayoutFinder.class
)
public class ToDoListPortletLayoutFinder extends BasePortletLayoutFinder {

	@Override
	protected String[] getPortletIds() {
		return _PORTLET_IDS;
	}

	private static final String[] _PORTLET_IDS = {
		TodoListPortletKeys.TODO_LIST
	};

}