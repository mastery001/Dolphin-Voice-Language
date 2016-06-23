package dv;

public class InvalidArgument extends Exception {
	/**
	 * 2016年6月21日 下午2:47:06
	 */
	private static final long serialVersionUID = -5666427531658018195L;

	/**
	 * @param arg
	 *            the invalid argument.
	 */
	public InvalidArgument(String arg) {
		message = Util.getMessage("InvalidArgument.1", arg) + "\n\n" + Util.getMessage("usage");
	} 

	public InvalidArgument() {
		message = Util.getMessage("InvalidArgument.2") + "\n\n" + Util.getMessage("usage");
	} 

	public String getMessage() {
		return message;
	} 

	private String message = null;
} // class InvalidArgument
