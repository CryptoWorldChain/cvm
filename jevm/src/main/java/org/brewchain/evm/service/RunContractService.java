package org.brewchain.evm.service;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.account.core.AccountHelper;
import org.brewchain.account.core.TransactionHelper;
import org.brewchain.account.gens.Act.Contract;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetRun;
import org.brewchain.cvm.pbgens.Cvm.PSRunContract;
import org.brewchain.evm.call.CallTransaction;
import org.brewchain.evm.exec.MTransactionHelper;
import org.brewchain.evm.jsonrpc.TypeConverter;
import org.fc.brewchain.bcapi.EncAPI;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import onight.oapi.scala.commons.SessionModules;
import onight.osgi.annotation.NActorProvider;
import onight.tfw.async.CompleteHandler;
import onight.tfw.ntrans.api.annotation.ActorRequire;
import onight.tfw.otransio.api.PacketHelper;
import onight.tfw.otransio.api.beans.FramePacket;

@NActorProvider
@Data
@Slf4j
public class RunContractService extends SessionModules<PSRunContract> {

	// @ActorRequire
	// CommonService commonService;
	
    @ActorRequire(name = "bc_encoder",scope = "global")
	EncAPI encAPI;
    
//    @ActorRequire(name = "mTransaction_Helper", scope = "global")
    MTransactionHelper mTransactionHelper = new MTransactionHelper();


	@ActorRequire(name = "Account_Helper", scope = "global")
	AccountHelper accountHelper;
	
	@ActorRequire(name = "Transaction_Helper", scope = "global")
	TransactionHelper transactionHelper;
    
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
			
			// 创建合约交易，无用
			String cowAcct = encAPI.genKeys(pbo.getFromAddr()).getAddress();
//			MTransactionHelper.CallArguments callArgs =  mTransactionHelper.genCallArguments(
//					cowAcct, pbo.getToAddr(), pbo.getFee(), pbo.getFeeLimit(), pbo.getValue(), pbo.getCode());
//			MTransactionHelper.CompilationResult compRes = mTransactionHelper.compileSolidity(callArgs.code);
//			log.info("compRes.bin.length()>10?="+compRes.bin.length());
//            callArgs.bin=compRes.bin;
//            String txHash = mTransactionHelper.sendTransaction(callArgs);
            
			// 获取 创建合约交易 所在块的hash，无用
//            String hash = mineBlock();
//            MTransactionHelper.BlockResult blockResult = mTransactionHelper.getBlockByHash(hash, true);
//            TransactionReceiptDTO receipt2 = mTransactionHelper.getTransactionReceipt(txHash);
//// //           (hash.equals(blockResult2.hash));
//// //          (txHash.equals(((TransactionResultDTO) blockResult2.transactions[0]).hash));
//// //           (receipt2.blockNumber > 1);
//// //           (receipt2.gasUsed > 0);
//// //           (sGas == receipt2.gasUsed);
//// //           (TypeConverter.StringHexToByteArray(receipt2.contractAddress).length == 20);
            
            MTransactionHelper.CallArguments callArgs =  mTransactionHelper.genCallArguments(
            		cowAcct, pbo.getToAddr(), pbo.getFee(), pbo.getFeeLimit(), pbo.getValue(), "");
            
            
			// 合约方法、参数类型
            String[] paramTypes;
            if(pbo.getParamList() != null) {
            		paramTypes = new String[pbo.getParamList().size()];
	            for (int i = 0; i < pbo.getParamList().size(); i++) {
	            		paramTypes[i] = pbo.getResultList().get(i).getType();
				}
            }else {
            		paramTypes = new String[0];
            }
            // 合约方法、返回值类型
            String[] resultTypes;
            if(pbo.getResultList() != null) {
                resultTypes = new String[pbo.getResultList().size()];
	            for (int i = 0; i < pbo.getResultList().size(); i++) {
	            		resultTypes[i] = pbo.getResultList().get(i).getType();
				}
            }else {
            		resultTypes = new String[0];
            }
            
            // TODO 
            Contract contract = accountHelper.GetContract(encAPI.hexDec(pbo.getCAddr()));
            
            contract.getValue().getData();
            
            CallTransaction.Function function = CallTransaction.Function.fromSignature(pbo.getFunName(), paramTypes, resultTypes);
//            Transaction rawTx = ethereum.createTransaction(valueOf(2),
//                    valueOf(pbo.getGasPrice()),
//                    valueOf(pbo.getGas()),
//                    TypeConverter.StringHexToByteArray(receipt2.contractAddress),
//                    valueOf(0), function.encode(callArgs.value));
//            rawTx.sign(sha3(pbo.getCode().getBytes()));
//            String txHash3 = mTransactionHelper.sendRawTransaction(TypeConverter.toJsonHex(rawTx.getEncoded()));

            callArgs.bin = TypeConverter.toJsonHex(function.encode());

            String ret_info = mTransactionHelper.call(callArgs);
            
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

		if (StringUtils.isBlank(pb.getCAddr())) {
			throw new IllegalArgumentException("参数c_addr,不能为空");
		}

		if (StringUtils.isBlank(pb.getFunName())) {
			throw new IllegalArgumentException("参数fun_name,不能为空");
		}

		if (StringUtils.isBlank(pb.getFromAddr())) {
			throw new IllegalArgumentException("参数from_addr,不能为空");
		}
		
		if (StringUtils.isBlank(pb.getPubKey())) {
			throw new IllegalArgumentException("参数pub_key,不能为空");
		}

		if(StringUtils.isBlank(pb.getToAddr())) {
			throw new IllegalArgumentException("参数to,不能为空");
		}
		
//		if (StringUtils.isBlank(pb.getData())) {
//			throw new IllegalArgumentException("参数data,不能为空");
//		}
		
//		if(pb.getFee()<=0) {
//			throw new IllegalArgumentException("参数fee,不能为空");
//		}
		
//		if(pb.getFeeLimit()<=0) {
//			throw new IllegalArgumentException("参数fee_limit,不能为空");
//		}
		
		if(pb.getValue()<=0) {
			throw new IllegalArgumentException("参数value,不能为空");
		}

	}
	
//	String mineBlock() throws InterruptedException {
//        String blockFilterId = jsonRpc.eth_newBlockFilter();
//        jsonRpc.miner_start();
//        int cnt = 0;
//        String hash1;
//        while (true) {
//            Object[] blocks = jsonRpc.eth_getFilterChanges(blockFilterId);
//            cnt += blocks.length;
//            if (cnt > 0) {
//                hash1 = (String) blocks[0];
//                break;
//            }
//            Thread.sleep(100);
//        }
//        jsonRpc.miner_stop();
//        Thread.sleep(100);
//        Object[] blocks = jsonRpc.eth_getFilterChanges(blockFilterId);
//        cnt += blocks.length;
//        log.info(cnt + " blocks mined");
//        boolean b = jsonRpc.eth_uninstallFilter(blockFilterId);
//        log.info("jsonRpc.eth_uninstallFilter(blockFilterId)="+b);
//        return hash1;
//    }
}
