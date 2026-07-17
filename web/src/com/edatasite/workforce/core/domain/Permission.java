package com.edatasite.workforce.core.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Oct 10, 2009
 * Time: 9:10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Permission {
    int READ = 1 << 1;   //2
    int EDIT = 1 << 2;   //4
    int CREATE = 1 << 3; //8
    int DELETE = 1 << 4; //16
}
