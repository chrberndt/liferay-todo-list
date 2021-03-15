package com.chberndt.liferay.todo.list.internal.search;

import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.portal.kernel.search.BaseSearcher;
import com.liferay.portal.kernel.search.Field;

import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "model.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = BaseSearcher.class
)
public class TaskSearcher extends BaseSearcher {

	public TaskSearcher() {
		setDefaultSelectedFieldNames(
			Field.ASSET_TAG_NAMES, Field.COMPANY_ID, Field.CONTENT,
			Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK, Field.GROUP_ID,
			Field.MODIFIED_DATE, Field.SCOPE_GROUP_ID, Field.TITLE, Field.UID);
		setFilterSearch(true);
		setPermissionAware(true);
	}

	@Override
	public String getClassName() {
		return _CLASS_NAME;
	}

	private static final String _CLASS_NAME = Task.class.getName();

}