package br.com.barghesla.barestoque.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.barghesla.barestoque.dto.usuario.AuthenticationRequest;
import br.com.barghesla.barestoque.dto.usuario.AuthenticationResponse;
import br.com.barghesla.barestoque.model.Usuario;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody @Valid AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var auth = this.manager.authenticate(authRequest);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
