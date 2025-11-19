package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.dtos.PageResponse;
import com.rioDesertoAcessoDb.model.Pessoa;
import com.rioDesertoAcessoDb.repositories.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository repo;

    public PageResponse<Pessoa> listar(Integer page, Integer size) {
        if (page == null || size == null) {
            List<Pessoa> todas = repo.findAll();
            long total = todas.size();
            return new PageResponse<>(0, (int) total, total, 1, todas);
        }

        long total = repo.count();
        List<Pessoa> items = repo.findPaginated(page, size);
        long totalPages = (long) Math.ceil((double) total / size);

        return new PageResponse<>(page, size, total, totalPages, items);
    }

    public Pessoa buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Pessoa buscarPorCpf(String cpf) {
        return repo.findByCpf(cpf);
    }

    public Pessoa buscarPorCnpj(String cnpj) {
        return repo.findByCnpj(cnpj);
    }
}
