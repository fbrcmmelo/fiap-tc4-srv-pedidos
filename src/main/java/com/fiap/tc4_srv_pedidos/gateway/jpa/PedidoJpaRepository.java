package com.fiap.tc4_srv_pedidos.gateway.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoJpaRepository extends MongoRepository<PedidoEntityJpa, String> {
}
