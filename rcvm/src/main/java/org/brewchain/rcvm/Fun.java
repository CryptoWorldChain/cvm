package org.brewchain.rcvm;

import org.brewchain.rcvm.impl.SolidityFunRun;

public class Fun {
	
	public interface Run {
		public void setCodeHash(String codeHash);
		public void setCode(String code);
//		public void setData(String data);
		/**
		 * 
		 * @param funNmae 方法名称
		 * @param args 方法参数...
		 * @return
		 */
		public Fun.Result run(String funName,Object... args);
		
		/**
		 * 要执行方法、参数生成的Bytes
		 * @param functionCallBytes
		 * @return
		 */
		public Fun.Result run(String functionCallBytes);
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
		public String args;
		public String functionCallBytes;
		public String cmd;
		
		public String error;
	}
	
	public static Fun.Run newBuild(Type type) {
		Fun.Run run = null;
		if(type == null) {
			return run;
		}
		if("sol".equals(type.getName())) {
			run = new SolidityFunRun();
		}else if("js".equals(type.getName())) {
			// TODO
		}
		return run;
	}
	
	public static Fun.Run newBuild(Type type,String data,String exData) {
		Fun.Run run = null;
		if(type == null) {
			return run;
		}
		if("sol".equals(type.getName())) {
			run = new SolidityFunRun();
		}else if("js".equals(type.getName())) {
			// TODO
		}
		return run;
	}
	
}
