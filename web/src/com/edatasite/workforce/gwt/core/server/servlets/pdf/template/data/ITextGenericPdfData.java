package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice.NumericWidget;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ar;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_uz_lotin;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants.ACCOUNT_NUMBER;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 21-Jun-2010
 * Time: 15:33:52
 * <p/>
 * This is class uses All Pdf Tamplate data repository <br/>
 * <p/>
 * <center>Template types</center>
 * <p/>
 * 1.If <b>Pdf View Type equals to ITextPdfViewTypeEnum.LISTTABLE </b><br/>
 * that uses ITextTableList object and draw Pdf Template pdf list view;
 * <p/>
 * 2.if <b>Pdf View Type equals to ITextPdfViewTypeEnum.SUMMARYVIEW</b><br/>
 * that uses ITextSummaryView object and draw Pdf Tempalte pdf sumary view;
 * <p/>
 * 3.if <b>Pdf View Type equals to ITextPdfViewTypeEnum.BASEINVOICE</b><br/>
 * that uses ITextBaseInvoice object and draw Pdf Tempalte pdf accounting view;
 */
public class ITextGenericPdfData {
    private boolean landscape;
    private int nameFontSize = 10;
    private int total = 0;
    private String tableName;
    private String extraData;
    private String currentDate;
    private byte[] userPassword;
    private byte[] ownerPassword;
//    private ITextFontTypeEnum fontFamily = ITextFontTypeEnum.TIMES_NEW_ROMAN;
    private ITextPdfViewTypeEnum pdfViewPdfViewType = ITextPdfViewTypeEnum.LISTTABLE;
    private ITextTableList listTable;// uses only lists template
    private ITextSummaryView summaryView;// uses only views template
    private ITextSummaryView[] summaryViewArray;// uses only views template
    private ITextBaseInvoice baseInvoice;// uses only accounting templates
    private ITextCustomView customView;// uses only accounting templates
    private ITextUserData userData;// user data tempalte
    private ITextCompanyData companyData;// company data template
    private ITextUserData creatorData; // creator data template
    private PdfParams params;

    private HashMap<String, CustomisedITextTable> customData;
    private HashMap<String, ArrayList<CustomisedITextTable>> customListData3;
    private List<NumericWidget> charts;
    private Map<String, String> localizeMap;
    private Map<String, String> localizeLabels;
    private List<NumericWidget> numericWidgets;
    private List<NumericWidget> tableWidgets;
    private HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customListData;
    private HashMap<String, LinkedList<HashMap<CustomisedITextTable, CustomisedITextTable>>> customListData2;
    private List<CustomisedITextTable> customEntityTables;
    private ArrayList<CustomisedITextTable> groups;

    private NumberToWord numberToWordConverter = new NumberToWord_en();
    private ProductItem[] productItems;
    private DecimalFormat decimalFormat = null;
    private String userId;
    private String period;

    public ITextGenericPdfData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<NumericWidget> getCharts() {
        return charts;
    }

    public void setCharts(List<NumericWidget> charts) {
        this.charts = charts;
    }

    public Map<String, String> getLocalizeMap() {
        return localizeMap;
    }

    public void setLocalizeMap(Map<String, String> localizeMap) {
        this.localizeMap = localizeMap;
    }

    public Map<String, String> getLocalizeLabels() {
        return localizeLabels;
    }

    public void setLocalizeLabels(Map<String, String> localizeLabels) {
        this.localizeLabels = localizeLabels;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * if not set font that return default font size = 10
     *
     * @return
     */
    public int getNameFontSize() {
        return nameFontSize;
    }

    /**
     * Name font size
     *
     * @param nameFontSize
     */
    public void setNameFontSize(int nameFontSize) {
        this.nameFontSize = nameFontSize;
    }

    /**
     * After header write Name
     *
     * @return
     */
    public String getTableName() {
        return tableName;
    }



    /**
     * After header write Name
     *
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public byte[] getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword != null && !userPassword.isEmpty() ? userPassword.getBytes() : null;
    }

    public byte[] getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword != null && !ownerPassword.isEmpty() ? ownerPassword.getBytes() : null;
    }

    /**
     * Template type
     */
    public ITextPdfViewTypeEnum getPdfViewType() {
        return pdfViewPdfViewType;
    }

    /**
     * Set Tempalte type
     *
     * @param pdfViewPdfViewType
     */
    public void setPdfViewType(ITextPdfViewTypeEnum pdfViewPdfViewType) {
        this.pdfViewPdfViewType = pdfViewPdfViewType;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.LISTTABLE <br/>
     * Object uses draw <b>List Table</b> pdf
     *
     * @return
     */
    public ITextTableList getListTable() {
        return listTable;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.LISTTABLE <br/>
     * Object uses draw <b>List Table</b> pdf
     *
     * @param listTable
     */
    public void setListTable(ITextTableList listTable) {
        this.listTable = listTable;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.SUMMARYVIEW <br/>
     * Object uses draw <b>Summary View</b> pdf
     *
     * @return ITextSummaryView
     */
    public ITextSummaryView getSummaryView() {
        return summaryView;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.SUMMARYVIEW <br/>
     * Object uses draw <b>Summary View</b> pdf
     *
     * @param summaryView
     */
    public void setSummaryView(ITextSummaryView summaryView) {
        this.summaryView = summaryView;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.BASEINVOICE <br/>
     * Object uses draw <b>Summary View</b> pdf
     *
     * @return ITextSummaryView
     */
    public ITextBaseInvoice getBaseInvoice() {
        return baseInvoice;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.BASEINVOICE <br/>
     * Object uses draw <b>Summary View</b> pdf
     *
     * @param baseInvoice
     */
    public void setBaseInvoice(ITextBaseInvoice baseInvoice) {
        this.baseInvoice = baseInvoice;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.CUSTOMVIEW <br/>
     * Object uses draw <b>Custom View</b> pdf
     *
     * @return ITextCustomView
     */
    public ITextCustomView getCustomView() {
        return customView;
    }

    /**
     * if pdfViewPdfViewType==ITextPdfViewTypeEnum.CUSTOMVIEW <br/>
     * Object uses draw <b>Custom View</b> pdf
     *
     * @param customView
     */
    public void setCustomView(ITextCustomView customView) {
        this.customView = customView;
    }

    /**
     *
     * @return
     */
    public ITextUserData getUserData() {
        return userData;
    }

    /**
     * Set User Data
     * @param userData
     */
    public void setUserData(ITextUserData userData) {
        this.userData = userData;
    }

    public void setCreatorData(ITextUserData creatorData) {
        this.creatorData = creatorData;
    }

    /**
     *
     * @return
     */
    public ITextCompanyData getCompanyData() {
        return companyData;
    }

    /**
     * Set All company data
     * @param companyData
     */
    public void setCompanyData(ITextCompanyData companyData) {
        this.companyData = companyData;
    }

    public ITextSummaryView[] getSummaryViewArray() {
        return summaryViewArray;
    }

    public void setSummaryViewArray(ITextSummaryView[] summaryViewArray) {
        this.summaryViewArray = summaryViewArray;
    }

    public HashMap<String, CustomisedITextTable> getCustomData() {
        return customData;
    }

    public void setCustomData(HashMap<String, CustomisedITextTable> customData) {
        this.customData = customData;
    }

    public ITextPdfViewTypeEnum getPdfViewPdfViewType() {
        return pdfViewPdfViewType;
    }

    public ITextUserData getCreatorData() {
        return creatorData;
    }

    public List<CustomisedITextTable> getCustomEntityTables() {
        return customEntityTables;
    }

    public NumberToWord getNumberToWordConverter() {
        return numberToWordConverter;
    }

    public HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> getCustomListData() {

        return customListData;
    }

    public void setCustomListData(HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customListData) {
        this.customListData = customListData;
    }

    public HashMap<String, LinkedList<HashMap<CustomisedITextTable, CustomisedITextTable>>> getCustomListData2() {
        return customListData2;
    }

    public void setCustomListData2(HashMap<String, LinkedList<HashMap<CustomisedITextTable, CustomisedITextTable>>> customListData2) {
        this.customListData2 = customListData2;
    }

    public void setCustomEntityTables(List<CustomisedITextTable> customEntityTables) {
        this.customEntityTables = customEntityTables;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public PdfParams getParams() {
        return params;
    }

    public void setParams(PdfParams params) {
        this.params = params;
    }

    public Double getValueAsDouble(String in) {
        double f = 0.0d;
        try {
            f = Double.parseDouble(in.replaceAll("[,]", ""));
        } catch (Exception e) {
            return 0.0d;
        }
        return f;
    }

    public String getAsFormatted(Object obj) {
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat(",##0.00");
        }
        return decimalFormat.format(obj);
    }

    public String getAsFormattedUz(Object obj) {
        String pattern = ",##0.00";
        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(currentLocale);
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern, decimalFormatSymbols);
        return decimalFormat.format(obj);
    }

    public String getAsFormatInternational(Object obj) {
        String pattern = ",##0.00";
        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(currentLocale);
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat(pattern, decimalFormatSymbols);
        return decimalFormat.format(obj);
    }

    public String getFormattedAsInt(Object obj) {
        String pattern = ",##0";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        if (obj == null) {
            return decimalFormat.format("0");
        }

        BigDecimal decObject = new BigDecimal(String.valueOf(obj)).setScale(2, BigDecimal.ROUND_HALF_UP);

        return decimalFormat.format(decObject.intValue());
    }


    public Integer getValueAsInt(String in) {
        int f = 0;
        try {
            f = Integer.parseInt(in.replaceAll("[,]", ""));
        } catch (Exception e) {
            return 0;
        }
        return f;
    }

    public String getNumberInWordsAll(Object obj) {
        if (obj == null || obj.equals("") || obj.equals("N/A") || obj.equals(0.0)) {
            return "";
        }
        String objToString = obj.toString().replace(",", "");
        String numberToWord = numberToWordConverter.convert(new BigDecimal(objToString));

        return !StringUtils.isEmpty(numberToWord) ? WordUtils.capitalizeFully(numberToWord) : "";
    }

    public String getNumberInWordsAllRu(Object obj) {
        if (obj == null || obj.equals("") || obj.equals("N/A") || obj.equals(0.0)) {
            return "";
        }
        numberToWordConverter = new NumberToWord_ru();
        String objToString = obj.toString().replace(",", "");
        String numberToWord = numberToWordConverter.convert(new BigDecimal(objToString));

        return !StringUtils.isEmpty(numberToWord) ? WordUtils.capitalizeFully(numberToWord) : "";
    }

    public String getNumberInWordsAllUz(Object obj) {
        if (obj == null || obj.equals("") || obj.equals("N/A") || obj.equals(0.0)) {
            return "";
        }
        numberToWordConverter = new NumberToWord_uz_lotin();
        String objToString = obj.toString().replace(",", "");
        String numberToWord = numberToWordConverter.convert(new BigDecimal(objToString));

        return !StringUtils.isEmpty(numberToWord) ? WordUtils.capitalizeFully(numberToWord) : "";
    }

    public String getNumberInWordsAllArabic(Object obj) {
        if (obj == null || obj.equals("") || obj.equals("N/A") || obj.equals(0.0)) {
            return "";
        }
        NumberToWord numberToWordConverter = new NumberToWord_ar();
        String objToString = obj.toString().replace(",", "");
        String numberToWord = numberToWordConverter.convert(Double.valueOf(objToString));

        return !StringUtils.isEmpty(numberToWord) ? numberToWord : "";
    }

    public CustomisedITextTable calculateTableByFields(CustomisedITextTable table, String byFiled, String calField, String positiveOrNegative, String notShowAccountNumber) {
        if (table != null && table.getRows() != null && table.getRows().size() > 0 && byFiled != null && calField != null) {
            Map<String, Double> map = new HashMap<>();
            for (HashMap<String, String> hashMap : table.getRows().values()) {
                if (map.containsKey(hashMap.get(byFiled)) && !notShowAccountNumber.equals(hashMap.get(ACCOUNT_NUMBER))) {
                    Double value = getValueAsDouble(hashMap.get(positiveOrNegative));
                    if (value != null && value >= 0) {
                        map.put(hashMap.get(byFiled), (map.get(hashMap.get(byFiled)) + getValueAsDouble(hashMap.get(calField))));
                    }
                } else {
                    Double value = getValueAsDouble(hashMap.get(positiveOrNegative));
                    if (value != null && value >= 0 && !notShowAccountNumber.equals(hashMap.get(ACCOUNT_NUMBER))) {
                        map.put(hashMap.get(byFiled), getValueAsDouble(hashMap.get(calField)));
                    }
                }
            }
            CustomisedITextTable textTable = new CustomisedITextTable();
            textTable.addColumn(byFiled, "");
            textTable.addColumn(calField, "");
            List<String> columnsValue = new ArrayList<>();
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                columnsValue.clear();
                columnsValue.add(entry.getKey());
                columnsValue.add(String.valueOf(entry.getValue()));
                textTable.addRow(columnsValue.toArray(new String[]{}));
            }
            return textTable;
        }
        return null;
    }

    public static String crylToLat(String message){
        message  = message.replace("<br/>","\n").replace("&quot;","\"");
        char[] abcCyr =   {' ','а','б','в','г','ғ','д','е','ё', 'ж','з','и','й','к','қ','л','м','н','о','п','р','с','т','у','ў','ф','х', 'ҳ', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Ғ','Д','Е','Ё', 'Ж','З','И','Й','К','Қ','Л','М','Н','О','П','Р','С','Т','У','Ў','Ф','Х', 'Ҳ', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0','-','–','“','”','\n','.',',','"'};
        String[] abcLat = {" ","a","b","v","g","g'","d","e","yo","j","z","i","y","k","q","l","m","n","o","p","r","s","t","u","o'","f","x", "h", "ts","ch","sh","sh", "'","i", "","e","yu","ya","A","B","V","G","G'","D","E","YO","J","Z","I","Y","K","Q","L","M","N","O","P","R","S","T","U","O'", "F", "X", "H","Ts","Ch","Sh","Sh", "","I", "","E","Yu","Ya","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0","-","–","“","”","\n",".",",","\""};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    public static String latToCry(String message) {
        message = message
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("<br/>", "\n");
        String [] cryl ={"йў" ,"Йў", "ю","Ю","я","Я","ё","Ё","ш","Ш","ч","Ч","ў","Ў",
                "қ","Қ","ғ","Ғ","ц","Ц","й","Й","у","У","к","К",
                "е","Е","н","Н","г","Г","щ","Щ","з","З","х","Х",
                "э","Э","ж","Ж","д","Д","л","Л","о","О","р","Р",
                "п","П","а","А","в","В","ф","Ф","с","С","м","М",
                "и","И","т","Т","б","Б","қ","Қ","ҳ","Ҳ","ғ","Ғ","ь"};

        String [] lat ={"yo'", "Yo'", "yu", "Yu", "ya", "Ya", "yo", "Yo", "sh", "Sh", "ch", "Ch", "o'", "O'",
                "q", "Q", "g'", "G'", "ts", "Ts", "y", "Y", "u", "U", "k", "K", "ye", "Ye",
                "n", "N", "g", "G", "sh", "Sh", "z", "Z", "x", "X", "e", "E", "j", "J",
                "d", "D", "l", "L", "o", "O", "r", "R", "p", "P", "a", "A", "v", "V", "f",
                "F", "s", "S", "m", "M", "i", "I", "t", "T", "b", "B", "q", "Q", "h", "H",
                "g'", "G'", "`"};

        for (int i = 0; i < cryl.length; i++) {
            message=message.replaceAll(lat[i],cryl[i]);
        }
        return message;
    }

    public ProductItem[] getProductItems() {
        return productItems;
    }

    public void setProductItems(ProductItem[] productItems) {
        this.productItems = productItems;
    }

    public void setPriceFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public List<NumericWidget> getNumericWidgets() {
        return numericWidgets;
    }

    public void setNumericWidgets(List<NumericWidget> numericWidgets) {
        this.numericWidgets = numericWidgets;
    }

    public List<NumericWidget> getTableWidgets() {
        return tableWidgets;
    }

    public void setTableWidgets(List<NumericWidget> tableWidgets) {
        this.tableWidgets = tableWidgets;
    }

    public ArrayList<CustomisedITextTable> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<CustomisedITextTable> groups) {
        this.groups = groups;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public HashMap<String, ArrayList<CustomisedITextTable>> getCustomListData3() {
        return customListData3;
    }

    public void setCustomListData3(HashMap<String, ArrayList<CustomisedITextTable>> customListData3) {
        this.customListData3 = customListData3;
    }
}
