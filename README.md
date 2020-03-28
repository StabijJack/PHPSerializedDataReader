# PHPSerializedDataReader
DeSerialize PHPSerialized data to java Objects
using definition on http://www.phpinternalsbook.com/php5/classes_objects/serialization.html 

 these **elements** are ready
 
 these ~~elements~~ not useful

NULL:         N;

**true:         b:1;**

****false:        b:0;****

42:           i:42;

42.3789:      d:42.378900000000002;

**"foobar":     s:6:"foobar";**

"[10, 11, 12]:"    "a:3:{i:0;i:10;i:1;i:11;i:2;i:12;}"

**["foo" => "aaa", "bar" => false]:     a:2:{s:3:"foo";s:3:"aaa";s:3:"bar";b:0}**

~~Test Object with properties: O:4:"Test":3:{s:6:"public";i:1;s:12:"\0*\0protected";i:2;s:13:"\0Test\0private";i:3;}~~

~~Test2 Object with hes own serialization method C:5:"Test2":6:{foobar}~~

~~internal reference a:2:{i:0;s:3:"foo";i:1;R:2;}~~

~~internal reference O:8:"stdClass":1:{s:3:"foo";r:1;}~~