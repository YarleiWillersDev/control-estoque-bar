package br.com.barghesla.barestoque.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import br.com.barghesla.barestoque.dto.produto.ProdutoRequest;
import br.com.barghesla.barestoque.dto.produto.ProdutoUpdateStatusRequest;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.StatusProduto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class ProdutoControllerIT extends BaseIntegrationTest {

        @Nested
        @DisplayName("Testes para testar Criação de Produto (POST /produtos)")
        class CriarProdutoTest {

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus201AoCriarProdutoComDadosValidos() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja Brahma",
                                        "Duplo Malte 350ml",
                                        100,
                                        new BigDecimal("3.99"),
                                        categoria.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(post("/produtos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.id").exists())
                                        .andExpect(jsonPath("$.nome").value("Cerveja Brahma"))
                                        .andExpect(jsonPath("$.quantidade").value(100))
                                        .andExpect(jsonPath("$.categoria.id").value(categoria.getId()));
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoCriarProdutoComNomeEmBranco() throws Exception {
                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "",
                                        "Duplo Malte 350ml",
                                        125,
                                        new BigDecimal("3.99"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(post("/produtos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoCriarProdutoComQuantidadeNegativa() throws Exception {
                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja Brahma",
                                        "Duplo Malte 350ml",
                                        -1,
                                        new BigDecimal("3.99"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(post("/produtos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoCriarProdutoComPrecoUnitarioNegativo() throws Exception {
                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja Brahma",
                                        "Duplo Malte 350ml",
                                        100,
                                        new BigDecimal("-3.90"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(post("/produtos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoCriarProdutoComCategoriaIdNula() throws Exception {
                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja Brahma",
                                        "Duplo Malte 350ml",
                                        100,
                                        new BigDecimal("3.90"),
                                        null);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(post("/produtos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus403AoCriarProdutoComUsuarioSemPermissao() throws Exception {
                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja Brahma",
                                        "Duplo Malte 350ml",
                                        100,
                                        new BigDecimal("3.90"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(post("/produtos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isForbidden());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus404AoCriarProdutoComCategoriaIdInexistenteNoBanco() throws Exception {

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja Brahma",
                                        "Duplo Malte 350ml",
                                        100,
                                        new BigDecimal("3.90"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(post("/produtos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isNotFound());
                }
        }

        @Nested
        @DisplayName("Testes para testar Atualização de Produtos (PUT /{id})")
        class AtualizarProdutoTest {

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus200AoAtualizarProdutoComDadosValidos() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criarProdutoParaTeste();

                        long produtoId = produto.getId();

                        ProdutoRequest novoProdutoRequest = new ProdutoRequest(
                                        "Cerveja IPA",
                                        "Schin 600ml",
                                        0,
                                        new BigDecimal("10.50"),
                                        categoria.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(novoProdutoRequest);

                        mockMvc.perform(put("/produtos/{id}", produtoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.id").exists())
                                        .andExpect(jsonPath("$.nome").value("Cerveja IPA"))
                                        .andExpect(jsonPath("$.quantidade").value(0))
                                        .andExpect(jsonPath("$.precoUnitario").value("10.5"))
                                        .andExpect(jsonPath("$.categoria.id").value(categoria.getId()));

                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoAtualizarProdutoComNomeVazio() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        long produtoId = produto.getId();

                        ProdutoRequest novProdutoRequest = new ProdutoRequest(
                                        "",
                                        "Brahma 600ml",
                                        0,
                                        new BigDecimal("10.50"),
                                        1L);

                        String requesBodyJson = objectMapper.writeValueAsString(novProdutoRequest);

                        mockMvc.perform(put("/produtos/{id}", produtoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requesBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoAtualizarProdutoComQuantidadeNegativa() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        long produtoId = produto.getId();

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja Pilsen",
                                        "Brahma 600ml",
                                        -1,
                                        new BigDecimal("10.50"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(put("/produtos/{id}", produtoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoAtualizarProdutoComPrecoNegativo() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        long produtoId = produto.getId();

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja IPA",
                                        "Brahma 600ml",
                                        0,
                                        new BigDecimal("-10.50"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(put("/produtos/{id}", produtoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());

                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoAtualizarProdutoComCategoriaNula() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        long protudoId = produto.getId();

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja IPA",
                                        "Brahma 600ml",
                                        0,
                                        new BigDecimal("10.50"),
                                        null);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(put("/produtos/{id}", protudoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus403AoAtualizarProdutoComUsuarioSemPermissao() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criarProdutoParaTeste();

                        long produtoId = produto.getId();

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja IPA",
                                        "Brahma 600ml",
                                        0,
                                        new BigDecimal("10.50"),
                                        categoria.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(put("/produtos/{id}", produtoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isForbidden());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus404AoAtualizarProdutoComCategoriaNaoCadastradaNoBanco() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        long produtoId = produto.getId();

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja IPA",
                                        "Brahma 600ml",
                                        0,
                                        new BigDecimal("10.50"),
                                        1L);

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(put("/produtos/{id}", produtoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isNotFound());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus404AoAtualizarProdutoComIdNaoCadastradoNoBanco() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();

                        ProdutoRequest produtoRequest = new ProdutoRequest(
                                        "Cerveja IPA",
                                        "Brahma 600ml",
                                        0,
                                        new BigDecimal("10.50"),
                                        categoria.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(produtoRequest);

                        mockMvc.perform(put("/produtos/{id}", 999L)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isNotFound());
                }
        }

        @Nested
        @DisplayName("Testes para testar Atualização de Status de Produtos (PATCH /{id})")
        class AtualizarStatusProdutoTest {

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus200AoAtualizarStatusProdutoComSucesso() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        ProdutoUpdateStatusRequest statusRequest = new ProdutoUpdateStatusRequest(
                                        StatusProduto.INATIVO.name());

                        String requestBodyJson = objectMapper.writeValueAsString(statusRequest);

                        mockMvc.perform(patch("/produtos/{id}/status", produto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isOk());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoAtualizarStatusProdutoSemAlterarStatusAtivar() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        ProdutoUpdateStatusRequest statusRequest = new ProdutoUpdateStatusRequest(
                                        StatusProduto.ATIVO.name());
                        String requestBodyJson = objectMapper.writeValueAsString(statusRequest);

                        mockMvc.perform(patch("/produtos/{id}/status", produto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoAtualizarStatusProdutoSemAlterarStatusInativar() throws Exception {
                        Produto produto = criarProdutoParaTeste();
                        produto.setStatus(StatusProduto.INATIVO);
                        produtoRepository.save(produto);

                        ProdutoUpdateStatusRequest statusRequest = new ProdutoUpdateStatusRequest(
                                        StatusProduto.INATIVO.name());
                        String requestBodyJson = objectMapper.writeValueAsString(statusRequest);

                        mockMvc.perform(patch("/produtos/{id}/status", produto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus400AoAtualizarStatusProdutoComStatusInvalido() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        ProdutoUpdateStatusRequest requestStatus = new ProdutoUpdateStatusRequest("SEM ATIVACAO");
                        String requestBodyJson = objectMapper.writeValueAsString(requestStatus);

                        mockMvc.perform(patch("/produtos/{id}/status", produto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus403AoAtualizarStatusProdutoComUsuarioSemPermissao() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        ProdutoUpdateStatusRequest requestStatus = new ProdutoUpdateStatusRequest(
                                        StatusProduto.INATIVO.name());
                        String requestBodyJson = objectMapper.writeValueAsString(requestStatus);

                        mockMvc.perform(patch("/produtos/{id}/status", produto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isForbidden());
                }

                @Test
                @WithMockUser(roles = "GERENTE")
                void deveRetornarStatus404AoAtualizarStatusDeProdutoInexistente() throws Exception {

                        long id = 999L;

                        ProdutoUpdateStatusRequest requestStatus = new ProdutoUpdateStatusRequest(
                                        StatusProduto.INATIVO.name());
                        String requestBodyJson = objectMapper.writeValueAsString(requestStatus);

                        mockMvc.perform(patch("/produtos/{id}/status", id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isNotFound());
                }
        }

        @Nested
        @DisplayName("Testes para testar Busca pelo id dos Produtos (GET /{id})")
        class BuscarPorIdProdutoTest {

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus200AoBuscarProdutoComIdCadastradoNoBanco() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        mockMvc.perform(get("/produtos/{id}", produto.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isOk());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetonarStatus400AoBuscarProdutoComIdNegativoOuZerado() throws Exception {
                        long idZerado = 0;

                        mockMvc.perform(get("/produtos/{id}", idZerado)
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus404AoBuscarProdutoComIdNaoCadastradoNoBanco() throws Exception {
                        long idNaoExistente = 1;

                        mockMvc.perform(get("/produtos/{id}", idNaoExistente)
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isNotFound());
                }

        }

        @Nested
        @DisplayName("Testes para testar Busca pelo nome dos Produtos (GET /buscar)")
        class BuscarPeloNomeProdutoTest {

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus200AoBuscarProdutoPeloNomeCadastrado() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        mockMvc.perform(get("/produtos/buscar")
                                        .param("nome", produto.getNome())
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$[0].id").value(produto.getId()))
                                        .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus200AoBuscarProdutoPeloNomeCadastradoPassandoParteDoNome() throws Exception {
                        Produto produto = criarProdutoParaTeste();

                        String parteDoNome = "Cer";

                        mockMvc.perform(get("/produtos/buscar")
                                        .param("nome", parteDoNome)
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$[0].id").value(produto.getId()))
                                        .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus200ComListaVaziaAoBuscaProdutoPorNomeInexistente() throws Exception {
                        String nome = "Cerveja";

                        mockMvc.perform(get("/produtos/buscar")
                                        .param("nome", nome)
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isOk());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoBuscarPorNomeVazio() throws Exception {
                        String nome = "";

                        mockMvc.perform(get("/produtos/buscar")
                                        .param("nome", nome)
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

        }

}
