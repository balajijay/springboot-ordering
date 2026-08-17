package com.balaji.queueservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.balaji.queueservice.entity.*;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
