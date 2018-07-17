package custom.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {


    /**
     * 获取指定时间。
     * 目前需求：不能预约当天的
     *
     * @return
     */
    public static List<String> daysSpan(int span, boolean today) {
        List<String> list = new ArrayList<>();
        if (today) {
            for (int i = 0; i < span; i++) {
                list.add(getFetureDate(i));

            }
        } else {
            for (int i = 1; i <= span; i++) {
                list.add(getFetureDate(i));

            }
        }
        return list;
    }

    public  static  List<String> daysSpan(LocalDateTime localDate ,int span){
        List<String> list = new ArrayList<>();
        for (int i=0;i<span;i++){
            list.add(localDate.plusDays(i).format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return list;
    }

    /**
     * 计算从今天算起，今后日期的字符串值
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        return LocalDate.now().plusDays(past).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 获取本月的最大天数
     */

    public static int getDaysOfMonth() {
        Calendar calendar = Calendar.getInstance();
        int a = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        int b = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(a);
        System.out.println(b);
        return 1;
    }

    public static String getWeekSay(DayOfWeek dayOfWeek) {

        String week = null;
        switch (dayOfWeek) {
            case SUNDAY:
                week = "周日";
                break;
            case MONDAY:
                week = "周一";
                break;
            case TUESDAY:
                week = "周二";
                break;
            case WEDNESDAY:
                week = "周三";
                break;
            case THURSDAY:
                week = "周四";
                break;
            case FRIDAY:
                week = "周五";
                break;
            case SATURDAY:
                week = "周六";
                break;

        }
        return week;
    }

    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }
    public static void main(String[] args) {
        System.out.println(daysSpan(LocalDateTime.now(),7));
    }

}
