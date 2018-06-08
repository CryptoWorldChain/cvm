package org.brewchain.bcvm;

import java.io.IOException;

import org.brewchain.bcvm.impl.SolidityBuild;

public class CodeBuild{
	
	public interface Build {
		/**
		 * 
		 * @param code 合约代码
		 * @param args 合约构造函数参数...
		 * @return
		 * @throws IOException
		 */
		public CodeBuild.Result build(String code,Object... args) throws IOException ;
	}
	public enum Type {
		SOLIDITY("sol"),
        JAVASCRIPT("js");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
	public class Result{
		public String data;
		public String exdata;
		public String error;
	}
	
	public static CodeBuild.Build newBuild(Type type) {
		CodeBuild.Build build = null;
		if(type == null) {
			return build;
		}
		if("sol".equals(type.getName())) {
			build = new SolidityBuild();
		}else if("js".equals(type.getName())) {
			// TODO
		}
		return build;
	}
	
}


