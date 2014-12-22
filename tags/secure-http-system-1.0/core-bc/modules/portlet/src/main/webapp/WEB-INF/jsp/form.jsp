<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<portlet:renderURL var="sesamAction">

</portlet:renderURL>

<style type="text/css">

    #autoSubmitSesamForm {
        font-size: 1.2em;
    }

    #autoSubmitSesamForm input[type="submit"] {
        background: none;
        border: none;
        color: #0066ff;
        text-decoration: underline;
        cursor: pointer;
        padding: 0;
    }
</style>

<div>
    <c:choose>
        <c:when test="${not empty base64SignedXml}">
            <form id="autoSubmitSesamForm" action="${targetUrl}" method="post">
                <span>Sesam öppnas i nytt fönster. Klicka <input type="submit" value="här"/> om det inte öppnas automatiskt.</span>
                <input type="hidden" name="data" value="${base64SignedXml}"/>
                    <%--<input type="submit" value="Fortsätt till Sesam"/>--%>
            </form>
        </c:when>
        <c:otherwise>
            <c:out value="${errorMessage}"/>
        </c:otherwise>
    </c:choose>

</div>

<script type="text/javascript">
    var form = document.getElementById('autoSubmitSesamForm');
    if (form) {
        form.submit();
    }
</script>