<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>


<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table { border-collapse: collapse; }
        th, td { border: 1px solid #888; padding: 6px 10px; }
        .ok { color: green; }
        .excess { color: red; }
    </style>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach items="${meals}" var="meal">
        <tr class="${meal.excess ? 'excess' : 'ok'}">
            <td>${fn:replace(meal.dateTime, 'T', ' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
