package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Timecard;

/**
 * タイムカードのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class TimecardConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv TimecardViewのインスタンス
     * @return Timecardのインスタンス
     */
    public static Timecard toModel(TimecardView tv) {
        return new Timecard(
                tv.getId(),
                EmployeeConverter.toModel(tv.getEmployee()),
                tv.getTimecardDate(),
                tv.getClockIn(),
                tv.getClockOut(),
                tv.getWorkingHours(),
                tv.getOvertime());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param t Timecardのインスタンス
     * @return TimecardViewのインスタンス
     */
    public static TimecardView toView(Timecard t) {

        if (t == null) {
            return null;
        }

        return new TimecardView(
                t.getId(),
                EmployeeConverter.toView(t.getEmployee()),
                t.getTimecardDate(),
                t.getClockIn(),
                t.getClockOut(),
                t.getWorkingHours(),
                t.getOvertime());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<TimecardView> toViewList(List<Timecard> list) {
        List<TimecardView> tvs = new ArrayList<>();

        for (Timecard t : list) {
            tvs.add(toView(t));
        }

        return tvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Timecard t, TimecardView tv) {
        t.setId(tv.getId());
        t.setEmployee(EmployeeConverter.toModel(tv.getEmployee()));
        t.setTimecardDate(tv.getTimecardDate());
        t.setClockIn(tv.getClockIn());
        t.setClockOut(tv.getClockOut());
        t.setWorkingHours(tv.getWorkingHours());
        t.setOvertime(tv.getOvertime());

    }

}