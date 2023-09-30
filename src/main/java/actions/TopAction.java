package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List; //追記

import javax.servlet.ServletException;

import actions.views.EmployeeView; //追記
import actions.views.ReportView; //追記
import actions.views.TimecardView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;  //追記
import constants.MessageConst;
import services.ReportService;  //追記
import services.TimecardService;

/**
 * トップページに関する処理を行うActionクラス
 *
 */
public class TopAction extends ActionBase {

    private ReportService service; //追記
    private TimecardService t_service; //タイムカード追記

    /**
     * indexメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService(); //追記
        t_service = new TimecardService(); //タイムカード追記
        //メソッドを実行
        invoke();

        service.close(); //追記
        t_service.close(); //タイムカード追記
    }

    /**
     * 一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        // 以下追記

        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //ログイン中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<ReportView> reports = service.getMinePerPage(loginEmployee, page);

        //ログイン中の従業員が作成した日報データの件数を取得
        long myReportsCount = service.countAllMine(loginEmployee);

        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, myReportsCount); //ログイン中の従業員が作成した日報の数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //↑ここまで追記

        //タイムカード追記
        //今日の日付を設定
        LocalDate day = LocalDate.now();
        //日付と従業員を条件に本日のタイムカードを取得する
        TimecardView tv = t_service.getTimecard(loginEmployee, day);

        putRequestScope(AttributeConst.TIMECARD, tv);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_TOP_INDEX);
    }
    /**
     * 出勤時間を登録する
     */
    public void clockIn() throws ServletException, IOException {

            //今日の日付を設定
            LocalDate day = LocalDate.now();

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //現在時刻を取得し、5分単位で繰り上げをする
            LocalTime time = LocalTime.of(LocalTime.now().getHour(),LocalTime.now().getMinute());
            if(time.getMinute() % 5 != 0){
                time = time.plusMinutes(5 - time.getMinute() % 5);
            }

            //パラメータの値をもとに日報情報のインスタンスを作成する
            TimecardView tv = new TimecardView(
                    null,
                    ev, //ログインしている従業員を、日報作成者として登録する
                    day,
                    time,
                    null,
                    null,
                    null);

            //日報情報登録
            List<String> errors = t_service.clockIn(tv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.TIMECARD, tv);//入力された日報情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //トップ画面を再表示
                forward(ForwardConst.FW_TOP_INDEX);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_CLOCK_IN.getMessage());

              //トップ画面にリダイレクト
                redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);
            }
        }

    /**
     * 退勤時間を登録する
     */
    public void clockOut() throws ServletException, IOException {

            //今日の日付を設定
            LocalDate day = LocalDate.now();

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //従業員と今日の日付を条件にタイムカードデータを取得する
            TimecardView tv = t_service.getTimecard(ev,day);

            //各種時刻を取得する
            //現在時刻を取得し、5分単位で繰り下げをする
            LocalTime time = LocalTime.of(LocalTime.now().getHour(),LocalTime.now().getMinute());
            if(time.getMinute() % 5 != 0){
                time = time.minusMinutes(time.getMinute() % 5);
            }
            // 勤務時間・残業時間
            LocalTime working = tv.getClockIn().minusHours(time.getHour()).minusMinutes((time.getMinute()));

            if(working.isAfter(LocalTime.of(8,0))) {
                working = working.minusHours(1);
            }else if(working.isAfter(LocalTime.of(6,0))){
                working = working.minusMinutes(45);
            }

            LocalTime overtime = null;
            if(working.isAfter(LocalTime.of(8,0))) {
                overtime = working.minusHours(8);
            }
            //入力された日報内容を設定する
            tv.setClockOut(time);
            tv.setWorkingHours(working);
            tv.setOvertime(overtime);

            //日報データを更新する
            List<String> errors = t_service.update(tv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.TIMECARD, tv); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_TOP_INDEX);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_CLOCK_OUT.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);

            }
    }


}