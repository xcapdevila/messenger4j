package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.BaseEvent;
import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.common.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.*;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.getPropertyAsJsonObject;
import static com.github.messenger4j.internal.gson.GsonUtil.getPropertyAsString;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
interface BaseEventFactory<E extends BaseEvent> {

	boolean isResponsible(JsonObject messagingEvent);

	E createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType);

	default PriorMessage getPriorMessageFromJsonObject(JsonObject jsonObject) {
		final String source = getPropertyAsString(jsonObject, PROP_SOURCE).orElseThrow(IllegalArgumentException::new);
		final String identifier = getPropertyAsString(jsonObject, PROP_IDENTIFIER).orElseThrow(IllegalArgumentException::new);
		return new PriorMessage(source, identifier);
	}

	default Referral createReferralFromJson(JsonObject jsonObject) {
		final String source = getPropertyAsString(jsonObject, PROP_SOURCE).orElseThrow(IllegalArgumentException::new);
		final String type = getPropertyAsString(jsonObject, PROP_TYPE).orElseThrow(IllegalArgumentException::new);
		final Optional<String> refPayload = getPropertyAsString(jsonObject, PROP_REF);
		final Optional<String> adId = getPropertyAsString(jsonObject, PROP_AD_ID);

		return new Referral(source, type, refPayload, adId);
	}

	default PassThreadControl getPassThreadControlFromJsonObject(JsonObject jsonObject) {
		final String newOwnerAppId = getPropertyAsString(jsonObject, PROP_NEW_OWNER_APP_ID).orElseThrow(IllegalArgumentException::new);
		final String metadata = getPropertyAsString(jsonObject, PROP_METADATA).orElseThrow(IllegalArgumentException::new);
		return new PassThreadControl(newOwnerAppId, metadata);
	}

	default TakeThreadControl getTakeThreadControlFromJsonObject(JsonObject jsonObject) {
		final String previousOwnerAppId = getPropertyAsString(jsonObject, PROP_PREVIOUS_OWNER_APP_ID).orElseThrow(IllegalArgumentException::new);
		final String metadata = getPropertyAsString(jsonObject, PROP_METADATA).orElseThrow(IllegalArgumentException::new);
		return new TakeThreadControl(previousOwnerAppId, metadata);
	}

	default RequestThreadControl getRequestThreadControlFromJsonObject(JsonObject jsonObject) {
		final String requestedOwnerAppId = getPropertyAsString(jsonObject, PROP_REQUESTED_OWNER_APP_ID).orElseThrow(IllegalArgumentException::new);
		final String metadata = getPropertyAsString(jsonObject, PROP_METADATA).orElseThrow(IllegalArgumentException::new);
		return new RequestThreadControl(requestedOwnerAppId, metadata);
	}

}
