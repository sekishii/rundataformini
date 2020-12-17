SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

SET GROUP_CONCAT_MAX_LEN=32768;
SET @tables = NULL;
SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables
  FROM information_schema.tables
  WHERE table_schema = (SELECT DATABASE());
SELECT IFNULL(@tables,'dummy') INTO @tables;

SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables);
PREPARE stmt FROM @tables;
EXECUTE stmt;
SET SESSION FOREIGN_KEY_CHECKS = 1;


/* Create Tables */
--
CREATE TABLE run_data
(
	-- id
	id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	-- WeChatID
	wechat_id varchar(128) COMMENT 'WeChatID',
	-- 採番：ACC+10桁数字
	user_name varchar(128) NOT NULL COMMENT '用户名',
	-- 用户状态
	-- D: 删除
	-- A：激活
	status char(1) DEFAULT 'D' NOT NULL COMMENT '用户状态',
	-- 运动日期
	run_date date COMMENT '运动日期',
	-- 运动步数
	run_steps int COMMENT '运动步数',
-- 	-- 作成日時
-- 	created_at timestamp(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '作成日時',
-- 	-- 更新日時
-- 	updated_at timestamp(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新日時',
-- 	-- 更新者
-- 	updated_by varchar(16) COMMENT '更新者',
	PRIMARY KEY (user_name),
	UNIQUE (id)
) COMMENT = '运动步数表' DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;





