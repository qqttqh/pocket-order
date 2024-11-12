package top.xyquan.pocketorderservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.xyquan.pocketorderservice.entity.MenuData;
import top.xyquan.pocketorderservice.mapper.MenuDataMapper;
import top.xyquan.pocketorderservice.service.MenuDataService;

import java.util.List;

@Service
public class MenuDataServiceImpl extends ServiceImpl<MenuDataMapper, MenuData> implements MenuDataService {

    // 获取所有彩蛋数据
    @Override
    @Cacheable(value = "menuCache", key = "'allMenuData'")
    public List<MenuData> getAllMenuData() {
        return this.list(); // 使用 MyBatis Plus 提供的 list() 方法
    }

    // 更新菜单数据
    @Override
    @CacheEvict(value = "menuCache", key = "'allMenuData'")
    public boolean updateById(MenuData menuData) {
        return super.updateById(menuData); // 调用父类方法执行更新
    }

    // 新增菜品，并清除相关缓存
    @CacheEvict(value = "menuCache", key = "'allMenuData'") // 清除缓存
    public boolean addMenuData(MenuData menuData) {
        return this.save(menuData);
    }

    // 删除菜品
    @Override
    @CacheEvict(value = "menuCache", key = "'allMenuData'")
    public boolean removeById(Long id) {
        return super.removeById(id); // 调用父类方法执行删除
    }
}
