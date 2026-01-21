package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.VazaoMina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface VazaoMinaRepository extends JpaRepository<VazaoMina, Long> {

    // Query 1: Estatísticas principais - agora com percentual de aumento
    @Query(value = """
            WITH primeira_ultima AS (
                SELECT
                    (SELECT vazao_bombeamento FROM tb_vazao_mina ORDER BY mes_ano_vazao ASC LIMIT 1) AS primeira_vazao,
                    (SELECT vazao_bombeamento FROM tb_vazao_mina ORDER BY mes_ano_vazao DESC LIMIT 1) AS ultima_vazao
            )
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
                ) AS meses_considerados_para_media,
                -- NOVO: Percentual de aumento da primeira para última vazão
                CAST(((pu.ultima_vazao - pu.primeira_vazao) / pu.primeira_vazao * 100) AS DOUBLE PRECISION) AS percentual_aumento_primeira_ultima,
                CAST(pu.primeira_vazao AS DOUBLE PRECISION) AS primeira_vazao_valor
            FROM tb_vazao_mina, primeira_ultima pu
            GROUP BY pu.primeira_vazao, pu.ultima_vazao
            """, nativeQuery = true)
    Map<String, Object> findEstatisticasVazao();

    // Query 2: Histórico completo
    @Query(value = "SELECT vazao_bombeamento, mes_ano_vazao, id FROM tb_vazao_mina ORDER BY mes_ano_vazao DESC", nativeQuery = true)
    List<Object[]> findHistoricoCompleto();

    // NOVA QUERY 3: Análise de tendência MÊS ANTERIOR para MÊS ATUAL
    @Query(value = """
            WITH mes_atual_sistema AS (
                SELECT
                    EXTRACT(MONTH FROM CURRENT_DATE) AS mes_atual_numero,
                    CASE EXTRACT(MONTH FROM CURRENT_DATE)
                        WHEN 1 THEN 'Janeiro'
                        WHEN 2 THEN 'Fevereiro'
                        WHEN 3 THEN 'Março'
                        WHEN 4 THEN 'Abril'
                        WHEN 5 THEN 'Maio'
                        WHEN 6 THEN 'Junho'
                        WHEN 7 THEN 'Julho'
                        WHEN 8 THEN 'Agosto'
                        WHEN 9 THEN 'Setembro'
                        WHEN 10 THEN 'Outubro'
                        WHEN 11 THEN 'Novembro'
                        WHEN 12 THEN 'Dezembro'
                    END AS mes_atual_nome,
                    CASE
                        WHEN EXTRACT(MONTH FROM CURRENT_DATE) = 1 THEN 12
                        ELSE EXTRACT(MONTH FROM CURRENT_DATE) - 1
                    END AS mes_anterior_numero
            ),
            -- Buscar todos os dados ordenados por data
            dados_completos AS (
                SELECT
                    vazao_bombeamento,
                    mes_ano_vazao,
                    EXTRACT(MONTH FROM mes_ano_vazao) AS mes_numero,
                    EXTRACT(YEAR FROM mes_ano_vazao) AS ano
                FROM tb_vazao_mina
                ORDER BY mes_ano_vazao
            ),
            -- Parear cada mês atual com seu mês anterior
            pares_mes_anterior_atual AS (
                SELECT
                    atual.ano,
                    atual.mes_numero AS mes_atual_numero,
                    atual.vazao_bombeamento AS vazao_mes_atual,
                    anterior.vazao_bombeamento AS vazao_mes_anterior,
                    anterior.mes_numero AS mes_anterior_numero,
                    anterior.ano AS ano_mes_anterior,
                    CASE
                        WHEN anterior.vazao_bombeamento IS NOT NULL
                        THEN ((atual.vazao_bombeamento - anterior.vazao_bombeamento) / anterior.vazao_bombeamento * 100)
                        ELSE NULL
                    END AS variacao_percentual
                FROM dados_completos atual
                LEFT JOIN dados_completos anterior ON (
                    -- Condição: mês anterior do mesmo ano OU dezembro do ano anterior
                    (atual.mes_numero = anterior.mes_numero + 1 AND atual.ano = anterior.ano) OR
                    (atual.mes_numero = 1 AND anterior.mes_numero = 12 AND atual.ano = anterior.ano + 1)
                )
            ),
            -- Filtrar apenas para o mês atual do sistema
            pares_mes_atual AS (
                SELECT pma.*
                FROM pares_mes_anterior_atual pma, mes_atual_sistema mas
                WHERE pma.mes_atual_numero = mas.mes_atual_numero
                AND pma.variacao_percentual IS NOT NULL
            ),
            estatisticas_mes_atual AS (
                SELECT
                    COUNT(*) AS total_comparacoes,
                    MIN(variacao_percentual) AS menor_variacao_percent,
                    MAX(variacao_percentual) AS maior_variacao_percent,
                    AVG(variacao_percentual) AS media_variacao_percent,
                    CASE
                        WHEN AVG(variacao_percentual) > 5 THEN 'Alta tendência de aumento'
                        WHEN AVG(variacao_percentual) > 0 THEN 'Tendência de aumento'
                        WHEN AVG(variacao_percentual) < -5 THEN 'Alta tendência de redução'
                        WHEN AVG(variacao_percentual) < 0 THEN 'Tendência de redução'
                        ELSE 'Tendência estável'
                    END AS classificacao_tendencia
                FROM pares_mes_atual
            )
            SELECT
                mas.mes_atual_numero AS mes_atual_numero,
                TRIM(mas.mes_atual_nome) AS mes_atual_nome,
                mas.mes_anterior_numero AS mes_anterior_numero,
                CAST(ema.total_comparacoes AS INTEGER) AS total_comparacoes,
                CAST(ema.menor_variacao_percent AS DOUBLE PRECISION) AS menor_variacao_percent,
                CAST(ema.maior_variacao_percent AS DOUBLE PRECISION) AS maior_variacao_percent,
                CAST(ema.media_variacao_percent AS DOUBLE PRECISION) AS media_variacao_percent,
                ema.classificacao_tendencia,
                -- Lista de variações por ano
                (
                    SELECT json_agg(
                        json_build_object(
                            'ano', pma.ano,
                            'vazao_mes_atual', pma.vazao_mes_atual,
                            'vazao_mes_anterior', pma.vazao_mes_anterior,
                            'mes_anterior_numero', pma.mes_anterior_numero,
                            'ano_mes_anterior', pma.ano_mes_anterior,
                            'variacao_percentual', pma.variacao_percentual
                        ) ORDER BY pma.ano
                    )
                    FROM pares_mes_atual pma
                ) AS historico_variacoes,
                -- Última vazão para referência
                (
                    SELECT vazao_bombeamento
                    FROM tb_vazao_mina
                    ORDER BY mes_ano_vazao DESC
                    LIMIT 1
                ) AS ultima_vazao_medida,
                -- Mês anterior nome
                CASE
                    WHEN mas.mes_anterior_numero = 1 THEN 'Janeiro'
                    WHEN mas.mes_anterior_numero = 2 THEN 'Fevereiro'
                    WHEN mas.mes_anterior_numero = 3 THEN 'Março'
                    WHEN mas.mes_anterior_numero = 4 THEN 'Abril'
                    WHEN mas.mes_anterior_numero = 5 THEN 'Maio'
                    WHEN mas.mes_anterior_numero = 6 THEN 'Junho'
                    WHEN mas.mes_anterior_numero = 7 THEN 'Julho'
                    WHEN mas.mes_anterior_numero = 8 THEN 'Agosto'
                    WHEN mas.mes_anterior_numero = 9 THEN 'Setembro'
                    WHEN mas.mes_anterior_numero = 10 THEN 'Outubro'
                    WHEN mas.mes_anterior_numero = 11 THEN 'Novembro'
                    WHEN mas.mes_anterior_numero = 12 THEN 'Dezembro'
                END AS mes_anterior_nome
            FROM mes_atual_sistema mas, estatisticas_mes_atual ema
            """, nativeQuery = true)
    Map<String, Object> findTendenciaSazonalVazao();
}