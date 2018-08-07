package com.github.messenger4j.test.integration;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.*;
import com.github.messenger4j.webhook.event.attachment.Attachment;
import com.github.messenger4j.webhook.event.attachment.RichMediaAttachment;
import com.github.messenger4j.webhook.event.common.PriorMessage;
import com.github.messenger4j.webhook.event.nlp.NLPEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;

import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Max Grabenhorst
 * @since 1.1.0
 */
public class WebhookTest {

    @SuppressWarnings("unchecked")
    private final Consumer<Event> mockEventHandler = (Consumer<Event>) mock(Consumer.class);
    private final Messenger messenger = Messenger.create("test", "60efff025951cddde78c8d03de52cc90", "CUSTOM_VERIFY_TOKEN");

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfObjectTypeIsNotPage() throws Exception {
        //given
        final String payload = "{\n" +
                "    \"object\": \"testValue\",\n" +
                "    \"entry\": [{\n" +
                "        \"id\": \"PAGE_ID\",\n" +
                "        \"time\": 1458692752478,\n" +
                "        \"messaging\": [{\n" +
                "            \"sender\": {\n" +
                "                \"id\": \"USER_ID\"\n" +
                "            },\n" +
                "            \"recipient\": {\n" +
                "                \"id\": \"PAGE_ID\"\n" +
                "            },\n" +
                "            \"timestamp\": 1458692752478,\n" +
                "            \"message\": {\n" +
                "                \"mid\": \"mid.1457764197618:41d102a3e1ae206a38\",\n" +
                "                \"text\": \"hello, world!\",\n" +
                "                \"quick_reply\": {\n" +
                "                    \"payload\": \"DEVELOPER_DEFINED_PAYLOAD\"\n" +
                "                }\n" +
                "            }\n" +
                "        }]\n" +
                "    }]\n" +
                "}";

        //when
        messenger.onReceiveEvents(payload, empty(), mockEventHandler);

        //then - throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoPayloadProvided() throws Exception {
        //given
        final String payload = null;

        //when
        messenger.onReceiveEvents(payload, empty(), mockEventHandler);

        //then - throw exception
    }

    @Test
    public void shouldVerifyTheGivenSignature() throws Exception {
        //given
        final String payload = "{\"object\":\"page\",\"entry\":[{\"id\":\"1717527131834678\",\"time\":1475942721780," +
                "\"messaging\":[{\"sender\":{\"id\":\"1256217357730577\"},\"recipient\":{\"id\":\"1717527131834678\"}," +
                "\"timestamp\":1475942721741,\"message\":{\"mid\":\"mid.1475942721728:3b9e3646712f9bed52\"," +
                "\"seq\":123,\"text\":\"34wrr3wr\"}}]}]}";
        final String signature = "sha1=3daa41999293ff66c3eb313e04bcf77861bb0276";

        //when
        messenger.onReceiveEvents(payload, of(signature), mockEventHandler);

        //then
        final ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(mockEventHandler).accept(eventCaptor.capture());
        final Event event = eventCaptor.getValue();

        assertThat(event.asTextMessageEvent().text(), is(equalTo("34wrr3wr")));
    }

    @Test(expected = MessengerVerificationException.class)
    public void shouldThrowExceptionIfSignatureIsInvalid() throws Exception {
        //given
        final String payload = "{\"object\":\"page\",\"entry\":[{\"id\":\"1717527131834678\",\"time\":1475942721780," +
                "\"messaging\":[{\"sender\":{\"id\":\"1256217357730577\"},\"recipient\":{\"id\":\"1717527131834678\"}," +
                "\"timestamp\":1475942721741,\"message\":{\"mid\":\"mid.1475942721728:3b9e3646712f9bed52\"," +
                "\"seq\":123,\"text\":\"CHANGED_TEXT_SO_SIGNATURE_IS_INVALID\"}}]}]}";
        final String signature = "sha1=3daa41999293ff66c3eb313e04bcf77861bb0276";

        //when
        messenger.onReceiveEvents(payload, of(signature), mockEventHandler);

        //then - throw exception
    }

    @Test
    public void shouldVerifyTheWebhook() throws Exception {
        final String mode = "subscribe";
        final String verifyToken = "CUSTOM_VERIFY_TOKEN";

        // tag::webhook-Verify[]
        messenger.verifyWebhook(mode, verifyToken);
        // end::webhook-Verify[]

        //no exception is thrown
        assertThat(true, is(true));
    }

    @Test(expected = MessengerVerificationException.class)
    public void shouldThrowExceptionIfVerifyModeIsInvalid() throws Exception {
        //given
        final String mode = "INVALID_MODE";
        final String verifyToken = "CUSTOM_VERIFY_TOKEN";

        //when
        messenger.verifyWebhook(mode, verifyToken);

        //then - throw exception
    }

    @Test(expected = MessengerVerificationException.class)
    public void shouldThrowExceptionIfVerifyTokenIsInvalid() throws Exception {
        //given
        final String mode = "subscribe";
        final String verifyToken = "INVALID_VERIFY_TOKEN";

        //when
        messenger.verifyWebhook(mode, verifyToken);

        //then - throw exception
    }
}
