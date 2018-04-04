package org.brewchain.evm.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.core.core.CallTransaction;
import org.brewchain.core.crypto.ECKey;
import org.brewchain.core.crypto.HashUtil;
import org.brewchain.core.solidity.compiler.CompilationResult;
import org.brewchain.core.solidity.compiler.SolidityCompiler;
import static org.brewchain.core.solidity.compiler.SolidityCompiler.Options.*;
import org.brewchain.cvm.pbgens.Cvm.PMContract;
import org.brewchain.cvm.pbgens.Cvm.PRetBuild;
import org.spongycastle.util.encoders.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VMUtil {

	public static void solidCompoler(PRetBuild.Builder ret,byte[] source) throws IOException{
		
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
				ret.setRetCode(0);
				ret.setRetMessage("没有找到合约");
			}
		}
		//return ret;
	}
	
	public static CompilationResult parse(String rawJson) throws IOException {
		CompilationResult result = CompilationResult.parse(rawJson);
		return result;
	}
}
