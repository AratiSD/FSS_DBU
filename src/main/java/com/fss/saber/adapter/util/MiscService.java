package com.fss.saber.adapter.util;

import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.util.iso8583.util.ByteHexUtil;

import com.fss.saber.adapter.model.CodeValue;
import com.fss.saber.adapter.model.RequestParamAeps;
import com.fss.saber.adapter.model.RequestParams;
import com.fss.saber.adapter.model.ResponseParamAeps;
import com.fss.saber.adapter.model.ResponseParams;

public final class MiscService {

	private static final Logger logger = LoggerFactory.getLogger(MiscService.class);
	
	public static final ResponseParams getMainKeys(RequestParams message) {
		ResponseParams response = new ResponseParams();
		response.setStatus(true);
		response.setMsg("Success");
		response.setMkskKeys("11111111111111111111111111111111#E5B6AC8CB1147317EDBB065494F20BED#163AC02F");
		return response;
	}

	public static final ResponseParams getSessionKeys(RequestParams message) {
		ResponseParams response = new ResponseParams();
		response.setStatus(true);
		response.setMsg("SDK Validated Success");
		response.setMkskKeys("7BDE22D9F433B9B85722A478609FBC48");
		response.setAuthid("A27F54B6");
		return response;
	}
	
	public static  byte[] decryptTDES(byte[] data, byte[] key) {
		try {
			if(key.length == 16) key = ByteHexUtil.concat(key, 0, 16, key, 0, 8);
			final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			final SecretKeySpec keySpec = new SecretKeySpec(key, "DESede");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			return cipher.doFinal(data);
		} catch (Exception e) {logger.error("decryptTDES", e);}
		return null;
	}
	
	public static byte[] encryptTDES(byte[] data, byte[] key) {
		try {
			if(key.length == 16) key = ByteHexUtil.concat(key, 0, 16, key, 0, 8);
			final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			final SecretKeySpec keySpec = new SecretKeySpec(key, "DESede");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			return cipher.doFinal(data);
		} catch (Exception e) {logger.error("encryptTDES", e);}
		return null;		
	}

	public static ResponseParamAeps getAepsBanks(RequestParamAeps message) {
		ResponseParamAeps response = new ResponseParamAeps();
		response.status = true;
		response.msg = "SUCCESS";
		response.respcode = "00";
		response.codeValues = new ArrayList<>();
		response.codeValues.add(new CodeValue("508505", "Bank of India"));
		return response;
	}
}
