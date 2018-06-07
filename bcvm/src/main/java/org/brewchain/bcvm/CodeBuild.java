package org.brewchain.bcvm;

import java.io.IOException;

import org.brewchain.bcvm.impl.SolidityBuild;

public class CodeBuild{
	
	public interface Build {
		public CodeBuild.Result build(String code,Object... args) throws IOException ;
	}
	
	public class Result{
		public String data;
		public String exdata;
		public String error;
	}
	
	public static CodeBuild.Build newBuild(String type) {
		CodeBuild.Build build = null;
		if("sol".equals(type)) {
			build = new SolidityBuild();
		}else if("js".equals(type)) {
			
		}
		return build;
	}
}


