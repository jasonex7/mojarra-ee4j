/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id: MockPageContext.java,v 1.1 2005/10/18 17:47:59 edburns Exp $
 */



package com.sun.faces.mock;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.el.ExpressionEvaluator;

import javax.el.ELContext;


// Mock Object for PageContext
public class MockPageContext extends PageContext {


    private Servlet servlet = null;
    private ServletRequest request = null;
    private ServletResponse response = null;
    private HttpSession session = null;
    private String errorPageURL = null;
    private boolean needsSession = false;
    private int bufferSize = 0;
    private boolean autoFlush = false;
    private ServletConfig config = null;
    private ServletContext context = null;
    private JspWriter out = null;
    private Hashtable attributes = new Hashtable();


    // ---------------------------------------------------- PageContext Methods


    public void clearPageScope() {

        attributes.clear();

    }

    public ELContext getELContext() {
      return null;
    }


    public Object findAttribute(String name) {

        Object value = attributes.get(name);
        if (value == null) {
            value = request.getAttribute(name);
        }
        if ((value == null) && (session != null)) {
            value = session.getAttribute(name);
        }
        if (value == null) {
            value = context.getAttribute(name);
        }
        return (value);

    }


    public void forward(String path) throws IOException, ServletException {

        throw new UnsupportedOperationException();

    }


    public Object getAttribute(String name) {

        return (getAttribute(name, PAGE_SCOPE));

    }


    public Object getAttribute(String name, int scope) {

        switch (scope) {
        case PAGE_SCOPE:
            return (attributes.get(name));
        case REQUEST_SCOPE:
            return (request.getAttribute(name));
        case SESSION_SCOPE:
            if (session == null) {
                throw new IllegalArgumentException("No session for this request");
            }
            return (session.getAttribute(name));
        case APPLICATION_SCOPE:
            return (context.getAttribute(name));
        default:
            throw new IllegalArgumentException("Invalid scope " + scope);
        }

    }


    public Enumeration getAttributeNamesInScope(int scope) {

        switch (scope) {
        case PAGE_SCOPE:
            return (attributes.keys());
        case REQUEST_SCOPE:
            return (request.getAttributeNames());
        case SESSION_SCOPE:
            if (session == null) {
                throw new IllegalArgumentException("No session for this request");
            }
            return (session.getAttributeNames());
        case APPLICATION_SCOPE:
            return (context.getAttributeNames());
        default:
            throw new IllegalArgumentException("Invalid scope " + scope);
        }

    }


    public int getAttributesScope(String name) {

        if (attributes.get(name) != null) {
            return (PAGE_SCOPE);
        } else if (request.getAttribute(name) != null) {
            return (REQUEST_SCOPE);
        } else if ((session != null) && (session.getAttribute(name) != null)) {
            return (SESSION_SCOPE);
        } else if (context.getAttribute(name) != null) {
            return (APPLICATION_SCOPE);
        } else {
            return (0);

        }

    }


    public Exception getException() {

        return ((Exception) request.getAttribute(EXCEPTION));

    }


    public JspWriter getOut() {

        return (this.out);

    }


    public Object getPage() {

        return (this.servlet);

    }


    public ServletRequest getRequest() {

        return (this.request);

    }


    public ServletResponse getResponse() {

        return (this.response);

    }


    public ServletConfig getServletConfig() {

        return (this.config);

    }


    public ServletContext getServletContext() {

        return (this.context);

    }


    public HttpSession getSession() {

        return (this.session);

    }

    public void include(String relativeUrlPath,  boolean flush)
	throws ServletException, IOException {
        throw new UnsupportedOperationException();
    }

    public void handlePageException(Exception e) throws IOException, ServletException {

        throw new UnsupportedOperationException();

    }


    public void handlePageException(Throwable t) throws IOException, ServletException {

        throw new UnsupportedOperationException();

    }


    public void include(String path) throws IOException, ServletException {

        throw new UnsupportedOperationException();

    }


    public void initialize(Servlet servlet, ServletRequest request,
                           ServletResponse response, String errorPageURL,
                           boolean needsSession, int bufferSize,
                           boolean autoFlush)
        throws IOException, IllegalStateException, IllegalArgumentException {

        // Initialize state - passed values
        this.servlet = servlet;
        this.request = request;
        this.response = response;
        this.errorPageURL = errorPageURL;
        this.bufferSize = bufferSize;
        this.autoFlush = autoFlush;

        // Initialize state - derived values
        this.config = servlet.getServletConfig();
        this.context = config.getServletContext();
        if (request instanceof HttpServletRequest && needsSession) {
            this.session = ((HttpServletRequest) request).getSession();
        }
        this.out = new MockJspWriter(bufferSize, autoFlush);

        // Register predefined page scope attributes
        setAttribute(OUT, this.out);
        setAttribute(REQUEST, this.request);
        setAttribute(RESPONSE, this.response);
        if (session != null) {
            setAttribute(SESSION, session);
        }
        setAttribute(PAGE, servlet);
        setAttribute(CONFIG, config);
        setAttribute(PAGECONTEXT, this);
        setAttribute(APPLICATION, context);

    }


    public VariableResolver getVariableResolver() {
        throw new UnsupportedOperationException();
    }

    public  ExpressionEvaluator getExpressionEvaluator() {
        throw new UnsupportedOperationException();
    }


    public JspWriter popBody() {

        throw new UnsupportedOperationException();

    }


    public BodyContent pushBody() {

        throw new UnsupportedOperationException();

    }


    public void release() {

        throw new UnsupportedOperationException();

    }


    public void removeAttribute(String name) {

        removeAttribute(name, PAGE_SCOPE);

    }


    public void removeAttribute(String name, int scope) {

        switch (scope) {
        case PAGE_SCOPE:
            attributes.remove(name);
            break;
        case REQUEST_SCOPE:
            request.removeAttribute(name);
            break;
        case SESSION_SCOPE:
            if (session == null) {
                throw new IllegalArgumentException("No session for this request");
            }
            session.removeAttribute(name);
            break;
        case APPLICATION_SCOPE:
            context.removeAttribute(name);
            break;
        default:
            throw new IllegalArgumentException("Invalid scope " + scope);
        }

    }


    public void setAttribute(String name, Object value) {

        setAttribute(name, value, PAGE_SCOPE);

    }


    public void setAttribute(String name, Object value, int scope) {

        switch (scope) {
        case PAGE_SCOPE:
            attributes.put(name, value);
            break;
        case REQUEST_SCOPE:
            request.setAttribute(name, value);
            break;
        case SESSION_SCOPE:
            if (session == null) {
                throw new IllegalArgumentException("No session for this request");
            }
            session.setAttribute(name, value);
            break;
        case APPLICATION_SCOPE:
            context.setAttribute(name, value);
            break;
        default:
            throw new IllegalArgumentException("Invalid scope " + scope);
        }

    }




}
