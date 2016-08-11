package com.aryalexa.sectionsapp;

import org.python.util.PythonInterpreter;

/**
 * Created by aryalexa on 9/8/16.
 */
public class Python {

    PythonInterpreter interpreter = new PythonInterpreter();


    /**
     * DOCS: http://www.jython.org/javadoc/org/python/util/PythonInterpreter.html
     *
     * you can just use the eval function:

     interpreter.execfile("/path/to/python_file.py");
     PyDictionary result = interpreter.eval("myPythonClass().abc()");

     * Or if you want to get a string:

     PyObject str = interpreter.eval("repr(myPythonClass().abc())");
     System.out.println(str.toString());

     * If you want to supply it with some input from Java variables,
     * you can use set beforehand and than use that variable name within your Python code:

     interpreter.set("myvariable", Integer(21));
     PyObject answer = interpreter.eval("'the answer is: %s' % (2*myvariable)");
     System.out.println(answer.toString());

     */




}
