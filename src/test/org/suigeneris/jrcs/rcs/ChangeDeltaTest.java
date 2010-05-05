/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: ChangeDeltaTest.java,v 1.3 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.rcs.Archive;

public class ChangeDeltaTest extends TestCase
{
    private Archive archive = null;

    String[] v1 = new String[]
    { "1", "2", "3", "4" };
    Object[] v2 = new String[]
    { "a0", "1",
    // deleted two lines
            // added three lines
            "a1", "a2", "a3", "4" };

    public ChangeDeltaTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        archive = new Archive(v1, "original");
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        archive = null;
        super.tearDown();
    }

    public static Test suite()
    {
        return new TestSuite(ArchiveTest.class);
    }

    public void testChangeDelta() throws Exception
    {
        archive.addRevision(v2, "applied change delta");
        archive.addRevision(v1, "back to original");

        String[] rcsFile = (String[]) Diff.stringToArray(archive.toString());
        for (int i = 0; i < rcsFile.length && i < expectedFile.length; i++)
        {
            if (!rcsFile[i].startsWith("date"))
                assertEquals("line " + i, expectedFile[i], rcsFile[i]);
        }
        assertEquals("file size", expectedFile.length, rcsFile.length);
    }

    public void testFileSave() throws Exception
    {
        this.testChangeDelta();
        String filePath = System.getProperty("user.home")
                + java.io.File.separator + "jrcs_test.rcs";
        archive.save(filePath);

        Archive newArc = new Archive(filePath);
        new java.io.File(filePath).delete();

        String[] rcsFile = (String[]) Diff.stringToArray(newArc.toString());
        for (int i = 0; i < rcsFile.length && i < expectedFile.length; i++)
        {
            if (!rcsFile[i].startsWith("date"))
                assertEquals("line " + i, expectedFile[i], rcsFile[i]);
        }
        assertEquals("file size", expectedFile.length, rcsFile.length);

        assertEquals(archive.toString(), newArc.toString());
    }

    String[] expectedFile =
    {
            "head\t1.3;", // 0
            "access;", // 1
            "symbols;", // 2
            "locks; strict;", // 3
            "comment\t@# @;", // 4
            "", // 5
            "", // 6
            "1.3", // 7
            "date\t2002.09.28.12.55.36;\tauthor juanca;\tstate Exp;",
            "branches;", // 9
            "next\t1.2;", // 10
            "", // 11
            "1.2", // 12
            "date\t2002.09.28.12.53.53;\tauthor juanca;\tstate Exp;",
            "branches;", // 14
            "next\t1.1;", // 15
            "", // 16
            "1.1", // 17
            "date\t2002.09.28.12.52.55;\tauthor juanca;\tstate Exp;",
            "branches;", // 19
            "next\t;", // 20
            "", // 21
            "", // 22
            "desc", // 23
            "@@", // 24
            "", // 25
            "", // 26
            "1.3", // 27
            "log", // 28
            "@back to original", // 29
            "@", // 30
            "text", // 31
            "@1", // 32
            "2", // 33
            "3", // 34
            "4@", // 35
            "", // 36
            "", // 37
            "1.2", // 38
            "log", // 39
            "@applied change delta", // 40
            "@", // 41
            "text", // 42
            "@a0 1", // 43
            "a0", // 44
            "d2 2", // 45
            "a3 3", // 46
            "a1", // 47
            "a2", // 48
            "a3", // 49
            "@", // 50
            "", // 51
            "", // 52
            "1.1", // 53
            "log", // 54
            "@original", // 55
            "@", // 56
            "text",
            "@d1 1",
            "d3 3",
            "a5 2",
            "2",
            "3",
            "@" };
}
