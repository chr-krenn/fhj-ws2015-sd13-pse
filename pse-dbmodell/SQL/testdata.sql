-- testuser
INSERT INTO `person` VALUES (999, 'integrationtestuser','Test','User','test.user@edu.fh-joanneum.at','+43666123456789','--','Knecht','Team 4','A','-2','666','1980-01-01 00:00:00','2015-01-01 00:00:00',1,0,1,0,0,NULL, NULL);


--Community for testing, test person is a member
insert into community
	(community_id, invitation_only, name, created_by, confirmed_by)
	values (100, 0, "SWD", 108, 108);
	
insert into community_member 
	(community_id, person_id)
	values(100, 108);

----------------------------------------------
--Required for DBMessageTest: message with header by other user
--should be selected in testActivityStream() (1)
insert into message 
	(message_id, created_by, headline, message, valid_from, delivered_by) 
	values (1, 105, "Message Headline", "Test message", '2015-10-10 00:00:00', 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values(1, 100);
----------------------------------------------

----------------------------------------------
--Required for DBMessageTest: comment on message
--should NOT be selected in testActivityStream()
--should be counted in testGetNumberOfComments()
insert into message 
	(created_by, message, commented_on_message_id, delivered_by) 
	values (105, "Comment 1", 1, 1);

insert into message 
	(created_by, message, commented_on_message_id, delivered_by) 
	values (106, "Comment 2", 1, 1);

insert into message 
	(created_by, message, commented_on_message_id, delivered_by) 
	values (105, "Comment 3", 1, 1);
----------------------------------------------

----------------------------------------------
--Required for DBMessageTest: expired message
--should NOT be selected in testActivityStream()
insert into message 
	(created_by, message, delivered_by, expires_on) 
	values (102, "Expired test message", 1, '2015-10-10 00:00:00');

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message like "Expired test message"), 100);
----------------------------------------------

----------------------------------------------
--Required for DBMessageTest: message not yet valid
--should NOT be selected in testActivityStream()
insert into message 
	(created_by, message, delivered_by, valid_from) 
	values (102, "Message that isn't valid yet", 1, '2017-01-01 00:00:00');

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message like "Message that isn't valid yet"), 100);
----------------------------------------------

----------------------------------------------
--Required for DBMessageTest: news entry (message in "Portal-News" Community (ID 1))
--should NOT be selected in testActivityStream()
insert into message 
	(created_by, message, delivered_by) 
	values (2, "News entry", 1);
	
insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message = "News entry" limit 1), 1);
----------------------------------------------

----------------------------------------------	
--Required for DBMessageTest: long message by other user (no community)
--should be selected in testActivityStream() (2)
--should be selected in testActivityStream2() (1)
insert into message 
	(created_by, message, delivered_by, created_at) 
	values (104, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
			, 1, '2015-10-01 00:00:00');
----------------------------------------------

----------------------------------------------			
--Required for DBMessageTest: Message in community of test person
--should be selected in testActivityStream() (3)
insert into message 
	(created_by, headline, message, delivered_by, created_at) 
	values (107, "Message in SWD Community", "Community message__", 1, '2015-10-10 00:00:00');
	
insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message = "Community message__" limit 1), 100);
----------------------------------------------

----------------------------------------------
--Required for DBMessageTest: message by test user (no community)
--should NOT be selected in testActivityStream()
--should be selected in testActivityStream2() (2)
insert into message 
	(created_by, message, delivered_by) 
	values (108, "Test message by pompenig13", 1);
----------------------------------------------

--Communities and tags used several times below
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

insert into message_tag
	(tag_id)
	values((select tag_id from tag where token = "Software" limit 1));

--Adding tag to test person
insert into person_tag
	(person_id, tag_id)
	values(108, (select tag_id from tag where token = "Software" limit 1));

insert into person_tag
	(person_id, tag_id)
	values(108, (select tag_id from tag where token = "NZ" limit 1));

----------------------------------------------
--Creating message for public community
--should be selected in testActivityStream() (4)
insert into message 
	(created_by, headline, message, delivered_by) 
	values (102, "New software", "Message in public community tagged with Software", 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where headline = "New software" limit 1), 
	(select community_id from community where name = "Public community" limit 1));

--Adding tag to message in public community
insert into message_tag_message
	(messageTags_message_tag_id, messages_message_id)
	values((select message_tag_id from message_tag where tag_id in (select tag_id from tag where token = "Software")),
			(select message_id from message where headline = "New software" limit 1));
----------------------------------------------

----------------------------------------------
--Creating message for private community
--should NOT be selected in testActivityStream()
insert into message 
	(created_by, headline, message, delivered_by) 
	values (102, "Bug report", "----", 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where headline = "Bug report" limit 1), 
	(select community_id from community where name = "Private community" limit 1));

--Adding tag to message in private community
insert into message_tag_message
	(messageTags_message_tag_id, messages_message_id)
	values((select message_tag_id from message_tag where tag_id in (select tag_id from tag where token = "Software")),
			(select message_id from message where headline = "Bug report" limit 1));
----------------------------------------------

----------------------------------------------
--Required for DBMessageTest: news entry (message in "Portal-News" Community (ID 1)) with tag
--should NOT be selected in testActivityStream()
insert into message 
	(created_by, message, delivered_by) 
	values (2, "Second news entry", 1);
	
insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where message = "Second news entry" limit 1), 1);

insert into message_tag_message
	(messageTags_message_tag_id, messages_message_id)
	values((select message_tag_id from message_tag where tag_id in (select tag_id from tag where token = "Software")),
			(select message_id from message where message = "Second news entry" limit 1));
----------------------------------------------

----------------------------------------------
--Adding message author to contacts of test person
insert into person_relation
	(source_person_id, target_person_id)
	values(100, 108);
	
--Creating message for public community with author that is a contact of test person
--should be selected in testActivityStream() (5)
insert into message 
	(created_by, headline, message, delivered_by) 
	values (100, "Public message without tag", "Message in public community by contact of pompenig13", 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where headline = "Public message without tag" limit 1), 
	(select community_id from community where name = "Public community" limit 1));

--Creating message for private community with author that is a contact of test person
--should NOT be selected in testActivityStream()
insert into message 
	(created_by, headline, message, delivered_by) 
	values (100, "Private community, no tag", "Message in private community without tag", 1);

insert into message_community
	(messages_message_id, communities_community_id)
	values((select message_id from message where headline = "Private community, no tag" limit 1), 
	(select community_id from community where name = "Private community" limit 1));
---------------------------------------------

--Adding other message author to contacts of test person
insert into person_relation
	(source_person_id, target_person_id)
	values(107, 108);

--Likes
insert into message_rating
	(message_id, rating_person_id, created_at)
	values(1, 108, '2015-10-10 00:00:00');

insert into message_rating
	(message_id, rating_person_id, created_at)
	values(1, 105, '2015-10-09 00:00:00');
	