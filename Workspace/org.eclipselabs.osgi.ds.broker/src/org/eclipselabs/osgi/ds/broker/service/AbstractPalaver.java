package org.eclipselabs.osgi.ds.broker.service;


public abstract class AbstractPalaver<T extends Object> implements IPalaver<T> {

	private String introduction;
	private boolean claimAttention;
	
	protected AbstractPalaver( String introduction ) {
		this.introduction = introduction;
	}

	protected AbstractPalaver( String introduction, boolean claimAttention ) {
		this( introduction );
		this.claimAttention = claimAttention;
	}

	@Override
	public String getIntroduction() {
		return this.introduction;
	}

	@Override
	public boolean claimAttention() {
		return this.claimAttention;
	}

	@Override
	public void setClaimAttention(boolean choice) {
		this.claimAttention = choice;
	}

}
