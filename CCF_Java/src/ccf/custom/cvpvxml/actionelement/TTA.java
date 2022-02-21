package ccf.custom.cvpvxml.actionelement;

import ccf.custom.Constants;

import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.Setting;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.AudiumException;

import java.util.HashMap;
import com.audium.server.xml.ActionElementConfig;
import com.audium.server.session.ActionElementData;
import com.audium.server.voiceElement.ActionElementBase;

public class TTA extends ActionElementBase implements ElementInterface {

	public static final String version = "1.6 2008-05-31 2:08 pm";

	public String getElementName() {

		return this.getClass().getName().substring(
				this.getClass().getName().lastIndexOf('.') + 1,
				this.getClass().getName().length());
	}

	/**
	 * This method returns the name of the folder in which this voice element
	 * resides. Return null if it is to appear directly under the Elements
	 * folder.
	 */
	public String getDisplayFolderName() {
		return Constants.ELEMENT_DIRECTORY + "/voice";
	}

	/**
	 * This method returns the text of a description of the voice element that
	 * will appear as a tooltip when the cursor points to the element.
	 */
	public String getDescription() {
		return "Converts text to audio for alpha num";
	}

	/**
	 * This method returns an array of Setting objects representing all the
	 * settings this voice element expects. This example tries to include all
	 * manners of setting types as well as those with dependencies.
	 */
	public Setting[] getSettings() throws ElementException {

		Setting[] settingArray = new Setting[5];

		settingArray[0] = new Setting("Text", "Text", "", true, true, true,
				Setting.STRING);
		settingArray[1] = new Setting("Type", "Type", "", true, true, true,
				Setting.STRING);
		settingArray[2] = new Setting("AudioBasePath", "AudioBasePath", "",
				true, true, true, Setting.STRING);
		settingArray[3] = new Setting("Pause", "Pause",
				"pause in seconds, 5s, 250ms -> translates to silence_5s.wav ",
				true, true, true, Setting.STRING);
		settingArray[3].setDefaultValue("0s");
		settingArray[4] = new Setting("ResultSVN", "ResultSVN",
				"Session variable name that holds the result", true, true,
				true, Setting.STRING);
		return settingArray;
	}

	/**
	 * This method returns a HashMap of arrays of AudioGroup objects
	 * representing each set of audio groups this voice element expects. The
	 * keys to the HashMap are the names of sets of audio groups. The arrays
	 * represent the audio groups that appear in the set. Return null if the
	 * voice element does not need any audio groups.
	 */
	public HashMap getAudioGroups() throws ElementException {
		return null;
	}

	/**
	 * This method returns an array of Strings representing the order in which
	 * the audio group sets are to be displayed in Audium Builder for Studio.
	 * Return null if the voice element does not need any audio groups.
	 */
	public String[] getAudioGroupDisplayOrder() {
		return null;

	}

	public void doAction(String arg0, ActionElementData arg1)
	throws AudiumException {
		try {
			// String syswavFilesFolder = "sys/";

			arg1.addToLog("Version", version);
			ActionElementConfig config = arg1.getActionElementConfig();
			String text = config.getSettingValue("Text", arg1);

			String type = config.getSettingValue("Type", arg1);
			String resultSVN = config.getSettingValue("ResultSVN", arg1);
			String audioBasePath = config
			.getSettingValue("AudioBasePath", arg1);
			String tta = "";

			String pause = config.getSettingValue("Pause", arg1);

			if (pause != null) {
				if (pause.equalsIgnoreCase("0s")) {
					pause = null;
				} else if (pause.equalsIgnoreCase("0ms")) {
					pause = null;
				}
			}

			if (audioBasePath != null) {
				audioBasePath = audioBasePath.trim();
				if (audioBasePath.length() > 0) {
					if (audioBasePath.charAt(audioBasePath.length() - 1) != '/') {
						audioBasePath += "/";
					}
				}
			}

			if ((tta == null) || tta.equals(""))
				tta = "";
			arg1.addToLog("TTA", tta);

			if ((text == null) || text.equals(""))
				return;
			else
				text = text.trim();

			text = text.toLowerCase();

			if ((type == null) || type.equals(""))
				return;
			else
				type = type.trim();
			type = type.toUpperCase();

			if (type.equalsIgnoreCase("alphanum")) {
				text = text.replaceAll("[^a-z0-9]", "");
				arg1.addToLog("Type1 is ", type);
				tta = tta + GetAlphaNumericAudio(audioBasePath, text, pause,arg1);
				arg1.setSessionData(resultSVN, tta);
			}
			else if (type.equalsIgnoreCase("spanish_date")) {
				text = text.replaceAll("[^0-9]", "");
				arg1.addToLog("Type2 is ", type);
				tta = tta + GetSpanishDateAudio(audioBasePath, text, pause,arg1);
				arg1.setSessionData(resultSVN, tta);				
			}
			else if (type.equalsIgnoreCase("spanish_currency")) {
				text = text.replaceAll("[^.0-9]", "");
				arg1.addToLog("Type3 is ", type);
				tta = tta + GetSpanishCurrencyAudio(audioBasePath, text, pause, arg1);
				arg1.setSessionData(resultSVN, tta);				
			}
			else
			{
				arg1.addToLog("Type4 is ","unrecognized");
				return;
			}

			arg1.addToLog("TTA", tta);

		} catch (Exception e) {
			arg1.addToLog("ERROR", "TTA: Exception:" + e.toString());
		}
	}


	public static String GetAlphaNumericAudio(String wavFilesFolder,
			String AlphaNum, String IntraDigitBreakTime, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetAlphaNumericAudio");
		arg1.addToLog("AlphaNum", AlphaNum);
		
		String wavFiles = "";

		if (AlphaNum == null) {
			AlphaNum = "";
		}

		AlphaNum = AlphaNum.toLowerCase();

		AlphaNum = AlphaNum.trim();

		for (int i = 0; i < AlphaNum.length(); i++) {
			if (!AlphaNum.substring(i, i + 1).equalsIgnoreCase(" ")) {
				wavFiles = wavFiles + "<audio src=\"" + wavFilesFolder
				+ AlphaNum.substring(i, i + 1) + ".wav" + "\"/>";
			}

			if (IntraDigitBreakTime != null) {
				wavFiles = wavFiles + "<audio src=\"" + wavFilesFolder
				+ "silence_" + IntraDigitBreakTime + ".wav" + "\"/>";
			}
		}

		return wavFiles;
	}

	public static String GetSpanishDateAudio(String wavFilesFolder,
			String AlphaNum, String IntraDigitBreakTime, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishDateAudio");
		arg1.addToLog("AlphaNum", AlphaNum);
		
		String wavFiles = "";

		if (AlphaNum == null) {
			AlphaNum = "";
		}

		AlphaNum = AlphaNum.toLowerCase();
		AlphaNum = AlphaNum.trim();

		// Expecting format mmddyyyy which is 8-long
		if(AlphaNum.length() != 8)
		{
			wavFiles = wavFiles + "<audio src=\"" + wavFilesFolder + "zulu.wav" + "\"/>";
			return wavFiles;
		}

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		String day = AlphaNum.substring(2,4);
		String dayAudio = "";
		if     (day.equals("01")) { dayAudio = "1st.wav" ; }
		else if(day.equals("02")) { dayAudio = "2nd.wav" ; }
		else if(day.equals("03")) { dayAudio = "3rd.wav" ; }
		else if(day.equals("04")) { dayAudio = "4th.wav" ; }
		else if(day.equals("05")) { dayAudio = "5th.wav" ; }
		else if(day.equals("06")) { dayAudio = "6th.wav" ; }
		else if(day.equals("07")) { dayAudio = "7th.wav" ; }
		else if(day.equals("08")) { dayAudio = "8th.wav" ; }
		else if(day.equals("09")) { dayAudio = "9th.wav" ; }
		else if(day.equals("10")) { dayAudio = "10th.wav"; }
		else if(day.equals("11")) { dayAudio = "11th.wav"; }
		else if(day.equals("12")) { dayAudio = "12th.wav"; }
		else if(day.equals("13")) { dayAudio = "13th.wav"; }
		else if(day.equals("14")) { dayAudio = "14th.wav"; }
		else if(day.equals("15")) { dayAudio = "15th.wav"; }
		else if(day.equals("16")) { dayAudio = "16th.wav"; }
		else if(day.equals("17")) { dayAudio = "17th.wav"; }
		else if(day.equals("18")) { dayAudio = "18th.wav"; }
		else if(day.equals("19")) { dayAudio = "19th.wav"; }
		else if(day.equals("20")) { dayAudio = "20th.wav"; }
		else if(day.equals("21")) { dayAudio = "21st.wav"; }
		else if(day.equals("22")) { dayAudio = "22nd.wav"; }
		else if(day.equals("23")) { dayAudio = "23rd.wav"; }
		else if(day.equals("24")) { dayAudio = "24th.wav"; }
		else if(day.equals("25")) { dayAudio = "25th.wav"; }
		else if(day.equals("26")) { dayAudio = "26th.wav"; }
		else if(day.equals("27")) { dayAudio = "27th.wav"; }
		else if(day.equals("28")) { dayAudio = "28th.wav"; }
		else if(day.equals("29")) { dayAudio = "29th.wav"; }
		else if(day.equals("30")) { dayAudio = "30th.wav"; }
		else if(day.equals("31")) { dayAudio = "31st.wav"; }
		if(dayAudio.length() != 0) { wavFiles = wavFiles + prefix + dayAudio + suffix; }

		// Add Spanish month "de" separator
		wavFiles = wavFiles + prefix + "of_de.wav" + suffix;

		// Get month audio
		String month = AlphaNum.substring(0,2);
		String monthAudio = "";
		if     (month.equals("01")) { monthAudio = "january.wav"  ; }
		else if(month.equals("02")) { monthAudio = "february.wav" ; }
		else if(month.equals("03")) { monthAudio = "march.wav"    ; }
		else if(month.equals("04")) { monthAudio = "april.wav"    ; }
		else if(month.equals("05")) { monthAudio = "may.wav"      ; }
		else if(month.equals("06")) { monthAudio = "june.wav"     ; }
		else if(month.equals("07")) { monthAudio = "july.wav"     ; }
		else if(month.equals("08")) { monthAudio = "august.wav"   ; }
		else if(month.equals("09")) { monthAudio = "september.wav"; }
		else if(month.equals("10")) { monthAudio = "october.wav"  ; }
		else if(month.equals("11")) { monthAudio = "november.wav" ; }
		else if(month.equals("12")) { monthAudio = "december.wav" ; }
		if(monthAudio.length() != 0) { wavFiles = wavFiles + prefix + monthAudio + suffix; }

		String year = AlphaNum.substring(4,8);

		// Add Spanish month "de" or "del" separator
		if(year.compareTo("2000") < 0)
		{
			wavFiles = wavFiles + prefix + "of_de.wav" + suffix;
		}
		else
		{
			wavFiles = wavFiles + prefix + "del.wav" + suffix;
		}

		wavFiles = wavFiles + GetSpanishYEAR(wavFilesFolder,year,arg1);

		return wavFiles;
	}

	public static String GetSpanishYEAR(String wavFilesFolder,
			String year, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishYEAR");
		arg1.addToLog("year", year);
		
		String wavFiles = "";

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		if(year.equals("2001"))
		{
			wavFiles = wavFiles + prefix + "2001.wav" + suffix;
		}
		else
		{
			String thousands = year.substring(0,1) + "000.wav";
			if(!thousands.equals("0")) wavFiles = wavFiles + prefix + thousands + suffix;

			String N3 = year.substring(1,4);
			//wavFiles = wavFiles + GetSpanishN3(wavFilesFolder,year.substring(1,4),arg1);
			
			String hundreds = N3.substring(0,1);
			String units = N3.substring(1,3);

			// hundreds
			if(N3.equals("100"))
			{
				wavFiles = wavFiles + prefix + "cien.wav" + suffix;
			}
			else if(!hundreds.equals("0"))
			{
				wavFiles = wavFiles + prefix + hundreds + "00.wav" + suffix;				
			}

			// units
			//wavFiles = wavFiles + GetSpanishN2(wavFilesFolder, units,arg1);	
			
			String N2 = units;
			
			String tens = N2.substring(0,1);
			String units_10to99 = N2.substring(0,2);
			String units_1to9 = N2.substring(1,2);

			// units
			if(tens.equals("0"))
			{
				if(!units_1to9.equals("0"))
				{
					wavFiles = wavFiles + prefix + units_1to9 + ".wav" + suffix;									
				}
			}
			else
			{
				// Check for special case for years ending in 21,31,41,51,61,71,81,91
				if(units_1to9.equals("1") && !tens.equals("1"))
				{
					wavFiles = wavFiles + prefix + units_10to99 + "_uno.wav" + suffix;
				}
				else
				{
					wavFiles = wavFiles + prefix + units_10to99 + ".wav" + suffix;					
				}
			}


		}
		return wavFiles;
	}

	public static String GetSpanishN12(String wavFilesFolder,
			String N12, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishN12");
		arg1.addToLog("N12", N12);
		
		String wavFiles = "";

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		String thousands = N12.substring(0,3);
		if(!thousands.equals("000"))
		{
			wavFiles = wavFiles + GetSpanishN3(wavFilesFolder,N12.substring(0,3),arg1);
			if(thousands.equals("001"))
			{
				wavFiles = wavFiles + prefix + "un.wav" + suffix;
				wavFiles = wavFiles + prefix + "billion.wav" + suffix;
			}
			else
			{
				wavFiles = wavFiles + prefix + "billions.wav" + suffix;
			}
		}
		wavFiles = wavFiles + GetSpanishN9(wavFilesFolder,N12.substring(3,12),arg1);
		return wavFiles;
	}

	public static String GetSpanishN9(String wavFilesFolder,
			String N9, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishN9");
		arg1.addToLog("N9", N9);
		
		String wavFiles = "";

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		String thousands = N9.substring(0,3);
		if(!thousands.equals("000"))
		{
			wavFiles = wavFiles + GetSpanishN3(wavFilesFolder,N9.substring(0,3),arg1);
			if(thousands.equals("001"))
			{
				wavFiles = wavFiles + prefix + "un.wav" + suffix;
				wavFiles = wavFiles + prefix + "million.wav" + suffix;
			}
			else
			{
				wavFiles = wavFiles + prefix + "millions.wav" + suffix;
			}
		}

		wavFiles = wavFiles + GetSpanishN6(wavFilesFolder,N9.substring(3,9),arg1);

		return wavFiles;
	}

	public static String GetSpanishN6(String wavFilesFolder,
			String N6, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishN6");
		arg1.addToLog("N6", N6);
		
		String wavFiles = "";

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		String thousands = N6.substring(0,3);
		if(!thousands.equals("000"))
		{
			if(!thousands.equals("001"))
			{
				wavFiles = wavFiles + GetSpanishN3(wavFilesFolder,N6.substring(0,3),arg1);
			}
			wavFiles = wavFiles + prefix + "1000.wav" + suffix;
		}
		wavFiles = wavFiles + GetSpanishN3(wavFilesFolder,N6.substring(3,6),arg1);
		return wavFiles;
	}


	public static String GetSpanishN3(String wavFilesFolder,
			String N3, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishN3");
		arg1.addToLog("N3", N3);
		
		String wavFiles = "";

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		String hundreds = N3.substring(0,1);
		String units = N3.substring(1,3);

		// hundreds
		if(N3.equals("100"))
		{
			wavFiles = wavFiles + prefix + "cien.wav" + suffix;
		}
		else if(!hundreds.equals("0"))
		{
			wavFiles = wavFiles + prefix + hundreds + "00.wav" + suffix;				
		}

		// units
		wavFiles = wavFiles + GetSpanishN2(wavFilesFolder, units,arg1);							

		return wavFiles;
	}

	public static String GetSpanishN2(String wavFilesFolder,
			String N2, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishN2");
		arg1.addToLog("N2", N2);
		
		String wavFiles = "";

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		String tens = N2.substring(0,1);
		String units_10to99 = N2.substring(0,2);
		String units_1to9 = N2.substring(1,2);

		// units
		if(tens.equals("0"))
		{
			if(!units_1to9.equals("0"))
			{
				wavFiles = wavFiles + prefix + units_1to9 + ".wav" + suffix;									
			}
		}
		else
		{
			wavFiles = wavFiles + prefix + units_10to99 + ".wav" + suffix;							
		}

		return wavFiles;
	}

	public static String GetSpanishCurrencyAudio(String wavFilesFolder,
			String AlphaNum, String IntraDigitBreakTime, ActionElementData arg1) throws Exception {

		arg1.addToLog("Location", "GetSpanishCurrencyAudio");
		arg1.addToLog("AlphaNum", AlphaNum);
		
		String wavFiles = "";

		if (AlphaNum == null) {
			AlphaNum = "";
		}

		AlphaNum = AlphaNum.toLowerCase();
		AlphaNum = AlphaNum.trim();

		// Get whole number part
		// Format NNNNN.CC
		// N = dollars
		// C = cents

		String prefix = "<audio src=\"" + wavFilesFolder;
		String suffix = "\"/>";

		Boolean hasDecimal = false;
		if(AlphaNum.indexOf(".") >= 0)
		{
			
		}
		int decimalPosition = AlphaNum.indexOf(".");
		if(decimalPosition >= 0) hasDecimal = true;
		
		String dollars = "";
		String cents = "00";
		String zeroPrefix = "000000000000";
		String oneString  = "000000000001";
		if(hasDecimal)
		{
			AlphaNum = AlphaNum + "00";
			cents = AlphaNum.substring(decimalPosition+1,decimalPosition+3);
			dollars = AlphaNum.substring(0,decimalPosition);
			dollars = (zeroPrefix + dollars).substring(dollars.length());
		}
		else
		{
			cents = "00";
			dollars = (zeroPrefix + AlphaNum).substring(AlphaNum.length());
		}

		arg1.addToLog("Dollars", dollars);
		arg1.addToLog("Cents", cents);

		if(!dollars.equals(zeroPrefix))
		{
			if(dollars.equals(oneString))
			{
				wavFiles = wavFiles + prefix + "un.wav" + suffix;	
				wavFiles = wavFiles + prefix + "dollar.wav" + suffix;	
			}
			else
			{
				wavFiles = wavFiles + GetSpanishN12(wavFilesFolder,dollars,arg1);
				wavFiles = wavFiles + prefix + "dollars.wav" + suffix;	
			}
		}

		if(!dollars.equals(zeroPrefix) && !cents.equals("00"))
		{
			wavFiles = wavFiles + prefix + "with_con.wav" + suffix;
		}

		if(!cents.equals("00"))
		{
			if(cents.equals("01"))
			{
				wavFiles = wavFiles + prefix + "un.wav" + suffix;	
				wavFiles = wavFiles + prefix + "cent.wav" + suffix;	
			}
			else
			{
				wavFiles = wavFiles + GetSpanishN2(wavFilesFolder,cents,arg1);
				wavFiles = wavFiles + prefix + "cents.wav" + suffix;	
			}
		}

		return wavFiles;
	}

}
