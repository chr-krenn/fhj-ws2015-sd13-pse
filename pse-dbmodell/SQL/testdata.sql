--Required for DBMessageTest: expired message
--should NOT be selected in testActivityStream()
insert into message 
	(message_id, created_by, message, valid_from, delivered_by, expires_on) 
	values (1, 102, "Expired test message", CURRENT_TIMESTAMP, 1, '2015-10-10 00:00:00');

--Required for DBMessageTest: long message by other user (no community)
--should be selected in testActivityStream() (1)
insert into message 
	(message_id, created_by, message, valid_from, delivered_by) 
	values (2, 104, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
			, CURRENT_TIMESTAMP, 1);

--Required for DBMessageTest: Message in community of test person
--should be selected in testActivityStream() (2)
insert into message 
	(message_id, created_by, message, valid_from, delivered_by) 
	values (3, 107, "Community message", CURRENT_TIMESTAMP, 1);
	
insert into community
	(community_id, invitation_only, name, created_by, confirmed_by)
	values (100, 0, "SWD", 108, 108);
	
insert into community_member 
	(community_member_id, community_id, person_id)
	values(100, 100, 108);

insert into message_community
	(messages_message_id, communities_community_id)
	values(3, 100);
	
--Required for DBMessageTest: message with header by other user (no community)
--should be selected in testActivityStream() (3)
insert into message 
	(message_id, created_by, headline, message, valid_from, delivered_by) 
	values (4, 105, "Message Headline", "Test message", CURRENT_TIMESTAMP, 1);	

--Required for DBMessageTest: message by test user
--should NOT be selected in testActivityStream()
insert into message 
	(message_id, created_by, message, valid_from, delivered_by) 
	values (5, 108, "Test message by pompenig13", CURRENT_TIMESTAMP, 1);
	

