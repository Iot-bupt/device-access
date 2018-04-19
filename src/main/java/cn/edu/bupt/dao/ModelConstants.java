package cn.edu.bupt.dao;

import com.datastax.driver.core.utils.UUIDs;

import java.util.UUID;

/**
 * Created by CZX on 2018/3/23.
 */
public class ModelConstants {

    public static final UUID NULL_UUID = UUIDs.startOf(0);

    /**
     * Generic constants.
     */
    public static final String ID_PROPERTY = "id";
    public static final String USER_ID_PROPERTY = "user_id";
    public static final String TENANT_ID_PROPERTY = "tenant_id";
    public static final String CUSTOMER_ID_PROPERTY = "customer_id";
    public static final String ADDITIONAL_INFO_PROPERTY = "additional_info";
    public static final String DEVICE_ID_PROPERTY = "device_id";
    public static final String SEARCH_TEXT_PROPERTY = "search_text";
    /**
     * Cassandra contact constants.
     */
    public static final String ADDRESS_PROPERTY = "address";
    public static final String PHONE_PROPERTY = "phone";
    public static final String EMAIL_PROPERTY = "email";

    /**
     * Cassandra user constants.
     */
    public static final String USER_COLUMN_FAMILY_NAME = "user";
    public static final String USER_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String USER_CUSTOMER_ID_PROPERTY = CUSTOMER_ID_PROPERTY;
    public static final String USER_EMAIL_PROPERTY = "email";
    public static final String USER_AUTHORITY_PROPERTY = "authority";
    public static final String USER_NAME_PROPERTY = "name";
    public static final String USER_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

    public static final String USER_BY_EMAIL_COLUMN_FAMILY_NAME = "user_by_email";
//    public static final String USER_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "user_by_tenant_and_search_text";
//    public static final String USER_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "user_by_customer_and_search_text";

    /**
     * Cassandra tenant constants.
     */
    public static final String TENANT_COLUMN_FAMILY_NAME = "tenant";
    public static final String TENANT_NAME_PROPERTY = "name";
    public static final String TENANT_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

//    public static final String TENANT_BY_REGION_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "tenant_by_region_and_search_text";

    /**
     * Cassandra customer constants.
     */
    public static final String CUSTOMER_COLUMN_FAMILY_NAME = "customer";
    public static final String CUSTOMER_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String CUSTOMER_NAME_PROPERTY = "name";
    public static final String CUSTOMER_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

//    public static final String CUSTOMER_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "customer_by_tenant_and_search_text";
//    public static final String CUSTOMER_BY_TENANT_AND_TITLE_VIEW_NAME = "customer_by_tenant_and_title";

    /**
     * Cassandra device constants.
     */
    public static final String DEVICE_COLUMN_FAMILY_NAME = "device";
    public static final String DEVICE_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String DEVICE_CUSTOMER_ID_PROPERTY = CUSTOMER_ID_PROPERTY;
    public static final String DEVICE_GROUP_PROPERTY = "group_id";
    public static final String DEVICE_NAME_PROPERTY = "name";
    public static final String DEVICE_PARENT_DEVICE_ID_PROPERTY = "parent_device_id";
    public static final String DEVICE_MANUFACTURE_PROPERTY = "manufacture";
    public static final String DEVICE_MODEL_PROPERTY = "model";
    public static final String DEVICE_DEVICE_TYPE_PROPERTY = "device_type";
    public static final String DEVICE_STATUS_PROPERTY = "status";
    public static final String DEVICE_LOCATION_PROPERTY = "location";
    public static final String DEVICE_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

    public static final String DEVICE_BY_TENANT_AND_PARENT_DEVICE_ID_COLUMN_FAMILY_NAME = "device_by_tenant_and_parent_device_id";
    public static final String DEVICE_BY_GROUP_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_group_and_search_text";
    public static final String DEVICE_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_tenant_and_search_text";
    public static final String DEVICE_BY_TENANT_BY_TYPE_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_tenant_by_type_and_search_text";
    public static final String DEVICE_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_customer_and_search_text";
    public static final String DEVICE_BY_CUSTOMER_BY_TYPE_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_customer_by_type_and_search_text";
    public static final String DEVICE_BY_TENANT_AND_NAME_VIEW_NAME = "device_by_tenant_and_name";
    public static final String DEVICE_TYPES_BY_TENANT_VIEW_NAME = "device_types_by_tenant";

    /**
     * Cassandra device group constants.
     */
    public static final String GROUP_COLUMN_FAMILY_NAME = "group";
    public static final String GROUP_NAME_PROPERTY = "name";
    public static final String GROUP_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String GROUP_CUSTOMER_ID_PROPERTY = CUSTOMER_ID_PROPERTY;

    public static final String GROUP_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "group_by_tenant_and_search_text";
    public static final String GROUP_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "group_by_customer_and_search_text";
    public static final String GROUP_BY_CUSTOMER_AND_NAME_COLUMN_FAMILY_NAME = "group_by_customer_and_name";

    /**
     * Cassandra deviceByGroupId constants.
     */
    public static final String DEVICE_BY_GROUP_ID_COLUMN_FAMILY_NAME = "device_by_group_id";
    public static final String DEVICE_BY_GROUP_ID_DEVICE_ID_PROPERTY = "device_id";
    public static final String DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY = "group_id";

    public static final String GROUP_BY_DEVICE_ID_COLUMN_FAMILY_NAME = "group_by_device_id";

    /**
     * Cassandra device_credentials constants.
     */
    public static final String DEVICE_CREDENTIALS_COLUMN_FAMILY_NAME = "device_credentials";
    public static final String DEVICE_CREDENTIALS_DEVICE_ID_PROPERTY = DEVICE_ID_PROPERTY;
    public static final String DEVICE_CREDENTIALS_TOKEN_PROPERTY = "device_token";

    public static final String DEVICE_CREDENTIALS_BY_DEVICE_COLUMN_FAMILY_NAME = "device_credentials_by_device";
    public static final String DEVICE_CREDENTIALS_BY_DEVICE_TOKEN_COLUMN_FAMILY_NAME = "device_credentials_by_device_token";

}