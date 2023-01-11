package com.fss.saber.adapter.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public final class TLV {

	private static final int   width  = 60;
	private static final int   size   = 3;
	public static final String format = "%0" + size + "d";

	public static final String separator = "-".repeat(width * 2 + 4);

	private static final String[] spaces = new String[width * 2 - 10];

	static {
		for (int i = 0; i < spaces.length; i++) {
			spaces[i] = " ".repeat(i);
		}
	}

	private final Map<String, String> store;

	public TLV() {
		store = new TreeMap<>();
	}

	public TLV(final boolean sort) {
		if (sort) store = new TreeMap<>();
		else store = new LinkedHashMap<>();
	}

	public final TLV put(final String k, final String v) {
		store.put(k, v);
		return this;
	}

	public final TLV put(final int k, final String v) {
		store.put(String.format(format, k), v);
		return this;
	}

	public final String get(String k) {
		return store.get(k);
	}
	
	public final String getOrElse(String k, final String elseVal) {
		return store.containsKey(k) ? store.get(k) : elseVal;
	}

	public final String remove(String k) {
		return store.remove(k);
	}

	public final TLV keepAll(final Collection<String> tags) {
		final Map<String, String> newmap = new HashMap<>();
		tags.forEach(tag -> { if (store.get(tag) != null) newmap.put(tag, store.get(tag)); });
		store.clear();
		store.putAll(newmap);
		return this;
	}

	public final TLV removeAll(final Collection<String> tags) {
		tags.forEach(tag -> store.remove(tag));
		return this;
	}

	public static final TLV parse(final String tlvString) {
		TLV tlv = new TLV();
		if (tlvString == null) return tlv;
		int i = 0;
		try {
			while (i < tlvString.length()) {
				String tagname = tlvString.substring(i, i + size);
				i = i + size;
				int taglen = Integer.parseInt(tlvString.substring(i, i + size));
				i = i + size;
				tlv.store.put(tagname, tlvString.substring(i, i + taglen));
				i = i + taglen;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tlv;
	}

	public final String build() {
		final StringBuilder sb = new StringBuilder(50);
		store.forEach((key, value) -> {
			if (value == null) value = "";
			sb.append(key);
			sb.append(String.format(format, value.length()));
			sb.append(value);
		});
		return sb.toString();
	}

	@Override
	public final String toString() {
		try {
			final Toggle        toggle = new Toggle(false);
			final StringBuilder sb     = new StringBuilder(30);
			sb.append("\r\n").append(separator).append("\r\n");
			store.forEach((key, value) -> {
				if (value.length() > (width - 10) && toggle.get()) sb.append("\r\n");
				else if (value.length() > (width - 10)) toggle.toggleAndGet();
				sb.append("| ").append(key).append(" : '").append(value).append("'").append(spaces[Math.max(width - (8 + value.length()), 0)]);
				//if (value.length() > (width- 8)) sb.append(spaces[width*2-value.length()-6]).append("|\r\n"); toggle.get();
				if (toggle.get()) {
					sb.append("|\r\n");
				} else if (value.length() > (width - 8)) sb.append(spaces[width * 2 - value.length() - 6]).append("|\r\n");
				toggle.toggleAndGet();
			});
			if (toggle.get()) sb.append("\r\n");
			sb.append(separator);
			return sb.toString();
		} catch (Exception e) {
			return ExceptionHelper.exceptionToString(e);
		}
	}

	public final String toString1() {
		try {
			final Toggle        toggle = new Toggle(true);
			final StringBuilder sb     = new StringBuilder(30);
			sb.append("\r\n").append(separator);
			store.forEach((key, value) -> {
				final int len = value.length();
				if(toggle.get()) sb.append("\r\n");
				
				if(toggle.get() && len > (width-10)) {
					sb.append(spaces[Math.max(0, width*2-len)]).append("|");
					toggle.getAndToggle();
				}
				else if(!toggle.get() && len > (width-10)) {
					sb.append(spaces[Math.max(0, width*2-len)]).append("|");
				}
				else {
					sb.append(spaces[Math.max(0, width-len+8)]);
				}
				sb.append("|").append(key).append(" : '").append(value).append("'");
				toggle.getAndToggle();
			});
			if (!toggle.get()) sb.append("\r\n");
			sb.append(separator);
			return sb.toString();
		} catch (Exception e) {
			return ExceptionHelper.exceptionToString(e);
		}
	}

	
	public static void main(String[] args) {
		TLV tlv = TLV
				.parse("001344ihA1tNMPeD0p7GZHlyBXOvYwGNhU8snX4/apjmJZmLlnyCkhSwhfr7v84W3XYbkBhK3r/FGki58YtOkjniIyUsVCFlGfLaqXooUA1mahDj8ElrMJr8Dzxsxe1dMo+xSnKibt0SxoJEK0+34cUApnDl6EDNRULan/ZaDqRVXOkErbFU3StRlPP3GKwFXDCUXRNYhi1ZNjzz/M05CU3xFijRyr8/z3vIBwtz+krkSSl4lioqnXNtJNTM9NykDtP60UhC6dpOaxlwRJVfLWtzq1mFDuBhNF/TPFNwQEvWzRIUlggd0LYbS5GmkwbsR2mFC1Cq/l4dHIrujRFHzTIyiitQ==00200820171105003064n5mlcHs/LYdjE0RjiLfksgw6dOIHASratGXNUL7Z2lSHBigVXyEvqNxUjho23Hh8007001Y0080192019-06-07T17:12:00");
		System.out.println(tlv.toString());
	}
}
