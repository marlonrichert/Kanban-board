package org.vaadin.kanban.web;

import java.util.HashSet;
import java.util.Set;

import org.vaadin.customfield.PropertyConverter;
import org.vaadin.kanban.web.EntityFieldWrapper.EntityFieldPropertyConverter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.FieldWrapper;

/**
 * A field that wraps another multi-select field (typically a TwinColSelect) and
 * uses a container using {@link BeanItem}s or a JPAContainer to convert between
 * its values (set of entity IDs) and a set of entity instances in the
 * underlying property.
 * 
 * This makes it easier to use {code JPAContainer} e.g. for relationships on
 * forms when the field in the underlying master entity field points to a set of
 * entity instances, not IDs.
 * 
 * @param <E>
 */
public class EntitySetFieldWrapper<E> extends FieldWrapper<Set> {

    public EntitySetFieldWrapper(Field wrappedField, Class<E> entityType,
            Container container, Object idPropertyId) {
        super(wrappedField, new EntitySetFieldPropertyConverter<E, Object>(
                entityType, container, idPropertyId), Set.class);

        setCompositionRoot(wrappedField);

        setImmediate(true);
    }

    public static class EntitySetFieldPropertyConverter<ENTITY_TYPE, ID_TYPE>
            extends PropertyConverter<Set, Set<ID_TYPE>> {

        private final Container container;
        private final Object idPropertyId;
        private final Class<? extends ENTITY_TYPE> entityType;

        public EntitySetFieldPropertyConverter(Class<ENTITY_TYPE> entityType,
                Container container, Object idPropertyId) {
            super(Set.class);

            this.container = container;
            this.idPropertyId = idPropertyId;
            this.entityType = entityType;
        }

        /**
         * Convert an entity instance to its identifier.
         */
        @Override
        public Set<ID_TYPE> format(Set values) {
            if (values != null) {
                Set<ID_TYPE> ids = new HashSet<ID_TYPE>();
                for (Object value : values) {
                    if (value != null
                            && entityType.isAssignableFrom(value.getClass())) {
                        ID_TYPE id = EntityFieldPropertyConverter
                                .getIdForEntity((ENTITY_TYPE) value,
                                        idPropertyId);
                        ids.add(id);
                    }
                }
                return ids;
            } else {
                return null;
            }
        }

        /**
         * Return the entity instance for an entity identifier.
         */
        @Override
        public Set<ENTITY_TYPE> parse(Set<ID_TYPE> fieldValue)
                throws ConversionException {
            Set<ENTITY_TYPE> entities = new HashSet<ENTITY_TYPE>();
            for (Object id : fieldValue) {
                Item item = container.getItem(id);
                if (item != null) {
                    ENTITY_TYPE entity = EntityFieldPropertyConverter
                            .getEntityForItem(item);
                    if (entity != null
                            && entityType.isAssignableFrom(entity.getClass())) {
                        entities.add(entity);
                    }
                }
            }
            return entities;
        }
    }

}