package com.teacai.activitypub;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacai.activitypub.model.Activity;
import com.teacai.activitypub.model.ActivityStreamsFields;
import com.teacai.activitypub.model.ActivityStreamsObject;
import com.teacai.activitypub.model.BaseCollection;
import com.teacai.activitypub.model.BaseObject;
import com.teacai.activitypub.model.CollectionPage;
import com.teacai.activitypub.model.Link;
import com.teacai.activitypub.model.ObjectType;
import com.teacai.activitypub.model.OrderedCollection;
import com.teacai.activitypub.model.OrderedCollectionPage;
import com.teacai.activitypub.model.Place;
import com.teacai.activitypub.model.Question;
import com.teacai.activitypub.model.Tombstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.teacai.activitypub.util.ParseUtils.doubleValue;
import static com.teacai.activitypub.util.ParseUtils.instantValue;
import static com.teacai.activitypub.util.ParseUtils.intValue;
import static com.teacai.activitypub.util.ParseUtils.strValue;

public class ActivityPubParser {

    private final ObjectMapper objectMapper;

    public ActivityPubParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ActivityStreamsObject parse(String json) throws JsonProcessingException {

        Map<String, Object> map = objectMapper.readValue(json,
                new TypeReference<Map<String, Object>>() {
                });
        return createActivityStreamObject(map);
    }

    protected static ActivityStreamsObject createActivityStreamObject(Object object) throws JsonParseException {
        if (object instanceof String) {
            return createActivityStreamObject((String) object);
        } else if (object instanceof Map) {
            return createActivityStreamObject((Map<String, Object>) object);
        } else {
            return null;
        }
    }

    protected static ActivityStreamsObject createActivityStreamObject(Map<String, Object> map) throws JsonParseException {
        ObjectType type = Optional.ofNullable(map.get("type"))
                .map(Object::toString)
                .map(ObjectType::parse)
                .orElse(ObjectType.Custom);

        switch (type) {
            case Place:
                return createPlace(map);
            case Tombstone:
                return createTombstone(map);
            case Link:
            case Mention:
                return createLink(map);
            case Accept:
            case Activity:
            case Add:
            case Announce:
            case Arrive:
            case Block:
            case Create:
            case Delete:
            case Dislike:
            case Flag:
            case Follow:
            case Ignore:
            case Invite:
            case Join:
            case Leave:
            case Like:
            case Listen:
            case Move:
            case Offer:
            case Reject:
            case Read:
            case Remove:
            case TentativeAccept:
            case TentativeReject:
            case Travel:
            case Undo:
            case Update:
            case View:
                return createActivity(map);
            case Question:
                return createQuestionActivity(map);
            case Collection:
                return createCollection(map);
            case OrderedCollection:
                return createOrderedCollection(map);
            case CollectionPage:
                return createCollectionPage(map);
            case OrderedCollectionPage:
                return createOrderedCollectionPage(map);
            default:
                return addBaseObjectFields(new BaseObject(), map);
        }
    }

    protected static List<ActivityStreamsObject> createActivityStreamList(Object object) throws JsonParseException {
        if (object instanceof Map || object instanceof String) {
            return Collections.singletonList(createActivityStreamObject(object));
        } else if (object instanceof Collection) {
            List<ActivityStreamsObject> parsedItems = new ArrayList<>();
            ((Collection<?>) object).forEach(item -> {
                if (item instanceof String) {
                    parsedItems.add(createActivityStreamObject((String) item));
                } else if (item instanceof Map) {
                    try {
                        parsedItems.add(createActivityStreamObject(item));
                    } catch (JsonParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return parsedItems;
        }
        return Collections.emptyList();
    }

    private static ActivityStreamsObject createTombstone(Map<String, Object> map) throws JsonParseException {
        Tombstone object = new Tombstone();
        object.setFormerType(ObjectType.parse(strValue(map.get(ActivityStreamsFields.FORMER_TYPE))));
        object.setDeleted(instantValue(map.get(ActivityStreamsFields.DELETED)));

        return addBaseObjectFields(object, map);
    }

    private static ActivityStreamsObject createPlace(Map<String, Object> map) throws JsonParseException {
        Place object = new Place();
        object.setUnits(strValue(map.get(ActivityStreamsFields.UNITS)));
        object.setAccuracy(doubleValue(map.get(ActivityStreamsFields.ACCURACY)));
        object.setAltitude(doubleValue(map.get(ActivityStreamsFields.ALTITUDE)));
        object.setLatitude(doubleValue(map.get(ActivityStreamsFields.LATITUDE)));
        object.setLongitude(doubleValue(map.get(ActivityStreamsFields.LONGITUDE)));
        object.setRadius(doubleValue(map.get(ActivityStreamsFields.RADIUS)));
        return addBaseObjectFields(object, map);
    }

    protected static ActivityStreamsObject createActivityStreamObject(String url) {
        Link link = new Link();
        link.setAtContext(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS);
        link.setHref(url);
        link.setType(ObjectType.Link);
        return link;
    }

    protected static Link createLink(Map<String, Object> map) {
        Link object = new Link();
        object.setHref(strValue(map.get(ActivityStreamsFields.HREF)));
        object.setHrefLang(strValue(map.get(ActivityStreamsFields.HREF_LANG)));
        object.setRel(strValue(map.get(ActivityStreamsFields.REL)));
        object.setPreview(strValue(map.get(ActivityStreamsFields.PREVIEW)));
        object.setHeight(intValue(map.get(ActivityStreamsFields.HEIGHT)));
        object.setWidth(intValue(map.get(ActivityStreamsFields.WIDTH)));
        return addActivityStreamsFields(object, map);
    }


    protected static ActivityStreamsObject createQuestionActivity(Map<String, Object> map) throws JsonParseException {
        Question object = new Question();
        addActivityFields(map, object);

        object.setOneOf(createActivityStreamList(map.get(ActivityStreamsFields.ONE_OF)));
        object.setAnyOf(createActivityStreamList(map.get(ActivityStreamsFields.ANY_OF)));
        object.setClosed(instantValue(map.get(ActivityStreamsFields.CLOSED)));

        return addBaseObjectFields(object, map);
    }

    protected static Activity createActivity(Map<String, Object> map) throws JsonParseException {
        Activity object = new Activity();

        addActivityFields(map, object);

        return addBaseObjectFields(object, map);
    }

    protected static void addActivityFields(Map<String, Object> map, Activity object) throws JsonParseException {
        object.setSummary(strValue(map.get(ActivityStreamsFields.SUMMARY)));
        object.setObjects(createActivityStreamList(map.get(ActivityStreamsFields.OBJECT)));
        object.setTargets(createActivityStreamList(map.get(ActivityStreamsFields.TARGET)));
        object.setResult(createActivityStreamObject(map.get(ActivityStreamsFields.RESULT)));
        object.setOrigin(createActivityStreamObject(map.get(ActivityStreamsFields.ORIGIN)));
        object.setInstrument(createActivityStreamObject(map.get(ActivityStreamsFields.INSTRUMENT)));
    }

    private static ActivityStreamsObject createCollectionPage(Map<String, Object> map) throws JsonParseException {
        CollectionPage collection = addCollectionFields(new CollectionPage(), map);
        addPageFields(map, collection);
        return collection;
    }

    private static ActivityStreamsObject createOrderedCollectionPage(Map<String, Object> map) throws JsonParseException {
        OrderedCollectionPage collection = addCollectionFields(new OrderedCollectionPage(), map);
        addPageFields(map, collection);
        collection.setOrderedItems(createActivityStreamList(map.get(ActivityStreamsFields.ORDERED_ITEMS)));
        collection.setStartIndex(intValue(map.get(ActivityStreamsFields.START_INDEX)));
        return collection;
    }

    private static void addPageFields(Map<String, Object> map, CollectionPage collection) throws JsonParseException {
        collection.setPartOf(strValue(map.get(ActivityStreamsFields.PART_OF)));
        collection.setNext(createActivityStreamObject(map.get(ActivityStreamsFields.NEXT)));
        collection.setPrev(createActivityStreamObject(map.get(ActivityStreamsFields.PREV)));
        collection.setItems(createActivityStreamList(map.get(ActivityStreamsFields.ITEMS)));
    }

    protected static BaseCollection createCollection(Map<String, Object> map) throws JsonParseException {
        return addCollectionFields(new BaseCollection(), map);
    }

    protected static OrderedCollection createOrderedCollection(Map<String, Object> map) throws JsonParseException {
        OrderedCollection collection = addCollectionFields(new OrderedCollection(), map);

        collection.setOrderedItems(createActivityStreamList(map.get(ActivityStreamsFields.ORDERED_ITEMS)));
        return collection;
    }

    protected static <T extends BaseCollection> T addCollectionFields(
            T activityStreamCollection, Map<String, Object> map) throws JsonParseException {

        activityStreamCollection.setTotalItems(intValue(map.get(ActivityStreamsFields.TOTAL_ITEMS)));
        activityStreamCollection.setCurrent(strValue(map.get(ActivityStreamsFields.CURRENT)));
        activityStreamCollection.setFirst(strValue(map.get(ActivityStreamsFields.FIRST)));
        activityStreamCollection.setLast(strValue(map.get(ActivityStreamsFields.LAST)));

        activityStreamCollection.setItems(createActivityStreamList(map.get(ActivityStreamsFields.ITEMS)));

        return addBaseObjectFields(activityStreamCollection, map);
    }

    protected static <T extends BaseObject> T addBaseObjectFields(T object, Map<String, Object> map) throws JsonParseException {
        object.setSummary(strValue(map.get(ActivityStreamsFields.SUMMARY)));
        object.setContext(createActivityStreamObject(map.get(ActivityStreamsFields.CONTEXT)));
        object.setActors(createActivityStreamList(map.get(ActivityStreamsFields.ACTOR)));
        object.setUrl(createActivityStreamList(map.get(ActivityStreamsFields.URL)));
        object.setLocation(createActivityStreamObject(map.get(ActivityStreamsFields.LOCATION)));
        object.setContent(strValue(map.get(ActivityStreamsFields.CONTENT)));
        return addActivityStreamsFields(object, map);
    }

    protected static <T extends ActivityStreamsObject> T addActivityStreamsFields(T object, Map<String, Object> map) {
        object.setAtContext(strValue(map.get(ActivityStreamsFields.AT_CONTEXT), ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS));
        object.setId(strValue(map.get(ActivityStreamsFields.ID)));
        object.setName(strValue(map.get(ActivityStreamsFields.NAME)));
        object.setType(ObjectType.parse(strValue(map.get(ActivityStreamsFields.TYPE))));
        if (object.getType() == ObjectType.Custom) {
            object.setCustomType(strValue(map.get(ActivityStreamsFields.TYPE)));
        }
        return object;
    }
}
