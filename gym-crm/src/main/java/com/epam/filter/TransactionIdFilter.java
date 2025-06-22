package com.epam.filter;

import com.epam.utility.TransactionId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TransactionIdFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        TransactionId.addTransactionId();

        try {
            filterChain.doFilter(request, response);
        } finally {
            TransactionId.removeTransactionId();
        }
    }


}