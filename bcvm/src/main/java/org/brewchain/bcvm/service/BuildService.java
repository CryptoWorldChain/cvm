package org.brewchain.bcvm.service;

import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.ABI;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.BIN;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.INTERFACE;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.METADATA;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.bcvm.call.CallTransaction;
import org.brewchain.bcvm.solidity.compiler.CompilationResult;
import org.brewchain.bcvm.solidity.compiler.SolidityCompiler;
import org.brewchain.bcvm.utils.VMUtil;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PMConstructor;
import org.brewchain.cvm.pbgens.Cvm.PMContract;
import org.brewchain.cvm.pbgens.Cvm.PMEXData;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetBuild;
import org.brewchain.cvm.pbgens.Cvm.PSBuildCode;
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
public class BuildService extends SessionModules<PSBuildCode> {

	@ActorRequire(name = "bc_encoder",scope = "global")
	EncAPI encAPI;
	
	@Override
	public String getModule() {
		return PModule.BCVM.name();
	}

	@Override
	public String[] getCmds() {
		return new String[] { PCommand.BCD.name() };
	}

	public String toString() {
		return "BuildService";
	}

	@Override
	public void onPBPacket(FramePacket pack, PSBuildCode pbo, CompleteHandler handler) {
		final PRetBuild.Builder ret = PRetBuild.newBuilder();
		try {
			checkNull(pbo);
			
			byte[] source = pbo.getCode().getBytes();
			SolidityCompiler.Result res = SolidityCompiler.compile(source, true, ABI, BIN, INTERFACE, METADATA);
			
			if (StringUtils.isNotBlank(res.errors) || StringUtils.isBlank(res.output)) {
				ret.setRetCode(-1);
				ret.setRetMessage(res.errors);
				log.error("res.errors：：" + res.errors);
			} else {
				// IOException
				CompilationResult result = VMUtil.parse(res.output);
				if (result.contracts != null && result.contracts.size() > 0) {
					ret.setRetCode(0);
					ret.setRetMessage("");
					for (String name : result.contracts.keySet()) {
						PMContract.Builder c = PMContract.newBuilder();
						
						CompilationResult.ContractMetadata cm = result.contracts.get(name);
						
						c.setData(cm.abi);
						
						PMEXData.Builder exdata = PMEXData.newBuilder();
						exdata.setBin(cm.bin);
						exdata.setMetadata(cm.metadata);
						exdata.setCode(pbo.getCode());

						// Abi abi = Abi.fromJson(cm.abi);
						// Entry onlyFunc = abi.get(0);
						// System.out.println();
						// if(onlyFunc.type == Type.function){
						// onlyFunc.inputs.size();
						// onlyFunc.outputs.size();
						// onlyFunc.constant;
						// }
						
						
						CallTransaction.Contract contract = new CallTransaction.Contract(cm.abi);
//						if (contract.functions != null && contract.functions.length > 0) {
//							for (int i = 0; i < contract.functions.length; i++) {
//								System.out.println("contract.functions[" + i + "]:「" + contract.functions[i].toString() + "」");
//							}
//						}
						
						// 合约构造函数
						CallTransaction.Function cfun = contract.getConstructor();
						if(cfun != null) {
							byte[] functionCallBytes = null;
							PMConstructor.Builder constructor = PMConstructor.newBuilder();
							if (StringUtils.isNotBlank(pbo.getData())) {
								constructor.setData(pbo.getData());
				                Object[] dataArray = pbo.getData().split(",");
				                functionCallBytes = cfun.encodeArguments(dataArray);
				            }else {
				            		functionCallBytes = cfun.encodeArguments();
				            }
							constructor.setBin(encAPI.base64Enc(functionCallBytes));
							exdata.setConstructor(constructor);
						}
						c.setExdata(exdata);
						ret.setBuild(c);
					}
				} else {
					ret.setRetCode(0);
					ret.setRetMessage("合约代码异常");
				}
			}

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
	
	public void checkNull(PSBuildCode pb) {
		if (pb == null) {
			throw new IllegalArgumentException("无请求参数");
		}
		if (StringUtils.isBlank(pb.getCode())) {
			throw new IllegalArgumentException("参数code,不能为空");
		}
	}

}
