package com.rioDesertoAcessoDb.dtos;

import java.util.List;

public record PageResponse<T>(
        int page,
        int pageSize,
        long totalElements,
        long totalPages,
        List<T> items
) {}
