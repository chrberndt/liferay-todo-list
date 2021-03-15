package com.chberndt.liferay.todo.list.internal.search.spi.model.query.contributor;

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.query.QueryHelper;
import com.liferay.portal.search.spi.model.query.contributor.KeywordQueryContributor;
import com.liferay.portal.search.spi.model.query.contributor.helper.KeywordQueryContributorHelper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = KeywordQueryContributor.class
)
public class TaskKeywordQueryContributor implements KeywordQueryContributor {

	@Override
	public void contribute(
		String keywords, BooleanQuery booleanQuery,
		KeywordQueryContributorHelper keywordQueryContributorHelper) {

		SearchContext searchContext =
			keywordQueryContributorHelper.getSearchContext();

		_queryHelper.addSearchLocalizedTerm(
			booleanQuery, searchContext, Field.CONTENT, false);
		_queryHelper.addSearchLocalizedTerm(
			booleanQuery, searchContext, Field.TITLE, false);
	}

	@Reference
	private QueryHelper _queryHelper;

}