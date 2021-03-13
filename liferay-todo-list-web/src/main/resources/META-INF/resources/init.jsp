<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.chberndt.liferay.todo.list.constants.ToDoListPortletKeys" %><%@
page import="com.chberndt.liferay.todo.list.exception.NoSuchTaskException" %><%@
page import="com.chberndt.liferay.todo.list.exception.TaskDueDateException" %><%@
page import="com.chberndt.liferay.todo.list.exception.TaskTitleException" %><%@
page import="com.chberndt.liferay.todo.list.internal.display.context.TasksDisplayContext" %><%@
page import="com.chberndt.liferay.todo.list.internal.display.context.TasksManagementToolbarDisplayContext" %><%@
page import="com.chberndt.liferay.todo.list.internal.util.WebKeys" %><%@
page import="com.chberndt.liferay.todo.list.model.Task" %><%@
page import="com.chberndt.liferay.todo.list.web.constants.ToDoListWebKeys" %>

<%@ page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.bean.BeanParamUtil" %><%@
page import="com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker" %><%@
page import="com.liferay.portal.kernel.dao.search.SearchContainer" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.PortalUtil" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.taglib.search.ResultRow" %><%@
page import="com.liferay.taglib.util.LexiconUtil" %>

<%@ page import="java.util.Date" %><%@
page import="java.util.HashMap" %><%@
page import="java.util.Map" %>

<%@ page import="javax.portlet.PortletRequest" %><%@
page import="javax.portlet.PortletURL" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
	String textProperty = GetterUtil.getString(portletPreferences.getValue("textProperty", null));
	boolean booleanProperty = GetterUtil.getBoolean(portletPreferences.getValue("booleanProperty", null));
%>