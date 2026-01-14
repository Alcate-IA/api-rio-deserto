package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.dtos.DelayedPiezometerDTO;
import com.rioDesertoAcessoDb.repositories.InspecaoPiezometroFreqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InspecaoPiezometroFreqService {

    @Autowired
    private InspecaoPiezometroFreqRepository repository;

    public List<DelayedPiezometerDTO> getPiezometrosAtrasados(Integer cdEmpresa) {
        List<Map<String, Object>> data = repository.findPiezometrosWithLatestInspection(cdEmpresa);
        List<DelayedPiezometerDTO> result = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();

        for (Map<String, Object> row : data) {
            Integer cdPiezometro = (Integer) row.get("cd_piezometro");
            String nmPiezometro = row.get("nm_piezometro") != null ? row.get("nm_piezometro").toString() : null;
            Object tpFrequenciaObj = row.get("tp_frequencia_new");
            String tpFrequencia = tpFrequenciaObj != null ? tpFrequenciaObj.toString() : null;
            Object dtUltimaObj = row.get("dt_ultima_inspecao");
            String origem = row.get("origem") != null ? row.get("origem").toString() : null;
            LocalDateTime dtUltima = convertToLocalDateTime(dtUltimaObj);

            if (tpFrequencia == null) {
                // Se não tem freq, vamos pular por enquanto ou definir como "NÃO DEFINIDA"
                // Mas o usuário quer ver os piezometros, então vamos adicionar com freq não
                // definida
                tpFrequencia = "X";
            }

            int diasLimite = getDiasPorFrequencia(tpFrequencia);
            String descFrequencia = getDescricaoFrequencia(tpFrequencia);

            long diasDesdeUltima = (dtUltima != null) ? ChronoUnit.DAYS.between(dtUltima, agora) : Long.MAX_VALUE;

            String situacao;
            long diasAtrasado = 0;

            if (dtUltima == null || diasDesdeUltima > diasLimite) {
                situacao = "atrasado";
                diasAtrasado = (dtUltima == null) ? 0 : diasDesdeUltima - diasLimite;
            } else {
                situacao = "em dia";
            }

            result.add(new DelayedPiezometerDTO(cdPiezometro, nmPiezometro, descFrequencia, dtUltima, origem, situacao,
                    diasAtrasado));
        }

        return result;
    }

    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) obj).toLocalDateTime();
        } else if (obj instanceof java.sql.Date) {
            return ((java.sql.Date) obj).toLocalDate().atStartOfDay();
        } else if (obj instanceof java.util.Date) {
            return ((java.util.Date) obj).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        } else if (obj instanceof LocalDateTime) {
            return (LocalDateTime) obj;
        }
        return null;
    }

    private int getDiasPorFrequencia(String tp) {
        return switch (tp) {
            case "D" -> 1;
            case "S" -> 7;
            case "Q" -> 15;
            case "M" -> 30;
            case "B" -> 60;
            case "R" -> 90;
            case "T" -> 150;
            case "A" -> 365;
            default -> 0;
        };
    }

    private String getDescricaoFrequencia(String tp) {
        return switch (tp) {
            case "D" -> "DIARIA";
            case "S" -> "SEMANAL";
            case "Q" -> "QUINZENAL";
            case "M" -> "MENSAL";
            case "B" -> "BIMESTRAL";
            case "R" -> "TRIMESTRAL";
            case "T" -> "SEMESTRAL";
            case "A" -> "ANUAL";
            default -> "NÃO DEFINIDA";
        };
    }
}
