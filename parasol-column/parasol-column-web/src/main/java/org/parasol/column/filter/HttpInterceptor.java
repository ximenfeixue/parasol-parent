package org.parasol.column.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ginkgocap.ywxt.user.model.User;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HttpInterceptor implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);
	private long threshold = 200;
	ThreadLocal<StopWatch> currentThread = new ThreadLocal<StopWatch>();

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		currentThread.set(stopWatch);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		StopWatch stopWatch = currentThread.get();
		stopWatch.split();
		long splitTime = stopWatch.getSplitTime();
		if (splitTime > threshold) {
			User user = (User) request.getAttribute("sessionUser");
			long userId = null == user ? -1 : user.getId();

			StringBuilder buff = new StringBuilder();
			buff.append("[url=").append(request.getRequestURI())
					.append("]  [s=")
					.append(splitTime)
					.append(" ms]")
					.append(" [userId=")
					.append(userId)
					.append("]")
					.append(" [params=")
					.append(request.getAttribute("requestJson") == null ? ""
							: request.getAttribute("requestJson").toString())
					.append("]");
			logger.info(buff.toString());
		}
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

}
