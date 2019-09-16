package com.core.constant;

public class Global {

    public static final String LOG_PARA_URL = "url";
    public static final String LOG_PARA_PARAS = "paras";
    public static final String LOG_PARA_RESPONSE = "response";
    public static final String DATA_SOURCE_NAME = "defaultDataSource";


    //删除无效服务接口，参数：服务ID
    public static final String deregister = "/v1/agent/service/deregister/";
    //删除无效节点接口，参数：节点ID
    public static final String force_leave = "/v1/agent/force-leave/";
    //获取所有服务接口，参数：无
    public static final String critical = "/v1/agent/service/critical/";

}
