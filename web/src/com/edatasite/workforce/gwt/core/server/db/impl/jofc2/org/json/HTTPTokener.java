package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.org.json;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 15:02:21
 * To change this template use File | Settings | File Templates.
 */
public class HTTPTokener extends JSONTokener {

    /**
     * Construct an XMLTokener from a string.
     * @param s A source string.
     */
    public HTTPTokener(String s) {
        super(s);
    }


    /**
     * Get the next token or string. This is used in parsing HTTP headers.
     * @throws JSONException
     * @return A String.
     */
    public String nextToken() throws JSONException {
        char c;
        char q;
        StringBuilder sb = new StringBuilder();
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == '"' || c == '\'') {
            q = c;
            for (;;) {
                c = next();
                if (c < ' ') {
                    throw syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return sb.toString();
                }
                sb.append(c);
            }
        }
        for (;;) {
            if (c == 0 || Character.isWhitespace(c)) {
                return sb.toString();
            }
            sb.append(c);
            c = next();
        }
    }
}
