<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>

<body>

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
            </form>
        </c:when>
        <c:otherwise>
            <c:out value="${errorMessage}"/>
        </c:otherwise>
    </c:choose>

</div>

<script type="text/javascript">

    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays*24*60*60*1000));
        var expires = "expires="+d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    }

    var form = document.getElementById('autoSubmitSesamForm');

    if (form) {
        window.setTimeout(function () {
            setCookie('VGRSESSION', '', 0);
            form.submit();
        }, 500);
    }
</script>

</body>

</html>
