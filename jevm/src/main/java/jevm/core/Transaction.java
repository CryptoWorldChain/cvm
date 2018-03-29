package jevm.core;

import static jevm.util.Number.*;

/**
 *
 * @author David J. Pearce
 *
 */
public abstract class Transaction {
	/**
	 * Records the number of transactions sent by the sender.
	 */
	private final u256 nonce;
	/**
	 * Determines the number of Wei to be paid per unit of gas for all
	 * computation costs incurred from executing this transaction.
	 */
	private final u256 gasPrice;
	/**
	 * Determines the maximum amount of gas available for executing this
	 * transaction. This is paid before any computation is done and may not be
	 * increased later.
	 */
	private final u256 gasLimit;
	/**
	 * The 160-bit address of the message call’s recipient or, for a contract
	 * creation transaction ?????.
	 */
	private final u160 to;
	/**
	 * Determines the amount of Wei to be transferred to the message's recipient or,
	 * in the case of contract creation, as an endowment to the newly created
	 * account.
	 */
	private final u256 value;

	private final u256 v, r, s;

	public Transaction(u256 nonce, u256 gasPrice, u256 gasLimit, u160 to, u256 value, u256 v, u256 r, u256 s) {
		this.nonce = nonce;
		this.gasPrice = gasPrice;
		this.gasLimit = gasLimit;
		this.to = to;
		this.value = value;
		this.v = v;
		this.r = r;
		this.s = s;
	}

	/**
	 * Represents the creation of a new account.
	 *
	 * @author David J. Pearce
	 *
	 */
	public static final class AccountCreation extends Transaction {
		/**
		 * A code fragment which returns the body to be associated with the newly
		 * created account. The body is another code fragment which executes every time
		 * the account receives a message call. The init code is executed exactly once
		 * at account creation time.
		 */
		private final Bytecode[] init;

		public AccountCreation(u256 nonce, u256 gasPrice, u256 gasLimit, u160 to, u256 value, u256 v, u256 r, u256 s, Bytecode[] init) {
			super(nonce,gasPrice,gasLimit,to,value,v,r,s);
			this.init = init;
		}
	}

	public static final class MessageCall extends Transaction {
		/**
		 * An unlimited size byte array which determines the input data for the message
		 * call.
		 */
		private final byte[] data;

		public MessageCall(u256 nonce, u256 gasPrice, u256 gasLimit, u160 to, u256 value, u256 v, u256 r, u256 s,
				byte[] data) {
			super(nonce, gasPrice, gasLimit, to, value, v, r, s);
			this.data = data;
		}
	}
}
