package com.github.messenger4j.webhook.event;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public enum BaseEventType {
	/**
	 * Standard Events
	 */
	MESSAGING,
	/**
	 * StandBy Events received when the application does NOT have the control of the conversation
	 */
	STANDBY,
	/**
	 * Handover Events received when handover requests are triggered from other applications
	 */
	HANDOVER
}
