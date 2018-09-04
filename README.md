# java-dependency-graph

Visualize dependencies between Java classes by scanning .class files

## Usage

The dependency graph is currently written as DOT file, so you'll want to install [Graphviz](http://graphviz.org/) to render it.

Render dependencies between classes:

```
mvn exec:java -Dexec.mainClass=de.tweerlei.analyzer.Analyze -Dexec.args=/path/to/target/classes/ | dot -Tpng -o classdependencies.png
```

Display simple class names only:

```
mvn exec:java -Dexec.mainClass=de.tweerlei.analyzer.Analyze -Dexec.args="-s /path/to/target/classes/" | dot -Tpng -o simpledependencies.png
```

Aggregate dependency on the package level:

```
mvn exec:java -Dexec.mainClass=de.tweerlei.analyzer.Analyze -Dexec.args="-p /path/to/target/classes/" | dot -Tpng -o pkgdependencies.png
```

Copyright 2018 tweerlei Wruck + Buchmeier GbR - http://www.tweerlei.de/
