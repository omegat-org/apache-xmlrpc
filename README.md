# Apache XML-RPC library (fork,patched)

This is a project forked version of Apache XML-RPC library version 3.1.3.
We only intend to use it for [moses-plugin](https://github.com/omegat-org/moses-plugin) project.

## Security Fix (CVE-2019-17570)

Because original apache project is abondoned over ten years, we patched a known security issue
CVE-2019-17570 using [a patch by Fedora/redhat project](https://bugzilla.redhat.com/show_bug.cgi?id=1775193)

## Changes

- Migrate to Gradle build system
- Published to Maven Central with group 'org.omegat'
- Fixed several errors when built on Java 8.
- Fixed several build errors for javadoc generation.


## How to use

For gradle,

```groovy
dependencies {
    implementation 'org.omegat:xmlrpc:3.1.3-20230208'
}
```


## Copyright

Apache XML-RPC

Copyright 1999-2009 The Apache Software Foundation

This product includes software developed at
The Apache Software Foundation (http://www.apache.org/).
