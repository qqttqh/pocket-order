package top.xyquan.pocketorderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.xyquan.pocketorderservice.entity.User;

public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE token = #{token}")
    User getUserByToken(String token);
}
