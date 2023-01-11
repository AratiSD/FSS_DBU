package com.fss.saber.adapter.util;


public final class ThalesHSMService implements HSMService {

	private final TranslationService translationService = new ThalesTranslationService();

	@Override
	public final String getName() {
		return "THALES";
	}

	@Override
	public final TranslationService translator() {
		return translationService;
	}


}
