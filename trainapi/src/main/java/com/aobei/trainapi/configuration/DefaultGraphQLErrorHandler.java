package com.aobei.trainapi.configuration;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GenericGraphQLError;
import graphql.servlet.GraphQLErrorHandler;

/**
 * Graphql Error 数据处理
 * @author liyi
 *
 */
@Component
public class DefaultGraphQLErrorHandler implements GraphQLErrorHandler {

    public static final Logger log = LoggerFactory.getLogger(DefaultGraphQLErrorHandler.class);

    private static final String DEFAULT_ERROR_CODE = "40000";

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        final List<GraphQLError> clientErrors = filterGraphQLErrors(errors);
        if (clientErrors.size() < errors.size()) {

            // Some errors were filtered out to hide implementation - put a generic error in place.
            //clientErrors.add(new GenericGraphQLError("Internal Server Error(s) while executing query"));

            errors.stream()
                .filter(error -> !isClientError(error))
                .forEach(error -> {
                    if(error instanceof Throwable) {
                        log.error("Error executing query!", (Throwable) error);
                    } else {
                    	if(error instanceof ExceptionWhileDataFetching){
                    		ExceptionWhileDataFetching e = (ExceptionWhileDataFetching) error;
                    		//添加异常数据
                    		clientErrors.add(new CusGenericGraphQLError(e.getMessage(),e.getPath()));
                    	}
                        log.error("Error executing query ({}): {}", error.getClass().getSimpleName(), error.getMessage());
                    }
                });
            
            if(clientErrors.size() == 0){
            	clientErrors.add(new GenericGraphQLError("Internal Server Error(s) while executing query"));
            }
        }

        return clientErrors;
    }

    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream()
            .filter(this::isClientError)
            .collect(Collectors.toList());
    }
    

    protected boolean isClientError(GraphQLError error) {
        return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
    }
    
	class CusGenericGraphQLError extends GenericGraphQLError{

		private String message_ext;

		private List<Object> path;

		private String errcode;

		
		public CusGenericGraphQLError(String message) {
			super(message);
			processMessage(message);
		}

		public CusGenericGraphQLError(String message, List<Object> path) {
			this(message);
			this.path = path;
		}

		private Pattern pattern = Pattern.compile(".*\\[errcode\\:(\\d*)\\]\\s*\\[errmsg\\:(.*)\\]");

		/**
		 * 解析ApiError 的数据格式
		 * @param message
		 */
		private void processMessage(String message) {
			Matcher matcher = pattern.matcher(message);
			if (matcher.find()) {
				this.errcode = matcher.group(1);
				this.message_ext = matcher.group(2);
			} else {
				this.errcode = DEFAULT_ERROR_CODE;
				this.message_ext = "未知错误";
			}
		}

		@Override
		public String getMessage() {
			return message_ext;
		}

		@Override
		public List<Object> getPath() {
			return path;
		}

		public String getErrcode() {
			return errcode;
		}

	}
}
