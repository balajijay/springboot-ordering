package com.balaji.orderservice.repository;

import com.balaji.orderservice.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    List<Outbox> findByProcessedFalse();
    List<Outbox> findByProcessedFalseOrderByIdAsc();
}

