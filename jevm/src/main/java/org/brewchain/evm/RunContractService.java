package org.brewchain.evm;

import static java.math.BigInteger.valueOf;
import static org.ethereum.crypto.HashUtil.sha3;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetRun;
import org.brewchain.cvm.pbgens.Cvm.PSRunContract;
import org.ethereum.core.CallTransaction;
import org.ethereum.core.Transaction;
import org.ethereum.facade.Ethereum;
import org.ethereum.jsonrpc.JsonRpc;
import org.ethereum.jsonrpc.TransactionReceiptDTO;
import org.ethereum.jsonrpc.TypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import onight.oapi.scala.commons.SessionModules;
import onight.osgi.annotation.NActorProvider;
import onight.tfw.async.CompleteHandler;
import onight.tfw.otransio.api.PacketHelper;
import onight.tfw.otransio.api.beans.FramePacket;

@NActorProvider
@Data
@Slf4j
public class RunContractService extends SessionModules<PSRunContract> {

	// @ActorRequire
	// CommonService commonService;
	
	@Autowired
    JsonRpc jsonRpc;

    @Autowired
    Ethereum ethereum;

	@Override
	public String getModule() {
		return PModule.CVM.name();
	}

	@Override
	public String[] getCmds() {
		return new String[] { PCommand.RUC.name() };
	}

	public String toString() {
		return "RunContractService";
	}

	@Override
	public void onPBPacket(FramePacket pack, PSRunContract pbo, CompleteHandler handler) {
		final PRetRun.Builder ret = PRetRun.newBuilder();
		try {
			checkNull(pbo);
			
			String cowAcct = jsonRpc.personal_newAccount(pbo.getFrom());
//			
//            String bal0 = jsonRpc.eth_getBalance(cowAcct);
//            
//            if(TypeConverter.StringHexToBigInteger(bal0).compareTo(BigInteger.ZERO) <= 0) {
//            	log.warn("TypeConverter.StringHexToBigInteger(bal0).compareTo(BigInteger.ZERO) <= 0");
//            }
//
//            String pendingTxFilterId = jsonRpc.eth_newPendingTransactionFilter();
//            Object[] changes = jsonRpc.eth_getFilterChanges(pendingTxFilterId);
//            
//            JsonRpc.CallArguments ca = new JsonRpc.CallArguments();
//            
//            
//            long sGas = TypeConverter.StringHexToBigInteger(jsonRpc.eth_estimateGas(ca)).longValue();
//
//            String txHash1 = jsonRpc.eth_sendTransaction(cowAcct,ca.to, ca.gas,ca.gasPrice, ca.value, ca.data, ca.nonce);
//            log.info("Tx hash: " + txHash1);
//            
//            if(TypeConverter.StringHexToBigInteger(txHash1).compareTo(BigInteger.ZERO) <= 0) {
//            		log.warn("TypeConverter.StringHexToBigInteger(txHash1).compareTo(BigInteger.ZERO) <= 0");
//            }
//            
//            for (int i = 0; i < 50 && changes.length == 0; i++) {
//                changes = jsonRpc.eth_getFilterChanges(pendingTxFilterId);
//                Thread.sleep(200);
//            }
//            
//            log.info("changes.length="+changes.length);
//            
//            changes = jsonRpc.eth_getFilterChanges(pendingTxFilterId);
//            
//            log.info("changes.length="+changes.length);
//
//            JsonRpc.BlockResult blockResult = jsonRpc.eth_getBlockByNumber("pending", true);
//            
//            log.info("blockResult="+blockResult);
//            
//            if(((TransactionResultDTO) blockResult.transactions[0]).hash.equals(txHash1)) {
//            		log.warn("((TransactionResultDTO) blockResult.transactions[0]).hash.equals(txHash1)");
//            }

            

            JsonRpc.CallArguments callArgs = new JsonRpc.CallArguments();
            callArgs.from = cowAcct;
            // 0x0000000000000000000000000000000000001234
            callArgs.to = pbo.getTo();
            callArgs.gas = "0x"+pbo.getGas();
            callArgs.gasPrice = "0x"+pbo.getGasPrice();
            callArgs.value = "0x"+pbo.getValue();
            
            JsonRpc.CompilationResult compRes = jsonRpc.eth_compileSolidity(pbo.getCode());
            log.info("compRes.code.length()>10?="+compRes.code.length());
            callArgs.data=compRes.code;
            
            String txHash = jsonRpc.eth_sendTransaction(callArgs);
            long sGas = TypeConverter.StringHexToBigInteger(jsonRpc.eth_estimateGas(callArgs)).longValue();

            String hash = mineBlock();

            JsonRpc.BlockResult blockResult = jsonRpc.eth_getBlockByHash(hash, true);
// //           (hash.equals(blockResult2.hash));
// //          (txHash.equals(((TransactionResultDTO) blockResult2.transactions[0]).hash));
            TransactionReceiptDTO receipt2 = jsonRpc.eth_getTransactionReceipt(txHash);
// //           (receipt2.blockNumber > 1);
// //           (receipt2.gasUsed > 0);
// //           (sGas == receipt2.gasUsed);
// //           (TypeConverter.StringHexToByteArray(receipt2.contractAddress).length == 20);

//            JsonRpc.FilterRequest filterReq = new JsonRpc.FilterRequest();
//            filterReq.topics = new Object[]{"0x2222"};
//            filterReq.fromBlock = "latest";
//            filterReq.toBlock = "latest";
//            String filterId = jsonRpc.eth_newFilter(filterReq);

            
//            CallTransaction.Function function = CallTransaction.Function.fromSignature("set", "uint");
            CallTransaction.Function function = CallTransaction.Function.fromSignature(pbo.getFunName());
            
            Transaction rawTx = ethereum.createTransaction(valueOf(2),
                    valueOf(pbo.getGasPrice()),
                    valueOf(pbo.getGas()),
                    TypeConverter.StringHexToByteArray(receipt2.contractAddress),
                    valueOf(0), function.encode(callArgs.value));
            
            // 签名
            rawTx.sign(sha3(pbo.getCode().getBytes()));

//            String txHash3 = jsonRpc.eth_sendRawTransaction(TypeConverter.toJsonHex(rawTx.getEncoded()));
//
//            JsonRpc.CallArguments callArgs2= new JsonRpc.CallArguments();
//            callArgs2.to = receipt2.contractAddress;
//            callArgs2.data = TypeConverter.toJsonHex(CallTransaction.Function.fromSignature("num").encode());
//
//            String ret3 = jsonRpc.eth_call(callArgs2, "pending");
//            String ret4 = jsonRpc.eth_call(callArgs2, "latest");
//
//            String hash3 = mineBlock();
//
//            JsonRpc.BlockResult blockResult3 = jsonRpc.eth_getBlockByHash(hash3, true);
//            assertEquals(hash3, blockResult3.hash);
//            assertEquals(txHash3, ((TransactionResultDTO) blockResult3.transactions[0]).hash);
//            TransactionReceiptDTO receipt3 = jsonRpc.eth_getTransactionReceipt(txHash3);
//            assertTrue(receipt3.blockNumber > 2);
//            assertTrue(receipt3.gasUsed > 0);
//
//            Object[] logs = jsonRpc.eth_getFilterLogs(filterId);
//            assertEquals(1, logs.length);
//            assertEquals("0x0000000000000000000000000000000000000000000000000000000000001111",
//                    ((JsonRpc.LogFilterElement)logs[0]).data);
//            assertEquals(0, jsonRpc.eth_getFilterLogs(filterId).length);

//          String ret_info = jsonRpc.eth_call(callArgs, "latest");
            String ret_info = jsonRpc.eth_call(callArgs, blockResult.number);
            
			ret.setRetCode(0);
			ret.setRunInfo(ret_info);
			ret.setRetMessage("");
			
		} catch (IllegalArgumentException e) {
			ret.setRetCode(-1);
			ret.setRetMessage(e.getMessage());
			log.error("error：：" + e.getMessage());
		} catch (UnknownError e) {
			ret.setRetCode(-1);
			ret.setRetMessage(e.getMessage());
			log.error("error：：" + e.getMessage());
		} catch (Exception e) {
			ret.setRetCode(-1);
			ret.setRetMessage("参数值错误");
		} finally {

		}
		handler.onFinished(PacketHelper.toPBReturn(pack, ret.build()));
	}

	public void checkNull(PSRunContract pb) {
		if (pb == null) {
			throw new IllegalArgumentException("无请求参数");
		}

		if (StringUtils.isBlank(pb.getCode())) {
			throw new IllegalArgumentException("参数code,不能为空");
		}

		if (StringUtils.isBlank(pb.getFunName())) {
			throw new IllegalArgumentException("参数fun_name,不能为空");
		}

		if (StringUtils.isBlank(pb.getFrom())) {
			throw new IllegalArgumentException("参数from,不能为空");
		}

		if(StringUtils.isBlank(pb.getTo())) {
			throw new IllegalArgumentException("参数to,不能为空");
		}
		
//		if (StringUtils.isBlank(pb.getData())) {
//			throw new IllegalArgumentException("参数data,不能为空");
//		}
		
		if(pb.getGas()<=0) {
			throw new IllegalArgumentException("参数gas,不能为空");
			}
		
		if(pb.getGasPrice()<=0) {
			throw new IllegalArgumentException("参数gas_price,不能为空");
		}
		
		if(pb.getValue()<=0) {
			throw new IllegalArgumentException("参数value,不能为空");
		}

	}
	
	String mineBlock() throws InterruptedException {
        String blockFilterId = jsonRpc.eth_newBlockFilter();
        jsonRpc.miner_start();
        int cnt = 0;
        String hash1;
        while (true) {
            Object[] blocks = jsonRpc.eth_getFilterChanges(blockFilterId);
            cnt += blocks.length;
            if (cnt > 0) {
                hash1 = (String) blocks[0];
                break;
            }
            Thread.sleep(100);
        }
        jsonRpc.miner_stop();
        Thread.sleep(100);
        Object[] blocks = jsonRpc.eth_getFilterChanges(blockFilterId);
        cnt += blocks.length;
        log.info(cnt + " blocks mined");
        boolean b = jsonRpc.eth_uninstallFilter(blockFilterId);
        log.info("jsonRpc.eth_uninstallFilter(blockFilterId)="+b);
        return hash1;
    }
}
