package log4j;

import org.apache.log4j.Logger;

/**
 * <p>
 * LOGGER
 * </p>
 * 
 * @author 
 * @version 3.0
 */
public class Log4j {

	/**
	 * STATIC
	 */
	private static final Logger logger = Logger.getLogger(Log4j.class);

	/**
	 * CONSTRUCTOR
	 */

	/**
	 * Default constructor
	 * 
	 * @category Constructor
	 */
	public Log4j() {
	}

	/**
	 * PUBLIC METHODS
	 */

	/**
	 * Display different messages possible with log4j
	 */
	public void test() {

		logger.debug("debugging message");
		logger.info("information message");
		logger.warn("warning message");
		logger.error("error message");
		logger.fatal("fatal error message");
	}
}