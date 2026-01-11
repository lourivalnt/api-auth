package com.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.exception.ProblemDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Administração", description = "Endpoints restritos a administradores")
public class AdminController {

    @Operation(
        summary = "Endpoint restrito a adminstradores",
        description = "Requer autenticação JWT e permissão ROLE_ADMIN"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Acesso liberado para ADMIN",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(example = "Acesso liberado para ADMIN")
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autenticado ou token inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetails.class) 
                )
            ),
        @ApiResponse(
            responseCode = "403",
            description = "Usuário autenticado sem permissão",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetails.class)
            )
        )
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Acesso liberado para ADMIN";
    }
}
