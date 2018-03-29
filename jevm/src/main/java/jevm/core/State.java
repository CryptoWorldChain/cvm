package jevm.core;

import java.util.Map;

import jevm.util.Number.u160;

public class State {
	/**
	 * A mapping between addresses (160-bit identifiers) and account states
	 */
	private Map<u160,Account> accounts;
}
