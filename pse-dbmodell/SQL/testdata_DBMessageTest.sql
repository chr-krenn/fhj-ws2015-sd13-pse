--message_id is set automatically (AUTO_INCREMENT column)
insert into message 
	(created_by, message, delivered_by)
	values(101, "Private message @pompenig13", 1);
	
--works only after the private community per person has been created (at app start or in DBMessageTest)
insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message = "Private message @pompenig13" LIMIT 1), 
		(select community_id from community where name = '@pompenig13'));