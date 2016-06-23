package dv;

import java.io.IOException;

public class InvalidCharacter extends IOException {
	/**
	 * 2016年6月15日 下午3:18:25
	 */
	private static final long serialVersionUID = -2003767950540954496L;

	public InvalidCharacter(String filename, String line, int lineNumber, int pos, char ch) {
		String pointer = "^";
		if (pos > 1) {
			byte[] bytes = new byte[pos - 1];
			for (int i = 0; i < pos - 1; ++i)
				bytes[i] = (byte) ' '; // <d62023>
			pointer = new String(bytes) + pointer;
		}
		String[] parameters = { filename, Integer.toString(lineNumber), "" + ch, Integer.toString((int) ch), line,
				pointer };
		message = Util.getMessage("InvalidCharacter.1", parameters);
	}

	public String getMessage() {
		return message;
	} // getMessage

	private String message = null;
}
