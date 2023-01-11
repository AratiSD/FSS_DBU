package com.fss.saber.adapter.piddata;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Resp {

	@XmlAttribute(name = "errCode") public int    errCode;
	@XmlAttribute(name = "errInfo") public String errInfo;
	@XmlAttribute(name = "fCount") public int     fCount;
	@XmlAttribute(name = "fType") public int      fType;
	@XmlAttribute(name = "nmPoints") public int   nmPoints;
	@XmlAttribute(name = "qScore") public int     qScore;
}
