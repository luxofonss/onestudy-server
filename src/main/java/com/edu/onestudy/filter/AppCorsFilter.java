package com.edu.onestudy.filter;
 
// Corrected Jakarta EE imports

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.util.UUID;

import static com.edu.onestudy.constant.RequestKeyConstant.*;
import static com.edu.onestudy.constant.UrlConstant.HEALTH_CHECK_URL;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j // Lombok annotation for logging
public class AppCorsFilter implements Filter {

    // Spring's standard multipart resolver.
    // Autowire it if it's a bean, or create it if not.
    // For a filter, it's often configured via a Bean or WebMvcConfigurer.
    // If you explicitly add @Bean for MultipartResolver, Spring will use it.
    private final MultipartResolver multipartResolver;

    // Constructor injection for MultipartResolver
    // This is good practice for dependency injection in Spring.
    // Ensure you have a @Bean for MultipartResolver in your config, e.g.,
    // @Bean public MultipartResolver multipartResolver() { return new StandardServletMultipartResolver(); }
    public AppCorsFilter(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    // Overloaded constructor for cases where Spring might instantiate without explicit injection,
    // although the above constructor is preferred when using @Component.
    public AppCorsFilter() {
        // Fallback for when Spring doesn't inject it via constructor.
        // In a typical Spring Boot setup, StandardServletMultipartResolver is usually auto-configured
        // if multipart properties are set (e.g., spring.servlet.multipart.enabled=true).
        // It's safer to rely on dependency injection.
        this.multipartResolver = new StandardServletMultipartResolver();
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // Skip health check URL
        if (request.getRequestURI().contains(HEALTH_CHECK_URL)) {
            chain.doFilter(req, res);
            return;
        }

        // Initialize requestId here to ensure it's always set
        String requestId = UUID.randomUUID().toString(); // Default if not found in header
        ThreadContext.put(THREAD_REQUEST_ID, requestId); // Always put in ThreadContext early

        try {
            // Check if it's a multipart request (e.g., file upload)
            boolean isMultipart = multipartResolver.isMultipart(request); // Use Spring's resolver directly

            if (isMultipart) {
                // Resolve the multipart request
                MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(request);

                // Get or generate X-Request-ID
                String headerRequestId = multipartRequest.getHeader(X_REQUEST_ID);
                if (!ObjectUtils.isEmpty(headerRequestId)) {
                    requestId = headerRequestId;
                    ThreadContext.put(THREAD_REQUEST_ID, requestId); // Update if found in header
                }

                // Attach multipart request body to attribute
                multipartRequest.setAttribute(REQUEST_BODY, multipartRequest);
                multipartRequest.setAttribute(REQUEST_ID, requestId);

                chain.doFilter(multipartRequest, response);

            } else {
                // For non-multipart requests, wrap the request to re-read the body
                ApiKeyVerifyRequestWrapper requestWrapper = new ApiKeyVerifyRequestWrapper(request);

                // Get or generate X-Request-ID from the wrapped request
                String headerRequestId = requestWrapper.getHeader(X_REQUEST_ID);
                if (!ObjectUtils.isEmpty(headerRequestId)) {
                    requestId = headerRequestId;
                    ThreadContext.put(THREAD_REQUEST_ID, requestId); // Update if found in header
                }

                // Parse JSON body if available
                JSONObject dataRequest = new JSONObject();
                String requestBodyString = requestWrapper.getBody();
                if (!ObjectUtils.isEmpty(requestBodyString)) {
                    try {
                        JSONParser parser = new JSONParser();
                        dataRequest = (JSONObject) parser.parse(requestBodyString);
                    } catch (ParseException e) {
                        log.warn("Failed to parse request body as JSON for URI: {}. Body: {}", request.getRequestURI(), requestBodyString, e);
                        // Decide how to handle non-JSON bodies. For now, dataRequest remains empty.
                        // You might want to return 400 Bad Request here if JSON is strictly required.
                    }
                }

                // Add request metadata to JSON object and request attributes
                dataRequest.put(REQUEST_ID, requestId);
                request.setAttribute(REQUEST_ID, requestId); // Use original request for attributes if chain expects it

                dataRequest.put(API_KEY, requestWrapper.getHeader(API_KEY));
                request.setAttribute(API_KEY, requestWrapper.getHeader(API_KEY));

                dataRequest.put(API_SECRET, requestWrapper.getHeader(API_SECRET));
                request.setAttribute(API_SECRET, requestWrapper.getHeader(API_SECRET));

                // Note: request.getRequestURI() is already available from the original request
                // requestWrapper.getHeader(RequestKeyConstant.URI) doesn't make sense as URI is not a header.
                // Assuming URI is needed in dataRequest for logging/processing.
                dataRequest.put(URI, request.getRequestURI());
                request.setAttribute(URI, request.getRequestURI()); // Set actual URI

                request.setAttribute(REQUEST_PARAMETERS, request.getParameterMap()); // Parameters from original request
                request.setAttribute(REQUEST_BODY, dataRequest); // The parsed JSON body

                // The requestWrapper's body is immutable (final), so setting it here won't change
                // the stream that ApiKeyVerifyRequestWrapper provides.
                // If you intend to *modify* the body and have the modified body flow downstream,
                // you would need a more sophisticated wrapper that allows body modification
                // before constructing its InputStream/Reader.
                // For now, `requestWrapper.setBody(dataRequest.toString());` is removed
                // as `setBody` was removed in the previous step and it was modifying the internal state
                // of the wrapper, not the original request input stream for subsequent filters/servlets.
                // The `requestWrapper` already holds the *original* request body.

                chain.doFilter(requestWrapper, response);
            }
        } catch (IOException | ServletException e) {
            log.error("Filter processing error for URI: {}", request.getRequestURI(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Or SC_BAD_REQUEST depending on error type
        } catch (Throwable e) { // Catch all other unhandled throwables
            log.error("An unexpected error occurred in filter for URI: {}", request.getRequestURI(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            // Always clear ThreadContext regardless of success or failure
            ThreadContext.clearAll();
        }
    }
}