package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.github.messenger4j.webhook.event.common.PriorMessage;
import com.github.messenger4j.webhook.event.nlp.NLPEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.*;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
final class TextMessageEventFactory implements BaseEventFactory<TextMessageEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_MESSAGE, PROP_TEXT) && !hasProperty(messagingEvent, PROP_MESSAGE, PROP_QUICK_REPLY) && !hasProperty(
				messagingEvent, PROP_MESSAGE, PROP_IS_ECHO);
	}

	@Override
	public TextMessageEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final String messageId = getPropertyAsString(messagingEvent, PROP_MESSAGE, PROP_MID).orElseThrow(IllegalArgumentException::new);
		final String text = getPropertyAsString(messagingEvent, PROP_MESSAGE, PROP_TEXT).orElseThrow(IllegalArgumentException::new);
		final Optional<Map<String, Set<NLPEntity>>> nlpEntities = getPropertyAsJsonObject(messagingEvent, PROP_MESSAGE, PROP_NLP, PROP_ENTITIES)
				.map(this::getNlpEntitiesFromJsonObject);
		final Optional<PriorMessage> priorMessage = getPropertyAsJsonObject(messagingEvent, PROP_PRIOR_MESSAGE).map(this::getPriorMessageFromJsonObject);

		return new TextMessageEvent(senderId, recipientId, timestamp, baseEventType, messageId, text, nlpEntities, priorMessage);
	}

	private Map<String, Set<NLPEntity>> getNlpEntitiesFromJsonObject(JsonObject jsonObject) {
		final Map<String, Set<NLPEntity>> nlpEntities = new HashMap<>();
		for (String key : jsonObject.keySet()) {
			final JsonArray valuesJsonArray = jsonObject.getAsJsonArray(key);
			final Set<NLPEntity> values = new HashSet<>(valuesJsonArray.size());
			for (JsonElement jsonElement : valuesJsonArray) {
				values.add(new NLPEntity(jsonElement.toString()));
			}
			nlpEntities.put(key, Collections.unmodifiableSet(values));
		}
		return nlpEntities;
	}
}
