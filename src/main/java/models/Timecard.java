package models;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日報データのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_TIM)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_TIM_GET_TIMECARD,
            query = JpaConst.Q_TIM_GET_TIMECARD_DEF),
    @NamedQuery(
            name = JpaConst.Q_TIM_GET_MONTH_TIMECARD,
            query = JpaConst.Q_TIM_GET_MONTH_TIMECARD_DEF)

})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Timecard {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.TIM_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * タイムカードを登録した従業員
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.TIM_COL_EMP, nullable = false)
    private Employee employee;

    /**
     * 登録内容の日付
     */
    @Column(name = JpaConst.TIM_COL_TIM_DATE, nullable = false)
    private LocalDate timecardDate;

    /**
     * 出勤時間
     */
    @Column(name = JpaConst.TIM_CLOCK_IN)
    private LocalTime clockIn;

    /**
     * 退勤時間
     */
    @Column(name = JpaConst.TIM_CLOCK_OUT)
    private LocalTime clockOut;

    /**
     * 勤務時間
     */
    @Column(name = JpaConst.TIM_WORKING_HOURS)
    private LocalTime workingHours;

    /**
     * 残業時間
     */
    @Column(name = JpaConst.TIM_OVERTIME)
    private LocalTime overtime;



}