package com.pcwk.ehr.cmn;

import java.util.Calendar;

public class DateUtil {
	/**
	 * 년/월.일
	 * @param cn
	 * @return
	 */
	public static String toDateYMD(Calendar cn) {
		String formatDate = "";
		formatDate = cn.get(Calendar.YEAR) +
		"/"+(cn.get(Calendar.MONTH)+1)
		+"/"+cn.get(Calendar.DAY_OF_MONTH);
		
		return formatDate;
	}
	public static String toDateYM(Calendar cn) {
		String formatDate = "";
		formatDate = cn.get(Calendar.YEAR) +
		"/"+(cn.get(Calendar.MONTH)+1);
		
		return formatDate;
	}
}
