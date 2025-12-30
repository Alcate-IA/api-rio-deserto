package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.service.RelatorioNivelEstaticoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorio-nivel-estatico")
@Tag(name = "Relatório Nível Estático", description = "APIs para geração de relatórios de nível estático de piezômetros")
public class RelatorioNivelEstaticoController {

        @Autowired
        private RelatorioNivelEstaticoService relatorioNivelEstaticoService;

        @GetMapping("/piezometro/{cdPiezometro}/filtro")
        @Operation(summary = "Obter dados de piezômetro filtrados", description = "Retorna uma lista simples de dados específicos de um piezômetro de acordo com seu tipo (PR, PC, PV, PP) para um período determinado")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Dados do piezômetro retornados com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Tipo de piezômetro não suportado"),
                        @ApiResponse(responseCode = "404", description = "Piezômetro não encontrado")
        })
        public List<Map<String, Object>> getDadosPiezometroComFiltro(
                        @PathVariable Integer cdPiezometro,
                        @RequestParam(required = false) String mesAnoInicio,
                        @RequestParam(required = false) String mesAnoFim) {
                return relatorioNivelEstaticoService.getDadosPiezometroComFiltro(cdPiezometro, mesAnoInicio, mesAnoFim);
        }

        @GetMapping("/piezometro/{cdPiezometro}/filtro-com-historico")
        @Operation(summary = "Obter dados de piezômetro filtrados e histórico completo", description = "Retorna um objeto contendo os dados filtrados e o histórico completo do piezômetro")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Dados do piezômetro retornados com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Tipo de piezômetro não suportado"),
                        @ApiResponse(responseCode = "404", description = "Piezômetro não encontrado")
        })
        public Map<String, Object> getDadosPiezometroComFiltroEHistorico(
                        @PathVariable Integer cdPiezometro,
                        @RequestParam String mesAnoInicio,
                        @RequestParam String mesAnoFim) {
                return relatorioNivelEstaticoService.getDadosPiezometroComFiltroEHistorico(cdPiezometro, mesAnoInicio,
                                mesAnoFim);
        }

        @GetMapping("/piezometro/{cdPiezometro}/diario")
        @Operation(summary = "Obter dados de piezômetro por dia", description = "Retorna os dados do piezômetro (cotas, precipitacao, vazao, nivel estatico) organizados por dia")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Dados do piezômetro retornados com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Piezômetro não encontrado")
        })
        public Map<String, Object> getDadosPiezometroDiario(
                        @PathVariable Integer cdPiezometro,
                        @RequestParam(required = false) String dataInicio,
                        @RequestParam(required = false) String dataFim) {
                return relatorioNivelEstaticoService.getDadosPiezometroDiario(cdPiezometro, dataInicio, dataFim);
        }
}
