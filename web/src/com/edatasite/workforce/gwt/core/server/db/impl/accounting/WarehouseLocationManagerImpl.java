//package com.edatasite.workforce.gwt.core.server.db.impl.accounting;
//
//import com.edatasite.workforce.core.domain.accounting.EdsWarehouseLocation;
//import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseLocationManager;
//import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by IntelliJ IDEA.
// * User: Anvar Akramov
// * Date: Apr 14, 2010
// * Time: 7:22:15 PM
// * To change this template use File | Settings | File Templates.
// */
//public class WarehouseLocationManagerImpl extends BaseManager<EdsWarehouseLocation> implements WarehouseLocationManager {
//
//    public WarehouseLocationManagerImpl() {
//        super(EdsWarehouseLocation.class);
//    }
//
//    public List<EdsWarehouseLocation> getLocationsByWarehouseID(Integer warehouseID) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("warehouseID", warehouseID);
//        return findByNamedParams("FROM EdsWarehouseLocation l WHERE l.warehouse.objectID = :warehouseID ", map);
//    }
//
//    public List<EdsWarehouseLocation> getLocationsByCompanyID(Integer companyID) {
//        return find("FROM EdsWarehouseLocation l ");
//    }
//
//    @Override
//    public EdsWarehouseLocation getLocationForImport(String name, Integer warehouseID) {
//        EdsWarehouseLocation location = (EdsWarehouseLocation)findSingle("select l FROM EdsWarehouseLocation l " +
//                " WHERE l.name = ? and l.warehouse.objectID = ?", name, warehouseID);
//        if(location==null){
//            location = (EdsWarehouseLocation)findSingle("select l FROM EdsWarehouseLocation l " +
//                    "WHERE l.warehouse.objectID = ? ORDER BY id desc limit 1",warehouseID);
//        }
//        return location;
//    }
//
//    @Override
//    public void deleteWarehouseLocations(Integer warehouseID) {
//        update("delete from EdsWarehouseLocation where warehouse.objectID = ?", warehouseID);
//    }
//}
