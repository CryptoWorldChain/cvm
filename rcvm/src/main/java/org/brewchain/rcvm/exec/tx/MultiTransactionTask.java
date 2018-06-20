package org.brewchain.rcvm.exec.tx;

import org.brewchain.evm.api.EvmApi;
//import org.brewchain.account.core.TransactionHelper;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

/**
 * @author Roman Mandeleil
 * @since 23.05.2014
 */
public class MultiTransactionTask implements Callable<List<MultiTransaction.Builder>> {

	private static final Logger logger = LoggerFactory.getLogger("net");

	private final List<MultiTransaction.Builder> tx;
	private final EvmApi transactionHelper;
	// private final ChannelManager channelManager;
	// private final Channel receivedFrom;

	public MultiTransactionTask(MultiTransaction.Builder tx, EvmApi transactionHelper) {
		this(Collections.singletonList(tx), transactionHelper);
	}

	public MultiTransactionTask(List<MultiTransaction.Builder> tx, EvmApi transactionHelper) {
		// this(tx, channelManager, null);
		this.tx = tx;
		this.transactionHelper = transactionHelper;
	}

	// public MultiTransactionTask(List<MultiTransaction.Builder> tx, ChannelManager
	// channelManager,Channel receivedFrom) {
	// this.tx = tx;
	// this.channelManager = channelManager;
	// this.receivedFrom = receivedFrom;
	// }

	@Override
	public List<MultiTransaction.Builder> call() throws Exception {

		try {
			logger.info("submit tx: {}", tx.toString());
			// channelManager.sendTransaction(tx, receivedFrom);
			for (MultiTransaction.Builder t : tx) {
				// transactionHelper.CreateMultiTransaction(t);
			}
			return tx;
		} catch (Exception e) {
			logger.warn("Exception caught: {}", e);
		}
		return null;
	}
}
