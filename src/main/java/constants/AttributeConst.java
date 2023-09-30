package constants;

/**
 * 画面の項目値等を定義するEnumクラス
 *
 */
public enum AttributeConst {

    //フラッシュメッセージ
    FLUSH("flush"),

    //一覧画面共通
    MAX_ROW("maxRow"),
    PAGE("page"),

    //入力フォーム共通
    TOKEN("_token"),
    ERR("errors"),

    //ログイン中の従業員
    LOGIN_EMP("login_employee"),

    //ログイン画面
    LOGIN_ERR("loginError"),

    //従業員管理
    EMPLOYEE("employee"),
    EMPLOYEES("employees"),
    EMP_COUNT("employees_count"),
    EMP_ID("id"),
    EMP_CODE("code"),
    EMP_PASS("password"),
    EMP_NAME("name"),
    EMP_ADMIN_FLG("admin_flag"),

    //管理者フラグ
    ROLE_ADMIN(1),
    ROLE_GENERAL(0),

    //削除フラグ
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0),

    //日報管理
    REPORT("report"),
    REPORTS("reports"),
    REP_COUNT("reports_count"),
    REP_ID("id"),
    REP_DATE("report_date"),
    REP_TITLE("title"),
    REP_CONTENT("content_msg"),

    //タイムカード
    TIMECARD("timecard"),
    TIMECARDS("timecards"),
    TIM_DATE("timecard_date"),
    TIM_EMP_ID("employee_id"),
    TIM_CLOCK_IN("clock_in"),
    TIM_CLOCK_OUT("clock_out"),
    TIM_WORKING_HOURS("working_hours"),
    TIM_OVERTIME("overtime"),
    TIM_YEAR("year"),
    TIM_MONTH("month"),
    TIM_DAY("day"),
    TIM_DAYS("days"),
    TIM_CLOCK_IN_H("clock_in_hour"),
    TIM_CLOCK_IN_M("clock_in_minute"),
    TIM_CLOCK_OUT_H("clock_out_hour"),
    TIM_CLOCK_OUT_M("clock_out_minute"),
    TIM_WORKING_HOURS_H("working_hours_hour"),
    TIM_WORKING_HOURS_M("working_hours_minute");

    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }

    public Integer getIntegerValue() {
        return this.i;
    }

}