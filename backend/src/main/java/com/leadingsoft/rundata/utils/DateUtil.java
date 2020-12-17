package com.leadingsoft.rundata.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

  public static final String TIME_FORMAT_YYYYMMDDHHMMSS = "yyyy/MM/dd HH:mm:ss";

  public static final String TIME_FORMAT_YYYYMMDD = "yyyy/MM/dd";


  public static String formatTimestampVal(Integer tsValInt, String format) {

    Timestamp timestampVal = new Timestamp(tsValInt.longValue() * 1000);

    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPAN);
    String fmtValue = sdf.format(timestampVal);

    return fmtValue;
  }


  /**
   *
   * @param dateVal
   * @return
   */
  public static Date formatDateStringVal(String dateVal) {

    return formatDateStringVal(dateVal, TIME_FORMAT_YYYYMMDD);
  }

  /**
   *
   * @param dateVal
   * @param format
   * @return
   */
  public static Date formatDateStringVal(String dateVal, String format) {

    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = null;
    try {
      date = sdf.parse(dateVal);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

//  public static void main(String[] args) {
//
//    String value = DateUtil.formatTimestampVal(1603555200, "yyyy-MM-dd HH:mm:ss");
//    System.out.println(value);
//  }
}
