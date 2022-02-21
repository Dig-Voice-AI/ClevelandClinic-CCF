package ccf.custom.cvpvxml.actionelement;

import ccf.custom.Constants;

import com.audium.server.voiceElement.ElementData;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.Setting;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.AudiumException;

import com.audium.server.xml.ActionElementConfig;
import com.audium.server.session.ActionElementData;
import com.audium.server.voiceElement.ActionElementBase;

public class Last4OfString extends ActionElementBase implements ElementInterface {

	private static final String RES_NAME = "Last4";

	public static final String version = "1.00";

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
		return Constants.ELEMENT_DIRECTORY + "/utility";
	}

	/**
	 * This method returns the text of a description of the voice element that
	 * will appear as a tooltip when the cursor points to the element.
	 */
	public String getDescription() {
		return "Returns last 4 characters of input string";
	}

	/**
	 * This method returns an array of Setting objects representing all the
	 * settings this voice element expects. This example tries to include all
	 * manners of setting types as well as those with dependencies.
	 */
	public Setting[] getSettings() throws ElementException {

		Setting[] settingArray = new Setting[1];

		settingArray[0] = new Setting(
				"inputString",
				"inputString",
				"inputString",
				true, true, true,Setting.STRING);
		return settingArray;
	}

	/**
	 * This method returns an array of ElementData objects representing the
	 * Element Data that this action element creates.
	 */

	public ElementData[] getElementData() throws ElementException {

		return new ElementData[] { new ElementData(RES_NAME, RES_NAME) };
	}

	public void doAction(String arg0, ActionElementData data)
			throws AudiumException {
		boolean debugFlag = false;

		try {
			try {
				debugFlag = (data.getSessionData("DEBUG") == null) ? false
						: ((String) data.getSessionData("DEBUG"))
								.equalsIgnoreCase("true");

			} catch (Exception e) {
				data.addToLog("ERROR", "Exception=" + e.toString());
			}

			data.addToLog("Version", version);
			ActionElementConfig config = data.getActionElementConfig();
			
			String input = config.getSettingValue("inputString", data);
			data.addToLog("INFO", "last 4 of " + input);

			String result = "";
		
			if(input.length() > 4)
			{
				result = input.substring(input.length()-4);
			}
			else
			{
				result = input;
			}
			
			
			data.setElementData(RES_NAME, result);

			if (debugFlag) {
				data.addToLog("DEBUG", "Last4 = '" + result + "'");
			}
		} catch (Exception e) {
			data.addToLog("ERROR", "Last4OfString: Exception:" + e.toString());
			throw new AudiumException(e);
		}
	}

}
