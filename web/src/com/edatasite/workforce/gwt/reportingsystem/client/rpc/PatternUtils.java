package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Virus on 9/25/14.
 */
public class PatternUtils implements IsSerializable {
    private String pattern = "";
    private String generated = "";

    public PatternUtils() {
    }

    public PatternUtils(ReportRpc report) {
        StringBuffer stringBuffer = new StringBuffer();
        if (report.getFilterPattern() != null && !report.getFilterPattern().isEmpty()) {
            stringBuffer.append(report.getFilterPattern());
            pattern = report.getFilterPattern();
            generate();
        }
        ArrayList<Integer> temp = new ArrayList<Integer>(report.getSett());
        int i = 1;

        while (!temp.isEmpty()) {
            String block = generateBlock(temp, i++, report);
//            if (i > 2 && !pattern.isEmpty()) {
//                stringBuffer.insert(0, "(");
//            }
            stringBuffer.append(block);
        }
        if (pattern != null) {
            pattern = stringBuffer.toString().trim();
            generate();
            clear(report.getValues().size());
            stringBuffer = new StringBuffer(pattern);
        }
        pattern = stringBuffer.toString();
        for (i = 0; i < report.getBoolType().size(); i++) {
            setType(i + 1, report.getBoolTypeAt(i));
        }
        reloadPattern();
//        report.setFilterPattern(pattern);
    }

    public String generate() {
        generated = "";
        for (String item : pattern.split(" ")) {
            String d = item.replace("(", "").replace(")", "");
            if (d.matches("^[0-9]+$")) {
                Integer index = Integer.valueOf(d);
                generated += " " + item.replace("" + index, "$(" + index + ")");
            } else {
                generated += " " + item;
            }
        }
        return generated;
    }

    private Integer lastIndex() {
        String[] items = pattern.split(" ");
        if (items.length > 0) {
            String lastIndex = items[items.length - 1].replaceAll("(\\(|\\))", "");
            return lastIndex.matches("^[0-9]+$") ? Integer.valueOf(lastIndex) : null;
        }
        return null;
    }

    private boolean hasFilter(Integer index) {
        String regex = "$(" + index + ")";
        return generated.contains(regex);
    }

    private void clear(Integer size) {
        String regex = "";
        for (Integer i = size + 1; i < 101; i++) {
            regex = "(" + ("\\$\\(" + i + "\\)\\s*(or|and)")
                    + "|" + ("(or|and)\\s*\\$\\(" + i + "\\)")
                    + "|" + "\\(\\s*\\$\\(" + i + "\\)\\s*\\)"
                    + "|" + "\\(\\s*\\(\\s*\\$\\(" + i + "\\)\\s*\\)\\s*\\)"
                    + ")";
            generated = generated.replaceAll(regex, "");
            regex = "(and|or)\\s*(and|or)";
            generated = generated.replaceAll(regex, "$1");
        }
        regex = "\\s*(and|or)\\s*$";
        generated = generated.replaceAll(regex, "").trim();
        reloadPattern();
    }

    public void reloadPattern() {
        String regex;
        pattern = generated;
        for (int i = 1; i < 101; i++) {
            regex = "\\$\\(" + i + "\\)";
            pattern = pattern.replaceAll(regex, "" + i);
        }
    }

    private String generateBlock(ArrayList<Integer> temp, Integer h, ReportRpc report) {
        ArrayList<String> boolTypeList = report.getBoolType();
        ArrayList<Integer> brackets = report.getSett();
        StringBuilder stringBuffer = new StringBuilder();
        Integer min = Collections.min(temp);
        String boolType = "";
        boolean changed = false;
        for (int i = 0; i < brackets.size(); i++) {
            while (brackets.size() > i && !min.equals(brackets.get(i))) {
                i++;
            }
            if (boolTypeList.size() <= i) {
                break;
            }
            int index = i + 1;
            if (hasFilter(index)) {
                continue;
            }

            Integer lastIndex = lastIndex();
            if (lastIndex != null && lastIndex > 0) {
                if (boolTypeList.size() > lastIndex - 1) {
                    boolType = boolTypeList.get(i - 1);
                    stringBuffer.append(" " + boolType.toLowerCase() + " " + index);
                    boolType = "";
                }
            } else {
                if (stringBuffer.toString().isEmpty()) {
                    String bracket = h > 0 ? "(" : "";
                    stringBuffer.append(bracket + index);
                    boolType = boolTypeList.get(i);
                } else {
                    stringBuffer.append(" " + boolType.toLowerCase() + " " + index);
                    boolType = boolTypeList.get(i - 1);
                }
            }
            changed = true;
        }

        String bracket = changed && h > 0 && stringBuffer.toString().split("\\(").length > stringBuffer.toString().split("\\)").length ? ") " : " ";
        stringBuffer.append(bracket);
        if (!min.equals(Collections.max(temp))) {
            stringBuffer.append(boolType.toLowerCase() + " ");
        }
        temp.removeIf(min::equals);
        return stringBuffer.toString();
    }

    public String getPattern() {
        return pattern;
    }

    public void setType(int index, String boolType) {
        if (boolType == null || boolType.isEmpty()) {
            return;
        }
        String regex = "(\\$\\(" + index + "\\)*)\\s*(and|or)";
        generated = generated.replace(regex, "$1 " + boolType.toLowerCase());
    }

    public String getGenerated() {
        return generated;
    }

    public static String generate(String pattern) {
        StringBuilder generated = new StringBuilder();
        for (String item : pattern.split(" ")) {
            String d = item.replace("(", "").replace(")", "");
            if (d.matches("^[0-9]+$")) {
                Integer index = Integer.valueOf(d);
                generated.append(" ").append(item.replace("" + index, "$(" + index + ")"));
            } else {
                generated.append(" ").append(item);
            }
        }
        return generated.toString().trim();
    }
}
