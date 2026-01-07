package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.model.FotoInspecao;
import com.rioDesertoAcessoDb.repositories.FotoInspecaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FotoInspecaoService {

    @Autowired
    private FotoInspecaoRepository repositorio;

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Busca todas as fotos de inspeção registradas.
     * 
     * @return Lista de todas as fotos de inspeção.
     */
    public List<FotoInspecao> buscarTodas() {
        return repositorio.findAll();
    }

    /**
     * Busca uma foto de inspeção pelo seu ID.
     * 
     * @param idFoto ID da foto a ser buscada.
     * @return A foto encontrada ou null se não existir.
     */
    public FotoInspecao buscarPorId(Integer idFoto) {
        return repositorio.findById(idFoto).orElse(null);
    }

    /**
     * Busca todas as fotos de inspeção de um piezômetro específico.
     * 
     * @param cdPiezometro Código do piezômetro.
     * @return Lista de fotos associadas ao piezômetro.
     */
    public List<FotoInspecao> buscarPorPiezometro(Integer cdPiezometro) {
        return repositorio.findByCdPiezometro(cdPiezometro);
    }

    /**
     * Busca fotos de um piezômetro em um determinado período.
     * 
     * @param cdPiezometro  Código do piezômetro.
     * @param dataInicioStr Data de início no formato dd/MM/yyyy.
     * @param dataFimStr    Data de fim no formato dd/MM/yyyy.
     * @return Lista de fotos no período.
     */
    public List<FotoInspecao> buscarPorPiezometroEPeriodo(Integer cdPiezometro, String dataInicioStr,
            String dataFimStr) {
        LocalDateTime inicio = (dataInicioStr != null && !dataInicioStr.isEmpty())
                ? LocalDate.parse(dataInicioStr, FORMATADOR).atStartOfDay()
                : LocalDateTime.of(1900, 1, 1, 0, 0);

        LocalDateTime fim = (dataFimStr != null && !dataFimStr.isEmpty())
                ? LocalDate.parse(dataFimStr, FORMATADOR).atTime(23, 59, 59)
                : LocalDateTime.of(2100, 12, 31, 23, 59);

        return repositorio.findByCdPiezometroAndDataInsercaoBetween(cdPiezometro, inicio, fim);
    }
}
