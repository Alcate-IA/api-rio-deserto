package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.InspecaoPiezometroFreq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspecaoPiezometroFreqRepository extends JpaRepository<InspecaoPiezometroFreq, Integer> {

    @Query(value = """
            SELECT DISTINCT ON (cd_piezometro)
                cd_piezometro, tp_frequencia_new, dt_inclusao
            FROM tb_inspecao_piezometro_freq
            ORDER BY cd_piezometro, dt_inclusao DESC
            """, nativeQuery = true)
    List<Map<String, Object>> findLatestFrequencies();

    @Query(value = """
            SELECT
                p.cd_piezometro, p.nm_piezometro, p.tp_piezometro, p.id_piezometro,
                ipf.tp_frequencia_new,
                latest_insp.dt_inspecao as dt_ultima_inspecao,
                latest_insp.origem
            FROM tb_piezometro p
            LEFT JOIN (
                SELECT DISTINCT ON (cd_piezometro) cd_piezometro, tp_frequencia_new
                FROM tb_inspecao_piezometro_freq
                ORDER BY cd_piezometro, dt_inclusao DESC
            ) ipf ON p.cd_piezometro = ipf.cd_piezometro
            LEFT JOIN LATERAL (
                SELECT dt_inspecao, origem
                FROM (
                    -- Inspecao Piezometro (PB, PP)
                    SELECT ipm.dt_inspecao, 'TB_INSPECAO_PIEZOMETRO_MVTO' as origem
                    FROM tb_inspecao_piezometro_mvto ipm
                    JOIN tb_inspecao_piezometro ip ON ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro
                    WHERE ip.cd_piezometro = p.cd_piezometro AND ipm.dt_inspecao IS NOT NULL

                    UNION ALL

                    -- Nivel Agua (PR)
                    SELECT nai.dt_inspecao, 'TB_NIVEL_AGUA_ITEM' as origem
                    FROM tb_nivel_agua_item nai
                    JOIN tb_nivel_agua na ON nai.cd_nivel_agua = na.cd_nivel_agua
                    WHERE na.cd_piezometro = p.cd_piezometro AND nai.dt_inspecao IS NOT NULL

                    UNION ALL

                    -- Recursos Hidricos (PC, PV)
                    SELECT rhi.dt_inspecao, 'TB_RECURSOS_HIDRICOS_ITEM' as origem
                    FROM tb_recursos_hidricos_item rhi
                    JOIN tb_recursos_hidricos rh ON rhi.cd_recursos_hidricos = rh.cd_recursos_hidricos
                    WHERE rh.cd_piezometro = p.cd_piezometro AND rhi.dt_inspecao IS NOT NULL
                ) insp
                ORDER BY dt_inspecao DESC NULLS LAST
                LIMIT 1
            ) latest_insp ON TRUE
            WHERE p.cd_empresa = :cdEmpresa AND p.fg_situacao = 'A'
            """, nativeQuery = true)
    List<Map<String, Object>> findPiezometrosWithLatestInspection(@Param("cdEmpresa") Integer cdEmpresa);
}
