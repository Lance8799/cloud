package org.lance.cloud.seata.order;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lance
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
