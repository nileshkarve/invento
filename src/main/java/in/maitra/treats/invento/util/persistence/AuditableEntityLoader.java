package in.maitra.treats.invento.util.persistence;

import in.maitra.treats.invento.exception.InventoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class AuditableEntityLoader implements SmartInitializingSingleton {

    private final List<String> persistableEntityPackageNames;

    @Override
    public void afterSingletonsInstantiated() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(PersistableEntity.class));
        log.info("Base packages to scan for Auditable annotations : {}", persistableEntityPackageNames);
        for(String basePackage : persistableEntityPackageNames) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            candidates.forEach(beanDefinition -> {
                try {
                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                    if(!clazz.isInterface() && PersistableEntity.class.isAssignableFrom(clazz)) {
                        if(clazz.isAnnotationPresent(Auditable.class)) {
                            AuditableEntityRegistry.registerAuditableEntity(clazz.getSimpleName());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    log.error("Error while loading Auditable class with name {} : ", beanDefinition.getBeanClassName(), e);
                } catch (InventoException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
