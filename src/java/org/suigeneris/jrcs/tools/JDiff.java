/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: JDiff.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.diff.Revision;

/**
 * A program to compare two files.
 * <p>
 * JDiff produces the deltas between the two given files in Unix diff format.
 * </p>
 * <p>
 * The program was written as a simple test of the
 * {@linkplain org.suigeneris.jrcs.diff diff} package.
 */
public class JDiff
{

    static final String[] loadFile(String name) throws IOException
    {
        BufferedReader data = new BufferedReader(new FileReader(name));
        List lines = new ArrayList();
        String s;
        while ((s = data.readLine()) != null)
        {
            lines.add(s);
        }
        return (String[]) lines.toArray(new String[lines.size()]);
    }

    static final void usage(String name)
    {
        System.err.println("Usage: " + name + " file1 file2");
    }

    public static void main(String[] argv) throws Exception
    {
        if (argv.length < 2)
        {
            usage("JDiff");
        }
        else
        {
            Object[] orig = loadFile(argv[0]);
            Object[] rev = loadFile(argv[1]);

            Diff df = new Diff(orig);
            Revision r = df.diff(rev);

            System.err.println("------");
            System.out.print(r.toString());
            System.err.println("------" + new Date());

            try
            {
                Object[] reco = r.patch(orig);
                // String recos = Diff.arrayToString(reco);
                if (!Diff.compare(rev, reco))
                {
                    System.err.println("INTERNAL ERROR:"
                            + "files differ after patching!");
                }
            }
            catch (Throwable o)
            {
                System.out.println("Patch failed");
            }
        }
    }
}
