package com.github.messenger4j.webhook.event.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public final class PassThreadControl {

	private final String newOwnerAppId;
	private final String metadata;

	public PassThreadControl(String newOwnerAppId, String metadata) {
		this.newOwnerAppId = newOwnerAppId;
		this.metadata = metadata;
	}

	public String getNewOwnerAppId() {
		return newOwnerAppId;
	}

	public String getMetadata() {
		return metadata;
	}

}
