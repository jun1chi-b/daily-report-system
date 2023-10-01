package models.validators;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import actions.views.TimecardView;
import constants.MessageConst;

/**
 * 日報インスタンスに設定されている値のバリデーションを行うクラス
 */
public class TimecardValidator {
    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(TimecardView tv) {
        List<String> errors = new ArrayList<String>();

        //タイトルのチェック
        String clockError = validateClock(tv.getClockIn(),tv.getClockOut());
        if (!clockError.equals("")) {
            errors.add(clockError);
        }

        return errors;
    }

    /**
     * 出勤時間が退勤時間より後になってないかチェック
     * @param clockIn 出勤時間
     * @param clockOut 退勤時間
     * @return エラーメッセージ
     */
    private static String validateClock(LocalTime clockIn, LocalTime clockOut) {
        if(clockIn != null && clockOut != null) {
            if (clockIn.isAfter(clockOut)) {
                return MessageConst.E_CLOCK.getMessage();
            }
        }

        //入力値がある場合は空文字を返却
        return "";
    }
}
