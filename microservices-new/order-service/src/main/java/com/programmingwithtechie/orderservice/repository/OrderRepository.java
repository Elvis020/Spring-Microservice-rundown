package com.programmingwithtechie.orderservice.repository;

import com.programmingwithtechie.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>{}