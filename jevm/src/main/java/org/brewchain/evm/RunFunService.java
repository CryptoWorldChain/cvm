package org.brewchain.evm;

import static org.brewchain.core.crypto.HashUtil.sha3;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.core.core.CallTransaction;
import org.brewchain.core.core.Transaction;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PMFunPut;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetRun;
import org.brewchain.cvm.pbgens.Cvm.PSRunFun;
import org.spongycastle.util.encoders.Hex;

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
public class RunFunService extends SessionModules<PSRunFun> {

	// @ActorRequire
	// CommonService commonService;

	@Override
	public String getModule() {
		return PModule.CVM.name();
	}

	@Override
	public String[] getCmds() {
		return new String[] { PCommand.RUF.name() };
	}

	public String toString() {
		return "RunFunService";
	}

	@Override
	public void onPBPacket(FramePacket pack, PSRunFun pbo, CompleteHandler handler) {
		final PRetRun.Builder ret = PRetRun.newBuilder();
		try {
			checkNull(pbo);
			
			
			String funcJson = "{ \n" ;
			
			if(pbo.getConstant()) {
				funcJson += "  'constant': true, \n";
			}else {
				funcJson += "  'constant': false, \n";
			}
			//{'name':'to', 'type':'address'}
			
			funcJson += "  'inputs': [\n";
			if(pbo.getInputsList() != null) {
				for(PMFunPut put : pbo.getInputsList()) {
					if(StringUtils.isNotBlank(put.getName()) || StringUtils.isNotBlank(put.getType())) {
						funcJson += "{'name':'"+put.getName()+"', 'type':'"+put.getType()+"'}\n";
					}
				}
			}
			funcJson +=  "], \n";
			
			funcJson += "  'outputs': [\n";
			if(pbo.getOutputsList() != null) {
				for(PMFunPut put : pbo.getOutputsList()) {
					if(StringUtils.isNotBlank(put.getName()) || StringUtils.isNotBlank(put.getType())) {
						funcJson += "{'name':'"+put.getName()+"', 'type':'"+put.getType()+"'}\n";
					}
				}
			}	
			funcJson += "], \n";
			
			funcJson += "  'name': '"+pbo.getName()+"', \n";
			if(StringUtils.isBlank(pbo.getType())) {
				funcJson += "  'type': 'function' \n";
			}else {
				funcJson += "  'type': '"+pbo.getType()+"' \n";
			}
			funcJson += "} \n";
			
			funcJson = funcJson.replaceAll("'", "\"");
			
			log.info("funcJson="+funcJson);
			
			long gasLimit = pbo.getGasLimit();
			if(gasLimit <= 0) {
				gasLimit = pbo.getGasPrice();//1_000_000_000
			}
			CallTransaction.Function function = CallTransaction.Function.fromJsonInterface(funcJson);
			
			// to_addr test = "86e0497e32a8e1d79fe38ab87dc80140df5470d9"
			// long nonce, long gasPrice, long gasLimit, String toAddress, long value, Function callFunc, Object... funcArgs
			Transaction ctx = CallTransaction.createCallTransaction(1, pbo.getGasPrice(), gasLimit,pbo.getToAddr(), 0, function);
	        
			//sha3("974f963ee4571e86e5f9bc3b493e453db9c15e5bd19829a4ef9a790de0da0015".getBytes())
			ctx.sign(sha3(pbo.getCtxSign().getBytes()));//合约交易签名

			ret.setRetCode(0);
			ret.setRetMessage("");
			ret.setRunInfo(Hex.toHexString(ctx.getData()));
	        
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
			log.error("error：：" + e.getMessage());
		} finally {

		}
		handler.onFinished(PacketHelper.toPBReturn(pack, ret.build()));
	}

	public void checkNull(PSRunFun pb) {
		if (pb == null) {
			throw new IllegalArgumentException("无请求参数");
		}

		if (StringUtils.isBlank(pb.getName())) {
			throw new IllegalArgumentException("参数name,不能为空");
		}

//		if (pb.getInputsList() == null || pb.getInputsList().size() == 0) {
//			throw new IllegalArgumentException("参数inputs,不能为空");
//		}
//		
//		if (pb.getOutputsList() == null || pb.getOutputsList().size() == 0) {
//			throw new IllegalArgumentException("参数outputs,不能为空");
//		}

		if (StringUtils.isBlank(pb.getToAddr())) {
			throw new IllegalArgumentException("参数to_addr,不能为空");
		}
		
		if (StringUtils.isBlank(pb.getCtxSign())) {
			throw new IllegalArgumentException("参数ctx_sign,不能为空");
		}
		
		
		if (pb.getGasPrice() <= 0) {
			throw new IllegalArgumentException("参数gas_price,不能为空");
		}
	}
}
