package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import java.util.HashMap;
import java.util.stream.Stream;

public enum VatReturnBox {
    ae_box_1a, ae_box_1b, ae_box_1c, ae_box_1d, ae_box_1e, ae_box_1f, ae_box_1g,
    ae_box_2, ae_box_3, ae_box_4, ae_box_5, ae_box_6, ae_box_7, ae_box_8, ae_box_9,
    ae_box_10, ae_box_11, ae_box_12, ae_box_13, ae_box_14, ae_box_15,

    sa_box_1, sa_box_2, sa_box_3, sa_box_4, sa_box_5, sa_box_6, sa_box_7,
    sa_box_8, sa_box_9, sa_box_10, sa_box_11,

    //UK
    BOX_1, BOX_2, BOX_3, BOX_4, BOX_5, BOX_6, BOX_7, BOX_8, BOX_9;

    static HashMap<String, VatReturnBox> map;

    static {
        map = new HashMap<>();
        Stream.of(values()).forEach(box -> map.put(box.name(), box));
    }

    public static VatReturnBox getBoxByString(String boxNumber) {
        return map.get(boxNumber);
    }
}
