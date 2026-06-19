package in.maitra.treats.invento.util.filter;

import in.maitra.treats.invento.util.persistence.PersistableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class PersistentEntityFilterMetadataLoader implements SmartInitializingSingleton {

    private final List<String> persistableEntityPackageNames;

    @Override
    public void afterSingletonsInstantiated() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(PersistableEntity.class));
        log.info("Base packages to scan for Filterable annotations : {}", persistableEntityPackageNames);
        for(String basePackage : persistableEntityPackageNames) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            candidates.forEach(beanDefinition -> {
                try {
                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                    if(!clazz.isInterface() && PersistableEntity.class.isAssignableFrom(clazz)) {
                        @SuppressWarnings("unchecked")
                        Class<? extends PersistableEntity> entityClass = (Class<? extends PersistableEntity>) clazz;
                        // Get all fields from the class and filter those with @Filterable annotation
                        List<Field> filterableFields = Arrays.stream(entityClass.getDeclaredFields())
                                .filter(field -> field.isAnnotationPresent(Filterable.class))
                                .toList();
                        filterableFields.forEach(field -> {
                            Filterable filterableAnnotation = field.getAnnotation(Filterable.class);
                            EntityFilterableFieldRegistry.registerFilterableField(entityClass.getSimpleName(), field.getName(), filterableAnnotation.columnName());
                        });
                        log.debug("Filterable fields found in entity class {} : {}", entityClass.getSimpleName(), filterableFields.size());
                    }
                } catch (ClassNotFoundException e) {
                    log.error("Error while loading RelationalDatabaseEntity with class name {} : ", beanDefinition.getBeanClassName(), e);
                }
            });
        }
    }
}
