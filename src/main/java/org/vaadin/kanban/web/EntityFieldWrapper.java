package org.vaadin.kanban.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.vaadin.customfield.PropertyConverter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.FieldWrapper;

/**
 * A field that wraps another single-select field (typically a combo box) and
 * uses a container of BeanItems or JPAContainer EntityItems to convert between
 * its values (entity ID) and entity instances in the underlying property.
 * 
 * This makes it easier to use containers such as {@code JPAContainer} for
 * relationships on forms when the field in the underlying master entity field
 * points to an entity instance, not an id.
 * 
 * @param <E>
 */
public class EntityFieldWrapper<E> extends FieldWrapper<E> {
    private static final String JPA_CONTAINER_ITEM_CLASS = "com.vaadin.addon.jpacontainer.JPAContainerItem";
    private static final String JPA_ITEM_GET_ENTITY_METHOD = "getEntity";

    public EntityFieldWrapper(Field wrappedField, Class<E> entityType,
            Container container, Object idPropertyId) {
        super(wrappedField, new EntityFieldPropertyConverter<E, Object>(
                entityType, container, idPropertyId), entityType);

        setCompositionRoot(wrappedField);

        setImmediate(true);
    }

    public static class EntityFieldPropertyConverter<ENTITY_TYPE, ID_TYPE>
            extends PropertyConverter<ENTITY_TYPE, ID_TYPE> {

        private final Container container;
        private final Object idPropertyId;

        public EntityFieldPropertyConverter(Class<ENTITY_TYPE> entityType,
                Container container, Object idPropertyId) {
            super(entityType);

            this.container = container;
            this.idPropertyId = idPropertyId;
        }

        /**
         * Convert an entity instance to its identifier.
         */
        @Override
        public ID_TYPE format(ENTITY_TYPE propertyValue) {
            if (propertyValue != null) {
                // reflection to avoid dependencies
                return getIdForEntity(propertyValue, idPropertyId);
            } else {
                return null;
            }
        }

        /**
         * Return the entity instance for an entity identifier.
         */
        @Override
        public ENTITY_TYPE parse(Object fieldValue) throws ConversionException {
            Item item = container.getItem(fieldValue);
            return getEntityForItem(item);
        }

        /**
         * Extract the entity instance from an item. The item can be a
         * {@link BeanItem} or a {@code JPAContainerItem}, and reflection is
         * used to avoid explicit dependencies to {@code JPAContainer}.
         * 
         * @param item
         *            a {@link BeanItem} or a {@code JPAContainerItem}
         * @return the entity corresponding to the item
         */
        static <ENTITY_TYPE> ENTITY_TYPE getEntityForItem(Item item) {
            if (item instanceof BeanItem) {
                // assume the bean type is E
                return ((BeanItem<ENTITY_TYPE>) item).getBean();
            } else if (item != null
                    && JPA_CONTAINER_ITEM_CLASS.equals(item.getClass()
                            .getName())) {
                // use reflection to avoid a JPAContainer dependency
                return callGetter(item, JPA_ITEM_GET_ENTITY_METHOD);
            } else {
                return null;
            }
        }

        /**
         * Extracts the entity identifier for an item by calling a getter with
         * reflection.
         * 
         * @param value
         * @param idPropertyId
         *            property identifier for the property containing the entity
         *            identifier
         * @return
         */
        static <ENTITY_TYPE, ID_TYPE> ID_TYPE getIdForEntity(ENTITY_TYPE value,
                Object idPropertyId) {
            ID_TYPE id = callGetter(value, "get"
                    + idPropertyId.toString().substring(0, 1).toUpperCase()
                    + idPropertyId.toString().substring(1));
            return id;
        }

        // helper method
        private static <T> T callGetter(Object obj, String methodName)
                throws ConversionException {
            try {
                Method method = obj.getClass().getMethod(methodName);
                method.setAccessible(true);
                return (T) method.invoke(obj);
            } catch (SecurityException e) {
                throw new ConversionException(e);
            } catch (NoSuchMethodException e) {
                throw new ConversionException(e);
            } catch (IllegalArgumentException e) {
                throw new ConversionException(e);
            } catch (IllegalAccessException e) {
                throw new ConversionException(e);
            } catch (InvocationTargetException e) {
                throw new ConversionException(e);
            }
        }

    }

}