USE ordermgr;

CREATE TABLE `transaction_type` (
  `TRANS_TYPE_ID` int(11) NOT NULL,
  `CODE` varchar(25) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`TRANS_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into `ordermgr`.`transaction_type`(`TRANS_TYPE_ID`,`CODE`,`NAME`) values (1,'PMT','PAYMENT');
insert into `ordermgr`.`transaction_type`(`TRANS_TYPE_ID`,`CODE`,`NAME`) values (2,'RFD','REFUND');
