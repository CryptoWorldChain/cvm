package org.brewchain.bcvm;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.lang3.StringUtils;

public class TestBuild {
	public static void main(String[] args) {
		try {
			File file = new File("/GIT/cwv/chaincode/cwv-contract-sol/contracts/auction.sol");
			FileReader reader = new FileReader(file);// 获取该文件的输入流  
	        char[] bb = new char[1024];// 用来保存每次读取到的字符  
	        
	        String code = "";// 用来将每次读取到的字符拼接，当然使用StringBuffer类更好  
	        int n;// 每次读取到的字符长度  
	        while ((n = reader.read(bb)) != -1) {  
	            code += new String(bb, 0, n);  
	        }  
	        reader.close();// 关闭输入流，释放连接  
	        //System.out.println(code);
	        
	        org.brewchain.bcvm.CodeBuild.Build  cvm = org.brewchain.bcvm.CodeBuild.newBuild(org.brewchain.bcvm.CodeBuild.Type.SOLIDITY);
	        
	        org.brewchain.bcvm.CodeBuild.Result ret = cvm.build(code);
	        
	        if(StringUtils.isBlank(ret.error)) {
	        		System.out.println("data="+ret.data);
	        		System.out.println("exdata="+ret.exdata);
	        }else {
	        		System.out.println("error="+ret.error);
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
