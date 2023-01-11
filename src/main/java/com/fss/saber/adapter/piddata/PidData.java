package com.fss.saber.adapter.piddata;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PidData")
public class PidData {

	@XmlElement(name = "Resp") public Resp             resp;
	@XmlElement(name = "DeviceInfo") public DeviceInfo deviceInfo;
	@XmlElement(name = "Skey") public Skey             skey;
	@XmlElement(name = "Hmac") public String           hmac;
	@XmlElement(name = "Data") public Data             data;
}
