package dv.bootstrap;

import java.io.IOException;

import dv.Compiler;

public class Bootstrap {
	
	public static void main(String[] args) throws IOException {
		String filename = "src/main/resources/test.dv";
		args = new String[]{filename};
		Compiler compiler = new Compiler();
		compiler.start(args);
	}
}
