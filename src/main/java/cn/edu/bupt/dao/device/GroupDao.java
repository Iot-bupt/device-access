package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Dao;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/18.
 */
public interface GroupDao extends Dao<Group> {

    /**
     * Save or update group object
     *
     * @param group the group object
     * @return saved group object
     */
    Group save(Group group);

    /**
     * Find groups by tenant id and page link.
     *
     * @param tenantId the tenant id
     * @param pageLink the page link
     * @return the list of group objects
     */
    List<Group> findGroupsByTenantId(Integer tenantId, TextPageLink pageLink);

    /**
     * Find groups by customerId and page link.
     *
     * @param customerId the customerId
     * @param pageLink the page link
     * @return the list of group object
     */
    List<Group> findGroupsByCustomerId(Integer customerId, TextPageLink pageLink) ;

    /**
     * Find group by customerId and name.
     *
     * @param customerId the customerId
     * @param name the group name
     * @return found group
     */
    Optional<Group> findGroupByCustomerIdAndName(Integer customerId, String name);

}
