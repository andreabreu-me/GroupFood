package com.dwbook.phonebook.representations;

import java.util.List;

/**
 * Created by howard on 11/4/14.
 */

public class OrderView {

    private final List<UserView> userView;
    private final List<MerchantView> merchantView;
    private final float userViewTotal;
    private final float merchantViewTotal;

    public OrderView() {
        this.userView=null;
        this.merchantView=null;
        this.userViewTotal=0;
        this.merchantViewTotal=0;
    }

    public OrderView(List<UserView> userView, List<MerchantView> merchantView, float userViewTotal, float merchantViewTotal) {
        this.userView=userView;
        this.merchantView=merchantView;
        this.userViewTotal=userViewTotal;
        this.merchantViewTotal=merchantViewTotal;
    }

	public List<MerchantView> getMerchantView() {
		return merchantView;
	}

	public List<UserView> getUserView() {
		return userView;
	}

	public float getUserViewTotal() {
		return userViewTotal;
	}

	public float getMerchantViewTotal() {
		return merchantViewTotal;
	}
	
}