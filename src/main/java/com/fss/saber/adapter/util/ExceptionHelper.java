package com.fss.saber.adapter.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHelper {

	public static final String exceptionToString(final Exception e) {
		if (e == null) return null;
		final StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
