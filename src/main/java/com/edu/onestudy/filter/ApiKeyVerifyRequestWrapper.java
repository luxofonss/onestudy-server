package com.edu.onestudy.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets; // Recommended for consistent character encoding

public class ApiKeyVerifyRequestWrapper extends HttpServletRequestWrapper {

    private final String body; // Make body final as it's set once in constructor

    public ApiKeyVerifyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try (InputStream inputStream = request.getInputStream()) { // Use try-with-resources for auto-closing
            if (inputStream != null) {
                // Use StandardCharsets for consistent character encoding
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } finally {
            // bufferedReader will be closed by try-with-resources if created
            // However, it's good practice to ensure it's not null before closing if not in try-with-resources,
            // or if dealing with older Java versions. Here, it's redundant due to try-with-resources on inputStream.
            // But if bufferedReader was directly managed, you'd keep this.
        }
        body = stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // Use StandardCharsets for consistent character encoding when getting bytes
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                // This indicates if all bytes have been read from the stream.
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                // This indicates if data is available to be read without blocking.
                return true; // Since we're reading from an in-memory byte array, it's always ready.
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // For non-blocking I/O, you'd typically implement async reading here.
                // For a wrapper that reads from a pre-loaded string, this can be a no-op,
                // or you could call listener.onAllDataRead() and listener.onDataAvailable() if needed.
                // For basic use cases with pre-read body, throwing UnsupportedOperationException might be safer
                // if you don't intend to support async reading from this wrapper.
                // However, returning true for isReady and overriding read() implies it's blocking-ready.
                // In a real async scenario, you'd need to manage the listener.
                // For now, we'll keep it as a no-op as it's common for simple wrappers.
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // Ensure consistent encoding when creating the reader from the input stream
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }

    public String getBody() {
        return body;
    }

    // Removing setBody as 'body' is final and set in constructor, making the wrapper immutable after creation.
    // If you need to modify the body, you would create a new wrapper instance or redesign.

    @Override
    public String getContentType() {
        String contentType = super.getContentType();
        // A common default if not explicitly set, or if you expect JSON for most API requests.
        // Be careful with this default if your API handles various content types.
        return contentType == null ? MediaType.APPLICATION_JSON_VALUE : contentType;
    }
}