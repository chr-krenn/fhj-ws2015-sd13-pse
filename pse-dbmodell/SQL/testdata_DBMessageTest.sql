--message_id is set automatically (AUTO_INCREMENT column)
insert into message 
	(created_by, message, delivered_by)
	values(101, "Private message @pompenig13", 1);

insert into message 
	(created_by, message, delivered_by)
	values(100, "Private message @florian.genser", 1);
	
--works only after the private community per person has been created (at app start or in DBMessageTest)
insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message = "Private message @pompenig13" LIMIT 1), 
		(select community_id from community where name = '@pompenig13'));

--private message from contact of test person to other contact of test person
insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message = "Private message @florian.genser" LIMIT 1), 
		(select community_id from community where name = '@florian.genser'));