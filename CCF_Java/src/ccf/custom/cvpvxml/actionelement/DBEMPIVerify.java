package ccf.custom.cvpvxml.actionelement;

import java.util.Date;

import ccf.custom.Constants;
import ccf.custom.da.EMPIDBDa;
import ccf.custom.dm.EMPIResponse;

import com.audium.server.session.DecisionElementData;
import com.audium.server.voiceElement.DecisionElementBase;
import com.audium.server.voiceElement.ElementData;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.DecisionElementConfig;
import com.toolkit.common.Util;

public class DBEMPIVerify extends DecisionElementBase implements ElementInterface {

	private static final String Version = "1.0";

	public static final String EXIT_STATE_FOUND    = "FOUND";
	public static final String EXIT_STATE_NOTFOUND = "NOTFOUND";
	public static final String EXIT_STATE_ERROR    = "ERROR";

	public static final String REQ_PARAM_EMPI       = "empi";
	public static final String REQ_PARAM_COMPONENTNAME = "componentName";

	public static final String RES_OUTPUT     = "output";

	public String getElementName() {

		return this.getClass().getName().substring(
				this.getClass().getName().lastIndexOf('.') + 1,
				this.getClass().getName().length());
	}

	/**
	 * This is the name of the folder in the Audium Builder in which this action
	 * element appears.
	 */
	public String getDisplayFolderName() {
		return Constants.ELEMENT_DIRECTORY + "/data";
	}

	/**
	 * This is the description of what this action element does.
	 */
	public String getDescription() {

		return "Get EMPI verified from DB, if any."
		+ Version + "\n" + this.getClass().getName();
	}

	/**
	 * This returns the settings used by this action element.
	 */
	public Setting[] getSettings() throws ElementException {

		Setting[] settingArray = new Setting[2];

		settingArray[0] = new Setting(
				REQ_PARAM_EMPI, 
				REQ_PARAM_EMPI, 
				REQ_PARAM_EMPI, 
				true, /** It is required  **/ 
				true, /** It appears only once **/ 
				true, /** It allows substitution **/ 
				Setting.STRING);
		settingArray[1] = new Setting(
				REQ_PARAM_COMPONENTNAME, 
				REQ_PARAM_COMPONENTNAME, 
				REQ_PARAM_COMPONENTNAME, 
				true, /** It is required  **/ 
				true, /** It appears only once **/ 
				true, /** It allows substitution **/ 
				Setting.STRING);

		return settingArray;
	}

	/**
	 * This method returns an array of ElementData objects representing the
	 * Element Data that this action element creates.
	 */

	public ElementData[] getElementData() throws ElementException {

		return new ElementData[] 
		                       {
				new ElementData(RES_OUTPUT,RES_OUTPUT),
		                       };
	}

	public ExitState[] getExitStates() throws ElementException {
		ExitState[] exitStateArray = new ExitState[3];

		exitStateArray[0] = new ExitState(EXIT_STATE_FOUND, EXIT_STATE_FOUND,
		"success.");
		exitStateArray[1] = new ExitState(EXIT_STATE_NOTFOUND, EXIT_STATE_NOTFOUND,
		"not found");
		exitStateArray[2] = new ExitState(EXIT_STATE_ERROR, EXIT_STATE_ERROR,
		"An error occured.");

		return exitStateArray;
	}

	/**
	 * This method performs the action.
	 */
	public String doDecision(String name, DecisionElementData data)
	throws ElementException {
		// Get the configuration

		Date dt = new Date();
		long t1 = dt.getTime();
		String exitState = EXIT_STATE_ERROR;
		
		try {
			data.addToLog("INFO", "Version=" + Version);

			DecisionElementConfig config = data.getDecisionElementConfig();

			String componentName = config.getSettingValue(REQ_PARAM_COMPONENTNAME, data);
			String empiValue = "E"+config.getSettingValue(REQ_PARAM_EMPI, data);
			data.addToLog("EMPI Value: ", empiValue);

			EMPIDBDa da = Util.createObject(componentName, data, true);
			EMPIResponse cr = new EMPIResponse();
				
			cr = da.getEmpiResponse(empiValue);
						
			// Set up default return values.
			data.setElementData(RES_OUTPUT, cr.empiResponse);
			data.addToLog("After DB: ", cr.empiResponse);

			if(cr.empiResponse.equals("0"))
			{
				exitState = EXIT_STATE_NOTFOUND;
				data.addToLog("INFO", "No EMPI found");				
			}
			else
			{
				exitState = EXIT_STATE_FOUND;
				data.setSessionData("s_VerifyEMPI", "Success");
				data.addToLog("INFO", "Found " + cr.empiResponse + " EMPI : " + empiValue);				
			}

			dt = new Date();
			long t2 = dt.getTime();
			data.addToLog("INFO", "Time=" + (t2 - t1) + "ms");
		} catch (Exception e) {
			exitState = EXIT_STATE_ERROR;
			data.addToLog("ERROR", "Exception='" + e.toString() + "'");
			String errorInfo = data.getCurrentElement() + ", Exception="
			+ e.toString();
			dt = new Date();
			long t2 = dt.getTime();
			data.addToLog("ERROR", "Time=" + (t2 - t1) + "ms");
			try {
				Throwable err = e.getCause();
				if (err != null) {
					data.addToLog("ERROR", "EXCEPTION: Caused By: "
							+ err.toString());
					errorInfo += ", EXCEPTION: Caused By: " + err.toString();
				}

			} catch (Exception e1) {
				// No Op
			}
			try {
				if (errorInfo.length() > 200)
					errorInfo = errorInfo.substring(0, 200);
				data.setSessionData("s__errorDetail", errorInfo);
			} catch (Exception e2) {
				// No Op
			}
		} catch (Error e1) {
			exitState = EXIT_STATE_ERROR;
			data.addToLog("ERROR", "Error='" + e1.toString() + "'");
			try {
				String errorInfo = data.getCurrentElement() + ", Error="
				+ e1.toString();
				if (errorInfo.length() > 200)
					errorInfo = errorInfo.substring(0, 200);
				data.setSessionData("s__errorDetail", errorInfo);
			} catch (Exception e2) {
				// No Op
			}
			dt = new Date();
			long t2 = dt.getTime();
			data.addToLog("ERROR", "Time=" + (t2 - t1) + "ms");
		}

		return exitState;
	}

}
