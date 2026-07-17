package com.edatasite.workforce.gwt.core.server.servlets.eml;

import java.io.*;
import java.util.*;

/**
 * User: Ilhombek
 * Date: 31.08.2010
 * Time: 17:05:57
 */
public class EMLReader {

    //default fields
    private static String _x_Sender;
    private static String[] _x_Receivers;
    private static String _Received;
    private static String _Mime_Version;
    private static String _From;
    private static String _To;
    private static String _CC;
    private static Date _Date;
    private static String _Subject;
    private static String _Content_Type;
    private static String _Content_Transfer_Encoding;
    private static String _Return_Path;
    private static String _Message_ID;
    private static Date _x_OriginalArrivalTime;
    private static String _Body;
    private static String _HTMLBody;
    private static Map<String, String> _listUnsupported = null;

    //custom fields
    private static String _Template_Name;
    private static String _Is_Default;
    private static String _Company_Id;
    private static String _Category_Id;
    private static String _Category_Name;
    private static String _From_User_Name;
    private static String _From_User_Id;
    public static InputStream _InputStream;

    public String get_x_Sender() {
        return _x_Sender;
    }

    public void set_x_Sender(String _x_Sender) {
        this._x_Sender = _x_Sender;
    }

    public String[] get_x_Receivers() {
        return _x_Receivers;
    }

    public void set_x_Receivers(String[] _x_Receivers) {
        this._x_Receivers = _x_Receivers;
    }

    public String get_Received() {
        return _Received;
    }

    public void set_Received(String _Received) {
        this._Received = _Received;
    }

    public String get_Mime_Version() {
        return _Mime_Version;
    }

    public void set_Mime_Version(String _Mime_Version) {
        this._Mime_Version = _Mime_Version;
    }

    public String get_From() {
        return _From;
    }

    public void set_From(String _From) {
        this._From = _From;
    }

    public String get_To() {
        return _To;
    }

    public void set_To(String _To) {
        this._To = _To;
    }

    public String get_CC() {
        return _CC;
    }

    public void set_CC(String _CC) {
        this._CC = _CC;
    }

    public Date get_Date() {
        return _Date;
    }

    public void set_Date(Date _Date) {
        this._Date = _Date;
    }

    public String get_Subject() {
        return _Subject;
    }

    public void set_Subject(String _Subject) {
        this._Subject = _Subject;
    }

    public String get_Content_Type() {
        return _Content_Type;
    }

    public void set_Content_Type(String _Content_Type) {
        this._Content_Type = _Content_Type;
    }

    public String get_Content_Transfer_Encoding() {
        return _Content_Transfer_Encoding;
    }

    public void set_Content_Transfer_Encoding(String _Content_Transfer_Encoding) {
        this._Content_Transfer_Encoding = _Content_Transfer_Encoding;
    }

    public String get_Return_Path() {
        return _Return_Path;
    }

    public void set_Return_Path(String _Return_Path) {
        this._Return_Path = _Return_Path;
    }

    public String get_Message_ID() {
        return _Message_ID;
    }

    public void set_Message_ID(String _Message_ID) {
        this._Message_ID = _Message_ID;
    }

    public Date get_x_OriginalArrivalTime() {
        return _x_OriginalArrivalTime;
    }

    public void set_x_OriginalArrivalTime(Date _x_OriginalArrivalTime) {
        this._x_OriginalArrivalTime = _x_OriginalArrivalTime;
    }

    public String get_Body() {
        return _Body;
    }

    public void set_Body(String _Body) {
        this._Body = _Body;
    }

    public String get_HTMLBody() {
        return _HTMLBody;
    }

    public void set_HTMLBody(String _HTMLBody) {
        this._HTMLBody = _HTMLBody;
    }

    public Map<String, String> get_listUnsupported() {
        return _listUnsupported;
    }

    public void set_listUnsupported(Map<String, String> _listUnsupported) {
        this._listUnsupported = _listUnsupported;
    }

    public String get_Template_Name() {
        return _Template_Name;
    }

    public void set_Template_Name(String _Template_Name) {
        this._Template_Name = _Template_Name;
    }

    public String is_Is_Default() {
        return _Is_Default;
    }

    public void set_Is_Default(String _Is_Default) {
        this._Is_Default = _Is_Default;
    }

    public String get_Company_Id() {
        return _Company_Id;
    }

    public void set_Company_Id(String _Company_Id) {
        this._Company_Id = _Company_Id;
    }

    public String get_Category_Id() {
        return _Category_Id;
    }

    public void set_Category_Id(String _Category_Id) {
        this._Category_Id = _Category_Id;
    }

    public String get_Category_Name() {
        return _Category_Name;
    }

    public void set_Category_Name(String _Category_Name) {
        this._Category_Name = _Category_Name;
    }

    public String get_From_User_Name() {
        return _From_User_Name;
    }

    public void set_From_User_Name(String _From_User_Name) {
        this._From_User_Name = _From_User_Name;
    }

    public String get_From_User_Id() {
        return _From_User_Id;
    }

    public void set_From_User_Id(String _From_User_Id) {
        this._From_User_Id = _From_User_Id;
    }

    public EMLReader(InputStream fsEML) {
        this._InputStream = fsEML;
        ParseEML(fsEML);
    }

    public static void main(String[] args) throws FileNotFoundException {
        File emlFile = new File("C:/Users/Ilhombek/Desktop/SIMPLE.eml");
        InputStream source = new FileInputStream(emlFile);

        ParseEML(source);
        System.out.println("----------------------------------------------------");
        System.out.println("----------------------------------------------------");
        System.out.println(_HTMLBody);
    }

    private static void ParseEML(InputStream fsEML) {
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(fsEML, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            inputStreamReader = new InputStreamReader(fsEML);
        }
        BufferedReader br = new BufferedReader(inputStreamReader);
        String sLine;
        List<String> listAll = new ArrayList<>();
        try {
            while ((sLine = br.readLine()) != null) {
                listAll.add(sLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        int nStartBody = -1;
        String[] saAll = new String[listAll.size()];
        listAll.toArray(saAll);

        for (int i = 0; i < saAll.length; i++) {
            if (saAll[i].isEmpty()) {
                nStartBody = i;
            }

            String sFullValue = saAll[i];
            GetFullValue(saAll, i, sFullValue);
            list.add(sFullValue);
            System.out.println(sFullValue);
        }
        SetFields(list.toArray(new String[]{}));

        if (nStartBody == -1) {   // no body ?
            return;
        }

        // Get the body info out of saAll and set the Body and/or HTMLBody properties
        if (_Content_Type != null && _Content_Type.toLowerCase().contains("multipart/alternative"))   // set for HTMLBody messages
        {
            int ix = _Content_Type.toLowerCase().indexOf("boundary");        // boundary is used to separate the different body types
//            ix = 2;
            if (ix == -1) {
                return;
            }

            final char[] chars = {'=', '"', ' ', '\t'};
            String sBoundaryMarker = _Content_Type.substring(ix + 8).replace("=", "").replace("\"", "").replace(" ", "").replace("\t", "");

            // save this boundaries elements into a list of strings
            list = new ArrayList<>();
            for (int n = nStartBody + 1; n < saAll.length; n++) {
                if (saAll[n].contains(sBoundaryMarker)) {
                    if (list.size() > 0) {
                        SetBody(list);
                        list = new ArrayList<>();
                    }
                    continue;
                }

                list.add(saAll[n]);
            }
        } else {  // plain text body type only
            if (_Body != null) {
                _Body.isEmpty();
            }
            for (int n = nStartBody + 1; n < saAll.length; n++) {
                _Body += saAll[n] + "\r\n";
            }
        }
        try {
            inputStreamReader.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void SetBody(List<String> list) {
        boolean bIsHTML = false;
        boolean bIsBodyStart = false;
        List<String> listBody = new ArrayList<>();

        for (String s : list) {
            // use to determine type of body
            if (s.toLowerCase().startsWith("content-type")) {
                if (s.toLowerCase().contains("text/html")) {
                    bIsHTML = true;
                } else if (!s.toLowerCase().contains("text/plain")) {
                    return;
                }
            } else if (s.isEmpty() && !bIsBodyStart) {
                bIsBodyStart = true;
            } else if (bIsBodyStart) {
                listBody.add(s);
            }
        }

        String[] sa = new String[listBody.size()];
        listBody.toArray(sa);

        if (bIsHTML) {
            _HTMLBody = String.format("\r\n", sa);
        } else {
            _Body = String.format("\r\n", sa);
        }
    }

    private static void GetFullValue(String[] sa, int i, String sValue) {
        String firstSa = sa[(i + 1 >= sa.length ? i : i + 1)];
        // spec says line's that begin with white space are continuation lines
        if (i + 1 < sa.length && sa[(i + 1 >= sa.length ? i : i + 1)].isEmpty() && Character.isWhitespace(firstSa.indexOf(0))) {
            i++;
            sValue += " " + sa[i].trim();

            GetFullValue(sa, i, sValue);
        }
    }

    private static void SetFields(String[] saLines) {
//        List<String> listUnsupported = new ArrayList<String>();
        _listUnsupported = new HashMap<>();
        List<String> listX_Receiver = new ArrayList<>();
        boolean bodyStart = false;
        for (String sHdr : saLines) {
            /*begin body*/
            if (sHdr.trim().startsWith("<html>")) {
                bodyStart = true;
                _HTMLBody = "";
            }
            if (bodyStart) {
                _HTMLBody = _HTMLBody + sHdr;
            }
            if (sHdr.trim().endsWith("</html>")) {
                bodyStart = false;
            }
            /*end body*/

            String[] saHdr = Split(sHdr);
            if (saHdr == null) { // not a valid header
                continue;
            }
            final String saH = saHdr[0].toLowerCase();
            if ("x-sender".equals(saH)) {
                _x_Sender = saHdr[1];
            } else if ("x-receiver".equals(saH)) {
                listX_Receiver.add(saHdr[1]);
            } else if ("received".equals(saH)) {
                _Received = saHdr[1];
            } else if ("mime-version".equals(saH)) {
                _Mime_Version = saHdr[1];
            } else if ("from".equals(saH)) {
                _From = saHdr[1];
            } else if ("to".equals(saH)) {
                _To = saHdr[1];
            } else if ("cc".equals(saH)) {
                _CC = saHdr[1];
            } else if ("date".equals(saH)) {
                _Date = new Date(saHdr[1]);
            } else if ("subject".equals(saH)) {
                _Subject = saHdr[1];
            } else if ("content-type".equals(saH)) {
//                if (saHdr[1].contains("multipart/alternative")) {
                _Content_Type = saHdr[1];
//                }
            } else if ("content-transfer-encoding".equals(saH)) {
                _Content_Transfer_Encoding = saHdr[1];
            } else if ("return-path".equals(saH)) {
                _Return_Path = saHdr[1];
            } else if ("message-id".equals(saH)) {
                _Message_ID = saHdr[1];
            } else if (EML._TEMPLATE_NAME.equals(saH)) {
                _Template_Name = saHdr[1];
            } else if (EML._CATEGORY_ID.equals(saH)) {
                _Category_Id = saHdr[1];
            } else if (EML._IS_DEFAULT.equals(saH)) {
                _Is_Default = saHdr[1];
            } else if (EML._COMPANY_ID.equals(saH)) {
                _Company_Id = saHdr[1];
            } else if (EML._CATEGORY_NAME.equals(saH)) {
                _Category_Name = saHdr[1];
            } else if (EML._FROM_USER_ID.equals(saH)) {
                _From_User_Id = saHdr[1];
            } else if (EML._FROM_USER_NAME.equals(saH)) {
                _From_User_Name = saHdr[1];
            } else if ("x-originalarrivaltime".equals(saH)) {
                int ix = saHdr[1].indexOf("FILETIME");
                if (ix != -1) {
                    String sOAT = saHdr[1].substring(0, ix);
                    sOAT = sOAT.replace("(UTC)", "-0000");
                    _x_OriginalArrivalTime = new Date(sOAT);
                }
            } /*else if ("body".equals(saH)) {
                _Body = saHdr[1];
            }*/
            else {
                _listUnsupported.put(saHdr[0], saHdr[1]);
            }
        }

        _x_Receivers = new String[listX_Receiver.size()];
        listX_Receiver.toArray(_x_Receivers);
    }

    // because string.Split won't work here...

    private static final String[] Split(String sHeader) {
        int ix;
        if ((ix = sHeader.indexOf(':')) == -1) {
            return null;
        }
        return new String[]{sHeader.substring(0, ix).trim(), sHeader.substring(ix + 1).trim()};
    }
}
