package com.jobbi.me.repositories;

import com.jobbi.me.entities.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {
    List<Summary> findByHeaderId(int headerId);
}
