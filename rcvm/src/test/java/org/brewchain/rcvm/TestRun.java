package org.brewchain.bcvm;

import org.apache.commons.lang3.StringUtils;

public class TestRun {
	public static void main(String[] args) {
		try {
			
	        org.brewchain.rcvm.Fun.Run  cvm = org.brewchain.rcvm.Fun.newBuild(org.brewchain.rcvm.Fun.Type.SOLIDITY);
	        
	        //执行方法
	        cvm.setCodeHash("");
	        cvm.setCode("");
	        org.brewchain.rcvm.Fun.Result ret = cvm.run("getAAA", "11","22","33","4","55");
	        
	        if(StringUtils.isNotBlank(ret.error)) {
	        		System.out.println("error="+ret.error);
	        }
    			System.out.println("args="+ret.args);
    			System.out.println("cmd="+ret.cmd);
    			System.out.println("functionCallBytes="+ret.functionCallBytes);
    			
    			
    			//执行方法
    			org.brewchain.rcvm.Fun.Result ret2 = cvm.run(ret.functionCallBytes);
    			System.out.println("functionCallBytes="+ret.functionCallBytes);
    			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
