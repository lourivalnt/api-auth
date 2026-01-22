package com.auth.controller;

import java.net.URI;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth.dto.request.RegisterRequest;
import com.auth.dto.response.UserResponseDTO;
import com.auth.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints relacionados ao usuário autenticado")
public class UsersController {

    private final UserService userService;  

    
    @Operation(
    summary = "Criar usuário",
    description = "Cria um novo usuário no sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            headers = {
                @io.swagger.v3.oas.annotations.headers.Header(
                    name = "Location",
                    description = "URI do recurso criado",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                        type = "string",
                        example = "/users/42"
                    )
                )
            }
        ),
    @ApiResponse(
        responseCode = "400",
        description = "Erro de validação",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class)
        )
    ),
    @ApiResponse(
        responseCode = "409",
        description = "Email já cadastrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class)
        )
    )
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody RegisterRequest request) {

        Long userId = userService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
    
    @Operation(
            summary = "Dados do usuário logado",
            description = "Retorna as informações do usuário autenticado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Dados retornados",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token inválido ou ausente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        ),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(
        summary = "Buscar usuário por ID",
        description = "Endpoint restrito a administradores"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class)
            )
        ),
          @ApiResponse(
            responseCode = "403",
            description = "Usuário autenticado sem permissão ROLE_ADMIN",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
