package com.edatasite.workforce.core.tools;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 16, 2010
 * Time: 7:13:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmtpScanner {
    static final Logger logger = LoggerFactory.getLogger(SmtpScanner.class);
    static final boolean isDebugEnabled = false;//logger.isDebugEnabled();
    final int maxLenToScan = 8192 * 4; // scan up to 32k

    private static final HashMap<String, String> RFC1893_STATUS_CODE = new HashMap<>();
    private static final HashMap<String, String> RFC1893_STATUS_DESC = new HashMap<>();
    private static final HashMap<String, String> RFC2821_STATUS_CODE = new HashMap<>();
    private static final HashMap<String, String> RFC2821_STATUS_DESC = new HashMap<>();
    private static final HashMap<String, String> RFC2821_STATUS_MATCHINGTEXT = new HashMap<>();

    public enum BOUNCETYPE {
        GENERIC
    }

    // default bounce type - not a bounce.

    public enum BOUNCE_TYPES {
        HARD_BOUNCE, // Hard bounce - suspend,notify,close
        SOFT_BOUNCE, // Soft bounce - bounce++,close
        MAILBOX_FULL, // mailbox full, can be treated as Soft Bounce
        CC_USER, // Mail received as a Carbon Copy
        MDN_RECEIPT, // MDN - read receipt
    }

    private static SmtpScanner smtpCodeScan = null;

    private static final String
            LETTER_S = "s",
            LETTER_H = "h",
            LETTER_F = "f",
            LETTER_K = "k",
            LETTER_U = "u";

    /**
     * default constructor
     */
    private SmtpScanner() throws IOException {
        loadRfc1893StatusCode();
        loadRfc2821StatusCode();
    }

    public static SmtpScanner getInstance() throws IOException {
        if (smtpCodeScan == null) {
            smtpCodeScan = new SmtpScanner();
        }
        return smtpCodeScan;
    }

    /**
     * returns a message id, null if not found
     *
     * @return message id, null if not found
     */
    String scanBody(String body) {
        String bounceType = scanBody(body, 1);
        if (bounceType == null) {
            bounceType = scanBody(body, 2);
        }
        return bounceType;
    }

    private static Pattern pattern1 = Pattern.compile("\\s([245]\\.\\d{1,3}\\.\\d{1,3})\\s", Pattern.DOTALL);
    private static Pattern pattern2 = Pattern.compile("\\s([245]\\d\\d)\\s", Pattern.DOTALL);

    /**
     * <ul>
     * <li> first pass: check if it contains a RFC1893 code. RFC1893 codes are
     * from 5 to 9 bytes long (x.x.x -> x.xxx.xxx) and start with 2.x.x or 4.x.x
     * or 5.x.x
     * <li> second pass: check if it contains a 3 digit numeric number: 2xx, 4xx
     * or 5xx.
     * </ul>
     *
     * @param pass -
     *             1) first pass: look for RFC1893 token (x.x.x).
     *             2) second pass: look for RFC2821 token (xxx), must also match reply text.
     * @return bounce type or null if no RFC code is found.
     */
    private String scanBody(String body, int pass) {
        if (isDebugEnabled)
            logger.debug("Entering the examineBody method, pass " + pass);
        if (StringUtil.isEmpty(body)) { // sanity check
            return null;
        }
        BOUNCE_TYPES bounceType = null;
        if (pass == 1) {
            Matcher m = pattern1.matcher(StringUtil.cut(body, maxLenToScan));
            if (m.find()) { // only one time
                String token = m.group(m.groupCount());
                //logger.info("examineBody(): RFC1893 token found: " + token);
                if ((bounceType = searchRfc1893CodeTable(token)) != null) {
                    return bounceType.toString();
                } else if (token.startsWith("5.")) { // 5.x.x
                    return BOUNCE_TYPES.HARD_BOUNCE.toString();
                } else if (token.startsWith("4.")) { // 4.x.x
                    return BOUNCE_TYPES.SOFT_BOUNCE.toString();
                } else if (token.startsWith("2.")) { // 2.x.x
                    // 2.x.x = OK message returned, MDN receipt.
                    return BOUNCE_TYPES.MDN_RECEIPT.toString();
                }
            }
        } else if (pass == 2) {
            Matcher m = pattern2.matcher(StringUtil.cut(body, maxLenToScan));
            int end = 0;
            int count = 0;
            while (m.find(end) && count++ < 2) { // repeat two times
                String token = m.group(m.groupCount());
                end = m.end(m.groupCount());
                //logger.info("examineBody(): Numeric token found: " + token);
                if ((bounceType = searchRfc2821CodeTable(token)) != null) {
                    //return bounceType;
                    return matchRfcText(bounceType, token, body, end);
                }
                if (token.startsWith("5")) {
                    // 5xx = permanent failure, re-send will fail
                    String r = matchRfcText(BOUNCE_TYPES.HARD_BOUNCE, token, body, end);
                    if (r != null) return r;
                    // else look for the second token
                } else if (token.equals("422")) {
                    // 422 = mailbox full, re-send may be successful
                    return matchRfcText(BOUNCE_TYPES.MAILBOX_FULL, token, body, end);
                } else if (token.startsWith("4")) {
                    // 4xx = persistent transient failure, re-send may be successful
                    String r = matchRfcText(BOUNCE_TYPES.SOFT_BOUNCE, token, body, end);
                    if (r != null) return r;
                    // else look for the second token
                } else if (token.startsWith("2")) {
                    // 2xx = OK message returned.
                }
            }
        }
        return null;
    }

    /**
     * For RFC 2821, to further match reply text to prevent false positives.
     *
     * @param bounceType -
     *                   Bounce Type
     * @param code       -
     *                   RFC2821 code
     * @param idx        -
     *                   where the RFC2821 code located in the array
     * @return bounce type, or null if failed to match reply text.
     */
    private String matchRfcText(BOUNCE_TYPES bounceType, String code, String body, int idx) {
        String matchingText = RFC2821_STATUS_MATCHINGTEXT.get(code);
        if (matchingText == null) {
            if (code.startsWith("4")) {
                matchingText = RFC2821_STATUS_MATCHINGTEXT.get("4xx");
            } else if (code.startsWith("5")) {
                matchingText = RFC2821_STATUS_MATCHINGTEXT.get("5xx");
            }
            if (matchingText == null) { // just for safety
                return null;
            }
        }
        // RFC reply text - the first 120 characters after the RFC code
        String rfcText = StringUtil.cut(body.substring(idx), 120);
        try {
            Pattern p = Pattern.compile(matchingText, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(rfcText);
            if (m.find()) {
                //logger.info("Match Succeeded: [" + rfcText + "] matched [" + matchingText + "]");
                return bounceType.toString();
            } else {
                logger.info("Match Failed: [" + rfcText + "] did not match [" + matchingText + "]");
            }
        }
        catch (PatternSyntaxException e) {
            logger.error("PatternSyntaxException caught", e);
        }
        return null;
    }

    /**
     * search smtp code table by RFC1893 token.
     *
     * @param token DSN status token, for example: 5.0.0
     * @return message id related to the token
     */
    private BOUNCE_TYPES searchRfc1893CodeTable(String token) {
        // search rfc1893 hash table - x.x.x
        BOUNCE_TYPES bounceType = searchRfcCodeTable(token, RFC1893_STATUS_CODE);
        // search rfc1893 hash table - .x.x
        if (bounceType == null) {
            bounceType = searchRfcCodeTable(token.substring(1), RFC1893_STATUS_CODE);
        }
        return bounceType;
    }

    /**
     * search smtp code table by RFC token.
     *
     * @param token -
     *              DSN status token, for example: 5.0.0, or 500 depending on the
     *              map used
     * @param map   -
     *              either RFC1893_STATUS_CODE or RFC2821_STATUS_CODE
     * @return message id of the token
     */
    private BOUNCE_TYPES searchRfcCodeTable(String token, HashMap<String, String> map) {
        String type = map.get(token);

        if (type != null) { // found RFC status code
            //logger.info("searchRfcCodeTable(): A match is found for type: " + type);
            switch (type) {
                case LETTER_H -> {
                    return BOUNCE_TYPES.HARD_BOUNCE;
                }
                case LETTER_S -> {
                    return BOUNCE_TYPES.SOFT_BOUNCE;
                }
                case LETTER_F -> {
                    return BOUNCE_TYPES.MAILBOX_FULL;
                }
                case LETTER_K -> {
                    return BOUNCE_TYPES.MDN_RECEIPT;
                }
                case LETTER_U -> {
                    if (token.startsWith("4")) {
                        return BOUNCE_TYPES.SOFT_BOUNCE;
                    } else if (token.startsWith("5")) {
                        return BOUNCE_TYPES.HARD_BOUNCE;
                    }
                }
            }
        }
        return null;
    }

    /**
     * search smtp code table by RFC token.
     *
     * @param token -
     *              RFC2821 token, for example: 500
     * @return message id of the token
     */
    private BOUNCE_TYPES searchRfc2821CodeTable(String token) {
        return searchRfcCodeTable(token, RFC2821_STATUS_CODE);
    }

    /**
     * load the rfc1893 code table, from Rfc1893.properties file, into memory.
     *
     * @throws IOException
     */
    private void loadRfc1893StatusCode() throws IOException {
        ClassLoader loader = this.getClass().getClassLoader();
        try {
            // read in RFC1893 status code file and store it in two property objects
            InputStream is = loader.getResourceAsStream("Rfc1893.properties");
            BufferedReader fr = new BufferedReader(new InputStreamReader(is));
            String inStr = null, code = null;
            while ((inStr = fr.readLine()) != null) {
                if (!inStr.startsWith("#")) {
                    if (isDebugEnabled)
                        logger.debug("loadRfc1893StatusCode(): " + inStr);
                    StringTokenizer st = new StringTokenizer(inStr, "^\r\n");
                    if (st.countTokens() == 3) {
                        code = st.nextToken();
                        RFC1893_STATUS_CODE.put(code, st.nextToken());
                        RFC1893_STATUS_DESC.put(code, st.nextToken());
                    } else if (st.countTokens() == 0) {
                        // ignore
                    } else {
                        logger.error("loadRfc1893StatusCode(): Wrong record format: " + inStr);
                    }
                }
            }
            fr.close();
        }
        catch (FileNotFoundException ex) {
            logger.error("file Rfc1893.properties does not exist", ex);
            throw ex;
        }
        catch (IOException ex) {
            logger.error("IOException caught during loading statcode.conf", ex);
            throw ex;
        }
    }

    /**
     * load the rfc2821 code table, from Rfc2821.properties file, into memory.
     *
     * @throws IOException
     */
    private void loadRfc2821StatusCode() throws IOException {
        ClassLoader loader = this.getClass().getClassLoader();
        try {
            // read in RFC2821 status code file and store it in two property objects
            InputStream is = loader.getResourceAsStream("Rfc2821.properties");
            BufferedReader fr = new BufferedReader(new InputStreamReader(is));
            String inStr = null, code = null;
            while ((inStr = fr.readLine()) != null) {
                if (!inStr.startsWith("#")) {
                    if (isDebugEnabled)
                        logger.debug("loadRfc2821StatusCode(): " + inStr);
                    StringTokenizer st = new StringTokenizer(inStr, "^\r\n");
                    if (st.countTokens() == 3) {
                        code = st.nextToken(); // 1st token = RFC code
                        RFC2821_STATUS_CODE.put(code, st.nextToken()); // 2nd token = type
                        String desc = st.nextToken(); // 3rd token = description
                        RFC2821_STATUS_DESC.put(code, desc);
                        // extract regular expression to be further matched
                        String matchingRegex = getMatchingRegex(desc);
                        if (matchingRegex != null) {
                            RFC2821_STATUS_MATCHINGTEXT.put(code, matchingRegex);
                        }
                    } else if (st.countTokens() == 0) {
                        // ignore
                    } else {
                        logger.error("loadRfc2821StatusCode(): Wrong record format: " + inStr);
                    }
                }
            }
            fr.close();
        }
        catch (FileNotFoundException ex) {
            logger.error("file Rfc2821.properties does not exist", ex);
            throw ex;
        }
        catch (IOException ex) {
            logger.error("IOException caught during loading statcode.conf", ex);
            throw ex;
        }
    }

    private String getMatchingRegex(String desc) throws IOException {
        int left = desc.indexOf("{");
        if (left < 0) {
            return null;
        }
        Stack<Integer> stack = new Stack<>();
        stack.push(left);
        int nextPos = left;
        while (stack.size() > 0) {
            int leftPos = desc.indexOf("{", nextPos + 1);
            int rightPos = desc.indexOf("}", nextPos + 1);
            if (leftPos > rightPos) {
                if (rightPos > 0) {
                    stack.pop();
                    nextPos = rightPos;
                }
            } else if (leftPos < rightPos) {
                if (leftPos > 0) {
                    nextPos = leftPos;
                    stack.push(leftPos);
                } else if (rightPos > 0) {
                    stack.pop();
                    nextPos = rightPos;
                }
            } else {
                break;
            }
        }
        if (nextPos > left) {
            if (stack.size() == 0) {
                return desc.substring(left + 1, nextPos);
            } else {
                logger.error("getMatchingRegex() - missing close curly brace: " + desc);
                throw new IOException("Missing close curly brace: " + desc);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            SmtpScanner scan = SmtpScanner.getInstance();
            String bounceType = scan.scanBody("aaaaab\n5.0.0\nefg ");
            System.out.println("BounceType: " + bounceType);
            bounceType = scan.scanBody("aaa 201 aab\n422\naccount is full ");
            System.out.println("BounceType: " + bounceType);
            bounceType = scan.scanBody("aaaaab\n400\ntemporary failure ");
            System.out.println("BounceType: " + bounceType);
            //System.out.println(scan.getMatchingRegex("{(?:mailbox|account).{0,180}(?:storage|full|limit|quota)}"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
