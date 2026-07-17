//package com.edatasite.workforce.gwt.core.server.db.impl.network;
//
//import com.edatasite.workforce.core.domain.EdsUser;
//import com.edatasite.workforce.core.domain.network.EdsNetworkNews;
//import com.edatasite.workforce.core.domain.network.EdsNetworkNewsView;
//import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
//import com.edatasite.workforce.gwt.core.server.db.network.NetworkNewsViewManager;
//
//import java.util.List;
//
///**
// * Created by IntelliJ IDEA.
// * User: Ruslan Muhammadov
// * Date: May 24, 2010
// * Time: 7:19:58 PM
// * To change this template use File | Settings | File Templates.
// */
//public class NetworkNewsViewManagerImpl extends BaseManager<EdsNetworkNewsView> implements NetworkNewsViewManager {
//
//    public NetworkNewsViewManagerImpl() {
//        super(EdsNetworkNewsView.class);
//    }
//
//    public EdsNetworkNewsView getRatedView(EdsUser user, EdsNetworkNews news) {
//        return (EdsNetworkNewsView) findSingle("from EdsNetworkNewsView nnv where nnv.viewer = ? and " +
//                "nnv.news = ? and nnv.commented <> true", user, news);
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<EdsNetworkNewsView> getNewsComments(EdsNetworkNews news) {
//        return find("from EdsNetworkNewsView nnv where nnv.news = ? and nnv.commented = true", news);
//    }
//}
