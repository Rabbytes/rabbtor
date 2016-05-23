<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <body>
        Hi
        <p>Request: ${request}</p>

        <p>Response: ${response}</p>

        <p>Session: ${session}</p>

        <p>Session: ${applicationContext}</p>

        <g:set var="name" scope="request" value="100"/>
        <t:write/>

        <g:elm tag="div" id="id" name="${200}">Bu div</g:elm>

        <t:done/>
        <t:done/>
        <t:done/>

        <p>
        <g:render template="/test" />
        </p>

        ${'<a href="#" >Link</a>'}
    </body>
</html>