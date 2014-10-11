USE ordermgr;

CREATE TABLE `user_group_map` (
  `user_id` varchar(20) NOT NULL DEFAULT '',
  `group_id` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into `ordermgr`.`user_group_map`(`user_id`,`group_id`) values ('admin','admin');
insert into `ordermgr`.`user_group_map`(`user_id`,`group_id`) values ('gjoy','manager');
