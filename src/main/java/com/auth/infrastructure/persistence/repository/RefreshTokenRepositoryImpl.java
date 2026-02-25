// package com.auth.infrastructure.persistence.repository;

// import com.auth.domain.model.RefreshToken;
// import com.auth.domain.repository.RefreshTokenRepository;
// import com.auth.infrastructure.persistence.repository.impl.RefreshTokenJpaRepository;

// import lombok.RequiredArgsConstructor;

// import org.springframework.stereotype.Repository;

// import java.util.Optional;
// import java.util.UUID;


// @Repository
// @RequiredArgsConstructor
// public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

//     private final RefreshTokenJpaRepository jpaRepository;

//     @Override
//     public Optional<RefreshToken> findByToken(String token) {
//         return jpaRepository.findByToken(token);
//     }

//     @Override
//     public void deleteByToken(String token) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'deleteByToken'");
//     }

//     @Override
//     public RefreshToken save(RefreshToken refreshToken) {
//         return jpaRepository.save(refreshToken);    
//     }

//     @Override
//     public void revokeAllByUserId(UUID userId) {
//         jpaRepository.revokeAllByUserId(userId);
//     }
// }
