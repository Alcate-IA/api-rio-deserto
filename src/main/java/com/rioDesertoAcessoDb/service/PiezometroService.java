package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.model.Piezometro;
import com.rioDesertoAcessoDb.repositories.PiezometroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PiezometroService {

    @Autowired
    private PiezometroRepository piezometroRepository;

    public List<Piezometro> getPiezometrosComFiltros(String situacao, List<String> tipos) {
        return piezometroRepository.findAll((Specification<Piezometro>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Sempre filtrar por empresa 18 (conforme padrão do projeto)
            predicates.add(criteriaBuilder.equal(root.get("cdEmpresa"), 18));

            if (situacao != null && !situacao.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("fgSituacao"), situacao));
            }

            if (tipos != null && !tipos.isEmpty()) {
                predicates.add(root.get("tpPiezometro").in(tipos));
            }

            query.orderBy(criteriaBuilder.asc(root.get("nmPiezometro")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
