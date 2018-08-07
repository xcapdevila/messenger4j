package com.github.messenger4j.test.integration;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.handover.HandoverPassThreadControlPayload;
import com.github.messenger4j.handover.HandoverRequestPayload;
import com.github.messenger4j.spi.MessengerHttpClient;
import com.github.messenger4j.spi.MessengerHttpClient.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static com.github.messenger4j.spi.MessengerHttpClient.HttpMethod.POST;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@Slf4j
public class SendHandoverTest {

	private static final String PAGE_ACCESS_TOKEN = "PAGE_ACCESS_TOKEN";

	private final MessengerHttpClient mockHttpClient = mock(MessengerHttpClient.class);
	private final HttpResponse handoverFakeResponse = new HttpResponse(200, "{\n" + "  \"success\":true\n" + "}");

	private final Messenger messenger = Messenger.create(PAGE_ACCESS_TOKEN, "test", "test", empty(), empty(), of(mockHttpClient));

	@Before
	public void beforeEach() throws IOException {
		when(mockHttpClient.execute(eq(POST), anyString(), anyString())).thenReturn(handoverFakeResponse);
	}

	@Test
	public void shouldSendPassThreadControl() throws Exception {
		// tag::passThreadControl[]
		final String recipientId = "<PSID>";
		final String targetAppId = "123456789";
		final String metadata = "String to pass to secondary receiver app";

		final HandoverPassThreadControlPayload payload = HandoverPassThreadControlPayload.create(recipientId, targetAppId, of(metadata));

		messenger.passThreadControl(payload);
		// end::passThreadControl[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\n" + "  \"recipient\":{\"id\":\"<PSID>\"},\n" + "  \"target_app_id\":\"123456789\",\n"
				+ "  \"metadata\":\"String to pass to secondary receiver app\" \n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTakeThreadControl() throws Exception {
		// tag::takeThreadControl[]
		final String recipientId = "<PSID>";
		final String metadata = "String to pass to secondary receiver app";

		final HandoverRequestPayload payload = HandoverRequestPayload.create(recipientId, of(metadata));

		messenger.takeThreadControl(payload);
		// end::takeThreadControl[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\"id\":\"<PSID>\"},\n" + "  \"metadata\":\"String to pass to secondary receiver app\" \n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendRequestThreadControl() throws Exception {
		// tag::takeThreadControl[]
		final String recipientId = "<PSID>";
		final String metadata = "Additional content that the caller wants to set";

		final HandoverRequestPayload payload = HandoverRequestPayload.create(recipientId, of(metadata));

		messenger.requestThreadControl(payload);
		// end::takeThreadControl[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\"id\":\"<PSID>\"},\n" + "  \"metadata\":\"Additional content that the caller wants to set\" \n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

}
