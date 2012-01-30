/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo MES
 * Version: 1.1.2
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.mes.technologies;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.EntityList;
import com.qcadoo.model.api.EntityTree;
import com.qcadoo.model.api.EntityTreeNode;

public class ProductQuantitiesServiceTest {

    private ProductQuantitiesService productQuantitiesService;

    @Mock
    private Entity order;

    @Mock
    private Entity technology;

    @Mock
    private Entity product1, product2, product3, product4;

    @Mock
    private Entity productInComponent1, productInComponent2, productInComponent3;

    @Mock
    private Entity productOutComponent2, productOutComponent4;

    @Mock
    private DataDefinition ddIn, ddOut;

    @Mock
    private EntityTreeNode operationComponent1, operationComponent2;

    @Mock
    private EntityList productInComponentsForOperation1;

    @Mock
    private EntityList productOutComponentsForOperation1;

    @Mock
    private EntityList productInComponentsForOperation2;

    @Mock
    private EntityList productOutComponentsForOperation2;

    private EntityList orders;

    private EntityTree tree;

    private Map<Entity, List<Entity>> productInComponents;

    private Map<Entity, List<Entity>> productOutComponents;

    private BigDecimal plannedQty;

    private static EntityList mockEntityListIterator(List<Entity> list) {
        EntityList entityList = mock(EntityList.class);
        when(entityList.iterator()).thenReturn(list.iterator());
        return entityList;
    }

    private static EntityTree mockEntityTreeIterator(List<Entity> list) {
        EntityTree entityTree = mock(EntityTree.class);
        when(entityTree.iterator()).thenReturn(list.iterator());
        return entityTree;
    }

    @Before
    @SuppressWarnings("unchecked")
    public void init() {
        MockitoAnnotations.initMocks(this);

        productQuantitiesService = new ProductQuantitiesService();

        orders = mockEntityListIterator(asList(order));

        when(order.getBelongsToField("technology")).thenReturn(technology);

        tree = mockEntityTreeIterator(asList((Entity) operationComponent1, (Entity) operationComponent2));

        productInComponentsForOperation1 = mock(EntityList.class);
        productOutComponentsForOperation1 = mock(EntityList.class);

        Iterator<Entity> productInComponentsForOperation1iterator = mock(Iterator.class);
        Iterator<Entity> productOutComponentsForOperation1iterator = mock(Iterator.class);
        when(productInComponentsForOperation1.iterator()).thenReturn(productInComponentsForOperation1iterator);
        when(productOutComponentsForOperation1.iterator()).thenReturn(productOutComponentsForOperation1iterator);
        when(productInComponentsForOperation1iterator.hasNext()).thenReturn(true, false, true, false, true, false);
        when(productOutComponentsForOperation1iterator.hasNext()).thenReturn(true, false, true, false, true, false);
        when(productInComponentsForOperation1iterator.next()).thenReturn(productInComponent1, productInComponent1,
                productInComponent1);
        when(productOutComponentsForOperation1iterator.next()).thenReturn(productOutComponent2, productOutComponent2,
                productOutComponent2);

        productInComponentsForOperation2 = mock(EntityList.class);
        productOutComponentsForOperation2 = mock(EntityList.class);

        Iterator<Entity> productInComponentsForOperation2iterator = mock(Iterator.class);
        Iterator<Entity> productOutComponentsForOperation2iterator = mock(Iterator.class);
        when(productInComponentsForOperation2.iterator()).thenReturn(productInComponentsForOperation2iterator);
        when(productOutComponentsForOperation2.iterator()).thenReturn(productOutComponentsForOperation2iterator);
        when(productInComponentsForOperation2iterator.hasNext()).thenReturn(true, true, false, true, true, false, true, true,
                false);
        when(productOutComponentsForOperation2iterator.hasNext()).thenReturn(true, false, true, false, true, false);
        when(productInComponentsForOperation2iterator.next()).thenReturn(productInComponent2, productInComponent3,
                productInComponent2, productInComponent3, productInComponent2, productInComponent3);
        when(productOutComponentsForOperation2iterator.next()).thenReturn(productOutComponent4, productOutComponent4,
                productOutComponent4);

        when(operationComponent1.getHasManyField("operationProductInComponents")).thenReturn(productInComponentsForOperation1);
        when(operationComponent1.getHasManyField("operationProductOutComponents")).thenReturn(productOutComponentsForOperation1);

        when(operationComponent2.getHasManyField("operationProductInComponents")).thenReturn(productInComponentsForOperation2);
        when(operationComponent2.getHasManyField("operationProductOutComponents")).thenReturn(productOutComponentsForOperation2);

        plannedQty = new BigDecimal(4.5f);

        when(order.getField("plannedQuantity")).thenReturn(plannedQty);

        when(productInComponent1.getField("quantity")).thenReturn(new BigDecimal(5));
        when(productInComponent2.getField("quantity")).thenReturn(new BigDecimal(2));
        when(productInComponent3.getField("quantity")).thenReturn(new BigDecimal(1));
        when(productOutComponent2.getField("quantity")).thenReturn(new BigDecimal(1));
        when(productOutComponent4.getField("quantity")).thenReturn(new BigDecimal(1));

        productInComponents = new HashMap<Entity, List<Entity>>();
        productOutComponents = new HashMap<Entity, List<Entity>>();

        productInComponents.put(operationComponent1, asList(productInComponent1));
        productInComponents.put(operationComponent2, asList(productInComponent2, productInComponent3));
        productOutComponents.put(operationComponent1, asList(productOutComponent2));
        productOutComponents.put(operationComponent2, asList(productOutComponent4));

        when(product1.getId()).thenReturn(1L);
        when(product2.getId()).thenReturn(2L);
        when(product3.getId()).thenReturn(3L);
        when(product4.getId()).thenReturn(4L);

        when(technology.getBelongsToField("product")).thenReturn(product4);
        when(productInComponent1.getBelongsToField("product")).thenReturn(product1);
        when(productInComponent2.getBelongsToField("product")).thenReturn(product2);
        when(productInComponent3.getBelongsToField("product")).thenReturn(product3);
        when(productOutComponent2.getBelongsToField("product")).thenReturn(product2);
        when(productOutComponent4.getBelongsToField("product")).thenReturn(product4);

        when(tree.getRoot()).thenReturn(operationComponent2);

        EntityList operationComponent1children = mockEntityListIterator(new LinkedList<Entity>());
        EntityList operationComponent2children = mockEntityListIterator(asList((Entity) operationComponent1));

        when(operationComponent1.getHasManyField("children")).thenReturn(operationComponent1children);
        when(operationComponent2.getHasManyField("children")).thenReturn(operationComponent2children);

        when(technology.getTreeField("operationComponents")).thenReturn(tree);

        when(ddIn.getName()).thenReturn("operationProductInComponent");
        when(ddOut.getName()).thenReturn("operationProductOutComponent");

        when(productInComponent1.getDataDefinition()).thenReturn(ddIn);
        when(productInComponent2.getDataDefinition()).thenReturn(ddIn);
        when(productInComponent3.getDataDefinition()).thenReturn(ddIn);
        when(productOutComponent2.getDataDefinition()).thenReturn(ddOut);
        when(productOutComponent4.getDataDefinition()).thenReturn(ddOut);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldReturnIllegalStateExceptionIfTheresNoTechnology() {
        // given
        when(order.getBelongsToField("technology")).thenReturn(null);

        // when
        productQuantitiesService.getProductComponentQuantities(orders);
    }

    @Test
    public void shouldReturnCorrectQuantities() {
        // when
        Map<Entity, BigDecimal> productQuantities = productQuantitiesService.getProductComponentQuantities(orders);

        // then
        assertEquals(new BigDecimal(50), productQuantities.get(productInComponent1));
        assertEquals(new BigDecimal(10), productQuantities.get(productInComponent2));
        assertEquals(new BigDecimal(5), productQuantities.get(productInComponent3));
        assertEquals(new BigDecimal(10), productQuantities.get(productOutComponent2));
        assertEquals(new BigDecimal(5), productQuantities.get(productOutComponent4));
    }

    @Test
    public void shouldReturnCorrectQuantitiesOfInputProductsForTechnology() {
        // given
        boolean onlyComponents = false;

        // when
        Map<Entity, BigDecimal> productQuantities = productQuantitiesService.getNeededProductQuantities(technology, plannedQty,
                onlyComponents);

        // then
        assertEquals(3, productQuantities.size());
        assertEquals(new BigDecimal(50), productQuantities.get(product1));
        assertEquals(new BigDecimal(10), productQuantities.get(product2));
        assertEquals(new BigDecimal(5), productQuantities.get(product3));
    }

    @Test
    public void shouldReturnQuantitiesOfInputProductsForOrdersAndIfToldCountOnlyComponents() {
        // given
        boolean onlyComponents = true;

        // when
        Map<Entity, BigDecimal> productQuantities = productQuantitiesService.getNeededProductQuantities(orders, onlyComponents);

        // then
        assertEquals(2, productQuantities.size());
        assertEquals(new BigDecimal(50), productQuantities.get(product1));
        assertEquals(new BigDecimal(5), productQuantities.get(product3));

    }

    @Test
    public void shouldReturnQuantitiesAlsoForListOfComponents() {
        // given
        Entity component = mock(Entity.class);
        when(component.getBelongsToField("order")).thenReturn(order);
        boolean onlyComponents = false;

        // when
        Map<Entity, BigDecimal> productQuantities = productQuantitiesService.getNeededProductQuantitiesForComponents(
                Arrays.asList(component), onlyComponents);

        // then
        assertEquals(3, productQuantities.size());
        assertEquals(new BigDecimal(50), productQuantities.get(product1));
        assertEquals(new BigDecimal(10), productQuantities.get(product2));
        assertEquals(new BigDecimal(5), productQuantities.get(product3));
    }

    @Test
    public void shouldReturnOperationMultipliers() {
        // given
        boolean onlyComponents = false;

        Map<Entity, BigDecimal> operationMultipliers = new HashMap<Entity, BigDecimal>();

        // when
        productQuantitiesService.getNeededProductQuantities(orders, onlyComponents, operationMultipliers);

        // then
        assertEquals(2, operationMultipliers.size());
        assertEquals(new BigDecimal(5), operationMultipliers.get(operationComponent2));
        assertEquals(new BigDecimal(10), operationMultipliers.get(operationComponent1));
    }

    @Test
    public void shouldReturnOperationMultipliersAlsoForComponents() {
        // given
        Map<Entity, BigDecimal> operationMultipliers = new HashMap<Entity, BigDecimal>();

        // when
        productQuantitiesService.getProductComponentQuantities(orders, operationMultipliers);

        // then
        assertEquals(2, operationMultipliers.size());
        assertEquals(new BigDecimal(5), operationMultipliers.get(operationComponent2));
        assertEquals(new BigDecimal(10), operationMultipliers.get(operationComponent1));
    }

    @Test
    public void shouldReturnOperationMultipliersAlsoForPlainTechnology() {
        // given
        Map<Entity, BigDecimal> operationMultipliers = new HashMap<Entity, BigDecimal>();

        // when
        productQuantitiesService.getProductComponentQuantities(technology, plannedQty, operationMultipliers);

        // then
        assertEquals(2, operationMultipliers.size());
        assertEquals(new BigDecimal(5), operationMultipliers.get(operationComponent2));
        assertEquals(new BigDecimal(10), operationMultipliers.get(operationComponent1));
    }
}