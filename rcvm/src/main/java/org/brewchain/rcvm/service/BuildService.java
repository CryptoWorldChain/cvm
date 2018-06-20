package org.brewchain.rcvm.service;

import static org.brewchain.rcvm.solidity.compiler.SolidityCompiler.Options.ABI;
import static org.brewchain.rcvm.solidity.compiler.SolidityCompiler.Options.BIN;
import static org.brewchain.rcvm.solidity.compiler.SolidityCompiler.Options.INTERFACE;
import static org.brewchain.rcvm.solidity.compiler.SolidityCompiler.Options.METADATA;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PMContract;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetBuild;
import org.brewchain.cvm.pbgens.Cvm.PSBuildCode;
import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.call.CallTransaction;
import org.brewchain.rcvm.exec.TransactionExecutor;
import org.brewchain.rcvm.jsonrpc.TransactionReceipt;
import org.brewchain.rcvm.solidity.compiler.CompilationResult;
import org.brewchain.rcvm.solidity.compiler.SolidityCompiler;
import org.brewchain.rcvm.utils.VMUtil;
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
	
	
	EvmApi evmapi;
	
//	@ActorRequire(name = "Account_Helper", scope = "global")
//	AccountHelper accountHelper;
//	
//	@ActorRequire(name = "Transaction_Helper", scope = "global")
//	TransactionHelper transactionHelper;
	
	@Override
	public String getModule() {
		return PModule.CVM.name();
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
			//CompilationResult result = null;
			// IOException
			
//			VMUtil.solidCompoler(encAPI,ret,pbo.getCode().getBytes());
			
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
						c.setName(name);
						CompilationResult.ContractMetadata cm = result.contracts.get(name);

//						KeyPairs key = encAPI.genKeys();
						
//						c.setAddr(pbo.getAddr());
						c.setBin(cm.bin);
						c.setAbi(cm.abi);
						c.setMetadata(cm.metadata);

						CallTransaction.Contract contract = new CallTransaction.Contract(cm.abi);
						if (contract.functions != null && contract.functions.length > 0) {
							for (int i = 0; i < contract.functions.length; i++) {
								System.out.println("contract.functions[" + i + "]:「" + contract.functions[i].toString() + "」");
								c.addFunName(contract.functions[i].toString());
							}
						}
						
						// 合约构造函数
						CallTransaction.Function cfun = contract.getConstructor();
						byte[] functionCallBytes = null;
						if(cfun != null) {
							if (StringUtils.isNotBlank(pbo.getData())) {
				                Object[] dataArray = pbo.getData().split(",");
				                functionCallBytes = cfun.encodeArguments(dataArray);
				            }else {
				            		functionCallBytes = cfun.encodeArguments();
				            }
//							new PendingTx(new byte[0], BigInteger.ZERO,
//									ByteUtil.merge(Hex.decode(contract.getBinary()), functionCallBytes), contract, null,
//									new TransactionResult())
							
//							System.out.println("cm.bin="+cm.bin);
//							System.out.println("Hex.decode(cm.bin)="+Hex.decode(cm.bin));
//							System.out.println("functionCallBytes="+functionCallBytes);
//							System.out.println("ByteUtil.merge(Hex.decode(cm.bin), functionCallBytes)="+ByteUtil.merge(Hex.decode(cm.bin), functionCallBytes));
							//ByteUtil.merge(Hex.decode(cm.bin), functionCallBytes);
						}
						
						
						
						
						long fee = 0L;
						long feeLimit = 0L;
						if(pbo.getFee() > 0) {
							fee = pbo.getFee();
							feeLimit = fee;
							if(pbo.getFeeLimit() > 0) {
								feeLimit = pbo.getFeeLimit  ();
							}
						}
						
						String  version = "v1.0.0";
						if(StringUtils.isNotBlank(pbo.getVersion())) {
							version = pbo.getVersion();
						}

						String exDataStr = "{"
									+ "'code':'"+pbo.getCode()+"'"
									+ ",'version':'"+version+"'"
									+ ",'bin':'"+c.getBin()+"'"
									+ ",'abi':'"+c.getAbi()+"'"
									+ ",'metadata':'"+c.getMetadata()+"'"
									+ ",'cfuncode':"+functionCallBytes == null?"":functionCallBytes+"'"
								+ "}";
						
//						MTransaction mtx = new MTransaction(evmapi);
//						mtx.addTXInput(pbo.getAddr(), pbo.getPubKey(), pbo.getSign(), 0L, fee, feeLimit);
//						mtx.addTXOutput(null, 0L);
						
//						mtx.setTXBodyData(c.getAbi().getBytes(), exDataStr.getBytes());
////						if(functionCallBytes == null) {
////							mtx.setTXBodyData(Hex.decode(c.getBin().getBytes()), exDataStr.getBytes());
////						}else {
////							mtx.setTXBodyData(ByteUtil.merge(Hex.decode(cm.bin), functionCallBytes), exDataStr.getBytes());
////						}
//						mtx.setSign(pbo.getPubKey(), pbo.getSign());
////						mtx.txSign(encAPI, pbo.getPubKey(), "3d6b03d3fa3ab3959abf2ffca1aa2c5fd14c90256234fb97b2f6b2e6e2549feb");
//						mtx.sendTX(evmapi);
//						
//						if(functionCallBytes != null) {
//							TransactionExecutor executor = new TransactionExecutor().withCommonConfig().setLocalCall(true);
//							executor.init(null,functionCallBytes);
//				            executor.execute();
//				            executor.go();
////				            executor.finalization();
//				            TransactionReceipt rest = executor.getReceipt();
//						}
//						
//						// TODO 创建合约交易
//						// Abi abi = Abi.fromJson(cm.abi);
//						// Entry onlyFunc = abi.get(0);
//						// System.out.println();
//						// if(onlyFunc.type == Type.function){
//						// onlyFunc.inputs.size();
//						// onlyFunc.outputs.size();
//						// onlyFunc.constant;
//						// }
//						
//						ret.setCAddr(
//								encAPI.genKeys(
//										pbo.getAddr()
//										+evmapi.getNonce(encAPI.hexDec(pbo.getAddr())))
//								.getAddress());
//						ret.addInfo(c);
					}
				} else {
					ret.setRetCode(0);
					ret.setRetMessage("没有找到合约");
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
		if (StringUtils.isBlank(pb.getAddr())) {
			throw new IllegalArgumentException("参数addr,不能为空");
		}
		if (StringUtils.isBlank(pb.getPubKey())) {
			throw new IllegalArgumentException("参数pub_key,不能为空");
		}
		if (StringUtils.isBlank(pb.getSign())) {
			throw new IllegalArgumentException("参数sign,不能为空");
		}
		if (StringUtils.isBlank(pb.getCode())) {
			throw new IllegalArgumentException("参数code,不能为空");
		}
	}

}
