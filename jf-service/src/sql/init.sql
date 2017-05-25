INSERT INTO `jf_tag` (`id`, `name`, `weight`)
VALUES
	(1,'律师',1),
	(2,'房屋纠纷',2),
	(3,'房产',3),
	(4,'离婚',3),
	(5,'税费承担',1),
	(6,'户口问题',2),
	(7,'财产',1),
	(8,'合同',2),
	(9,'政策变动',3),
	(10,'户籍',1);
	
INSERT INTO `jf_oss_role` (`id`, `name`, `desc`, `status`)
VALUES
	(1,'super_admin','特别管理员',0),
	(2,'admin','一般管理员',0),
	(3,'order_admin','订单管理员',0),
	(4,'lawyer','律师',0);
	

INSERT INTO `jf_property` (`id`, `type_id`, `name`)
VALUES
	(1,1,'买家'),
	(2,1,'卖家'),
	(3,1,'中介'),
	(4,2,'居间协议'),
	(5,2,'买卖合同');
	
INSERT INTO `jf_phase` (`id`, `phase`, `parent_phase_id`, `role_id`)
VALUES
	(1,'已完成网签',0,1),
	(2,'已完成网签',0,2),
	(3,'已办理过户登记',0,1),
	(4,'已办理过户登记',0,2),
	(5,'已款清交房',0,1),
	(6,'已款清交房',0,2),
	(7,'任何阶段',0,3),
	(8,'购房资格',1,1),
	(9,'购房资格',2,2),
	(10,'贷款问题',1,1),
	(11,'贷款问题',2,2),
	(12,'税收问题',1,1),
	(13,'税收问题',2,2),
	(14,'资金问题',1,1),
	(15,'资金问题',2,2),
	(16,'产权问题（含查封）',1,1),
	(17,'产权问题（含查封）',2,2),
	(18,'共有人异议',1,1),
	(19,'共有人异议',2,2),
	(20,'网签条款有分歧',1,1),
	(21,'网签条款有分歧',2,2),
	(22,'无理由拒绝网签',1,1),
	(23,'无理由拒绝网签',2,2),
	(24,'网签时间有异议',1,1),
	(25,'网签时间有异议',2,2),
	(26,'其他',1,1),
	(27,'其他',2,2),
	(28,'逾期付款',3,1),
	(29,'逾期付款',4,2),
	(30,'逾期过户',3,1),
	(31,'逾期过户',4,2),
	(32,'被查封',3,1),
	(33,'被查封',4,2),
	(34,'税收问题',3,1),
	(35,'税收问题',4,2),
	(36,'家事更名',3,1),
	(37,'家事更名',4,2),
	(38,'以房抵债',3,1),
	(39,'以房抵债',4,2),
	(40,'房屋问题',3,1),
	(41,'房屋问题',4,2),
	(42,'其他',3,1),
	(43,'其他',4,2),
	(44,'逾期交房',5,1),
	(45,'逾期交房',6,2),
	(46,'违章搭建',5,1),
	(47,'违章搭建',6,2),
	(48,'尾款问题',5,1),
	(49,'尾款问题',6,2),
	(50,'贷款发放',5,1),
	(51,'贷款发放',6,2),
	(52,'户口问题',5,1),
	(53,'户口问题',6,2),
	(54,'其他',5,1),
	(55,'其他',6,2),
	(56,'户口未迁出',7,1),
	(57,'户口未迁出',8,2),
	(58,'凶宅',7,1),
	(59,'凶宅',8,2),
	(60,'主张合同无效',7,1),
	(61,'主张合同无效',8,2),
	(62,'其他',7,1),
	(63,'其他',8,2),
	(64,'拒付佣金',9,3),
	(65,'被要求返还佣金',9,3),
	(66,'重大过错赔偿责任',9,3),
	(67,'其他',9,3);
	
INSERT INTO `jf_product` 
VALUES 
	(1,'一站式咨询',99,NULL,NULL,0,'Y'),
	(2,'一站式咨询(加急)',129,NULL,NULL,0,'YP'),
	(3,'简单问',9.9,NULL,NULL,0,'J'),
	(4,'代查户口',600,NULL,NULL,0,'H'),
	(5,'查封信息',1000,NULL,NULL,0,'C');