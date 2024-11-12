package top.xyquan.pocketorderservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xyquan.pocketorderservice.entity.MenuGroup;
import top.xyquan.pocketorderservice.mapper.MenuGroupMapper;
import top.xyquan.pocketorderservice.service.MenuGroupService;

@Service
public class MenuGroupServiceImpl extends ServiceImpl<MenuGroupMapper, MenuGroup> implements MenuGroupService {

    @Autowired
    private MenuGroupMapper menuGroupMapper;

    @Override
    public void removeGroupById(Integer id) {
        try {
            // 执行删除操作
            int deletedRows = menuGroupMapper.deleteById(id);
            if (deletedRows == 0) {
                throw new RuntimeException("无该分组");
            }
        } catch (Exception e) {
            throw new RuntimeException("分组中存在菜品");
        }
    }
}
