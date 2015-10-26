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
	
--Public community
insert into community
	(invitation_only, name, created_by, confirmed_by)
	values(0,"Public community", 2, 2);

--Private community
insert into community
	(invitation_only, name, created_by, confirmed_by)
	values(1,"Private community", 2, 2);

--Tags
insert into tag
	(token)
	values("Software");

insert into tag
	(token)
	values("Other");

insert into tag
	(token)
	values("NZ");

--Adding tag to test person
insert into person_tag
	(person_id, tag_id)
	values(108, (select tag_id from tag where token = "Software"));

insert into person_tag
	(person_id, tag_id)
	values(108, (select tag_id from tag where token = "NZ"));
	
--Creating messages for public community
insert into message 
	(created_by, headline, message, delivered_by) 
	values (102, "New software", "Message in public community tagged with Software", 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where headline = "New software"), (select community_id from community where name = "Public community"));

--Creating message for private community
insert into message 
	(created_by, headline, message, delivered_by) 
	values (102, "Bug report", "----", 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where headline = "Bug report"), (select community_id from community where name = "Private community"));

--Adding tag to message in public community
insert into message_tag
	(tag_id)
	values((select tag_id from tag where token = "Software"));

insert into message_tag_message
	(messageTags_message_tag_id, messages_message_id)
	values((select message_tag_id from message_tag where tag_id in (select tag_id from tag where token = "Software")),
			(select message_id from message where headline = "New software"));

--Adding tag to message in private community
insert into message_tag_message
	(messageTags_message_tag_id, messages_message_id)
	values((select message_tag_id from message_tag where tag_id in (select tag_id from tag where token = "Software")),
			(select message_id from message where headline = "Bug report"));

--Creating messages for public community
insert into message 
	(created_by, headline, message, delivered_by) 
	values (100, "Public message without tag", "Message in public community by contact of pompenig13", 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where headline = "Public message without tag"), (select community_id from community where name = "Public community"));

--Adding message author to contacts of test person
insert into person_relation
	(source_person_id, target_person_id)
	values(100, 108);