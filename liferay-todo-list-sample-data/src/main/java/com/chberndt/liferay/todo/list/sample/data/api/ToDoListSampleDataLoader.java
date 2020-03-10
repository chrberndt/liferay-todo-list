package com.chberndt.liferay.todo.list.sample.data.api;

import com.chberndt.liferay.todo.list.service.TaskLocalService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.Date;
import java.util.Locale;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author berndt
 */
@Component
public class ToDoListSampleDataLoader {

	protected ServiceContext getServiceContext() throws PortalException {

		// This strategy is limited to single instance configurations

		long defaultCompanyId = _portal.getDefaultCompanyId();

		User user = _userLocalService.getDefaultUser(defaultCompanyId);

		Locale locale = LocaleUtil.getSiteDefault();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(defaultCompanyId);
		serviceContext.setLanguageId(_language.getLanguageId(locale));
		serviceContext.setScopeGroupId(defaultCompanyId);
		serviceContext.setTimeZone(user.getTimeZone());
		serviceContext.setUserId(user.getUserId());

		return serviceContext;
	}

	@Activate
	private void activate() throws Exception {
		_log.info("activate()");

		ServiceContext serviceContext = getServiceContext();

		_taskLocalService.addTask(
			serviceContext.getUserId(), "Meet Robert", "At Harry's Bar", false,
			new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 5),
			serviceContext);
		_taskLocalService.addTask(
			serviceContext.getUserId(), "Catherine's birthday", "Buy flowers",
			false, new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 2),
			serviceContext);
		_taskLocalService.addTask(
			serviceContext.getUserId(), "Pick up Rosalind",
			"Kindergarten closes at 5pm", false,
			new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 3),
			serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ToDoListSampleDataLoader.class);

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private TaskLocalService _taskLocalService;

	@Reference
	private UserLocalService _userLocalService;

}