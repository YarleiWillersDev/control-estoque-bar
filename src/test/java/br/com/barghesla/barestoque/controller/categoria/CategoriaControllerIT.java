package br.com.barghesla.barestoque.controller.categoria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
