/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: DiffHelper.java,v 1.1 2006/06/08 05:28:34 juanca Exp $
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.suigeneris.jrcs.util.ToString;

/**
 * Provides methods useful for setting up differencing scenarios.
 * 
 * @author juanca
 */
public class DiffHelper extends ToString
{

    public DiffHelper()
    {
    }

    /**
     * Edits all of the items in the input sequence.
     * 
     * @param text
     *            The input sequence.
     * @return A sequence of the same length with all the lines differing from
     *         the corresponding ones in the input.
     */
    public static Object[] editAll(Object[] text)
    {
        Object[] result = new String[text.length];

        for (int i = 0; i < text.length; i++)
            result[i] = text[i] + " <edited>";

        return result;
    }

    /**
     * Performs random edits on the input sequence. Useful for testing.
     * 
     * @param text
     *            The input sequence.
     * @return The sequence with random edits performed.
     */
    public static Object[] randomEdit(Object[] text)
    {
        return DiffHelper.randomEdit(text, text.length);
    }

    /**
     * Performs random edits on the input sequence. Useful for testing.
     * 
     * @param text
     *            The input sequence.
     * @param seed
     *            A seed value for the randomizer.
     * @return The sequence with random edits performed.
     */
    public static Object[] randomEdit(Object[] text, long seed)
    {
        List result = new ArrayList(Arrays.asList(text));
        Random r = new Random(seed);
        int nops = r.nextInt(10);
        for (int i = 0; i < nops; i++)
        {
            boolean del = r.nextBoolean();
            int pos = r.nextInt(result.size() + 1);
            int len = Math.min(result.size() - pos, 1 + r.nextInt(4));
            if (del && result.size() > 0)
            { // delete
                result.subList(pos, pos + len).clear();
            }
            else
            {
                for (int k = 0; k < len; k++, pos++)
                {
                    result.add(pos, "[" + i + "] random edit[" + i + "][" + i
                            + "]");
                }
            }
        }
        return result.toArray();
    }

    /**
     * Shuffles around the items in the input sequence.
     * 
     * @param text
     *            The input sequence.
     * @return The shuffled sequence.
     */
    public static Object[] shuffle(Object[] text)
    {
        return DiffHelper.shuffle(text, text.length);
    }

    /**
     * Shuffles around the items in the input sequence.
     * 
     * @param text
     *            The input sequence.
     * @param seed
     *            A seed value for randomizing the suffle.
     * @return The shuffled sequence.
     */
    public static Object[] shuffle(Object[] text, long seed)
    {
        List result = new ArrayList(Arrays.asList(text));
        Collections.shuffle(result);
        return result.toArray();
    }

    /**
     * Generate a random sequence of the given size.
     * 
     * @param The
     *            size of the sequence to generate.
     * @return The generated sequence.
     */
    public static Object[] randomSequence(int size)
    {
        return DiffHelper.randomSequence(size, size);
    }

    /**
     * Generate a random sequence of the given size.
     * 
     * @param The
     *            size of the sequence to generate.
     * @param seed
     *            A seed value for randomizing the generation.
     * @return The generated sequence.
     */
    public static Object[] randomSequence(int size, long seed)
    {
        Integer[] result = new Integer[size];
        Random r = new Random(seed);
        for (int i = 0; i < result.length; i++)
        {
            result[i] = new Integer(r.nextInt(size));
        }
        return result;
    }

}
