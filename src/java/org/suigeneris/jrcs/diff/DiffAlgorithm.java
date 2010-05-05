/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: DiffAlgorithm.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
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

/**
 * A simple interface for implementations of differencing algorithms.
 * 
 * @version $Revision: 1.2 $ $Date: 2006/06/08 01:22:27 $
 * 
 * @author <a href="mailto:bwm@hplb.hpl.hp.com">Brian McBride</a>
 */
public interface DiffAlgorithm
{
    /**
     * Computes the difference between the original sequence and the revised
     * sequence and returns it as a
     * {@link org.suigeneris.jrcs.diff.Revision Revision} object.
     * <p>
     * The revision can be used to construct the revised sequence from the
     * original sequence.
     * 
     * @param rev
     *            the revised text
     * @return the revision script.
     * @throws DifferentiationFailedException
     *             if the diff could not be computed.
     */
    public abstract Revision diff(Object[] orig, Object[] rev)
            throws DifferentiationFailedException;
}