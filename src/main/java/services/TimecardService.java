package services;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.TimecardConverter;
import actions.views.TimecardView;
import constants.JpaConst;
import models.Timecard;
import models.validators.TimecardValidator;

/**
 * 従業員テーブルの操作に関わる処理を行うクラス
 */
public class TimecardService extends ServiceBase {

    /**
     * 本日のタイムカードを取得する
     * @param employee 従業員
     * @param day 本日の日付
     * @return バリデーションで発生したエラーのリスト
     */
    public TimecardView getTimecard(EmployeeView employee,LocalDate day){
        Timecard t = null;
        try {
            t = em.createNamedQuery(JpaConst.Q_TIM_GET_TIMECARD, Timecard.class)
                    .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                    .setParameter(JpaConst.TIM_COL_TIM_DATE, day)
                    .getSingleResult();
        }catch (NoResultException ex) {
            t = new Timecard(null,null,day,null,null,null,null);
        }
         return TimecardConverter.toView(t);
    }
    /**
     * 指定された年月のタイムカードを取得する
     * @param firstDay 指定した年月の初日
     * @param lastDay 指定した年月の最終日
     * @return バリデーションで発生したエラーのリスト
     */
    public List<TimecardView> getMonthTimecard(EmployeeView employee,LocalDate firstDay, LocalDate lastDay){
        List<Timecard> timecards = null;
        try {
            timecards = em.createNamedQuery(JpaConst.Q_TIM_GET_MONTH_TIMECARD, Timecard.class)
                    .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                    .setParameter(JpaConst.JPQL_PARM_FIRST_DAY,firstDay)
                    .setParameter(JpaConst.JPQL_PARM_LAST_DAY, lastDay)
                    .getResultList();
        }catch (NoResultException ex) {
        }
         return TimecardConverter.toViewList(timecards);
    }


    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録する
     * @param rv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> clockIn(TimecardView tv) {
        List<String> errors = TimecardValidator.validate(tv);
        if(errors.size() == 0) {
        createInternal(tv);
        }
        return errors;
    }
    /**
     * 編集したタイムカードが未登録の場合、新規登録する
     * @param tv タイムカードの登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(TimecardView tv) {
        List<String> errors = TimecardValidator.validate(tv);
        if(errors.size() == 0) {
        createInternal(tv);
        }
        return errors;
    }


    /**
     * タイムデータを更新する
     * @param tv タイムカードの更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(TimecardView tv) {

        List<String> errors = TimecardValidator.validate(tv);
        if (errors.size() == 0) {
            updateInternal(tv);
        }
        return errors;
    }

    /**
     * タイムカードデータを1件登録する
     * @param tv タイムカードデータ
     */
    private void createInternal(TimecardView tv) {

        em.getTransaction().begin();
        em.persist(TimecardConverter.toModel(tv));
        em.getTransaction().commit();

    }

    private void updateInternal(TimecardView tv) {

        em.getTransaction().begin();
        Timecard t = findOneInternal(tv.getId());
        TimecardConverter.copyViewToModel(t, tv);
        em.getTransaction().commit();

    }
    /**
     * idを条件に取得したデータをTimecardViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public TimecardView findOne(int id) {
        return TimecardConverter.toView(findOneInternal(id));
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Timecard findOneInternal(int id) {
        return em.find(Timecard.class, id);
    }

}
