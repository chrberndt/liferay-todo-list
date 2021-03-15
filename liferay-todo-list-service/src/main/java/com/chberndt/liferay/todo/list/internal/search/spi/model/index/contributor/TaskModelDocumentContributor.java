package com.chberndt.liferay.todo.list.internal.search.spi.model.index.contributor;

import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = ModelDocumentContributor.class
)
public class TaskModelDocumentContributor
	implements ModelDocumentContributor<Task> {

	@Override
	public void contribute(Document document, Task task) {
		String content = HtmlUtil.extractText(task.getDescription());

		document.addText(Field.CONTENT, content);

		document.addText(Field.DESCRIPTION, task.getDescription());

		// TODO

		//		document.addDate(Field.DISPLAY_DATE, task.getDisplayDate());
		document.addDate(Field.MODIFIED_DATE, task.getModifiedDate());
		document.addText(Field.TITLE, task.getTitle());
		//		document.addKeywordSortable("urlTitle", task.getUrlTitle());

		for (Locale locale :
				LanguageUtil.getAvailableLocales(task.getGroupId())) {

			String languageId = LocaleUtil.toLanguageId(locale);

			document.addText(
				LocalizationUtil.getLocalizedName(Field.CONTENT, languageId),
				content);
			document.addText(
				LocalizationUtil.getLocalizedName(Field.TITLE, languageId),
				task.getTitle());
		}
	}

}