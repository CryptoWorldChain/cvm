package org.brewchain.bcvm.impl;

import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.ABI;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.BIN;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.INTERFACE;
import static org.brewchain.bcvm.solidity.compiler.SolidityCompiler.Options.METADATA;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.brewchain.bcvm.CodeBuild;
import org.brewchain.bcvm.call.CallTransaction;
import org.brewchain.bcvm.solidity.compiler.CompilationResult;
import org.brewchain.bcvm.solidity.compiler.SolidityCompiler;
import org.brewchain.bcvm.utils.VMUtil;

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
					ret.data = cm.abi;

					String exdata = "{\"bin\":\"" + cm.bin + "\"" 
										+ ",\"metadata\":\"" + cm.metadata + "\""
										+ ",\"code\":\"" + code + "\"";

					// Abi abi = Abi.fromJson(cm.abi);
					// Entry onlyFunc = abi.get(0);
					// System.out.println();
					// if(onlyFunc.type == Type.function){
					// onlyFunc.inputs.size();
					// onlyFunc.outputs.size();
					// onlyFunc.constant;
					// }

					CallTransaction.Contract contract = new CallTransaction.Contract(cm.abi);
					// if (contract.functions != null && contract.functions.length > 0) {
					// for (int i = 0; i < contract.functions.length; i++) {
					// System.out.println("contract.functions[" + i + "]:「" +
					// contract.functions[i].toString() + "」");
					// }
					// }

					// 合约构造函数
					CallTransaction.Function cfun = contract.getConstructor();
					if (cfun != null) {
						byte[] functionCallBytes = null;
						String constructor = "{";
						if (args != null && args.length > 0) {
							String str = "";
							for(Object o:args) {
								str += String.valueOf(o) + ",";
							}
							str = str.substring(0, str.length()-1);
							constructor += "\"data\":\"" + str + "\"";
							functionCallBytes = cfun.encodeArguments(args);
						} else {
							constructor += "\"data\":\"\"";
							functionCallBytes = cfun.encodeArguments();
						}
						constructor += ",\"bin\":\"" + Base64.encodeBase64String(functionCallBytes) + "\"}";
						exdata += ",\"constructor\":" + constructor;
					}else {
						ret.error = "未找到构造方法";
					}
					ret.exdata = exdata + "}";
				}
			}else {
				ret.error = "未找到合约";
			}
		}
		return ret;
	}
}
