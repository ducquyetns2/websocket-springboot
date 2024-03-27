package com.ducquyet.websocket.filter;

import com.ducquyet.websocket.entity.Token;
import com.ducquyet.websocket.repository.TokenRepository;
import org.springframework.core.annotation.Order;
import responseDto.ErrorDetail;
import com.ducquyet.websocket.service.JwtService;
import com.ducquyet.websocket.service.UserDetailsServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader=request.getHeader("Authorization");

            if(authHeader==null || !authHeader.startsWith("Bearer ") ) {
                filterChain.doFilter(request,response);
                return;
            } else {
                final String token=authHeader.substring(7);
                if(!jwtService.validateToken(token)) {
                    throw new JwtException("Token has been expired");
                }
                UserDetails userDetails= jwtService.getUserDetails(token);
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
            filterChain.doFilter(request,response);
        }catch(Exception ex) {
          customHandleException(ex,response);
        }
    }
    private void customHandleException(Exception ex,HttpServletResponse response) throws IOException {
        ErrorDetail errorDetail=ErrorDetail.builder()
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();
        ObjectMapper mapper=new ObjectMapper().registerModule(new JavaTimeModule());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().write(mapper.writeValueAsString(errorDetail));
    }
}
