package com.github.messenger4j.handover;

import com.github.messenger4j.send.recipient.Recipient;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public abstract class HandoverPayload {

	private final Recipient recipient;

	private final Optional<String> metadata;

	HandoverPayload(Recipient recipient, Optional<String> metadata) {
		this.recipient = recipient;
		this.metadata = metadata;
	}

	public Recipient recipient() {
		return recipient;
	}

	public Optional<String> metadata() {
		return metadata;
	}

}
