package com.aobei.trainapi.server.bean;

import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.schema.type.MutationResult;

public class ApiResponse<T> implements ApiResult{

    MutationResult mutationResult;
    T t ;
    String msg;
    Errors errors;

    public MutationResult getMutationResult() {
        return mutationResult;
    }

    public void setMutationResult(MutationResult mutationResult) {
        this.mutationResult = mutationResult;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
