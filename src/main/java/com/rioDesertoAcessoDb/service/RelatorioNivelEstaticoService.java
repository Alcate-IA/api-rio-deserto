package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.repositories.PiezometroRepository;
import com.rioDesertoAcessoDb.repositories.RelatorioNivelEstaticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioNivelEstaticoService {

        @Autowired
        private PiezometroRepository piezometroRepository;

        @Autowired
        private RelatorioNivelEstaticoRepository relatorioNivelEstaticoRepository;

        public List<Map<String, Object>> getDadosPiezometroComFiltro(Integer cdPiezometro, String mesAnoInicio,
                        String mesAnoFim) {
                // Verifica o tipo do piezômetro
                String tipoPiezometro = piezometroRepository.findTipoPiezometroById(cdPiezometro);

                String dataInicio = (mesAnoInicio != null && !mesAnoInicio.isEmpty()) ? "01/" + mesAnoInicio
                                : "01/01/1900";
                String dataFim = (mesAnoFim != null && !mesAnoFim.isEmpty()) ? "01/" + mesAnoFim : "01/01/2100";

                if ("PR".equals(tipoPiezometro)) {
                        return relatorioNivelEstaticoRepository.findDadosReguaComFiltro(cdPiezometro, dataInicio,
                                        dataFim);
                } else if ("PC".equals(tipoPiezometro) || "PV".equals(tipoPiezometro)) {
                        return relatorioNivelEstaticoRepository.findDadosRecursosHidricosComFiltro(cdPiezometro,
                                        dataInicio,
                                        dataFim);
                } else if ("PP".equals(tipoPiezometro)) {
                        return relatorioNivelEstaticoRepository.findDadosPiezometroComumComFiltro(cdPiezometro,
                                        dataInicio,
                                        dataFim);
                } else {
                        throw new IllegalArgumentException("Tipo de piezômetro não suportado: " + tipoPiezometro);
                }
        }

        public Map<String, Object> getDadosPiezometroComFiltroEHistorico(Integer cdPiezometro, String mesAnoInicio,
                        String mesAnoFim) {
                // Verifica o tipo do piezômetro
                String tipoPiezometro = piezometroRepository.findTipoPiezometroById(cdPiezometro);

                String dataInicio = (mesAnoInicio != null && !mesAnoInicio.isEmpty()) ? "01/" + mesAnoInicio
                                : "01/01/1900";
                String dataFim = (mesAnoFim != null && !mesAnoFim.isEmpty()) ? "01/" + mesAnoFim : "01/01/2100";

                String dataInicioHistorico = "01/01/1900";
                String dataFimHistorico = "01/01/2100";

                List<Map<String, Object>> dadosFiltrados;
                List<Map<String, Object>> historicoCompleto;

                if ("PR".equals(tipoPiezometro)) {
                        dadosFiltrados = relatorioNivelEstaticoRepository.findDadosReguaComFiltro(cdPiezometro,
                                        dataInicio,
                                        dataFim);
                        historicoCompleto = relatorioNivelEstaticoRepository.findDadosReguaComFiltro(cdPiezometro,
                                        dataInicioHistorico,
                                        dataFimHistorico);
                } else if ("PC".equals(tipoPiezometro) || "PV".equals(tipoPiezometro)) {
                        dadosFiltrados = relatorioNivelEstaticoRepository.findDadosRecursosHidricosComFiltro(
                                        cdPiezometro,
                                        dataInicio,
                                        dataFim);
                        historicoCompleto = relatorioNivelEstaticoRepository.findDadosRecursosHidricosComFiltro(
                                        cdPiezometro,
                                        dataInicioHistorico, dataFimHistorico);
                } else if ("PP".equals(tipoPiezometro)) {
                        dadosFiltrados = relatorioNivelEstaticoRepository.findDadosPiezometroComumComFiltro(
                                        cdPiezometro,
                                        dataInicio,
                                        dataFim);
                        historicoCompleto = relatorioNivelEstaticoRepository.findDadosPiezometroComumComFiltro(
                                        cdPiezometro,
                                        dataInicioHistorico, dataFimHistorico);
                } else {
                        throw new IllegalArgumentException("Tipo de piezômetro não suportado: " + tipoPiezometro);
                }

                Map<String, Object> response = new HashMap<>();
                response.put("dadosFiltrados", dadosFiltrados);
                response.put("historicoCompleto", historicoCompleto);

                return response;
        }

        public Map<String, Object> getDadosPiezometroDiario(Integer cdPiezometro, String dataInicio, String dataFim) {
                // Verifica o tipo do piezômetro
                String tipoPiezometro = piezometroRepository.findTipoPiezometroById(cdPiezometro);

                if (dataInicio == null || dataInicio.isEmpty()) {
                        dataInicio = "01/01/1900";
                }
                if (dataFim == null || dataFim.isEmpty()) {
                        dataFim = "31/12/2100";
                }

                String dataInicioHistorico = "01/01/1900";
                String dataFimHistorico = "31/12/2100";

                Map<String, Object> response = new HashMap<>();

                if ("PP".equals(tipoPiezometro)) {
                        response.put("dadosFiltrados", fetchDadosDiarios(cdPiezometro, dataInicio, dataFim));
                        response.put("historicoCompleto",
                                        fetchDadosDiarios(cdPiezometro, dataInicioHistorico, dataFimHistorico));
                } else if ("PR".equals(tipoPiezometro)) {
                        response.put("dadosFiltrados", fetchDadosDiariosRegua(cdPiezometro, dataInicio, dataFim));
                        response.put("historicoCompleto",
                                        fetchDadosDiariosRegua(cdPiezometro, dataInicioHistorico, dataFimHistorico));
                } else if ("PC".equals(tipoPiezometro) || "PV".equals(tipoPiezometro)) {
                        response.put("mensagem", "Esse piezômetro é do tipo " + tipoPiezometro);
                } else {
                        throw new IllegalArgumentException("Tipo de piezômetro não suportado: " + tipoPiezometro);
                }

                return response;
        }

        private Map<String, Object> fetchDadosDiarios(Integer cdPiezometro, String dataInicio, String dataFim) {
                Map<String, Object> cotas = relatorioNivelEstaticoRepository.findCotasPiezometro(cdPiezometro);
                List<Map<String, Object>> precipitacao = relatorioNivelEstaticoRepository.findPrecipitacaoDiaria(
                                dataInicio,
                                dataFim);
                List<Map<String, Object>> vazao = relatorioNivelEstaticoRepository.findVazaoDiaria(dataInicio, dataFim);
                List<Map<String, Object>> nivelEstatico = relatorioNivelEstaticoRepository.findNivelEstaticoDiario(
                                cdPiezometro,
                                dataInicio, dataFim);

                Map<String, Object> result = new HashMap<>();
                if (cotas != null) {
                        result.put("cota_superficie", cotas.get("cota_superficie"));
                        result.put("cota_base", cotas.get("cota_base"));
                        result.put("cota_boca", cotas.get("cota_boca"));
                } else {
                        result.put("cota_superficie", null);
                        result.put("cota_base", null);
                        result.put("cota_boca", null);
                }
                result.put("precipitacao", precipitacao);
                result.put("vazao_bombeamento", vazao);
                result.put("nivel_estatico", nivelEstatico);

                return result;
        }

        private Map<String, Object> fetchDadosDiariosRegua(Integer cdPiezometro, String dataInicio, String dataFim) {
                Map<String, Object> cotas = relatorioNivelEstaticoRepository.findCotaSuperficieRegua(cdPiezometro);
                List<Map<String, Object>> precipitacao = relatorioNivelEstaticoRepository.findPrecipitacaoDiaria(
                                dataInicio,
                                dataFim);
                List<Map<String, Object>> vazao = relatorioNivelEstaticoRepository.findVazaoDiaria(dataInicio, dataFim);
                List<Map<String, Object>> nivelEstatico = relatorioNivelEstaticoRepository.findNivelEstaticoReguaDiario(
                                cdPiezometro,
                                dataInicio, dataFim);

                Map<String, Object> result = new HashMap<>();
                if (cotas != null) {
                        result.put("cota_superficie", cotas.get("cota_superficie"));
                } else {
                        result.put("cota_superficie", null);
                }
                result.put("cota_base", null);
                result.put("precipitacao", precipitacao);
                result.put("vazao_bombeamento", vazao);
                result.put("nivel_estatico", nivelEstatico);

                return result;
        }
}
