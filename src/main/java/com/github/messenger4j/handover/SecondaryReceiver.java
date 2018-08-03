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
public final class SecondaryReceiver {

	private final Optional<String> id;
	private final Optional<String> name;

	public SecondaryReceiver(@NonNull Optional<String> id, @NonNull Optional<String> name) {
		this.id = id;
		this.name = name;
	}

	public Optional<String> id() {
		return id;
	}

	public Optional<String> name() {
		return name;
	}
}
