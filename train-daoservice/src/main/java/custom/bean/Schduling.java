package custom.bean;

import java.io.Serializable;

import com.aobei.train.model.Order;
import com.aobei.train.model.ServiceUnit;

public class Schduling implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Order order;
	private ServiceUnit serviceUnit;
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public ServiceUnit getServiceUnit() {
		return serviceUnit;
	}
	public void setServiceUnit(ServiceUnit serviceUnit) {
		this.serviceUnit = serviceUnit;
	}
	@Override
	public String toString() {
		return "Schduling [order=" + order + ", serviceUnit=" + serviceUnit + "]";
	}
	
}
