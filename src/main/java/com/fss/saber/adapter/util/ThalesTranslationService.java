package com.fss.saber.adapter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ThalesTranslationService implements TranslationService {

	private static final Logger logger = LoggerFactory.getLogger(ThalesTranslationService.class);
	
	public final HSMResponse fromZPKToZPK(final HSMConfig hsmConfig, final String pan12, final String pinblock, final PinBlockFormat sourceFormat, final String sourceZPK,
			final PinBlockFormat targetFormat, final String targetZPK) {
		try {
			final String      command     = new StringBuilder().append("0000CC").append(sourceZPK).append(targetZPK).append(hsmConfig.maximumPinLength)
											.append(pinblock).append(sourceFormat).append(targetFormat).append(pan12).toString();
			final String      response    = ThalesHSMConnect.send(hsmConfig, command);
			final HSMResponse hsmResponse = new HSMResponse(response.substring(6, 8));
			if (hsmResponse.isSuccess) hsmResponse.value = response.substring(10, 26);
			return hsmResponse;
		} catch (Exception e) {logger.error("",e);}
		return HSMResponse.IO;
	}

	public final HSMResponse fromTPKToZPK(final HSMConfig hsmConfig, final String pan12, final String sourcePinBlock, final PinBlockFormat sourceFormat, final String sourceTPK,
			final PinBlockFormat targetFormat, final String targetZPK) {
		try {
			final String      command     = new StringBuilder().append("0000CA").append(sourceTPK).append(targetZPK).append(hsmConfig.maximumPinLength)
											.append(sourcePinBlock).append(sourceFormat).append(targetFormat).append(pan12).toString();
			final String      response    = ThalesHSMConnect.send(hsmConfig, command);
			final HSMResponse hsmResponse = new HSMResponse(response.substring(6, 8));
			if (hsmResponse.isSuccess) hsmResponse.value = response.substring(10, 26);
			return hsmResponse;
		} catch (Exception e) {logger.error("",e);}
		return HSMResponse.IO;
	}


}
