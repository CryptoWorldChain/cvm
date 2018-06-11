package org.brewchain.rcvm.service;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.account.core.AccountHelper;
import org.brewchain.account.core.TransactionHelper;
//import org.brewchain.account.gens.Act.Account;
import org.brewchain.evm.api.gens.Act.Account;
import org.brewchain.account.gens.Tx.MultiTransaction;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetRun;
import org.brewchain.cvm.pbgens.Cvm.PSRunContract;
import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.base.MTransaction;
import org.brewchain.rcvm.call.CallTransaction;
import org.brewchain.rcvm.exec.MTransactionHelper;
import org.brewchain.rcvm.exec.TransactionExecutor;
import org.brewchain.rcvm.jsonrpc.TransactionReceipt;
import org.brewchain.rcvm.jsonrpc.TypeConverter;
import org.fc.brewchain.bcapi.EncAPI;
import org.fc.brewchain.bcapi.KeyPairs;

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
    
//    @ActorRequire(name = "evm_api",scope = "global")
//    EvmApi evmApi;
    
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
			KeyPairs cowAcct = encAPI.genKeys(pbo.getFromAddr());
			
			
//            MTransactionHelper.CallArguments callArgs =  mTransactionHelper.genCallArguments(
//            		cowAcct, pbo.getToAddr(), pbo.getFee(), pbo.getFeeLimit(), pbo.getValue(), "");
            
//			// 合约方法、参数类型
//            String[] paramTypes;
//            if(pbo.getParamList() != null) {
//            		paramTypes = new String[pbo.getParamList().size()];
//	            for (int i = 0; i < pbo.getParamList().size(); i++) {
//	            		paramTypes[i] = pbo.getResultList().get(i).getType();
//				}
//            }else {
//            		paramTypes = new String[0];
//            }
//            // 合约方法、返回值类型
//            String[] resultTypes;
//            if(pbo.getResultList() != null) {
//                resultTypes = new String[pbo.getResultList().size()];
//	            for (int i = 0; i < pbo.getResultList().size(); i++) {
//	            		resultTypes[i] = pbo.getResultList().get(i).getType();
//				}
//            }else {
//            		resultTypes = new String[0];
//            }

            Account contractAccount = null;//accountHelper.GetAccount(encAPI.hexDec(pbo.getCAddr()));
            if(contractAccount == null) {
            		throw new IllegalArgumentException("合约"+pbo.getCAddr()+",未找到,hexDec="+encAPI.hexDec(pbo.getCAddr()));
            }
//          callArgs.bin = TypeConverter.toJsonHex(contract.getByName(pbo.getFunName()));
//          callArgs.code = TypeConverter.toJsonHex(contract.getByName(pbo.getFunName()));
		
            // TODO Test
//            System.out.println(contractAccount.getValue().getCode().toStringUtf8());
            
            CallTransaction.Contract contract = new CallTransaction.Contract(contractAccount.getValue().getCode().toStringUtf8());
            CallTransaction.Function fun = contract.getByName(pbo.getFunName());
            if(fun  == null) {
            		throw new IllegalArgumentException("合约方法"+pbo.getFunName()+",未找到");
            }
            byte[] functionCallBytes;
            if (StringUtils.isNotBlank(pbo.getData())) {
                Object[] dataArray = pbo.getData().split(",");
                functionCallBytes = fun.encode(dataArray);
            }else {
            		functionCallBytes = fun.encode();
            }
            
//            CallTransaction.Function function = CallTransaction.Function.fromSignature(pbo.getFunName(), paramTypes, resultTypes);
            
            long fee = 0L;
			long feeLimit = 0L;
			if(pbo.getFee() > 0) {
				fee = pbo.getFee();
				feeLimit = fee;
				if(pbo.getFeeLimit() > 0) {
					feeLimit = pbo.getFeeLimit  ();
				}
			}
			
            MTransaction mtx = new MTransaction(accountHelper);
			mtx.addTXInput(pbo.getFromAddr(), pbo.getPubKey(), pbo.getSign(), 0L, fee, feeLimit);
			mtx.addTXOutput(null, 0L);
			mtx.setTXBodyData(functionCallBytes, pbo.getCAddr().getBytes());
			mtx.txSign(encAPI, cowAcct.getPubkey(), cowAcct.getPrikey());
			MultiTransaction.Builder tx = mtx.genTX();
			
//            String ret_info = mTransactionHelper.call(callArgs);
			
			TransactionExecutor executor = new TransactionExecutor(tx).withCommonConfig().setLocalCall(true);

//            executor.init(null,functionCallBytes);
			executor.init(contractAccount.getValue().getCodeHash().toByteArray(),functionCallBytes);
            executor.execute();
            executor.go();
//            executor.finalization();
            TransactionReceipt res = executor.getReceipt();
            
//			String ret_info = mTransactionHelper.call(callArgs);
			ret.setRetCode(0);
			ret.setRunInfo(TypeConverter.toJsonHex(res.getExecutionResult()));
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
			ret.setRetMessage(e.getMessage());
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

//		if (StringUtils.isBlank(pb.getFromAddr())) {
//			throw new IllegalArgumentException("参数from_addr,不能为空");
//		}
//		
//		if (StringUtils.isBlank(pb.getPubKey())) {
//			throw new IllegalArgumentException("参数pub_key,不能为空");
//		}
//
//		if(StringUtils.isBlank(pb.getToAddr())) {
//			throw new IllegalArgumentException("参数to,不能为空");
//		}
		
//		if (StringUtils.isBlank(pb.getData())) {
//			throw new IllegalArgumentException("参数data,不能为空");
//		}
		
//		if(pb.getFee()<=0) {
//			throw new IllegalArgumentException("参数fee,不能为空");
//		}
		
//		if(pb.getFeeLimit()<=0) {
//			throw new IllegalArgumentException("参数fee_limit,不能为空");
//		}
		
//		if(pb.getValue()<=0) {
//			throw new IllegalArgumentException("参数value,不能为空");
//		}

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
