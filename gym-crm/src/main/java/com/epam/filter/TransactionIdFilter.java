package com.epam.filter;

import com.epam.utility.TransactionId;
import jakarta.servlet.*;
import java.io.IOException;

public class TransactionIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {

        TransactionId.addTransactionId();
        try {
            chain.doFilter(request, response);
        } finally {
            TransactionId.removeTransactionId();
        }
    }
}
