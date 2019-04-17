package com.bolsadeideas.springboot.backend.apirest.models.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Factura;

@Transactional
public interface IFacturaDao extends CrudRepository<Factura, Long>{

}
