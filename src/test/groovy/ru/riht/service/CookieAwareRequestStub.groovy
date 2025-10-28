package ru.riht.service

import jakarta.servlet.AsyncContext
import jakarta.servlet.DispatcherType
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.ServletConnection
import jakarta.servlet.ServletContext
import jakarta.servlet.ServletException
import jakarta.servlet.ServletInputStream
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import jakarta.servlet.http.HttpUpgradeHandler
import jakarta.servlet.http.Part

import java.security.Principal

class CookieAwareRequestStub implements HttpServletRequest{
    private Cookie[] cookies = []

    @Override
    String getAuthType() {
        return null
    }

    void setCookies(Cookie... cookies){
        this.cookies = cookies
    }

    @Override
    Cookie[] getCookies() {
        return cookies
    }

    @Override
    long getDateHeader(String s) {
        return 0
    }

    @Override
    String getHeader(String s) {
        return null
    }

    @Override
    Enumeration<String> getHeaders(String s) {
        return null
    }

    @Override
    Enumeration<String> getHeaderNames() {
        return null
    }

    @Override
    int getIntHeader(String s) {
        return 0
    }

    @Override
    String getMethod() {
        return null
    }

    @Override
    String getPathInfo() {
        return null
    }

    @Override
    String getPathTranslated() {
        return null
    }

    @Override
    String getContextPath() {
        return null
    }

    @Override
    String getQueryString() {
        return null
    }

    @Override
    String getRemoteUser() {
        return null
    }

    @Override
    boolean isUserInRole(String s) {
        return false
    }

    @Override
    Principal getUserPrincipal() {
        return null
    }

    @Override
    String getRequestedSessionId() {
        return null
    }

    @Override
    String getRequestURI() {
        return null
    }

    @Override
    StringBuffer getRequestURL() {
        return null
    }

    @Override
    String getServletPath() {
        return null
    }

    @Override
    HttpSession getSession(boolean b) {
        return null
    }

    @Override
    HttpSession getSession() {
        return null
    }

    @Override
    String changeSessionId() {
        return null
    }

    @Override
    boolean isRequestedSessionIdValid() {
        return false
    }

    @Override
    boolean isRequestedSessionIdFromCookie() {
        return false
    }

    @Override
    boolean isRequestedSessionIdFromURL() {
        return false
    }

    @Override
    boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false
    }

    @Override
    void login(String s, String s1) throws ServletException {

    }

    @Override
    void logout() throws ServletException {

    }

    @Override
    Collection<Part> getParts() throws IOException, ServletException {
        return null
    }

    @Override
    Part getPart(String s) throws IOException, ServletException {
        return null
    }

    @Override
    def <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null
    }

    @Override
    Object getAttribute(String s) {
        return null
    }

    @Override
    Enumeration<String> getAttributeNames() {
        return null
    }

    @Override
    String getCharacterEncoding() {
        return null
    }

    @Override
    void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    int getContentLength() {
        return 0
    }

    @Override
    long getContentLengthLong() {
        return 0
    }

    @Override
    String getContentType() {
        return null
    }

    @Override
    ServletInputStream getInputStream() throws IOException {
        return null
    }

    @Override
    String getParameter(String s) {
        return null
    }

    @Override
    Enumeration<String> getParameterNames() {
        return null
    }

    @Override
    String[] getParameterValues(String s) {
        return null
    }

    @Override
    Map<String, String[]> getParameterMap() {
        return null
    }

    @Override
    String getProtocol() {
        return null
    }

    @Override
    String getScheme() {
        return null
    }

    @Override
    String getServerName() {
        return null
    }

    @Override
    int getServerPort() {
        return 0
    }

    @Override
    BufferedReader getReader() throws IOException {
        return null
    }

    @Override
    String getRemoteAddr() {
        return null
    }

    @Override
    String getRemoteHost() {
        return null
    }

    @Override
    void setAttribute(String s, Object o) {

    }

    @Override
    void removeAttribute(String s) {

    }

    @Override
    Locale getLocale() {
        return null
    }

    @Override
    Enumeration<Locale> getLocales() {
        return null
    }

    @Override
    boolean isSecure() {
        return false
    }

    @Override
    RequestDispatcher getRequestDispatcher(String s) {
        return null
    }

    @Override
    int getRemotePort() {
        return 0
    }

    @Override
    String getLocalName() {
        return null
    }

    @Override
    String getLocalAddr() {
        return null
    }

    @Override
    int getLocalPort() {
        return 0
    }

    @Override
    ServletContext getServletContext() {
        return null
    }

    @Override
    AsyncContext startAsync() throws IllegalStateException {
        return null
    }

    @Override
    AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null
    }

    @Override
    boolean isAsyncStarted() {
        return false
    }

    @Override
    boolean isAsyncSupported() {
        return false
    }

    @Override
    AsyncContext getAsyncContext() {
        return null
    }

    @Override
    DispatcherType getDispatcherType() {
        return null
    }

    @Override
    String getRequestId() {
        return null
    }

    @Override
    String getProtocolRequestId() {
        return null
    }

    @Override
    ServletConnection getServletConnection() {
        return null
    }
}
