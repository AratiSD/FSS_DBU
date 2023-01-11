package com.fss.saber.adapter.piddata;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "DeviceInfo")
public class DeviceInfo {

	@XmlAttribute(name = "dpId") public String                               dpId;
	@XmlAttribute(name = "rdsId") public String                       rdsId;
	@XmlAttribute(name = "rdsVer") public String                      rdsVer;
	@XmlAttribute(name = "mi") public String                          mi;
	@XmlAttribute(name = "mc") public String                          mc;
	@XmlAttribute(name = "dc") public String                          dc;
	@XmlElement (name = "additional_info") public AdditionalInfo additional_info;
}
