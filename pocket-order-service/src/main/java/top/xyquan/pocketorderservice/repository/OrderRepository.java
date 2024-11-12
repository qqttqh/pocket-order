package top.xyquan.pocketorderservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.xyquan.pocketorderservice.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT * FROM orders ORDER BY create_time DESC",
            countQuery = "SELECT COUNT(*) FROM orders",
            nativeQuery = true)
    Page<Order> findPagedOrders(Pageable pageable);

    // 通过 userId 查询订单，倒序排列并分页
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId ORDER BY create_time DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Order> findOrdersByUserId(@Param("userId") Integer userId, @Param("limit") int limit, @Param("offset") int offset);

    // 通过 user_uuid 查询订单，倒序排列并分页
    @Query(value = "SELECT * FROM orders WHERE user_uuid = :uuid ORDER BY create_time DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Order> findOrdersByUserUuid(@Param("uuid") String uuid, @Param("limit") int limit, @Param("offset") int offset);

    // 通过 订单id 查询订单
    @Query(value = "SELECT * FROM orders WHERE id = :id", nativeQuery = true)
    Order findOrderByIdNative(@Param("id") Integer id);

}
