package top.xyquan.pocketorderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import top.xyquan.pocketorderservice.entity.MenuData;

import java.util.List;

public interface MenuDataService extends IService<MenuData> {

    @Cacheable(value = "menuCache", key = "'allMenuData'")
    List<MenuData> getAllMenuData();

    boolean addMenuData(MenuData menuData);

    // 删除菜品
    @CacheEvict(value = "menuCache", key = "'allMenuData'")
    boolean removeById(Long id);
}
