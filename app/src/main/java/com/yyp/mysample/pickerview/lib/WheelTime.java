package com.yyp.mysample.pickerview.lib;

import android.content.Context;
import android.view.View;

import com.yyp.mysample.R;
import com.yyp.mysample.pickerview.TimePopupWindow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WheelTime {

	// 添加大小月月份并将其转换为list,方便之后的判断
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };


	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	public int screenheight;
	
	private TimePopupWindow.Type type;

	private TimePopupWindow.Model model;

	private Calendar nowCalendar;

	private static int START_YEAR = 1900, END_YEAR = 2100;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelTime(View view) {
		this(view, TimePopupWindow.Type.ALL);
	}
	public WheelTime(View view,TimePopupWindow.Type type) {
		this(view,type, TimePopupWindow.Model.NONE);
	}

	public WheelTime(View view,TimePopupWindow.Type type,TimePopupWindow.Model model) {
		super();
		this.view = view;
		this.type = type;
		this.model = model;
		setView(view);
	}


	public void setPicker(int year ,int month,int day){
		this.setPicker(year, month, day, 0, 0);
	}
	
	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void setPicker(final int year ,int month ,int day,int h,int m) {
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		nowCalendar = Calendar.getInstance();

		Context context = view.getContext();
		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		final int y[] = getYear();
		wv_year.setAdapter(new NumericWheelAdapter(y[0], y[1]));// 设置"年"的显示数据
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - y[0]);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		int monthes[] = getMonth(year);
		wv_month.setAdapter(new NumericWheelAdapter(monthes[0], monthes[1]));
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month-monthes[0]+1);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		int days[] = getDay(year, month+1);
		// 判断大小月及是否闰年,用来确定"日"的数据
//		if (list_big.contains(String.valueOf(month + 1))) {
//			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//		} else if (list_little.contains(String.valueOf(month + 1))) {
//			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//		} else {
//			// 闰年
//			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
//				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//			else
//				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//		}
		wv_day.setAdapter(new NumericWheelAdapter(days[0], days[1]));
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - days[0]);

		wv_hours = (WheelView)view.findViewById(R.id.hour);
		int hours[] = getHours(year,month,day);
		wv_hours.setAdapter(new NumericWheelAdapter(hours[0], hours[1]));
		wv_hours.setLabel("时");// 添加文字
		wv_hours.setCurrentItem(h-hours[0]);
		
		wv_mins = (WheelView)view.findViewById(R.id.min);
		int mins[] = getMin(year,month,day,h);
		wv_mins.setAdapter(new NumericWheelAdapter(mins[0], mins[1]));
		wv_mins.setLabel("分");// 添加文字
		wv_mins.setCurrentItem(m- mins[0]);
		
		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + y[0];
				// 判断大小月及是否闰年,用来确定"日"的数据
//				int maxItem = 30;

				int newDays[] = getDay(year_num, wv_month.getCurrentItem() + 1);


//				if (list_big
//						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//					maxItem = 31;
//				} else if (list_little.contains(String.valueOf(wv_month
//						.getCurrentItem() + 1))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//					maxItem = 30;
//				} else {
//					if ((year_num % 4 == 0 && year_num % 100 != 0)
//							|| year_num % 400 == 0){
//						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//						maxItem = 29;
//					}
//					else{
//						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//						maxItem = 28;
//					}
//				}
				wv_day.setAdapter(new NumericWheelAdapter(newDays[0], newDays[1]));
				if (wv_day.getCurrentItem() > newDays[1] - 1){
					wv_day.setCurrentItem(newDays[1] - 1);
				}
				if(model != TimePopupWindow.Model.NONE){
					int newMonys[] = getMonth(year_num);
					wv_month.setAdapter(new NumericWheelAdapter(newMonys[0],newMonys[1]));
					if (wv_month.getCurrentItem() > newMonys[1] - 1){
						wv_month.setCurrentItem(newMonys[1] - 1);
					}

					if(type != TimePopupWindow.Type.YEAR_MONTH_DAY){
						int newhours[] = getHours(year_num, wv_month.getCurrentItem() + 1,wv_month
								.getCurrentItem());
						wv_hours.setAdapter(new NumericWheelAdapter(newhours[0], newhours[1]));
						if (wv_hours.getCurrentItem() > newhours[1] - 1){
							wv_hours.setCurrentItem(newhours[1] - 1);
						}

						int newMins[] = getMin(year_num, wv_month.getCurrentItem() + 1, wv_month
								.getCurrentItem(), wv_hours.getCurrentItem());
						wv_mins.setAdapter(new NumericWheelAdapter(newMins[0], newMins[1]));
						if (wv_mins.getCurrentItem() > newMins[1] - 1){
							wv_mins.setCurrentItem(newMins[1] - 1);
						}
					}
				}

			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
//				int maxItem = 30;

				int newDays[] = getDay(wv_year.getCurrentItem() + y[0],month_num);

//				// 判断大小月及是否闰年,用来确定"日"的数据
//				if (list_big.contains(String.valueOf(month_num))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//					maxItem = 31;
//				} else if (list_little.contains(String.valueOf(month_num))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//					maxItem = 30;
//				} else {
//					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
//							.getCurrentItem() + START_YEAR) % 100 != 0)
//							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0){
//						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//						maxItem = 29;
//					}
//					else{
//						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//						maxItem = 28;
//					}
//				}
				wv_day.setAdapter(new NumericWheelAdapter(newDays[0], newDays[1]));
				if (wv_day.getCurrentItem() > newDays[1] - 1){
					wv_day.setCurrentItem(newDays[1] - 1);
				}

				if(model != TimePopupWindow.Model.NONE){

					if(type != TimePopupWindow.Type.YEAR_MONTH_DAY){
						int newhours[] = getHours(wv_year.getCurrentItem() + y[0],month_num,wv_month
								.getCurrentItem());
						wv_hours.setAdapter(new NumericWheelAdapter(newhours[0], newhours[1]));
						if (wv_hours.getCurrentItem() > newhours[1] - 1){
							wv_hours.setCurrentItem(newhours[1] - 1);
						}

						int newMins[] = getMin(wv_year.getCurrentItem() + y[0], month_num, wv_month
								.getCurrentItem(), wv_hours.getCurrentItem());
						wv_mins.setAdapter(new NumericWheelAdapter(newMins[0], newMins[1]));
						if (wv_mins.getCurrentItem() > newMins[1] - 1){
							wv_mins.setCurrentItem(newMins[1] - 1);
						}
					}
				}

			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		switch(type){
		case ALL:
			textSize = (int) ((screenheight / 100) * 2.5);
			break;
		case YEAR_MONTH_DAY:
			textSize = (screenheight / 100) * 4;
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
			break;
		case HOURS_MINS:
			textSize = (screenheight / 100) * 4;
			wv_year.setVisibility(View.GONE);
			wv_month.setVisibility(View.GONE);
			wv_day.setVisibility(View.GONE);
			break;
		case MONTH_DAY_HOUR_MIN:
			textSize = (screenheight / 100) * 3;
			wv_year.setVisibility(View.GONE);
			break;
		}
			
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;


	}

	/**
	 * 设置是否循环滚动
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic){
		wv_year.setCyclic(cyclic);
		wv_month.setCyclic(cyclic);
		wv_day.setCyclic(cyclic);
		wv_hours.setCyclic(cyclic);
		wv_mins.setCyclic(cyclic);
	}
	public String getTime() {
		StringBuffer sb = new StringBuffer();
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
			.append((wv_month.getCurrentItem() + 1)).append("-")
			.append((wv_day.getCurrentItem() + 1)).append(" ")
			.append(wv_hours.getCurrentItem()).append(":")
			.append(wv_mins.getCurrentItem());
		return sb.toString();
	}

	private int[] getYear(){
		int []y = new int[2];
		switch (model){
			case NONE:
				y[0] = START_YEAR;
				y[1] = END_YEAR;
				break;
			case BEFORE:
				y[0] = START_YEAR;
				y[1] = nowCalendar.get(Calendar.YEAR);
				break;
			case AFTER:
				y[0] = nowCalendar.get(Calendar.YEAR);
				y[1] = END_YEAR;
				break;
		}
		return y;
	}

	private int[] getMonth(int y){
		if(y == nowCalendar.get(Calendar.YEAR)){
			int []m = new int[2];
			switch (model){
				case NONE:
					m[0] = 1;
					m[1] = 12;
					break;
				case BEFORE:
					m[0] = 1;
					m[1] = nowCalendar.get(Calendar.MONTH)+1;
					break;
				case AFTER:
					m[0] = nowCalendar.get(Calendar.MONTH)+1;
					m[1] = 12;
					break;
			}
			return m;
		}else {
			return new int[]{1,12};
		}
	}

	private int[] getDay(int year,int month){

		List<String> list_big = Arrays.asList(months_big);
		List<String> list_little = Arrays.asList(months_little);
		int maxDay;
		if (list_big.contains(String.valueOf(month))) {
			maxDay = 31;
		} else if (list_little.contains(String.valueOf(month))) {
			maxDay = 30;
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				maxDay = 29;
			else
				maxDay = 28;
		}
		if(month == nowCalendar.get(Calendar.MONTH)+1 && year == nowCalendar.get(Calendar.YEAR)){
			int []y = new int[2];
			switch (model){
				case NONE:
					y[0] = 1;
					y[1] = maxDay;
					break;
				case BEFORE:
					y[0] = 1;
					y[1] = nowCalendar.get(Calendar.DAY_OF_MONTH);
					break;
				case AFTER:
					y[0] = nowCalendar.get(Calendar.DAY_OF_MONTH);
					y[1] = maxDay;
					break;
			}

			return y;
		}else {
			return new int[]{1,maxDay};
		}
	}

	private int[] getHours(int y,int m,int d){
		if(y == nowCalendar.get(Calendar.YEAR) && m == nowCalendar.get(Calendar.MONTH)+1 && d ==
				nowCalendar.get(Calendar.DAY_OF_MONTH) && type != TimePopupWindow.Type.YEAR_MONTH_DAY){
			int h[] = new int[2];
			switch (model){
				case NONE:
					h[0] = 0;
					h[1] = 23;
					break;
				case BEFORE:
					h[0] = 0;
					h[1] = nowCalendar.get(Calendar.HOUR_OF_DAY);
					break;
				case AFTER:
					h[0] = nowCalendar.get(Calendar.HOUR_OF_DAY);
					h[1] = 23;
					break;
			}
			return h;

		}else {
			return new int[]{0,23};
		}
	}

	private int[] getMin(int y,int m,int d,int h){
		if(y == nowCalendar.get(Calendar.YEAR) && m == nowCalendar.get(Calendar.MONTH)+1 && d ==
				nowCalendar.get(Calendar.DAY_OF_MONTH) && h == nowCalendar.get(Calendar.HOUR_OF_DAY) && type !=
				TimePopupWindow.Type.YEAR_MONTH_DAY){
			int min[] = new int[2];
			switch (model){
				case NONE:
					min[0] = 0;
					min[1] = 59;
					break;
				case BEFORE:
					min[0] = 0;
					min[1] = nowCalendar.get(Calendar.MINUTE);
					break;
				case AFTER:
					min[0] = nowCalendar.get(Calendar.MINUTE);
					min[1] = 59;
					break;
			}
			return min;

		}else {
			return new int[]{0,59};
		}
	}
}
