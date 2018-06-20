package org.brewchain.rcvm.exec;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;
import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.exec.tx.MultiTransactionTask;
import org.brewchain.rcvm.jsonrpc.TransactionReceipt;
import org.brewchain.rcvm.jsonrpc.TypeConverter;
//import org.brewchain.rcvm.jsonrpc.JsonRpc.CallArguments;
//import org.brewchain.rcvm.jsonrpc.JsonRpc.CompilationInfo;
//import org.brewchain.rcvm.jsonrpc.JsonRpcImpl.BinaryCallArguments;
import org.brewchain.rcvm.solidity.compiler.SolidityCompiler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MTransactionHelper {

//	@ActorRequire(name = "Transaction_Helper", scope = "global")
//	TransactionHelper transactionHelper;
//
//	@ActorRequire(name = "Account_Helper", scope = "global")
//	AccountHelper accountHelper;
	
//	EvmApi accountHelper;
	
	SolidityCompiler solidityCompiler;
	
    public CallArguments genCallArguments(String from,String to,long fee,long feeLimit,long value,String code) {
    		CallArguments callArgs = new CallArguments();
    		
    		if(from.startsWith("0x")) {
        		callArgs.from = from;
        }else {
        		callArgs.from = "0x"+from;
        }
    		if(to.startsWith("0x")) {
        		callArgs.to = to;
        }else {
        		callArgs.to = "0x"+to;
        }
        callArgs.fee = "0x"+fee;
        callArgs.feeLimit = "0x"+feeLimit;
        callArgs.value = "0x"+value;
        if(code.startsWith("0x")) {
        		callArgs.code = code;
        }else {
        		callArgs.code = "0x"+code;
        }
    		return callArgs;
    }
    

	public void submitTransaction(MultiTransaction.Builder transaction,EvmApi transactionHelper) {
		
		MultiTransactionTask txTask = new MultiTransactionTask(transaction, transactionHelper);
		
	//    final Future<List<Transaction>> listFuture =
	//            TransactionExecutor.instance.submitTransaction(txTask);
	    
	//    pendingState.addPendingTransaction(transaction);
	//    return new FutureAdapter<Transaction, List<Transaction>>(listFuture) {
	//        @Override
	//        protected Transaction adapt(List<Transaction> adapteeResult) throws ExecutionException {
	//            return adapteeResult.get(0);
	//        }
	//    };
	}
	
//	public String call(CallArguments args) throws Exception {
//
//        String s = null;
//        try {
//            TransactionReceipt res = createCallTxAndExecute(args);
//            return s = TypeConverter.toJsonHex(res.getExecutionResult());
//        } finally {
//            if (log.isDebugEnabled()) log.debug("eth_call(" + args + "): " + s);
//        }
//    }
//	private TransactionReceipt createCallTxAndExecute(CallArguments args) throws Exception {
//        BinaryCallArguments bca = new BinaryCallArguments();
//        bca.setArguments(args);
//        
//        // TODO
//        MultiTransaction tx = null;
////        MultiTransaction tx = CallTransaction.createRawTransaction(0,
////                bca.gasPrice,
////                bca.gasLimit,
////                bca.toAddress,
////                bca.value,
////                bca.data);
//
//        // put mock signature if not present
////        if (tx.getSignature() == null) {
////            tx.sign(ECKey.fromPrivate(new byte[32]));
////        }
//
//        try {
////            TransactionExecutor executor = new TransactionExecutor
////                    (tx, block.getCoinbase(), repository, blockStore,
////                            programInvokeFactory, block, new EthereumListenerAdapter(), 0)
////                    .withCommonConfig(commonConfig)
////                    .setLocalCall(true);
//        	TransactionExecutor executor = new TransactionExecutor(tx, encApi).withCommonConfig().setLocalCall(true);
//
//            executor.init(null,null);
//            executor.execute();
//            executor.go();
////            executor.finalization();
//            return executor.getReceipt();
//        } finally {
////            repository.rollback();
//        }
//    }
//    
	
//    public CompilationResult compileSolidity(String contract) throws Exception {
//        CompilationResult s = null;
//        try {
//            SolidityCompiler.Result res = solidityCompiler.compileSrc(contract.getBytes(), true, true, SolidityCompiler.Options.ABI, SolidityCompiler.Options.BIN);
//            if (res.isFailed()) {
//                throw new RuntimeException("Compilation error: " + res.errors);
//            }
//            org.brewchain.rcvm.solidity.compiler.CompilationResult result = org.brewchain.rcvm.solidity.compiler.CompilationResult.parse(res.output);
//            CompilationResult ret = new CompilationResult();
//            org.brewchain.rcvm.solidity.compiler.CompilationResult.ContractMetadata contractMetadata = result.contracts.values().iterator().next();
//            ret.bin = toJsonHex(contractMetadata.bin);
//            ret.info = new CompilationInfo();
//            ret.info.source = contract;
//            ret.info.language = "Solidity";
//            ret.info.languageVersion = "0";
//            ret.info.compilerVersion = result.version;
//            ret.info.abiDefinition = new CallTransaction.Contract(contractMetadata.abi).functions;
//            return s = ret;
//        } finally {
//            if (log.isDebugEnabled()) log.debug("compileSolidity(" + contract + ")" + s);
//        }
//    }
//	
//	Map<ByteArrayWrapper, Account.Builder> accounts = new HashMap<>();
//	public Account.Builder getAccount(String address) throws Exception {
//        return accounts.get(new ByteArrayWrapper(StringHexToByteArray(address)));
//    }
	
	//创建合约交易，无用
//	public String sendTransaction(CallArguments args) throws Exception {
//
//        String s = null;
//        try {
//            Account.Builder account = getAccount(args.from);
//
//            if (account == null)
//                throw new Exception("From address private key could not be found in this node");
//
//            if (args.data != null && args.data.startsWith("0x"))
//                args.data = args.data.substring(2);
//
//            MTransaction mt = new MTransaction(accountHelper);
//            
//            mt.addTXInput(address, pubKey, sign, value, fee, feeLimit);
//            mt.addTXOutput(address, value);
//            MultiTransaction tx = mt.genTX(data, exData);
//            
////            MultiTransaction tx = new MultiTransaction(
////                    args.nonce != null ? StringHexToByteArray(args.nonce) : ByteUtil.bigIntegerToBytes(pendingState.getRepository().getNonce(account.getAddress())),
////                    args.gasPrice != null ? StringHexToByteArray(args.gasPrice) : ByteUtil.longToBytesNoLeadZeroes(eth.getGasPrice()),
////                    args.gas != null ? StringHexToByteArray(args.gas) : ByteUtil.longToBytes(90_000),
////                    args.to != null ? StringHexToByteArray(args.to) : ByteUtil.EMPTY_BYTE_ARRAY,
////                    args.value != null ? StringHexToByteArray(args.value) : ByteUtil.EMPTY_BYTE_ARRAY,
////                    args.data != null ? StringHexToByteArray(args.data) : ByteUtil.EMPTY_BYTE_ARRAY,
////                    eth.getChainIdForNextBlock());
////            tx.sign(account.getEcKey().getPrivKeyBytes());
//
////          args.nonce != null ? StringHexToByteArray(args.nonce) : ByteUtil.bigIntegerToBytes(pendingState.getRepository().getNonce(account.getAddress()));
////          args.gasPrice != null ? StringHexToByteArray(args.gasPrice) : ByteUtil.longToBytesNoLeadZeroes(eth.getGasPrice());
////          args.gas != null ? StringHexToByteArray(args.gas) : ByteUtil.longToBytes(90_000);
////          args.to != null ? StringHexToByteArray(args.to) : ByteUtil.EMPTY_BYTE_ARRAY;
////          args.value != null ? StringHexToByteArray(args.value) : ByteUtil.EMPTY_BYTE_ARRAY;
////          args.data != null ? StringHexToByteArray(args.data) : ByteUtil.EMPTY_BYTE_ARRAY;
//            
//            MultiTransaction.Builder tx = MultiTransaction.newBuilder();
//            
//            // TODO set tx object
//            
//            submitTransaction(tx,transactionHelper);
//
//            return s = TypeConverter.toJsonHex(tx.getTxHash().toByteArray());
//        } finally {
//            if (log.isDebugEnabled()) log.debug("eth_sendTransaction(" + args + "): " + s);
//        }
//    }

    
    
    
	public class CallArguments {
        public String from;
        public String to;
        public String fee;
        public String feeLimit;
        public String value;
        public String code; // contract
        public String bin; // compiledCode
        @Getter
        private String nonce;

        	public void setNonce(String nonce) {
        		if(StringUtils.isNotBlank(this.nonce)) {
        			if(this.nonce.startsWith("0x")) {
        				this.nonce = nonce;
        			}else {
        				this.nonce = "0x"+nonce;
        			}
        		}
        	}
        	
        	public void setNonce(int nonce) {
        		setNonce(nonce+"");
        	}
        
        @Override
        public String toString() {
            return "CallArguments{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", fee='" + fee + '\'' +
                    ", feeLimit='" + feeLimit + '\'' +
                    ", value='" + value + '\'' +
                    ", nonce='" + nonce + '\'' +
                    ", code='" + code + '\'' +
                    ", bin='" + bin + '\'' +
                    '}';
        }
	}
	
	public class BinaryCallArguments {
        public int nonce;
        public long fee;
        public long feeLimit;
        public String toAddress;
        public long value;
        public byte[] bin;
        public void setArguments(CallArguments args) throws Exception {
            nonce = 0;
            if (args.nonce != null && args.nonce.length() != 0)
                nonce = JSonHexToInt(args.nonce);

            fee = 0;
            if (args.fee != null && args.fee.length()!=0)
                fee = JSonHexToLong(args.fee);

            feeLimit = 4_000_000;
            if (args.feeLimit != null && args.feeLimit.length()!=0)
            		feeLimit = JSonHexToLong(args.feeLimit);

            toAddress = null;
            if (args.to != null && !args.to.isEmpty())
                toAddress = JSonHexToHex(args.to);

            value=0;
            if (args.value != null && args.value.length()!=0)
                value = JSonHexToLong(args.value);

            bin = null;
            if (args.bin != null && args.bin.length()!=0)
            		bin = TypeConverter.StringHexToByteArray(args.bin);
        }
    }
	
	public long JSonHexToLong(String x) throws Exception {
        if (!x.startsWith("0x"))
            throw new Exception("Incorrect hex syntax");
        x = x.substring(2);
        return Long.parseLong(x, 16);
    }

    public int JSonHexToInt(String x) throws Exception {
        if (!x.startsWith("0x"))
            throw new Exception("Incorrect hex syntax");
        x = x.substring(2);
        return Integer.parseInt(x, 16);
    }

    public String JSonHexToHex(String x) throws Exception {
        if (!x.startsWith("0x"))
            throw new Exception("Incorrect hex syntax");
        x = x.substring(2);
        return x;
    }
    
//	public class CompilationResult {
//        public String bin;
//        public CompilationInfo info;
//
//        @Override
//        public String toString() {
//            return "CompilationResult{" +
//                    "bin='" + bin + '\'' +
//                    ", info=" + info +
//                    '}';
//        }
//    }
	
//	public class BlockResult {
//        public String number; // QUANTITY - the block number. null when its pending block.
//        public String hash; // DATA, 32 Bytes - hash of the block. null when its pending block.
//        public String parentHash; // DATA, 32 Bytes - hash of the parent block.
//        public String nonce; // DATA, 8 Bytes - hash of the generated proof-of-work. null when its pending block.
//        public String sha3Uncles; // DATA, 32 Bytes - SHA3 of the uncles data in the block.
//        public String logsBloom; // DATA, 256 Bytes - the bloom filter for the logs of the block. null when its pending block.
//        public String transactionsRoot; // DATA, 32 Bytes - the root of the transaction trie of the block.
//        public String stateRoot; // DATA, 32 Bytes - the root of the final state trie of the block.
//        public String receiptsRoot; // DATA, 32 Bytes - the root of the receipts trie of the block.
//        public String miner; // DATA, 20 Bytes - the address of the beneficiary to whom the mining rewards were given.
//        public String difficulty; // QUANTITY - integer of the difficulty for this block.
//        public String totalDifficulty; // QUANTITY - integer of the total difficulty of the chain until this block.
//        public String extraData; // DATA - the "extra data" field of this block
//        public String size;//QUANTITY - integer the size of this block in bytes.
//        public String feeLimit;//: QUANTITY - the maximum gas allowed in this block.
//        public String feeUsed; // QUANTITY - the total used gas by all transactions in this block.
//        public String timestamp; //: QUANTITY - the unix timestamp for when the block was collated.
//        public Object[] transactions; //: Array - Array of transaction objects, or 32 Bytes transaction hashes depending on the last given parameter.
//        public String[] uncles; //: Array - Array of uncle hashes.
//
//        @Override
//        public String toString() {
//            return "BlockResult{" +
//                    "number='" + number + '\'' +
//                    ", hash='" + hash + '\'' +
//                    ", parentHash='" + parentHash + '\'' +
//                    ", nonce='" + nonce + '\'' +
//                    ", sha3Uncles='" + sha3Uncles + '\'' +
//                    ", logsBloom='" + logsBloom + '\'' +
//                    ", transactionsRoot='" + transactionsRoot + '\'' +
//                    ", stateRoot='" + stateRoot + '\'' +
//                    ", receiptsRoot='" + receiptsRoot + '\'' +
//                    ", miner='" + miner + '\'' +
//                    ", difficulty='" + difficulty + '\'' +
//                    ", totalDifficulty='" + totalDifficulty + '\'' +
//                    ", extraData='" + extraData + '\'' +
//                    ", size='" + size + '\'' +
//                    ", feeLimit='" + feeLimit + '\'' +
//                    ", feeUsed='" + feeUsed + '\'' +
//                    ", timestamp='" + timestamp + '\'' +
//                    ", transactions=" + Arrays.toString(transactions) +
//                    ", uncles=" + Arrays.toString(uncles) +
//                    '}';
//        }
//    }
	
}
