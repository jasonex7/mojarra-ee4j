/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.servlet30.facelets;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Issue3167IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.close();
    }

    @Test(expected = FailingHttpStatusCodeException.class)
    public void testDecorateDirectContract() throws Exception {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getPage(webUrl + "faces/decorateDirectContract.xhtml");
    }

    @Test(expected = FailingHttpStatusCodeException.class)
    public void testIncludeDirectContract() throws Exception {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getPage(webUrl + "faces/includeDirectContract.xhtml");
    }
}
