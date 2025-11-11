package br.com.barghesla.barestoque.controller.usuario;

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
import br.com.barghesla.barestoque.dto.usuario.UsuarioRequest;
import br.com.barghesla.barestoque.model.Perfil;

public class UsuarioControllerIT extends BaseIntegrationTest {

    @Nested
    @DisplayName("Testes para testar Criação de Usuario (POST /usuarios)")
    class CriarUsuarioTest {

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus201AoCriarUsuarioComDadosValidos() throws Exception {

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "gabriel@email",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.nome").value("Gabriel"))
                    .andExpect(jsonPath("$.email").value("gabriel@email"));
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComNomeVazio() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "",
                    "gabriel@email",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComNomeNull() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    null,
                    "gabriel@email",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);

            String requesBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requesBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComEmailVazio() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);

            String requesBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requesBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.email").value("O email não pode ser vazio"));
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComEmailNull() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    null,
                    "Antena2000ACG",
                    Perfil.VENDEDOR);

            String requesBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requesBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComEmailInvalido() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "yarlei250",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComSenhaVazia() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "gabriel@email",
                    "",
                    Perfil.VENDEDOR);

            String requesBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requesBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComSenhaNull() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "gabriel@email",
                    null,
                    Perfil.VENDEDOR);

            String requesBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requesBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComSenhaInvalida() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "gabriel@email",
                    "1234567",
                    Perfil.VENDEDOR);
            
            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());   
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoCriarUsuarioComPerfilNull() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "gabriel@email",
                    "antena2000acg",
                    null);

            String requesBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requesBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus403AoCriarUsuarioComPerfilNaoAutorizado() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "gabriel@email",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);
            
            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        void deveRetornarStatus403AoCriarUsuarioComUsuarioNaoAutenticado() throws Exception {
            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "gabriel@email",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);
            
            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus409AoCriarUsuarioComEmailJaCadastradoParaOutroUsuario() throws Exception {
            criarUsuarioGerenteParaTeste();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Gabriel",
                    "yarlei@email",
                    "Antena2000ACG",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

}
