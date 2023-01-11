package com.fss.saber.adapter.piddata;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class AdditionalInfo {

	@XmlElement(name = "Param") public List<Param> param;

}