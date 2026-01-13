package com.rioDesertoAcessoDb.repositories;


import com.rioDesertoAcessoDb.model.VazaoMina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface VazaoMinaRepository extends JpaRepository<VazaoMina, Long> {

    // Query 1: Estatísticas principais - agora retorna Map
    @Query(value = """
        SELECT
            CAST(AVG(vazao_bombeamento) AS DOUBLE PRECISION) AS media_vazao,
            CAST((
                SELECT AVG(vazao_bombeamento)
                FROM (
                    SELECT vazao_bombeamento
                    FROM tb_vazao_mina
                    ORDER BY mes_ano_vazao DESC
                    OFFSET 1
                    LIMIT 3
                ) AS tres_anteriores
            ) AS DOUBLE PRECISION) AS media_3_meses_anteriores,
            CAST(MIN(vazao_bombeamento) AS DOUBLE PRECISION) AS minimo_vazao,
            CAST(MAX(vazao_bombeamento) AS DOUBLE PRECISION) AS maximo_vazao,
            CAST((SELECT vazao_bombeamento
             FROM tb_vazao_mina
             ORDER BY mes_ano_vazao DESC, id DESC
             LIMIT 1) AS DOUBLE PRECISION) AS ultima_vazao,
            MIN(mes_ano_vazao) AS primeira_data,
            MAX(mes_ano_vazao) AS ultima_data,
            CAST(COUNT(*) AS BIGINT) AS total_registros,
            (
                SELECT ARRAY_AGG(mes_ano_vazao ORDER BY mes_ano_vazao DESC)
                FROM (
                    SELECT mes_ano_vazao
                    FROM tb_vazao_mina
                    ORDER BY mes_ano_vazao DESC
                    OFFSET 1
                    LIMIT 3
                ) AS meses
            ) AS meses_considerados_para_media
        FROM tb_vazao_mina
        """, nativeQuery = true)
    Map<String, Object> findEstatisticasVazao();

    // Query 2: Histórico completo
    @Query(value = "SELECT vazao_bombeamento, mes_ano_vazao, id FROM tb_vazao_mina ORDER BY mes_ano_vazao DESC", nativeQuery = true)
    List<Object[]> findHistoricoCompleto();
}
