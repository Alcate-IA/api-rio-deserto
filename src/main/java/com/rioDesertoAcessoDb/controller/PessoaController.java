package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.dtos.PageResponse;
import com.rioDesertoAcessoDb.model.Pessoa;
import com.rioDesertoAcessoDb.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 50;

    @GetMapping
    public PageResponse<Pessoa> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        int p = page != null ? page : DEFAULT_PAGE;
        int s = size != null ? size : DEFAULT_SIZE;
        return pessoaService.listar(p, s);
    }

    @GetMapping("/{id}")
    public Pessoa buscarPorId(@PathVariable Long id) {
        return pessoaService.buscarPorId(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Pessoa buscarPorCpf(@PathVariable String cpf) {
        return pessoaService.buscarPorCpf(cpf);
    }

    @GetMapping("/cnpj/{cnpj}")
    public Pessoa buscarPorCnpj(@PathVariable String cnpj) {
        return pessoaService.buscarPorCnpj(cnpj);
    }
}
