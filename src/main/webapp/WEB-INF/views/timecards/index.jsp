<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst, java.util.List, java.util.Date, java.util.HashMap, java.util.Map, java.time.LocalDate, java.time.LocalTime, java.util.Iterator, java.util.Set, actions.views.TimecardView" %>


<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actTim" value="${ForwardConst.ACT_TIM.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>タイムカード　一覧</h2>

        <select>
        <c:forEach begin="2000" end="2030" step="1" var="i">
        <option value="year${i}"><c:out value="${i}"/></option>
        </c:forEach>
        </select>
        年
        <select>
        <c:forEach begin="1" end="12" step="1" var="i">
        <option value="${i}"<c:if test="${i == timecard.clockOut.getMinute()}"> selected </c:if>><c:if test="${i < 10}">0</c:if><c:out value="${i}"/></option>
        </c:forEach>
        </select>
        月
        <br />

        <table id="timecard_list">
            <tbody>
                <tr>
                    <th class="timecard_date">日付</th>
                    <th class="timecard_clock_in">出勤</th>
                    <th class="timecard_clock_out">退勤</th>
                    <th class="timecard_working_hours">勤務時間</th>
                    <th class="timecard_overtime">残業</th>
                    <th class="timecard_action">編集</th>
                </tr>

                  <c:forEach var="day" items="${days}" varStatus="status">
                  <tr class="row">
                  <fmt:parseDate value="${day.keySet().iterator().next() }" pattern="yyyy-MM-dd" var="timecardDay" type="date" />
                      <td class="timecard_date"><fmt:formatDate value='${timecardDay}' pattern='yyyy-MM-dd' /></td>
                      <c:choose>
                      <c:when test="${day.values().iterator().next()} == null}">
                        <td class="timecard_clock_in"></td>
                        <td class="timecard_clock_out"></td>
                        <td class="timecard_working_hours"></td>
                        <td class="timecard_overtime"></td>
                      </c:when>
                      <c:otherwise>
                        <td class="timecard_clock_in"><c:out value="${day.values().iterator().next().getClockIn()}"/></td>
                        <td class="timecard_clock_out"><c:out value="${day.values().iterator().next().getClockOut()}"/></td>
                        <td class="timecard_working_hours"><c:out value="${day.values().iterator().next().getWorkingHours()}"/></td>
                        <td class="timecard_overtime"><c:out value="${day.values().iterator().next().getOvertime()}"/></td>
                      </c:otherwise>
                      </c:choose>
                      <td class="timecard_action"><a href="<c:url value='?action=${actTim}&command=${commEdit}&year=${day.keySet().iterator().next().getYear()}&month=${day.keySet().iterator().next().getMonthValue()}&day=${day.keySet().iterator().next().getDayOfMonth()}' />">編集</a></td>
                    </tr>
                  </c:forEach>
             </tbody>
          </table>

    </c:param>
</c:import>