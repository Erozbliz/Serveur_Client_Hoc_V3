package test;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * THREAD POOL
 * https://www.javacodegeeks.com/2013/01/java-thread-pool-example-using-executors-and-threadpoolexecutor.html
 *
 */
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler{

	 @Override
	    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
	        System.out.println(r.toString() + " is rejected");
	    }

}
