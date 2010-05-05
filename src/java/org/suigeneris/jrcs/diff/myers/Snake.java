/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: Snake.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff.myers;

/**
 * Represents a snake in a diffpath.
 * <p>
 * 
 * {@link DiffNode DiffNodes} and {@link Snake Snakes} allow for compression of
 * diffpaths, as each snake is represented by a single {@link Snake Snake} node
 * and each contiguous series of insertions and deletions is represented by a
 * single {@link DiffNode DiffNodes}.
 * 
 * @version $Revision: 1.2 $ $Date: 2006/06/08 01:22:27 $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * 
 */
public final class Snake extends PathNode
{
    /**
     * Constructs a snake node.
     * 
     * @param the
     *            position in the original sequence
     * @param the
     *            position in the revised sequence
     * @param prev
     *            the previous node in the path.
     */
    public Snake(int i, int j, PathNode prev)
    {
        super(i, j, prev);
    }

    /**
     * {@inheritDoc}
     * 
     * @return true always
     */
    public boolean isSnake()
    {
        return true;
    }

}