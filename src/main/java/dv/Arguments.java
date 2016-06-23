package dv;

public class Arguments {

	void parseArgs(String[] args) throws InvalidArgument {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException("start args is null");
		}
		if(args.length == 1) {
			file = args[0];
		}else {
			int i = 0;
			try {
				for (i = 0; i < args.length - 1; i++) {
					String lcArg = args[i].toLowerCase();
					if (lcArg.charAt(0) != '-' && lcArg.charAt(0) != '/')
						throw new InvalidArgument(args[i]);
					if (lcArg.charAt(0) == '-') {
						lcArg = lcArg.substring(1);
					}
					String[] entry = lcArg.split("=");
					String key = entry[0];
					String value = entry[1];
					if (key.equals("debug")) {
						boolean debug = Boolean.valueOf(value);
						SystemLog.debug(debug);
				    }
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new InvalidArgument (args[args.length - 1]);
			}
			if (i == args.length - 1) {
				file = args[i];
			}
		}
	}

	public String file;
}
