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

        String dataInicio = (mesAnoInicio != null && !mesAnoInicio.isEmpty()) ? "01/" + mesAnoInicio : "01/01/1900";
        String dataFim = (mesAnoFim != null && !mesAnoFim.isEmpty()) ? "01/" + mesAnoFim : "01/01/2100";

        if ("PR".equals(tipoPiezometro)) {
            return relatorioNivelEstaticoRepository.findDadosReguaComFiltro(cdPiezometro, dataInicio, dataFim);
        } else if ("PC".equals(tipoPiezometro) || "PV".equals(tipoPiezometro)) {
            return relatorioNivelEstaticoRepository.findDadosRecursosHidricosComFiltro(cdPiezometro, dataInicio,
                    dataFim);
        } else if ("PP".equals(tipoPiezometro)) {
            return relatorioNivelEstaticoRepository.findDadosPiezometroComumComFiltro(cdPiezometro, dataInicio,
                    dataFim);
        } else {
            throw new IllegalArgumentException("Tipo de piezômetro não suportado: " + tipoPiezometro);
        }
    }

    public Map<String, Object> getDadosPiezometroComFiltroEHistorico(Integer cdPiezometro, String mesAnoInicio,
            String mesAnoFim) {
        // Verifica o tipo do piezômetro
        String tipoPiezometro = piezometroRepository.findTipoPiezometroById(cdPiezometro);

        String dataInicio = (mesAnoInicio != null && !mesAnoInicio.isEmpty()) ? "01/" + mesAnoInicio : "01/01/1900";
        String dataFim = (mesAnoFim != null && !mesAnoFim.isEmpty()) ? "01/" + mesAnoFim : "01/01/2100";

        // Datas fixas para o histórico completo
        String dataInicioHistorico = "01/01/1900";
        String dataFimHistorico = "01/01/2100";

        List<Map<String, Object>> dadosFiltradados;
        List<Map<String, Object>> historicoCompleto;

        if ("PR".equals(tipoPiezometro)) {
            dadosFiltradados = relatorioNivelEstaticoRepository.findDadosReguaComFiltro(cdPiezometro, dataInicio,
                    dataFim);
            historicoCompleto = relatorioNivelEstaticoRepository.findDadosReguaComFiltro(cdPiezometro,
                    dataInicioHistorico,
                    dataFimHistorico);
        } else if ("PC".equals(tipoPiezometro) || "PV".equals(tipoPiezometro)) {
            dadosFiltradados = relatorioNivelEstaticoRepository.findDadosRecursosHidricosComFiltro(cdPiezometro,
                    dataInicio,
                    dataFim);
            historicoCompleto = relatorioNivelEstaticoRepository.findDadosRecursosHidricosComFiltro(cdPiezometro,
                    dataInicioHistorico, dataFimHistorico);
        } else if ("PP".equals(tipoPiezometro)) {
            dadosFiltradados = relatorioNivelEstaticoRepository.findDadosPiezometroComumComFiltro(cdPiezometro,
                    dataInicio,
                    dataFim);
            historicoCompleto = relatorioNivelEstaticoRepository.findDadosPiezometroComumComFiltro(cdPiezometro,
                    dataInicioHistorico, dataFimHistorico);
        } else {
            throw new IllegalArgumentException("Tipo de piezômetro não suportado: " + tipoPiezometro);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("dadosFiltradados", dadosFiltradados);
        response.put("historicoCompleto", historicoCompleto);

        return response;
    }
}
