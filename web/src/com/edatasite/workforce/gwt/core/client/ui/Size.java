package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 10/24/11
 * Time: 5:32 PM
 */
public class Size implements IsSerializable {

    public static Size newInstance(int x, int y) {
        return new Size(x, y);
    }

    /**
     * The width.
     */
    public int width;

    /**
     * The height.
     */
    public int height;

    /**
     * Creates a new size instance.
     *
     * @param width  the width
     * @param height the height
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return "height: " + height + ", width: " + width;
    }

}
