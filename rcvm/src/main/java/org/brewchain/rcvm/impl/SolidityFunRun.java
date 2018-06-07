package org.brewchain.rcvm.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.brewchain.rcvm.Fun;
import org.brewchain.rcvm.Fun.Result;
import org.brewchain.rcvm.call.CallTransaction;
import org.brewchain.rcvm.exec.TransactionExecutor;
import org.brewchain.rcvm.jsonrpc.TransactionReceipt;

public class SolidityFunRun implements Fun.Run{

	private String codeHash;
	private String code;
//	private String data;
	
	public SolidityFunRun() {
		
	}

	@Override
	public void setCodeHash(String codeHash) {
		this.codeHash = codeHash;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

//	@Override
//	public void setData(String data) {
//		this.data = data;
//	}

	public SolidityFunRun(String codeHash,String code,String data) {
		this.codeHash = codeHash;
		this.code = code;
//		this.data = data;
	}

	@Override
	public Result run(String funName, Object... args) {
		
		Fun.Result ret = (new Fun()).new Result();
		
		CallTransaction.Contract contract = new CallTransaction.Contract(this.code);
		if(contract == null) {
			ret.error = "code异常";
			return ret;
		}
		CallTransaction.Function fun = contract.getByName(funName);
		if(fun == null) {
			ret.error = "未找到方法["+funName+"]";
			return ret;
		}
		
		byte[] functionCallBytes = null;
		if (args != null && args.length > 0) {
			String str = "";
			for(Object o:args) {
				str += String.valueOf(o) + ",";
			}
			ret.args = str.substring(0, str.length()-1);
			functionCallBytes = fun.encodeArguments(args);
		} else {
			functionCallBytes = fun.encodeArguments();
		}
		
		ret.functionCallBytes = Base64.encodeBase64String(functionCallBytes);
		
		TransactionExecutor executor = new TransactionExecutor().withCommonConfig().setLocalCall(true);

		executor.init(codeHash.getBytes(),functionCallBytes);
		executor.execute();
		executor.go();
		
		
		TransactionReceipt res = executor.getReceipt();
		if(StringUtils.isNotBlank(res.getError())) {
			ret.error = res.getError();
		}
		ret.cmd = res.getCmds();
		return ret;
	}

	@Override
	public Result run(String functionCallBytes) {
		Fun.Result ret = (new Fun()).new Result();
		
		TransactionExecutor executor = new TransactionExecutor().withCommonConfig().setLocalCall(true);

		executor.init(null,Base64.decodeBase64(functionCallBytes));
		executor.execute();
		executor.go();
		
		TransactionReceipt res = executor.getReceipt();
//		ret.cmd = res.get
		return ret;
	}

	
}
