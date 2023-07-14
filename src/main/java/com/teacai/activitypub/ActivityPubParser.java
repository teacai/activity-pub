package com.teacai.activitypub;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacai.activitypub.model.Activity;
import com.teacai.activitypub.model.ActivityStreamCollection;
import com.teacai.activitypub.model.ActivityStreamsFields;
import com.teacai.activitypub.model.ActivityStreamsObject;
import com.teacai.activitypub.model.BaseObject;
import com.teacai.activitypub.model.Link;
import com.teacai.activitypub.model.ObjectType;
import com.teacai.activitypub.model.OrderedCollection;
import com.teacai.activitypub.model.Tombstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    protected static ActivityStreamsObject createActivityStreamObject(Map<String, Object> map) throws JsonParseException {
        ObjectType type = Optional.ofNullable(map.get("type"))
                .map(Object::toString)
                .map(ObjectType::parse)
                .orElseThrow(() -> new JsonParseException("Object type missing."));

        switch (type) {
            case Tombstone:
                return createTombstone(map);
            case Link:
                return createLink(map);
            case Activity:
            case Invite:
            case Offer:
                return createActivity(map);
            case OrderedCollection:
                return createOrderedCollection(map);
            default:
                return addObjectFields(new BaseObject(), map);
        }
    }

    protected static List<ActivityStreamsObject> createActivityStreamList(Object object) throws JsonParseException {
        if (object instanceof Map) {
            return Collections.singletonList(createActivityStreamObject((Map<String, Object>) object));
        } else if (object instanceof Collection) {
            List<ActivityStreamsObject> parsedItems = new ArrayList<>();
            ((Collection<?>) object).forEach(item -> {
                if (item instanceof String) {
                    parsedItems.add(createActivityStreamObject((String) item));
                } else if (item instanceof Map) {
                    try {
                        parsedItems.add(createActivityStreamObject((Map<String, Object>) item));
                    } catch (JsonParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return parsedItems;
        }
        return Collections.emptyList();
    }

    private static ActivityStreamsObject createTombstone(Map<String, Object> map) {
        Tombstone object = new Tombstone();
        object.setFormerType(ObjectType.parse(strValue(map.get(ActivityStreamsFields.FORMER_TYPE))));
        object.setDeleted(instantValue(map.get(ActivityStreamsFields.DELETED)));

        return addObjectFields(object, map);
    }

    protected static <T extends ActivityStreamsObject> T addObjectFields(T object, Map<String, Object> map) {

        object.setContext(strValue(map.get(ActivityStreamsFields.CONTEXT), ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS));
        object.setId(strValue(map.get(ActivityStreamsFields.ID)));
        object.setName(strValue(map.get(ActivityStreamsFields.NAME)));
        object.setType(ObjectType.parse(strValue(map.get(ActivityStreamsFields.TYPE))));
        return object;
    }

    protected static ActivityStreamsObject createActivityStreamObject(String url) {
        Link link = new Link();
        link.setContext(ActivityStreamsFields.CONTEXT_ACTIVITY_STREAMS);
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
        return addObjectFields(object, map);
    }

    protected static Activity createActivity(Map<String, Object> map) throws JsonParseException {
        Activity object = new Activity();

        object.setSummary(strValue(map.get(ActivityStreamsFields.SUMMARY)));

        object.setActors(createActivityStreamList(map.get(ActivityStreamsFields.ACTOR)));

        return addObjectFields(object, map);
    }

    protected static OrderedCollection createOrderedCollection(Map<String, Object> map) throws JsonParseException {
        OrderedCollection collection = addCollectionFields(new OrderedCollection(), map);

        collection.setOrderedItems(createActivityStreamList(map.get(ActivityStreamsFields.ORDERED_ITEMS)));
        return collection;
    }

    protected static <T extends ActivityStreamCollection> T addCollectionFields(
            T activityStreamCollection, Map<String, Object> map) {

        activityStreamCollection.setTotalItems(intValue(map.get(ActivityStreamsFields.TOTAL_ITEMS)));
        activityStreamCollection.setCurrent(strValue(map.get(ActivityStreamsFields.CURRENT)));
        activityStreamCollection.setFirst(strValue(map.get(ActivityStreamsFields.FIRST)));
        activityStreamCollection.setLast(strValue(map.get(ActivityStreamsFields.LAST)));

        List<ActivityStreamsObject> items = new ArrayList<>();
        activityStreamCollection.setItems(items);

        return addObjectFields(activityStreamCollection, map);
    }
}
