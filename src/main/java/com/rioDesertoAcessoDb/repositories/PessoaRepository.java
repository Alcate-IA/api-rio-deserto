package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PessoaRepository {

    private final JdbcTemplate jdbcTemplate;

    private Pessoa map(ResultSet rs, int rowNum) throws SQLException {
        return Pessoa.builder()
                .codigoPessoa(getLongSafe(rs, "CD_PESSOA"))
                .codigoPessoaOld(getLongSafe(rs, "CD_PESSOA_OLD"))
                .nome(rs.getString("NM_PESSOA"))
                .nomeFantasia(rs.getString("NM_FANTASIA"))
                .cpf(rs.getString("NR_CPF"))
                .cnpj(rs.getString("NR_CNPJ"))
                .rg(rs.getString("NR_RG"))
                .crm(rs.getString("NR_CRM"))
                .cep(rs.getString("NR_CEP"))
                .cepFaturamento(rs.getString("NR_CEP_FAT"))
                .bairro(rs.getString("NM_BAIRRO"))
                .bairroFaturamento(rs.getString("NM_BAIRRO_FAT"))
                .endereco(rs.getString("DS_ENDERECO"))
                .enderecoFaturamento(rs.getString("DS_ENDERECO_FAT"))
                .complemento(rs.getString("DS_COMPLEMENTO"))
                .complementoFaturamento(rs.getString("DS_COMPLEMENTO_FAT"))
                .longitude(getDoubleSafe(rs, "PO_LONGITUDE"))
                .latitude(getDoubleSafe(rs, "PO_LATITUDE"))
                .situacao(rs.getString("FG_SITUACAO"))
                .pessoaTipo(rs.getString("FG_FISICA_JURIDICA"))
                .dataNascimento(getDateSafe(rs, "DT_NASCIMENTO"))
                .dataAlteracao(getDateSafe(rs, "DT_ALTERACAO"))
                .dataInclusao(getDateSafe(rs, "DT_INCLUSAO"))
                .dataUltimaRevisao(getDateSafe(rs, "DT_ULTIMA_REVISAO"))
                .cidade(getLongSafe(rs, "CD_CIDADE"))
                .cidadeFaturamento(getLongSafe(rs, "CD_CIDADE_FAT"))
                .build();
    }

    public List<Pessoa> findAll() {
        return jdbcTemplate.query("SELECT * FROM TB_PESSOA", this::map);
    }

    public long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TB_PESSOA", Long.class);
    }

    public List<Pessoa> findPaginated(int page, int size) {
        int offset = page * size;
        int start = offset + 1;
        int end = offset + size;

        String sql = """
            SELECT * FROM TB_PESSOA
            ROWS ? TO ?
        """;

        return jdbcTemplate.query(sql, this::map, start, end);
    }

    public Pessoa findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM TB_PESSOA WHERE CD_PESSOA = ?",
                this::map,
                id
        );
    }

    public Pessoa findByCpf(String cpf) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM TB_PESSOA WHERE NR_CPF = ?",
                this::map,
                cpf
        );
    }

    public Pessoa findByCnpj(String cnpj) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM TB_PESSOA WHERE NR_CNPJ = ?",
                this::map,
                cnpj
        );
    }

    private Double getDoubleSafe(ResultSet rs, String column) throws SQLException {
        String value = rs.getString(column);
        if (value == null || value.trim().isEmpty()) return null;

        value = value.trim().replace(",", ".");

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long getLongSafe(ResultSet rs, String column) throws SQLException {
        String val = rs.getString(column);
        if (val == null || val.trim().isEmpty()) return null;

        try {
            return Long.parseLong(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate getDateSafe(ResultSet rs, String column) throws SQLException {
        return rs.getDate(column) != null ? rs.getDate(column).toLocalDate() : null;
    }
}
