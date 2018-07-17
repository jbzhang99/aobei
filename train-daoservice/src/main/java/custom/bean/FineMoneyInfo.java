package custom.bean;

import com.aobei.train.model.FineMoney;
import com.aobei.train.model.Order;
import com.aobei.train.model.ServiceUnit;
import com.aobei.train.model.Users;

/**
 * Created by mr_bl on 2018/6/20.
 */
public class FineMoneyInfo extends FineMoney{
    private Order order;

    private ServiceUnit unit;

    private Users apply_operator;

    private Users confirm_operators;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ServiceUnit getUnit() {
        return unit;
    }

    public void setUnit(ServiceUnit unit) {
        this.unit = unit;
    }

    public Users getApply_operator() {
        return apply_operator;
    }

    public void setApply_operator(Users apply_operator) {
        this.apply_operator = apply_operator;
    }

    public Users getConfirm_operators() {
        return confirm_operators;
    }

    public void setConfirm_operators(Users confirm_operators) {
        this.confirm_operators = confirm_operators;
    }
}
