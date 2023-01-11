package com.fss.saber.adapter.piddata;

import jakarta.xml.bind.annotation.XmlAttribute;


public class Param {

	@XmlAttribute(name = "name") public String name;

	@XmlAttribute(name = "value") public String value;

	public Param() {
		super();
	}

	public Param(String name, String value) {
		super();
		this.name  = name;
		this.value = value;
	}

}
