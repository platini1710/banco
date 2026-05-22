package com.bci.tareas.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidatorSimple {

	    private static final  String EMAIL_PATTERN = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	    public static boolean isValid( String email) {
	    	      Matcher matcher = pattern.matcher(email);
	        return matcher.matches();
	    }
}
