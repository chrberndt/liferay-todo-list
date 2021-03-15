package com.chberndt.liferay.todo.list.internal.search.spi.model.result.contributor;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.search.spi.model.result.contributor.ModelSummaryContributor;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = ModelSummaryContributor.class
)
public class TaskModelSummaryContributor implements ModelSummaryContributor {

	@Override
	public Summary getSummary(
		Document document, Locale locale, String snippet) {

		String languageId = LocaleUtil.toLanguageId(locale);

		return _createSummary(
			document,
			LocalizationUtil.getLocalizedName(Field.CONTENT, languageId),
			LocalizationUtil.getLocalizedName(Field.TITLE, languageId));
	}

	private Summary _createSummary(
		Document document, String contentField, String titleField) {

		String prefix = Field.SNIPPET + StringPool.UNDERLINE;

		Summary summary = new Summary(
			document.get(prefix + titleField, titleField),
			document.get(prefix + contentField, contentField));

		summary.setMaxContentLength(200);

		return summary;
	}

}