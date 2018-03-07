package com.github.messenger4j.test.integration;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.WebviewHeightRatio;
import com.github.messenger4j.common.WebviewShareButtonState;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.send.*;
import com.github.messenger4j.send.message.RichMediaMessage;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.*;
import com.github.messenger4j.send.message.richmedia.ReusableRichMediaAsset;
import com.github.messenger4j.send.message.richmedia.UrlRichMediaAsset;
import com.github.messenger4j.send.message.template.*;
import com.github.messenger4j.send.message.template.ListTemplate.TopElementStyle;
import com.github.messenger4j.send.message.template.button.*;
import com.github.messenger4j.send.message.template.common.DefaultAction;
import com.github.messenger4j.send.message.template.common.Element;
import com.github.messenger4j.send.message.template.opengraph.OpenGraphObject;
import com.github.messenger4j.send.message.template.receipt.Address;
import com.github.messenger4j.send.message.template.receipt.Adjustment;
import com.github.messenger4j.send.message.template.receipt.Item;
import com.github.messenger4j.send.message.template.receipt.Summary;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.send.recipient.PhoneNumberRecipient;
import com.github.messenger4j.send.recipient.Recipient;
import com.github.messenger4j.send.recipient.UserRefRecipient;
import com.github.messenger4j.send.senderaction.SenderAction;
import com.github.messenger4j.spi.MessengerHttpClient;
import com.github.messenger4j.spi.MessengerHttpClient.HttpMethod;
import com.github.messenger4j.spi.MessengerHttpClient.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;

import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.messenger4j.send.message.richmedia.RichMediaAsset.Type.IMAGE;
import static com.github.messenger4j.spi.MessengerHttpClient.HttpMethod.POST;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
@Slf4j
public class SendTest {

	private static final String PAGE_ACCESS_TOKEN = "PAGE_ACCESS_TOKEN";

	private final MessengerHttpClient mockHttpClient = mock(MessengerHttpClient.class);
	private final HttpResponse fakeResponse = new HttpResponse(200,
			"{\n" + "  \"recipient_id\": \"USER_ID\",\n" + "  \"message_id\": \"mid.1473372944816:94f72b88c597657974\"\n" + "}");

	private final Messenger messenger = Messenger.create(PAGE_ACCESS_TOKEN, "test", "test", empty(), empty(), of(mockHttpClient));

	@Before
	public void beforeEach() throws Exception {
		when(mockHttpClient.execute(any(HttpMethod.class), anyString(), anyString())).thenReturn(fakeResponse);
	}

	@Test
	public void shouldSendSenderAction() throws Exception {
		// tag::send-SenderAction[]
		final String recipientId = "USER_ID";
		final SenderAction senderAction = SenderAction.MARK_SEEN;

		final SenderActionPayload payload = SenderActionPayload.create(recipientId, senderAction);

		messenger.send(payload);
		// end::send-SenderAction[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"sender_action\":\"mark_seen\"}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTextMessage() throws Exception {
		// tag::send-TextMessage[]
		final String recipientId = "USER_ID";
		final String text = "Hello Messenger Platform";

		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TextMessage.create(text));

		messenger.send(payload);
		// end::send-TextMessage[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\"recipient\":{\"id\":\"USER_ID\"}," + "\"messaging_type\":\"RESPONSE\"," + "\"message\":{\"text\":\"Hello Messenger Platform\"}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTextMessageWithPhoneNumberRecipientWithName() throws Exception {
		final Recipient recipient = PhoneNumberRecipient.create("+1 (555) 857-6309", "Jenny", "Doe");
		final String text = "Your package has arrived in Lobby 2.";

		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, TextMessage.create(text));

		messenger.send(payload);

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\"recipient\":{\"phone_number\":\"+1 (555) 857-6309\", " + "\"name\":{\"first_name\":\"Jenny\", \"last_name\":\"Doe\"}}, \n"
						+ "\"messaging_type\":\"RESPONSE\"," + "\"message\":{\"text\":\"Your package has arrived in Lobby 2.\"}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTextMessageWithPhoneNumberRecipientWithoutName() throws Exception {
		final Recipient recipient = PhoneNumberRecipient.create("+1 (555) 857-6309");
		final String text = "Your package has arrived in Lobby 2.";

		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, TextMessage.create(text));

		messenger.send(payload);

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"phone_number\":\"+1 (555) 857-6309\"}, \n" + "\"messaging_type\":\"RESPONSE\","
				+ "\"message\":{\"text\":\"Your package has arrived in Lobby 2.\"}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTextMessageWithUserRefRecipient() throws Exception {
		final Recipient recipient = UserRefRecipient.create("<UNIQUE_REF_PARAM>");
		final String text = "hello, world!";

		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, TextMessage.create(text));

		messenger.send(payload);

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\": {\n" + "    \"user_ref\":\"<UNIQUE_REF_PARAM>\"\n" + "  }, \n" + "  \"messaging_type\":\"RESPONSE\","
						+ "  \"message\": {\n" + "    \"text\":\"hello, world!\"\n" + "  }\n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTextMessageWithNotificationTypeAndMessageTag() throws Exception {
		// tag::send-TextMessageNotificationTypeMessageTag[]
		final Recipient recipient = IdRecipient.create("USER_ID");
		final TextMessage message = TextMessage.create("Hello Messenger Platform");
		final NotificationType notificationType = NotificationType.SILENT_PUSH;
		final MessageTag messageTag = MessageTag.APPLICATION_UPDATE;

		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, message, of(notificationType), of(messageTag));

		messenger.send(payload);
		// end::send-TextMessageNotificationTypeMessageTag[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"messaging_type\":\"RESPONSE\"," + "\"notification_type\":\"SILENT_PUSH\","
				+ "\"tag\":\"APPLICATION_UPDATE\"," + "\"message\":{\"text\":\"Hello Messenger Platform\"}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTextMessageWithQuickReplies() throws Exception {
		// tag::send-TextMessageQuickReplies[]
		final IdRecipient recipient = IdRecipient.create("<PSID>");

		final String text = "Here is a quick reply!";

		final TextQuickReply quickReplyA = TextQuickReply.create("Search", "<POSTBACK_PAYLOAD>", of(new URL("http://example.com/img/red.png")));
		final LocationQuickReply quickReplyB = LocationQuickReply.create();
		final TextQuickReply quickReplyC = TextQuickReply.create("Something Else", "<POSTBACK_PAYLOAD>");

		final List<QuickReply> quickReplies = Arrays.asList(quickReplyA, quickReplyB, quickReplyC);

		final TextMessage message = TextMessage.create(text, of(quickReplies), empty());
		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, message);

		messenger.send(payload);
		// end::send-TextMessageQuickReplies[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\n" + "    \"id\":\"<PSID>\"\n" + "  },\n" + "  \"messaging_type\":\"RESPONSE\"," + "  \"message\":{\n"
						+ "    \"text\": \"Here is a quick reply!\",\n" + "    \"quick_replies\":[\n" + "      {\n" + "        \"content_type\":\"text\",\n"
						+ "        \"title\":\"Search\",\n" + "        \"payload\":\"<POSTBACK_PAYLOAD>\",\n"
						+ "        \"image_url\":\"http://example.com/img/red.png\"\n" + "      },\n" + "      {\n" + "        \"content_type\":\"location\"\n"
						+ "      },\n" + "      {\n" + "        \"content_type\":\"text\",\n" + "        \"title\":\"Something Else\",\n"
						+ "        \"payload\":\"<POSTBACK_PAYLOAD>\"\n" + "      }\n" + "    ]\n" + "  }\n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	//FIXME split in two!!
	@Test
	public void shouldSendTextMessageWithEmailAndPhoneNumberQuickReplies() throws Exception {
		final IdRecipient recipient = IdRecipient.create("<PSID>");

		final String text = "Please send us your email or phone number.";

		final UserEmailQuickReply userEmailQuickReply = UserEmailQuickReply.create();
		final UserPhoneNumberQuickReply userPhoneNumberQuickReply = UserPhoneNumberQuickReply.create();

		final List<QuickReply> quickReplies = Arrays.asList(userEmailQuickReply, userPhoneNumberQuickReply);

		final TextMessage message = TextMessage.create(text, of(quickReplies), empty());
		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, message);

		messenger.send(payload);

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\n" + "    \"id\":\"<PSID>\"\n" + "  },\n" + "  \"messaging_type\":\"RESPONSE\"," + "  \"message\":{\n"
						+ "    \"text\": \"Please send us your email or phone number.\",\n" + "    \"quick_replies\":[\n" + "      {\n"
						+ "        \"content_type\":\"user_email\"\n" + "      },\n" + "      {\n" + "        \"content_type\":\"user_phone_number\"\n"
						+ "      }\n" + "    ]\n" + "  }\n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendTextMessageWithMetadata() throws Exception {
		// tag::send-TextMessageMetadata[]
		final IdRecipient recipient = IdRecipient.create("USER_ID");
		final NotificationType notificationType = NotificationType.SILENT_PUSH;
		final String text = "Hello Messenger Platform";
		final String metadata = "DEVELOPER_DEFINED_METADATA";

		final TextMessage textMessage = TextMessage.create(text, empty(), of(metadata));
		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage, of(notificationType), empty());

		messenger.send(payload);
		// end::send-TextMessageMetadata[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\"recipient\":{\"id\":\"USER_ID\"}," + "\"notification_type\":\"SILENT_PUSH\"," + "\"messaging_type\":\"RESPONSE\"," + "\"message\":{"
						+ "\"text\":\"Hello Messenger Platform\"," + "\"metadata\":\"DEVELOPER_DEFINED_METADATA\"" + "}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendImageAttachmentMessageWithUrl() throws Exception {
		// tag::send-ImageMessageUrl[]
		final String recipientId = "USER_ID";
		final String imageUrl = "https://petersapparel.com/img/shirt.png";

		final UrlRichMediaAsset richMediaAsset = UrlRichMediaAsset.create(IMAGE, new URL(imageUrl));
		final RichMediaMessage richMediaMessage = RichMediaMessage.create(richMediaAsset);
		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, richMediaMessage);

		messenger.send(payload);
		// end::send-ImageMessageUrl[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\"recipient\":{\"id\":\"USER_ID\"}," + "\"messaging_type\":\"RESPONSE\"," + "\"message\":{\"attachment\":{" + "\"type\":\"image\","
						+ "\"payload\":{\"url\":\"https://petersapparel.com/img/shirt.png\"}" + "}}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendReusableImageAttachmentMessageWithUrl() throws Exception {
		// tag::send-ImageMessageUrlReusable[]
		final IdRecipient recipient = IdRecipient.create("USER_ID");
		final NotificationType notificationType = NotificationType.NO_PUSH;
		final String imageUrl = "https://petersapparel.com/img/shirt.png";

		final UrlRichMediaAsset richMediaAsset = UrlRichMediaAsset.create(IMAGE, new URL(imageUrl), of(true));
		final RichMediaMessage richMediaMessage = RichMediaMessage.create(richMediaAsset);
		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, richMediaMessage, of(notificationType), empty());

		messenger.send(payload);
		// end::send-ImageMessageUrlReusable[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"notification_type\":\"NO_PUSH\"," + "\"messaging_type\":\"RESPONSE\","
				+ "\"message\":{\"attachment\":{" + "\"type\":\"image\","
				+ "\"payload\":{\"url\":\"https://petersapparel.com/img/shirt.png\",\"is_reusable\":true}" + "}}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendImageAttachmentMessageWithAttachmentId() throws Exception {
		// tag::send-ImageMessageAttachmentId[]
		final IdRecipient recipient = IdRecipient.create("USER_ID");
		final NotificationType notificationType = NotificationType.NO_PUSH;
		final String attachmentId = "1745504518999123";

		final ReusableRichMediaAsset richMediaAsset = ReusableRichMediaAsset.create(IMAGE, attachmentId);
		final RichMediaMessage richMediaMessage = RichMediaMessage.create(richMediaAsset);
		final MessagePayload payload = MessagePayload.create(recipient, MessagingType.RESPONSE, richMediaMessage, of(notificationType), empty());

		messenger.send(payload);
		// end::send-ImageMessageAttachmentId[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"notification_type\":\"NO_PUSH\"," + "\"messaging_type\":\"RESPONSE\","
				+ "\"message\":{\"attachment\":{" + "\"type\":\"image\"," + "\"payload\":{\"attachment_id\":\"1745504518999123\"}" + "}}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendButtonTemplateMessage() throws Exception {
		// tag::send-ButtonTemplate[]
		final String recipientId = "USER_ID";

		final UrlButton buttonA = UrlButton.create("Show Website", new URL("https://petersapparel.parseapp.com"));
		final PostbackButton buttonB = PostbackButton.create("Start Chatting", "USER_DEFINED_PAYLOAD");
		final UrlButton buttonC = UrlButton.create("Show Website", new URL("https://petersapparel.parseapp.com"), of(WebviewHeightRatio.FULL), of(true),
				of(new URL("https://petersfancyapparel.com/fallback")), empty());

		final List<Button> buttons = Arrays.asList(buttonA, buttonB, buttonC);
		final ButtonTemplate buttonTemplate = ButtonTemplate.create("What do you want to do next?", buttons);

		final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);

		messenger.send(payload);
		// end::send-ButtonTemplate[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"messaging_type\":\"RESPONSE\","
				+ "\"message\":{\"attachment\":{\"type\":\"template\",\"payload\":{\"text\":\"What do you want to do next?\""
				+ ",\"buttons\":[{\"url\":\"https://petersapparel.parseapp.com\",\"title\":\"Show Website\""
				+ ",\"type\":\"web_url\"},{\"payload\":\"USER_DEFINED_PAYLOAD\",\"title\":\"Start Chatting\""
				+ ",\"type\":\"postback\"},{\"url\":\"https://petersapparel.parseapp.com\",\"webview_height_ratio\":\"full\""
				+ ",\"messenger_extensions\":true,\"fallback_url\":\"https://petersfancyapparel.com/fallback\""
				+ ",\"title\":\"Show Website\",\"type\":\"web_url\"}],\"template_type\":\"button\"}}}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendGenericTemplateMessageWithButtons() throws Exception {
		// tag::send-GenericTemplateButtons[]
		final String recipientId = "USER_ID";

		final List<Button> buttons = Arrays.asList(UrlButton
						.create("Select Criteria", new URL("https://petersfancyapparel.com/criteria_selector"), of(WebviewHeightRatio.FULL), of(true),
								of(new URL("https://petersfancyapparel.com/fallback")), empty()), CallButton.create("Call Representative", "+15105551234"),
				PostbackButton.create("Start Chatting", "DEVELOPER_DEFINED_PAYLOAD"));

		final DefaultAction defaultAction = DefaultAction
				.create(new URL("https://peterssendreceiveapp.ngrok.io/view?item=103"), of(WebviewHeightRatio.TALL), of(true),
						of(new URL("https://peterssendreceiveapp.ngrok.io/")), of(WebviewShareButtonState.HIDE));

		final Element element = Element.create("Welcome to Peters Hats", of("We have got the right hat for everyone."),
				of(new URL("https://petersfancybrownhats.com/company_image.png")), of(defaultAction), of(buttons));

		final GenericTemplate genericTemplate = GenericTemplate.create(singletonList(element));

		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(genericTemplate));

		messenger.send(payload);
		// end::send-GenericTemplateButtons[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\n" + "    \"id\":\"USER_ID\"\n" + "  },\n" + "  \"messaging_type\":\"RESPONSE\"," + "  \"message\":{\n"
						+ "    \"attachment\":{\n" + "      \"type\":\"template\",\n" + "      \"payload\":{\n" + "        \"template_type\":\"generic\",\n"
						+ "        \"elements\":[\n" + "           {\n" + "            \"title\":\"Welcome to Peters Hats\",\n"
						+ "            \"image_url\":\"https://petersfancybrownhats.com/company_image.png\",\n"
						+ "            \"subtitle\":\"We have got the right hat for everyone.\",\n" + "            \"default_action\": {\n"
						+ "              \"type\": \"web_url\",\n" + "              \"url\": \"https://peterssendreceiveapp.ngrok.io/view?item=103\",\n"
						+ "              \"messenger_extensions\": true,\n" + "              \"webview_height_ratio\": \"tall\",\n"
						+ "              \"webview_share_button\": \"hide\",\n" + "              \"fallback_url\": \"https://peterssendreceiveapp.ngrok.io/\"\n"
						+ "            },\n" + "            \"buttons\":[\n" + "              {\n" + "                   \"type\":\"web_url\",\n"
						+ "                   \"url\":\"https://petersfancyapparel.com/criteria_selector\",\n"
						+ "                   \"title\":\"Select Criteria\",\n" + "                   \"webview_height_ratio\": \"full\",\n"
						+ "                   \"messenger_extensions\": true,  \n"
						+ "                   \"fallback_url\": \"https://petersfancyapparel.com/fallback\"\n" + "              }," + "               {\n"
						+ "                   \"type\":\"phone_number\",\n" + "                   \"title\":\"Call Representative\",\n"
						+ "                   \"payload\":\"+15105551234\"\n" + "               }," + "               {\n"
						+ "                \"type\":\"postback\",\n" + "                \"title\":\"Start Chatting\",\n"
						+ "                \"payload\":\"DEVELOPER_DEFINED_PAYLOAD\"\n" + "              }              \n" + "            ]      \n"
						+ "          }\n" + "        ]\n" + "      }\n" + "    }\n" + "  }\n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendGenericTemplateMessageWithLogInAndLogOutButtons() throws Exception {
		// tag::send-GenericTemplateLoginLogoutButtons[]
		final String recipientId = "USER_ID";

		final List<Button> buttons = Arrays.asList(LogInButton.create(new URL("https://www.example.com/authorize")), LogOutButton.create());

		final Element element = Element.create("Welcome to M-Bank", empty(), of(new URL("http://www.example.com/images/m-bank.png")), empty(), of(buttons));

		final GenericTemplate genericTemplate = GenericTemplate.create(singletonList(element));

		messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(genericTemplate)));
		// end::send-GenericTemplateLoginLogoutButtons[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"messaging_type\":\"RESPONSE\"," + "\"message\":"
				+ "{\"attachment\":{\"type\":\"template\",\"payload\":{\"elements\":"
				+ "[{\"title\":\"Welcome to M-Bank\",\"image_url\":\"http://www.example.com/images/m-bank.png\","
				+ "\"buttons\":[{\"url\":\"https://www.example.com/authorize\",\"type\":\"account_link\"},"
				+ "{\"type\":\"account_unlink\"}]}],\"template_type\":\"generic\"}}}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendGenericTemplateMessageWithShareButton() throws Exception {
		final String recipientId = "<PSID>";
		final Element element = Element.create("Breaking News: Record Thunderstorms", of("The local area is due for record thunderstorms over the weekend."),
				of(new URL("https://thechangreport.com/img/lightning.png")), empty(), of(singletonList(ShareButton.create())));

		final GenericTemplate genericTemplate = GenericTemplate.create(singletonList(element));

		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(genericTemplate));

		messenger.send(payload);

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\n" + "    \"id\":\"<PSID>\"\n" + "  },\n" + "  \"messaging_type\":\"RESPONSE\"," + "  \"message\":{\n"
						+ "    \"attachment\":{\n" + "      \"type\":\"template\",\n" + "      \"payload\":{\n" + "        \"template_type\":\"generic\",\n"
						+ "        \"elements\":[\n" + "          {\n" + "            \"title\":\"Breaking News: Record Thunderstorms\",\n"
						+ "            \"subtitle\":\"The local area is due for record thunderstorms over the weekend.\",\n"
						+ "            \"image_url\":\"https://thechangreport.com/img/lightning.png\",\n" + "            \"buttons\": [\n" + "              {\n"
						+ "                \"type\": \"element_share\"\n" + "              }\n" + "            ]\n" + "          }\n" + "        ]\n"
						+ "      }\n" + "    }\n" + "  }\n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendGenericTemplateMessageWithShareButtonWithShareContents() throws Exception {
		final String recipientId = "<PSID>";

		final GenericTemplate shareGenericTemplate = GenericTemplate.create(singletonList(
				Element.create("I took the hat quiz", of("My result: Fez"), of(new URL("https://bot.peters-hats.com/img/hats/fez.jpg")),
						of(DefaultAction.create(new URL("http://m.me/petershats?ref=invited_by_24601"))),
						of(singletonList(UrlButton.create("Take Quiz", new URL("http://m.me/petershats?ref=invited_by_24601")))))));

		final Element element = Element.create("Breaking News: Record Thunderstorms", of("The local area is due for record thunderstorms over the weekend."),
				of(new URL("https://thechangreport.com/img/lightning.png")), empty(), of(singletonList(ShareButton.create(of(shareGenericTemplate)))));

		final GenericTemplate genericTemplate = GenericTemplate.create(singletonList(element));

		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(genericTemplate));

		messenger.send(payload);

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\n" + "    \"id\":\"<PSID>\"\n" + "  },\n" + "  \"messaging_type\":\"RESPONSE\"," + "  \"message\":{\n"
						+ "    \"attachment\":{\n" + "      \"type\":\"template\",\n" + "      \"payload\":{\n" + "        \"template_type\":\"generic\",\n"
						+ "        \"elements\":[\n" + "          {\n" + "            \"title\":\"Breaking News: Record Thunderstorms\",\n"
						+ "            \"subtitle\":\"The local area is due for record thunderstorms over the weekend.\",\n"
						+ "            \"image_url\":\"https://thechangreport.com/img/lightning.png\",\n" + "            \"buttons\": [\n" + "              {\n"
						+ "                \"type\": \"element_share\",\n" + "                \"share_contents\": { \n"
						+ "                  \"attachment\": {\n" + "                    \"type\": \"template\",\n" + "                    \"payload\": {\n"
						+ "                      \"template_type\": \"generic\",\n" + "                      \"elements\": [\n" + "                        {\n"
						+ "                          \"title\": \"I took the hat quiz\",\n" + "                          \"subtitle\": \"My result: Fez\",\n"
						+ "                          \"image_url\": \"https://bot.peters-hats.com/img/hats/fez.jpg\",\n"
						+ "                          \"default_action\": {\n" + "                            \"type\": \"web_url\",\n"
						+ "                            \"url\": \"http://m.me/petershats?ref=invited_by_24601\"\n" + "                          },\n"
						+ "                          \"buttons\": [\n" + "                            {\n"
						+ "                              \"type\": \"web_url\",\n"
						+ "                              \"url\": \"http://m.me/petershats?ref=invited_by_24601\", \n"
						+ "                              \"title\": \"Take Quiz\"\n" + "                            }\n" + "                          ]\n"
						+ "                        }\n" + "                      ]\n" + "                    }\n" + "                  }\n"
						+ "                }\n" + "              }\n" + "            ]\n" + "          }\n" + "        ]\n" + "      }\n" + "    }\n" + "  }\n"
						+ "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendReceiptTemplateMessage() throws Exception {
		// tag::send-ReceiptTemplate[]
		final String recipientId = "USER_ID";

		final Adjustment adjustment1 = Adjustment.create("New Customer Discount", 20.00F);
		final Adjustment adjustment2 = Adjustment.create("$10 Off Coupon", 10.00F);

		final Item item1 = Item.create("Classic White T-Shirt", 50F, of("100% Soft and Luxurious Cotton"), of(2), of("USD"),
				of(new URL("http://petersapparel.parseapp.com/img/whiteshirt.png")));

		final Item item2 = Item.create("Classic Gray T-Shirt", 25F, of("100% Soft and Luxurious Cotton"), of(1), of("USD"),
				of(new URL("http://petersapparel.parseapp.com/img/grayshirt.png")));

		final Address address = Address.create("1 Hacker Way", of(""), "Menlo Park", "94025", "CA", "US");
		final Summary summary = Summary.create(56.14F, of(75.00F), of(6.19F), of(4.95F));

		final ReceiptTemplate receiptTemplate = ReceiptTemplate
				.create("Stephane Crozatier", "12345678902", "Visa 2345", "USD", summary, of(address), of(Arrays.asList(item1, item2)),
						of(Arrays.asList(adjustment1, adjustment2)), empty(), of(new URL("http://petersapparel.parseapp.com/order?order_id=123456")), empty(),
						of(ZonedDateTime.of(2015, 4, 7, 22, 14, 12, 0, ZoneOffset.UTC).toInstant()));

		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(receiptTemplate));

		messenger.send(payload);
		// end::send-ReceiptTemplate[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"messaging_type\":\"RESPONSE\","
				+ "\"message\":{\"attachment\":{\"type\":\"template\",\"payload\":{\"recipient_name\":\"Stephane Crozatier\","
				+ "\"order_number\":\"12345678902\",\"currency\":\"USD\",\"payment_method\":\"Visa 2345\","
				+ "\"timestamp\":\"1428444852\",\"order_url\":\"http://petersapparel.parseapp.com/order?order_id\\u003d123456\","
				+ "\"elements\":[{\"title\":\"Classic White T-Shirt\",\"subtitle\":\"100% Soft and Luxurious Cotton\","
				+ "\"quantity\":2,\"price\":50.00,\"currency\":\"USD\","
				+ "\"image_url\":\"http://petersapparel.parseapp.com/img/whiteshirt.png\"},{\"title\":\"Classic Gray T-Shirt\","
				+ "\"subtitle\":\"100% Soft and Luxurious Cotton\",\"quantity\":1,\"price\":25.00,\"currency\":\"USD\","
				+ "\"image_url\":\"http://petersapparel.parseapp.com/img/grayshirt.png\"}],"
				+ "\"address\":{\"street_1\":\"1 Hacker Way\",\"street_2\":\"\",\"city\":\"Menlo Park\",\"postal_code\":\"94025\","
				+ "\"state\":\"CA\",\"country\":\"US\"},\"summary\":{\"total_cost\":56.14,\"total_tax\":6.19,"
				+ "\"shipping_cost\":4.95,\"subtotal\":75.00},\"adjustments\":[{\"name\":\"New Customer Discount\","
				+ "\"amount\":20.00},{\"name\":\"$10 Off Coupon\",\"amount\":10.00}],\"template_type\":\"receipt\"}}}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendListTemplateMessage() throws Exception {
		// tag::send-ListTemplate[]
		final String recipientId = "USER_ID";

		final Element element1 = Element
				.create("Classic T-Shirt Collection", of("See all our colors"), of(new URL("https://peterssendreceiveapp.ngrok.io/img/collection.png")),
						of(DefaultAction.create(new URL("https://peterssendreceiveapp.ngrok.io/shop_collection"), of(WebviewHeightRatio.TALL), of(true),
								of(new URL("https://peterssendreceiveapp.ngrok.io/fallback")), empty())), of(singletonList(UrlButton
								.create("View", new URL("https://peterssendreceiveapp.ngrok.io/collection"), of(WebviewHeightRatio.TALL), empty(), empty(),
										empty()))));

		final Element element2 = Element.create("Classic White T-Shirt", of("100% Cotton, 200% Comfortable"),
				of(new URL("https://peterssendreceiveapp.ngrok.io/img/white-t-shirt.png")), of(DefaultAction
						.create(new URL("https://peterssendreceiveapp.ngrok.io/view?item=100"), of(WebviewHeightRatio.TALL), empty(), empty(), empty())),
				of(singletonList(UrlButton
						.create("Shop Now", new URL("https://peterssendreceiveapp.ngrok.io/shop?item=100"), of(WebviewHeightRatio.TALL), empty(), empty(),
								empty()))));

		final Element element3 = Element
				.create("Classic Blue T-Shirt", of("100% Cotton, 200% Comfortable"), of(new URL("https://peterssendreceiveapp.ngrok.io/img/blue-t-shirt.png")),
						of(DefaultAction.create(new URL("https://peterssendreceiveapp.ngrok.io/view?item=101"), of(WebviewHeightRatio.TALL), empty(), empty(),
								empty())), of(singletonList(UrlButton
								.create("Shop Now", new URL("https://peterssendreceiveapp.ngrok.io/shop?item=101"), of(WebviewHeightRatio.TALL), empty(),
										empty(), empty()))));

		final Element element4 = Element.create("Classic Black T-Shirt", of("100% Cotton, 200% Comfortable"),
				of(new URL("https://peterssendreceiveapp.ngrok.io/img/black-t-shirt.png")), of(DefaultAction
						.create(new URL("https://peterssendreceiveapp.ngrok.io/view?item=102"), of(WebviewHeightRatio.TALL), empty(), empty(), empty())),
				of(singletonList(UrlButton
						.create("Shop Now", new URL("https://peterssendreceiveapp.ngrok.io/shop?item=102"), of(WebviewHeightRatio.TALL), empty(), empty(),
								empty()))));

		final ListTemplate listTemplate = ListTemplate.create(Arrays.asList(element1, element2, element3, element4), of(TopElementStyle.LARGE),
				of(singletonList(PostbackButton.create("View More", "payload"))));

		messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(listTemplate)));
		// end::send-ListTemplate[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\"recipient\":{\"id\":\"USER_ID\"}," + "\"messaging_type\":\"RESPONSE\"," + "\"message\":{\"attachment\":{\"type\":\"template\","
						+ "\"payload\":{\"top_element_style\":\"large\",\"buttons\":[{\"payload\":\"payload\",\"title\":\"View More\","
						+ "\"type\":\"postback\"}],\"elements\":[{\"title\":\"Classic T-Shirt Collection\","
						+ "\"subtitle\":\"See all our colors\",\"image_url\":\"https://peterssendreceiveapp.ngrok.io/img/collection.png\","
						+ "\"buttons\":[{\"url\":\"https://peterssendreceiveapp.ngrok.io/collection\",\"webview_height_ratio\":\"tall\","
						+ "\"title\":\"View\",\"type\":\"web_url\"}],\"default_action\":{\"type\":\"web_url\","
						+ "\"url\":\"https://peterssendreceiveapp.ngrok.io/shop_collection\",\"webview_height_ratio\":\"tall\","
						+ "\"messenger_extensions\":true,\"fallback_url\":\"https://peterssendreceiveapp.ngrok.io/fallback\"}},"
						+ "{\"title\":\"Classic White T-Shirt\",\"subtitle\":\"100% Cotton, 200% Comfortable\","
						+ "\"image_url\":\"https://peterssendreceiveapp.ngrok.io/img/white-t-shirt.png\","
						+ "\"buttons\":[{\"url\":\"https://peterssendreceiveapp.ngrok.io/shop?item\\u003d100\",\"webview_height_ratio\":\"tall\","
						+ "\"title\":\"Shop Now\",\"type\":\"web_url\"}],\"default_action\":{\"type\":\"web_url\","
						+ "\"url\":\"https://peterssendreceiveapp.ngrok.io/view?item\\u003d100\",\"webview_height_ratio\":\"tall\"}},"
						+ "{\"title\":\"Classic Blue T-Shirt\",\"subtitle\":\"100% Cotton, 200% Comfortable\","
						+ "\"image_url\":\"https://peterssendreceiveapp.ngrok.io/img/blue-t-shirt.png\","
						+ "\"buttons\":[{\"url\":\"https://peterssendreceiveapp.ngrok.io/shop?item\\u003d101\",\"webview_height_ratio\":\"tall\","
						+ "\"title\":\"Shop Now\",\"type\":\"web_url\"}],\"default_action\":{\"type\":\"web_url\","
						+ "\"url\":\"https://peterssendreceiveapp.ngrok.io/view?item\\u003d101\",\"webview_height_ratio\":\"tall\"}},"
						+ "{\"title\":\"Classic Black T-Shirt\",\"subtitle\":\"100% Cotton, 200% Comfortable\","
						+ "\"image_url\":\"https://peterssendreceiveapp.ngrok.io/img/black-t-shirt.png\","
						+ "\"buttons\":[{\"url\":\"https://peterssendreceiveapp.ngrok.io/shop?item\\u003d102\",\"webview_height_ratio\":\"tall\","
						+ "\"title\":\"Shop Now\",\"type\":\"web_url\"}],\"default_action\":{\"type\":\"web_url\","
						+ "\"url\":\"https://peterssendreceiveapp.ngrok.io/view?item\\u003d102\",\"webview_height_ratio\":\"tall\"}}],"
						+ "\"template_type\":\"list\"}}}}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendOpenGraphTemplateMessage() throws Exception {
		// tag::send-OpenGraphTemplate[]
		final String recipientId = "USER_ID";

		final UrlButton urlButton = UrlButton.create("View More", new URL("https://en.wikipedia.org/wiki/Rickrolling"));
		final OpenGraphObject openGraphObject = OpenGraphObject
				.create(new URL("https://open.spotify.com/track/7GhIk7Il098yCjg4BQjzvb"), of(singletonList(urlButton)));
		final OpenGraphTemplate openGraphTemplate = OpenGraphTemplate.create(singletonList(openGraphObject));

		messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(openGraphTemplate)));
		// end::send-OpenGraphTemplate[]

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\":{\n" + "    \"id\":\"USER_ID\"\n" + "  },\n" + "  \"messaging_type\":\"RESPONSE\"," + "  \"message\":{\n"
						+ "    \"attachment\":{\n" + "      \"type\":\"template\",\n" + "      \"payload\":{\n" + "        \"template_type\":\"open_graph\",\n"
						+ "        \"elements\":[\n" + "           {\n" + "            \"url\":\"https://open.spotify.com/track/7GhIk7Il098yCjg4BQjzvb\",\n"
						+ "            \"buttons\":[\n" + "              {\n" + "                \"type\":\"web_url\",\n"
						+ "                \"url\":\"https://en.wikipedia.org/wiki/Rickrolling\",\n" + "                \"title\":\"View More\"\n"
						+ "              }              \n" + "            ]      \n" + "          }\n" + "        ]\n" + "      }\n" + "    }\n" + "  }\n"
						+ "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldSendButtonTemplateMessageWithUrlButtonWithWebviewShareButtonState() throws Exception {
		final String recipientId = "USER_ID";

		final UrlButton buttonA = UrlButton.create("Show Website", new URL("https://petersapparel.parseapp.com"));
		final UrlButton buttonB = UrlButton.create("Show Website", new URL("https://petersapparel.parseapp.com"), of(WebviewHeightRatio.FULL), of(true),
				of(new URL("https://petersfancyapparel.com/fallback")), of(WebviewShareButtonState.SHOW));
		final UrlButton buttonC = UrlButton.create("Show Website", new URL("https://petersapparel.parseapp.com"), of(WebviewHeightRatio.FULL), of(true),
				of(new URL("https://petersfancyapparel.com/fallback")), of(WebviewShareButtonState.HIDE));

		final List<Button> buttons = Arrays.asList(buttonA, buttonB, buttonC);
		final ButtonTemplate buttonTemplate = ButtonTemplate.create("What do you want to do next?", buttons);

		final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);

		messenger.send(payload);

		final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
		final String expectedJsonBody =
				"{\n" + "  \"recipient\": {\n" + "    \"id\": \"USER_ID\"\n" + "  },\n" + "  \"messaging_type\": \"RESPONSE\",\n" + "  \"message\": {\n"
						+ "    \"attachment\": {\n" + "      \"type\": \"template\",\n" + "      \"payload\": {\n"
						+ "        \"text\": \"What do you want to do next?\",\n" + "        \"buttons\": [{\n"
						+ "          \"url\": \"https://petersapparel.parseapp.com\",\n" + "          \"title\": \"Show Website\",\n"
						+ "          \"type\": \"web_url\"\n" + "        }, {\n" + "          \"url\": \"https://petersapparel.parseapp.com\",\n"
						+ "          \"webview_height_ratio\": \"full\",\n" + "          \"messenger_extensions\": true,\n"
						+ "          \"fallback_url\": \"https://petersfancyapparel.com/fallback\",\n" + "          \"title\": \"Show Website\",\n"
						+ "          \"type\": \"web_url\"\n" + "        }, {\n" + "          \"url\": \"https://petersapparel.parseapp.com\",\n"
						+ "          \"webview_share_button\": \"hide\",\n" + "          \"webview_height_ratio\": \"full\",\n"
						+ "          \"messenger_extensions\": true,\n" + "          \"fallback_url\": \"https://petersfancyapparel.com/fallback\",\n"
						+ "          \"title\": \"Show Website\",\n" + "          \"type\": \"web_url\"\n" + "        }],\n"
						+ "        \"template_type\": \"button\"\n" + "      }\n" + "    }\n" + "  }\n" + "}";
		verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), payloadCaptor.capture());
		JSONAssert.assertEquals(expectedJsonBody, payloadCaptor.getValue(), true);
	}

	@Test
	public void shouldHandleSuccessResponse() throws Exception {
		final HttpResponse successfulResponse = new HttpResponse(200,
				"{\n" + "  \"recipient_id\": \"USER_ID\",\n" + "  \"message_id\": \"mid.1473372944816:94f72b88c597657974\",\n"
						+ "  \"attachment_id\": \"1745504518999123\"\n" + "}");
		when(mockHttpClient.execute(any(HttpMethod.class), anyString(), anyString())).thenReturn(successfulResponse);

		// tag::send-SuccessResponse[]
		final UrlRichMediaAsset richMediaAsset = UrlRichMediaAsset.create(IMAGE, new URL("http://image.url"), of(true));
		final MessagePayload payload = MessagePayload.create("USER_ID", MessagingType.RESPONSE, RichMediaMessage.create(richMediaAsset));

		final MessageResponse messageResponse = messenger.send(payload);

		final Optional<String> recipientId = messageResponse.recipientId();
		final Optional<String> messageId = messageResponse.messageId();
		final Optional<String> attachmentId = messageResponse.attachmentId();
		log.debug("RecipientId: {} | MessageId: {} | AttachmentId: {}", recipientId, messageId, attachmentId);
		// end::send-SuccessResponse[]

		assertThat(recipientId, is(equalTo(of("USER_ID"))));
		assertThat(messageId, is(equalTo(of("mid.1473372944816:94f72b88c597657974"))));
		assertThat(attachmentId, is(equalTo(of("1745504518999123"))));
	}

	@Test
	public void shouldHandleErrorResponse() throws Exception {
		final HttpResponse errorResponse = new HttpResponse(401,
				"{\n" + "  \"error\": {\n" + "    \"message\": \"Invalid OAuth access token.\",\n" + "    \"type\": \"OAuthException\",\n"
						+ "    \"code\": 190,\n" + "    \"fbtrace_id\": \"BLBz/WZt8dN\"\n" + "  }\n" + "}");
		when(mockHttpClient.execute(any(HttpMethod.class), anyString(), anyString())).thenReturn(errorResponse);

		MessengerApiException messengerApiException = null;
		try {
			final MessagePayload payload = MessagePayload.create("test", MessagingType.RESPONSE, TextMessage.create("test"));
			messenger.send(payload);
		} catch (MessengerApiException e) {
			messengerApiException = e;
		}

		assertThat(messengerApiException, is(notNullValue()));
		assertThat(messengerApiException.message(), is(equalTo("Invalid OAuth access token.")));
		assertThat(messengerApiException.type(), is(equalTo(of("OAuthException"))));
		assertThat(messengerApiException.code(), is(equalTo(of(190))));
		assertThat(messengerApiException.fbTraceId(), is(equalTo(of("BLBz/WZt8dN"))));
	}
}
