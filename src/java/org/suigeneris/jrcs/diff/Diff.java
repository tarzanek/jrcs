/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: Diff.java,v 1.3 2006/06/08 05:28:34 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff;

import org.suigeneris.jrcs.diff.delta.Delta;
import org.suigeneris.jrcs.diff.myers.MyersDiff;
import org.suigeneris.jrcs.util.ToString;

/**
 * Implements a differencing engine that works on arrays of
 * {@link Object Object}.
 * 
 * <p>
 * Within this library, the word <i>text</i> means a unit of information
 * subject to version control.
 * 
 * <p>
 * Text is represented as <code>Object[]</code> because the diff engine is
 * capable of handling more than plain ascci. In fact, arrays of any type that
 * implements {@link java.lang.Object#hashCode hashCode()} and
 * {@link java.lang.Object#equals equals()} correctly can be subject to
 * differencing using this library.
 * </p>
 * 
 * <p>
 * This library provides a framework in which different differencing algorithms
 * may be used. If no algorithm is specififed, a default algorithm is used.
 * </p>
 * 
 * @version $Revision: 1.3 $ $Date: 2006/06/08 05:28:34 $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Delta
 * @see DiffAlgorithm
 * 
 * modifications:
 * 
 * 27 Apr 2003 bwm
 * 
 * Added some comments whilst trying to figure out the algorithm
 * 
 * 03 May 2003 bwm
 * 
 * Factored out the algorithm implementation into a separate difference
 * algorithm class to allow pluggable algorithms.
 */

public class Diff extends ToString
{
    /** The standard line separator. */
    public static final String NL = System.getProperty("line.separator");

    /** The line separator to use in RCS format output. */
    public static final String RCS_EOL = "\n";

    /** The original sequence. */
    protected final Object[] orig;

    /** The differencing algorithm to use. */
    protected DiffAlgorithm algorithm;

    /**
     * Create a differencing object using the default algorithm
     * 
     * @param the
     *            original text that will be compared
     */
    public Diff(Object[] original)
    {
        this(original, null);
    }

    /**
     * Create a differencing object using the given algorithm
     * 
     * @param o
     *            the original text which will be compared against
     * @param algorithm
     *            the difference algorithm to use.
     */
    public Diff(Object[] original, DiffAlgorithm algorithm)
    {
        if (original == null)
        {
            throw new IllegalArgumentException("no original text");
        }

        this.orig = original;
        if (algorithm != null)
            this.algorithm = algorithm;
        else
            this.algorithm = defaultAlgorithm();
    }

    /**
     * Returns the default differencing algorith.
     * @return the default algorithm.
     */
    protected DiffAlgorithm defaultAlgorithm()
    {
        return new MyersDiff();
    }

    /**
     * compute the difference between the original and a revision.
     * 
     * @param rev
     *            the revision to compare with the original.
     * @return a Revision describing the differences
     */
    public Revision diff(Object[] rev) throws DifferentiationFailedException
    {
        if (orig.length == 0 && rev.length == 0)
            return new Revision();
        else
            return algorithm.diff(orig, rev);
    }

    /**
     * compute the difference between an original and a revision.
     * 
     * @param orig
     *            the original
     * @param rev
     *            the revision to compare with the original.
     * @param algorithm
     *            the difference algorithm to use
     * @return a Revision describing the differences
     */
    public static Revision diff(Object[] orig, Object[] rev,
            DiffAlgorithm algorithm) throws DifferentiationFailedException
    {
        if (orig == null || rev == null)
        {
            throw new IllegalArgumentException("orig or reve is null");
        }
    
        return new Diff(orig, algorithm).diff(rev);
    }

    /**
     * Compute the difference between an original and a revision.
     * 
     * @param orig
     *            the original
     * @param rev
     *            the revision to compare with the original.
     * @return a Revision describing the differences
     */
    public static Revision diff(Object[] orig, Object[] rev)
            throws DifferentiationFailedException
    {
        if (orig == null)
        {
            throw new IllegalArgumentException("orig is null");
        }
    
        if (rev == null)
        {
            throw new IllegalArgumentException("rev is null");
        }
        return Diff.diff(orig, rev, null);
    }

    /**
     * Compares the two input sequences.
     * 
     * @param orig
     *            The original sequence.
     * @param rev
     *            The revised sequence.
     * @return true if the sequences are identical. False otherwise.
     */
    public static boolean compare(Object[] orig, Object[] rev)
    {
        if (orig.length != rev.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < orig.length; i++)
            {
                if (!orig[i].equals(rev[i]))
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Converts an array of {@link Object Object} to a string using
     * {@link NL Diff.NL} as the line separator.
     * 
     * @param o
     *            the array of objects.
     */
    public static String arrayToString(Object[] o)
    {
        return arrayToString(o, NL);
    }

}