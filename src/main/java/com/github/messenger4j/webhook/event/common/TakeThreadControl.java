package com.github.messenger4j.webhook.event.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public final class TakeThreadControl {

	private final String previousOwnerAppId;
	private final String metadata;

	public TakeThreadControl(String previousOwnerAppId, String metadata) {
		this.previousOwnerAppId = previousOwnerAppId;
		this.metadata = metadata;
	}

	public String getPreviousOwnerAppId() {
		return previousOwnerAppId;
	}

	public String getMetadata() {
		return metadata;
	}

}
