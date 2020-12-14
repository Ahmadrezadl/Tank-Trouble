package Components;

public class ConvertTime {
    public static long toSecond(long ms)
    {
        return ms/1000;
    }

    public static long toMinute(long ms)
    {
        return toSecond(ms)/60;
    }

    public static long toHour(long ms)
    {
        return toMinute(ms)/60;
    }

    public static long toDay(long ms)
    {
        return toHour(ms)/24;
    }

    public static long toWeek(long ms)
    {
        return toDay(ms)/7;
    }

    public static String toTime(long ms)
    {
        long week = (toWeek(ms));
        long day = (toDay(ms));
        long hour = (toHour(ms));
        long minute = (toMinute(ms));
        String output = "";
        if(week != 0)
            output += week + " Weeks, ";
        if(day != 0)
            output += day%7 + " Days, ";
        output += hour%24 + " ساعت و " + minute%60 + " دقیقه";
        return output;
    }
}
