package com.aobei.train.model;

import custom.bean.Programme_type;

/**
 * Created by mr_bl on 2018/5/8.
 */
public class CompensationInfo extends VOrderUnit{

    private Compensation compensation;

    private Users operator_create;

    private Users operator_confirm;

    private Programme_type programmeType;

    public Compensation getCompensation() {
        return compensation;
    }

    public void setCompensation(Compensation compensation) {
        this.compensation = compensation;
    }

    public Users getOperator_create() {
        return operator_create;
    }

    public void setOperator_create(Users operator_create) {
        this.operator_create = operator_create;
    }

    public Users getOperator_confirm() {
        return operator_confirm;
    }

    public void setOperator_confirm(Users operator_confirm) {
        this.operator_confirm = operator_confirm;
    }

    public Programme_type getProgrammeType() {
        return programmeType;
    }

    public void setProgrammeType(Programme_type programmeType) {
        this.programmeType = programmeType;
    }
}
