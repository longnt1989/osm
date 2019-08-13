/**
*
* VIETEK JSC - VOpen - Vietnam open framework
* Create date: Jul 19, 2016
* Author: tuanpa
*
*/
package com.pt.osm.common;

import java.security.MessageDigest;

/**
 * 
 * @author VuD
 * 
 */

public class SecurityUtils {
	public static synchronized String encryptMd5(String value) {
		String result = null;
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			digester.update(value.getBytes());
			byte[] hash = digester.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0"
							+ Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
			result = hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
