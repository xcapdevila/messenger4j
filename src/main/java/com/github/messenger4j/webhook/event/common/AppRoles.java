package com.github.messenger4j.webhook.event.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public final class AppRoles {

	private final Map<String, List<String>> idRoles;

	public AppRoles(Map<String, List<String>> idRoles) {
		this.idRoles = idRoles;
	}

	public Map<String, List<String>> getIdRoles() {
		return idRoles;
	}

}
