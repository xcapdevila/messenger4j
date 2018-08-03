package com.github.messenger4j.handover;

import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.send.recipient.Recipient;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

/**
 * TAKE CONTROL & REQUEST CONTROL
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class HandoverRequestPayload extends HandoverPayload {

	public static HandoverRequestPayload create(@NonNull String recipientId, @NonNull Optional<String> metadata) {
		return new HandoverRequestPayload(IdRecipient.create(recipientId), metadata);
	}

	public HandoverRequestPayload(Recipient recipient, Optional<String> metadata) {
		super(recipient, metadata);
	}
}
