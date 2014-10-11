USE ordermgr;

CREATE TABLE `user_login` (
  `user_id` varchar(20) NOT NULL,
  `password` varchar(500) DEFAULT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_ts` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into `ordermgr`.`user_login`(`user_id`,`password`,`create_ts`,`update_ts`) values ('admin','admin','2013-11-08 01:12:31','2013-11-08 01:12:31');
insert into `ordermgr`.`user_login`(`user_id`,`password`,`create_ts`,`update_ts`) values ('gjoy','gjoy','2014-01-29 23:06:17','2014-01-29 23:06:26');
