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
public final class HandoverResponse {

	private final Optional<Boolean> success;

	public HandoverResponse(@NonNull Optional<Boolean> success) {
		this.success = success;
	}

	public Optional<Boolean> success() {
		return success;
	}

}
