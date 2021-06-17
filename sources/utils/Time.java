package utils;

public class Time
{
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	private int second;
	private String yyyymmdd;
	
	public Time()
	{
		this(0, 0, 0, 0, 0, 0);
	}

	public Time(String yyyymmdd)
	{
		char[] yyyymmdd_array = yyyymmdd.toCharArray();
		int yyyy = Character.getNumericValue(yyyymmdd_array[0]) * 1000
				 + Character.getNumericValue(yyyymmdd_array[1]) * 100
				 + Character.getNumericValue(yyyymmdd_array[2]) * 10
				 + Character.getNumericValue(yyyymmdd_array[3]);
		int mm   = Character.getNumericValue(yyyymmdd_array[5]) * 10
				 + Character.getNumericValue(yyyymmdd_array[6]);
		int dd	 = Character.getNumericValue(yyyymmdd_array[8]) * 10
				 + Character.getNumericValue(yyyymmdd_array[9]);
		day = dd;
		month = mm;
		year = yyyy;
		hour = 0;
		minute = 0;
		second = 0;
		this.yyyymmdd = yyyymmdd;
	}

	public Time(int day, int month, int year)
	{
		this(day, month, year, 0, 0, 0);
	}
	
	public Time(int day, int month, int year, int hour, int minute, int second)
	{
		this.day = day;
		this.month = month;
		this.year = year;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		yyyymmdd = year + "-" + month + "-" + day;
	}

	public Time(Time other)
	{
		day = other.get_day();
		month = other.get_month();
		year = other.get_year();
		hour = other.get_hour();
		minute = other.get_minute();
		second = other.get_second();
		yyyymmdd = other.get_date();
	}
	
	public final int get_day()
	{
		return day;
	}
	
	public final int get_month()
	{
		return month;
	}
	
	public final int get_year()
	{
		return year;
	}
	
	public final int get_hour()
	{
		return hour;
	}
	
	public final int get_minute()
	{
		return minute;
	}
	
	public final int get_second()
	{
		return second;
	}

	public final String get_date() { return yyyymmdd; }

	public void add_interval(Time interval)
	{
		year += interval.get_year();

		month += interval.get_month();
		if(month > 12)
		{
			month -= 12;
			year += 1;
		}

		day += interval.get_day();
		if(day > 31 && (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ||
				month == 12))
		{
			day -= 31;
			month += 1;
		}
		else if(day > 30 && (month == 4 || month == 6 || month == 9 || month == 11))
		{
			day -= 30;
			month += 1;
		}
		else if(day > 29 && month == 2 && year % 4 == 0)
		{
			day -= 29;
			month = 3;
		}
		else if(day > 28 && month == 2 && year % 4 != 0)
		{
			day -= 28;
			month = 3;
		}

		yyyymmdd = year + "-";
		if(month < 10)
			yyyymmdd += "0";
		yyyymmdd += (month + "-");
		if(day < 10)
			yyyymmdd += "0";
		yyyymmdd += (day);
	}
	
	public boolean equals(Time other)
	{
		return day == other.day && month == other.month && year == other.year &&
			hour == other.hour && minute == other.minute && second == other.second;
	}
}
