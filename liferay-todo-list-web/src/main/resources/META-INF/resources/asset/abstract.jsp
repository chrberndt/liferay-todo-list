<%@ include file="/init.jsp" %>

<%
Task task = (Task)request.getAttribute(ToDoListWebKeys.TASK);
%>

<div class="asset-summary">

	<%
	AssetRenderer<?> assetRenderer = (AssetRenderer<?>)request.getAttribute(WebKeys.ASSET_RENDERER);
	%>

	<%= HtmlUtil.stripHtml(assetRenderer.getSummary(renderRequest, renderResponse)) %>
</div>