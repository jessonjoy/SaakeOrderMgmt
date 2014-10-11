USE ordermgr;

CREATE TABLE `user_group` (
  `group_id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into `ordermgr`.`user_group`(`group_id`,`name`) values ('admin','admin');
insert into `ordermgr`.`user_group`(`group_id`,`name`) values ('manager','manager');
insert into `ordermgr`.`user_group`(`group_id`,`name`) values ('technician','technician');
