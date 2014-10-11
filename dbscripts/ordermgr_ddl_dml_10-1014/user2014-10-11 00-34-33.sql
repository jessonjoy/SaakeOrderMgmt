USE ordermgr;

CREATE TABLE `user` (
  `user_id` varchar(20) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `prefix` varchar(20) DEFAULT NULL,
  `suffix` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


insert into `ordermgr`.`user`(`user_id`,`first_name`,`middle_name`,`last_name`,`prefix`,`suffix`,`email`,`id`) values ('admin','Admin','','Admin',null,null,null,1);
insert into `ordermgr`.`user`(`user_id`,`first_name`,`middle_name`,`last_name`,`prefix`,`suffix`,`email`,`id`) values ('gjoy','George',null,'Joy',null,null,null,2);
