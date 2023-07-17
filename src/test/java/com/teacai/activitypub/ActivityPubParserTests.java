package com.teacai.activitypub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacai.activitypub.model.Activity;
import com.teacai.activitypub.model.BaseCollection;
import com.teacai.activitypub.model.ActivityStreamsFields;
import com.teacai.activitypub.model.ActivityStreamsObject;
import com.teacai.activitypub.model.BaseObject;
import com.teacai.activitypub.model.CollectionPage;
import com.teacai.activitypub.model.Link;
import com.teacai.activitypub.model.ObjectType;
import com.teacai.activitypub.model.OrderedCollection;
import com.teacai.activitypub.model.OrderedCollectionPage;
import com.teacai.activitypub.model.Place;
import com.teacai.activitypub.model.Question;
import com.teacai.activitypub.model.Tombstone;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityPubParserTests {

	private final ActivityPubParser parser = new ActivityPubParser(new ObjectMapper());

	@Test
	void parseEx001() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Object\",\n" +
				"  \"id\": \"http://www.test.example/object/1\",\n" +
				"  \"name\": \"A Simple, non-specific object\"\n" +
				"}");

		assertEquals("http://www.test.example/object/1", object.getId());
		assertEquals("A Simple, non-specific object", object.getName());
		assertEquals(ObjectType.Object, object.getType());
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getAtContext());
	}

	@Test
	void parseEx002() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Link\",\n" +
				"  \"href\": \"http://example.org/abc\",\n" +
				"  \"hreflang\": \"en\",\n" +
				"  \"mediaType\": \"text/html\",\n" +
				"  \"name\": \"An example link\"\n" +
				"}");

		assertTrue(object instanceof Link);
		assertNull(object.getId());
		assertEquals("An example link", object.getName());
		assertEquals(ObjectType.Link, object.getType());
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getAtContext());
	}

	@Test
	void parseEx003() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Activity\",\n" +
				"  \"summary\": \"Sally did something to a note\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Note\",\n" +
				"    \"name\": \"A Note\"\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.Activity, object.getType());
		assertEquals("Sally did something to a note", ((Activity) object).getSummary());
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getAtContext());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
	}

	@Test
	void parseEx004() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Travel\",\n" +
				"  \"summary\": \"Sally went to work\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"target\": {\n" +
				"    \"type\": \"Place\",\n" +
				"    \"name\": \"Work\"\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.Travel, object.getType());
		assertEquals("Sally went to work", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals(ObjectType.Place, ((Activity) object).getTargets().get(0).getType());
		assertEquals("Work", ((Activity) object).getTargets().get(0).getName());
	}

	@Test
	void parseEx005() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally's notes\",\n" +
				"  \"type\": \"Collection\",\n" +
				"  \"totalItems\": 2,\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"A Simple Note\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Another Simple Note\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");
		assertEquals(ObjectType.Collection, object.getType());
		assertEquals(2, ((BaseCollection) object).getTotalItems());
		assertEquals("A Simple Note", ((BaseCollection) object).getItems().get(0).getName());
		assertEquals(ObjectType.Note, ((BaseCollection) object).getItems().get(0).getType());
		assertEquals("Another Simple Note", ((BaseCollection) object).getItems().get(1).getName());
		assertEquals(ObjectType.Note, ((BaseCollection) object).getItems().get(1).getType());
	}

	@Test
	void parseEx006() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally's notes\",\n" +
				"  \"type\": \"OrderedCollection\",\n" +
				"  \"totalItems\": 2,\n" +
				"  \"orderedItems\": [\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"A Simple Note\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Another Simple Note\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");
		assertEquals(ObjectType.OrderedCollection, object.getType());
		assertEquals(2, ((BaseCollection) object).getTotalItems());
		assertEquals("A Simple Note", ((OrderedCollection) object).getOrderedItems().get(0).getName());
		assertEquals(ObjectType.Note, ((OrderedCollection) object).getOrderedItems().get(0).getType());
		assertEquals("Another Simple Note", ((OrderedCollection) object).getOrderedItems().get(1).getName());
		assertEquals(ObjectType.Note, ((OrderedCollection) object).getOrderedItems().get(1).getType());
	}

	@Test
	void parseEx007() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Page 1 of Sally's notes\",\n" +
				"  \"type\": \"CollectionPage\",\n" +
				"  \"id\": \"http://example.org/foo?page=1\",\n" +
				"  \"partOf\": \"http://example.org/foo\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"A Simple Note\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Another Simple Note\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");
		assertEquals(ObjectType.CollectionPage, object.getType());
		assertEquals("http://example.org/foo?page=1", object.getId());
		assertEquals("http://example.org/foo", ((CollectionPage) object).getPartOf());
		assertEquals("A Simple Note", ((CollectionPage) object).getItems().get(0).getName());
		assertEquals(ObjectType.Note, ((CollectionPage) object).getItems().get(0).getType());
		assertEquals("Another Simple Note", ((CollectionPage) object).getItems().get(1).getName());
		assertEquals(ObjectType.Note, ((CollectionPage) object).getItems().get(1).getType());
	}

	@Test
	void parseEx008() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Page 1 of Sally's notes\",\n" +
				"  \"type\": \"OrderedCollectionPage\",\n" +
				"  \"id\": \"http://example.org/foo?page=1\",\n" +
				"  \"partOf\": \"http://example.org/foo\",\n" +
				"  \"orderedItems\": [\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"A Simple Note\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Another Simple Note\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");
		assertEquals(ObjectType.OrderedCollectionPage, object.getType());
		assertEquals("Page 1 of Sally's notes", ((OrderedCollectionPage) object).getSummary());
		assertEquals("http://example.org/foo?page=1", object.getId());
		assertEquals("http://example.org/foo", ((OrderedCollectionPage) object).getPartOf());
		assertEquals("A Simple Note", ((OrderedCollectionPage) object).getOrderedItems().get(0).getName());
		assertEquals(ObjectType.Note, ((OrderedCollectionPage) object).getOrderedItems().get(0).getType());
		assertEquals("Another Simple Note", ((OrderedCollectionPage) object).getOrderedItems().get(1).getName());
		assertEquals(ObjectType.Note, ((OrderedCollectionPage) object).getOrderedItems().get(1).getType());
	}

	@Test
	void parseEx009() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally accepted an invitation to a party\",\n" +
				"  \"type\": \"Accept\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Invite\",\n" +
				"    \"actor\": \"http://john.example.org\",\n" +
				"    \"object\": {\n" +
				"      \"type\": \"Event\",\n" +
				"      \"name\": \"Going-Away Party for Jim\"\n" +
				"    }\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Accept, object.getType());
		assertEquals("Sally accepted an invitation to a party", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("Going-Away Party for Jim", ((Activity) ((Activity) object).getObjects().get(0)).getObjects().get(0).getName());
	}

	@Test
	void parseEx010() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"    \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"    \"summary\": \"Sally accepted Joe into the club\",\n" +
				"    \"type\": \"Accept\",\n" +
				"    \"actor\": {\n" +
				"      \"type\": \"Person\",\n" +
				"      \"name\": \"Sally\"\n" +
				"    },\n" +
				"    \"object\": {\n" +
				"      \"type\": \"Person\",\n" +
				"      \"name\": \"Joe\"\n" +
				"    },\n" +
				"    \"target\": {\n" +
				"      \"type\": \"Group\",\n" +
				"      \"name\": \"The Club\"\n" +
				"    }\n" +
				"  }");
		assertEquals(ObjectType.Accept, object.getType());
		assertEquals("Sally accepted Joe into the club", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("Joe", ((Activity) object).getObjects().get(0).getName());
		assertEquals("The Club", ((Activity) object).getTargets().get(0).getName());
	}

	@Test
	void parseEx011() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally tentatively accepted an invitation to a party\",\n" +
				"  \"type\": \"TentativeAccept\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Invite\",\n" +
				"    \"actor\": \"http://john.example.org\",\n" +
				"    \"object\": {\n" +
				"      \"type\": \"Event\",\n" +
				"      \"name\": \"Going-Away Party for Jim\"\n" +
				"    }\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.TentativeAccept, object.getType());
		assertEquals("Sally tentatively accepted an invitation to a party", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Link, ((Activity)((Activity) object).getObjects().get(0)).getActors().get(0).getType());
		assertEquals(ObjectType.Event, ((Activity)((Activity) object).getObjects().get(0)).getObjects().get(0).getType());
		assertEquals("Going-Away Party for Jim", ((Activity)((Activity) object).getObjects().get(0)).getObjects().get(0).getName());
	}

	@Test
	void parseEx012() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally added an object\",\n" +
				"  \"type\": \"Add\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/abc\"\n" +
				"}");
		assertEquals(ObjectType.Add, object.getType());
		assertEquals("Sally added an object", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/abc", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx013() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally added a picture of her cat to her cat picture collection\",\n" +
				"  \"type\": \"Add\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Image\",\n" +
				"    \"name\": \"A picture of my cat\",\n" +
				"    \"url\": \"http://example.org/img/cat.png\"\n" +
				"  },\n" +
				"  \"origin\": {\n" +
				"    \"type\": \"Collection\",\n" +
				"    \"name\": \"Camera Roll\"\n" +
				"  },\n" +
				"  \"target\": {\n" +
				"    \"type\": \"Collection\",\n" +
				"    \"name\": \"My Cat Pictures\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Add, object.getType());
		assertEquals("Sally added a picture of her cat to her cat picture collection", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("http://example.org/img/cat.png", ((Link) ((BaseObject) ((Activity) object).getObjects().get(0)).getUrl().get(0)).getHref());
		assertEquals("Camera Roll", ((Activity) object).getOrigin().getName());
		assertEquals("My Cat Pictures", ((Activity) object).getTargets().get(0).getName());
	}

	@Test
	void parseEx014() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally arrived at work\",\n" +
				"  \"type\": \"Arrive\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"location\": {\n" +
				"    \"type\": \"Place\",\n" +
				"    \"name\": \"Work\"\n" +
				"  },\n" +
				"  \"origin\": {\n" +
				"    \"type\": \"Place\",\n" +
				"    \"name\": \"Home\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Arrive, object.getType());
		assertEquals("Sally arrived at work", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("Work", ((Activity) object).getLocation().getName());
		assertEquals("Home", ((Activity) object).getOrigin().getName());
	}

	@Test
	void parseEx015() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally created a note\",\n" +
				"  \"type\": \"Create\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Note\",\n" +
				"    \"name\": \"A Simple Note\",\n" +
				"    \"content\": \"This is a simple note\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Create, object.getType());
		assertEquals("Sally created a note", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("A Simple Note", ((Activity) object).getObjects().get(0).getName());
		assertEquals(ObjectType.Note, ((Activity) object).getObjects().get(0).getType());
		assertEquals("This is a simple note", ((BaseObject) ((Activity) object).getObjects().get(0)).getContent());
	}

	@Test
	void parseEx016() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally deleted a note\",\n" +
				"  \"type\": \"Delete\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/notes/1\",\n" +
				"  \"origin\": {\n" +
				"    \"type\": \"Collection\",\n" +
				"    \"name\": \"Sally's Notes\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Delete, object.getType());
		assertEquals("Sally deleted a note", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("http://example.org/notes/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());
		assertEquals(ObjectType.Collection, ((Activity) object).getOrigin().getType());
		assertEquals("Sally's Notes", ((Activity) object).getOrigin().getName());
	}

	@Test
	void parseEx017() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally followed John\",\n" +
				"  \"type\": \"Follow\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"John\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Follow, object.getType());
		assertEquals("Sally followed John", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("John", ((Activity) object).getObjects().get(0).getName());
	}

	@Test
	void parseEx018() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally ignored a note\",\n" +
				"  \"type\": \"Ignore\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/notes/1\"\n" +
				"}");
		assertEquals(ObjectType.Ignore, object.getType());
		assertEquals("Sally ignored a note", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("http://example.org/notes/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx019() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally joined a group\",\n" +
				"  \"type\": \"Join\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Group\",\n" +
				"    \"name\": \"A Simple Group\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Join, object.getType());
		assertEquals("Sally joined a group", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Group, ((Activity) object).getObjects().get(0).getType());
		assertEquals("A Simple Group", ((Activity) object).getObjects().get(0).getName());
	}

	@Test
	void parseEx020() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally left work\",\n" +
				"  \"type\": \"Leave\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Place\",\n" +
				"    \"name\": \"Work\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Leave, object.getType());
		assertEquals("Sally left work", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Place, ((Activity) object).getObjects().get(0).getType());
		assertEquals("Work", ((Activity) object).getObjects().get(0).getName());
	}

	@Test
	void parseEx021() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally left a group\",\n" +
				"  \"type\": \"Leave\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Group\",\n" +
				"    \"name\": \"A Simple Group\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Leave, object.getType());
		assertEquals("Sally left a group", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Group, ((Activity) object).getObjects().get(0).getType());
		assertEquals("A Simple Group", ((Activity) object).getObjects().get(0).getName());
	}

	@Test
	void parseEx022() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally liked a note\",\n" +
				"  \"type\": \"Like\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/notes/1\"\n" +
				"}");
		assertEquals(ObjectType.Like, object.getType());
		assertEquals("Sally liked a note", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/notes/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx023() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally offered 50% off to Lewis\",\n" +
				"  \"type\": \"Offer\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"http://www.types.example/ProductOffer\",\n" +
				"    \"name\": \"50% Off!\"\n" +
				"  },\n" +
				"  \"target\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Lewis\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Offer, object.getType());
		assertEquals("Sally offered 50% off to Lewis", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Custom, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://www.types.example/ProductOffer", ((Activity) object).getObjects().get(0).getCustomType());
		assertEquals("50% Off!", ((Activity) object).getObjects().get(0).getName());
		assertEquals(ObjectType.Person, ((Activity) object).getTargets().get(0).getType());
		assertEquals("Lewis", ((Activity) object).getTargets().get(0).getName());
	}

	@Test
	void parseEx024() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally invited John and Lisa to a party\",\n" +
				"  \"type\": \"Invite\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Event\",\n" +
				"    \"name\": \"A Party\"\n" +
				"  },\n" +
				"  \"target\": [\n" +
				"    {\n" +
				"      \"type\": \"Person\",\n" +
				"      \"name\": \"John\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Person\",\n" +
				"      \"name\": \"Lisa\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");
		assertEquals(ObjectType.Invite, object.getType());
		assertEquals("Sally invited John and Lisa to a party", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Event, ((Activity) object).getObjects().get(0).getType());
		assertEquals("A Party", ((Activity) object).getObjects().get(0).getName());
		assertEquals(ObjectType.Person, ((Activity) object).getTargets().get(0).getType());
		assertEquals("John", ((Activity) object).getTargets().get(0).getName());
		assertEquals(ObjectType.Person, ((Activity) object).getTargets().get(1).getType());
		assertEquals("Lisa", ((Activity) object).getTargets().get(1).getName());
	}

	@Test
	void parseEx025() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally rejected an invitation to a party\",\n" +
				"  \"type\": \"Reject\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Invite\",\n" +
				"    \"actor\": \"http://john.example.org\",\n" +
				"    \"object\": {\n" +
				"      \"type\": \"Event\",\n" +
				"      \"name\": \"Going-Away Party for Jim\"\n" +
				"    }\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Reject, object.getType());
		assertEquals("Sally rejected an invitation to a party", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Invite, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://john.example.org", ((Link) ((BaseObject) ((Activity) object).getObjects().get(0)).getActors().get(0)).getHref());
		assertEquals(ObjectType.Event, ((Activity) ((Activity) object).getObjects().get(0)).getObjects().get(0).getType());
		assertEquals("Going-Away Party for Jim", ((Activity) ((Activity) object).getObjects().get(0)).getObjects().get(0).getName());
	}

	@Test
	void parseEx026() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally tentatively rejected an invitation to a party\",\n" +
				"  \"type\": \"TentativeReject\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Invite\",\n" +
				"    \"actor\": \"http://john.example.org\",\n" +
				"    \"object\": {\n" +
				"      \"type\": \"Event\",\n" +
				"      \"name\": \"Going-Away Party for Jim\"\n" +
				"    }\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.TentativeReject, object.getType());
		assertEquals("Sally tentatively rejected an invitation to a party", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Invite, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://john.example.org", ((Link) ((BaseObject) ((Activity) object).getObjects().get(0)).getActors().get(0)).getHref());
		assertEquals(ObjectType.Event, ((Activity) ((Activity) object).getObjects().get(0)).getObjects().get(0).getType());
		assertEquals("Going-Away Party for Jim", ((Activity) ((Activity) object).getObjects().get(0)).getObjects().get(0).getName());
	}

	@Test
	void parseEx027() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally removed a note from her notes folder\",\n" +
				"  \"type\": \"Remove\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/notes/1\",\n" +
				"  \"target\": {\n" +
				"    \"type\": \"Collection\",\n" +
				"    \"name\": \"Notes Folder\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Remove, object.getType());
		assertEquals("Sally removed a note from her notes folder", ((Activity) object).getSummary());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/notes/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());
		assertEquals(ObjectType.Collection, ((Activity) object).getTargets().get(0).getType());
		assertEquals("Notes Folder", ((Activity) object).getTargets().get(0).getName());
	}

	@Test
	void parseEx028() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"The moderator removed Sally from a group\",\n" +
				"  \"type\": \"Remove\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"http://example.org/Role\",\n" +
				"    \"name\": \"The Moderator\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"origin\": {\n" +
				"    \"type\": \"Group\",\n" +
				"    \"name\": \"A Simple Group\"\n" +
				"  }\n" +
				"}");
		assertEquals(ObjectType.Remove, object.getType());
		assertEquals("The moderator removed Sally from a group", ((Activity) object).getSummary());
		assertEquals("The Moderator", ((Activity) object).getActors().get(0).getName());
		assertEquals(ObjectType.Custom, ((Activity) object).getActors().get(0).getType());
		assertEquals("http://example.org/Role", ((Activity) object).getActors().get(0).getCustomType());

		assertEquals(ObjectType.Person, ((Activity) object).getObjects().get(0).getType());
		assertEquals("Sally", ((Activity) object).getObjects().get(0).getName());
		assertEquals(ObjectType.Group, ((Activity) object).getOrigin().getType());
		assertEquals("A Simple Group", ((Activity) object).getOrigin().getName());
	}

	@Test
	void parseEx029() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally retracted her offer to John\",\n" +
				"  \"type\": \"Undo\",\n" +
				"  \"actor\": \"http://sally.example.org\",\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Offer\",\n" +
				"    \"actor\": \"http://sally.example.org\",\n" +
				"    \"object\": \"http://example.org/posts/1\",\n" +
				"    \"target\": \"http://john.example.org\"\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.Undo, object.getType());
		assertEquals("Sally retracted her offer to John", ((Activity) object).getSummary());

		assertEquals(ObjectType.Link, ((Activity) object).getActors().get(0).getType());
		assertEquals("http://sally.example.org", ((Link) ((Activity) object).getActors().get(0)).getHref());

		assertEquals(ObjectType.Offer, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://sally.example.org", ((Link) ((Activity) ((Activity) object).getObjects().get(0)).getActors().get(0)).getHref());
		assertEquals("http://example.org/posts/1", ((Link) ((Activity) ((Activity) object).getObjects().get(0)).getObjects().get(0)).getHref());
		assertEquals("http://john.example.org", ((Link) ((Activity) ((Activity) object).getObjects().get(0)).getTargets().get(0)).getHref());
	}

	@Test
	void parseEx030() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally updated her note\",\n" +
				"  \"type\": \"Update\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/notes/1\"\n" +
				"}");

		assertEquals(ObjectType.Update, object.getType());
		assertEquals("Sally updated her note", ((Activity) object).getSummary());

		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());

		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/notes/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx031() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally read an article\",\n" +
				"  \"type\": \"View\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Article\",\n" +
				"    \"name\": \"What You Should Know About Activity Streams\"\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.View, object.getType());
		assertEquals("Sally read an article", ((Activity) object).getSummary());

		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());

		assertEquals(ObjectType.Article, ((Activity) object).getObjects().get(0).getType());
		assertEquals("What You Should Know About Activity Streams", ((Activity) object).getObjects().get(0).getName());
	}

	@Test
	void parseEx032() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally listened to a piece of music\",\n" +
				"  \"type\": \"Listen\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/music.mp3\"\n" +
				"}");

		assertEquals(ObjectType.Listen, object.getType());
		assertEquals("Sally listened to a piece of music", ((Activity) object).getSummary());

		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());

		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/music.mp3", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx033() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally read a blog post\",\n" +
				"  \"type\": \"Read\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/posts/1\"\n" +
				"}");

		assertEquals(ObjectType.Read, object.getType());
		assertEquals("Sally read a blog post", ((Activity) object).getSummary());

		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());

		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/posts/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx034() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally moved a post from List A to List B\",\n" +
				"  \"type\": \"Move\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": \"http://example.org/posts/1\",\n" +
				"  \"target\": {\n" +
				"    \"type\": \"Collection\",\n" +
				"    \"name\": \"List B\"\n" +
				"  },\n" +
				"  \"origin\": {\n" +
				"    \"type\": \"Collection\",\n" +
				"    \"name\": \"List A\"\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.Move, object.getType());
		assertEquals("Sally moved a post from List A to List B", ((Activity) object).getSummary());

		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());

		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/posts/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());

		assertEquals(ObjectType.Collection, ((Activity) object).getTargets().get(0).getType());
		assertEquals("List B", ((Activity) object).getTargets().get(0).getName());

		assertEquals(ObjectType.Collection, ((Activity) object).getOrigin().getType());
		assertEquals("List A", ((Activity) object).getOrigin().getName());
	}

	@Test
	void parseEx035() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally went home from work\",\n" +
				"  \"type\": \"Travel\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"target\": {\n" +
				"    \"type\": \"Place\",\n" +
				"    \"name\": \"Home\"\n" +
				"  },\n" +
				"  \"origin\": {\n" +
				"    \"type\": \"Place\",\n" +
				"    \"name\": \"Work\"\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.Travel, object.getType());
		assertEquals("Sally went home from work", ((Activity) object).getSummary());

		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());

		assertEquals(ObjectType.Place, ((Activity) object).getTargets().get(0).getType());
		assertEquals("Home", ((Activity) object).getTargets().get(0).getName());

		assertEquals(ObjectType.Place, ((Activity) object).getOrigin().getType());
		assertEquals("Work", ((Activity) object).getOrigin().getName());
	}

	@Test
	void parseEx036() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally announced that she had arrived at work\",\n" +
				"  \"type\": \"Announce\",\n" +
				"  \"actor\": {\n" +
				"    \"type\": \"Person\",\n" +
				"    \"id\": \"http://sally.example.org\",\n" +
				"    \"name\": \"Sally\"\n" +
				"  },\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Arrive\",\n" +
				"    \"actor\": \"http://sally.example.org\",\n" +
				"    \"location\": {\n" +
				"      \"type\": \"Place\",\n" +
				"      \"name\": \"Work\"\n" +
				"    }\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.Announce, object.getType());
		assertEquals("Sally announced that she had arrived at work", ((Activity) object).getSummary());

		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
		assertEquals("http://sally.example.org", ((Activity) object).getActors().get(0).getId());

		assertEquals(ObjectType.Arrive, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://sally.example.org", ((Link) ((Activity) ((Activity) object).getObjects().get(0)).getActors().get(0)).getHref());
		assertEquals(ObjectType.Place, ((Activity) ((Activity) object).getObjects().get(0)).getLocation().getType());
		assertEquals("Work", ((Activity) ((Activity) object).getObjects().get(0)).getLocation().getName());
	}

	@Test
	void parseEx037() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally blocked Joe\",\n" +
				"  \"type\": \"Block\",\n" +
				"  \"actor\": \"http://sally.example.org\",\n" +
				"  \"object\": \"http://joe.example.org\"\n" +
				"}");

		assertEquals(ObjectType.Block, object.getType());
		assertEquals("Sally blocked Joe", ((Activity) object).getSummary());

		assertEquals(ObjectType.Link, ((Activity) object).getActors().get(0).getType());
		assertEquals("http://sally.example.org", ((Link) ((Activity) object).getActors().get(0)).getHref());
		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://joe.example.org", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx038() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally flagged an inappropriate note\",\n" +
				"  \"type\": \"Flag\",\n" +
				"  \"actor\": \"http://sally.example.org\",\n" +
				"  \"object\": {\n" +
				"    \"type\": \"Note\",\n" +
				"    \"content\": \"An inappropriate note\"\n" +
				"  }\n" +
				"}");

		assertEquals(ObjectType.Flag, object.getType());
		assertEquals("Sally flagged an inappropriate note", ((Activity) object).getSummary());

		assertEquals(ObjectType.Link, ((Activity) object).getActors().get(0).getType());
		assertEquals("http://sally.example.org", ((Link) ((Activity) object).getActors().get(0)).getHref());
		assertEquals(ObjectType.Note, ((Activity) object).getObjects().get(0).getType());
		assertEquals("An inappropriate note", ((BaseObject) ((Activity) object).getObjects().get(0)).getContent());
	}

	@Test
	void parseEx039() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally disliked a post\",\n" +
				"  \"type\": \"Dislike\",\n" +
				"  \"actor\": \"http://sally.example.org\",\n" +
				"  \"object\": \"http://example.org/posts/1\"\n" +
				"}");

		assertEquals(ObjectType.Dislike, object.getType());
		assertEquals("Sally disliked a post", ((Activity) object).getSummary());

		assertEquals(ObjectType.Link, ((Activity) object).getActors().get(0).getType());
		assertEquals("http://sally.example.org", ((Link) ((Activity) object).getActors().get(0)).getHref());
		assertEquals(ObjectType.Link, ((Activity) object).getObjects().get(0).getType());
		assertEquals("http://example.org/posts/1", ((Link) ((Activity) object).getObjects().get(0)).getHref());
	}

	@Test
	void parseEx040() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Question\",\n" +
				"  \"name\": \"What is the answer?\",\n" +
				"  \"oneOf\": [\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Option A\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Option B\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");

		assertEquals(ObjectType.Question, object.getType());
		assertEquals("What is the answer?", object.getName());
		assertEquals(ObjectType.Note, ((Question) object).getOneOf().get(0).getType());
		assertEquals("Option A", ((Question) object).getOneOf().get(0).getName());
		assertEquals(ObjectType.Note, ((Question) object).getOneOf().get(1).getType());
		assertEquals("Option B", ((Question) object).getOneOf().get(1).getName());
	}

	@Test
	void parseEx041() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Question\",\n" +
				"  \"name\": \"What is the answer?\",\n" +
				"  \"closed\": \"2016-05-10T00:00:00Z\"\n" +
				"}");

		assertEquals(ObjectType.Question, object.getType());
		assertEquals("What is the answer?", object.getName());

		assertEquals(Instant.parse("2016-05-10T00:00:00Z"), ((Question) object).getClosed());
	}

	@Test
	void parseEx042() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Application\",\n" +
				"  \"name\": \"Exampletron 3000\"\n" +
				"}");

		assertEquals(ObjectType.Application, object.getType());
		assertEquals("Exampletron 3000", object.getName());
	}

	@Test
	void parseEx043() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Group\",\n" +
				"  \"name\": \"Big Beards of Austin\"\n" +
				"}");

		assertEquals(ObjectType.Group, object.getType());
		assertEquals("Big Beards of Austin", object.getName());
	}

	@Test
	void parseEx044() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Organization\",\n" +
				"  \"name\": \"Example Co.\"\n" +
				"}");

		assertEquals(ObjectType.Organization, object.getType());
		assertEquals("Example Co.", object.getName());
	}

	@Test
	void parseEx045() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Person\",\n" +
				"  \"name\": \"Sally Smith\"\n" +
				"}");

		assertEquals(ObjectType.Person, object.getType());
		assertEquals("Sally Smith", object.getName());
	}

	@Test
	void parseEx046() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Service\",\n" +
				"  \"name\": \"Acme Web Service\"\n" +
				"}");

		assertEquals(ObjectType.Service, object.getType());
		assertEquals("Acme Web Service", object.getName());
	}

	@Test
	void parseEx057() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Place\",\n" +
				"  \"name\": \"Fresno Area\",\n" +
				"  \"latitude\": 36.75,\n" +
				"  \"longitude\": 119.7667,\n" +
				"  \"radius\": 15,\n" +
				"  \"units\": \"miles\"\n" +
				"}");

		assertEquals(ObjectType.Place, object.getType());
		assertEquals("Fresno Area", object.getName());
		assertEquals(36.75, ((Place) object).getLatitude());
		assertEquals(119.7667, ((Place) object).getLongitude());
		assertEquals(15, ((Place) object).getRadius());
		assertEquals("miles", ((Place) object).getUnits());
	}

	@Test
	void parseEx058() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Mention of Joe by Carrie in her note\",\n" +
				"  \"type\": \"Mention\",\n" +
				"  \"href\": \"http://example.org/joe\",\n" +
				"  \"name\": \"Joe\"\n" +
				"}");
		//TODO: Summary in a Mention/Link?
		assertEquals(ObjectType.Mention, object.getType());
		assertEquals("Joe", object.getName());
		assertEquals("http://example.org/joe", ((Link) object).getHref());
	}

	@Test
	void parseEx060() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"type\": \"OrderedCollection\",\n" +
				"  \"totalItems\": 3,\n" +
				"  \"name\": \"Vacation photos 2016\",\n" +
				"  \"orderedItems\": [\n" +
				"    {\n" +
				"      \"type\": \"Image\",\n" +
				"      \"id\": \"http://image.example/1\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Tombstone\",\n" +
				"      \"formerType\": \"Image\",\n" +
				"      \"id\": \"http://image.example/2\",\n" +
				"      \"deleted\": \"2016-03-17T00:00:00Z\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Image\",\n" +
				"      \"id\": \"http://image.example/3\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");

		assertEquals(ObjectType.OrderedCollection, object.getType());
		assertEquals(ObjectType.Image, ((OrderedCollection) object).getOrderedItems().get(0).getType());
		assertEquals("http://image.example/1", ((OrderedCollection) object).getOrderedItems().get(0).getId());
		assertEquals(ObjectType.Tombstone, ((OrderedCollection) object).getOrderedItems().get(1).getType());
		assertEquals("http://image.example/2", ((OrderedCollection) object).getOrderedItems().get(1).getId());
		assertEquals(ObjectType.Image, ((Tombstone) ((OrderedCollection) object).getOrderedItems().get(1)).getFormerType());
		assertEquals(Instant.parse("2016-03-17T00:00:00Z"), ((Tombstone) ((OrderedCollection) object).getOrderedItems().get(1)).getDeleted());
	}

	@Test
	void parseEx065() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Sally and Joe offered the Foo object\",\n" +
				"  \"type\": \"Offer\",\n" +
				"  \"actor\": [\n" +
				"    \"http://joe.example.org\",\n" +
				"    {\n" +
				"      \"type\": \"Person\",\n" +
				"      \"id\": \"http://sally.example.org\",\n" +
				"      \"name\": \"Sally\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"object\": \"http://example.org/foo\"\n" +
				"}");

		assertNull(((Activity) object).getActors().get(0).getName());
		assertEquals("http://joe.example.org", ((Link) ((Activity) object).getActors().get(0)).getHref());
		assertEquals(ObjectType.Link, ((Activity) object).getActors().get(0).getType());
		assertEquals("Sally", ((Activity) object).getActors().get(1).getName());
		assertEquals("http://sally.example.org", ((Activity) object).getActors().get(1).getId());
		assertEquals(ObjectType.Person, ((Activity) object).getActors().get(1).getType());
	}

	@Test
	void parseEx073() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Activities in context 1\",\n" +
				"  \"type\": \"Collection\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"type\": \"Offer\",\n" +
				"      \"actor\": \"http://sally.example.org\",\n" +
				"      \"object\": \"http://example.org/posts/1\",\n" +
				"      \"target\": \"http://john.example.org\",\n" +
				"      \"context\": \"http://example.org/contexts/1\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Like\",\n" +
				"      \"actor\": \"http://joe.example.org\",\n" +
				"      \"object\": \"http://example.org/posts/2\",\n" +
				"      \"context\": \"http://example.org/contexts/1\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");

		assertEquals(ObjectType.Collection, object.getType());
		assertEquals(ObjectType.Link, ((BaseObject) ((BaseCollection) object).getItems().get(0)).getContext().getType());
		assertEquals(ObjectType.Link, ((BaseObject) ((BaseCollection) object).getItems().get(1)).getContext().getType());
		assertEquals("http://example.org/contexts/1", ((Link) ((Activity) ((BaseCollection) object).getItems().get(0)).getContext()).getHref());
		assertEquals("http://example.org/contexts/1", ((Link) ((Activity) ((BaseCollection) object).getItems().get(1)).getContext()).getHref());
		assertEquals("http://sally.example.org", ((Link) ((Activity) ((BaseCollection) object).getItems().get(0)).getActors().get(0)).getHref());
		assertEquals("http://joe.example.org", ((Link) ((Activity) ((BaseCollection) object).getItems().get(1)).getActors().get(0)).getHref());
		assertEquals("http://john.example.org", ((Link) ((Activity) ((BaseCollection) object).getItems().get(0)).getTargets().get(0)).getHref());

	}

	@Test
	void parseEx096() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Page 2 of Sally's blog posts\",\n" +
				"  \"type\": \"CollectionPage\",\n" +
				"  \"next\": {\n" +
				"    \"type\": \"Link\",\n" +
				"    \"name\": \"Next Page\",\n" +
				"    \"href\": \"http://example.org/collection?page=2\"\n" +
				"  },\n" +
				"  \"items\": [\n" +
				"    \"http://example.org/posts/1\",\n" +
				"    \"http://example.org/posts/2\",\n" +
				"    \"http://example.org/posts/3\"\n" +
				"  ]\n" +
				"}");

		assertEquals(ObjectType.CollectionPage, object.getType());
		assertEquals("http://example.org/posts/3", ((Link) ((CollectionPage) object).getItems().get(2)).getHref());
		assertEquals(ObjectType.Link, ((CollectionPage) object).getNext().getType());
		assertEquals("http://example.org/collection?page=2", ((Link) ((CollectionPage) object).getNext()).getHref());
	}

	@Test
	void parseEx100() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Page 1 of Sally's blog posts\",\n" +
				"  \"type\": \"CollectionPage\",\n" +
				"  \"prev\": \"http://example.org/collection?page=1\",\n" +
				"  \"items\": [\n" +
				"    \"http://example.org/posts/1\",\n" +
				"    \"http://example.org/posts/2\",\n" +
				"    \"http://example.org/posts/3\"\n" +
				"  ]\n" +
				"}");

		assertEquals(ObjectType.CollectionPage, object.getType());
		assertEquals("Page 1 of Sally's blog posts", ((CollectionPage) object).getSummary());
		assertEquals("http://example.org/posts/3", ((Link) ((CollectionPage) object).getItems().get(2)).getHref());
		assertEquals(ObjectType.Link, ((CollectionPage) object).getPrev().getType());
		assertEquals("http://example.org/collection?page=1", ((Link) ((CollectionPage) object).getPrev()).getHref());
	}

	@Test
	void parseEx101() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Page 1 of Sally's blog posts\",\n" +
				"  \"type\": \"CollectionPage\",\n" +
				"  \"prev\": {\n" +
				"    \"type\": \"Link\",\n" +
				"    \"name\": \"Previous Page\",\n" +
				"    \"href\": \"http://example.org/collection?page=1\"\n" +
				"  },\n" +
				"  \"items\": [\n" +
				"    \"http://example.org/posts/1\",\n" +
				"    \"http://example.org/posts/2\",\n" +
				"    \"http://example.org/posts/3\"\n" +
				"  ]\n" +
				"}");

		assertEquals(ObjectType.CollectionPage, object.getType());
		assertEquals("Page 1 of Sally's blog posts", ((CollectionPage) object).getSummary());
		assertEquals("http://example.org/posts/1", ((Link) ((CollectionPage) object).getItems().get(0)).getHref());
		assertEquals("http://example.org/posts/2", ((Link) ((CollectionPage) object).getItems().get(1)).getHref());
		assertEquals("http://example.org/posts/3", ((Link) ((CollectionPage) object).getItems().get(2)).getHref());
		assertEquals(ObjectType.Link, ((CollectionPage) object).getPrev().getType());
		assertEquals("http://example.org/collection?page=1", ((Link) ((CollectionPage) object).getPrev()).getHref());
		assertEquals("Previous Page", ((CollectionPage) object).getPrev().getName());
	}

	@Test
	void parseEx112() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"name\": \"Liu Gu Lu Cun, Pingdu, Qingdao, Shandong, China\",\n" +
				"  \"type\": \"Place\",\n" +
				"  \"latitude\": 36.75,\n" +
				"  \"longitude\": 119.7667,\n" +
				"  \"accuracy\": 94.5\n" +
				"}");

		assertEquals(ObjectType.Place, object.getType());
		assertEquals("Liu Gu Lu Cun, Pingdu, Qingdao, Shandong, China", object.getName());
		assertEquals(94.5, ((Place) object).getAccuracy());
		assertEquals(36.75, ((Place) object).getLatitude());
		assertEquals(119.7667, ((Place) object).getLongitude());
	}

	@Test
	void parseEx113() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Place\",\n" +
				"  \"name\": \"Fresno Area\",\n" +
				"  \"altitude\": 15.0,\n" +
				"  \"latitude\": 36.75,\n" +
				"  \"longitude\": 119.7667,\n" +
				"  \"units\": \"miles\"\n" +
				"}");

		assertEquals(ObjectType.Place, object.getType());
		assertEquals("Fresno Area", object.getName());
		assertEquals(15.0, ((Place) object).getAltitude());
		assertEquals(36.75, ((Place) object).getLatitude());
		assertEquals(119.7667, ((Place) object).getLongitude());
		assertEquals("miles", ((Place) object).getUnits());
	}

	@Test
	void parseEx132() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"summary\": \"Page 1 of Sally's notes\",\n" +
				"  \"type\": \"OrderedCollectionPage\",\n" +
				"  \"startIndex\": 0,\n" +
				"  \"orderedItems\": [\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Density of Water\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"type\": \"Note\",\n" +
				"      \"name\": \"Air Mattress Idea\"\n" +
				"    }\n" +
				"  ]\n" +
				"}");

		assertEquals(ObjectType.OrderedCollectionPage, object.getType());
		assertEquals("Page 1 of Sally's notes", ((OrderedCollectionPage) object).getSummary());
		assertEquals(0, ((OrderedCollectionPage) object).getStartIndex());
		assertEquals("Density of Water", ((OrderedCollectionPage) object).getOrderedItems().get(0).getName());
		assertEquals(ObjectType.Note, ((OrderedCollectionPage) object).getOrderedItems().get(0).getType());
		assertEquals("Air Mattress Idea", ((OrderedCollectionPage) object).getOrderedItems().get(1).getName());
		assertEquals(ObjectType.Note, ((OrderedCollectionPage) object).getOrderedItems().get(1).getType());
	}

}
