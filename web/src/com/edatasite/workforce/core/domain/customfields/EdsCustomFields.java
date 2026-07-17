package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-Nov-2010
 * Time: 16:58:36
 */
@MappedSuperclass
public class EdsCustomFields extends EdsObject {

    public static HashMap<String, String> map = new HashMap<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    @Column(name = "string_value1", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue1;

    @Type(type = "text")
    @Column(name = "string_value2", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue2;

    @Type(type = "text")
    @Column(name = "string_value3", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue3;

    @Type(type = "text")
    @Column(name = "string_value4", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue4;

    @Type(type = "text")
    @Column(name = "string_value5", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue5;

    @Type(type = "text")
    @Column(name = "string_value6", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue6;

    @Type(type = "text")
    @Column(name = "string_value7", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue7;

    @Type(type = "text")
    @Column(name = "string_value8", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue8;

    @Type(type = "text")
    @Column(name = "string_value9", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue9;

    @Type(type = "text")
    @Column(name = "string_value10", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue10;

    @Type(type = "text")
    @Column(name = "string_value11", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue11;

    @Type(type = "text")
    @Column(name = "string_value12", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue12;

    @Type(type = "text")
    @Column(name = "string_value13", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue13;

    @Type(type = "text")
    @Column(name = "string_value14", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue14;

    @Type(type = "text")
    @Column(name = "string_value15", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue15;

    @Type(type = "text")
    @Column(name = "string_value16", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue16;

    @Type(type = "text")
    @Column(name = "string_value17", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue17;

    @Type(type = "text")
    @Column(name = "string_value18", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue18;

    @Type(type = "text")
    @Column(name = "string_value19", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue19;

    @Type(type = "text")
    @Column(name = "string_value20", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue20;

    @Type(type = "text")
    @Column(name = "string_value21", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue21;

    @Type(type = "text")
    @Column(name = "string_value22", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue22;

    @Type(type = "text")
    @Column(name = "string_value23", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue23;

    @Type(type = "text")
    @Column(name = "string_value24", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue24;

    @Type(type = "text")
    @Column(name = "string_value25", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue25;

    @Type(type = "text")
    @Column(name = "string_value26", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue26;

    @Type(type = "text")
    @Column(name = "string_value27", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue27;

    @Type(type = "text")
    @Column(name = "string_value28", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue28;

    @Type(type = "text")
    @Column(name = "string_value29", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue29;

    @Type(type = "text")
    @Column(name = "string_value30", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue30;

    @Type(type = "text")
    @Column(name = "string_value31", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue31;

    @Type(type = "text")
    @Column(name = "string_value32", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue32;

    @Type(type = "text")
    @Column(name = "string_value33", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue33;

    @Type(type = "text")
    @Column(name = "string_value34", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue34;

    @Type(type = "text")
    @Column(name = "string_value35", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue35;

    @Type(type = "text")
    @Column(name = "string_value36", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue36;

    @Type(type = "text")
    @Column(name = "string_value37", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue37;

    @Type(type = "text")
    @Column(name = "string_value38", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue38;

    @Type(type = "text")
    @Column(name = "string_value39", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue39;

    @Type(type = "text")
    @Column(name = "string_value40", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue40;

    @Type(type = "text")
    @Column(name = "string_value41", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue41;

    @Type(type = "text")
    @Column(name = "string_value42", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue42;

    @Type(type = "text")
    @Column(name = "string_value43", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue43;

    @Type(type = "text")
    @Column(name = "string_value44", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue44;

    @Type(type = "text")
    @Column(name = "string_value45", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue45;

    @Type(type = "text")
    @Column(name = "string_value46", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue46;

    @Type(type = "text")
    @Column(name = "string_value47", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue47;

    @Type(type = "text")
    @Column(name = "string_value48", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue48;

    @Type(type = "text")
    @Column(name = "string_value49", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue49;

    @Type(type = "text")
    @Column(name = "string_value50", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue50;

    @Type(type = "text")
    @Column(name = "string_value51", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue51;
    @Type(type = "text")
    @Column(name = "string_value52", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue52;
    @Type(type = "text")
    @Column(name = "string_value53", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue53;
    @Type(type = "text")
    @Column(name = "string_value54", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue54;
    @Type(type = "text")
    @Column(name = "string_value55", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue55;
    @Type(type = "text")
    @Column(name = "string_value56", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue56;
    @Type(type = "text")
    @Column(name = "string_value57", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue57;
    @Type(type = "text")
    @Column(name = "string_value58", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue58;
    @Type(type = "text")
    @Column(name = "string_value59", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue59;
    @Type(type = "text")
    @Column(name = "string_value60", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue60;
    @Type(type = "text")
    @Column(name = "string_value61", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue61;
    @Type(type = "text")
    @Column(name = "string_value62", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue62;
    @Type(type = "text")
    @Column(name = "string_value63", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue63;
    @Type(type = "text")
    @Column(name = "string_value64", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue64;
    @Type(type = "text")
    @Column(name = "string_value65", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue65;
    @Type(type = "text")
    @Column(name = "string_value66", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue66;
    @Type(type = "text")
    @Column(name = "string_value67", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue67;
    @Type(type = "text")
    @Column(name = "string_value68", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue68;
    @Type(type = "text")
    @Column(name = "string_value69", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue69;
    @Type(type = "text")
    @Column(name = "string_value70", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue70;
    @Type(type = "text")
    @Column(name = "string_value71", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue71;
    @Type(type = "text")
    @Column(name = "string_value72", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue72;
    @Type(type = "text")
    @Column(name = "string_value73", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue73;
    @Type(type = "text")
    @Column(name = "string_value74", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue74;
    @Type(type = "text")
    @Column(name = "string_value75", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue75;
    @Type(type = "text")
    @Column(name = "string_value76", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue76;
    @Type(type = "text")
    @Column(name = "string_value77", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue77;
    @Type(type = "text")
    @Column(name = "string_value78", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue78;
    @Type(type = "text")
    @Column(name = "string_value79", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue79;
    @Type(type = "text")
    @Column(name = "string_value80", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue80;
    @Type(type = "text")
    @Column(name = "string_value81", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue81;
    @Type(type = "text")
    @Column(name = "string_value82", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue82;
    @Type(type = "text")
    @Column(name = "string_value83", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue83;
    @Type(type = "text")
    @Column(name = "string_value84", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue84;
    @Type(type = "text")
    @Column(name = "string_value85", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue85;
    @Type(type = "text")
    @Column(name = "string_value86", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue86;
    @Type(type = "text")
    @Column(name = "string_value87", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue87;
    @Type(type = "text")
    @Column(name = "string_value88", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue88;
    @Type(type = "text")
    @Column(name = "string_value89", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue89;
    @Type(type = "text")
    @Column(name = "string_value90", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue90;
    @Type(type = "text")
    @Column(name = "string_value91", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue91;
    @Type(type = "text")
    @Column(name = "string_value92", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue92;
    @Type(type = "text")
    @Column(name = "string_value93", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue93;
    @Type(type = "text")
    @Column(name = "string_value94", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue94;
    @Type(type = "text")
    @Column(name = "string_value95", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue95;
    @Type(type = "text")
    @Column(name = "string_value96", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue96;
    @Type(type = "text")
    @Column(name = "string_value97", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue97;
    @Type(type = "text")
    @Column(name = "string_value98", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue98;
    @Type(type = "text")
    @Column(name = "string_value99", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue99;

    static {
        for (int i = 1; i < Constants.FIELD_LIMIT + 1; i++) {
            map.put(CustomFieldsUtils.DATE_VALUE + i, "DateValue" + i);
        }

        for (int i = 1; i <= Constants.STRING_FIELD_LIMIT; i++) {
            map.put(CustomFieldsUtils.STRING_VALUE + i, "StringValue" + i);
        }

        for (int i = 1; i <= Constants.DOULE_FIELD_LIMIT; i++) {
            map.put(CustomFieldsUtils.DOUBLE_VALUE + i, "DoubleValue" + i);
        }
    }

    @Type(type = "text")
    @Column(name = "stringValue100", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue100;
    @Type(type = "text")
    @Column(name = "stringValue101", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue101;
    @Type(type = "text")
    @Column(name = "stringValue102", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue102;
    @Type(type = "text")
    @Column(name = "stringValue103", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue103;
    @Type(type = "text")
    @Column(name = "stringValue104", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue104;
    @Type(type = "text")
    @Column(name = "stringValue105", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue105;
    @Type(type = "text")
    @Column(name = "stringValue106", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue106;
    @Type(type = "text")
    @Column(name = "stringValue107", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue107;
    @Type(type = "text")
    @Column(name = "stringValue108", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue108;
    @Type(type = "text")
    @Column(name = "stringValue109", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue109;
    @Type(type = "text")
    @Column(name = "stringValue110", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue110;
    @Type(type = "text")
    @Column(name = "stringValue111", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue111;
    @Type(type = "text")
    @Column(name = "stringValue112", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue112;
    @Type(type = "text")
    @Column(name = "stringValue113", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue113;
    @Type(type = "text")
    @Column(name = "stringValue114", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue114;
    @Type(type = "text")
    @Column(name = "stringValue115", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue115;
    @Type(type = "text")
    @Column(name = "stringValue116", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue116;
    @Type(type = "text")
    @Column(name = "stringValue117", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue117;
    @Type(type = "text")
    @Column(name = "stringValue118", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue118;
    @Type(type = "text")
    @Column(name = "stringValue119", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue119;
    @Type(type = "text")
    @Column(name = "stringValue120", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue120;
    @Type(type = "text")
    @Column(name = "stringValue121", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue121;
    @Type(type = "text")
    @Column(name = "stringValue122", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue122;
    @Type(type = "text")
    @Column(name = "stringValue123", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue123;
    @Type(type = "text")
    @Column(name = "stringValue124", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue124;
    @Type(type = "text")
    @Column(name = "stringValue125", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue125;
    @Type(type = "text")
    @Column(name = "stringValue126", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue126;
    @Type(type = "text")
    @Column(name = "stringValue127", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue127;
    @Type(type = "text")
    @Column(name = "stringValue128", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue128;
    @Type(type = "text")
    @Column(name = "stringValue129", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue129;
    @Type(type = "text")
    @Column(name = "stringValue130", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue130;
    @Type(type = "text")
    @Column(name = "stringValue131", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue131;
    @Type(type = "text")
    @Column(name = "stringValue132", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue132;
    @Type(type = "text")
    @Column(name = "stringValue133", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue133;
    @Type(type = "text")
    @Column(name = "stringValue134", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue134;
    @Type(type = "text")
    @Column(name = "stringValue135", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue135;
    @Type(type = "text")
    @Column(name = "stringValue136", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue136;
    @Type(type = "text")
    @Column(name = "stringValue137", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue137;
    @Type(type = "text")
    @Column(name = "stringValue138", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue138;
    @Type(type = "text")
    @Column(name = "stringValue139", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue139;
    @Type(type = "text")
    @Column(name = "stringValue140", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue140;
    @Type(type = "text")
    @Column(name = "stringValue141", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue141;
    @Type(type = "text")
    @Column(name = "stringValue142", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue142;
    @Type(type = "text")
    @Column(name = "stringValue143", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue143;
    @Type(type = "text")
    @Column(name = "stringValue144", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue144;
    @Type(type = "text")
    @Column(name = "stringValue145", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue145;
    @Type(type = "text")
    @Column(name = "stringValue146", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue146;
    @Type(type = "text")
    @Column(name = "stringValue147", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue147;
    @Type(type = "text")
    @Column(name = "stringValue148", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue148;
    @Type(type = "text")
    @Column(name = "stringValue149", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue149;


    @Column(name = "double_value1")
    private Double doubleValue1;

    @Column(name = "double_value2")
    private Double doubleValue2;

    @Column(name = "double_value3")
    private Double doubleValue3;

    @Column(name = "double_value4")
    private Double doubleValue4;

    @Column(name = "double_value5")
    private Double doubleValue5;

    @Column(name = "double_value6")
    private Double doubleValue6;

    @Column(name = "double_value7")
    private Double doubleValue7;

    @Column(name = "double_value8")
    private Double doubleValue8;

    @Column(name = "double_value9")
    private Double doubleValue9;

    @Column(name = "double_value10")
    private Double doubleValue10;

    @Column(name = "double_value11")
    private Double doubleValue11;

    @Column(name = "double_value12")
    private Double doubleValue12;

    @Column(name = "double_value13")
    private Double doubleValue13;

    @Column(name = "double_value14")
    private Double doubleValue14;

    @Column(name = "double_value15")
    private Double doubleValue15;

    @Column(name = "double_value16")
    private Double doubleValue16;

    @Column(name = "double_value17")
    private Double doubleValue17;

    @Column(name = "double_value18")
    private Double doubleValue18;

    @Column(name = "double_value19")
    private Double doubleValue19;

    @Column(name = "double_value20")
    private Double doubleValue20;

    @Column(name = "double_value21")
    private Double doubleValue21;

    @Column(name = "double_value22")
    private Double doubleValue22;

    @Column(name = "double_value23")
    private Double doubleValue23;

    @Column(name = "double_value24")
    private Double doubleValue24;

    @Column(name = "double_value25")
    private Double doubleValue25;

    @Column(name = "double_value26")
    private Double doubleValue26;

    @Column(name = "double_value27")
    private Double doubleValue27;

    @Column(name = "double_value28")
    private Double doubleValue28;

    @Column(name = "double_value29")
    private Double doubleValue29;

    @Column(name = "double_value30")
    private Double doubleValue30;

    @Column(name = "double_value31")
    private Double doubleValue31;

    @Column(name = "double_value32")
    private Double doubleValue32;

    @Column(name = "double_value33")
    private Double doubleValue33;

    @Column(name = "double_value34")
    private Double doubleValue34;

    @Column(name = "double_value35")
    private Double doubleValue35;

    @Column(name = "double_value36")
    private Double doubleValue36;

    @Column(name = "double_value37")
    private Double doubleValue37;

    @Column(name = "double_value38")
    private Double doubleValue38;

    @Column(name = "double_value39")
    private Double doubleValue39;

    @Column(name = "double_value40")
    private Double doubleValue40;

    @Column(name = "double_value41")
    private Double doubleValue41;

    @Column(name = "double_value42")
    private Double doubleValue42;

    @Column(name = "double_value43")
    private Double doubleValue43;

    @Column(name = "double_value44")
    private Double doubleValue44;

    @Column(name = "double_value45")
    private Double doubleValue45;

    @Column(name = "double_value46")
    private Double doubleValue46;

    @Column(name = "double_value47")
    private Double doubleValue47;

    @Column(name = "double_value48")
    private Double doubleValue48;

    @Column(name = "double_value49")
    private Double doubleValue49;

    @Column(name = "double_value50")
    private Double doubleValue50;
    @Type(type = "text")
    @Column(name = "stringValue150", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String stringValue150;
    @Type(type = "text")
    @Column(name = "double_value51", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue51;
    @Type(type = "text")
    @Column(name = "double_value52", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue52;
    @Type(type = "text")
    @Column(name = "double_value53", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue53;
    @Type(type = "text")
    @Column(name = "double_value54", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue54;
    @Type(type = "text")
    @Column(name = "double_value55", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue55;
    @Type(type = "text")
    @Column(name = "double_value56", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue56;
    @Type(type = "text")
    @Column(name = "double_value57", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue57;
    @Type(type = "text")
    @Column(name = "double_value58", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue58;
    @Type(type = "text")
    @Column(name = "double_value59", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue59;
    @Type(type = "text")
    @Column(name = "double_value60", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue60;
    @Type(type = "text")
    @Column(name = "double_value61", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue61;
    @Type(type = "text")
    @Column(name = "double_value62", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue62;
    @Type(type = "text")
    @Column(name = "double_value63", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue63;
    @Type(type = "text")
    @Column(name = "double_value64", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue64;
    @Type(type = "text")
    @Column(name = "double_value65", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue65;
    @Type(type = "text")
    @Column(name = "double_value66", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue66;
    @Type(type = "text")
    @Column(name = "double_value67", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue67;
    @Type(type = "text")
    @Column(name = "double_value68", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue68;
    @Type(type = "text")
    @Column(name = "double_value69", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue69;
    @Type(type = "text")
    @Column(name = "double_value70", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue70;
    @Type(type = "text")
    @Column(name = "double_value71", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue71;
    @Type(type = "text")
    @Column(name = "double_value72", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue72;
    @Type(type = "text")
    @Column(name = "double_value73", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue73;
    @Type(type = "text")
    @Column(name = "double_value74", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue74;
    @Type(type = "text")
    @Column(name = "double_value75", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue75;
    @Type(type = "text")
    @Column(name = "double_value76", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue76;
    @Type(type = "text")
    @Column(name = "double_value77", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue77;
    @Type(type = "text")
    @Column(name = "double_value78", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue78;
    @Type(type = "text")
    @Column(name = "double_value79", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue79;
    @Type(type = "text")
    @Column(name = "double_value80", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue80;
    @Type(type = "text")
    @Column(name = "double_value81", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue81;
    @Type(type = "text")
    @Column(name = "double_value82", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue82;
    @Type(type = "text")
    @Column(name = "double_value83", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue83;
    @Type(type = "text")
    @Column(name = "double_value84", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue84;
    @Type(type = "text")
    @Column(name = "double_value85", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue85;
    @Type(type = "text")
    @Column(name = "double_value86", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue86;
    @Type(type = "text")
    @Column(name = "double_value87", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue87;
    @Type(type = "text")
    @Column(name = "double_value88", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue88;
    @Type(type = "text")
    @Column(name = "double_value89", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue89;
    @Type(type = "text")
    @Column(name = "double_value90", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue90;
    @Type(type = "text")
    @Column(name = "double_value91", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue91;
    @Type(type = "text")
    @Column(name = "double_value92", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue92;
    @Type(type = "text")
    @Column(name = "double_value93", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue93;
    @Type(type = "text")
    @Column(name = "double_value94", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue94;
    @Type(type = "text")
    @Column(name = "double_value95", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue95;
    @Type(type = "text")
    @Column(name = "double_value96", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue96;
    @Type(type = "text")
    @Column(name = "double_value97", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue97;
    @Type(type = "text")
    @Column(name = "double_value98", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue98;
    @Type(type = "text")
    @Column(name = "double_value99", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue99;

    @Column(name = "date_value1")
    private Date dateValue1;

    @Column(name = "date_value2")
    private Date dateValue2;

    @Column(name = "date_value3")
    private Date dateValue3;

    @Column(name = "date_value4")
    private Date dateValue4;

    @Column(name = "date_value5")
    private Date dateValue5;

    @Column(name = "date_value6")
    private Date dateValue6;

    @Column(name = "date_value7")
    private Date dateValue7;

    @Column(name = "date_value8")
    private Date dateValue8;

    @Column(name = "date_value9")
    private Date dateValue9;

    @Column(name = "date_value10")
    private Date dateValue10;

    @Column(name = "date_value11")
    private Date dateValue11;

    @Column(name = "date_value12")
    private Date dateValue12;

    @Column(name = "date_value13")
    private Date dateValue13;

    @Column(name = "date_value14")
    private Date dateValue14;

    @Column(name = "date_value15")
    private Date dateValue15;

    @Column(name = "date_value16")
    private Date dateValue16;

    @Column(name = "date_value17")
    private Date dateValue17;

    @Column(name = "date_value18")
    private Date dateValue18;

    @Column(name = "date_value19")
    private Date dateValue19;

    @Column(name = "date_value20")
    private Date dateValue20;

    @Column(name = "date_value21")
    private Date dateValue21;

    @Column(name = "date_value22")
    private Date dateValue22;

    @Column(name = "date_value23")
    private Date dateValue23;

    @Column(name = "date_value24")
    private Date dateValue24;

    @Column(name = "date_value25")
    private Date dateValue25;

    @Column(name = "date_value26")
    private Date dateValue26;

    @Column(name = "date_value27")
    private Date dateValue27;

    @Column(name = "date_value28")
    private Date dateValue28;

    @Column(name = "date_value29")
    private Date dateValue29;

    @Column(name = "date_value30")
    private Date dateValue30;

    @Column(name = "date_value31")
    private Date dateValue31;

    @Column(name = "date_value32")
    private Date dateValue32;

    @Column(name = "date_value33")
    private Date dateValue33;

    @Column(name = "date_value34")
    private Date dateValue34;

    @Column(name = "date_value35")
    private Date dateValue35;

    @Column(name = "date_value36")
    private Date dateValue36;

    @Column(name = "date_value37")
    private Date dateValue37;

    @Column(name = "date_value38")
    private Date dateValue38;

    @Column(name = "date_value39")
    private Date dateValue39;

    @Column(name = "date_value40")
    private Date dateValue40;

    @Column(name = "date_value41")
    private Date dateValue41;

    @Column(name = "date_value42")
    private Date dateValue42;

    @Column(name = "date_value43")
    private Date dateValue43;

    @Column(name = "date_value44")
    private Date dateValue44;

    @Column(name = "date_value45")
    private Date dateValue45;

    @Column(name = "date_value46")
    private Date dateValue46;

    @Column(name = "date_value47")
    private Date dateValue47;

    @Column(name = "date_value48")
    private Date dateValue48;

    @Column(name = "date_value49")
    private Date dateValue49;

    @Column(name = "date_value50")
    private Date dateValue50;

    @Type(type = "text")
    private String jsonEntities;

    @Transient
    private HashMap<String, String> customEntMap;

    public HashMap<String, String> getCustomEntMap() {
        if (customEntMap == null) {
            customEntMap = new HashMap<>();
        }
        return customEntMap;
    }

    public void setCustomEntMap(HashMap<String, String> customEntMap) {
        this.customEntMap = customEntMap;
    }

    public String getJsonEntities() {
        return jsonEntities;
    }

    public void setJsonEntities(String jsonEntities) {
        this.jsonEntities = jsonEntities;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getStringValue1() {
        return stringValue1;
    }

    public void setStringValue1(String stringValue1) {
        this.stringValue1 = stringValue1;
    }

    public String getStringValue2() {
        return stringValue2;
    }

    public void setStringValue2(String stringValue2) {
        this.stringValue2 = stringValue2;
    }

    public String getStringValue3() {
        return stringValue3;
    }

    public void setStringValue3(String stringValue3) {
        this.stringValue3 = stringValue3;
    }

    public String getStringValue4() {
        return stringValue4;
    }

    public void setStringValue4(String stringValue4) {
        this.stringValue4 = stringValue4;
    }

    public String getStringValue5() {
        return stringValue5;
    }

    public void setStringValue5(String stringValue5) {
        this.stringValue5 = stringValue5;
    }

    public String getStringValue6() {
        return stringValue6;
    }

    public void setStringValue6(String stringValue6) {
        this.stringValue6 = stringValue6;
    }

    public String getStringValue7() {
        return stringValue7;
    }

    public void setStringValue7(String stringValue7) {
        this.stringValue7 = stringValue7;
    }

    public String getStringValue8() {
        return stringValue8;
    }

    public void setStringValue8(String stringValue8) {
        this.stringValue8 = stringValue8;
    }

    public String getStringValue9() {
        return stringValue9;
    }

    public void setStringValue9(String stringValue9) {
        this.stringValue9 = stringValue9;
    }

    public String getStringValue10() {
        return stringValue10;
    }

    public void setStringValue10(String stringValue10) {
        this.stringValue10 = stringValue10;
    }

    public Double getDoubleValue1() {
        return doubleValue1;
    }

    public void setDoubleValue1(Double doubleValue1) {
        this.doubleValue1 = doubleValue1;
    }

    public Double getDoubleValue2() {
        return doubleValue2;
    }

    public void setDoubleValue2(Double doubleValue2) {
        this.doubleValue2 = doubleValue2;
    }

    public Double getDoubleValue3() {
        return doubleValue3;
    }

    public void setDoubleValue3(Double doubleValue3) {
        this.doubleValue3 = doubleValue3;
    }

    public Double getDoubleValue4() {
        return doubleValue4;
    }

    public void setDoubleValue4(Double doubleValue4) {
        this.doubleValue4 = doubleValue4;
    }

    public Double getDoubleValue5() {
        return doubleValue5;
    }

    public void setDoubleValue5(Double doubleValue5) {
        this.doubleValue5 = doubleValue5;
    }

    public Double getDoubleValue6() {
        return doubleValue6;
    }

    public void setDoubleValue6(Double doubleValue6) {
        this.doubleValue6 = doubleValue6;
    }

    public Double getDoubleValue7() {
        return doubleValue7;
    }

    public void setDoubleValue7(Double doubleValue7) {
        this.doubleValue7 = doubleValue7;
    }

    public Double getDoubleValue8() {
        return doubleValue8;
    }

    public void setDoubleValue8(Double doubleValue8) {
        this.doubleValue8 = doubleValue8;
    }

    public Double getDoubleValue9() {
        return doubleValue9;
    }

    public void setDoubleValue9(Double doubleValue9) {
        this.doubleValue9 = doubleValue9;
    }

    public Double getDoubleValue10() {
        return doubleValue10;
    }

    public void setDoubleValue10(Double doubleValue10) {
        this.doubleValue10 = doubleValue10;
    }

    public Date getDateValue1() {
        return dateValue1;
    }

    public void setDateValue1(Date dateValue1) {
        this.dateValue1 = dateValue1;
    }

    public Date getDateValue2() {
        return dateValue2;
    }

    public void setDateValue2(Date dateValue2) {
        this.dateValue2 = dateValue2;
    }

    public Date getDateValue3() {
        return dateValue3;
    }

    public void setDateValue3(Date dateValue3) {
        this.dateValue3 = dateValue3;
    }

    public Date getDateValue4() {
        return dateValue4;
    }

    public void setDateValue4(Date dateValue4) {
        this.dateValue4 = dateValue4;
    }

    public Date getDateValue5() {
        return dateValue5;
    }

    public void setDateValue5(Date dateValue5) {
        this.dateValue5 = dateValue5;
    }

    public Date getDateValue6() {
        return dateValue6;
    }

    public void setDateValue6(Date dateValue6) {
        this.dateValue6 = dateValue6;
    }

    public Date getDateValue7() {
        return dateValue7;
    }

    public void setDateValue7(Date dateValue7) {
        this.dateValue7 = dateValue7;
    }

    public Date getDateValue8() {
        return dateValue8;
    }

    public void setDateValue8(Date dateValue8) {
        this.dateValue8 = dateValue8;
    }

    public Date getDateValue9() {
        return dateValue9;
    }

    public void setDateValue9(Date dateValue9) {
        this.dateValue9 = dateValue9;
    }

    public Date getDateValue10() {
        return dateValue10;
    }

    public void setDateValue10(Date dateValue10) {
        this.dateValue10 = dateValue10;
    }

    public String getStringValue11() {
        return stringValue11;
    }

    public void setStringValue11(String stringValue11) {
        this.stringValue11 = stringValue11;
    }

    public String getStringValue12() {
        return stringValue12;
    }

    public void setStringValue12(String stringValue12) {
        this.stringValue12 = stringValue12;
    }

    public String getStringValue13() {
        return stringValue13;
    }

    public void setStringValue13(String stringValue13) {
        this.stringValue13 = stringValue13;
    }

    public String getStringValue14() {
        return stringValue14;
    }

    public void setStringValue14(String stringValue14) {
        this.stringValue14 = stringValue14;
    }

    public String getStringValue15() {
        return stringValue15;
    }

    public void setStringValue15(String stringValue15) {
        this.stringValue15 = stringValue15;
    }

    public String getStringValue16() {
        return stringValue16;
    }

    public void setStringValue16(String stringValue16) {
        this.stringValue16 = stringValue16;
    }

    public String getStringValue17() {
        return stringValue17;
    }

    public void setStringValue17(String stringValue17) {
        this.stringValue17 = stringValue17;
    }

    public String getStringValue18() {
        return stringValue18;
    }

    public void setStringValue18(String stringValue18) {
        this.stringValue18 = stringValue18;
    }

    public String getStringValue19() {
        return stringValue19;
    }

    public void setStringValue19(String stringValue19) {
        this.stringValue19 = stringValue19;
    }

    public String getStringValue20() {
        return stringValue20;
    }

    public void setStringValue20(String stringValue20) {
        this.stringValue20 = stringValue20;
    }

    public String getStringValue21() {
        return stringValue21;
    }

    public void setStringValue21(String stringValue21) {
        this.stringValue21 = stringValue21;
    }

    public String getStringValue22() {
        return stringValue22;
    }

    public void setStringValue22(String stringValue22) {
        this.stringValue22 = stringValue22;
    }

    public String getStringValue23() {
        return stringValue23;
    }

    public void setStringValue23(String stringValue23) {
        this.stringValue23 = stringValue23;
    }

    public String getStringValue24() {
        return stringValue24;
    }

    public void setStringValue24(String stringValue24) {
        this.stringValue24 = stringValue24;
    }

    public String getStringValue25() {
        return stringValue25;
    }

    public void setStringValue25(String stringValue25) {
        this.stringValue25 = stringValue25;
    }

    public String getStringValue26() {
        return stringValue26;
    }

    public void setStringValue26(String stringValue26) {
        this.stringValue26 = stringValue26;
    }

    public String getStringValue27() {
        return stringValue27;
    }

    public void setStringValue27(String stringValue27) {
        this.stringValue27 = stringValue27;
    }

    public String getStringValue28() {
        return stringValue28;
    }

    public void setStringValue28(String stringValue28) {
        this.stringValue28 = stringValue28;
    }

    public String getStringValue29() {
        return stringValue29;
    }

    public void setStringValue29(String stringValue29) {
        this.stringValue29 = stringValue29;
    }

    public String getStringValue30() {
        return stringValue30;
    }

    public void setStringValue30(String stringValue30) {
        this.stringValue30 = stringValue30;
    }

    public String getStringValue31() {
        return stringValue31;
    }

    public void setStringValue31(String stringValue31) {
        this.stringValue31 = stringValue31;
    }

    public String getStringValue32() {
        return stringValue32;
    }

    public void setStringValue32(String stringValue32) {
        this.stringValue32 = stringValue32;
    }

    public String getStringValue33() {
        return stringValue33;
    }

    public void setStringValue33(String stringValue33) {
        this.stringValue33 = stringValue33;
    }

    public String getStringValue34() {
        return stringValue34;
    }

    public void setStringValue34(String stringValue34) {
        this.stringValue34 = stringValue34;
    }

    public String getStringValue35() {
        return stringValue35;
    }

    public void setStringValue35(String stringValue35) {
        this.stringValue35 = stringValue35;
    }

    public String getStringValue36() {
        return stringValue36;
    }

    public void setStringValue36(String stringValue36) {
        this.stringValue36 = stringValue36;
    }

    public String getStringValue37() {
        return stringValue37;
    }

    public void setStringValue37(String stringValue37) {
        this.stringValue37 = stringValue37;
    }

    public String getStringValue38() {
        return stringValue38;
    }

    public void setStringValue38(String stringValue38) {
        this.stringValue38 = stringValue38;
    }

    public String getStringValue39() {
        return stringValue39;
    }

    public void setStringValue39(String stringValue39) {
        this.stringValue39 = stringValue39;
    }

    public String getStringValue40() {
        return stringValue40;
    }

    public void setStringValue40(String stringValue40) {
        this.stringValue40 = stringValue40;
    }

    public String getStringValue41() {
        return stringValue41;
    }

    public void setStringValue41(String stringValue41) {
        this.stringValue41 = stringValue41;
    }

    public String getStringValue42() {
        return stringValue42;
    }

    public void setStringValue42(String stringValue42) {
        this.stringValue42 = stringValue42;
    }

    public String getStringValue43() {
        return stringValue43;
    }

    public void setStringValue43(String stringValue43) {
        this.stringValue43 = stringValue43;
    }

    public String getStringValue44() {
        return stringValue44;
    }

    public void setStringValue44(String stringValue44) {
        this.stringValue44 = stringValue44;
    }

    public String getStringValue45() {
        return stringValue45;
    }

    public void setStringValue45(String stringValue45) {
        this.stringValue45 = stringValue45;
    }

    public String getStringValue46() {
        return stringValue46;
    }

    public void setStringValue46(String stringValue46) {
        this.stringValue46 = stringValue46;
    }

    public String getStringValue47() {
        return stringValue47;
    }

    public void setStringValue47(String stringValue47) {
        this.stringValue47 = stringValue47;
    }

    public String getStringValue48() {
        return stringValue48;
    }

    public void setStringValue48(String stringValue48) {
        this.stringValue48 = stringValue48;
    }

    public String getStringValue49() {
        return stringValue49;
    }

    public void setStringValue49(String stringValue49) {
        this.stringValue49 = stringValue49;
    }

    public String getStringValue50() {
        return stringValue50;
    }

    public void setStringValue50(String stringValue50) {
        this.stringValue50 = stringValue50;
    }

    public String getStringValue51() {
        return stringValue51;
    }

    public void setStringValue51(String stringValue51) {
        this.stringValue51 = stringValue51;
    }

    public String getStringValue52() {
        return stringValue52;
    }

    public void setStringValue52(String stringValue52) {
        this.stringValue52 = stringValue52;
    }

    public String getStringValue53() {
        return stringValue53;
    }

    public void setStringValue53(String stringValue53) {
        this.stringValue53 = stringValue53;
    }

    public String getStringValue54() {
        return stringValue54;
    }

    public void setStringValue54(String stringValue54) {
        this.stringValue54 = stringValue54;
    }

    public String getStringValue55() {
        return stringValue55;
    }

    public void setStringValue55(String stringValue55) {
        this.stringValue55 = stringValue55;
    }

    public String getStringValue56() {
        return stringValue56;
    }

    public void setStringValue56(String stringValue56) {
        this.stringValue56 = stringValue56;
    }

    public String getStringValue57() {
        return stringValue57;
    }

    public void setStringValue57(String stringValue57) {
        this.stringValue57 = stringValue57;
    }

    public String getStringValue58() {
        return stringValue58;
    }

    public void setStringValue58(String stringValue58) {
        this.stringValue58 = stringValue58;
    }

    public String getStringValue59() {
        return stringValue59;
    }

    public void setStringValue59(String stringValue59) {
        this.stringValue59 = stringValue59;
    }

    public String getStringValue60() {
        return stringValue60;
    }

    public void setStringValue60(String stringValue60) {
        this.stringValue60 = stringValue60;
    }

    public String getStringValue61() {
        return stringValue61;
    }

    public void setStringValue61(String stringValue61) {
        this.stringValue61 = stringValue61;
    }

    public String getStringValue62() {
        return stringValue62;
    }

    public void setStringValue62(String stringValue62) {
        this.stringValue62 = stringValue62;
    }

    public String getStringValue63() {
        return stringValue63;
    }

    public void setStringValue63(String stringValue63) {
        this.stringValue63 = stringValue63;
    }

    public String getStringValue64() {
        return stringValue64;
    }

    public void setStringValue64(String stringValue64) {
        this.stringValue64 = stringValue64;
    }

    public String getStringValue65() {
        return stringValue65;
    }

    public void setStringValue65(String stringValue65) {
        this.stringValue65 = stringValue65;
    }

    public String getStringValue66() {
        return stringValue66;
    }

    public void setStringValue66(String stringValue66) {
        this.stringValue66 = stringValue66;
    }

    public String getStringValue67() {
        return stringValue67;
    }

    public void setStringValue67(String stringValue67) {
        this.stringValue67 = stringValue67;
    }

    public String getStringValue68() {
        return stringValue68;
    }

    public void setStringValue68(String stringValue68) {
        this.stringValue68 = stringValue68;
    }

    public String getStringValue69() {
        return stringValue69;
    }

    public void setStringValue69(String stringValue69) {
        this.stringValue69 = stringValue69;
    }

    public String getStringValue70() {
        return stringValue70;
    }

    public void setStringValue70(String stringValue70) {
        this.stringValue70 = stringValue70;
    }

    public String getStringValue71() {
        return stringValue71;
    }

    public void setStringValue71(String stringValue71) {
        this.stringValue71 = stringValue71;
    }

    public String getStringValue72() {
        return stringValue72;
    }

    public void setStringValue72(String stringValue72) {
        this.stringValue72 = stringValue72;
    }

    public String getStringValue73() {
        return stringValue73;
    }

    public void setStringValue73(String stringValue73) {
        this.stringValue73 = stringValue73;
    }

    public String getStringValue74() {
        return stringValue74;
    }

    public void setStringValue74(String stringValue74) {
        this.stringValue74 = stringValue74;
    }

    public String getStringValue75() {
        return stringValue75;
    }

    public void setStringValue75(String stringValue75) {
        this.stringValue75 = stringValue75;
    }

    public String getStringValue76() {
        return stringValue76;
    }

    public void setStringValue76(String stringValue76) {
        this.stringValue76 = stringValue76;
    }

    public String getStringValue77() {
        return stringValue77;
    }

    public void setStringValue77(String stringValue77) {
        this.stringValue77 = stringValue77;
    }

    public String getStringValue78() {
        return stringValue78;
    }

    public void setStringValue78(String stringValue78) {
        this.stringValue78 = stringValue78;
    }

    public String getStringValue79() {
        return stringValue79;
    }

    public void setStringValue79(String stringValue79) {
        this.stringValue79 = stringValue79;
    }

    public String getStringValue80() {
        return stringValue80;
    }

    public void setStringValue80(String stringValue80) {
        this.stringValue80 = stringValue80;
    }

    public String getStringValue81() {
        return stringValue81;
    }

    public void setStringValue81(String stringValue81) {
        this.stringValue81 = stringValue81;
    }

    public String getStringValue82() {
        return stringValue82;
    }

    public void setStringValue82(String stringValue82) {
        this.stringValue82 = stringValue82;
    }

    public String getStringValue83() {
        return stringValue83;
    }

    public void setStringValue83(String stringValue83) {
        this.stringValue83 = stringValue83;
    }

    public String getStringValue84() {
        return stringValue84;
    }

    public void setStringValue84(String stringValue84) {
        this.stringValue84 = stringValue84;
    }

    public String getStringValue85() {
        return stringValue85;
    }

    public void setStringValue85(String stringValue85) {
        this.stringValue85 = stringValue85;
    }

    public String getStringValue86() {
        return stringValue86;
    }

    public void setStringValue86(String stringValue86) {
        this.stringValue86 = stringValue86;
    }

    public String getStringValue87() {
        return stringValue87;
    }

    public void setStringValue87(String stringValue87) {
        this.stringValue87 = stringValue87;
    }

    public String getStringValue88() {
        return stringValue88;
    }

    public void setStringValue88(String stringValue88) {
        this.stringValue88 = stringValue88;
    }

    public String getStringValue89() {
        return stringValue89;
    }

    public void setStringValue89(String stringValue89) {
        this.stringValue89 = stringValue89;
    }

    public String getStringValue90() {
        return stringValue90;
    }

    public void setStringValue90(String stringValue90) {
        this.stringValue90 = stringValue90;
    }

    public String getStringValue91() {
        return stringValue91;
    }

    public void setStringValue91(String stringValue91) {
        this.stringValue91 = stringValue91;
    }

    public String getStringValue92() {
        return stringValue92;
    }

    public void setStringValue92(String stringValue92) {
        this.stringValue92 = stringValue92;
    }

    public String getStringValue93() {
        return stringValue93;
    }

    public void setStringValue93(String stringValue93) {
        this.stringValue93 = stringValue93;
    }

    public String getStringValue94() {
        return stringValue94;
    }

    public void setStringValue94(String stringValue94) {
        this.stringValue94 = stringValue94;
    }

    public String getStringValue95() {
        return stringValue95;
    }

    public void setStringValue95(String stringValue95) {
        this.stringValue95 = stringValue95;
    }

    public String getStringValue96() {
        return stringValue96;
    }

    public void setStringValue96(String stringValue96) {
        this.stringValue96 = stringValue96;
    }

    public String getStringValue97() {
        return stringValue97;
    }

    public void setStringValue97(String stringValue97) {
        this.stringValue97 = stringValue97;
    }

    public String getStringValue98() {
        return stringValue98;
    }

    public void setStringValue98(String stringValue98) {
        this.stringValue98 = stringValue98;
    }

    public String getStringValue99() {
        return stringValue99;
    }

    public void setStringValue99(String stringValue99) {
        this.stringValue99 = stringValue99;
    }

    public String getStringValue100() {
        return stringValue100;
    }

    public void setStringValue100(String stringValue100) {
        this.stringValue100 = stringValue100;
    }

    public Double getDoubleValue11() {
        return doubleValue11;
    }

    public void setDoubleValue11(Double doubleValue11) {
        this.doubleValue11 = doubleValue11;
    }

    public Double getDoubleValue12() {
        return doubleValue12;
    }

    public void setDoubleValue12(Double doubleValue12) {
        this.doubleValue12 = doubleValue12;
    }

    public Double getDoubleValue13() {
        return doubleValue13;
    }

    public void setDoubleValue13(Double doubleValue13) {
        this.doubleValue13 = doubleValue13;
    }

    public Double getDoubleValue14() {
        return doubleValue14;
    }

    public void setDoubleValue14(Double doubleValue14) {
        this.doubleValue14 = doubleValue14;
    }

    public Double getDoubleValue15() {
        return doubleValue15;
    }

    public void setDoubleValue15(Double doubleValue15) {
        this.doubleValue15 = doubleValue15;
    }

    public Double getDoubleValue16() {
        return doubleValue16;
    }

    public void setDoubleValue16(Double doubleValue16) {
        this.doubleValue16 = doubleValue16;
    }

    public Double getDoubleValue17() {
        return doubleValue17;
    }

    public void setDoubleValue17(Double doubleValue17) {
        this.doubleValue17 = doubleValue17;
    }

    public Double getDoubleValue18() {
        return doubleValue18;
    }

    public void setDoubleValue18(Double doubleValue18) {
        this.doubleValue18 = doubleValue18;
    }

    public Double getDoubleValue19() {
        return doubleValue19;
    }

    public void setDoubleValue19(Double doubleValue19) {
        this.doubleValue19 = doubleValue19;
    }

    public Double getDoubleValue20() {
        return doubleValue20;
    }

    public void setDoubleValue20(Double doubleValue20) {
        this.doubleValue20 = doubleValue20;
    }

    public Double getDoubleValue21() {
        return doubleValue21;
    }

    public void setDoubleValue21(Double doubleValue21) {
        this.doubleValue21 = doubleValue21;
    }

    public Double getDoubleValue22() {
        return doubleValue22;
    }

    public void setDoubleValue22(Double doubleValue22) {
        this.doubleValue22 = doubleValue22;
    }

    public Double getDoubleValue23() {
        return doubleValue23;
    }

    public void setDoubleValue23(Double doubleValue23) {
        this.doubleValue23 = doubleValue23;
    }

    public Double getDoubleValue24() {
        return doubleValue24;
    }

    public void setDoubleValue24(Double doubleValue24) {
        this.doubleValue24 = doubleValue24;
    }

    public Double getDoubleValue25() {
        return doubleValue25;
    }

    public void setDoubleValue25(Double doubleValue25) {
        this.doubleValue25 = doubleValue25;
    }

    public Double getDoubleValue26() {
        return doubleValue26;
    }

    public void setDoubleValue26(Double doubleValue26) {
        this.doubleValue26 = doubleValue26;
    }

    public Double getDoubleValue27() {
        return doubleValue27;
    }

    public void setDoubleValue27(Double doubleValue27) {
        this.doubleValue27 = doubleValue27;
    }

    public Double getDoubleValue28() {
        return doubleValue28;
    }

    public void setDoubleValue28(Double doubleValue28) {
        this.doubleValue28 = doubleValue28;
    }

    public Double getDoubleValue29() {
        return doubleValue29;
    }

    public void setDoubleValue29(Double doubleValue29) {
        this.doubleValue29 = doubleValue29;
    }

    public Double getDoubleValue30() {
        return doubleValue30;
    }

    public void setDoubleValue30(Double doubleValue30) {
        this.doubleValue30 = doubleValue30;
    }

    public Double getDoubleValue31() {
        return doubleValue31;
    }

    public void setDoubleValue31(Double doubleValue31) {
        this.doubleValue31 = doubleValue31;
    }

    public Double getDoubleValue32() {
        return doubleValue32;
    }

    public void setDoubleValue32(Double doubleValue32) {
        this.doubleValue32 = doubleValue32;
    }

    public Double getDoubleValue33() {
        return doubleValue33;
    }

    public void setDoubleValue33(Double doubleValue33) {
        this.doubleValue33 = doubleValue33;
    }

    public Double getDoubleValue34() {
        return doubleValue34;
    }

    public void setDoubleValue34(Double doubleValue34) {
        this.doubleValue34 = doubleValue34;
    }

    public Double getDoubleValue35() {
        return doubleValue35;
    }

    public void setDoubleValue35(Double doubleValue35) {
        this.doubleValue35 = doubleValue35;
    }

    public Double getDoubleValue36() {
        return doubleValue36;
    }

    public void setDoubleValue36(Double doubleValue36) {
        this.doubleValue36 = doubleValue36;
    }

    public Double getDoubleValue37() {
        return doubleValue37;
    }

    public void setDoubleValue37(Double doubleValue37) {
        this.doubleValue37 = doubleValue37;
    }

    public Double getDoubleValue38() {
        return doubleValue38;
    }

    public void setDoubleValue38(Double doubleValue38) {
        this.doubleValue38 = doubleValue38;
    }

    public Double getDoubleValue39() {
        return doubleValue39;
    }

    public void setDoubleValue39(Double doubleValue39) {
        this.doubleValue39 = doubleValue39;
    }

    public Double getDoubleValue40() {
        return doubleValue40;
    }

    public void setDoubleValue40(Double doubleValue40) {
        this.doubleValue40 = doubleValue40;
    }

    public Double getDoubleValue41() {
        return doubleValue41;
    }

    public void setDoubleValue41(Double doubleValue41) {
        this.doubleValue41 = doubleValue41;
    }

    public Double getDoubleValue42() {
        return doubleValue42;
    }

    public void setDoubleValue42(Double doubleValue42) {
        this.doubleValue42 = doubleValue42;
    }

    public Double getDoubleValue43() {
        return doubleValue43;
    }

    public void setDoubleValue43(Double doubleValue43) {
        this.doubleValue43 = doubleValue43;
    }

    public Double getDoubleValue44() {
        return doubleValue44;
    }

    public void setDoubleValue44(Double doubleValue44) {
        this.doubleValue44 = doubleValue44;
    }

    public Double getDoubleValue45() {
        return doubleValue45;
    }

    public void setDoubleValue45(Double doubleValue45) {
        this.doubleValue45 = doubleValue45;
    }

    public Double getDoubleValue46() {
        return doubleValue46;
    }

    public void setDoubleValue46(Double doubleValue46) {
        this.doubleValue46 = doubleValue46;
    }

    public Double getDoubleValue47() {
        return doubleValue47;
    }

    public void setDoubleValue47(Double doubleValue47) {
        this.doubleValue47 = doubleValue47;
    }

    public Double getDoubleValue48() {
        return doubleValue48;
    }

    public void setDoubleValue48(Double doubleValue48) {
        this.doubleValue48 = doubleValue48;
    }

    public Double getDoubleValue49() {
        return doubleValue49;
    }

    public void setDoubleValue49(Double doubleValue49) {
        this.doubleValue49 = doubleValue49;
    }

    public Double getDoubleValue50() {
        return doubleValue50;
    }

    public void setDoubleValue50(Double doubleValue50) {
        this.doubleValue50 = doubleValue50;
    }

    public Date getDateValue11() {
        return dateValue11;
    }

    public void setDateValue11(Date dateValue11) {
        this.dateValue11 = dateValue11;
    }

    public Date getDateValue12() {
        return dateValue12;
    }

    public void setDateValue12(Date dateValue12) {
        this.dateValue12 = dateValue12;
    }

    public Date getDateValue13() {
        return dateValue13;
    }

    public void setDateValue13(Date dateValue13) {
        this.dateValue13 = dateValue13;
    }

    public Date getDateValue14() {
        return dateValue14;
    }

    public void setDateValue14(Date dateValue14) {
        this.dateValue14 = dateValue14;
    }

    public Date getDateValue15() {
        return dateValue15;
    }

    public void setDateValue15(Date dateValue15) {
        this.dateValue15 = dateValue15;
    }

    public Date getDateValue16() {
        return dateValue16;
    }

    public void setDateValue16(Date dateValue16) {
        this.dateValue16 = dateValue16;
    }

    public Date getDateValue17() {
        return dateValue17;
    }

    public void setDateValue17(Date dateValue17) {
        this.dateValue17 = dateValue17;
    }

    public Date getDateValue18() {
        return dateValue18;
    }

    public void setDateValue18(Date dateValue18) {
        this.dateValue18 = dateValue18;
    }

    public Date getDateValue19() {
        return dateValue19;
    }

    public void setDateValue19(Date dateValue19) {
        this.dateValue19 = dateValue19;
    }

    public Date getDateValue20() {
        return dateValue20;
    }

    public void setDateValue20(Date dateValue20) {
        this.dateValue20 = dateValue20;
    }

    public Date getDateValue21() {
        return dateValue21;
    }

    public void setDateValue21(Date dateValue21) {
        this.dateValue21 = dateValue21;
    }

    public Date getDateValue22() {
        return dateValue22;
    }

    public void setDateValue22(Date dateValue22) {
        this.dateValue22 = dateValue22;
    }

    public Date getDateValue23() {
        return dateValue23;
    }

    public void setDateValue23(Date dateValue23) {
        this.dateValue23 = dateValue23;
    }

    public Date getDateValue24() {
        return dateValue24;
    }

    public void setDateValue24(Date dateValue24) {
        this.dateValue24 = dateValue24;
    }

    public Date getDateValue25() {
        return dateValue25;
    }

    public void setDateValue25(Date dateValue25) {
        this.dateValue25 = dateValue25;
    }

    public Date getDateValue26() {
        return dateValue26;
    }

    public void setDateValue26(Date dateValue26) {
        this.dateValue26 = dateValue26;
    }

    public Date getDateValue27() {
        return dateValue27;
    }

    public void setDateValue27(Date dateValue27) {
        this.dateValue27 = dateValue27;
    }

    public Date getDateValue28() {
        return dateValue28;
    }

    public void setDateValue28(Date dateValue28) {
        this.dateValue28 = dateValue28;
    }

    public Date getDateValue29() {
        return dateValue29;
    }

    public void setDateValue29(Date dateValue29) {
        this.dateValue29 = dateValue29;
    }

    public Date getDateValue30() {
        return dateValue30;
    }

    public void setDateValue30(Date dateValue30) {
        this.dateValue30 = dateValue30;
    }

    public Date getDateValue31() {
        return dateValue31;
    }

    public void setDateValue31(Date dateValue31) {
        this.dateValue31 = dateValue31;
    }

    public Date getDateValue32() {
        return dateValue32;
    }

    public void setDateValue32(Date dateValue32) {
        this.dateValue32 = dateValue32;
    }

    public Date getDateValue33() {
        return dateValue33;
    }

    public void setDateValue33(Date dateValue33) {
        this.dateValue33 = dateValue33;
    }

    public Date getDateValue34() {
        return dateValue34;
    }

    public void setDateValue34(Date dateValue34) {
        this.dateValue34 = dateValue34;
    }

    public Date getDateValue35() {
        return dateValue35;
    }

    public void setDateValue35(Date dateValue35) {
        this.dateValue35 = dateValue35;
    }

    public Date getDateValue36() {
        return dateValue36;
    }

    public void setDateValue36(Date dateValue36) {
        this.dateValue36 = dateValue36;
    }

    public Date getDateValue37() {
        return dateValue37;
    }

    public void setDateValue37(Date dateValue37) {
        this.dateValue37 = dateValue37;
    }

    public Date getDateValue38() {
        return dateValue38;
    }

    public void setDateValue38(Date dateValue38) {
        this.dateValue38 = dateValue38;
    }

    public Date getDateValue39() {
        return dateValue39;
    }

    public void setDateValue39(Date dateValue39) {
        this.dateValue39 = dateValue39;
    }

    public Date getDateValue40() {
        return dateValue40;
    }

    public void setDateValue40(Date dateValue40) {
        this.dateValue40 = dateValue40;
    }

    public Date getDateValue41() {
        return dateValue41;
    }

    public void setDateValue41(Date dateValue41) {
        this.dateValue41 = dateValue41;
    }

    public Date getDateValue42() {
        return dateValue42;
    }

    public void setDateValue42(Date dateValue42) {
        this.dateValue42 = dateValue42;
    }

    public Date getDateValue43() {
        return dateValue43;
    }

    public void setDateValue43(Date dateValue43) {
        this.dateValue43 = dateValue43;
    }

    public Date getDateValue44() {
        return dateValue44;
    }

    public void setDateValue44(Date dateValue44) {
        this.dateValue44 = dateValue44;
    }

    public Date getDateValue45() {
        return dateValue45;
    }

    public void setDateValue45(Date dateValue45) {
        this.dateValue45 = dateValue45;
    }

    public Date getDateValue46() {
        return dateValue46;
    }

    public void setDateValue46(Date dateValue46) {
        this.dateValue46 = dateValue46;
    }

    public Date getDateValue47() {
        return dateValue47;
    }

    public void setDateValue47(Date dateValue47) {
        this.dateValue47 = dateValue47;
    }

    public Date getDateValue48() {
        return dateValue48;
    }

    public void setDateValue48(Date dateValue48) {
        this.dateValue48 = dateValue48;
    }

    public Date getDateValue49() {
        return dateValue49;
    }

    public void setDateValue49(Date dateValue49) {
        this.dateValue49 = dateValue49;
    }

    public Date getDateValue50() {
        return dateValue50;
    }

    public void setDateValue50(Date dateValue50) {
        this.dateValue50 = dateValue50;
    }

    @Type(type = "text")
    @Column(name = "doubleValue100", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String doubleValue100;

    public String getStringValue101() {
        return stringValue101;
    }

    public void setStringValue101(String stringValue101) {
        this.stringValue101 = stringValue101;
    }

    public String getStringValue102() {
        return stringValue102;
    }

    public void setStringValue102(String stringValue102) {
        this.stringValue102 = stringValue102;
    }

    public String getStringValue103() {
        return stringValue103;
    }

    public void setStringValue103(String stringValue103) {
        this.stringValue103 = stringValue103;
    }

    public String getStringValue104() {
        return stringValue104;
    }

    public void setStringValue104(String stringValue104) {
        this.stringValue104 = stringValue104;
    }

    public String getStringValue105() {
        return stringValue105;
    }

    public void setStringValue105(String stringValue105) {
        this.stringValue105 = stringValue105;
    }

    public String getStringValue106() {
        return stringValue106;
    }

    public void setStringValue106(String stringValue106) {
        this.stringValue106 = stringValue106;
    }

    public String getStringValue107() {
        return stringValue107;
    }

    public void setStringValue107(String stringValue107) {
        this.stringValue107 = stringValue107;
    }

    public String getStringValue108() {
        return stringValue108;
    }

    public void setStringValue108(String stringValue108) {
        this.stringValue108 = stringValue108;
    }

    public String getStringValue109() {
        return stringValue109;
    }

    public void setStringValue109(String stringValue109) {
        this.stringValue109 = stringValue109;
    }

    public String getStringValue110() {
        return stringValue110;
    }

    public void setStringValue110(String stringValue110) {
        this.stringValue110 = stringValue110;
    }

    public String getStringValue111() {
        return stringValue111;
    }

    public void setStringValue111(String stringValue111) {
        this.stringValue111 = stringValue111;
    }

    public String getStringValue112() {
        return stringValue112;
    }

    public void setStringValue112(String stringValue112) {
        this.stringValue112 = stringValue112;
    }

    public String getStringValue113() {
        return stringValue113;
    }

    public void setStringValue113(String stringValue113) {
        this.stringValue113 = stringValue113;
    }

    public String getStringValue114() {
        return stringValue114;
    }

    public void setStringValue114(String stringValue114) {
        this.stringValue114 = stringValue114;
    }

    public String getStringValue115() {
        return stringValue115;
    }

    public void setStringValue115(String stringValue115) {
        this.stringValue115 = stringValue115;
    }

    public String getStringValue116() {
        return stringValue116;
    }

    public void setStringValue116(String stringValue116) {
        this.stringValue116 = stringValue116;
    }

    public String getStringValue117() {
        return stringValue117;
    }

    public void setStringValue117(String stringValue117) {
        this.stringValue117 = stringValue117;
    }

    public String getStringValue118() {
        return stringValue118;
    }

    public void setStringValue118(String stringValue118) {
        this.stringValue118 = stringValue118;
    }

    public String getStringValue119() {
        return stringValue119;
    }

    public void setStringValue119(String stringValue119) {
        this.stringValue119 = stringValue119;
    }

    public String getStringValue120() {
        return stringValue120;
    }

    public void setStringValue120(String stringValue120) {
        this.stringValue120 = stringValue120;
    }

    public String getStringValue121() {
        return stringValue121;
    }

    public void setStringValue121(String stringValue121) {
        this.stringValue121 = stringValue121;
    }

    public String getStringValue122() {
        return stringValue122;
    }

    public void setStringValue122(String stringValue122) {
        this.stringValue122 = stringValue122;
    }

    public String getStringValue123() {
        return stringValue123;
    }

    public void setStringValue123(String stringValue123) {
        this.stringValue123 = stringValue123;
    }

    public String getStringValue124() {
        return stringValue124;
    }

    public void setStringValue124(String stringValue124) {
        this.stringValue124 = stringValue124;
    }

    public String getStringValue125() {
        return stringValue125;
    }

    public void setStringValue125(String stringValue125) {
        this.stringValue125 = stringValue125;
    }

    public String getStringValue126() {
        return stringValue126;
    }

    public void setStringValue126(String stringValue126) {
        this.stringValue126 = stringValue126;
    }

    public String getStringValue127() {
        return stringValue127;
    }

    public void setStringValue127(String stringValue127) {
        this.stringValue127 = stringValue127;
    }

    public String getStringValue128() {
        return stringValue128;
    }

    public void setStringValue128(String stringValue128) {
        this.stringValue128 = stringValue128;
    }

    public String getStringValue129() {
        return stringValue129;
    }

    public void setStringValue129(String stringValue129) {
        this.stringValue129 = stringValue129;
    }

    public String getStringValue130() {
        return stringValue130;
    }

    public void setStringValue130(String stringValue130) {
        this.stringValue130 = stringValue130;
    }

    public String getStringValue131() {
        return stringValue131;
    }

    public void setStringValue131(String stringValue131) {
        this.stringValue131 = stringValue131;
    }

    public String getStringValue132() {
        return stringValue132;
    }

    public void setStringValue132(String stringValue132) {
        this.stringValue132 = stringValue132;
    }

    public String getStringValue133() {
        return stringValue133;
    }

    public void setStringValue133(String stringValue133) {
        this.stringValue133 = stringValue133;
    }

    public String getStringValue134() {
        return stringValue134;
    }

    public void setStringValue134(String stringValue134) {
        this.stringValue134 = stringValue134;
    }

    public String getStringValue135() {
        return stringValue135;
    }

    public void setStringValue135(String stringValue135) {
        this.stringValue135 = stringValue135;
    }

    public String getStringValue136() {
        return stringValue136;
    }

    public void setStringValue136(String stringValue136) {
        this.stringValue136 = stringValue136;
    }

    public String getStringValue137() {
        return stringValue137;
    }

    public void setStringValue137(String stringValue137) {
        this.stringValue137 = stringValue137;
    }

    public String getStringValue138() {
        return stringValue138;
    }

    public void setStringValue138(String stringValue138) {
        this.stringValue138 = stringValue138;
    }

    public String getStringValue139() {
        return stringValue139;
    }

    public void setStringValue139(String stringValue139) {
        this.stringValue139 = stringValue139;
    }

    public String getStringValue140() {
        return stringValue140;
    }

    public void setStringValue140(String stringValue140) {
        this.stringValue140 = stringValue140;
    }

    public String getStringValue141() {
        return stringValue141;
    }

    public void setStringValue141(String stringValue141) {
        this.stringValue141 = stringValue141;
    }

    public String getStringValue142() {
        return stringValue142;
    }

    public void setStringValue142(String stringValue142) {
        this.stringValue142 = stringValue142;
    }

    public String getStringValue143() {
        return stringValue143;
    }

    public void setStringValue143(String stringValue143) {
        this.stringValue143 = stringValue143;
    }

    public String getStringValue144() {
        return stringValue144;
    }

    public void setStringValue144(String stringValue144) {
        this.stringValue144 = stringValue144;
    }

    public String getStringValue145() {
        return stringValue145;
    }

    public void setStringValue145(String stringValue145) {
        this.stringValue145 = stringValue145;
    }

    public String getStringValue146() {
        return stringValue146;
    }

    public void setStringValue146(String stringValue146) {
        this.stringValue146 = stringValue146;
    }

    public String getStringValue147() {
        return stringValue147;
    }

    public void setStringValue147(String stringValue147) {
        this.stringValue147 = stringValue147;
    }

    public String getStringValue148() {
        return stringValue148;
    }

    public void setStringValue148(String stringValue148) {
        this.stringValue148 = stringValue148;
    }

    public String getStringValue149() {
        return stringValue149;
    }

    public void setStringValue149(String stringValue149) {
        this.stringValue149 = stringValue149;
    }

    public String getStringValue150() {
        return stringValue150;
    }

    public void setStringValue150(String stringValue150) {
        this.stringValue150 = stringValue150;
    }

    public String getDoubleValue51() {
        return doubleValue51;
    }

    public void setDoubleValue51(String doubleValue51) {
        this.doubleValue51 = doubleValue51;
    }

    public String getDoubleValue52() {
        return doubleValue52;
    }

    public void setDoubleValue52(String doubleValue52) {
        this.doubleValue52 = doubleValue52;
    }

    public String getDoubleValue53() {
        return doubleValue53;
    }

    public void setDoubleValue53(String doubleValue53) {
        this.doubleValue53 = doubleValue53;
    }

    public String getDoubleValue54() {
        return doubleValue54;
    }

    public void setDoubleValue54(String doubleValue54) {
        this.doubleValue54 = doubleValue54;
    }

    public String getDoubleValue55() {
        return doubleValue55;
    }

    public void setDoubleValue55(String doubleValue55) {
        this.doubleValue55 = doubleValue55;
    }

    public String getDoubleValue56() {
        return doubleValue56;
    }

    public void setDoubleValue56(String doubleValue56) {
        this.doubleValue56 = doubleValue56;
    }

    public String getDoubleValue57() {
        return doubleValue57;
    }

    public void setDoubleValue57(String doubleValue57) {
        this.doubleValue57 = doubleValue57;
    }

    public String getDoubleValue58() {
        return doubleValue58;
    }

    public void setDoubleValue58(String doubleValue58) {
        this.doubleValue58 = doubleValue58;
    }

    public String getDoubleValue59() {
        return doubleValue59;
    }

    public void setDoubleValue59(String doubleValue59) {
        this.doubleValue59 = doubleValue59;
    }

    public String getDoubleValue60() {
        return doubleValue60;
    }

    public void setDoubleValue60(String doubleValue60) {
        this.doubleValue60 = doubleValue60;
    }

    public String getDoubleValue61() {
        return doubleValue61;
    }

    public void setDoubleValue61(String doubleValue61) {
        this.doubleValue61 = doubleValue61;
    }

    public String getDoubleValue62() {
        return doubleValue62;
    }

    public void setDoubleValue62(String doubleValue62) {
        this.doubleValue62 = doubleValue62;
    }

    public String getDoubleValue63() {
        return doubleValue63;
    }

    public void setDoubleValue63(String doubleValue63) {
        this.doubleValue63 = doubleValue63;
    }

    public String getDoubleValue64() {
        return doubleValue64;
    }

    public void setDoubleValue64(String doubleValue64) {
        this.doubleValue64 = doubleValue64;
    }

    public String getDoubleValue65() {
        return doubleValue65;
    }

    public void setDoubleValue65(String doubleValue65) {
        this.doubleValue65 = doubleValue65;
    }

    public String getDoubleValue66() {
        return doubleValue66;
    }

    public void setDoubleValue66(String doubleValue66) {
        this.doubleValue66 = doubleValue66;
    }

    public String getDoubleValue67() {
        return doubleValue67;
    }

    public void setDoubleValue67(String doubleValue67) {
        this.doubleValue67 = doubleValue67;
    }

    public String getDoubleValue68() {
        return doubleValue68;
    }

    public void setDoubleValue68(String doubleValue68) {
        this.doubleValue68 = doubleValue68;
    }

    public String getDoubleValue69() {
        return doubleValue69;
    }

    public void setDoubleValue69(String doubleValue69) {
        this.doubleValue69 = doubleValue69;
    }

    public String getDoubleValue70() {
        return doubleValue70;
    }

    public void setDoubleValue70(String doubleValue70) {
        this.doubleValue70 = doubleValue70;
    }

    public String getDoubleValue71() {
        return doubleValue71;
    }

    public void setDoubleValue71(String doubleValue71) {
        this.doubleValue71 = doubleValue71;
    }

    public String getDoubleValue72() {
        return doubleValue72;
    }

    public void setDoubleValue72(String doubleValue72) {
        this.doubleValue72 = doubleValue72;
    }

    public String getDoubleValue73() {
        return doubleValue73;
    }

    public void setDoubleValue73(String doubleValue73) {
        this.doubleValue73 = doubleValue73;
    }

    public String getDoubleValue74() {
        return doubleValue74;
    }

    public void setDoubleValue74(String doubleValue74) {
        this.doubleValue74 = doubleValue74;
    }

    public String getDoubleValue75() {
        return doubleValue75;
    }

    public void setDoubleValue75(String doubleValue75) {
        this.doubleValue75 = doubleValue75;
    }

    public String getDoubleValue76() {
        return doubleValue76;
    }

    public void setDoubleValue76(String doubleValue76) {
        this.doubleValue76 = doubleValue76;
    }

    public String getDoubleValue77() {
        return doubleValue77;
    }

    public void setDoubleValue77(String doubleValue77) {
        this.doubleValue77 = doubleValue77;
    }

    public String getDoubleValue78() {
        return doubleValue78;
    }

    public void setDoubleValue78(String doubleValue78) {
        this.doubleValue78 = doubleValue78;
    }

    public String getDoubleValue79() {
        return doubleValue79;
    }

    public void setDoubleValue79(String doubleValue79) {
        this.doubleValue79 = doubleValue79;
    }

    public String getDoubleValue80() {
        return doubleValue80;
    }

    public void setDoubleValue80(String doubleValue80) {
        this.doubleValue80 = doubleValue80;
    }

    public String getDoubleValue81() {
        return doubleValue81;
    }

    public void setDoubleValue81(String doubleValue81) {
        this.doubleValue81 = doubleValue81;
    }

    public String getDoubleValue82() {
        return doubleValue82;
    }

    public void setDoubleValue82(String doubleValue82) {
        this.doubleValue82 = doubleValue82;
    }

    public String getDoubleValue83() {
        return doubleValue83;
    }

    public void setDoubleValue83(String doubleValue83) {
        this.doubleValue83 = doubleValue83;
    }

    public String getDoubleValue84() {
        return doubleValue84;
    }

    public void setDoubleValue84(String doubleValue84) {
        this.doubleValue84 = doubleValue84;
    }

    public String getDoubleValue85() {
        return doubleValue85;
    }

    public void setDoubleValue85(String doubleValue85) {
        this.doubleValue85 = doubleValue85;
    }

    public String getDoubleValue86() {
        return doubleValue86;
    }

    public void setDoubleValue86(String doubleValue86) {
        this.doubleValue86 = doubleValue86;
    }

    public String getDoubleValue87() {
        return doubleValue87;
    }

    public void setDoubleValue87(String doubleValue87) {
        this.doubleValue87 = doubleValue87;
    }

    public String getDoubleValue88() {
        return doubleValue88;
    }

    public void setDoubleValue88(String doubleValue88) {
        this.doubleValue88 = doubleValue88;
    }

    public String getDoubleValue89() {
        return doubleValue89;
    }

    public void setDoubleValue89(String doubleValue89) {
        this.doubleValue89 = doubleValue89;
    }

    public String getDoubleValue90() {
        return doubleValue90;
    }

    public void setDoubleValue90(String doubleValue90) {
        this.doubleValue90 = doubleValue90;
    }

    public String getDoubleValue91() {
        return doubleValue91;
    }

    public void setDoubleValue91(String doubleValue91) {
        this.doubleValue91 = doubleValue91;
    }

    public String getDoubleValue92() {
        return doubleValue92;
    }

    public void setDoubleValue92(String doubleValue92) {
        this.doubleValue92 = doubleValue92;
    }

    public String getDoubleValue93() {
        return doubleValue93;
    }

    public void setDoubleValue93(String doubleValue93) {
        this.doubleValue93 = doubleValue93;
    }

    public String getDoubleValue94() {
        return doubleValue94;
    }

    public void setDoubleValue94(String doubleValue94) {
        this.doubleValue94 = doubleValue94;
    }

    public String getDoubleValue95() {
        return doubleValue95;
    }

    public void setDoubleValue95(String doubleValue95) {
        this.doubleValue95 = doubleValue95;
    }

    public String getDoubleValue96() {
        return doubleValue96;
    }

    public void setDoubleValue96(String doubleValue96) {
        this.doubleValue96 = doubleValue96;
    }

    public String getDoubleValue97() {
        return doubleValue97;
    }

    public void setDoubleValue97(String doubleValue97) {
        this.doubleValue97 = doubleValue97;
    }

    public String getDoubleValue98() {
        return doubleValue98;
    }

    public void setDoubleValue98(String doubleValue98) {
        this.doubleValue98 = doubleValue98;
    }

    public String getDoubleValue99() {
        return doubleValue99;
    }

    public void setDoubleValue99(String doubleValue99) {
        this.doubleValue99 = doubleValue99;
    }

    public String getDoubleValue100() {
        return doubleValue100;
    }

    public void setDoubleValue100(String doubleValue100) {
        this.doubleValue100 = doubleValue100;
    }

    public String getValueAsString(String dataType, String code, TimeZone timeZone, SimpleDateFormat... formattedVersionOfDate) {
        Object value = getValueByCode(dataType, code);
        if (Constants.DATA_TYPE_DATE.equals(dataType)) {
            SimpleDateFormat format = formattedVersionOfDate != null && formattedVersionOfDate.length > 0 ? formattedVersionOfDate[0] : new SimpleDateFormat(Constants.DATE_PATTERN);
            if (format == null) {
                format = new SimpleDateFormat(Constants.DATE_PATTERN);
            }
            if (value != null && value instanceof Date) {
                if (timeZone == null) {
                    timeZone = TimeZone.getDefault();
                }
                Date tempDate = (Date) ((Date) value).clone();
                tempDate.setMinutes(tempDate.getMinutes() + (timeZone.getRawOffset() / 60000));
                return format.format(tempDate);
            }
        }
        return value != null ? value.toString() : null;
    }

    public Object getValueByCode(String dataType, String code) {
        if (Constants.DATA_TYPE_DATE.equals(dataType)) {
            return getDateValue(code);
        } else if (Constants.DATA_TYPE_NUMBER.equals(dataType) || Constants.DATA_TYPE_FILE_UPLOAD.equals(dataType) || Constants.DATA_TYPE_PROFILE_IMAGE.equals(dataType)) {
            return getDoubleValue(code);
        } else if (Constants.DATA_TYPE_TEXT.equals(dataType)) {
            return getStringValue(code);
        }
        return null;
    }

    public String getStringValue(String code) {
        return (String) CustomFieldsUtils.getObjectValue(this, code);
    }

    public Double getDoubleValue(String code) {
        return (Double) CustomFieldsUtils.getObjectValue(this, code);
    }

    public Date getDateValue(String code) {
        return (Date) CustomFieldsUtils.getObjectValue(this, code);
    }
}
