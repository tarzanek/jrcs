/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: ArchiveTest.java,v 1.7 2007-07-27 17:19:47 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import java.io.StringReader;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.diff.DiffException;
import org.suigeneris.jrcs.rcs.impl.Node;
import org.suigeneris.jrcs.rcs.impl.NodeNotFoundException;
import org.suigeneris.jrcs.util.ToString;

public class ArchiveTest extends TestCase
{

    public ArchiveTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(ArchiveTest.class);
    }

    Object[] v1_1 = new String[]
    { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five", "[6] six" };

    Object[] v1_2 = new String[]
    { "[1] one", "[2] two", "[3] three", "[3.1]", // inserted this
            "[4] four",
            "[5] five"
    // deleted [6]
    };

    String user = System.getProperty("user.name");

    // WARNING: there apparently uneeded string concatenations
    // are there to prevent CVS from mangling our test data.

    Object[] v1_2_with_keywords = new String[]
    {
            "",
            "[3] three  $" + "Source: trash #3$",
            "[4] four   $" + "RCSfile:  trash #4$",
            "[5] five   $" + "Revision: trash # 5 $",
            "[7] seven  $" + "Author: trash #7 $",
            "[8] eight  $" + "State: trash 8 $",
            "[9] nine   $Locker:  $",
            "[10] ten   $" + "RCSfile: trash #10 $ " + "$"
                    + "Revision: trash #10 $ " + "$" + "Author: trash $",
            "" };

    Object[] v1_2_with_expanded_keywords = new String[]
    {
            "",
            "[3] three  $" + "Source: /a/test/path/test_file,v $",
            "[4] four   $" + "RCSfile: test_file,v $",
            "[5] five   $" + "Revision: 1.2 $",
            "[7] seven  $" + "Author: " + user + " $",
            "[8] eight  $" + "State: Exp $",
            "[9] nine   $Locker:  $",
            "[10] ten   $" + "RCSfile: test_file,v $ " + "$"
                    + "Revision: 1.2 $ " + "$" + "Author: " + user + " $",
            "" };

    Object[] v1_3 = new String[]
    {
            "[1] one changed",
            "[2] two",
            "[3] three",
            "[3.1]",
            "[4] four",
            "[5] five" };

    Object[] v1_20 = new String[]
    { "[1:1.20] one changed", "[3] three", "[3.1]", "[5:1.20] five" };

    Object[] v1_2_1_1 = new String[]
    {
            "[1] one",
            "[2] two",
            "[2.1]",
            "[3] three",
            "[3.1]",
            "[4] four changed",
            "[5] five",
            "[5.1]" };

    Object[] v1_2_1_2 = new String[]
    {
            "[1:1.2.1.1] one",
            "[2.1]",
            "[3] three",
            "[3.1]",
            "[4] four changed",
            "[5:1.2.1.1] five",
            "[5.1]" };

    Object[] v1_2_8_2 = new String[]
    {
            "[1:1.2.8.1] one",
            "[2.1]",
            "[3] three",
            "[3.1]",
            "[4] four changed",
            "[5:1.2.8.1] five",
            "[5.1]" };

    Object[] v1_2_8_4 = new String[]
    {
            "[1:1.2.8.1] one",
            "[2.1]",
            "[3.1:1.2.8.2]",
            "[4] four changed",
            "[5:1.2.8.1] five",
            "[5.1]" };

    Object[] v1_2_8_5 = new String[]
    { "[1:1.2.8.5] one" };

    String[] sampleFile =
    {
            "head\t1.3;",
            "access;",
            "symbols;",
            "locks; strict;",
            "comment\t@# @;",
            "",
            "",
            "1.3",
            "date\t99.08.24.16.58.59;\tauthor juanca;\tstate Exp;",
            "branches;",
            "next\t1.2;",
            "",
            "1.2",
            "date\t99.08.24.16.57.54;\tauthor juanca;\tstate Exp;",
            "branches",
            "\t1.2.1.1;",
            "next\t1.1;",
            "",
            "1.1",
            "date\t99.08.24.16.56.51;\tauthor juanca;\tstate Exp;",
            "branches;",
            "next\t;",
            "",
            "1.2.1.1",
            "date\t99.08.24.17.00.30;\tauthor juanca;\tstate Exp;",
            "branches;",
            "next\t;",
            "",
            "",
            "desc",
            "@@",
            "",
            "",
            "1.3",
            "log",
            "@Changed 1",
            "@",
            "text",
            "@[1] one changed",
            "[2] two",
            "[3] three",
            "[3.1]",
            "[4] four",
            "[5] five@",
            "",
            "",
            "1.2",
            "log",
            "@Added 3.1, deleted 6",
            "@", // 50
            "text",
            "@d1 1",
            "a1 1",
            "[1] one",
            "@",
            "",
            "",
            "1.2.1.1",
            "log",
            "@Added 2.1, changed 4, added 5.1", // 60
            "@",
            "text",
            "@a2 1",
            "[2.1]",
            "d5 1",
            "a5 1",
            "[4] four changed",
            "a6 1",
            "[5.1]",
            "@", // 70
            "",
            "",
            "1.1",
            "log",
            "@A simple test file",
            "@",
            "text",
            "@d4 1",
            "a6 1",
            "[6] six",
            "@" };

    Archive archive;

    public void setUp()
    {
        archive = new Archive(v1_1, "A simple test file");
        archive.setFileName("/a/test/path/test_file,v");
    }

    public void testEmptyArchive()
    {
        try
        {
            Object[] rev = new Archive().getRevision();
            if (rev != null)
                fail("empty archive, exception should be thrown");
        }
        catch (Exception e)
        {
        }
    }

    public void testAdd1_1() throws DiffException, RCSException
    {
        Object[] rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_1), ToString.arrayToString(rev));
        assertTrue(Diff.compare(v1_1, rev));
        assertNull(archive.addRevision(v1_1, "should not be added"));

        assertEquals(new Version("1.1"), archive.getRevisionVersion());
        assertEquals(new Version("1.1"), archive.getRevisionVersion("1."));

        assertNull(archive.getRevisionVersion("2"));
        assertNull(archive.getRevisionVersion("1.2.1"));

        Node[] log = archive.changeLog();
        assertNotNull("log is null", log);
        assertEquals(1, log.length);
        assertEquals("1.1", log[0].version.toString());
    }

    public void testAdd1_2() throws DiffException, RCSException
    {
        testAdd1_1();
        archive.addRevision(v1_2, "Added 3.1, deleted 6");
        Object[] rev = archive.getRevision();
        assertEquals("1.2", archive.head.version.toString());
        assertEquals(ToString.arrayToString(v1_2), ToString.arrayToString(rev));
        assertNull(archive.addRevision(v1_2, "should not be added"));

        assertEquals(new Version("1.2"), archive.getRevisionVersion());
        assertEquals(new Version("1.2"), archive.getRevisionVersion("1."));

        assertNull(archive.getRevisionVersion("2"));
        assertNull(archive.getRevisionVersion("1.2.1"));

        Node[] log = archive.changeLog();
        assertNotNull("log is null", log);
        assertEquals(2, log.length);
        assertEquals("1.1", log[0].version.toString());
        assertEquals("1.2", log[1].version.toString());
    }

    public void testAdd1_2_with_keywords() throws DiffException, RCSException
    {
        testAdd1_1();
        archive.addRevision(v1_2_with_keywords, "Added revision with keywords");
        Object[] rev = archive.getRevision();

        assertEquals("1.2", archive.head.version.toString());

        String o = ToString.arrayToString(v1_2_with_expanded_keywords);
        String r = ToString.arrayToString(rev);
        assertEquals(ToString.toStringOfChars(o), ToString.toStringOfChars(r));
        assertNull(archive.addRevision(v1_2_with_expanded_keywords,
                "should not be added"));
        assertNull(archive.addRevision(v1_2_with_keywords,
                "should not be added"));

        assertEquals(new Version("1.2"), archive.getRevisionVersion());
        assertEquals(new Version("1.2"), archive.getRevisionVersion("1."));

        assertNull(archive.getRevisionVersion("2"));
        assertNull(archive.getRevisionVersion("1.2.1"));
    }

    public void testAdd1_3() throws DiffException, RCSException
    {
        testAdd1_2();
        archive.addRevision(v1_3, "Changed 1");
        Object[] rev = archive.getRevision();
        assertTrue(Diff.compare(v1_3, rev));
        assertNull(archive.addRevision(v1_3, "should not be added"));

        assertEquals(new Version("1.3"), archive.getRevisionVersion());
        assertEquals(new Version("1.3"), archive.getRevisionVersion("1"));

        assertNull(archive.getRevisionVersion("2"));
        assertNull(archive.getRevisionVersion("1.2.1"));

        Node[] log = archive.changeLog();
        assertNotNull("log is null", log);
        assertEquals(3, log.length);
        assertEquals("1.1", log[0].version.toString());
        assertEquals("1.2", log[1].version.toString());
        assertEquals("1.3", log[2].version.toString());
    }

    public void testAdd1_2_1() throws DiffException, RCSException
    {
        testAdd1_3();
        archive.addRevision(v1_2_1_1, "1.2.1",
                "Added 2.1, changed 4, added 5.1");
        String filestr = archive.toString();
        String[] file = (String[]) Diff.stringToArray(filestr);

        for (int i = 0; i < sampleFile.length && i < file.length; i++)
        {
            if (!sampleFile[i].startsWith("date"))
                assertEquals("line " + i, sampleFile[i], file[i]);
        }
        assertEquals("file size", sampleFile.length, file.length);

        Object[] rev = archive.getRevision("1.2.1");
        assertTrue("diffs equal", Diff.compare(v1_2_1_1, rev));
        assertNull("should not be added", archive.addRevision(v1_2_1_1,
                "1.2.1", "should not be added"));

        assertEquals("dot", new Version("1.2.1.1"), archive
                .getRevisionVersion("1.2."));
        assertEquals("zero", new Version("1.2.1.1"), archive
                .getRevisionVersion("1.2.0"));

        assertEquals(new Version("1.2.1.1"), archive
                .getRevisionVersion("1.2.1"));
        assertEquals(new Version("1.2.1.1"), archive
                .getRevisionVersion("1.2.1"));

        assertNull(archive.getRevisionVersion("2"));
        assertNull(archive.getRevisionVersion("1.3.1"));

        assertNull(archive.getRevisionVersion("1.2.1.2"));
        assertNull(archive.getRevisionVersion("1.2.2"));

        Node[] log = archive.changeLog(new Version("1.2.1"));
        assertNotNull("log is null", log);
        assertEquals(3, log.length);
        assertEquals("1.1", log[0].version.toString());
        assertEquals("1.2", log[1].version.toString());
        assertEquals("1.2.1.1", log[2].version.toString());

        log = archive.changeLog(new Version("1.2.1"), new Version("1.2"));
        assertNotNull("log is null", log);
        assertEquals(2, log.length);
        assertEquals("1.2", log[0].version.toString());
        assertEquals("1.2.1.1", log[1].version.toString());
    }

    public void testBranch() throws DiffException, RCSException
    {
        testAdd1_3();
        archive.setBranch("1.2.0");
        archive.addRevision(v1_2_1_1, "Added 2.1, changed 4");
        archive.toString();
        Object[] rev = archive.getRevision("1.2.1.1");
        assertTrue(Diff.compare(v1_2_1_1, rev));
        Version v;
        v = archive.addRevision(v1_2_1_2, "1.2.0", "Arbitrary revision number");
        assertEquals("1.2.1.2", v.toString());

        v = archive.addRevision(v1_2_8_2, "1.2.8.2",
                "Arbitrary revision number");
        assertEquals("1.2.8.2", v.toString());

        try
        {
            v = archive.addRevision(v1_2_8_4, "1.2.8.1",
                    "Added to arbitrary branch");
            fail("could add revision 1.2.8.1 after having added 1.2.8.2");
        }
        catch (InvalidVersionNumberException e)
        {
        }

        v = archive.addRevision(v1_2_8_4, "1.2.8.4",
                "Added to arbitrary branch");
        assertEquals("1.2.8.4", v.toString());

        v = archive.addRevision(v1_2_8_5, "1.2.0", "Added to arbitrary branch");
        assertEquals("1.2.8.5", v.toString());

        assertEquals(new Version("1.2.8.5"), archive.getRevisionVersion());
        assertEquals(".8", new Version("1.2.8.5"), archive
                .getRevisionVersion("1.2.8"));
        assertEquals(new Version("1.2.8.5"), archive.getRevisionVersion("1.2."));
        assertEquals(new Version("1.2.8.5"), archive
                .getRevisionVersion("1.2.0"));

        assertNull(archive.getRevisionVersion("1.2.8.6"));
        assertNull(archive.getRevisionVersion("1.2.3"));
        assertNull(archive.getRevisionVersion("1.2.9"));

        Node[] log = archive.changeLog(new Version("1.2.8"));
        assertNotNull("log is null", log);
        assertEquals(5, log.length);
        assertEquals("1.1", log[0].version.toString());
        assertEquals("1.2", log[1].version.toString());
        assertEquals("1.2.8.2", log[2].version.toString());
        assertEquals("1.2.8.4", log[3].version.toString());
        assertEquals("1.2.8.5", log[4].version.toString());

        try
        {
            log = archive.changeLog(new Version("1.2.8"), new Version("1.2.1"));
            fail("found change log between 1.2.8.5 and 1.2.1.2");
        }
        catch (NodeNotFoundException e)
        {
        }

    }

    public void testInvalidBranch() throws DiffException, RCSException
    {
        testAdd1_1();
        try
        {
            archive.setBranch("1.3.1");
            fail("succeeded with invalid branch");
        }
        catch (InvalidVersionNumberException e)
        {
        }
    }

    public void testUnicodeEscapes() throws DiffException, RCSException
    {
        Archive archive = new Archive(new String[]
        { "\\user" }, "original");
        archive.addRevision(new String[]
        { "user" }, "original");
    }

    public void testRemoveSingleNode() throws Exception
    {
        testAdd1_1();
        try
        {
            archive.Remove();
            fail("removed last node");
        }
        catch (UnsupportedOperationException e)
        {
        }
    }

    public void testRemoveFromTrunkTip() throws Exception
    {
        testAdd1_3();
        archive.Remove();
        Version v = archive.getRevisionVersion();
        assertTrue(v.isTrunk());
        assertEquals("1.2", v.toString());

        Object[] rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2), ToString.arrayToString(rev));
    }

    public void testRemoveFromTrunkTipIndirect() throws Exception
    {
        testAdd1_3();
        archive.Remove("1.3");
        Version v = archive.getRevisionVersion();
        assertTrue(v.isTrunk());
        assertEquals("1.2", v.toString());

        Object[] rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2), ToString.arrayToString(rev));
    }

    public void testRemoveBranchTip() throws Exception
    {
        testBranch();
        Version v;
        Object[] rev;

        v = archive.getRevisionVersion();
        assertTrue(v.isBranch());
        assertEquals("1.2.8.5", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_8_5), ToString
                .arrayToString(rev));

        archive.Remove();
        v = archive.getRevisionVersion();
        assertEquals("1.2.8.4", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_8_4), ToString
                .arrayToString(rev));

        archive.Remove();
        v = archive.getRevisionVersion();
        assertEquals("1.2.8.2", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_8_2), ToString
                .arrayToString(rev));

        assertEquals("1.2.0", archive.branch.toString());

        archive.Remove();
        v = archive.getRevisionVersion();
        assertEquals("1.2.1.2", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_1_2), ToString
                .arrayToString(rev));

        archive.Remove();
        v = archive.getRevisionVersion();
        assertEquals("1.2.1.1", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_1_1), ToString
                .arrayToString(rev));

        archive.Remove();
        try
        {
            v = archive.getRevisionVersion();
            fail("found node when no more nodes in branch");
        }
        catch (NoSuchElementException e)
        {
        }
        archive.setBranch((String) null);
        v = archive.getRevisionVersion();
        assertEquals("1.3", v.toString());
        assertNull(archive.branch);
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_3), ToString.arrayToString(rev));
    }

    public void testRemoveBranchTipIndirect() throws Exception
    {
        testBranch();
        Version v;
        Object[] rev;

        v = archive.getRevisionVersion();
        assertTrue(v.isBranch());
        assertEquals("1.2.8.5", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_8_5), ToString
                .arrayToString(rev));

        archive.Remove("1.2.8.5");
        v = archive.getRevisionVersion();
        assertEquals("1.2.8.4", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_8_4), ToString
                .arrayToString(rev));

        archive.Remove("1.2.8.4");
        v = archive.getRevisionVersion();
        assertEquals("1.2.8.2", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_8_2), ToString
                .arrayToString(rev));

        archive.Remove("1.2.8.2");
        v = archive.getRevisionVersion();
        assertEquals("1.2.1.2", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_1_2), ToString
                .arrayToString(rev));
    }

    public void testRemoveFromTrunk() throws Exception
    {
        testAdd1_3();
        archive.Remove("1.2");
        Version v = archive.getRevisionVersion();
        assertTrue(v.isTrunk());
        assertEquals("1.3", v.toString());

        Object[] rev;

        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_3), ToString.arrayToString(rev));

        rev = archive.getRevision("1.1");
        assertEquals(ToString.arrayToString(v1_1), ToString.arrayToString(rev));
    }

    public void testRemoveFromBranch() throws Exception
    {
        testBranch();
        Version v;
        Object[] rev;

        archive.Remove("1.2.8.4");

        v = archive.getRevisionVersion();
        assertEquals("1.2.8.5", v.toString());
        rev = archive.getRevision();
        assertEquals(ToString.arrayToString(v1_2_8_5), ToString
                .arrayToString(rev));

        v = archive.getRevisionVersion("1.2.8.2");
        assertEquals("1.2.8.2", v.toString());
        rev = archive.getRevision(v);
        assertEquals(ToString.arrayToString(v1_2_8_2), ToString
                .arrayToString(rev));
    }

    public void testSymbolicTags() throws Exception
    {
        testBranch();
        Version v;

        archive.addSymbol("tag1", "1.2.8");
        v = archive.getRevisionVersion("tag1");
        assertEquals("1.2.8.5", v.toString());

        archive.addSymbol("tag2", "1.2.1");
        v = archive.getRevisionVersion("tag2");
        assertEquals("1.2.1.2", v.toString());

        archive.addSymbol("tag3", "1.2.1.1");
        v = archive.getRevisionVersion("tag3");
        assertEquals("1.2.1.1", v.toString());

        archive.addSymbol("tag4", "1");
        v = archive.getRevisionVersion("tag4");
        assertEquals("1.3", v.toString());
    }

    public void testQuoteString() throws Exception
    {
        Archive arch = new Archive(new String[]
        { "qwe @ qwe", "qwe @@ qwe" }, "some bad @ log");
        System.out.println(arch.toString());

        Archive arch2 = new Archive("", new StringReader(arch.toString()));
        System.out.println(arch2.toString());
    }
}
