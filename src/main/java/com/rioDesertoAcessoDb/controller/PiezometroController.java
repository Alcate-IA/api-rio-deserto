package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.Piezometro;
import com.rioDesertoAcessoDb.service.PiezometroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/piezometros")
@Tag(name = "Piezômetros", description = "APIs para consulta e gerenciamento de piezômetros")
public class PiezometroController {

    @Autowired
    private PiezometroService piezometroService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    @Operation(summary = "Listar piezômetros com filtros", description = "Retorna uma lista de piezômetros da mina 101 (cd_empresa 18), permitindo filtrar por situação e tipo")
    public List<Piezometro> getPiezometros(
            @Parameter(description = "Situação do piezômetro ('A' para Ativo, 'I' para Inativo)", example = "A") @RequestParam(required = false) String situacao,
            @Parameter(description = "Lista de tipos de piezômetro (PP, PR, PC, PV, PB)", example = "PP,PR") @RequestParam(required = false) List<String> tipos) {
        return piezometroService.getPiezometrosComFiltros(situacao, tipos);
    }

    @GetMapping("/rd-lab-piezometros")
    @Operation(summary = "Listar piezômetros ativos com vínculo Zeus", description = "Retorna uma lista de piezômetros que possuem vínculo com a tabela de identificação (Zeus/RD Lab). Permite filtrar por situação e tipo.")
    @ApiResponse(responseCode = "200", description = "Lista de piezômetros retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    public List<Map<String, Object>> getPiezometrosAtivos(
            @Parameter(description = "Situação do piezômetro ('A' para Ativo, 'I' para Inativo)", example = "A") @RequestParam(required = false) String situacao,
            @Parameter(description = "Lista de tipos de piezômetro (PP, PR, PC, PV, PB)", example = "PP,PR") @RequestParam(required = false) List<String> tipos) {

        StringBuilder sql = new StringBuilder("""
                SELECT
                    i.id_zeus,
                    p.nm_piezometro,
                    p.cd_piezometro,
                    p.id_piezometro,
                    p.tp_piezometro,
                    p.fg_situacao
                FROM identificacao i
                JOIN tb_piezometro p
                    ON p.cd_piezometro = i.id_zeus
                WHERE p.tp_piezometro IN ('A', 'PP', 'PR', 'PV', 'PC')
                AND p.cd_empresa = 18
                """);

        List<Object> params = new ArrayList<>();

        if (situacao != null && !situacao.trim().isEmpty()) {
            sql.append(" AND p.fg_situacao = ?");
            params.add(situacao);
        }

        if (tipos != null && !tipos.isEmpty()) {
            List<String> tiposFiltrados = tipos.stream()
                    .filter(t -> t != null && !t.trim().isEmpty())
                    .collect(Collectors.toList());

            if (!tiposFiltrados.isEmpty()) {
                sql.append(" AND p.tp_piezometro IN (");
                for (int i = 0; i < tiposFiltrados.size(); i++) {
                    if (i > 0) sql.append(", ");
                    sql.append("?");
                    params.add(tiposFiltrados.get(i));
                }
                sql.append(")");
            }
        }

        sql.append(" ORDER BY p.nm_piezometro");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }
}