package com.teacai.activitypub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacai.activitypub.model.Activity;
import com.teacai.activitypub.model.ActivityStreamsCollection;
import com.teacai.activitypub.model.ActivityStreamsFields;
import com.teacai.activitypub.model.ActivityStreamsObject;
import com.teacai.activitypub.model.BaseObject;
import com.teacai.activitypub.model.CollectionPage;
import com.teacai.activitypub.model.Link;
import com.teacai.activitypub.model.ObjectType;
import com.teacai.activitypub.model.OrderedCollection;
import com.teacai.activitypub.model.OrderedCollectionPage;
import com.teacai.activitypub.model.Place;
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
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getContext());
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
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getContext());
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
		assertEquals(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS, object.getContext());
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
		assertEquals(ObjectType.Place, ((Activity) object).getTarget().getType());
		assertEquals("Work", ((Activity) object).getTarget().getName());
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
		assertEquals(2, ((ActivityStreamsCollection) object).getTotalItems());
		assertEquals("A Simple Note", ((ActivityStreamsCollection) object).getItems().get(0).getName());
		assertEquals(ObjectType.Note, ((ActivityStreamsCollection) object).getItems().get(0).getType());
		assertEquals("Another Simple Note", ((ActivityStreamsCollection) object).getItems().get(1).getName());
		assertEquals(ObjectType.Note, ((ActivityStreamsCollection) object).getItems().get(1).getType());
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
		assertEquals(2, ((ActivityStreamsCollection) object).getTotalItems());
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
