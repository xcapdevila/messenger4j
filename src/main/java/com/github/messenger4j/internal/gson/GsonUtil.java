package com.github.messenger4j.internal.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
public final class GsonUtil {

	private GsonUtil() {
	}

	private static Optional<JsonElement> getProperty(JsonObject jsonObject, Constants... propertyPath) {
		JsonObject internalValue = jsonObject;
		for (int i = 0; i <= propertyPath.length - 2; i++) {
			final JsonElement property = internalValue.get(propertyPath[i].value());
			if (property == null || !property.isJsonObject()) {
				return Optional.empty();
			}
			internalValue = property.getAsJsonObject();
		}
		final JsonElement property = internalValue.get(propertyPath[propertyPath.length - 1].value());
		return property == null || property.isJsonNull() ? Optional.empty() : Optional.of(property);
	}

	public static boolean hasProperty(JsonObject jsonObject, Constants... propertyPath) {
		return getProperty(jsonObject, propertyPath).isPresent();
	}

	public static Optional<String> getPropertyAsString(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsString);
	}

	public static Optional<Boolean> getPropertyAsBoolean(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsBoolean);
	}

	public static Optional<Integer> getPropertyAsInt(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsInt);
	}

	public static Optional<Long> getPropertyAsLong(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsLong);
	}

	public static Optional<Instant> getPropertyAsInstant(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<Long> longValue = getPropertyAsLong(jsonObject, propertyPath);
		return longValue.map(Instant::ofEpochMilli);
	}

	public static Optional<Double> getPropertyAsDouble(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsDouble);
	}

	public static Optional<Float> getPropertyAsFloat(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsFloat);
	}

	public static Optional<JsonArray> getPropertyAsJsonArray(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsJsonArray);
	}

	public static Optional<JsonObject> getPropertyAsJsonObject(JsonObject jsonObject, Constants... propertyPath) {
		final Optional<JsonElement> jsonElement = getProperty(jsonObject, propertyPath);
		return jsonElement.map(JsonElement::getAsJsonObject);
	}

	public static boolean hasAnyMember(JsonObject jsonObject, String... members) {
		return Arrays.stream(members).anyMatch(member -> jsonObject.has(member));
	}

	/**
	 * @author Max Grabenhorst
	 * @since 1.0.0
	 */
	public enum Constants {

		PROP_OBJECT("object"),
		PROP_ENTRY("entry"),
		PROP_MESSAGING("messaging"),
		PROP_SENDER("sender"),
		PROP_RECIPIENT("recipient"),
		PROP_ID("id"),
		PROP_TIMESTAMP("timestamp"),
		PROP_OPTIN("optin"),
		PROP_MESSAGE("message"),
		PROP_MID("mid"),
		PROP_IS_ECHO("is_echo"),
		PROP_QUICK_REPLY("quick_reply"),
		PROP_TEXT("text"),
		PROP_ATTACHMENTS("attachments"),
		PROP_PAYLOAD("payload"),
		PROP_TYPE("type"),
		PROP_URL("url"),
		PROP_COORDINATES("coordinates"),
		PROP_LAT("lat"),
		PROP_LONG("long"),
		PROP_REF("ref"),
		PROP_USER_REF("user_ref"),
		PROP_APP_ID("app_id"),
		PROP_METADATA("metadata"),
		PROP_POSTBACK("postback"),
		PROP_ACCOUNT_LINKING("account_linking"),
		PROP_STATUS("status"),
		PROP_AUTHORIZATION_CODE("authorization_code"),
		PROP_READ("read"),
		PROP_WATERMARK("watermark"),
		PROP_DELIVERY("delivery"),
		PROP_MIDS("mids"),
		PROP_RECIPIENT_ID("recipient_id"),
		PROP_MESSAGE_ID("message_id"),
		PROP_ATTACHMENT_ID("attachment_id"),
		PROP_ERROR("error"),
		PROP_CODE("code"),
		PROP_FB_TRACE_ID("fbtrace_id"),
		PROP_RESULT("result"),
		PROP_FIRST_NAME("first_name"),
		PROP_LAST_NAME("last_name"),
		PROP_PROFILE_PIC("profile_pic"),
		PROP_LOCALE("locale"),
		PROP_TIMEZONE("timezone"),
		PROP_GENDER("gender"),
		PROP_TITLE("title"),
		PROP_REFERRAL("referral"),
		PROP_SOURCE("source"),
		PROP_AD_ID("ad_id"),
		PROP_IS_PAYMENT_ENABLED("is_payment_enabled"),
		PROP_LAST_AD_REFERRAL("last_ad_referral"),
		PROP_NLP("nlp"),
		PROP_ENTITIES("entities"),
		PROP_PRIOR_MESSAGE("prior_message"),
		PROP_IDENTIFIER("identifier"),
		PROP_GAME_PLAY("game_play"),
		PROP_GAME_ID("game_id"),
		PROP_PLAYER_ID("player_id"),
		PROP_CONTEXT_TYPE("context_type"),
		PROP_CONTEXT_ID("context_id"),
		PROP_SCORE("score"),
		PROP_PASS_THREAD_CONTROL("pass_thread_control"),
		PROP_NEW_OWNER_APP_ID("new_owner_app_id"),
		PROP_TAKE_THREAD_CONTROL("take_thread_control"),
		PROP_PREVIOUS_OWNER_APP_ID("previous_owner_app_id"),
		PROP_REQUEST_THREAD_CONTROL("request_thread_control"),
		PROP_REQUESTED_OWNER_APP_ID("requested_owner_app_id"),
		PROP_APP_ROLES("app_roles"),
		PROP_STANDBY("standby"),
		PROP_SUCCESS("success"),
		PROP_DATA("data"),
		PROP_NAME("name"),
		PROP_THREAD_OWNER("name");

		private final String value;

		Constants(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}
	}
}
