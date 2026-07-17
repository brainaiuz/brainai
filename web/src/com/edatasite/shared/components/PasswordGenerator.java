package com.edatasite.shared.components;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Ula
 * Date: Jun 20, 2007
 * Time: 12:22:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordGenerator {
    public static final char[] LATIN_LOWERCASE =
            {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static final char[] DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private char[] symbols;
    private Random rnd;
    private int length;

    public PasswordGenerator(int length, char[]... characters) {
        setLength(length);
        rnd = new Random();
        setSymbols(characters);
    }

    public PasswordGenerator(int length) {
        this(length, LATIN_LOWERCASE, DIGITS);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length mast be > 0");
        }
        this.length = length;
    }

    public char[] getSymbols() {
        return symbols;
    }

    public void setSymbols(char[]... characters) {
        int i = 0;
        for (char[] ch : characters) {
            i += ch.length;
        }
        if (i <= 0) {
            throw new IllegalArgumentException("Empty character set");
        }
        symbols = new char[i];
        i = 0;
        for (char[] ch : characters) {
            System.arraycopy(ch, 0, symbols, i, ch.length);
            i += ch.length;
        }
    }

    public String generateAsString() {
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            buffer.append(symbols[rnd.nextInt(symbols.length)]);
        }
        return buffer.toString();
    }
}

