package com.github.messenger4j.webhook;

import com.github.messenger4j.webhook.event.*;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

/**
 * @author Max Grabenhorst
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public final class Event {

	private final BaseEvent baseEvent;

	public Event(@NonNull BaseEvent baseEvent) {
		this.baseEvent = baseEvent;
	}

	public String senderId() {
		return baseEvent.senderId();
	}

	public String recipientId() {
		return baseEvent.recipientId();
	}

	public Instant timestamp() {
		return baseEvent.timestamp();
	}

	public BaseEventType baseEventType() {
		return baseEvent.baseEventType();
	}

	public boolean isAccountLinkingEvent() {
		return baseEvent instanceof AccountLinkingEvent;
	}

	public AccountLinkingEvent asAccountLinkingEvent() {
		if (!isAccountLinkingEvent()) {
			throw new UnsupportedOperationException("not a AccountLinkingEvent");
		}
		return (AccountLinkingEvent) baseEvent;
	}

	public boolean isMessageDeliveredEvent() {
		return baseEvent instanceof MessageDeliveredEvent;
	}

	public MessageDeliveredEvent asMessageDeliveredEvent() {
		if (!isMessageDeliveredEvent()) {
			throw new UnsupportedOperationException("not a MessageDeliveredEvent");
		}
		return (MessageDeliveredEvent) baseEvent;
	}

	public boolean isMessageEchoEvent() {
		return baseEvent instanceof MessageEchoEvent;
	}

	public MessageEchoEvent asMessageEchoEvent() {
		if (!isMessageEchoEvent()) {
			throw new UnsupportedOperationException("not a MessageEchoEvent");
		}
		return (MessageEchoEvent) baseEvent;
	}

	public boolean isAttachmentMessageEvent() {
		return baseEvent instanceof AttachmentMessageEvent;
	}

	public AttachmentMessageEvent asAttachmentMessageEvent() {
		if (!isAttachmentMessageEvent()) {
			throw new UnsupportedOperationException("not a AttachmentMessageEvent");
		}
		return (AttachmentMessageEvent) baseEvent;
	}

	public boolean isQuickReplyMessageEvent() {
		return baseEvent instanceof QuickReplyMessageEvent;
	}

	public QuickReplyMessageEvent asQuickReplyMessageEvent() {
		if (!isQuickReplyMessageEvent()) {
			throw new UnsupportedOperationException("not a QuickReplyMessageEvent");
		}
		return (QuickReplyMessageEvent) baseEvent;
	}

	public boolean isTextMessageEvent() {
		return baseEvent instanceof TextMessageEvent;
	}

	public TextMessageEvent asTextMessageEvent() {
		if (!isTextMessageEvent()) {
			throw new UnsupportedOperationException("not a TextMessageEvent");
		}
		return (TextMessageEvent) baseEvent;
	}

	public boolean isMessageReadEvent() {
		return baseEvent instanceof MessageReadEvent;
	}

	public MessageReadEvent asMessageReadEvent() {
		if (!isMessageReadEvent()) {
			throw new UnsupportedOperationException("not a MessageReadEvent");
		}
		return (MessageReadEvent) baseEvent;
	}

	public boolean isOptInEvent() {
		return baseEvent instanceof OptInEvent;
	}

	public OptInEvent asOptInEvent() {
		if (!isOptInEvent()) {
			throw new UnsupportedOperationException("not a OptInEvent");
		}
		return (OptInEvent) baseEvent;
	}

	public boolean isPostbackEvent() {
		return baseEvent instanceof PostbackEvent;
	}

	public PostbackEvent asPostbackEvent() {
		if (!isPostbackEvent()) {
			throw new UnsupportedOperationException("not a PostbackEvent");
		}
		return (PostbackEvent) baseEvent;
	}

	public boolean isReferralEvent() {
		return baseEvent instanceof ReferralEvent;
	}

	public ReferralEvent asReferralEvent() {
		if (!isReferralEvent()) {
			throw new UnsupportedOperationException("not a ReferralEvent");
		}
		return (ReferralEvent) baseEvent;
	}

	public boolean isInstantGameEvent() {
		return baseEvent instanceof InstantGameEvent;
	}

	public InstantGameEvent asInstantGameEvent() {
		if (!isInstantGameEvent()) {
			throw new UnsupportedOperationException("not a InstantGameEvent");
		}
		return (InstantGameEvent) baseEvent;
	}

	public boolean isPassThreadControlEvent() {
		return baseEvent instanceof PassThreadControlEvent;
	}

	public PassThreadControlEvent asPassThreadControlEvent() {
		if (!isPassThreadControlEvent()) {
			throw new UnsupportedOperationException("not a PassThreadControlEvent");
		}
		return (PassThreadControlEvent) baseEvent;
	}

	public boolean isTakeThreadControlEvent() {
		return baseEvent instanceof TakeThreadControlEvent;
	}

	public TakeThreadControlEvent asTakeThreadControlEvent() {
		if (!isTakeThreadControlEvent()) {
			throw new UnsupportedOperationException("not a TakeThreadControlEvent");
		}
		return (TakeThreadControlEvent) baseEvent;
	}

	public boolean isRequestThreadControlEvent() {
		return baseEvent instanceof RequestThreadControlEvent;
	}

	public RequestThreadControlEvent asRequestThreadControlEvent() {
		if (!isRequestThreadControlEvent()) {
			throw new UnsupportedOperationException("not a RequestThreadControlEvent");
		}
		return (RequestThreadControlEvent) baseEvent;
	}

	public boolean isAppRolesEvent() {
		return baseEvent instanceof AppRolesEvent;
	}

	public AppRolesEvent asAppRolesEvent() {
		if (!isAppRolesEvent()) {
			throw new UnsupportedOperationException("not a AppRolesEvent");
		}
		return (AppRolesEvent) baseEvent;
	}

}
