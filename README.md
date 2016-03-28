# Use a Markdown Wiki as Your CMS

This is a single jar (available at clojars) to add the [Smeagol Markdown Wiki](https://github.com/simon-brooke/smeagol) as a read-only repository to your website.  All code lives in the *smeagol.contrib* namespace.

### Why Markdown?

The Wiki Language Wars are over surpassed by Markdown ...at least for geeks.  Since you're here on Github, you have at least a passing familiarity with [Markdown the  easy-to-read, easy-to-write plain text format](https://daringfireball.net/projects/markdown/syntax).

That said, [Smeagol](https://github.com/simon-brooke/smeagol) is an easy to use simple wiki engine that...
* Is database-free, Smeagol uses plain text files!
* In Markdown format (provided by [markdown-clj](https://github.com/yogthos/markdown-clj)).
* Using git to provide versioning and backup.

Using Smeagol for your Clojure-based webiste is a no-brainer.  You don't need to fire up another host, pay for another database or connect another microservice.   ...in fact you __don't even need to use Smeagol__, just fill the *content* directory with Markdown files and you're good to go.


## Usage


Put this in your namespace:
```clojure
(:require [smeagol.contrib.util :as wiki])
```

...and read Markdown files directly from the *./content/* directory as HTML:
```clojure
(wiki/fetch-html "TestFile")

"<h1>Oh No Not Again</h1><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><p>The end.</p>"
```

Optionally, if you supply your Markdown files with [metadata](https://github.com/fletcher/MultiMarkdown/wiki/MultiMarkdown-Syntax-Guide#metadata) they'll be included.  Also you can read the files in 'lisp-style' rather than 'CamelCase':

```clojure
(wiki/fetch-article "test-file")

{:metadata {:author ["Mr Foo\n"], :type ["storytime"]},
 :html "<h1>Oh No Not Again</h1><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><p>The end.</p>",
 :title "test-file"}
```

## Installation

All artifacts are published in [clojars](https://clojars.org/smeagol.contrib.util), include this in your Leiningen project file:

```
[smeagol.contrib.util "0.9.0"]
```

### Notes

Based on the location of the *contents* Markdown data directory, there may be slight variations based on whether your system is run as a standalone webapp or Jetty WAR.



## License

Copyright © 2016 Heow Goodman

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
