package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Ubicacion;

@Repository
public interface UbicacionRepositorio extends JpaRepository<Ubicacion, Integer> {

}
