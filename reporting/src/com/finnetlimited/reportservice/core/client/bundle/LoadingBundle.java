package com.finnetlimited.reportservice.core.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: ${Dilsh0d}
 * Date: 30-Mar-2010
 * Time: 14:35:32
 */
public interface LoadingBundle extends ClientBundle {

    LoadingBundle instance = GWT.create(LoadingBundle.class);

    @ClientBundle.Source("com/finnetlimited/reportservice/core/client/bundle/sun-loading.gif")
    ImageResource sunLoading();

    @ClientBundle.Source("com/finnetlimited/reportservice/core/client/bundle/big-sun.gif")
    ImageResource bigSunLoading();

    @ClientBundle.Source("com/finnetlimited/reportservice/core/client/bundle/snake-loaing.gif")
    ImageResource snakeLoading();

    @ClientBundle.Source("com/finnetlimited/reportservice/core/client/bundle/big-snake.gif")
    ImageResource bigSnakeLoading();

    @ClientBundle.Source("com/finnetlimited/reportservice/core/client/bundle/flower.gif")
    ImageResource flowerLoading();

    @ClientBundle.Source("com/finnetlimited/reportservice/core/client/bundle/msgInfo.png")
    ImageResource msgInfo();

    @Source("com/finnetlimited/reportservice/core/client/bundle/pdf.png")
    ImageResource pdf();

    @Source("com/finnetlimited/reportservice/core/client/bundle/excel.png")
    ImageResource csv();

    @Source("com/finnetlimited/reportservice/core/client/bundle/orderInfo.png")
    ImageResource orderInfo();

    @Source("com/finnetlimited/reportservice/core/client/bundle/tabular.jpg")
    ImageResource tabularImg();

    @Source("com/finnetlimited/reportservice/core/client/bundle/summaries.jpg")
    ImageResource summaryImg();

    @Source("com/finnetlimited/reportservice/core/client/bundle/summariesCol.jpg")
    ImageResource summariesColInfo();

}
