package top.xyquan.pocketorderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xyquan.pocketorderservice.entity.DailyOrderStats;
import top.xyquan.pocketorderservice.repository.DailyOrderStatsRepository;
import top.xyquan.pocketorderservice.service.DailyOrderStatsService;

import java.util.List;

@Service
public class DailyOrderStatsServiceImpl implements DailyOrderStatsService {

    @Autowired
    private DailyOrderStatsRepository dailyOrderStatsRepository;

    @Override
    public List<DailyOrderStats> getLast30DaysStats() {
        // 调用 repository 层获取数据
        return dailyOrderStatsRepository.findLast30DaysStats();
    }
}
