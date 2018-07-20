package org.brewchain.bcvm.impl;

import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.ABI;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.BIN;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.INTERFACE;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.METADATA;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.bcvm.CodeBuild;
import org.brewchain.bcvm.call.CallTransaction;
import org.brewchain.bcvm.solidity.compiler.CompilationResult;
import org.brewchain.bcvm.solidity.compiler.SolidityCompiler;
import org.brewchain.bcvm.utils.VMUtil;
import org.spongycastle.util.encoders.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SolidityBuild implements CodeBuild.Build {

	@Override
	public CodeBuild.Result build(String code, Object... args) throws IOException {
		
		CodeBuild.Result ret = (new CodeBuild()).new Result();
		
		byte[] source = code.getBytes();

		

		SolidityCompiler.Result res = SolidityCompiler.compile(source, true, ABI, BIN, INTERFACE, METADATA);
		
		if (StringUtils.isNotBlank(res.errors) || StringUtils.isBlank(res.output)) {
			log.error("res.errors：：" + res.errors);
			ret.error = res.errors;
		} else {
			CompilationResult result = VMUtil.parse(res.output);
			if (result.contracts != null && result.contracts.size() > 0) {
				for (String name : result.contracts.keySet()) {
					
					CompilationResult.ContractMetadata cm = result.contracts.get(name);
					


					CallTransaction.Contract contract = new CallTransaction.Contract(cm.abi);

					// 合约构造函数
					CallTransaction.Function cfun = contract.getConstructor();
//					String fun = "";
					String argsBytes = "";
					if (cfun != null) {
//						byte[] functionCallBytes = null;
						if (args != null && args.length > 0) {
							argsBytes = Hex.toHexString(cfun.encodeArguments(args));
						}
					}
					ret.data = cm.bin + argsBytes;
					ret.abi = cm.abi;
					ret.exdata = code;
				}
			}else {
				ret.error = "No contract found";
			}
		}
		return ret;
	}
}
