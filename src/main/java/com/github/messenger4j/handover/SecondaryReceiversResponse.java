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
public final class SecondaryReceiversResponse {

	private final Optional<List<SecondaryReceiver>> data;

	public SecondaryReceiversResponse(@NonNull Optional<List<SecondaryReceiver>> data) {
		this.data = data;
	}

	public Optional<List<SecondaryReceiver>> data() {
		return data;
	}

}
