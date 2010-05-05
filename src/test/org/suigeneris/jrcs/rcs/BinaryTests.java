/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: BinaryTests.java,v 1.3 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BinaryTests extends TestCase
{
    private static URL archiveURL = BinaryTests.class.getResource("test.png_v");
    private static URL originalURL = BinaryTests.class.getResource("java.png");
    private static URL changedURL = BinaryTests.class
            .getResource("java_cut.png");

    Archive archive;
    char[] original;
    char[] changed;

    public BinaryTests(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(BinaryTests.class);
    }

    protected char[] readURL(URL url) throws Exception
    {
        InputStream stream = url.openStream();
        InputStreamReader r = new InputStreamReader(stream);
        char[] result = new char[stream.available()];
        r.read(result);
        r.close();
        return result;
    }

    public void setUp() throws Exception
    {
        archive = new Archive(archiveURL);
        original = readURL(originalURL);
        changed = readURL(changedURL);
    }

    public void testParse() throws Exception
    {
        Archive archive = new Archive(archiveURL);
        assertEquals(Archive.EXP_Binary, archive.getExpand());
    }

    public void testExtract() throws Exception
    {
        Object[] last = archive.getRevision();
        char[] rev = last[0].toString().toCharArray();
        assertEquals(original.length, rev.length);
        for (int i = 0; i < original.length; i++)
            assertTrue("pos " + i + ":", original[i] == rev[i]);
    }

}
