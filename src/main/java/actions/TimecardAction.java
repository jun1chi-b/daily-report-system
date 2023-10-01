package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.TimecardView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import services.TimecardService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class TimecardAction extends ActionBase {

    private TimecardService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new TimecardService();

        //メソッドを実行
        invoke();
        service.close();
    }
    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {
        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //指定されたページ数の一覧画面に表示するデータを取得
        int year = toNumber(getRequestParam(AttributeConst.TIM_YEAR));
        int month = toNumber(getRequestParam(AttributeConst.TIM_MONTH));
        int allDay = getAllDay(year,month);
        LocalDate firstDay = LocalDate.of(year,month,1);
        LocalDate lastDay = LocalDate.of(year,month,allDay);

        List<TimecardView> timecards = service.getMonthTimecard(ev,firstDay,lastDay);
        List<Map<LocalDate,TimecardView>> days = getDayList(year,month,allDay,timecards);

        putRequestScope(AttributeConst.TIM_DAYS,days);
        putRequestScope(AttributeConst.TIM_YEAR, year);
        putRequestScope(AttributeConst.TIM_MONTH, month);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_TIM_INDEX);

    } //追記
    /**
     * 月毎の最終日を取得する
     */
    public int getAllDay(int year,int month) {
        int lastDay = 31;
        switch(month){
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
            lastDay = 31;
            break;
        case 4:
        case 6:
        case 9:
        case 11:
            lastDay = 30;
            break;
        case 2:
            if(year % 4 == 0 && !(year % 100 == 0 && year % 400 != 0)) {
                lastDay = 29;
            }else {
                lastDay = 28;
            }
            break;
        }
        return lastDay;
    }

    public List<Map<LocalDate,TimecardView>> getDayList(int year,int month,int allDay, List<TimecardView> tv){
        List<Map<LocalDate,TimecardView>> days = new ArrayList<Map<LocalDate,TimecardView>>();

        for(int i = 1;i <= allDay;i++) {
            Map<LocalDate,TimecardView> m = new HashMap<LocalDate,TimecardView>();
            LocalDate day = LocalDate.of(year, month,i);
            m.put(day, null);
            for(TimecardView timecard : tv) {
                LocalDate date = timecard.getTimecardDate();
                if(day.isEqual(date)) {
                    m.replace(day, timecard);
                    break;
                }
            }
            days.add(m);
        }

        return days;

    }


    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //指定された日付のタイムカードを取得
        int year = toNumber(getRequestParam(AttributeConst.TIM_YEAR));
        int month = toNumber(getRequestParam(AttributeConst.TIM_MONTH));
        int day = toNumber(getRequestParam(AttributeConst.TIM_DAY));
        LocalDate date = LocalDate.of(year, month, day);

        TimecardView tv = service.getTimecard(ev, date);

        if ((tv.getEmployee() != null) && (ev.getId() != tv.getEmployee().getId())) {
            //ログインしている従業員が日報の作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.TIMECARD, tv); //取得した日報データ

            //編集画面を表示
            forward(ForwardConst.FW_TIM_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        //if (checkToken()) {

            //入力された日報内容を設定する
            LocalDate date = toLocalDate(getRequestParam(AttributeConst.TIM_DATE));
            LocalTime clockIn = LocalTime.of(toNumber(getRequestParam(AttributeConst.TIM_CLOCK_IN_H)),toNumber(getRequestParam(AttributeConst.TIM_CLOCK_IN_M)));
            LocalTime clockOut = LocalTime.of(toNumber(getRequestParam(AttributeConst.TIM_CLOCK_OUT_H)), toNumber(getRequestParam(AttributeConst.TIM_CLOCK_OUT_M)));
            LocalTime workingHours = LocalTime.of(toNumber(getRequestParam(AttributeConst.TIM_WORKING_HOURS_H)), toNumber(getRequestParam(AttributeConst.TIM_WORKING_HOURS_M)));
            LocalTime overtime = null;
            if(workingHours.isAfter(LocalTime.of(8,0))) {
                overtime = workingHours.minusHours(8);
            }
            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //idを条件に日報データを取得する
            TimecardView tv = service.getTimecard(ev,date);

            //
            List<String> errors = new ArrayList<String>();
            if(tv.getId() == null) {
                tv.setEmployee(ev);
                tv.setClockIn(clockIn);
                tv.setClockOut(clockOut);
                tv.setWorkingHours(workingHours);
                tv.setOvertime(overtime);
                errors = service.create(tv);
            }else {
                tv.setClockIn(clockIn);
                tv.setClockOut(clockOut);
                tv.setWorkingHours(workingHours);
                tv.setOvertime(overtime);
                errors = service.update(tv);
            }

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.TIMECARD, tv); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_TIM_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);

            }
        }
   // }

}