package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 23-Jun-2010
 * Time: 19:21:23
 * <p/>
 * This is uses pdf font family
 */
public enum ITextFontTypeEnum {
    ARIAL(1, "arial", "arial.ttf"),
    TIMES_NEW_ROMAN(2, "times new roman", "times.ttf"),
    COMIC_SANS_MS(3, "comic sans ms", "comic.ttf"),
    TAHOMA(4, "tahoma", "tahoma.ttf"),
    VERDANA(5, "verdana", "verdana.ttf"),
    CALIBRI(6, "calibri", "calibri.ttf"),
    ARIAL_NARROW(7, "arial_narrow", "arial_narrow.ttf"),
    AVANT_GARDE(8, "avant_garde", "avant_garde.ttf"),
    TREBUCHET_MS(9, "trebuchet ms", "trebuchet_ms.ttf"),
    ARIALUNI(10, "arial unicode ms", "arialuni.ttf"),
    DEJAVUSANS(11, "dejavusans", "dejavusans.ttf"),
    DEJAVUSANS_BOLD(12, "dejaVuSans-bold", "dejaVuSans-bold.ttf"),
    SOURCE_SANS_PRO(13, "source_sans_pro", "source_sans_pro.ttf"),
    ARIAL_ROUNDED_BOLD(14, "arial rounded mt bold", "arial-rounded-mt-bold.ttf"),
    HELVETICA(15, "helvetica", "helvetica.ttf"),
    GARAMOUND(16, "Garamond", "GARA.TTF"),
    CENTURY_GOTHIC(17, "Century Gothic", "GOTHIC.TTF"),
    Montserrat(18, "Montserrat", "Montserrat-Regular.ttf"),
    Manila_Sans(19, "Manila Sans", "ManilaSansReg.ttf"),
    Helvetica_Neue(20, "Helvetica Neue", "HelveticaNeueLTArabic-Light.ttf"),
    Gotham(21, "Gotham", "GothamLight.ttf"),
    OpenSans(22, "OpenSans", "OpenSans-Regular.ttf"),
    AlsAgroFont(23, "ALS Agrofont", "ALSAgrofont-Regular.ttf");

    ITextFontTypeEnum(Integer id, String name, String fileName) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
    }

    private Integer id;
    private String name;
    private String fileName;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getFileName() {
        return fileName;
    }
}
