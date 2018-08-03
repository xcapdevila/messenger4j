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

	private final Optional<ThreadOwner> threadOwner;

	public ThreadOwnerResponse(@NonNull Optional<ThreadOwner> threadOwner) {
		this.threadOwner = threadOwner;
	}

	public Optional<ThreadOwner> data() {
		return threadOwner;
	}

}
