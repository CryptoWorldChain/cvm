package org.brewchain.bcvm.utils;

import java.io.IOException;

import org.brewchain.bcvm.solidity.compiler.CompilationResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VMUtil {

	public static CompilationResult parse(String rawJson) throws IOException {
		CompilationResult result = CompilationResult.parse(rawJson);
		return result;
	}
	
}
