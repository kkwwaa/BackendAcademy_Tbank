package academy.report.utils;

public record RequestPerDateStat(
        String date, String weekday, long totalRequestsCount, double totalRequestsPercentage) {}
