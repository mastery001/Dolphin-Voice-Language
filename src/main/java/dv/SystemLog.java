package dv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统日志
 * @author zouziwen
 *
 * 2016年6月21日 下午2:43:35
 */
public class SystemLog {

	private static final Logger logger = LoggerFactory.getLogger(SystemLog.class);

	public static void debug(String msg) {
		if (debug()) {
			logger.info(msg);
		}else {
			System.err.println(msg);
		}
	}

	public static void debug(String format, Object arg) {
		if (debug()) {
			logger.info(format, arg);
		}
	}

	public static void debug(String format, Object arg1, Object arg2) {
		if (debug()) {
			logger.info(format, arg1, arg2);
		}
	}

	public void debug(String format, Object... arguments) {
		if (debug()) {
			logger.info(format, arguments);
		}
	}

	public static void debug(String msg, Throwable t) {
		if (debug()) {
			logger.info(msg, t);
		}
	}

	public static void debug(boolean debug) {
		_debug = debug;
	}

	public static boolean debug() {
		return _debug;
	}

	private static boolean _debug;

}
