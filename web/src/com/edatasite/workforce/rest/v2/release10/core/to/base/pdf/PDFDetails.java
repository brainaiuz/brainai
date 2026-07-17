package com.edatasite.workforce.rest.v2.release10.core.to.base.pdf;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created by Anvar Akramov on 3/10/18.
 */
public class PDFDetails extends ResponseData {

    private Integer id;
    private Integer company_id;
    private boolean default_template;
    private boolean browser_version;
    private Integer pdf_reference_id;
    private String template_name;
    private String image_name;
    private String content;
    private String content_froala;
    private String font_file_name;
    private String num_format;
    private String num_format_dec_separator;
    private String num_format_group_separator;
    private String ex_num_format;
    private String ex_num_format_dec_separator;
    private String ex_num_format_group_separator;
//    private BigDecimal price;
    /*private SelectItem[] references;
    private SelectItem[] systemTemplates;
    private SelectItem[] fonts;*/

    public PDFDetails() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public boolean isDefault_template() {
        return default_template;
    }

    public void setDefault_template(boolean default_template) {
        this.default_template = default_template;
    }

    public boolean isBrowser_version() {
        return browser_version;
    }

    public void setBrowser_version(boolean browser_version) {
        this.browser_version = browser_version;
    }

    public Integer getPdf_reference_id() {
        return pdf_reference_id;
    }

    public void setPdf_reference_id(Integer pdf_rreference_id) {
        this.pdf_reference_id = pdf_rreference_id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_froala() {
        return content_froala;
    }

    public void setContent_froala(String content_froala) {
        this.content_froala = content_froala;
    }

    public String getFont_file_name() {
        return font_file_name;
    }

    public void setFont_file_name(String font_file_name) {
        this.font_file_name = font_file_name;
    }

    public String getNum_format() {
        return num_format;
    }

    public void setNum_format(String num_format) {
        this.num_format = num_format;
    }

    public String getNum_format_dec_separator() {
        return num_format_dec_separator;
    }

    public void setNum_format_dec_separator(String num_format_dec_separator) {
        this.num_format_dec_separator = num_format_dec_separator;
    }

    public String getNum_format_group_separator() {
        return num_format_group_separator;
    }

    public void setNum_format_group_separator(String num_format_group_separator) {
        this.num_format_group_separator = num_format_group_separator;
    }

    public String getEx_num_format() {
        return ex_num_format;
    }

    public void setEx_num_format(String ex_num_format) {
        this.ex_num_format = ex_num_format;
    }

    public String getEx_num_format_dec_separator() {
        return ex_num_format_dec_separator;
    }

    public void setEx_num_format_dec_separator(String ex_num_format_dec_separator) {
        this.ex_num_format_dec_separator = ex_num_format_dec_separator;
    }

    public String getEx_num_format_group_separator() {
        return ex_num_format_group_separator;
    }

    public void setEx_num_format_group_separator(String ex_num_format_group_separator) {
        this.ex_num_format_group_separator = ex_num_format_group_separator;
    }

    /*public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }*/
}
