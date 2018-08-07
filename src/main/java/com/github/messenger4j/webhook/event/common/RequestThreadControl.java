package com.github.messenger4j.webhook.event.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public final class RequestThreadControl {

	private final String requestedOwnerAppId;
	private final String metadata;

	public RequestThreadControl(String requestedOwnerAppId, String metadata) {
		this.requestedOwnerAppId = requestedOwnerAppId;
		this.metadata = metadata;
	}

	public String getRequestedOwnerAppId() {
		return requestedOwnerAppId;
	}

	public String getMetadata() {
		return metadata;
	}

}
