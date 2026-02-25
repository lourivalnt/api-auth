// package com.auth.application.service.impl;

// import static org.mockito.Mockito.when;
// import static org.assertj.core.api.Assertions.assertThat;

// import java.util.Optional;
// import java.util.Set;
// import java.util.UUID;

// import com.auth.application.dto.request.LoginRequest;
// import com.auth.application.dto.response.TokenResponse;
// import com.auth.config.security.JwtTokenProvider;
// import com.auth.config.security.SecurityUser;
// import com.auth.domain.model.RefreshToken;
// import com.auth.domain.model.User;
// import com.auth.domain.repository.RefreshTokenRepository;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;

// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.verify;

// import java.time.Instant;
// import java.time.temporal.ChronoUnit;

// import com.auth.application.dto.request.RefreshTokenRequest;

// import static org.assertj.core.api.Assertions.assertThatThrownBy;

// import com.auth.domain.exception.InvalidRefreshTokenException;

// @ExtendWith(MockitoExtension.class)
// class AuthServiceImplTest {

//         @Mock
//         private AuthenticationManager authenticationManager;

//         @Mock
//         private JwtTokenProvider jwtTokenProvider;

//         @Mock
//         private RefreshTokenRepository refreshTokenRepository;

//         @InjectMocks
//         private AuthServiceImpl authService;

//         private User user;

//         @BeforeEach
//         void setup() {
//                 user = User.builder()
//                                 .id(UUID.randomUUID())
//                                 .username("john")
//                                 .password("123")
//                                 .roles(Set.of("USER"))
//                                 .enabled(true)
//                                 .build();
//         }

//         @Test
//         void shouldLoginSuccessfully() {

//                 LoginRequest request = new LoginRequest("john", "123");

//                 Authentication authentication = new UsernamePasswordAuthenticationToken(
//                                 user,
//                                 null,
//                                 Set.of());

//                 when(authenticationManager.authenticate(
//                                 new UsernamePasswordAuthenticationToken(
//                                                 "john", "123")))
//                                 .thenReturn(authentication);

//                 when(jwtTokenProvider.generateAccessToken("john"))
//                                 .thenReturn("access-token");

//                 when(jwtTokenProvider.getAccessTokenExpirationSeconds())
//                                 .thenReturn(3600L);

//                 when(refreshTokenRepository.save(org.mockito.ArgumentMatchers.any()))
//                                 .thenAnswer(inv -> inv.getArgument(0));

//                 TokenResponse response = authService.login(request);

//                 assertThat(response).isNotNull();
//                 assertThat(response.accessToken()).isEqualTo("access-token");
//                 assertThat(response.tokenType()).isEqualTo("Bearer");
//                 assertThat(response.refreshToken()).isNotBlank();
//         }

//         @Test
//         void shouldRotateRefreshTokenSuccessfully() {

//                 String oldTokenValue = "old-refresh-token";

//                 RefreshToken oldToken = RefreshToken.builder()
//                                 .id(UUID.randomUUID())
//                                 .token(oldTokenValue)
//                                 .user(user)
//                                 .revoked(false)
//                                 .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
//                                 .build();

//                 when(refreshTokenRepository.findByToken(oldTokenValue))
//                                 .thenReturn(Optional.of(oldToken));

//                 when(refreshTokenRepository.save(any()))
//                                 .thenAnswer(inv -> inv.getArgument(0));

//                 when(jwtTokenProvider.generateAccessToken(user.getUsername()))
//                                 .thenReturn("new-access-token");

//                 when(jwtTokenProvider.getAccessTokenExpirationSeconds())
//                                 .thenReturn(3600L);

//                 RefreshTokenRequest request = new RefreshTokenRequest(oldTokenValue);

//                 TokenResponse response = authService.refreshToken(request);

//                 // resposta correta
//                 assertThat(response.accessToken())
//                                 .isEqualTo("new-access-token");

//                 assertThat(response.refreshToken())
//                                 .isNotEqualTo(oldTokenValue);

//                 // token antigo revogado
//                 assertThat(oldToken.isRevoked()).isTrue();

//                 // dois saves: revogação + novo token
//                 verify(refreshTokenRepository, org.mockito.Mockito.times(2))
//                                 .save(any());
//         }

//         @Test
//         void shouldRejectRevokedRefreshToken() {

//                 String revokedTokenValue = "revoked-token";

//                 RefreshToken revokedToken = RefreshToken.builder()
//                                 .id(UUID.randomUUID())
//                                 .token(revokedTokenValue)
//                                 .user(user)
//                                 .revoked(true) // já revogado
//                                 .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
//                                 .build();

//                 when(refreshTokenRepository.findByToken(revokedTokenValue))
//                                 .thenReturn(Optional.of(revokedToken));

//                 RefreshTokenRequest request = new RefreshTokenRequest(revokedTokenValue);

//                 assertThatThrownBy(() -> authService.refreshToken(request))
//                                 .isInstanceOf(InvalidRefreshTokenException.class);
//         }

//         @Test
//         void shouldRevokeAllTokensOnLogout() {

//                 UUID userId = UUID.randomUUID();

//                 // domínio
//                 User user = new User();
//                 user.setId(userId);

//                 // principal de segurança
//                 SecurityUser securityUser = mock(SecurityUser.class);
//                 when(securityUser.getDomainUser()).thenReturn(user);

//                 // authentication
//                 Authentication authentication = mock(Authentication.class);
//                 when(authentication.getPrincipal()).thenReturn(securityUser);

//                 // security context
//                 SecurityContext securityContext = mock(SecurityContext.class);
//                 when(securityContext.getAuthentication()).thenReturn(authentication);

//                 SecurityContextHolder.setContext(securityContext);

//                 // execução
//                 authService.logout();

//                 // verificação
//                 verify(refreshTokenRepository)
//                                 .revokeAllByUserId(userId);
//         }

// }
