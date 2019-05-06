package com.hy.util;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class DateUtil
{
  private static final java.text.SimpleDateFormat sdfYear = new java.text.SimpleDateFormat("yyyy");
  
  private static final java.text.SimpleDateFormat sdfDay = new java.text.SimpleDateFormat(
    "yyyy-MM-dd");
  
  private static final java.text.SimpleDateFormat sdfDays = new java.text.SimpleDateFormat(
    "yyyyMMdd");
  
  private static final java.text.SimpleDateFormat sdfTime = new java.text.SimpleDateFormat(
    "yyyy-MM-dd HH:mm:ss");
  
  private static final java.text.SimpleDateFormat sdftimes = new java.text.SimpleDateFormat(
    "yyyyMMddHHmmss");
  
  public static String getYear()
  {
    return sdfYear.format(new java.util.Date());
  }
  
  public static String getDay()
  {
    return sdfDay.format(new java.util.Date());
  }
  
  	/**
  	 * 
  	* <b>Description:</b><br> 获取批次号： 由前台下单生成，格式为：年+月+批次编码(周期),生成规则：根据当月日期进行判断最终的批次编码 
  	* @return
  	* @Note
  	* <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
  	* <br><b>Date:</b> 2018年10月8日 上午11:15:17
  	* <br><b>Version:</b> 1.0
  	 */
    @SuppressWarnings("all")
	public static String group() {

		String thisdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List toplist = getTopzhouricount();
		////System.out.println("--------------------上月周日");
		for (int i = 0; i < toplist.size(); i++) {
			//System.out.println(toplist.get(i));
		}
		////System.out.println("--------------------本月周日");
		List benlist = getBenzhouricount();
		/*for (int i = 0; i < benlist.size(); i++) {
			////System.out.println(benlist.get(i));
		}*/
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);// 年份
		// int month=cal.get(Calendar.MONTH)+1; //当前月
		int month = cal.get(Calendar.MONTH);// 上月
		int yue = 0;

		String str = "";// 批次码
		if (DateUtil.compare_date(thisdate, benlist.get(0).toString()) == false
				&& DateUtil.compare_date(thisdate,
						toplist.get(toplist.size() - 1).toString()) == true) {
			// 如果当前日期 小于 本月 第一个星期一 那就return 上个月最后一个
			str = "0" + toplist.size();
			yue = month;
		} else if (DateUtil.compare_date(thisdate, benlist.get(0).toString()) == true
				&& DateUtil.compare_date(thisdate, benlist.get(1).toString()) == false) {
			// 如果当前日期 大于 本月 第一个星期一 并且 小于 本月第二个星期一 return 01
			str = "01";
			yue = month + 1;
		} else if (DateUtil.compare_date(thisdate, benlist.get(1).toString()) == true
				&& DateUtil.compare_date(thisdate, benlist.get(2).toString()) == false) {
			// 如果当前日期 大于 第二个星期一 && 小于 第三个星期一 return 02
			str = "02";
			yue = month + 1;
		} else if (DateUtil.compare_date(thisdate, benlist.get(2).toString()) == true
				&& DateUtil.compare_date(thisdate, benlist.get(3).toString()) == false) {
			// 如果 当前日期 大于 第三个星期一 && 小于 第四个 星期一 return 03
			str = "03";
			yue = month + 1;
		} else if (DateUtil.compare_date(thisdate, benlist.get(3).toString()) == true) {
			if (benlist.size() == 5
					&& DateUtil.compare_date(thisdate, benlist.get(4)
							.toString()) == true) {
				str = "05";
				yue = month + 1;
			} else {
				// 如果 当前日期 大于 第四个星期一 returen 03
				str = "04";
				yue = month + 1;
			}
		}
		String yuestr = "";
		if (yue < 10) {
			yuestr = "0" + yue;
		} else {
			yuestr = "" + yue;
		}

		return year + yuestr + str;

		// try{
		// //Date date=new SimpleDateFormat("yyyy-MM-dd").parse(new
		// SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		// cal.setTime(new Date());
		// }catch (Exception e) {
		// e.printStackTrace();
		// }
		// int d =2-cal.get(Calendar.DAY_OF_WEEK);
		// cal.add(Calendar.DAY_OF_WEEK, d);
		// int month=cal.get(Calendar.MONTH)+1; //当前月
		// int weekOfMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		// //当前月的第几个星期天
		// String yy=new SimpleDateFormat("yyyy").format(new Date());
		// if(month>=10){
		// return yy+month+"0"+weekOfMonth;
		// }else{
		// return yy+"0"+month+"0"+weekOfMonth;
		// }
	}
  
  
  /**
   * 
  * <b>Description:</b><br> 判断两个时间区间 
  * @param DATE1 时间1
  * @param DATE2 时间2
  * @return
  * @Note
  * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
  * <br><b>Date:</b> 2018年10月8日 上午11:08:24
  * <br><b>Version:</b> 1.0
   */
  public static boolean compare_date(String DATE1, String DATE2) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      try {
          Date dt1 = df.parse(DATE1);
          Date dt2 = df.parse(DATE2);
          if (dt1.getTime() > dt2.getTime()) {
          	//System.out.println(DATE1+" 在"+DATE2+"前");
          	//System.out.println(true);
              return true;
          } else if (dt1.getTime() < dt2.getTime()) {
          	//System.out.println(DATE1+"在 "+DATE2+" 后");
          	//System.out.println(false);
              return false;
          } else{
          	//System.out.println(DATE1+" == "+DATE2+" ");
          	//System.out.println(true);
          	return true;
          }
      } catch (Exception exception) {
          exception.printStackTrace();
      }
      return true;
  }
  
  	 /**
  	  * 
  	 * <b>Description:</b><br> 获取 上个月 有几个 周日 
  	 * @return
  	 * @Note
  	 * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
  	 * <br><b>Date:</b> 2018年10月8日 上午11:08:11
  	 * <br><b>Version:</b> 1.0
  	  */
	 public static List getTopzhouricount(){
		 List list = new ArrayList();
		 	try {
		 		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
			    int year = aCalendar.get(Calendar.YEAR);//年份
			    int month = aCalendar.get(Calendar.MONTH);//月份
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(String.valueOf(year)+"-"+month));
				int day =	calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			    for (int i = 1; i <= day; i++) {
			        String aDate = String.valueOf(year)+"-"+month+"-"+i;
			        if(dayForWeek(aDate)==7){
			        	list.add(aDate);
			        }
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return list;
	 }
	 
	 
	 /**
	  * 
	 * <b>Description:</b><br> 获取 本月 有几个 周日 
	 * @return
	 * @Note
	 * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
	 * <br><b>Date:</b> 2018年10月8日 上午11:09:29
	 * <br><b>Version:</b> 1.0
	  */
	 public static List getBenzhouricount( ) {
		  	List list = new ArrayList();
		 	try {
		 		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
			    int year = aCalendar.get(Calendar.YEAR);//年份
			    int month = aCalendar.get(Calendar.MONTH) + 1;//月份
			    int day = aCalendar.getActualMaximum(Calendar.DATE);
			    for (int i = 1; i <= day; i++) {
			        String aDate = String.valueOf(year)+"-"+month+"-"+i;
			        if(dayForWeek(aDate)==7){
			        	list.add(aDate);
			        }
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return list;
	}
  
  public static String getDays()
  {
    return sdfDays.format(new java.util.Date());
  }
  
  public static String getTime()
  {
    return sdfTime.format(new java.util.Date());
  }
  
  public static String getTimes()
  {
    return sdftimes.format(new java.util.Date());
  }
  
  public static boolean compareDate(String s, String e)
  {
    if ((fomatDate(s) == null) || (fomatDate(e) == null)) {
      return false;
    }
    return fomatDate(s).getTime() >= fomatDate(e).getTime();
  }
  
  public static java.util.Date fomatDate(String date)
  {
    DateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
    try {
      return fmt.parse(date);
    } catch (ParseException e) {
      e.printStackTrace(); }
    return null;
  }
  
  public static boolean isValidDate(String s)
  {
    DateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
    try {
      fmt.parse(s);
      return true;
    }
    catch (Exception e) {}
    return false;
  }
  
  public static int getDiffYear(String startTime, String endTime) {
    DateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
    try {
      long aa = 0L;
      return (int)((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / 86400000L / 365L);
    }
    catch (Exception e) {}
    
    return 0;
  }
  
  public static long getDaySub(String beginDateStr, String endDateStr)
  {
    long day = 0L;
    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.util.Date beginDate = null;
    java.util.Date endDate = null;
    try
    {
      beginDate = format.parse(beginDateStr);
      endDate = format.parse(endDateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    day = (endDate.getTime() - beginDate.getTime()) / 86400000L;
    
    return day;
  }
  
  public static String getAfterDayDate(String days)
  {
    int daysInt = Integer.parseInt(days);
    
    Calendar canlendar = Calendar.getInstance();
    canlendar.add(5, daysInt);
    java.util.Date date = canlendar.getTime();
    
    java.text.SimpleDateFormat sdfd = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateStr = sdfd.format(date);
    
    return dateStr;
  }
  
  public static String getAfterDayWeek(String days)
  {
    int daysInt = Integer.parseInt(days);
    
    Calendar canlendar = Calendar.getInstance();
    canlendar.add(5, daysInt);
    java.util.Date date = canlendar.getTime();
    
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("E");
    String dateStr = sdf.format(date);
    
    return dateStr;
  }
  
  public static int dayForWeek(String pTime) throws Exception {
	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	  Calendar c = Calendar.getInstance();
	  c.setTime(format.parse(pTime));
	  int dayForWeek = 0;
	  if(c.get(Calendar.DAY_OF_WEEK) == 1){
	   dayForWeek = 7;
	  }else{
	   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
	  }
	  return dayForWeek;
	 }
	  /**  
	   * 将短时间格式字符串转换为时间 yyyy-MM-dd  
	   *  
	   * @param strDate  
	   * @return  
	   */  
	  public static Date strToDate(String strDate) {  
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	      ParsePosition pos = new ParsePosition(0);  
	      Date strtodate = formatter.parse(strDate, pos);  
	      return strtodate;  
	  }
	  /**  
	   * 获得一个日期所在的周的星期几的日期  
	   *  
	   * @param sdate  
	   * @param num  
	   * @return  
	   */  
	  public static String getWeek(String sdate, String num) {  
	      // 再转换为时间  
	      Date dd = DateUtil.strToDate(sdate);  
	      Calendar c = Calendar.getInstance();  
	      c.setTime(dd);  
	      if (num.equals("1")) // 返回星期一所在的日期  
	    	  c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	          //c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
	      else if (num.equals("2")) // 返回星期二所在的日期  
	          c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);  
	      else if (num.equals("3")) // 返回星期三所在的日期  
	    	  c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	    	  //c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);  
	      else if (num.equals("4")) // 返回星期四所在的日期  
	    	  c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	          //c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);  
	      else if (num.equals("5")) // 返回星期五所在的日期 
	    	  c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	          //c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);  
	      else if (num.equals("6")) // 返回星期六所在的日期  
	    	  c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	          //c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);  
	      else if (num.equals("0")) // 返回星期日所在的日期  
	    	  c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	          //c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
	      return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());  
	  }
	  /**  
	   * 得到二个日期间的间隔天数  
	   */ 
	  public static String getTwoDay(String sj1, String sj2) {  
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		  long day = 0;  
	        try {  
	            java.util.Date date = formatter.parse(sj1);  
	            java.util.Date mydate = formatter.parse(sj2);  
	            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);  
	        } catch (Exception e) {  
	            return "";  
	        }  
	        return day + "";  
	    }  
	  
	  
	  
	  /**
	   * 
	  * <b>Description:</b><br> 根据时间获取批次号 
	  * @param date
	  * @return
	  * @Note
	  * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
	  * <br><b>Date:</b> 2018年10月10日 上午9:49:12
	  * <br><b>Version:</b> 1.0
	   */
	public static String getBatchNum(String date) {
		
		List<String> lastMonthSundays = getSundaysByDateStr(date, true);
		List<String> currentMonthSundays = getSundaysByDateStr(date, false);
		
		String batchNum = "";// 批次号
		if (compare_date(date, currentMonthSundays.get(0).toString()) == false
			&& compare_date(date, lastMonthSundays.get(lastMonthSundays.size() - 1).toString()) == true) {
			// 如果当前日期 小于 本月 第一个星期一 那就return 上个月最后一个
			batchNum = "0" + lastMonthSundays.size();
		} else if (compare_date(date, currentMonthSundays.get(0).toString()) == true
				&& compare_date(date, currentMonthSundays.get(1).toString()) == false) {
			// 如果当前日期 大于 本月 第一个星期一 并且 小于 本月第二个星期一 return 01
			batchNum = "1";
		} else if (compare_date(date, currentMonthSundays.get(1).toString()) == true
				&& compare_date(date, currentMonthSundays.get(2).toString()) == false) {
			// 如果当前日期 大于 第二个星期一 && 小于 第三个星期一 return 02
			batchNum = "2";
		} else if (compare_date(date, currentMonthSundays.get(2).toString()) == true
				&& compare_date(date, currentMonthSundays.get(3).toString()) == false) {
			// 如果 当前日期 大于 第三个星期一 && 小于 第四个 星期一 return 03
			batchNum = "3";
		} else if (compare_date(date, currentMonthSundays.get(3).toString()) == true) {
			if (currentMonthSundays.size() == 5
					&& compare_date(date, currentMonthSundays.get(4).toString()) == true) {
				batchNum = "5";
			} else {
				// 如果 当前日期 大于 第四个星期一 returen 03
				batchNum = "4";
			}
		}
		return batchNum;
	}
	
	/**
	 * 
	* <b>Description:</b><br> 根据时间获取改月下所有周日
	* @param date 传入时间
	* @param isLastMonth 是否为上个月，true：查询上月； false：查询当月
	* @return
	* @Note
	* <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
	* <br><b>Date:</b> 2018年10月10日 上午10:32:37
	* <br><b>Version:</b> 1.0
	 */
	public static List<String> getSundaysByDateStr(String date,boolean isLastMonth){
		List<String> datelist = new ArrayList<String>();
		int year = 0;
		int month = 0;
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		year = cal.get(Calendar.YEAR);
		if(!isLastMonth){
			month = cal.get(Calendar.MONTH); // 当前月
		}else{
			month = cal.get(Calendar.MONTH) - 1; // 上个月
		}
		// 根据年月获取改所有周日/周六时间
		Calendar calendar = new GregorianCalendar(year, month, 1);
		int i = 1;
		while (calendar.get(Calendar.MONTH) < month + 1) {
			calendar.set(Calendar.WEEK_OF_YEAR, i++);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			if (calendar.get(Calendar.MONTH) == month) {
//				//System.out.printf("星期天：%tF%n", calendar);
				datelist.add(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			if (calendar.get(Calendar.MONTH) == month) {
//				//System.out.printf("星期六：%tF%n", calendar);
			}
		}
		return datelist;
	}
	
	public static void main(String[] args) {
		// //System.out.println(getDays());
		// //System.out.println(getAfterDayWeek("3"));

//		List<String> sundaysByDateStr_last = getSundaysByDateStr("2018-10-10",true);
//		List<String> sundaysByDateStr_current = getSundaysByDateStr("2018-10-10",false);
//		//System.out.println(sundaysByDateStr_last);
//		//System.out.println(sundaysByDateStr_current);
		
		
//		String batchNum = getBatchNum("2018-09-27");
//		//System.out.println(batchNum);
	}
	
}


