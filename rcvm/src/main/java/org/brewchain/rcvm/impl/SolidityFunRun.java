package org.brewchain.rcvm.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.brewchain.rcvm.Fun;
import org.brewchain.rcvm.Fun.Result;
import org.brewchain.rcvm.call.CallTransaction;
import org.brewchain.rcvm.exec.TransactionExecutor;
import org.brewchain.rcvm.jsonrpc.TransactionReceipt;
import org.spongycastle.util.encoders.Hex;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		
		CallTransaction.Contract contract;
		try {
			contract = new CallTransaction.Contract(this.code);
		} catch (Exception e) {
			log.error("code异常",e);
			ret.error = "code异常";
			return ret;
		}
		CallTransaction.Function fun = contract.getByName(funName);
		if(fun == null) {
			ret.error = "未找到方法["+funName+"]";
			return ret;
		}
		
		String funJson = "{";
		byte[] functionCallBytes = null;
		if (args != null && args.length > 0) {
			String str = "";
			for(Object o:args) {
				str += String.valueOf(o) + ",";
			}
			str = str.substring(0, str.length()-1);
			funJson += "\"name\":\"" + funName + "\"";
			funJson += ",\"args\":\"" + str + "\"";
			functionCallBytes = fun.encode(args);
		} else {
			funJson += "\"name\":\"" + funName + "\"";
			funJson += ",\"args\":\"\"";
			functionCallBytes = fun.encode();
		}
		funJson += ",\"bin\":\"" + Base64.encodeBase64String(functionCallBytes) + "\"}";
		ret.fun = funJson;
		
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

		executor.init(codeHash.getBytes(),Base64.decodeBase64(functionCallBytes));
		executor.execute();
		executor.go();
		
		TransactionReceipt res = executor.getReceipt();
		if(StringUtils.isNotBlank(res.getError())) {
			ret.error = res.getError();
		}
		ret.cmd = res.getCmds();
//		ret.fun = "";
		
		return ret;
	}

	
}
