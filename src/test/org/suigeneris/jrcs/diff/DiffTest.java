/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: DiffTest.java,v 1.3 2006/06/08 05:28:34 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.diff.DiffAlgorithm;
import org.suigeneris.jrcs.diff.DifferentiationFailedException;
import org.suigeneris.jrcs.diff.PatchFailedException;
import org.suigeneris.jrcs.diff.Revision;
import org.suigeneris.jrcs.diff.RevisionVisitor;
import org.suigeneris.jrcs.diff.delta.AddDelta;
import org.suigeneris.jrcs.diff.delta.ChangeDelta;
import org.suigeneris.jrcs.diff.delta.DeleteDelta;
import org.suigeneris.jrcs.diff.delta.Delta;
import org.suigeneris.jrcs.diff.simple.SimpleDiff;

import junit.framework.*;

public abstract class DiffTest extends TestCase
{
    static final int LARGE = 2 * 1024;

    protected DiffAlgorithm algorithm;

    public DiffTest(String testName, DiffAlgorithm algorithm)
    {
        super(testName);
        this.algorithm = algorithm;
    }

    public static Test suite()
    {
        return new TestSuite(DiffTest.class);
    }

    Object[] empty = new Object[]
    {};
    Object[] original = new String[]
    {
            "[1] one",
            "[2] two",
            "[3] three",
            "[4] four",
            "[5] five",
            "[6] six",
            "[7] seven",
            "[8] eight",
            "[9] nine" };
    // lines 3 and 9 deleted
    Object[] rev1 = new String[]
    {
            "[1] one",
            "[2] two",
            "[4] four",
            "[5] five",
            "[6] six",
            "[7] seven",
            "[8] eight", };

    // lines 7 and 8 changed, 9 deleted
    Object[] rev2 = new String[]
    {
            "[1] one",
            "[2] two",
            "[3] three",
            "[4] four",
            "[5] five",
            "[6] six",
            "[7] seven revised",
            "[8] eight revised", };

    public void testCompare()
    {
        assertTrue(!Diff.compare(original, empty));
        assertTrue(!Diff.compare(empty, original));
        assertTrue(Diff.compare(empty, empty));
        assertTrue(Diff.compare(original, original));
    }

    public void testEmptySequences() throws DifferentiationFailedException
    {
        String[] emptyOrig =
        {};
        String[] emptyRev =
        {};
        Revision revision = Diff.diff(emptyOrig, emptyRev, algorithm);

        assertEquals("revision size is not zero", 0, revision.size());
    }

    public void testOriginalEmpty() throws DifferentiationFailedException
    {
        String[] emptyOrig =
        {};
        String[] rev =
        { "1", "2", "3" };
        Revision revision = Diff.diff(emptyOrig, rev, algorithm);

        assertEquals("revision size should be one", 1, revision.size());
        assertTrue(revision.getDelta(0) instanceof AddDelta);
    }

    public void testRevisedEmpty() throws DifferentiationFailedException
    {
        String[] orig =
        { "1", "2", "3" };
        String[] emptyRev =
        {};
        Revision revision = Diff.diff(orig, emptyRev, algorithm);

        assertEquals("revision size should be one", 1, revision.size());
        assertTrue(revision.getDelta(0) instanceof DeleteDelta);
    }

    public void testDeleteAll() throws DifferentiationFailedException,
            PatchFailedException
    {
        Revision revision = Diff.diff(original, empty, algorithm);
        assertEquals(1, revision.size());
        assertEquals(DeleteDelta.class, revision.getDelta(0).getClass());
        assertTrue(Diff.compare(revision.patch(original), empty));
    }

    public void testTwoDeletes() throws DifferentiationFailedException,
            PatchFailedException
    {
        Revision revision = Diff.diff(original, rev1, algorithm);
        assertEquals(2, revision.size());
        assertEquals(DeleteDelta.class, revision.getDelta(0).getClass());
        assertEquals(DeleteDelta.class, revision.getDelta(1).getClass());
        assertTrue(Diff.compare(revision.patch(original), rev1));
        assertEquals("3d2" + Diff.NL + "< [3] three" + Diff.NL + "9d7"
                + Diff.NL + "< [9] nine" + Diff.NL, revision.toString());
    }

    public void testChangeAtTheEnd() throws DifferentiationFailedException,
            PatchFailedException
    {
        Revision revision = Diff.diff(original, rev2, algorithm);
        assertEquals(1, revision.size());
        assertEquals(ChangeDelta.class, revision.getDelta(0).getClass());
        assertTrue(Diff.compare(revision.patch(original), rev2));
        assertEquals("d7 3" + Diff.NL + "a9 2" + Diff.NL + "[7] seven revised"
                + Diff.NL + "[8] eight revised" + Diff.NL, revision
                .toRCSString());
    }

    public void testPatchFailed() throws DifferentiationFailedException
    {
        try
        {
            Revision revision = Diff.diff(original, rev2, algorithm);
            assertTrue(!Diff.compare(revision.patch(rev1), rev2));
            fail("PatchFailedException not thrown");
        }
        catch (PatchFailedException e)
        {
        }
    }

    public void testPreviouslyFailedShuffle()
            throws DifferentiationFailedException, PatchFailedException
    {
        Object[] orig = new String[]
        { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five", "[6] six" };

        Object[] rev = new String[]
        { "[3] three", "[1] one", "[5] five", "[2] two", "[6] six", "[4] four" };
        Revision revision = Diff.diff(orig, rev, algorithm);
        Object[] patched = revision.patch(orig);
        assertTrue(Diff.compare(patched, rev));
    }

    public void testEdit5() throws DifferentiationFailedException,
            PatchFailedException
    {
        Object[] orig = new String[]
        { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five", "[6] six" };

        Object[] rev = new String[]
        {
                "one revised",
                "two revised",
                "[2] two",
                "[3] three",
                "five revised",
                "six revised",
                "[5] five" };
        Revision revision = Diff.diff(orig, rev, algorithm);
        Object[] patched = revision.patch(orig);
        assertTrue(Diff.compare(patched, rev));
    }

    public void testShuffle() throws DifferentiationFailedException,
            PatchFailedException
    {
        Object[] orig = new String[]
        { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five", "[6] six" };

        for (int seed = 0; seed < 10; seed++)
        {
            Object[] shuffle = DiffHelper.shuffle(orig);
            Revision revision = Diff.diff(orig, shuffle, algorithm);
            Object[] patched = revision.patch(orig);
            if (!Diff.compare(patched, shuffle))
            {
                fail("iter " + seed + " revisions differ after patch");
            }
        }
    }

    public void testRandomEdit() throws DifferentiationFailedException,
            PatchFailedException
    {
        Object[] orig = original;
        for (int seed = 0; seed < 10; seed++)
        {
            Object[] random = DiffHelper.randomEdit(orig, seed);
            Revision revision = Diff.diff(orig, random, algorithm);
            Object[] patched = revision.patch(orig);
            if (!Diff.compare(patched, random))
            {
                fail("iter " + seed + " revisions differ after patch");
            }
            orig = random;
        }
    }

    public void testVisitor()
    {
        Object[] orig = new String[]
        { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five", "[6] six" };
        Object[] rev = new String[]
        {
                "[1] one",
                "[2] two revised",
                "[3] three",
                "[4] four revised",
                "[5] five",
                "[6] six" };

        class Visitor implements RevisionVisitor
        {

            StringBuffer sb = new StringBuffer();

            public void visit(Revision revision)
            {
                sb.append("visited Revision\n");
            }

            public void visit(DeleteDelta delta)
            {
                visit((Delta) delta);
            }

            public void visit(ChangeDelta delta)
            {
                visit((Delta) delta);
            }

            public void visit(AddDelta delta)
            {
                visit((Delta) delta);
            }

            public void visit(Delta delta)
            {
                sb.append(delta.getRevised());
                sb.append("\n");
            }

            public String toString()
            {
                return sb.toString();
            }
        }

        Visitor visitor = new Visitor();
        try
        {
            Diff.diff(orig, rev, algorithm).accept(visitor);
            assertEquals(visitor.toString(), "visited Revision\n"
                    + "[2] two revised\n" + "[4] four revised\n");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }

    public void testAlternativeAlgorithm()
            throws DifferentiationFailedException, PatchFailedException
    {
        Revision revision = Diff.diff(original, rev2, new SimpleDiff());
        assertEquals(1, revision.size());
        assertEquals(ChangeDelta.class, revision.getDelta(0).getClass());
        assertTrue(Diff.compare(revision.patch(original), rev2));
        assertEquals("d7 3" + Diff.NL + "a9 2" + Diff.NL + "[7] seven revised"
                + Diff.NL + "[8] eight revised" + Diff.NL, revision
                .toRCSString());
    }

    public void testLargeShuffles() throws DifferentiationFailedException,
            PatchFailedException
    {
        Object[] orig = DiffHelper.randomSequence(LARGE);
        for (int seed = 0; seed < 3; seed++)
        {
            Object[] rev = DiffHelper.shuffle(orig);
            Revision revision = Diff.diff(orig, rev, algorithm);
            Object[] patched = revision.patch(orig);
            if (!Diff.compare(patched, rev))
            {
                fail("iter " + seed + " revisions differ after patch");
            }
            orig = rev;
        }
    }

    public void testLargeShuffleEdits() throws DifferentiationFailedException,
            PatchFailedException
    {
        Object[] orig = DiffHelper.randomSequence(LARGE);
        for (int seed = 0; seed < 3; seed++)
        {
            Object[] rev = DiffHelper.randomEdit(orig, seed);
            Revision revision = Diff.diff(orig, rev, algorithm);
            Object[] patched = revision.patch(orig);
            if (!Diff.compare(patched, rev))
            {
                fail("iter " + seed + " revisions differ after patch");
            }
        }
    }

    public void testLargeAllEdited() throws DifferentiationFailedException,
            PatchFailedException
    {
        Object[] orig = DiffHelper.randomSequence(LARGE);
        Object[] rev = DiffHelper.editAll(orig);
        Revision revision = Diff.diff(orig, rev, algorithm);
        Object[] patched = revision.patch(orig);
        if (!Diff.compare(patched, rev))
        {
            fail("revisions differ after patch");
        }

    }
}
