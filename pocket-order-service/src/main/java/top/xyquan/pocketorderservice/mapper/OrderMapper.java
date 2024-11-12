package top.xyquan.pocketorderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.xyquan.pocketorderservice.entity.Order;

import java.util.List;
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT * FROM orders LIMIT #{offset}, #{limit}")
    List<Order> getOrders(@Param("offset") int offset, @Param("limit") int limit);

}
