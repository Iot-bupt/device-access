package cn.edu.bupt.service;

import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Group;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;

/**
 * Created by CZX on 2018/4/19.
 */
public interface GroupService {

    Group saveGroup(Group group);

    TextPageData<Group> findGroupsByTenantId(Integer tenantId, TextPageLink pageLink);

    TextPageData<Group> findGroupsByCustomerId(Integer customerId, TextPageLink pageLink) ;

    void deleteGroup(UUID groupId);
}
