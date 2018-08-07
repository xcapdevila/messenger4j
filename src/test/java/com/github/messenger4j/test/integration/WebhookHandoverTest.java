package com.github.messenger4j.test.integration;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.function.Consumer;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Xavier Capdevila
 * @see https://developers.facebook.com/docs/messenger-platform/reference/webhook-events/messaging_handovers/
 * @since 1.1.0
 */
@SuppressWarnings("JavadocReference")
public class WebhookHandoverTest {

	@SuppressWarnings("unchecked")
	private final Consumer<Event> mockEventHandler = (Consumer<Event>) mock(Consumer.class);
	private final Messenger messenger = Messenger.create("test", "60efff025951cddde78c8d03de52cc90", "CUSTOM_VERIFY_TOKEN");

	@Test
	public void shouldHandlePassThreadControlEvent() throws Exception {
		//FIXME finish & cleanUp
		//given
		final String payload = "{\"object\":\"page\","
				+ "\"entry\":["
				+ "{\"id\":\"PAGE_ID\","
				+ "\"time\":1458692752478,"
				+ "\"messaging\":["
				+ "{\"recipient\":{\"id\":\"<PAGE_ID>\"},"
				+ "\"timestamp\":1458692752478,"
				+ "\"sender\":{\"id\":\"<PSID>\"},"
				+ "\"pass_thread_control\":{\"new_owner_app_id\":123456789,\"metadata\":\"Additional content that the caller wants to set\"}}]}]}";

		//when
		messenger.onReceiveEvents(payload, empty(), mockEventHandler);

		//then
		final ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
		verify(mockEventHandler).accept(eventCaptor.capture());
		final Event event = eventCaptor.getValue();

		final PassThreadControlEvent passThreadControlEvent = event.asPassThreadControlEvent();
		assertThat(passThreadControlEvent.baseEventType(), equalTo(BaseEventType.HANDOVER));
		assertThat(passThreadControlEvent.senderId(), equalTo("<PSID>"));
		assertThat(passThreadControlEvent.recipientId(), equalTo("<PAGE_ID>"));
		assertThat(passThreadControlEvent.timestamp(), equalTo(Instant.ofEpochMilli(1458692752478L)));
		assertThat(passThreadControlEvent.getPassThreadControl().isPresent(), is(true));
		assertThat(passThreadControlEvent.getPassThreadControl().get().getNewOwnerAppId(), equalTo("123456789"));
		assertThat(passThreadControlEvent.getPassThreadControl().get().getMetadata(), equalTo("Additional content that the caller wants to set"));

	}

	@Test
	public void shouldHandleTakeThreadControlEvent() throws Exception {
		//given
		final String payload = "{\"object\":\"page\","
				+ "\"entry\":["
				+ "{\"id\":\"PAGE_ID\","
				+ "\"time\":1458692752478,"
				+ "\"messaging\":["
				+ "{\"recipient\":{\"id\":\"<PSID>\"},"
				+ "\"timestamp\":1458692752478,"
				+ "\"sender\":{\"id\":\"<USER_ID>\"},"
				+ "\"take_thread_control\":{\"previous_owner_app_id\":123456789,\"metadata\":\"Additional content that the caller wants to set\"}}]}]}";

		//when
		messenger.onReceiveEvents(payload, empty(), mockEventHandler);

		//then
		final ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
		verify(mockEventHandler).accept(eventCaptor.capture());
		final Event event = eventCaptor.getValue();

		final TakeThreadControlEvent takeThreadControlEvent = event.asTakeThreadControlEvent();
		assertThat(takeThreadControlEvent.baseEventType(), equalTo(BaseEventType.HANDOVER));
		assertThat(takeThreadControlEvent.senderId(), equalTo("<USER_ID>"));
		assertThat(takeThreadControlEvent.recipientId(), equalTo("<PSID>"));
		assertThat(takeThreadControlEvent.timestamp(), equalTo(Instant.ofEpochMilli(1458692752478L)));
		assertThat(takeThreadControlEvent.getTakeThreadControl().isPresent(), is(true));
		assertThat(takeThreadControlEvent.getTakeThreadControl().get().getPreviousOwnerAppId(), equalTo("123456789"));
		assertThat(takeThreadControlEvent.getTakeThreadControl().get().getMetadata(), equalTo("Additional content that the caller wants to set"));

	}

	@Test
	public void shouldHandleRequestThreadControlEvent() throws Exception {
		//given
		final String payload = "{\"object\":\"page\","
				+ "\"entry\":["
				+ "{\"id\":\"PAGE_ID\","
				+ "\"time\":1458692752478,"
				+ "\"messaging\":["
				+ "{\"recipient\":{\"id\":\"<PSID>\"},"
				+ "\"timestamp\":1458692752478,"
				+ "\"sender\":{\"id\":\"<USER_ID>\"},"
				+ "\"request_thread_control\":{\"requested_owner_app_id\":123456789,\"metadata\":\"Additional content that the caller wants to set\"}}]}]}";

		//when
		messenger.onReceiveEvents(payload, empty(), mockEventHandler);

		//then
		final ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
		verify(mockEventHandler).accept(eventCaptor.capture());
		final Event event = eventCaptor.getValue();

		final RequestThreadControlEvent requestThreadControlEvent = event.asRequestThreadControlEvent();
		assertThat(requestThreadControlEvent.baseEventType(), equalTo(BaseEventType.HANDOVER));
		assertThat(requestThreadControlEvent.senderId(), equalTo("<USER_ID>"));
		assertThat(requestThreadControlEvent.recipientId(), equalTo("<PSID>"));
		assertThat(requestThreadControlEvent.timestamp(), equalTo(Instant.ofEpochMilli(1458692752478L)));
		assertThat(requestThreadControlEvent.getRequestThreadControl().isPresent(), is(true));
		assertThat(requestThreadControlEvent.getRequestThreadControl().get().getRequestedOwnerAppId(), equalTo("123456789"));
		assertThat(requestThreadControlEvent.getRequestThreadControl().get().getMetadata(), equalTo("Additional content that the caller wants to set"));

	}

	@Test
	public void shouldHandleAppRolesEvent() throws Exception {
		//given
		final String payload = "{\"object\":\"page\","
				+ "\"entry\":["
				+ "{\"id\":\"PAGE_ID\","
				+ "\"time\":1458692752478,"
				+ "\"messaging\":["
				+ "{\"recipient\":{\"id\":\"<PSID>\"},"
				+ "\"timestamp\":1458692752478,"
				+ "\"app_roles\":{\"123456789\":[\"primary_receiver\"]}}]}]}";

		//when
		messenger.onReceiveEvents(payload, empty(), mockEventHandler);

		//then
		final ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
		verify(mockEventHandler).accept(eventCaptor.capture());
		final Event event = eventCaptor.getValue();

		final AppRolesEvent asAppRolesEvent = event.asAppRolesEvent();
		assertThat(asAppRolesEvent.baseEventType(), equalTo(BaseEventType.HANDOVER));
		assertThat(asAppRolesEvent.recipientId(), equalTo("<PSID>"));
		assertThat(asAppRolesEvent.timestamp(), equalTo(Instant.ofEpochMilli(1458692752478L)));
		assertThat(asAppRolesEvent.getAppRoles().isPresent(), is(true));
		assertFalse(asAppRolesEvent.getAppRoles().get().getIdRoles().get("123456789").isEmpty());
		assertThat(asAppRolesEvent.getAppRoles().get().getIdRoles().get("123456789").get(0), equalTo("primary_receiver"));

	}

}
