package com.aobei.train.model;

import java.util.ArrayList;
import java.util.List;

public class PlanInfo extends Plan {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Partner> partner_list = new ArrayList<>();

	public List<Partner> getPartner_list() {
		return partner_list;
	}

	public void setPartner_list(List<Partner> partner_list) {
		this.partner_list = partner_list;
	}

	@Override
	public String toString() {
		return "PlanInfo [partner_list=" + partner_list + "]";
	}
	
	
}
