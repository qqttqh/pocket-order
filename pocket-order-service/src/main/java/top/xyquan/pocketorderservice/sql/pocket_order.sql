/*
Navicat Premium Data Transfer

Source Server         : localhost
Source Server Type    : MySQL
Source Server Version : 80036
Source Host           : localhost:3306
Source Schema         : pocket_order

Target Server Type    : MySQL
Target Server Version : 80036
File Encoding         : 65001

Date: 28/10/2024 20:47:32
 */
SET
  NAMES utf8mb4;

SET
  FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for daily_order_stats
-- ----------------------------
DROP TABLE IF EXISTS `daily_order_stats`;

CREATE TABLE
  `daily_order_stats` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '订单日常统计表',
    `date` date NOT NULL COMMENT '日期',
    `total_orders` int NULL DEFAULT 0 COMMENT '当日销量',
    `total_sales` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '当日营业额',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_date` (`date`) USING BTREE
  ) ENGINE = InnoDB AUTO_INCREMENT = 328 CHARACTER
SET
  = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of daily_order_stats
-- ----------------------------
INSERT INTO
  `daily_order_stats`
VALUES
  (101, '2024-09-24', 2, 6.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (102, '2024-09-25', 2, 10.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (105, '2024-09-26', 5, 115.98);

INSERT INTO
  `daily_order_stats`
VALUES
  (108, '2024-09-27', 1, 6.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (110, '2024-09-28', 2, 8.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (113, '2024-09-29', 1, 52.99);

INSERT INTO
  `daily_order_stats`
VALUES
  (116, '2024-09-30', 6, 87.98);

INSERT INTO
  `daily_order_stats`
VALUES
  (118, '2024-10-01', 2, 165.95);

INSERT INTO
  `daily_order_stats`
VALUES
  (120, '2024-10-02', 4, 322.99);

INSERT INTO
  `daily_order_stats`
VALUES
  (124, '2024-10-03', 4, 307.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (128, '2024-10-04', 2, 325.98);

INSERT INTO
  `daily_order_stats`
VALUES
  (130, '2024-10-05', 2, 134.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (133, '2024-10-06', 10, 456.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (136, '2024-10-07', 6, 324.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (137, '2024-10-08', 7, 378.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (139, '2024-10-09', 8, 453.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (140, '2024-10-10', 5, 345.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (141, '2024-10-11', 7, 675.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (142, '2024-10-12', 4, 354.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (143, '2024-10-13', 6, 564.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (144, '2024-10-14', 7, 657.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (148, '2024-10-15', 8, 679.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (149, '2024-10-16', 6, 657.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (153, '2024-10-17', 5, 546.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (154, '2024-10-18', 8, 758.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (157, '2024-10-19', 4, 453.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (159, '2024-10-20', 8, 867.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (161, '2024-10-22', 5, 536.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (163, '2024-10-23', 25, 1877.96);

INSERT INTO
  `daily_order_stats`
VALUES
  (221, '2024-10-24', 26, 4756.71);

INSERT INTO
  `daily_order_stats`
VALUES
  (318, '2024-10-25', 1, 80.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (326, '2024-10-26', 0, 0.00);

INSERT INTO
  `daily_order_stats`
VALUES
  (328, '2024-10-28', 24, 1816.00);

-- ----------------------------
-- Table structure for menu_data
-- ----------------------------
DROP TABLE IF EXISTS `menu_data`;

CREATE TABLE
  `menu_data` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '菜品ID',
    `name` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜品名称',
      `introduce` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜品介绍',
      `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '菜品价格',
      `img` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜品图片URL',
      `group_id` int NULL DEFAULT NULL COMMENT '菜品分组ID',
      PRIMARY KEY (`id`) USING BTREE,
      INDEX `fk_menu_group` (`group_id`) USING BTREE,
      CONSTRAINT `fk_menu_group` FOREIGN KEY (`group_id`) REFERENCES `menu_group` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
  ) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER
SET
  = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu_data
-- ----------------------------
INSERT INTO
  `menu_data`
VALUES
  (
    1,
    '宫保鸡丁',
    '鸡胸肉、鸡腿肉、花生米、大葱、干辣椒、花椒、蒜、姜',
    20.00,
    '/api/uploads/8351ee3e-4979-4652-9ae9-869bad97e238.jpg',
    1
  );

INSERT INTO
  `menu_data`
VALUES
  (
    2,
    '鱼香肉丝',
    '猪里脊肉、黑木耳、胡萝卜、莴笋、泡椒、姜、蒜、小葱、豆瓣酱',
    22.00,
    '/api/uploads/24204adc-0fe1-4c69-9e43-f602bbdf3a1a.jpg',
    1
  );

INSERT INTO
  `menu_data`
VALUES
  (
    3,
    '麻婆豆腐',
    '豆腐、牛肉末、豆瓣酱、花椒、干辣椒、姜、蒜、小葱、淀粉',
    16.00,
    '/api/uploads/607deef8-0001-4dab-b3ca-4a7a12b6c679.jpg',
    1
  );

INSERT INTO
  `menu_data`
VALUES
  (
    4,
    '青椒肉丝',
    '猪肉、青椒、姜、蒜、盐、生抽、料酒、淀粉',
    18.00,
    '/api/uploads/0557bec4-d779-4828-8aae-47084c4dbd18.jpg',
    1
  );

INSERT INTO
  `menu_data`
VALUES
  (
    5,
    '红烧肉',
    '五花肉、葱、姜、蒜、八角、桂皮、香叶、老抽、生抽、料酒',
    25.00,
    '/api/uploads/7d7d611e-2b14-46e0-9e02-8ed4185f6788.jpg',
    1
  );

INSERT INTO
  `menu_data`
VALUES
  (
    6,
    '卤牛肉',
    '牛肉、角、桂皮、香叶、花椒、干辣椒、姜、蒜、葱',
    20.00,
    '/api/uploads/da9298eb-27e4-4510-b229-07cb30d7a478.jpg',
    2
  );

INSERT INTO
  `menu_data`
VALUES
  (
    7,
    '卤猪蹄',
    '猪蹄、八角、桂皮、香叶、花椒、干辣椒、姜、蒜、葱',
    18.00,
    '/api/uploads/c7c0ff9b-6c7c-44be-8335-463939f6802a.jpg',
    2
  );

INSERT INTO
  `menu_data`
VALUES
  (
    8,
    '卤鸭脖',
    '鸭脖、八角、桂皮、香叶、花椒、干辣椒、姜、蒜、葱',
    18.00,
    '/api/uploads/30a84931-00d8-4979-b18a-509781b34d9c.jpg',
    2
  );

INSERT INTO
  `menu_data`
VALUES
  (
    9,
    '白米饭',
    '大米',
    1.00,
    '/api/uploads/a1fd7869-da30-45b9-902c-e04748540dc7.jpg',
    3
  );

INSERT INTO
  `menu_data`
VALUES
  (
    10,
    '蛋炒饭',
    '米饭、鸡蛋、火腿、胡萝卜、葱花',
    7.00,
    '/api/uploads/e7bf24dd-dc4a-4c8d-908e-a21e9ea2271f.jpg',
    3
  );

INSERT INTO
  `menu_data`
VALUES
  (
    11,
    '车仔面',
    '车仔面、XO酱',
    5.00,
    '/api/uploads/692f3b23-b1c9-4dde-a207-cd557104161d.jpg',
    3
  );

INSERT INTO
  `menu_data`
VALUES
  (
    12,
    '乌冬面',
    '乌冬面、XO酱',
    5.00,
    '/api/uploads/10aa3ae6-7ed2-4da4-bc0f-7653369dfd9b.jpg',
    3
  );

INSERT INTO
  `menu_data`
VALUES
  (
    13,
    '青柠可乐',
    '青柠檬、可乐',
    5.00,
    '/api/uploads/1002af44-9223-4356-a411-fe1e75debeca.jpg',
    4
  );

INSERT INTO
  `menu_data`
VALUES
  (
    14,
    '青柠雪碧',
    '青柠檬、雪碧',
    5.00,
    '/api/uploads/fdbe0e65-28a4-4a13-bd5c-058bfafad6c0.jpg',
    4
  );

INSERT INTO
  `menu_data`
VALUES
  (
    15,
    '柠檬红茶',
    '红茶、青柠檬、蜂蜜',
    6.00,
    '/api/uploads/c2982c06-a333-4b01-9620-a201ca27a8bb.jpg',
    4
  );

INSERT INTO
  `menu_data`
VALUES
  (
    16,
    '柠檬绿茶',
    '绿茶、青柠檬、蜂蜜',
    6.00,
    '/api/uploads/8a691f1a-811b-48b0-841f-7c805a4219d9.jpg',
    4
  );

INSERT INTO
  `menu_data`
VALUES
  (
    17,
    '牛奶冰淇淋',
    '牛奶、糖',
    4.00,
    '/api/uploads/8935f2e3-f79d-4cd9-942c-b1426ab6ec20.jpg',
    5
  );

INSERT INTO
  `menu_data`
VALUES
  (
    18,
    '牛奶布丁',
    '牛奶、吉利丁片、糖',
    4.00,
    '/api/uploads/e13f33b3-da88-4990-858c-62a9f080abc3.jpg',
    5
  );

INSERT INTO
  `menu_data`
VALUES
  (
    19,
    '杨枝甘露',
    '芒果、西柚、西米、椰浆、牛奶、糖',
    6.00,
    '/api/uploads/5b2560db-1d5e-4144-8701-da60a911d32f.jpg',
    5
  );

INSERT INTO
  `menu_data`
VALUES
  (
    20,
    '双皮奶',
    '牛奶、鸡蛋、糖',
    5.00,
    '/api/uploads/f85b82f6-254c-436c-9b97-0d5f6a139a57.jpg',
    5
  );

-- ----------------------------
-- Table structure for menu_group
-- ----------------------------
DROP TABLE IF EXISTS `menu_group`;

CREATE TABLE
  `menu_group` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
      PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER
SET
  = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu_group
-- ----------------------------
INSERT INTO
  `menu_group`
VALUES
  (1, '小炒');

INSERT INTO
  `menu_group`
VALUES
  (2, '卤菜');

INSERT INTO
  `menu_group`
VALUES
  (3, '主食');

INSERT INTO
  `menu_group`
VALUES
  (4, '饮料');

INSERT INTO
  `menu_group`
VALUES
  (5, '甜品');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;

CREATE TABLE
  `orders` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
    `user_uuid` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户UUID',
      `table_number` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桌号',
      `order_list` text CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜品列表',
      `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格总计',
      `status` int NULL DEFAULT 0 COMMENT '订单状态',
      `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
      PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB AUTO_INCREMENT = 10010 CHARACTER
SET
  = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO
  `orders`
VALUES
  (
    10015,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":8,\"name\":\"卤鸭脖\",\"price\":18.0,\"count\":1,\"success\":1},{\"id\":9,\"name\":\"白米饭\",\"price\":1.0,\"count\":1,\"success\":1},{\"id\":10,\"name\":\"蛋炒饭\",\"price\":7.0,\"count\":1,\"success\":1}]',
    26.00,
    1,
    '2024-10-28 12:38:38'
  );

INSERT INTO
  `orders`
VALUES
  (
    10017,
    1003,
    NULL,
    'A003',
    '[{\"id\":1,\"name\":\"宫保鸡丁\",\"price\":200.0,\"count\":4,\"success\":4},{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":3,\"success\":3}]',
    866.00,
    1,
    '2024-10-28 12:52:48'
  );

INSERT INTO
  `orders`
VALUES
  (
    10018,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A002',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1},{\"id\":4,\"name\":\"青椒肉丝\",\"price\":18.0,\"count\":1,\"success\":1}]',
    56.00,
    1,
    '2024-10-28 12:57:57'
  );

INSERT INTO
  `orders`
VALUES
  (
    10019,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A002',
    '[{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1},{\"id\":4,\"name\":\"青椒肉丝\",\"price\":18.0,\"count\":1,\"success\":1}]',
    34.00,
    1,
    '2024-10-28 13:00:04'
  );

INSERT INTO
  `orders`
VALUES
  (
    10020,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A007',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 13:00:32'
  );

INSERT INTO
  `orders`
VALUES
  (
    10021,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A007',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 13:03:11'
  );

INSERT INTO
  `orders`
VALUES
  (
    10022,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A007',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":2,\"success\":2}]',
    54.00,
    1,
    '2024-10-28 13:04:35'
  );

INSERT INTO
  `orders`
VALUES
  (
    10023,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":3,\"success\":3}]',
    48.00,
    1,
    '2024-10-28 16:27:18'
  );

INSERT INTO
  `orders`
VALUES
  (
    10024,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:28:04'
  );

INSERT INTO
  `orders`
VALUES
  (
    10025,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:28:26'
  );

INSERT INTO
  `orders`
VALUES
  (
    10026,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:31:03'
  );

INSERT INTO
  `orders`
VALUES
  (
    10027,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:32:33'
  );

INSERT INTO
  `orders`
VALUES
  (
    10028,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:37:21'
  );

INSERT INTO
  `orders`
VALUES
  (
    10029,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:42:37'
  );

INSERT INTO
  `orders`
VALUES
  (
    10030,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:44:00'
  );

INSERT INTO
  `orders`
VALUES
  (
    10031,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A003',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:45:05'
  );

INSERT INTO
  `orders`
VALUES
  (
    10032,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A003',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:45:58'
  );

INSERT INTO
  `orders`
VALUES
  (
    10033,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A003',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:48:20'
  );

INSERT INTO
  `orders`
VALUES
  (
    10034,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A003',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:54:06'
  );

INSERT INTO
  `orders`
VALUES
  (
    10035,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A003',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:55:40'
  );

INSERT INTO
  `orders`
VALUES
  (
    10036,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A003',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:56:43'
  );

INSERT INTO
  `orders`
VALUES
  (
    10037,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A004',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:56:58'
  );

INSERT INTO
  `orders`
VALUES
  (
    10038,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A009',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":1,\"success\":1},{\"id\":3,\"name\":\"麻婆豆腐\",\"price\":16.0,\"count\":1,\"success\":1}]',
    38.00,
    1,
    '2024-10-28 16:57:18'
  );

INSERT INTO
  `orders`
VALUES
  (
    10039,
    NULL,
    '0426c7c0-dc7e-4b22-8b9d-1e1c837287c3',
    'A008',
    '[{\"id\":2,\"name\":\"鱼香肉丝\",\"price\":22.0,\"count\":2,\"success\":2}]',
    44.00,
    1,
    '2024-10-28 20:03:17'
  );

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;

CREATE TABLE
  `user` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
      `password` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
      `integrate` int NULL DEFAULT 0 COMMENT '积分',
      `avatar` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/api/uploads/avatar.jpg' COMMENT '头像URL',
      `role` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'USER' COMMENT '角色',
      `token` varchar(255) CHARACTER
    SET
      utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'token令牌',
      PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER
SET
  = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO
  `user`
VALUES
  (
    1000,
    'admin',
    '$2a$10$r1o/32ttBVh/V/axLDZ8ZeKhf7l0tNo9lFTrroaVF6pVmQRoigapG',
    0,
    '/api/uploads/avatar.jpg',
    'ADMIN',
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczMDExOTYwMywiZXhwIjoxNzMxNDE1NjAzfQ.45TRNA7_c30noTitFilQaQfRZcvYs87emPUUD-9bweM'
  );

-- ----------------------------
-- Triggers structure for table orders
-- ----------------------------
DROP TRIGGER IF EXISTS `after_order_insert`;

delimiter;

;

CREATE TRIGGER `after_order_insert` AFTER INSERT ON `orders` FOR EACH ROW BEGIN
-- 如果当天的统计记录不存在，插入新记录
INSERT INTO
  `daily_order_stats` (`date`, `total_orders`, `total_sales`)
VALUES
  (DATE (NEW.create_time), 1, NEW.total_price) ON DUPLICATE KEY
UPDATE total_orders = total_orders + 1,
total_sales = total_sales + NEW.total_price;

END;

;

delimiter;

-- ----------------------------
-- Triggers structure for table orders
-- ----------------------------
DROP TRIGGER IF EXISTS `after_order_delete`;

delimiter;

;

CREATE TRIGGER `after_order_delete` AFTER DELETE ON `orders` FOR EACH ROW BEGIN
-- 更新 daily_order_stats，减少对应日期的订单数量和销售额
UPDATE `daily_order_stats`
SET
  total_orders = total_orders - 1,
  total_sales = total_sales - OLD.total_price
WHERE
  `date` = DATE (OLD.create_time);

END;

;

delimiter;

SET
  FOREIGN_KEY_CHECKS = 1;