package com.aobei.trainconsole.configuration;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.OperateLog;
import com.aobei.train.model.Users;
import com.aobei.train.service.OperateLogService;
import com.aobei.train.service.UsersService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by adminL on 2018/5/23.
 */
@Aspect
@Component
public class LoggerAopConfiguration {

    @Autowired
    private UsersService usersService;
    @Autowired
    private OperateLogService operateLogService;

    private String username ;


    @Pointcut("execution(Object com.aobei.trainconsole.controller.*.*(..))")
    private void controllerMethod(){

    }

    @Before("controllerMethod()")
    public void get(JoinPoint point){
        Object[] args = point.getArgs();
        if(args.length!=0){
            for (Object o:args) {
                if(o!=null){
                    if("CasAuthenticationToken".equals(o.getClass().getSimpleName())){
                        CasAuthenticationToken token  = (CasAuthenticationToken)o;
                        UserDetails user  = token.getUserDetails();

                        if (user != null) {
                            username = user.getUsername();
                        }else{
                            username = null;
                        }
                        break;
                    }
                }
            }
        }

    }

    @After("controllerMethod()")
    public void recordLof(JoinPoint point){
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        if(username!=null){
            Users users = usersService.xSelectUserByUsername(username);
            OperateLog operateLog = new OperateLog();
            operateLog.setOperate_log_id(IdGenerator.generateId());
            operateLog.setOperate("U["+users.getUsername()+"] F["+methodName+"] M["+className+"] ");
            operateLog.setUser_id(users.getUser_id());
            operateLog.setCreate_datetime(new Date());
            operateLogService.insertSelective(operateLog);
        }

    }

}
