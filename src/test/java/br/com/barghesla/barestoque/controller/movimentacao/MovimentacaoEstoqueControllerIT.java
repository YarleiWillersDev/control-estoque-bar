package br.com.barghesla.barestoque.controller.movimentacao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import br.com.barghesla.barestoque.controller.BaseIntegrationTest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueUpdateQuantidadeRequest;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.MovimentacaoEstoque;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.Usuario;

public class MovimentacaoEstoqueControllerIT extends BaseIntegrationTest {

        @Nested
        @DisplayName("Teste para testar Criação de Movimentação de Estoque (POST /movimentacoes)")
        class CriarMovimentacaoEstoqueTest {

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus201AoCriarMovimentacaoEstoqueEntradaComDadosCorretos() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        50,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.id").exists())
                                        .andExpect(jsonPath("$.tipo").value("ENTRADA"))
                                        .andExpect(jsonPath("$.quantidade").value(50))
                                        .andExpect(jsonPath("$.produto.id").value(produto.getId()))
                                        .andExpect(jsonPath("$.usuario.id").value(usuario.getId()));
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus201AoCriarMovimentacaoEstoqueSaidaComDadosCorretos() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "SAIDA",
                                        10,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.id").exists())
                                        .andExpect(jsonPath("$.tipo").value("SAIDA"))
                                        .andExpect(jsonPath("$.quantidade").value(10))
                                        .andExpect(jsonPath("$.produto.id").value(produto.getId()))
                                        .andExpect(jsonPath("$.usuario.id").value(usuario.getId()));
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComQuantidadeDeSaidaMaiorQueQuantidadeProduto()
                                throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "SAIDA",
                                        999,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComTipoVazio() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "",
                                        50,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComTipoNull() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        null,
                                        50,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComQuantidadeNegativa() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        -1,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComQuantidadeZerada() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        0,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComQuantidadeNull() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        null,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComProdutoIdNull() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        50,
                                        null,
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComProdutoIdNaoCadastrado() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        Produto produto = new Produto();
                        produto.setNome("Cerveja Pilsen");
                        produto.setDescricao("Schin 600ml");
                        produto.setQuantidade(100);
                        produto.setPrecoUnitario(new BigDecimal("8.59"));
                        produto.setCategoria(categoria);

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        50,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComUsuarioIdNull() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        50,
                                        produto.getId(),
                                        null);

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoCriarMovimentacaoEstoqueComUsuarioIdNaoCadastrado() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);

                        Usuario usuario = new Usuario();
                        usuario.setNome("Gabriel");
                        usuario.setEmail("gabriel@email");
                        usuario.setSenha("Antena2000ACG");
                        usuario.setPerfil(Perfil.VENDEDOR);

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        50,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                void deveRetornarStatus403AoCriarMovimentacaoEstoqueComUsuarioNaoAutenticado() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoqueRequest movimentacaoEstoqueRequest = new MovimentacaoEstoqueRequest(
                                        "ENTRADA",
                                        50,
                                        produto.getId(),
                                        usuario.getId());

                        String requestBodyJson = objectMapper.writeValueAsString(movimentacaoEstoqueRequest);

                        mockMvc.perform(post("/movimentacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isForbidden());

                }
        }

        @Nested
        @DisplayName("Teste para testar Atualização de Movimentacao de Estoque (POST /{id})")
        class AtualizarMovimentacaoEstoqueTest {

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus200AoAtualizarMovimentacaoDeEstoqueComDadosValidos() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoque movimentacaoEstoque = criarMovimentacaoEstoqueTeste(produto, usuario);
                        long movimentacaoId = movimentacaoEstoque.getId();

                        MovimentacaoEstoqueUpdateQuantidadeRequest request = new MovimentacaoEstoqueUpdateQuantidadeRequest(
                                        150);

                        String requestBodyJson = objectMapper.writeValueAsString(request);

                        mockMvc.perform(patch("/movimentacoes/{id}", movimentacaoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.id").exists())
                                        .andExpect(jsonPath("$.quantidade").value(150));
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoTentarAtualizarMovimentacaoEstoqueComQuantidadeZerada() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoque movimentacaoEstoque = criarMovimentacaoEstoqueTeste(produto, usuario);
                        long movimentacaoId = movimentacaoEstoque.getId();

                        MovimentacaoEstoqueUpdateQuantidadeRequest request = new MovimentacaoEstoqueUpdateQuantidadeRequest(
                                        0);

                        String requestBodyJson = objectMapper.writeValueAsString(request);

                        mockMvc.perform(patch("/movimentacoes/{id}", movimentacaoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoTentarAtualizarMovimentacaoEstoqueComQuantidadeNegativa() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoque movimentacaoEstoque = criarMovimentacaoEstoqueTeste(produto, usuario);
                        long movimentacaoId = movimentacaoEstoque.getId();

                        MovimentacaoEstoqueUpdateQuantidadeRequest request = new MovimentacaoEstoqueUpdateQuantidadeRequest(
                                        -1);

                        String requestBodyJson = objectMapper.writeValueAsString(request);

                        mockMvc.perform(patch("/movimentacoes/{id}", movimentacaoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus400AoTentarAtualizarMovimentacaoEstoqueComQuantidadeNull() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoque movimentacaoEstoque = criarMovimentacaoEstoqueTeste(produto, usuario);
                        long movimentacaoId = movimentacaoEstoque.getId();

                        MovimentacaoEstoqueUpdateQuantidadeRequest request = new MovimentacaoEstoqueUpdateQuantidadeRequest(
                                        null);

                        String requestBodyJson = objectMapper.writeValueAsString(request);

                        mockMvc.perform(patch("/movimentacoes/{id}", movimentacaoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
                }

                @Test
                void deveRetornarStatus403AoAtualizarMovimentacaoEstoqueComUsuarioNaoAutenticado() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        MovimentacaoEstoque movimentacaoEstoque = criarMovimentacaoEstoqueTeste(produto, usuario);
                        long movimentacaoId = movimentacaoEstoque.getId();

                        MovimentacaoEstoqueUpdateQuantidadeRequest request = new MovimentacaoEstoqueUpdateQuantidadeRequest(
                                        150);

                        String requestBodyJson = objectMapper.writeValueAsString(request);

                        mockMvc.perform(patch("/movimentacoes/{id}", movimentacaoId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isForbidden());
                }

                @Test
                @WithMockUser(roles = "VENDEDOR")
                void deveRetornarStatus404AoAtualizarMovimentacaoEstoqueComIdNaoCadastrado() throws Exception {
                        Categoria categoria = criarCategoriaParaTeste();
                        Produto produto = criaProdutoParaTeste(categoria);
                        Usuario usuario = criarUsuarioVendedorParaTeste();

                        criarMovimentacaoEstoqueTeste(produto, usuario);

                        MovimentacaoEstoqueUpdateQuantidadeRequest request = new MovimentacaoEstoqueUpdateQuantidadeRequest(
                                        150);

                        String requestBodyJson = objectMapper.writeValueAsString(request);

                        mockMvc.perform(patch("/movimentacoes/{id}", 999L)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestBodyJson))
                                        .andDo(print())
                                        .andExpect(status().isNotFound());
                }
        }
}
