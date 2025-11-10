package br.com.barghesla.barestoque.controller.categoria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import br.com.barghesla.barestoque.controller.BaseIntegrationTest;
import br.com.barghesla.barestoque.dto.categoria.CategoriaRequest;
import br.com.barghesla.barestoque.model.Categoria;

public class CategoriaControllerIT extends BaseIntegrationTest {

    @Nested
    @DisplayName("Testes para testar Criação de Categoria (POST /categoria)")
    class CriarCategoriaTest {

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus201AoCriarCategoriaComDadosValidos() throws Exception {
            CategoriaRequest categoriaRequest = new CategoriaRequest("Bebidas");

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(post("/categoria")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.nome").value("Bebidas"));
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarCategoriaComNomeVazio() throws Exception {
            CategoriaRequest categoriaRequest = new CategoriaRequest("");

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(post("/categoria")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarCategoriaComNomeNulo() throws Exception {
            CategoriaRequest categoriaRequest = new CategoriaRequest(null);

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(post("/categoria")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus403AoCriarCategoriaComUsuarioNaoPermitido() throws Exception {
            CategoriaRequest categoriaRequest = new CategoriaRequest("Bebidas");

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(post("/categoria")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarCategoriaComNomeDuplicado() throws Exception {
            criarCategoriaParaTeste();

            CategoriaRequest categoriaRequest = new CategoriaRequest("Bebidas");

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(post("/categoria")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isConflict());

        }
    }

    @Nested
    @DisplayName("Teste para testar a Atualização de Categoria (PUT /{id})")
    class AtualizarCategoriaTest {

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus200AoAtualizarCategoriaComDadosValidos() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            long categoriaId = categoria.getId();

            CategoriaRequest categoriaRequest = new CategoriaRequest("Comidas");

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(put("/categoria/{id}", categoriaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.nome").value("Comidas"));
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarCategoriaComNomeVazio() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            Long categoriaId = categoria.getId();

            CategoriaRequest categoriaRequest = new CategoriaRequest("");

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(put("/categoria/{id}", categoriaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.nome").exists());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarCategoriaComNomeNull() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            Long categoriaId = categoria.getId();

            CategoriaRequest categoriaRequest = new CategoriaRequest(null);

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(put("/categoria/{id}", categoriaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.nome").exists());
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus403AoAtualizarCategoriaComUsuarioSemPermissao() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            long categoriaId = categoria.getId();

            CategoriaRequest categoriaRequest = new CategoriaRequest("Comidas");

            String requestBodyJson = objectMapper.writeValueAsString(categoriaRequest);

            mockMvc.perform(put("/categoria/{id}", categoriaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus404AoAtualizarCategoriaComIdNaoCadastrado() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            long categoriaId = 999L;

            String requestBodyJson = objectMapper.writeValueAsString(categoria);

            mockMvc.perform(put("/categoria/{id}", categoriaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Teste para testar a Busca por ID de Categoria (GET /{id})")
    class BuscarPorIdCategoriaTest {

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200AoBuscarCategoriaPorId() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            mockMvc.perform(get("/categoria/{id}", categoria.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(categoria.getId()))
                    .andExpect(jsonPath("$.nome").value(categoria.getNome()));
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus400AoBuscarCategoriaPorIdInvalido() throws Exception {
            criarCategoriaParaTeste();

            String categoriaId = "abcd";

            mockMvc.perform(get("/categoria/{id}", categoriaId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void deveRetornarStatus403AoAtualizarCategoriaComUsuarioNaoAutenticado() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            mockMvc.perform(get("/categoria/{id}", categoria.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus404AoBuscarCategoriaPorIdNaoCadastrado() throws Exception {
            criarCategoriaParaTeste();

            Long categoriaId = 999L;

            mockMvc.perform(get("/categoria/{id}", categoriaId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensagem").exists());
        }

    }

    @Nested
    @DisplayName("Teste para testar a Busca por nome de Categoria (GET /buscar)")
    class BuscarPorNomeCategoriaTest {
        
        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200AoBuscarCategoriaPorNome() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            mockMvc.perform(get("/categoria/buscar")
                    .param("nome", categoria.getNome())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(categoria.getId()))
                    .andExpect(jsonPath("$[0].nome").value(categoria.getNome()));
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200AoBuscarCategoriaPorNomeComParteDoNome() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            String parteNome = "Beb";

            mockMvc.perform(get("/categoria/buscar")
                    .param("nome", parteNome)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(categoria.getId()))
                    .andExpect(jsonPath("$[0].nome").value(categoria.getNome()));
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200ComListaVaziaAoBuscarCategoriaComNomeNaoCadastrado() throws Exception {
            String nome = "Bebidas";

            mockMvc.perform(get("/categoria/buscar")
                    .param("nome", nome)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());   
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200ComListaVaziaAoBuscarCategoriaComNomeVazio() throws Exception {
            String nome = "";

            mockMvc.perform(get("/categoria/buscar")
                    .param("nome", nome)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());   
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus400AoBuscarCategoriaComNomeNull() throws Exception {
            String nome = null;

            mockMvc.perform(get("/categoria/buscar")
                    .param("nome", nome)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest()); 

        }

        @Test
        void deveRetornarStatus403AoBuscarCategoriaComUsuarioNaoAutenticado() throws Exception {
            Categoria categoria = criarCategoriaParaTeste();

            mockMvc.perform(get("/categoria/buscar")
                    .param("nome", categoria.getNome())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }
}
