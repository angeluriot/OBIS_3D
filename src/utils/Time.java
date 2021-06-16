package utils;

public class Time
{
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	private int second;
	
	public Time()
	{
		this(0, 0, 0, 0, 0, 0);
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
	
	public boolean equals(Time other)
	{
		return day == other.day && month == other.month && year == other.year &&
			hour == other.hour && minute == other.minute && second == other.second;
	}
}
