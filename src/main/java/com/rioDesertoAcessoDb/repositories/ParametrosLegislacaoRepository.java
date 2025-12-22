package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.ParametrosLegislacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ParametrosLegislacaoRepository extends JpaRepository<ParametrosLegislacao, Integer> {

    @Query(value = """
            SELECT DISTINCT ON (a.id_analise)
                a.simbolo,
                a.nome,
                pl.id_parametro_legislacao,
                pl.id_analise
            FROM parametros_legislacao pl
            JOIN analise a ON pl.id_analise = a.id_analise
            ORDER BY a.id_analise
            """, nativeQuery = true)
    List<Map<String, Object>> findParametrosParaFiltros();

    @Query(value = """
            SELECT
                pl.id_parametro_legislacao,
                pl.id_analise,
                a.nome as nome_analise,
                a.simbolo,
                pl.id_legislacao,
                l.nome_legislacao
            FROM parametros_legislacao pl
            JOIN analise a ON pl.id_analise = a.id_analise
            JOIN legislacoes l ON pl.id_legislacao = l.id_legislacao
            """, nativeQuery = true)
    List<Map<String, Object>> findDadosRelacionados();
}
