CREATE TABLE `t_operation_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `opt_module` varchar(255) DEFAULT NULL COMMENT '操作模块',
  `opt_type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `opt_url` varchar(255) DEFAULT NULL COMMENT '操作url',
  `opt_method` varchar(255) DEFAULT NULL COMMENT '操作方法',
  `request_param` longtext COMMENT '请求参数',
  `request_method` varchar(20) DEFAULT NULL COMMENT '请求方式',
  `response_data` longtext COMMENT '返回数据',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(200) DEFAULT NULL COMMENT '用户昵称',
  `ip_address` varchar(100) DEFAULT NULL COMMENT '操作ip',
  `ip_source` varchar(100) DEFAULT NULL COMMENT '操作地址',
  `total_time` int(11) NOT NULL DEFAULT '0' COMMENT '请求时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';


CREATE TABLE `t_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `mobile_phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';