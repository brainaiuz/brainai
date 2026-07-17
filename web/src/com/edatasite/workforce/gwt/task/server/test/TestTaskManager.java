//package com.edatasite.workforce.gwt.task.server.test;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//
//import com.edatasite.workforce.core.domain.EdsTask;
//import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
//import com.google.gwt.user.server.rpc.security.DefaultUserImpl;
//import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
//
///**
// * Created by IntelliJ IDEA.
// * User: iskan
// * Date: Dec 27, 2007
// * Time: 6:20:41 PM
// * To change this template use File | Settings | File Templates.
// */
//
//public class TestTaskManager extends BaseManager<EdsTask> {
//
//  private static HashMap<String, List<EdsTask>> tasks = new HashMap<String, List<EdsTask>>();
//
//
//  public TestTaskManager() {
//    super(EdsTask.class);
//  }
//
//  static {
//    List<EdsTask> testTasks = new LinkedList<EdsTask>();
//    EdsTask task = new EdsTask();
//    task.setObjectID(1);
//    task.setName("TASK1");
//    testTasks.add(task);
//    task = new EdsTask();
//    task.setObjectID(2);
//    task.setName("TASK2");
//    testTasks.add(task);
//    task = new EdsTask();
//    task.setObjectID(3);
//    task.setName("TASK3");
//    testTasks.add(task);
//    tasks.put("test", testTasks);
//  }
//
//  public List<EdsTask> list() {
//    DefaultUserImpl user = (DefaultUserImpl) getUser();
//    List<EdsTask> result = null;
//    if (user != null)
//      result = tasks.get(user.getUserName());
//    if (result != null)
//      return result;
//    return new LinkedList<EdsTask>();
//  }
//
//
//}
