package com.sstohnij.stacktraceqabackendv0.repository;

import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Token;
import com.sstohnij.stacktraceqabackendv0.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findTokensByTokenTypeAndUser(TokenType type, AppUser user);
    Optional<Token> findTokenByToken(String token);
}
