package com.github.messenger4j.webhook.event;

import com.github.messenger4j.webhook.event.common.AppRoles;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;
import java.util.Optional;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class AppRolesEvent extends BaseEvent {

	private final Optional<AppRoles> appRoles;

	public AppRolesEvent(@NonNull String senderId, @NonNull String recipientId, @NonNull Instant timestamp, @NonNull BaseEventType baseEventType,
			@NonNull Optional<AppRoles> appRoles) {
		super(senderId, recipientId, timestamp, baseEventType);
		this.appRoles = appRoles;
	}

	public Optional<AppRoles> getAppRoles() {
		return appRoles;
	}
}
