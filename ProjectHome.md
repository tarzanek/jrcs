JRCS is a library that knows how to manipulate the archive files produced by the RCS and CVS version control systems. JRCS is not intended to replace neither tool. JRCS was written to be able create archive analysis tools that can do things like identify hot spots in the source code, measure the contributions by each developer, o assess how bugs make it in.

The reasons why JRCS has the ability do do check-ins and save archives is API symmetry, and to simplify the writing of unit tests.

The org.suigeneris.jrcs.diff package implements the differencing engine that JRCS uses. The engine has the power of Unix diff, is simple to understand, and can be used independently of the archive handling functionality. The entry point to the differencing engine is class org.suigeneris.jrcs.diff.Diff .

The org.suigeneris.jrcs.rcs package implements the archive handling functionality. The entry point to the library is class org.suigeneris.jrcs.rcs.Archive .

CAVEAT UTILITOR: Do not make modifications to your archives with JRCS. There needs to be an important amount of additional testing before it's safe to do that.

original author: Juanco Anez <juanco@suigeneris.org>