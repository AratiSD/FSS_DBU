package com.fss.saber.adapter.piddata;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class Test {

	public static void main(String[] args) throws JAXBException {
		test1();
		System.exit(0);
		
		JAXBContext  xContext     = JAXBContext.newInstance(PidData.class, DeviceInfo.class);
		Marshaller   marshaller   = xContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		final DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.additional_info = new AdditionalInfo();
		deviceInfo.additional_info.param = new ArrayList<>();
		deviceInfo.additional_info.param.add(new Param("srno", "3576834"));
		deviceInfo.additional_info.param.add(new Param("sysid", "bff4acb55268a5d9"));
		
		StringWriter sw = new StringWriter();
		marshaller.marshal(deviceInfo, sw);
		System.out.println(sw);
		
		Unmarshaller unmarshaller = xContext.createUnmarshaller();
		DeviceInfo      data         = (DeviceInfo) unmarshaller.unmarshal(new StringReader(sw.toString()));
		System.err.println(data.additional_info.param.size());
	}
	
	
	private static void test1() throws JAXBException {
		JAXBContext  xContext     = JAXBContext.newInstance(PidData.class, DeviceInfo.class);
		Marshaller   marshaller   = xContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		String piddata = "<PidData>\n   <Data type=\"X\">MjAyMS0wNS0yNVQwNTo1NTowNYCVzDczEhi66pm82rr3Zvx486hsyF+wnlbgLSlgGC5JvENZyOSkLL2+nfzr4TLg74Ngda2Run2rpOUyV+hwXUeSfmpqoui893Og+kB7TsKR4+HVFGJHyPsoQajujiNRxp8jqzHEmvKbSusI3Cj9FbrqFNgy40qmDiH+SrSkUIk9DBGCfL2wqlEtD+4n1YkxLvfA+E0aM7XfaRDvqODPG8QMikUEJesiP6RwngUYQL57OMemXE2/szvEYdzfpynUeCTJurFV0e7hq4wdiCIVrQKGvnBVP1cKpLcVj5jfDcdi2jedmbfI37fZrx5W+UtgqrinbeTAkMYle3JgRVzRnChesxSVQPRyM483XLZN91SJXvb20JJKpBj7IjURbUtCMa3Zg7H3S7EsCVJ2yKjv4iM9pAirw0she7GRy6bz7hdtNSi3a2TRwkSUl2y7yxk7UoJ4V/blh+gmoCthGTZab6OVbmJxNytYUxdY3yLCl9rV68Qu0bIvswOJwHU8EbXDVWCcxe6r8UbDApDCA/2U4mmlPTBeDjyXnq2sbpYKYN0Ap/VYGKPBrUWZeZZpXTPBnL7BBSQvTovTemm1mlRgzJ8jp5A9+JxVrRPu1ZblLtuJ9woylnfW78+zwymTS7Nnblnrrbi/BzCjohTA4GPYacoof8UQnq64Ad21yz5s7VfiuSyyXvrzFk2v25F7VF1rzxCmjPyefUzmk5U4xn36QhvwdBZ+II1O1Y8qbWVXftgQqEfEB39CJ9LFZ9VRjgkz6fmFBmonSX6KsNxJsLGOncAqsrcFEc/HP7I9tZX+/cmZpqrpPDz5UUixwMDGdqzyBwbUCM98BrtdTceZam2oS2N6IkwV+oDDYfVGySraJYBJRnLSUr+I0iXvZurzsHOQm5lK+mgJGEOPypoiM2mQR9qmHKjamWhim6z2X8Mi3G/U0SzHvx8gwXswo/uBeY7Uge04dVQeP5hzDQZwftopn77/07E3BsnAW+sb9fpWseW+QpTI1nrXSRV2dhrFUoCRAQBbrg01UNzIqgW0XXJPxghWDyM3BUMxlEdpnsVGbqlOACQSLJUKPWHQrHjONBQfKrCXsQRkPQegdnD/rxd6iaWjZBlMJkaxgJXAhYrAp29WBRWoqsCsbvoC16I5ksJu16TlaAJbdqVTd+FFis0rNzB4qVcYLpgqT9w4Xo2JnlLUO8DDOGm0Iy3gKXsWOPobgoI8I2fvgWuPwHdXxFxY79QpPYfZTwGAtRksRUeTVYfLSFmrMOd71bPWQLb30ZKw6/54yc3UoCBDXqm+z8YIQdQaF6q9z6a1soDHnigYDPa0TWVl1wf6Vz/ek5UGgUuF0a/Lzbt4UB8uqchzVzGFBmVgzRPm1B0NeKYuiiNbExBQpK96gD2JLNPkKN1FYuDvJULg/FmL28kw+IHHqR74BpC+ecAg2g==</Data>\n   <DeviceInfo dc=\"ef2cf616-c3f6-4b78-82c8-ea99f4b1f4bb\" dpId=\"MANTRA.MSIPL\" mc=\"MIIEGjCCAwKgAwIBAgIGAXlk4jR7MA0GCSqGSIb3DQEBCwUAMIHqMSowKAYDVQQDEyFEUyBNYW50cmEgU29mdGVjaCBJbmRpYSBQdnQgTHRkIDcxQzBBBgNVBDMTOkIgMjAzIFNoYXBhdGggSGV4YSBvcHBvc2l0ZSBHdWphcmF0IEhpZ2ggQ291cnQgUyBHIEhpZ2h3YXkxEjAQBgNVBAkTCUFobWVkYWJhZDEQMA4GA1UECBMHR3VqYXJhdDEdMBsGA1UECxMUVGVjaG5pY2FsIERlcGFydG1lbnQxJTAjBgNVBAoTHE1hbnRyYSBTb2Z0ZWNoIEluZGlhIFB2dCBMdGQxCzAJBgNVBAYTAklOMB4XDTIxMDUyMzExMjkyMFoXDTIxMDYxMjA4NDAxNlowgbAxJTAjBgNVBAMTHE1hbnRyYSBTb2Z0ZWNoIEluZGlhIFB2dCBMdGQxHjAcBgNVBAsTFUJpb21ldHJpYyBNYW51ZmFjdHVyZTEOMAwGA1UEChMFTVNJUEwxEjAQBgNVBAcTCUFITUVEQUJBRDEQMA4GA1UECBMHR1VKQVJBVDELMAkGA1UEBhMCSU4xJDAiBgkqhkiG9w0BCQEWFXN1cHBvcnRAbWFudHJhdGVjLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALeGqiC2ARZkifWDL7aq18oR6f1uv/Yiz2ylMAz+cvl5j6hY4Cnv8E9VeqUWnaFA25pdg+HTQSJWy13BBrYVg7ZPeHz2SEkmEH79SWBlq3BqHPYqS1mfrbcVTBtntTDm7onD7wfSwW+3qHaLLimCZZVvARv77uA8jY6cg/Dcl+CFNeI7Cjgk0u+/SqhC2NwptLRJxY1maaRoVwkpV3Fe/2IF2RNN1dEQPolplAF5jMiFDxv5IKlwguwMh99M6ZR2xCNOCitrY3zYTwK9N1kQPMfrlAAHETM/zwcqjPO4gmHC24yxEUxSUT72xQH/reObON7SDdI/+LAfzRF0OvFyuZ8CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAZupYuTPzyWBvf+qa3YkV9Xpj2KRGFoOjMmjl7NmwJELWQPO0ZeaSXZl5NlnW6jLSrgP6NxUgagBIH745k85aMPaNQs1vu4SVBNUj3YhVolbdl0d9/i8MYVIIzMmquGW77tAVBCgFmqQfSYdRC6DCpn40BNEyP3qI5uIiPSC0dfMj3Oac1YBYyJmDZe89fW/I3Mx/PMFPZ1JTGI7m2pzPVpYrjG5jXU/mjQIHHxxyM/9bUq/kkM+bFDlIu86qLl0ZPIGQRKeszC9dd4IHrzMlQSWLMOnPspzo4l9vV2RY7fCZlbMfJuGdI2hLrFqOnBE1xnLNd5/+1rVq+xF1SYZ9/A==\" mi=\"MFS100\" rdsId=\"MANTRA.AND.001\" rdsVer=\"1.0.4\">\n      <additional_info>\n         <Param name=\"srno\" value=\"3576834\"/>\n         <Param name=\"sysid\" value=\"bff4acb55268a5d9\"/>\n         <Param name=\"ts\" value=\"2021-05-25T05:55:06+05:30\"/>\n      </additional_info>\n   </DeviceInfo>\n   <Hmac>C5fsdbKNVyzXoRmq03h7wpUoPKiQTwCZzpN/XZ3/micYyH0zEzq9CLY55FLUWumx</Hmac>\n   <Resp errCode=\"0\" errInfo=\"Capture Success\" fCount=\"1\" fType=\"0\" iCount=\"0\" iType=\"0\" nmPoints=\"64\" pCount=\"0\" pType=\"0\" qScore=\"91\"/>\n   <Skey ci=\"20221021\">dycAelbr1fFM3CWYf2Yugq4B4eXYBf5xm9EOlFLUY8/uEHCV6yEHIjZCVsyBWQIS8TKwEp5gQ5xSz0pcpf+BTXg0654zsbTRgmYMcJ7AKSK14EIZWVQzFUapiLhCa+Ozxq4KRL0E/DoVmOD/XC+b6J5BRv458onvDW7U5gYQ4GQyD4DiVyW4V7sOcbmwE3CVr+9mZCmEFyP3tSifkiAPQ/CCrkIOAgv5psut6JZ/hbKwbrASEKdxe3HeheHXNTpz3Asx3ICy6AUemfym8UUqtF27Ef9mN8+FCoIbBqEtd8c08fCD9Jph1NpvhG4wm+x9en4iePKL2qLzMmceFKedjw==</Skey>\n</PidData>";
		System.out.println(piddata);
		StringWriter sw = new StringWriter();
		Unmarshaller unmarshaller = xContext.createUnmarshaller();
		PidData      data         = (PidData) unmarshaller.unmarshal(new StringReader(piddata));
		System.out.println(data.deviceInfo.additional_info.param.size());
		marshaller.marshal(data, sw);
		System.out.println(sw);
	}
}
