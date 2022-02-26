package practice.springsecurity.security.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.util.ObjectUtils;
import practice.springsecurity.security.service.SecurityResourceService;

import java.util.LinkedHashMap;
import java.util.List;

public class MethodResourcesFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

    private SecurityResourceService service;
    private LinkedHashMap<String, List<ConfigAttribute>> resourceMap;

    public MethodResourcesFactoryBean(SecurityResourceService service) {
        this.service = service;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }

    @Override
    public LinkedHashMap<String, List<ConfigAttribute>> getObject() {
        if (ObjectUtils.isEmpty(this.resourceMap)) {
            init();
        }

        return this.resourceMap;
    }

    private void init() {
        this.resourceMap = service.getMethodResourceList();
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }
}
