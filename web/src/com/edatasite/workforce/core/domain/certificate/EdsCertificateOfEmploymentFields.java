package com.edatasite.workforce.core.domain.certificate;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Khasan on 06.10.14.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "certificateofemploymentfields")
public class EdsCertificateOfEmploymentFields extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String textBox1;

    private String textBox2;

    private String textBox3;

    private String textBox4;

    private String textBox5;

    private String textBox6;

    private String textBox7;

    private String textBox8;

    private String textBox9;

    private String textBox10;

    private String textBox11;

    private String textBox12;

    private String textBox13;

    private String textBox14;

    private String textBox15;

    private String textBox16;

    private String textBox17;

    private String textBox18;

    @Column(name = "textarea1")
    @Type(type = "text")
    private String textArea1;

    @Column(name = "textarea2")
    @Type(type = "text")
    private String textArea2;

    @Column(name = "textarea3")
    @Type(type = "text")
    private String textArea3;

    @Column(name = "textarea4")
    @Type(type = "text")
    private String textArea4;

    @Column(name = "textarea5")
    @Type(type = "text")
    private String textArea5;

    @Column(name = "textarea6")
    @Type(type = "text")
    private String textArea6;

    @Column(name = "textarea7")
    @Type(type = "text")
    private String textArea7;

    @Column(name = "textarea8")
    @Type(type = "text")
    private String textArea8;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTextBox1() {
        return textBox1;
    }

    public void setTextBox1(String textBox1) {
        this.textBox1 = textBox1;
    }

    public String getTextBox2() {
        return textBox2;
    }

    public void setTextBox2(String textBox2) {
        this.textBox2 = textBox2;
    }

    public String getTextBox3() {
        return textBox3;
    }

    public void setTextBox3(String textBox3) {
        this.textBox3 = textBox3;
    }

    public String getTextBox4() {
        return textBox4;
    }

    public void setTextBox4(String textBox4) {
        this.textBox4 = textBox4;
    }

    public String getTextArea1() {
        return textArea1;
    }

    public void setTextArea1(String textArea1) {
        this.textArea1 = textArea1;
    }

    public String getTextArea2() {
        return textArea2;
    }

    public void setTextArea2(String textArea2) {
        this.textArea2 = textArea2;
    }

    public String getTextArea3() {
        return textArea3;
    }

    public void setTextArea3(String textArea3) {
        this.textArea3 = textArea3;
    }

    public String getTextArea4() {
        return textArea4;
    }

    public void setTextArea4(String textArea4) {
        this.textArea4 = textArea4;
    }

    public String getTextArea5() {
        return textArea5;
    }

    public void setTextArea5(String textArea5) {
        this.textArea5 = textArea5;
    }

    public String getTextArea6() {
        return textArea6;
    }

    public void setTextArea6(String textArea6) {
        this.textArea6 = textArea6;
    }

    public String getTextArea7() {
        return textArea7;
    }

    public void setTextArea7(String textArea7) {
        this.textArea7 = textArea7;
    }

    public String getTextArea8() {
        return textArea8;
    }

    public void setTextArea8(String textArea8) {
        this.textArea8 = textArea8;
    }

    public String getTextBox5() {
        return textBox5;
    }

    public void setTextBox5(String textBox5) {
        this.textBox5 = textBox5;
    }

    public String getTextBox6() {
        return textBox6;
    }

    public void setTextBox6(String textBox6) {
        this.textBox6 = textBox6;
    }

    public String getTextBox7() {
        return textBox7;
    }

    public void setTextBox7(String textBox7) {
        this.textBox7 = textBox7;
    }

    public String getTextBox8() {
        return textBox8;
    }

    public void setTextBox8(String textBox8) {
        this.textBox8 = textBox8;
    }

    public String getTextBox9() {
        return textBox9;
    }

    public void setTextBox9(String textBox9) {
        this.textBox9 = textBox9;
    }

    public String getTextBox10() {
        return textBox10;
    }

    public void setTextBox10(String textBox10) {
        this.textBox10 = textBox10;
    }

    public String getTextBox11() {
        return textBox11;
    }

    public void setTextBox11(String textBox11) {
        this.textBox11 = textBox11;
    }

    public String getTextBox12() {
        return textBox12;
    }

    public void setTextBox12(String textBox12) {
        this.textBox12 = textBox12;
    }

    public String getTextBox13() {
        return textBox13;
    }

    public void setTextBox13(String textBox13) {
        this.textBox13 = textBox13;
    }

    public String getTextBox14() {
        return textBox14;
    }

    public void setTextBox14(String textBox14) {
        this.textBox14 = textBox14;
    }

    public String getTextBox15() {
        return textBox15;
    }

    public void setTextBox15(String textBox15) {
        this.textBox15 = textBox15;
    }

    public String getTextBox16() {
        return textBox16;
    }

    public void setTextBox16(String textBox16) {
        this.textBox16 = textBox16;
    }

    public String getTextBox17() {
        return textBox17;
    }

    public void setTextBox17(String textBox17) {
        this.textBox17 = textBox17;
    }

    public String getTextBox18() {
        return textBox18;
    }

    public void setTextBox18(String textBox18) {
        this.textBox18 = textBox18;
    }
}
