package top.xyquan.pocketorderservice.service;

import top.xyquan.pocketorderservice.entity.DailyOrderStats;

import java.util.List;

public interface DailyOrderStatsService {
    List<DailyOrderStats> getLast30DaysStats();
}
