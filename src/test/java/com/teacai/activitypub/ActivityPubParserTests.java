package com.teacai.activitypub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacai.activitypub.model.Activity;
import com.teacai.activitypub.model.ActivityStreamsFields;
import com.teacai.activitypub.model.ActivityStreamsObject;
import com.teacai.activitypub.model.Link;
import com.teacai.activitypub.model.ObjectType;
import com.teacai.activitypub.model.OrderedCollection;
import com.teacai.activitypub.model.Tombstone;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityPubParserTests {

	private final ActivityPubParser parser = new ActivityPubParser(new ObjectMapper());

	@Test
	void parseEx1() throws JsonProcessingException {
		ActivityStreamsObject object = parser.parse("{\n" +
				"  \"@context\": \"https://www.w3.org/ns/activitystreams\",\n" +
				"  \"type\": \"Object\",\n" +
				"  \"id\": \"http://www.test.example/object/1\",\n" +
				"  \"name\": \"A Simple, non-specific object\"\n" +
				"}");

		assertEquals("http://www.test.example/object/1", object.getId());
		assertEquals("A Simple, non-specific object", object.getName());
		assertEquals(ObjectType.Object, object.getType());
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getContext());
	}

	@Test
	void parseEx2() throws JsonProcessingException {
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
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getContext());
	}

	@Test
	void parseEx3() throws JsonProcessingException {
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
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getContext());
		assertEquals("Sally", ((Activity) object).getActors().get(0).getName());
	}

	@Test
	void parseEx60() throws JsonProcessingException {
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
	void parseEx65() throws JsonProcessingException {
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

}
