package actions.views;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 従業員情報について画面の入力値・出力値を扱うViewモデル
 *
 */
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
public class TimecardView {

    /**
     * id
     */
    private Integer id;

    /**
     * タイムカードを登録した従業員
     */
    private EmployeeView employee;

    /**
     * 登録内容の日付
     */
    private LocalDate timecardDate;

    /**
     * 出勤時間
     */
    private LocalTime clockIn;

    /**
     * 退勤時間
     */
    private LocalTime clockOut;

    /**
     * 勤務時間
     */
    private LocalTime workingHours;

    /**
     * 残業時間
     */
    private LocalTime overtime;

}