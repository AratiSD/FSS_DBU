package com.fss.saber.adapter.util;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fss.saber.adapter.model.RequestParamAeps;
import com.fss.saber.adapter.model.RequestParams;
import com.fss.saber.adapter.model.ResponseParamAeps;
import com.fss.saber.adapter.model.ResponseParams;
import com.google.common.hash.Hashing;

@Component
public class SessionIdGenerator {
	
	@Value("${hash.key}")
	public String hashKey;
	
	public static String hashSHA256(String string) {
		return Hashing.sha256().hashString(string, StandardCharsets.UTF_8).toString();
	}
	  public void generateHash(RequestParams req, ResponseParams resp) { String
	  hashStr = req.getUsername() + ":" + req.getRrn() + ":" + req.getAmt() + ":" +
	  hashKey + ":" + resp.getRrn() + ":" + resp.getRespCode(); String expectedHash
	  = hashSHA256(hashStr); resp.setSessionId(expectedHash); }
	 
	
	public String generateHash(RequestParamAeps req, ResponseParamAeps resp) {
		String hashStr = req.username + ":" + req.stan + ":" + req.amount + ":" + hashKey + ":" + resp.respcode;
		return resp.sessionId = hashSHA256(hashStr);
	}
	
}
