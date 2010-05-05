/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: ParsingTest.java,v 1.5 2007-03-14 00:13:06 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author Matt Quail (matt_quail AT yahoo DOT com)
 */
public class ParsingTest extends TestCase
{

    public ParsingTest(String testName)
    {
        super(testName);
    }

    /**
     * Test to check that all the parser
     */
    public void testIDCHAR() throws Exception
    {
        InputStream is = ParsingTest.class
                .getResourceAsStream("idchar.testfile");
        assertNotNull("input stream is null", is);
        try
        {
            new Archive("idchar_test.txt,v", is);
            fail("parsed archive with illegal characters");
        }
        catch (Throwable t)
        {
        }
    }

    public void testAuthorNameWithSpaces() throws Exception
    {
        String text = "head\t1.2;\naccess;\nsymbols;\nlocks; strict;\ncomment\t@# @;\n\n\n"
                + "1.2\ndate\t2007.02.20.22.00.44;\tauthor Vincent Massol;\tstate Exp;\nbranches;\n"
                + "next\t1.1;\n\n1.1\ndate\t2007.02.14.14.01.57;\tauthor vmassol;\tstate Exp;\n"
                + "branches;\nnext\t;\n\n\ndesc\n@@\n\n\n1.2\nlog\n@@\ntext\n"
                + "@<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n<xwikidoc>\n"
                + "</xwikidoc>\n@\n\n\n1.1\nlog\n@Main.WebHome\n@\ntext\n@d15 3\na17 3\nd37 1\n"
                + "a37 1\n@";
        ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
        new Archive("", bais);
    }

    public void testAuthorCalled_text() throws Exception
    {
        String text = "" + "head 1.3;\n" + "access;\n" + "symbols;\n"
                + "locks; strict;\n" + "comment  @# @;\n" + "expand   @b@;\n"
                + "\n" + "\n" + "1.3\n"
                + "date 2007.03.06.23.37.13;    author text;    state Exp;\n"
                + "branches;\n" + "next 1.2;\n" + "\n" + "1.2\n"
                + "date 2007.03.06.23.22.11;    author text;    state Exp;\n"
                + "branches;\n" + "next 1.1;\n" + "\n" + "1.1\n"
                + "date 2007.03.06.19.54.12;    author text;    state Exp;\n"
                + "branches;\n" + "next ;\n" + "\n" + "\n" + "desc\n" + "@@\n"
                + "\n" + "\n" + "1.3\n" + "log\n"
                + "@CO000075 EMAIL EM ESPANHOL\n" + "@\n" + "text\n"
                + "@asdfsdfsdfdfsdfasdasd\n" + "d\n" + "asdasdjlkasdfasdfas\n"
                + "ASDFAasdfaxcvbdffasdC\n" + "teste\n" + "@\n" + "\n" + "\n"
                + "1.2\n" + "log\n" + "@CO000075 EMAIL EM ESPANHOL\n" + "@\n"
                + "text\n" + "@d5 1\n" + "@\n" + "\n" + "\n" + "1.1\n"
                + "log\n" + "@CO000074 subject\n" + "@\n" + "text\n"
                + "@d4 1\n" + "a4 1\n" + "ASDFAasdfaxcvbdffasd@\n" + "\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
        new Archive("", bais);
    }
}
