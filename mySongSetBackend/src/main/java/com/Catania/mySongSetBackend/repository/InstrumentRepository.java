package com.Catania.mySongSetBackend.repository;

import com.Catania.mySongSetBackend.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {
}
