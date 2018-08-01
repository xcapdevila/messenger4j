package com.github.messenger4j.handover;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public final class ThreadOwnerResponse {

	private final Optional<List<ThreadOwner>> data;

	public ThreadOwnerResponse(@NonNull Optional<List<ThreadOwner>> data) {
		this.data = data;
	}

	public Optional<List<ThreadOwner>> data() {
		return data;
	}

}
