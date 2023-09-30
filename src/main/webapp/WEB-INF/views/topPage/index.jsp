<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst, java.time.LocalDate" %>


<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}" />
<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="actTim" value="${ForwardConst.ACT_TIM.getValue()}" />

<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commClockIn" value="${ForwardConst.CMD_CLOCK_IN.getValue()}" />
<c:set var="commClockOut" value="${ForwardConst.CMD_CLOCK_OUT.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日報管理システムへようこそ</h2>
        <h3>本日のタイムカード</h3>
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
                <tr class="row">
                    <fmt:parseDate value="${timecard.timecardDate}" pattern="yyyy-MM-dd" var="timecardDay" type="date" />
                    <td class="timecard_date"><fmt:formatDate value='${timecardDay}' pattern='yyyy-MM-dd' /></td>
                    <c:choose>
                      <c:when test="${timecard.clockIn == null}">
                        <td class="timecard_clock_in"><button onclick='location.href="<c:url value='?action=${actTop}&command=${commClockIn}' />"'>出勤</button></td>
                        <td class="timecard_clock_out"></td>
                        <td class="timecard_working_hours"></td>
                        <td class="timecard_overtime"></td>
                      </c:when>
                      <c:when test="${timecard.clockOut == null}">
                        <td class="timecard_clock_in"><c:out value="${timecard.clockIn}" /></td>
                        <td class="timecard_clock_out"><button onclick='location.href="<c:url value='?action=${actTop}&command=${commClockOut}' />"'>退勤</button></td>
                        <td class="timecard_working_hours"></td>
                        <td class="timecard_overtime"></td>
                      </c:when>
                      <c:otherwise>
                        <td class="timecard_clock_in"><c:out value="${timecard.clockIn}" /></td>
                        <td class="timecard_clock_out"><c:out value="${timecard.clockOut}" /></td>
                        <td class="timecard_working_hours"><c:out value="${timecard.workingHours}" /></td>
                        <td class="timecard_overtime"><c:out value="${timecard.overtime}" /></td>
                      </c:otherwise>
                      </c:choose>
                      <td class="timecard_action"><a href="<c:url value='?action=${actTim}&command=${commEdit}&year=${timecard.timecardDate.getYear()}&month=${timecard.timecardDate.getMonthValue()}&day=${timecard.timecardDate.getDayOfMonth()}' />">編集</a></td>
                   </tr>
             </tbody>
        </table>
        <h3>【自分の日報　一覧】</h3>
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actTop}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actRep}&command=${commNew}' />">新規日報の登録</a></p>
    </c:param>
</c:import>