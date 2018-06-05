
package org.brewchain.evm.exec;

import java.util.List;

import org.brewchain.account.gens.Tx.MultiTransaction;
import org.brewchain.evm.base.LogInfo;
import org.brewchain.evm.exec.invoke.ProgramInvoke;
import org.brewchain.evm.exec.invoke.ProgramInvokeImpl;
import org.brewchain.evm.jsonrpc.TransactionReceipt;
//import org.ethereum.config.BlockchainConfig;
//import org.ethereum.config.CommonConfig;
//import org.ethereum.config.SystemProperties;
//import org.ethereum.db.BlockStore;
//import org.ethereum.db.ContractDetails;
//import org.ethereum.listener.EthereumListener;
//import org.ethereum.listener.EthereumListenerAdapter;
//import org.brewchain.account.util.ByteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionExecutor {

    private static final Logger logger = LoggerFactory.getLogger("execute");
//    private static final Logger stateLogger = LoggerFactory.getLogger("state");

//    SystemProperties config;
//    CommonConfig commonConfig;
//    BlockchainConfig blockchainConfig;
//
    private MultiTransaction.Builder tx;
//    private Repository track;
//    private Repository cacheTrack;
//    private BlockHeader blockStore;
//    private final long gasUsedInTheBlock;
    
    private boolean readyToExecute = false;
    private String execError;

//    private byte[] coinbase;

    private TransactionReceipt receipt;
    private ProgramResult result = new ProgramResult();
//    private Block currentBlock;

//    private final EthereumListener listener;

    private VM vm;
    private Program program;

//    BigInteger m_endFee = BigInteger.ZERO;
    long basicTxCost = 0;
    List<LogInfo> logs = null;

//    private ByteArraySet touchedAccounts = new ByteArraySet();

    boolean localCall = false;

    public TransactionExecutor(MultiTransaction.Builder tx) {
        this.tx = tx;
    }

//    public TransactionExecutor withCommonConfig(CommonConfig commonConfig) {
//        this.commonConfig = commonConfig;
//        this.config = commonConfig.systemProperties();
//        this.blockchainConfig = config.getBlockchainConfig().getConfigForBlock(currentBlock.getNumber());
//        return this;
//    }
	  public TransactionExecutor withCommonConfig() {
		  return this;
	  }

    private void execError(String err) {
        logger.warn(err);
        execError = err;
    }
    byte[] codeHash;
    byte[] code;
    public void init(byte[] codeHash,byte[] code) {
        readyToExecute = true;
        this.codeHash=codeHash;
        this.code=code;
    }

    public void execute() {
        if (!readyToExecute) return;
        call();
    }

    private void call() {
        if (!readyToExecute) return;
   
        this.vm = new VM();
        
        byte[] address = tx.getTxBody().getInputs(0).getAddress().toByteArray();
//        ProgramInvoke programInvoke = new ProgramInvokeImpl();
//        ProgramInvoke programInvoke = programInvokeFactory.createProgramInvoke(tx, currentBlock, cacheTrack, blockStore);
//        this.program = new Program(track.getCodeHash(targetAddress), code, programInvoke, tx, config).withCommonConfig(commonConfig);
        
//        byte[] codeHash = null;//= track.getCodeHash(targetAddress);
//        byte[] code = null;//= track.getCode(targetAddress);
        
        this.program = new Program(codeHash, code, null, tx).withCommonConfig();
//        BigInteger endowment = toBI(tx.getValue());

////      byte[] targetAddress = tx.getReceiveAddress();
//        byte[] targetAddress = tx.getTxBody().getOutputs(0).getAddress().toByteArray();
        
//        transfer(cacheTrack, tx.getSender(), targetAddress, endowment);
//        touchedAccounts.add(targetAddress);
    }

    public void go() {
        if (!readyToExecute) return;
        try {
            if (vm != null) {
                // Charge basic cost of the transaction
//                program.spendGas(tx.transactionCost(config.getBlockchainConfig(), currentBlock), "TRANSACTION COST");

//                if (config.playVM())
                    vm.play(program);

                result = program.getResult();
//                m_endFee = toBI(tx.getGasLimit()).subtract(toBI(program.getResult().getGasUsed()));
//
                if (result.getException() != null || result.isRevert()) {
                    result.getDeleteAccounts().clear();
                    result.getLogInfoList().clear();
                    result.resetFutureRefund();
                    rollback();

                    if (result.getException() != null) {
                        throw result.getException();
                    } else {
                        execError("REVERT opcode executed");
                    }
                }
            } else {
            		System.out.println("vm = null -- TransactionExecutor.java -- 373");
            }

        } catch (Throwable e) {
            rollback();
//            m_endFee = BigInteger.ZERO;
            execError(e.getMessage());
        }
    }

    private void rollback() {
//        touchedAccounts.remove(tx.getReceiveAddress());
    }

//    public TransactionExecutionSummary finalization() {
//        if (!readyToExecute) return null;
//        TransactionExecutionSummary.Builder summaryBuilder = TransactionExecutionSummary.builderFor(tx)
//                .gasLeftover(m_endGas)
//                .logs(result.getLogInfoList())
//                .result(result.getHReturn());
//
//        if (result != null) {
//            // Accumulate refunds for suicides
//            result.addFutureRefund(result.getDeleteAccounts().size() * config.getBlockchainConfig().
//                    getConfigForBlock(currentBlock.getNumber()).getGasCost().getSUICIDE_REFUND());
//            long gasRefund = Math.min(result.getFutureRefund(), getGasUsed() / 2);
//            byte[] addr = tx.isContractCreation() ? tx.getContractAddress() : tx.getReceiveAddress();
//            m_endGas = m_endGas.add(BigInteger.valueOf(gasRefund));
//
//            summaryBuilder
//                    .gasUsed(toBI(result.getGasUsed()))
//                    .gasRefund(toBI(gasRefund))
//                    .deletedAccounts(result.getDeleteAccounts())
//                    .internalTransactions(result.getInternalTransactions());
//
//            ContractDetails contractDetails = track.getContractDetails(addr);
//            if (contractDetails != null) {
//                // TODO
////                summaryBuilder.storageDiff(track.getContractDetails(addr).getStorage());
////
////                if (program != null) {
////                    summaryBuilder.touchedStorage(contractDetails.getStorage(), program.getStorageDiff());
////                }
//            }
//
//            if (result.getException() != null) {
//                summaryBuilder.markAsFailed();
//            }
//        }
//
//        TransactionExecutionSummary summary = summaryBuilder.build();
//
//        // Refund for gas leftover
//        track.addBalance(tx.getSender(), summary.getLeftover().add(summary.getRefund()));
//        logger.info("Pay total refund to sender: [{}], refund val: [{}]", Hex.toHexString(tx.getSender()), summary.getRefund());
//
//        // Transfer fees to miner
//        track.addBalance(coinbase, summary.getFee());
//        touchedAccounts.add(coinbase);
//        logger.info("Pay fees to miner: [{}], feesEarned: [{}]", Hex.toHexString(coinbase), summary.getFee());
//
//        if (result != null) {
//            logs = result.getLogInfoList();
//            // Traverse list of suicides
//            for (DataWord address : result.getDeleteAccounts()) {
//                track.delete(address.getLast20Bytes());
//            }
//        }
//
//        if (blockchainConfig.eip161()) {
//            for (byte[] acctAddr : touchedAccounts) {
//                AccountState state = track.getAccountState(acctAddr);
//                if (state != null && state.isEmpty()) {
//                    track.delete(acctAddr);
//                }
//            }
//        }
//
//
//        listener.onTransactionExecuted(summary);
//
//        if (config.vmTrace() && program != null && result != null) {
//            String trace = program.getTrace()
//                    .result(result.getHReturn())
//                    .error(result.getException())
//                    .toString();
//
//
//            if (config.vmTraceCompressed()) {
//                trace = zipAndEncode(trace);
//            }
//
//            String txHash = toHexString(tx.getHash());
//            saveProgramTraceFile(txHash, trace);
//            listener.onVMTraceCreated(txHash, trace);
//        }
//        return summary;
//    }

    public TransactionExecutor setLocalCall(boolean localCall) {
        this.localCall = localCall;
        return this;
    }


    public TransactionReceipt getReceipt() {
        if (receipt == null) {
            receipt = new TransactionReceipt();
//            long totalGasUsed = gasUsedInTheBlock + getGasUsed();
//            receipt.setCumulativeGas(totalGasUsed);
            receipt.setTransaction(tx);
            receipt.setLogInfoList(getVMLogs());
//            receipt.setGasUsed(getGasUsed());
            receipt.setExecutionResult(getResult().getHReturn());
            receipt.setError(execError);
////            receipt.setPostTxState(track.getRoot()); // TODO later when RepositoryTrack.getRoot() is implemented
        }
        return receipt;
    }

    public List<LogInfo> getVMLogs() {
        return logs;
    }

    public ProgramResult getResult() {
        return result;
    }

//    public long getGasUsed() {
//        return toBI(tx.getGasLimit()).subtract(m_endGas).longValue();
//    }

}
