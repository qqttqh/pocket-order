package top.xyquan.pocketorderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.xyquan.pocketorderservice.entity.MenuGroup;

public interface MenuGroupService extends IService<MenuGroup> {
    void removeGroupById(Integer id);
}
