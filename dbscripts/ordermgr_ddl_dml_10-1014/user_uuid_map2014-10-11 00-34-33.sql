USE ordermgr;

CREATE TABLE `user_uuid_map` (
  `user_id` int(11) NOT NULL,
  `uuid` varchar(50) NOT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


