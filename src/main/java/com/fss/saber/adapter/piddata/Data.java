package com.fss.saber.adapter.piddata;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

public class Data {

	@XmlAttribute(name = "type") 
	public String type;
	
	@XmlValue 
	public String value;
}
