package com.edatasite.workforce.gwt.core.server.servlets.eml;

import com.edatasite.shared.mail.Upload;
import com.edatasite.workforce.utils.EdsContextParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: Ilhombek
 * Date: 16.09.2010
 * Time: 12:57:30
 */
public class EML {

    public static String _DEFAULT_FOLDER = "../emls/";
    public static String EMAIL_TEMPLATE_FOLDER_NAME = "WFT_Templates_";

    public static String _TEMPLATE_NAME = "templatename";
    public static String _CATEGORY_ID = "categoryid";
    public static String _IS_DEFAULT = "isdefault";
    public static String _COMPANY_ID = "companyid";
    public static String _CATEGORY_NAME = "categoryname";
    public static String _FROM_USER_NAME = "fromusername";
    public static String _FROM_USER_ID = "fromuserid";


    public static String getEMLFileDirectory() {
        String path = EdsContextParams.getEMLDirectory();
        /*if (EdsContextParams.isLive()) {
            return Constants.applicationRootLive + "/emluploads/";
        } else if (EdsContextParams.isAWS()) {
            return Constants.applicationRootAws  + "/emluploads/";
        } else if (EdsContextParams.isLocal()) {
            return "C:/emluploads/";
        }*/
        if(path!=null){
            return path;
        }
        else {
            throw new IllegalArgumentException("There is no such EML host.");
        }
    }

    //default fields
    private String _x_Sender;
    private String[] _x_Receivers;
    private String _Received;
    private String _Mime_Version;
    private String _From;
    private String _To;
    private String _CC;
    private Date _Date;
    private String _Subject;
    private String _Content_Type;
    private String _Content_Transfer_Encoding;
    private String _Return_Path;
    private String _Message_ID;
    private Date _x_OriginalArrivalTime;
    private String _Body;
    private String _HTMLBody;
    private File _File;
    private String _FileName;
    private ArrayList<Upload> _Attachments;

    //custom fields
    private String _Template_Name;
    private String _Is_Default;
    private String _Company_Id;
    private String _Category_Id;
    private String _Category_Name;
    private String _From_User_Name;
    private String _From_User_Id;


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

    public File get_File() {
        return _File;
    }

    public void set_File(File _File) {
        this._File = _File;
    }

    public String get_FileName() {
        return _FileName;
    }

    public void set_FileName(String _FileName) {
        this._FileName = _FileName;
    }

    public ArrayList<Upload> get_Attachments() {
        return _Attachments;
    }

    public void set_Attachments(ArrayList<Upload> _Attachments) {
        this._Attachments = _Attachments;
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
}
