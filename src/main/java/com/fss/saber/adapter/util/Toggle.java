package com.fss.saber.adapter.util;

public final class Toggle {

	private boolean value;

	public Toggle() {}

	public Toggle(final boolean value) {
		this.value = value;
	}

	//returns new value
	public final boolean toggleAndGet() {
		return value ^= true;
	}
	
	public final boolean getAndToggle() {
		return !(value ^= true);
	}

	public final boolean get() {
		return value;
	}

	public final void set(final boolean value) {
		this.value = value;
	}
	
	public static void main(String[] args) {
		Toggle toggle = new Toggle(false);
		System.out.println(toggle.get());
		System.out.println(toggle.toggleAndGet());
		System.out.println(toggle.getAndToggle());
		System.out.println(toggle.getAndToggle());
	}
}
