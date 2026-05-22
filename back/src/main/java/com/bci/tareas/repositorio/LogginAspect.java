package com.bci.tareas.repositorio;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
@Aspect
@Component
public class LogginAspect {
	Logger logger=LoggerFactory.getLogger(LogginAspect.class);
	@Around("execution(* com.bci.tareas.controllers.ControllerConsulta.*ById(String)) && args(id)")
	public Object logObjects(ProceedingJoinPoint procedingJointPoint ,String id) throws Throwable {
		Object response=procedingJointPoint.proceed();
		logger.info("****LoggingAspect.logAroundAllMethods() : " + procedingJointPoint.getSignature().getName() + ": Before Method Execution");
		   
		logger.info("returned  response for request :{} {}" ,id,response );
		return response;
	}

}
