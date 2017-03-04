package dv;

  
/**  
 *@Description:  解释运行时的异常
 *@Author:zouziwen
 *@Since:2016年6月13日
 *@Version:1.1.0  
 */
public class InterpreteException extends RuntimeException{

	/**
	 * 2016年6月19日 下午8:28:00
	 */
	private static final long serialVersionUID = -2722155713764350013L;

	public InterpreteException() {
		super();
	}

	public InterpreteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InterpreteException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterpreteException(String message) {
		super(message);
	}

	public InterpreteException(Throwable cause) {
		super(cause);
	}

	
	
}
