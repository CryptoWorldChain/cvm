package org.brewchain.rcvm;

import org.apache.commons.lang3.StringUtils;

public class TestRun {
	public static void main(String[] args) {
		try {
			
	        org.brewchain.rcvm.Fun.Run  cvm = org.brewchain.rcvm.Fun.newBuild(org.brewchain.rcvm.Fun.Type.SOLIDITY);
	       
    			//执行方法
	        cvm.setCodeHash("test");
    	        cvm.setCode("[{\"constant\":false,\"inputs\":[{\"name\":\"pirce\",\"type\":\"uint256\"}],\"name\":\"testBid\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_tempAddr\",\"type\":\"address\"},{\"name\":\"_tempNow\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]");
    	        org.brewchain.rcvm.Fun.Result ret = cvm.run("testBid", "12");
    	        
    	        if(StringUtils.isNotBlank(ret.error)) {
    	        		System.out.println("error="+ret.error);
    	        }
    			System.out.println("cmd="+ret.cmd);
    			System.out.println("fun="+ret.fun);
        			
        			

        			////执行方法
    	        /// 构造    functionCallBytes
    	        ////TIIWlAAAAAAAAAAAAAAAMNJtfi4Injd8CzqlJ3xs6RZEbYh9AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGM=
//    	        String functionCallBytes = "TIIWlAAAAAAAAAAAAAAAMNJtfi4Injd8CzqlJ3xs6RZEbYh9AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGM=";
    	        
    	        ////testBid   functionCallBytes
    	        ////cko+BQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAM
        			
//    	        String functionCallBytes = "cko+BQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAM";
//    			org.brewchain.rcvm.Fun.Result ret2 = cvm.run(functionCallBytes);
//    			System.out.println("cmd="+ret2.cmd);
//    			System.out.println("fun="+ret2.fun);
        			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
