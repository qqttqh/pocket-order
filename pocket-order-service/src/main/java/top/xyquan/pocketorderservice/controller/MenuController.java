package top.xyquan.pocketorderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xyquan.pocketorderservice.entity.MenuData;
import top.xyquan.pocketorderservice.entity.MenuGroup;
import top.xyquan.pocketorderservice.service.MenuDataService;
import top.xyquan.pocketorderservice.service.MenuGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    MenuDataService menuDataService;
    @Autowired
    MenuGroupService menuGroupService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> menu() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MenuData> menuData = menuDataService.getAllMenuData();
            List<MenuGroup> menuGroup = menuGroupService.list();
            response.put("menuData", menuData);
            response.put("menuGroup", menuGroup);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "failure");
            response.put("my", "yes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
