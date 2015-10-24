insert into message 
	(message_id, created_by, message, valid_from, delivered_by, expires_on) 
	values (1, 108, "Expired test message", CURRENT_TIMESTAMP, 1, '2015-10-10 00:00:00');

insert into message 
	(message_id, created_by, message, valid_from, delivered_by) 
	values (2, 104, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
			, CURRENT_TIMESTAMP, 1);

insert into message 
	(message_id, created_by, message, valid_from, delivered_by) 
	values (3, 108, "Community message", CURRENT_TIMESTAMP, 1);
	
insert into message 
	(message_id, created_by, message, valid_from, delivered_by) 
	values (4, 105, "Test message", CURRENT_TIMESTAMP, 1);	
	
insert into message 
	(message_id, created_by, message, valid_from, delivered_by) 
	values (5, 100, "Private message @angelofr13", CURRENT_TIMESTAMP, 1);	
	
insert into community
	(community_id, invitation_only, name, created_by, confirmed_by)
	values (100, 0, "SWD", 108, 108);
	
insert into community_member 
	(community_member_id, community_id, person_id)
	values(100, 100, 108);

insert into message_community
	(messages_message_id, communities_community_id)
	values(3, 100);

--works in app, but not in junit because the private community per person is created at app start
--insert into message_community
--	(messages_message_id, communities_community_id)
--	values(5, 101);
