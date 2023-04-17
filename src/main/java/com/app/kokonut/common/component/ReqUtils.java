package com.app.kokonut.common.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReqUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ReqUtils.class);
	
	/***
	 * HttpServletRequest에서 받은 parameter를 Map으로 전환 & 값 형변환
	 * @param HttpServletRequest
	 * @return Map<String, Object>
	 */
	public static HashMap<String, Object> getParameterMapAndRevertCharset(HttpServletRequest request) {
		HashMap<String, Object>  map = new HashMap<String, Object>();
        try {
            Map<String, String[]> paramerterMap = request.getParameterMap();
            Iterator<String> iter = paramerterMap.keySet().iterator();
            String key = null;
            String[] value = null;
            while(iter.hasNext()) {
                key = (String)iter.next();
                value = (String []) paramerterMap.get(key);
                if(value.length > 1){
                	String[] reValue = new String[value.length];
                	for(int i=0; i<value.length; i++){
                		String rVal = value[i];
                		if(value[i] == null || value[i].equals("") || value[i].equals("null")){
                    		rVal = "";
                    	}else{
                    		rVal = filter(rVal);
                    	}
                		reValue[i] = rVal;
                	}
                	map.put(key, reValue);
                } else {
                	String rVal = value[0];
                	if(value[0] == null || value[0].equals("") || value[0].equals("null")){
                		rVal = "";
                	}else{
                		rVal = new String(filter(rVal).getBytes("8859_1"), "utf-8");

                	}
                	map.put(key, rVal);
                }
            }
        } catch(Exception e) {
        	logger.error(e.getMessage());
        }
        
        return map;
	}


	
	public static String filter(String value) {
    	if(value==null) {
            return null;
        }
    	
    	//You'll need to remove the spaces from the html entities below    	
    	value = value.replaceAll("&lt;", "&#60;").replaceAll("&gt;", "&#62;");
    	value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    	value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
    	value = value.replaceAll("'", "&#39;");
    	value = value.replaceAll("\"", "&#34;");
    	value = value.replaceAll("eval\\((.*)\\)", "");
    	value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
    	//value = value.replaceAll("script", "");
    	value = value.replaceAll("onabort","").
				        replaceAll("onactivate","").
				        replaceAll("afterprint",""). 
				        replaceAll("afterupdate","").
				        replaceAll("beforeactivate","").
				        replaceAll("beforecopy","").
				        replaceAll("beforecut","").
				        replaceAll("beforedeactivate","").
				        replaceAll("beforeeditfocus","").
				        replaceAll("beforepaste","").
				        replaceAll("beforeprint","").
				        replaceAll("beforeunload","").
				        replaceAll("beforeupdate","").
				        replaceAll("blur","").
				        replaceAll("bounce","").
				        replaceAll("cellchange","").
				        //replaceAll("change","").
				        replaceAll("click","").
				        replaceAll("contextmenu","").
				        replaceAll("controlselect","").
				        replaceAll("copy","").
				        replaceAll("cut","").
				        replaceAll("dataavailable","").
				        replaceAll("datasetchanged","").
				        replaceAll("datasetcomplete","").
				        replaceAll("dblclick","").
				        replaceAll("deactivate","").
				        replaceAll("drag","").
				        replaceAll("dragend","").
				        replaceAll("dragenter","").
				        replaceAll("dragleave","").
				        replaceAll("dragover","").
				        replaceAll("dragstart","").
				        replaceAll("drop","").
				        //replaceAll("error","").
				        //replaceAll("errorupdate","").
				        replaceAll("filterchange","").
				        replaceAll("finish","").
				        //replaceAll("focus","").
				        replaceAll("focusin","").
				        replaceAll("focusout","").
				        //replaceAll("help","").
				        replaceAll("keydown","").
				        replaceAll("keypress","").
				        replaceAll("keyup","").
				        replaceAll("layoutcomplete","").
				        //replaceAll("load","").
				        replaceAll("losecapture","").
				        replaceAll("mousedown","").
				        replaceAll("mouseenter","").
				        replaceAll("mouseleave","").
				        replaceAll("mousemove","").
				        replaceAll("mouseout","").
				        replaceAll("mouseover","").
				        replaceAll("mouseup","").
				        replaceAll("mousewheel","").
				        replaceAll("move","").
				        replaceAll("moveend","").
				        replaceAll("movestart","").
				        replaceAll("paste","").
				        replaceAll("propertychange","").
				        replaceAll("readystatechange","").
				        replaceAll("reset","").
				        replaceAll("resize","").
				        replaceAll("resizeend","").
				        replaceAll("resizestart","").
				        replaceAll("rowenter","").
				        replaceAll("rowexit","").
				        replaceAll("rowsdelete","").
				        replaceAll("rowsinserted","").
				        replaceAll("scroll","").
				        //replaceAll("select","").
				        replaceAll("selectionchange","").
				        replaceAll("selectstart","").
				        replaceAll("start","").
				        replaceAll("stop","").
				        replaceAll("submit","").replaceAll("eval\\((.*)\\)", "");
    	return value;
    }

	public static String unFilter(String value) {
		if(value==null) {
			return null;
		}
		// org.apache.commons.lang3.StringEscapeUtils.unescapeHtml3
		// BASIC_UNESCAPE
		value = value.replaceAll("&quot;", "\"");
		value = value.replaceAll("&amp;", "&");
		value = value.replaceAll("&lt;", "<");
		value = value.replaceAll("&gt;", ">");
		value = value.replaceAll("&lt;", "<");

		// ISO8859_1_UNESCAPE
		value = value.replaceAll("&nbsp;",	"\u00A0"	); // non-breaking space
		value = value.replaceAll("&iexcl;",	"\u00A1"	); // inverted exclamation mark
		value = value.replaceAll("&cent;",	"\u00A2"	); // cent sign
		value = value.replaceAll("&pound;",	"\u00A3"	); // pound sign
		value = value.replaceAll("&curren;",	"\u00A4"	); // currency sign
		value = value.replaceAll("&yen;",	    "\u00A5"	); // yen sign = yuan sign
		value = value.replaceAll("&brvbar;",	"\u00A6"	); // broken bar = broken vertical bar
		value = value.replaceAll("&sect;",	"\u00A7"	); // section sign
		value = value.replaceAll("&uml;",	    "\u00A8"	); // diaeresis = spacing diaeresis
		value = value.replaceAll("&copy;",	"\u00A9"	); // copyright sign
		value = value.replaceAll("&ordf;",	"\u00AA"	); // feminine ordinal indicator
		value = value.replaceAll("&laquo;",	"\u00AB"	); // left-pointing double angle quotation mark = left pointing
		value = value.replaceAll("&not;",	    "\u00AC"	); // not sign
		value = value.replaceAll("&shy;",	    "\u00AD"	); // soft hyphen = discretionary hyphen
		value = value.replaceAll("&reg;",	    "\u00AE"	); // registered trademark sign
		value = value.replaceAll("&macr;",	"\u00AF"	); // macron = spacing macron = overline = APL overbar
		value = value.replaceAll("&deg;",	    "\u00B0"	); // degree sign
		value = value.replaceAll("&plusmn;",	"\u00B1"	); // plus-minus sign = plus-or-minus sign
		value = value.replaceAll("&sup2;",	"\u00B2"	); // superscript two = superscript digit two = squared
		value = value.replaceAll("&sup3;",	"\u00B3"	); // superscript three = superscript digit three = cubed
		value = value.replaceAll("&acute;",	"\u00B4"	); // acute accent = spacing acute
		value = value.replaceAll("&micro;",	"\u00B5"	); // micro sign
		value = value.replaceAll("&para;",	"\u00B6"	); // pilcrow sign = paragraph sign
		value = value.replaceAll("&middot;",	"\u00B7"	); // middle dot = Georgian comma = Greek middle dot
		value = value.replaceAll("&cedil;",	"\u00B8"	); // cedilla = spacing cedilla
		value = value.replaceAll("&sup1;",	"\u00B9"	); // superscript one = superscript digit one
		value = value.replaceAll("&ordm;",	"\u00BA"	); // masculine ordinal indicator
		value = value.replaceAll("&raquo;",	"\u00BB"	); // right-pointing double angle quotation mark = right pointing
		value = value.replaceAll("&frac14;",	"\u00BC"	); // vulgar fraction one quarter = fraction one quarter
		value = value.replaceAll("&frac12;",	"\u00BD"	); // vulgar fraction one half = fraction one half
		value = value.replaceAll("&frac34;",	"\u00BE"	); // vulgar fraction three quarters = fraction three quarters
		value = value.replaceAll("&iquest;",	"\u00BF"	); // inverted question mark = turned question mark
		value = value.replaceAll("&Agrave;",	"\u00C0"	); // ppercase A, grave accent
		value = value.replaceAll("&Aacute;",	"\u00C1"	); // uppercase A, acute accent
		value = value.replaceAll("&Acirc;",	"\u00C2"	); // uppercase A, circumflex accent
		value = value.replaceAll("&Atilde;",	"\u00C3"	); // uppercase A, tilde
		value = value.replaceAll("&Auml;",	"\u00C4"	); // uppercase A, umlaut
		value = value.replaceAll("&Aring;",	"\u00C5"	); // uppercase A, ring
		value = value.replaceAll("&AElig;",	"\u00C6"	); // uppercase AE
		value = value.replaceAll("&Ccedil;",	"\u00C7"	); // uppercase C, cedilla
		value = value.replaceAll("&Egrave;",	"\u00C8"	); // uppercase E, grave accent
		value = value.replaceAll("&Eacute;",	"\u00C9"	); // uppercase E, acute accent
		value = value.replaceAll("&Ecirc;",	"\u00CA"	); // uppercase E, circumflex accent
		value = value.replaceAll("&Euml;",	"\u00CB"	); // uppercase E, umlaut
		value = value.replaceAll("&Igrave;",	"\u00CC"	); // uppercase I, grave accent
		value = value.replaceAll("&Iacute;",	"\u00CD"	); // uppercase I, acute accent
		value = value.replaceAll("&Icirc;",	"\u00CE"	); // uppercase I, circumflex accent
		value = value.replaceAll("&Iuml;",	"\u00CF"	); // uppercase I, umlaut
		value = value.replaceAll("&ETH;",	    "\u00D0"	); // uppercase Eth, Icelandic
		value = value.replaceAll("&Ntilde;",	"\u00D1"	); // uppercase N, tilde
		value = value.replaceAll("&Ograve;",	"\u00D2"	); // uppercase O, grave accent
		value = value.replaceAll("&Oacute;",	"\u00D3"	); // uppercase O, acute accent
		value = value.replaceAll("&Ocirc;",	"\u00D4"	); // uppercase O, circumflex accent
		value = value.replaceAll("&Otilde;",	"\u00D5"	); // uppercase O, tilde
		value = value.replaceAll("&Ouml;",	"\u00D6"	); // uppercase O, umlaut
		value = value.replaceAll("&times;",	"\u00D7"	); // multiplication sign
		value = value.replaceAll("&Oslash;",	"\u00D8"	); // uppercase O, slash
		value = value.replaceAll("&Ugrave;",	"\u00D9"	); // uppercase U, grave accent
		value = value.replaceAll("&Uacute;",	"\u00DA"	); // uppercase U, acute accent
		value = value.replaceAll("&Ucirc;",	"\u00DB"	); // uppercase U, circumflex accent
		value = value.replaceAll("&Uuml;",	"\u00DC"	); // uppercase U, umlaut
		value = value.replaceAll("&Yacute;",	"\u00DD"	); // uppercase Y, acute accent
		value = value.replaceAll("&THORN;",	"\u00DE"	); // uppercase THORN, Icelandic
		value = value.replaceAll("&szlig;",	"\u00DF"	); // lowercase sharps, German
		value = value.replaceAll("&agrave;",	"\u00E0"	); // lowercase a, grave accent
		value = value.replaceAll("&aacute;",	"\u00E1"	); // lowercase a, acute accent
		value = value.replaceAll("&acirc;",	"\u00E2"	); // lowercase a, circumflex accent
		value = value.replaceAll("&atilde;",	"\u00E3"	); // lowercase a, tilde
		value = value.replaceAll("&auml;",	"\u00E4"	); // lowercase a, umlaut
		value = value.replaceAll("&aring;",	"\u00E5"	); // lowercase a, ring
		value = value.replaceAll("&aelig;",	"\u00E6"	); // lowercase ae
		value = value.replaceAll("&ccedil;",	"\u00E7"	); // lowercase c, cedilla
		value = value.replaceAll("&egrave;",	"\u00E8"	); // lowercase e, grave accent
		value = value.replaceAll("&eacute;",	"\u00E9"	); // lowercase e, acute accent
		value = value.replaceAll("&ecirc;",	"\u00EA"	); // lowercase e, circumflex accent
		value = value.replaceAll("&euml;",	"\u00EB"	); // lowercase e, umlaut
		value = value.replaceAll("&igrave;",	"\u00EC"	); // lowercase i, grave accent
		value = value.replaceAll("&iacute;",	"\u00ED"	); // lowercase i, acute accent
		value = value.replaceAll("&icirc;",	"\u00EE"	); // lowercase i, circumflex accent
		value = value.replaceAll("&iuml;",	"\u00EF"	); // lowercase i, umlaut
		value = value.replaceAll("&eth;",	    "\u00F0"	); // lowercase eth, Icelandic
		value = value.replaceAll("&ntilde;",	"\u00F1"	); // lowercase n, tilde
		value = value.replaceAll("&ograve;",	"\u00F2"	); // lowercase o, grave accent
		value = value.replaceAll("&oacute;",	"\u00F3"	); // lowercase o, acute accent
		value = value.replaceAll("&ocirc;",	"\u00F4"	); // lowercase o, circumflex accent
		value = value.replaceAll("&otilde;",	"\u00F5"	); // lowercase o, tilde
		value = value.replaceAll("&ouml;",	"\u00F6"	); // lowercase o, umlaut
		value = value.replaceAll("&divide;",	"\u00F7"	); // division sign
		value = value.replaceAll("&oslash;",	"\u00F8"	); // lowercase o, slash
		value = value.replaceAll("&ugrave;",	"\u00F9"	); // lowercase u, grave accent
		value = value.replaceAll("&uacute;",	"\u00FA"	); // lowercase u, acute accent
		value = value.replaceAll("&ucirc;",	"\u00FB"	); // lowercase u, circumflex accent
		value = value.replaceAll("&uuml;",	"\u00FC"	); // lowercase u, umlaut
		value = value.replaceAll("&yacute;",	"\u00FD"	); // lowercase y, acute accent
		value = value.replaceAll("&thorn;",	"\u00FE"	); // lowercase thorn, Icelandic
		value = value.replaceAll("&yuml;",	"\u00FF"	); // lowercase y, umlaut


		return value;
	}


}
