package top.xyquan.pocketorderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.xyquan.pocketorderservice.entity.DailyOrderStats;

import java.util.List;

public interface DailyOrderStatsRepository extends JpaRepository<DailyOrderStats, Integer> {

    // 查询最后30条数据，按日期正序
    @Query(value = "SELECT * FROM daily_order_stats ORDER BY date DESC LIMIT 30", nativeQuery = true)
    List<DailyOrderStats> findLast30DaysStats();
}
