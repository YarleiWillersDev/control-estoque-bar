package br.com.barghesla.barestoque.controller.usuario;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import br.com.barghesla.barestoque.controller.BaseIntegrationTest;
import br.com.barghesla.barestoque.dto.usuario.UsuarioRequest;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Usuario;

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

    @Nested
    @DisplayName("Teste para testar Atualização de Usuario (PUT /{id})")
    class AtualizarUsuarioTest {

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus200AoAtualizarUsuarioComDadosValidos() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    "TralaleroTralala123",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.nome").value("Yarlei Rafael Willers"))
                    .andExpect(jsonPath("$.email").value("yarleiwillers@email"));
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComNomeVazio() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "",
                    "yarleiwillers@email",
                    "TralaleroTralala123",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComNomeNull() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    null,
                    "yarleiwillers@email",
                    "TralaleroTralala123",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComEmailVazio() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "",
                    "TralaleroTralala123",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComEmailNull() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    null,
                    "TralaleroTralala123",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComEmailInvalido() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarlinhogames123",
                    "TralaleroTralala123",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComSenhaVazia() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    "",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComSenhaNull() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    null,
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComSenhaInvalida() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    "1234567",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus400AoAtualizarUsuarioComPerfilNull() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    "12345678",
                    null);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus403AoAtualizarUsuarioComPerfilSemPrmissao() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    "12345678",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isForbidden());

        }

        @Test
        void deveRetornarStatus403AoAtualizarUsuarioComUsuarioNaoAutenticado() throws Exception {
            Usuario usuario = criarUsuarioGerenteParaTeste();

            long usuarioId = usuario.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    "12345678",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isForbidden());

        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus404AoAtualizarUsuarioComIdInexistentee() throws Exception {
            criarUsuarioGerenteParaTeste();

            long usuarioId = 999L;

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "yarleiwillers@email",
                    "12345678",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isNotFound());       
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus409AoAtualizarUsuarioComEmailJaCadastradoParaOutroUsuario() throws Exception {
            Usuario usuarioAtualizacao = criarUsuarioGerenteParaTeste(); // -> email = yarlei@email
                                         criarUsuarioVendedorParaTeste(); // -> email = gabriel@email

            long usuarioId = usuarioAtualizacao.getId();

            UsuarioRequest usuarioRequest = new UsuarioRequest(
                    "Yarlei Rafael Willers",
                    "gabriel@email",
                    "TralaleroTralala123",
                    Perfil.VENDEDOR);

            String requestBodyJson = objectMapper.writeValueAsString(usuarioRequest);

            mockMvc.perform(put("/usuarios/{id}", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("Teste para testar Exclusão de Usuario (DELETE {id})")
    class DeletarUsuarioTest {

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus204AoDeletarUsuarioComSucesso() throws Exception {
                Usuario usuario = criarUsuarioGerenteParaTeste();

                long usuarioId = usuario.getId();

                mockMvc.perform(delete("/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNoContent());

        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus403AoDeletarUsuarioComPerfilSemPermissao() throws Exception {
                Usuario usuario = criarUsuarioGerenteParaTeste();

                long usuarioId = usuario.getId();

                mockMvc.perform(delete("/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isForbidden());
        }

        @Test
        void deveRetornarStatus403AoDeletarUsuarioComUsuarioNaoAutenticado() throws Exception {
                Usuario usuario = criarUsuarioGerenteParaTeste();

                long usuarioId = usuario.getId();

                mockMvc.perform(delete("/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "GERENTE")
        void deveRetornarStatus404AoDeletarUsuarioComIdNaoCadastrado() throws Exception {
                criarUsuarioGerenteParaTeste();

                long usuarioId = 999L;

                mockMvc.perform(delete("/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Teste para testar Busca de Usuario pelo nome (GET /buscar)")
    class BuscarPorNomeUsuarioTest {

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200AoBuscarUsuarioPeloNome() throws Exception {
                Usuario usuario = criarUsuarioGerenteParaTeste();

                mockMvc.perform(get("/usuarios/buscar")
                        .param("nome", usuario.getNome())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].id").value(usuario.getId()))
                        .andExpect(jsonPath("$[0].nome").value(usuario.getNome()));
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200AoBuscarUsuarioComParteDoNome() throws Exception {
                Usuario usuario = criarUsuarioGerenteParaTeste();

                String parteDoNome = "Yar";

                mockMvc.perform(get("/usuarios/buscar")
                        .param("nome", parteDoNome)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].id").value(usuario.getId()))
                        .andExpect(jsonPath("$[0].nome").value(usuario.getNome()));
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200ComListaVaziaAoBuscarUsuariosNaoCadastrados() throws Exception {
                String nome = "Yarlei";

                mockMvc.perform(get("/usuarios/buscar")
                        .param("nome", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().json("[]"));
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus400AoBuscarUsuarioComNomeVazio() throws Exception {
                criarUsuarioGerenteParaTeste();

                String nome = "";

                mockMvc.perform(get("/usuarios/buscar")
                        .param("nome", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus400AoBuscarUsuarioComNomeNull() throws Exception {
                criarUsuarioGerenteParaTeste();

                String nome = null;

                mockMvc.perform(get("/usuarios/buscar")
                        .param("nome", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
        }

        @Test
        void deveRetornarStatus403AoBuscarUsuarioPeloNomeComUsuarioNaoAutenticado() throws Exception {
                Usuario usuario = criarUsuarioGerenteParaTeste();

                mockMvc.perform(get("/usuarios/buscar")
                        .param("nome", usuario.getNome())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("Teste para testar Busca por todos os Usuários cadastrados (GET /usuarios)")
    class ListarTodosUsuariosTest {

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200AoListarTodosUsuariosCadastrados() throws Exception {
                Usuario usuario = criarUsuarioGerenteParaTeste();
                Usuario usuario2 = criarUsuarioVendedorParaTeste();

                int quantidadeEsperada = 2;

                mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(quantidadeEsperada))
                        .andExpect(jsonPath("$[*].id", containsInAnyOrder(usuario.getId().intValue(), usuario2.getId().intValue())))
                        .andExpect(jsonPath("$[*].nome", containsInAnyOrder(usuario.getNome(), usuario2.getNome())));
        }

        @Test
        @WithMockUser(roles = "VENDEDOR")
        void deveRetornarStatus200AoListarTodosUsuariosComListaVazia() throws Exception {

                mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().json("[]"));
        }

        @Test
        void deveRetornarStatus403AoListarTodosUsuariosComUsuarioNaoAutenticado() throws Exception {
                criarUsuarioGerenteParaTeste();
                criarUsuarioVendedorParaTeste();

                mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isForbidden());
        }

    }
}
