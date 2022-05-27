--初始化项目信息

CREATE TABLE `t_crawl_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '爬虫源名称',
  `rule` json NOT NULL COMMENT '爬虫规则',
  `status` tinyint(1) NOT NULL COMMENT '爬虫源状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫源信息';

INSERT INTO `l_novel`.`t_crawl_source` (`name`, `rule`, `status`)
VALUES ('书趣阁', '{"descEnd": "最新章节推荐地址", "descStart": "<div class=\"intro_info\">", "webPreUrl": "https://wap.shuquge.com", "contentEnd": "</div>", "bookListUrl": "https://wap.shuquge.com/sort/{cid}/0_{page}.html", "categoryUrl": "https://wap.shuquge.com/sort/", "bookIdPatten": "href=\"/s/(\\d+)\\.html\"", "contentStart": "<div id=\"nr1\">", "picUrlPatten": "<img\\s+src=\"(https://www.shuquge.com/files/article/image/\\d+/\\d+/\\d+s.jpg)\"", "bookDetailUrl": "https://wap.shuquge.com/s/{bookId}.html", "indexIdPatten": "<li><a\\s+href=\"/chapter/\\d+_(\\d+).html\">[^/]+</a></li>", "bookContentUrl": "https://wap.shuquge.com/chapter/{bookId}_{indexId}.html", "bookIndexStart": "正文", "bookNamePatten": "<a\\s+href=\"/s/\\d+\\.html\"><h2>([^/]+)</h2></a>", "nextPagePatten": "<a\\s+id=\"pb_next\"\\s+href=\"/chapter/\\d+_(\\d+_\\d+).html\">下一页</a>", "indexNamePatten": "<li><a\\s+href=\"/chapter/\\d+_\\d+.html\">([^/]+)</a></li>", "authorNamePatten": "<p>作者：([^/]+)</p>", "categoryIdPatten": "<li\\s+class=\"prev\"><a[^/]+href=\"/sort/(\\d+)/0_1.html\">[^/]+</a></li>", "contentPattenMap": {"[(]第\\d+/\\d+页[)]": "", "（本章未完，请点击下一页继续阅读）": ""}, "categoryNamePatten": "<li\\s+class=\"prev\"><a[^/]+href=\"/sort/\\d+/0_1.html\">([^/]+)</a></li>", "nextIndexUrlPatten": "<a\\s+href=\"(/d/\\d+_\\d+.html)\"\\s+class=\"onclick\">下一页</a>", "detailCategoryIdPatten": "<p>分类：<a\\s+href=\"/sort/(\\d+)/0_1.html\">[^/]+</a></p>"}', 1);

CREATE TABLE `t_category_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` int(11) NOT NULL COMMENT '爬虫源id',
  `source_cid` varchar(50) NOT NULL COMMENT '爬虫源分类id',
  `cname` varchar(100) NOT NULL COMMENT '分类名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类信息';


CREATE TABLE `t_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_id` int(11) NOT NULL COMMENT '来源id',
  `cid` int(11) NOT NULL COMMENT '分类id',
  `source_book_id` varchar(50) NOT NULL COMMENT '爬虫源书id',
  `book_name` varchar(100) NOT NULL COMMENT '书名',
  `book_pic` varchar(100) NOT NULL DEFAULT '' COMMENT '封面地址',
  `book_desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '描述',
  `author_name` varchar(100) NOT NULL COMMENT '作者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='电子书信息';


CREATE TABLE `t_book_index` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `book_id` bigint(20) NOT NULL COMMENT '书id',
  `source_index_id` varchar(50) NOT NULL COMMENT '数据源章节id',
  `sort_id` int(11) NOT NULL COMMENT '排序值',
  `title` varchar(200) NOT NULL COMMENT '章节标题',
  `success` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='章节信息';


CREATE TABLE `t_book_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `index_id` bigint(20) NOT NULL COMMENT '章节id',
  `content` text NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='章节内容信息';

