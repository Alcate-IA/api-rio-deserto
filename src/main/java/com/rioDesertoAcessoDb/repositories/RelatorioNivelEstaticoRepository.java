package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.Piezometro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RelatorioNivelEstaticoRepository extends JpaRepository<Piezometro, Integer> {

  @Query(value = """
      SELECT
          na.vl_cota AS cota_superficie,
          NULL AS cota_base,
          COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) AS mes_ano,
          p.precipitacao_total AS precipitacao,
          v.vazao_bombeamento AS vazao_bombeamento,
          n.media_nivel_estatico AS nivel_estatico,
          n.ds_observacao
      FROM
          (SELECT
              DATE_TRUNC('month', dt_item)::date AS mes_ano,
              SUM(vl_precipitacao) AS precipitacao_total
           FROM tb_meteorologia_item
           WHERE dt_item >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND dt_item <= TO_DATE(:dataFim, 'DD/MM/YYYY')
             AND cd_meteorologia = 12
           GROUP BY DATE_TRUNC('month', dt_item)
          ) p
      FULL JOIN
          (SELECT
              mes_ano_vazao AS mes_ano,
              vazao_bombeamento
           FROM tb_vazao_mina
           WHERE mes_ano_vazao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND mes_ano_vazao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
          ) v ON p.mes_ano = v.mes_ano
      FULL JOIN
          (SELECT
              DATE_TRUNC('month', nai.dt_inspecao)::date AS mes_ano,
              AVG(nai.qt_nivel_estatico) AS media_nivel_estatico,
              STRING_AGG(nai.ds_observacao, ', ') AS ds_observacao
           FROM tb_nivel_agua_item nai
           INNER JOIN tb_nivel_agua na ON nai.cd_nivel_agua = na.cd_nivel_agua
           WHERE na.cd_piezometro = :cdPiezometro
             AND nai.dt_inspecao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND nai.dt_inspecao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
           GROUP BY DATE_TRUNC('month', nai.dt_inspecao)
          ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
      CROSS JOIN (
          SELECT vl_cota
          FROM tb_nivel_agua
          WHERE cd_piezometro = :cdPiezometro
          LIMIT 1
      ) na
      ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) ASC
      """, nativeQuery = true)
  List<Map<String, Object>> findDadosReguaComFiltro(
      @Param("cdPiezometro") Integer cdPiezometro,
      @Param("dataInicio") String dataInicio,
      @Param("dataFim") String dataFim);

  @Query(value = """
      SELECT
          NULL AS cota_superficie,
          NULL AS cota_base,
          COALESCE(p.mes_ano, v.mes_ano, rh.mes_ano) AS mes_ano,
          p.precipitacao_total AS precipitacao,
          v.vazao_bombeamento AS vazao_bombeamento,
          rh.vazao_calha AS vazao_calha,
          NULL AS nivel_estatico
      FROM
          (SELECT
              DATE_TRUNC('month', dt_item)::date AS mes_ano,
              SUM(vl_precipitacao) AS precipitacao_total
           FROM tb_meteorologia_item
           WHERE dt_item >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND dt_item <= TO_DATE(:dataFim, 'DD/MM/YYYY')
             AND cd_meteorologia = 12
           GROUP BY DATE_TRUNC('month', dt_item)
          ) p
      FULL JOIN
          (SELECT
              mes_ano_vazao AS mes_ano,
              vazao_bombeamento
           FROM tb_vazao_mina
           WHERE mes_ano_vazao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND mes_ano_vazao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
          ) v ON p.mes_ano = v.mes_ano
      FULL JOIN
          (SELECT
              DATE_TRUNC('month', rhi.dt_inspecao)::date AS mes_ano,
              AVG(rhi.qt_leitura) AS vazao_calha
           FROM tb_recursos_hidricos_item rhi
           INNER JOIN tb_recursos_hidricos rh ON rhi.cd_recursos_hidricos = rh.cd_recursos_hidricos
           WHERE rh.cd_piezometro = :cdPiezometro
             AND rhi.dt_inspecao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND rhi.dt_inspecao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
           GROUP BY DATE_TRUNC('month', rhi.dt_inspecao)
          ) rh ON COALESCE(p.mes_ano, v.mes_ano) = rh.mes_ano
      ORDER BY COALESCE(p.mes_ano, v.mes_ano, rh.mes_ano) ASC
      """, nativeQuery = true)
  List<Map<String, Object>> findDadosRecursosHidricosComFiltro(
      @Param("cdPiezometro") Integer cdPiezometro,
      @Param("dataInicio") String dataInicio,
      @Param("dataFim") String dataFim);

  @Query(value = """
      SELECT
          ip.qt_cota_superficie AS cota_superficie,
          ip.qt_cota_base AS cota_base,
          ip.qt_cota_boca AS cota_boca,
          COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) AS mes_ano,
          p.precipitacao_total AS precipitacao,
          v.vazao_bombeamento AS vazao_bombeamento,
          n.media_nivel_estatico AS nivel_estatico,
          n.ds_observacao,
          NULL AS vazao_calha
      FROM
          (SELECT
              DATE_TRUNC('month', dt_item)::date AS mes_ano,
              SUM(vl_precipitacao) AS precipitacao_total
           FROM tb_meteorologia_item
           WHERE dt_item >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND dt_item <= TO_DATE(:dataFim, 'DD/MM/YYYY')
             AND cd_meteorologia = 12
           GROUP BY DATE_TRUNC('month', dt_item)
          ) p
      FULL JOIN
          (SELECT
              mes_ano_vazao AS mes_ano,
              vazao_bombeamento
           FROM tb_vazao_mina
           WHERE mes_ano_vazao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND mes_ano_vazao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
          ) v ON p.mes_ano = v.mes_ano
      FULL JOIN
          (SELECT
              DATE_TRUNC('month', ipm.dt_inspecao)::date AS mes_ano,
              AVG(ipm.qt_nivel_estatico) AS media_nivel_estatico,
              STRING_AGG(ipm.ds_observacao, ', ') AS ds_observacao
           FROM tb_inspecao_piezometro_mvto ipm
           INNER JOIN tb_inspecao_piezometro ip ON ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro
           WHERE ip.cd_piezometro = :cdPiezometro
             AND ipm.dt_inspecao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
             AND ipm.dt_inspecao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
           GROUP BY DATE_TRUNC('month', ipm.dt_inspecao)
          ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
      CROSS JOIN (
          SELECT qt_cota_superficie, qt_cota_base, qt_cota_boca
          FROM tb_inspecao_piezometro
          WHERE cd_piezometro = :cdPiezometro
          LIMIT 1
      ) ip
      ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) ASC
      """, nativeQuery = true)
  List<Map<String, Object>> findDadosPiezometroComumComFiltro(
      @Param("cdPiezometro") Integer cdPiezometro,
      @Param("dataInicio") String dataInicio,
      @Param("dataFim") String dataFim);

  @Query(value = """
      SELECT qt_cota_superficie AS cota_superficie, qt_cota_base AS cota_base, qt_cota_boca AS cota_boca
      FROM tb_inspecao_piezometro
      WHERE cd_piezometro = :cdPiezometro
      LIMIT 1
      """, nativeQuery = true)
  Map<String, Object> findCotasPiezometro(@Param("cdPiezometro") Integer cdPiezometro);

  @Query(value = """
      SELECT vl_precipitacao AS precipitacao, TO_CHAR(dt_item, 'DD/MM/YYYY') AS data
      FROM tb_meteorologia_item
      WHERE cd_meteorologia = 12
        AND dt_item >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
        AND dt_item <= TO_DATE(:dataFim, 'DD/MM/YYYY')
      ORDER BY dt_item ASC
      """, nativeQuery = true)
  List<Map<String, Object>> findPrecipitacaoDiaria(
      @Param("dataInicio") String dataInicio,
      @Param("dataFim") String dataFim);

  @Query(value = """
      SELECT vazao_bombeamento AS vazao_bombeamento, TO_CHAR(mes_ano_vazao, 'DD/MM/YYYY') AS data
      FROM tb_vazao_mina
      WHERE mes_ano_vazao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
        AND mes_ano_vazao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
      ORDER BY mes_ano_vazao ASC
      """, nativeQuery = true)
  List<Map<String, Object>> findVazaoDiaria(
      @Param("dataInicio") String dataInicio,
      @Param("dataFim") String dataFim);

  @Query(value = """
      SELECT ipm.qt_nivel_estatico AS nivel_estatico, TO_CHAR(ipm.dt_inspecao, 'DD/MM/YYYY') AS data, ipm.ds_observacao AS ds_observacao
      FROM tb_inspecao_piezometro_mvto ipm
      INNER JOIN tb_inspecao_piezometro ip ON ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro
      WHERE ip.cd_piezometro = :cdPiezometro
        AND ipm.dt_inspecao >= TO_DATE(:dataInicio, 'DD/MM/YYYY')
        AND ipm.dt_inspecao <= TO_DATE(:dataFim, 'DD/MM/YYYY')
      ORDER BY ipm.dt_inspecao ASC
      """, nativeQuery = true)
  List<Map<String, Object>> findNivelEstaticoDiario(
      @Param("cdPiezometro") Integer cdPiezometro,
      @Param("dataInicio") String dataInicio,
      @Param("dataFim") String dataFim);
}
