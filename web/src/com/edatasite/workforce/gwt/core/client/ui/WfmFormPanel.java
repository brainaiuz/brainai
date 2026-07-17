package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author iskandar
 */

public class WfmFormPanel extends FormPanel implements CommandConstants, Constants, Clearable {

    private Map parameters = new HashMap();
    private boolean success = false;
    private String errorString = null;
    private String returnValue;
    private Integer objectID;
    private static final WfmStrings wfmStrign = WfmStrings.App.get();

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public WfmFormPanel(NamedFrame name) {
        super(name);
    }

    public WfmFormPanel(NamedFrame name, String target) {
        super(target);
    }
    /**
     * @param action action name starting with / for example /CreateTask
     */
    public WfmFormPanel(String action) {
        this();
        setActionHandler(action);
    }
    public WfmFormPanel() {
        setMethod(METHOD_POST);
        setEncoding(ENCODING_MULTIPART);
        addSubmitHandler(submitHandler);
        addSubmitCompleteHandler(submitCompleteHandler);
    }

    public void setActionHandler(String action) {
        setAction(Utils.getHostNameURL() + COMMAND_URL + action);
    }

    public void setParameter(String name, String value) {
        setParameter(name, value, false);
    }

    /**
     * Sets additional parameter to form If append set to false overrides
     * already existing parameter value otherwise appends it as a new value
     *
     * @param name
     * @param value
     * @param append
     */

    public void setParameter(String name, String value, boolean append) {
        Object o = parameters.get(name);
        if (o != null) {
            if (append) {
                appendParameter(name, value, o);
                return;
            } else {
                deleteParam(o);
                parameters.put(name, null);
            }
        }
        Hidden parameter = new Hidden();
        parameter.setName(name);
        parameter.setValue(value);
        parameters.put(name, parameter);
        DOM.appendChild(getElement(), parameter.getElement());
    }

    private void appendParameter(String name, String value, Object param) {
        List lst = null;
        if (param instanceof Hidden) {
            lst = new ArrayList();
            lst.add(param);
            parameters.put(name, lst);
        } else if (param instanceof List) {
            lst = (List) param;
        }
        Hidden parameter = new Hidden();
        parameter.setName(name);
        parameter.setValue(value);
        lst.add(parameter);
        DOM.appendChild(getElement(), parameter.getElement());
    }

    private void deleteParam(Object param) {
        if (param instanceof Hidden) {
            Hidden p = (Hidden) param;
            DOM.removeChild(getElement(), p.getElement());
        } else if (param instanceof List) {
            List lst = (List) param;
            for (Object aLst : lst) {
                Hidden p = (Hidden) aLst;
                DOM.removeChild(getElement(), p.getElement());
            }
        }
    }

    public boolean isSuccess() {
        return success;
    }

    private SubmitHandler submitHandler = (event) -> {
        setParameter(SESSION_ID_PARAM_NAME, Cookies.getCookie(SESSION_ID_COOKIE));
        if (objectID != null) {
            setParameter(PROJECT_ID, objectID.toString());
        }
    };

    private SubmitCompleteHandler submitCompleteHandler =  (event) ->  {
        errorString = null;
        String result = event.getResults().toUpperCase();
        String s = result.replace("<PRE>", "");
        returnValue = s.replace("</PRE>", "");
        if (returnValue.indexOf('[') != -1 && returnValue.indexOf(']') != -1) {
            returnValue = returnValue.substring(returnValue.indexOf('[') + 1, returnValue.indexOf(']'));
        }
        if (returnValue.contains(FAIL)) {
            errorString = event.getResults().split(FAIL)[1].replace("</pre>", "");
            success = false;
        } else if (result.indexOf(1) != -1 || "".equals(returnValue) || result.contains(CommandConstants.SUCCESS)) {
            success = true;
        } else if (result.toUpperCase().contains(FAIL)) {
            success = false;
        } else {
            if (returnValue.length() > 0) {
                try {
                    final String retVal;
                    final int i = returnValue.lastIndexOf("=");
                    if (i != -1) {
                        final int length = returnValue.length();
                        retVal = returnValue.substring(i + 1, length - 1);
                    } else {
                        retVal = returnValue;
                    }
                    if (getParameters() == null || getParameters().get("url") == null) {
                        if(retVal.contains(",")){
                            objectID = Integer.parseInt(retVal.substring(0,retVal.indexOf(',')));

                        }else {
                            objectID = Integer.parseInt(retVal);
                        }
                    }
                    success = true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    success = false;
                    errorString = wfmStrign.anErrorOccure();
                }
            }
        }
        if (!success && errorString == null) {
            errorString = event.getResults();
        }
    };

    public void clearSelected() {
        Widget widget = getWidget();
        if (widget instanceof Clearable) {
            Clearable cl = (Clearable) widget;
            cl.clearSelected();
        }
        for (Object o : parameters.values()) {
            deleteParam(o);
        }
        parameters.clear();
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

	public Map getParameters() {
		return parameters;
	}
}
