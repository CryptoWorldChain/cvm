package org.brewchain.evm;

import static org.ethereum.solidity.compiler.SolidityCompiler.Options.INTERFACE;
import static org.ethereum.solidity.compiler.SolidityCompiler.Options.METADATA;
import static org.ethereum.solidity.compiler.SolidityCompiler.Options.ABI;
import static org.ethereum.solidity.compiler.SolidityCompiler.Options.BIN;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PMContract;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetBuild;
import org.brewchain.cvm.pbgens.Cvm.PSBuildCode;
import org.brewchain.evm.solidity.compiler.CompilationResult;
import org.ethereum.core.CallTransaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.crypto.HashUtil;
import org.ethereum.solidity.compiler.SolidityCompiler;
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
public class Txt extends SessionModules<PSBuildCode> {

	// @ActorRequire
	// CommonService commonService;

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
			CompilationResult result = null;
			// IOException
			SolidityCompiler.Result res = SolidityCompiler.compile(pbo.getCode().getBytes(), true, ABI, BIN, INTERFACE, METADATA);

			if (StringUtils.isNotBlank(res.errors) || StringUtils.isBlank(res.output)) {
				ret.setRetCode(-1);
				ret.setRetMessage(res.errors);
				log.error("res.errors：：" + res.errors);
			} else {
				// IOException
				result = CompilationResult.parse(res.output);
				if (result.contracts != null && result.contracts.size() > 0) {
					ret.setRetCode(0);
					ret.setRetMessage("");
					for (String name : result.contracts.keySet()) {
						PMContract.Builder c = PMContract.newBuilder();
						c.setName(name);
						CompilationResult.ContractMetadata cm = result.contracts.get(name);
						c.setBin(cm.bin);

						ECKey key = new ECKey();
						byte[] hash = HashUtil.sha256(cm.bin.getBytes());
						ECKey.ECDSASignature sig = key.doSign(hash);
						c.setAddr(Hex.toHexString(key.getAddress()));

						c.setAbi(cm.abi);
						c.setMetadata(cm.metadata);

						CallTransaction.Contract contract = new CallTransaction.Contract(cm.abi);
						if (contract.functions != null && contract.functions.length > 0) {
							for (int i = 0; i < contract.functions.length; i++) {
								System.out.println(
										"contract.functions[" + i + "]:「" + contract.functions[i].toString() + "」");
								c.addFunName(contract.functions[i].toString());
							}
						}

						// Abi abi = Abi.fromJson(cm.abi);
						// Entry onlyFunc = abi.get(0);
						// System.out.println();
						// if(onlyFunc.type == Type.function){
						// onlyFunc.inputs.size();
						// onlyFunc.outputs.size();
						// onlyFunc.constant;
						// }

						ret.addInfo(c);
					}
				} else {
					ret.setRetCode(-1);
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

		if (StringUtils.isBlank(pb.getCode())) {
			throw new IllegalArgumentException("参数code,不能为空");
		}

	}
}
