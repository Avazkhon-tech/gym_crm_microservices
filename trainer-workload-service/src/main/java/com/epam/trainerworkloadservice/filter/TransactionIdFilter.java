package com.epam.trainerworkloadservice.filter;

import com.epam.trainerworkloadservice.utility.TransactionId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TransactionIdFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String transactionId = request.getHeader("TransactionId");
        if (transactionId == null) {
            TransactionId.addTransactionId();
        } else {
            TransactionId.addTransactionId(request.getHeader(transactionId));
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            TransactionId.removeTransactionId();
        }
    }
}
