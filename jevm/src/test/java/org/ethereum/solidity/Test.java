package org.ethereum.solidity;

import static org.ethereum.solidity.compiler.SolidityCompiler.Options.ABI;
import static org.ethereum.solidity.compiler.SolidityCompiler.Options.BIN;
import static org.ethereum.solidity.compiler.SolidityCompiler.Options.INTERFACE;
import static org.ethereum.solidity.compiler.SolidityCompiler.Options.METADATA;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.StringUtils;
import org.ethereum.core.CallTransaction;
import org.ethereum.solidity.compiler.CompilationResult;
import org.ethereum.solidity.compiler.SolidityCompiler;
import org.ethereum.solidity.utils.FileUtil;

public class Test {
	public static void main(String[] args) throws IOException {
		String contractSrc =
                "pragma solidity ^0.4.0;\n" +
	                "contract test {" +
	                "	function() {throw;}" +
	                "}";
		
		System.out.println("测试Solidity字符串");
		CompilationResult res = compileStr(contractSrc);
		CompilationResult.ContractMetadata metadate = res.contracts.get("test");
		
		System.out.println("abi length:「"+metadate.abi.length()+"」");
		System.out.println("bin length:「"+metadate.bin.length()+"」");
//        System.out.println("interface length:「"+metadate.getInterface().length()+"」");
		System.out.println("metadata length:「"+metadate.metadata.length()+"」");
		
		System.out.println("abi:「"+metadate.abi+"」");
		System.out.println("bin:「"+metadate.bin+"」");
        System.out.println("interface:「"+metadate.getInterface()+"」");
		System.out.println("metadata:「"+metadate.metadata+"」");


//        String abif = "src/main/resources/contracts/abi/1.abi";
//        String binf ="src/main/resources/contracts/bin/1.bin";
//        FileUtil.writeFile(abif,metadate.abi);
//        FileUtil.writeFile(binf,metadate.bin);
        
        
		System.out.println();
		System.out.println();
		
		System.out.println("测试Solidity .sol文件");
		res = compileFile(new File("src/test/resources/solidity/ballot.sol"));
		metadate = res.contracts.get("Ballot");

		System.out.println("abi length:「"+metadate.abi.length()+"」");
		System.out.println("bin length:「"+metadate.bin.length()+"」");
//        System.out.println("interface length:「"+metadate.getInterface().length()+"」");
		System.out.println("metadata length:「"+metadate.metadata.length()+"」");
		
		System.out.println("abi:「"+metadate.abi+"」");
		System.out.println("bin:「"+metadate.bin+"」");
        System.out.println("interface:「"+metadate.getInterface()+"」");
		System.out.println("metadata:「"+metadate.metadata+"」");

		
		System.out.println("################");
        
        CallTransaction.Contract contract = new CallTransaction.Contract(metadate.abi);

        System.out.println("contract.functions.length=「"+contract.functions.length+"」");
        for(int i=0; i<contract.functions.length;i++) {
        	System.out.println("contract.functions["+i+"]:「"+contract.functions[0].toString()+"」");
        }
        System.out.println("################");
        
//        abif = "src/main/resources/contracts/abi/2.abi";
//        binf ="src/main/resources/contracts/bin/2.bin";
//        FileUtil.writeFile(abif,metadate.abi);
//        FileUtil.writeFile(binf,metadate.bin);
        
//        String packageName ="com.contracs";
//        String outputDir = "src/test/java";
//        String[] args_ = {  "generate" ,binf, abif , "-p" ,packageName, "-o", outputDir};
//        org.web3j.codegen.SolidityFunctionWrapperGenerator.run(args_);
        
	}
	
	public static CompilationResult compileStr(String contractSrc) throws IOException {
		
		CompilationResult result = null;
		
		System.out.println(contractSrc);
        System.out.println();
        
        SolidityCompiler.Result res = SolidityCompiler.compile(contractSrc.getBytes(), true, ABI, BIN, INTERFACE, METADATA);
        if(StringUtils.isNotBlank(res.output)) {
            result = CompilationResult.parse(res.output);
        	System.out.println("Out:「" + res.output + "」");
		}
        if (StringUtils.isNotBlank(res.errors))
        	System.err.println("Err:「" + res.errors + "」\n");

        
//        System.out.println();
//       
//        res = SolidityCompiler.compile(contractSrc.getBytes(), true, ABI);
//        System.out.println("abi Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("abi Err:「" + res.errors + "」\n");
//
//        System.out.println();
//        
//		res = SolidityCompiler.compile(contractSrc.getBytes(), true, BIN);
//        System.out.println("bin Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("bin Err:「" + res.errors + "」\n");
//		
//        System.out.println();
//
//        res = SolidityCompiler.compile(contractSrc.getBytes(), false, BIN);
//        System.out.println("bin Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("bin Err:「" + res.errors + "」\n");
//		
//        System.out.println();
//        
//        res = SolidityCompiler.compile(contractSrc.getBytes(), true, INTERFACE);
//        System.out.println("interface Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("interface Err:「" + res.errors + "」\n");
//
//        System.out.println();
//        
//        res = SolidityCompiler.compile(contractSrc.getBytes(), true, METADATA);
//        System.out.println("mteadata Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("mteadata Err:「" + res.errors + "」\n");
        

        return result;
	}
	
	public static CompilationResult compileFile(File contractSrc) throws IOException {

		CompilationResult result = null;
		
		System.out.println(contractSrc.getPath());
		System.out.println();
		
        SolidityCompiler.Result res = SolidityCompiler.compile(contractSrc, true, ABI, BIN, INTERFACE, METADATA);
        if (StringUtils.isNotBlank(res.output)) {
            result = CompilationResult.parse(res.output);
        	System.out.println("Out:「" + res.output + "」");
        }
        if (StringUtils.isNotBlank(res.errors))
        	System.err.println("Err:「" + res.errors + "」\n");

//        System.out.println();
//
//        res = SolidityCompiler.compile(contractSrc, true, ABI);
//        System.out.println("abi Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("abi Err:「" + res.errors + "」\n");
//
//        System.out.println();
//        
//		res = SolidityCompiler.compile(contractSrc, true, BIN);
//        System.out.println("bin Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("bin Err:「" + res.errors + "」\n");
//		
//        System.out.println();
//
//        res = SolidityCompiler.compile(contractSrc, false, BIN);
//        System.out.println("bin Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("bin Err:「" + res.errors + "」\n");
//		
//        System.out.println();
//        
//        res = SolidityCompiler.compile(contractSrc, true, INTERFACE);
//        System.out.println("interface Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("interface Err:「" + res.errors + "」\n");
//
//        System.out.println();
//        
//        res = SolidityCompiler.compile(contractSrc, true, METADATA);
//        System.out.println("mteadata Out:「" + res.output + "」");
//        if (StringUtils.isNotBlank(res.errors))
//        	System.err.println("mteadata Err:「" + res.errors + "」\n");
        
        return result;
	}
	
}
