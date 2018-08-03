package com.github.messenger4j.handover;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public final class ThreadOwner {

	private final Optional<String> appId;

	public ThreadOwner(@NonNull Optional<String> appId) {
		this.appId = appId;
	}

	public Optional<String> appId() {
		return appId;
	}

}
