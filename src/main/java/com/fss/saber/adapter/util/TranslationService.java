package com.fss.saber.adapter.util;

public interface TranslationService {

	public abstract HSMResponse fromZPKToZPK(final HSMConfig hsmConfig, final String pan12, final String pinblock, final PinBlockFormat sourceFormat, final String sourceZPK,
			final PinBlockFormat targetFormat, final String targetZPK) ;

	public abstract HSMResponse fromTPKToZPK(final HSMConfig hsmConfig, final String pan12, final String sourcePinBlock, final PinBlockFormat sourceFormat, final String sourceTPK,
			final PinBlockFormat targetFormat, final String targetZPK);

}
