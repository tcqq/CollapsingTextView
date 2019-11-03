[![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=17)
[ ![Download](https://api.bintray.com/packages/tcqq/android/collapsingtextview/images/download.svg?version=1.0.0) ](https://bintray.com/tcqq/android/collapsingtextview/1.0.0/link)
[![Licence](https://img.shields.io/badge/Licence-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# CollapsingTextView

CollapsingTextView enables TextView to support displaying more or less text.

# Usage
Supported attributes with _default_ values:
``` 
<com.tcqq.collapsingtextview.CollapsingTextView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android usual attrs
    (see below).../>
```
|**Attrs**|**Default** |
|:---|:---|
| `collapsing_more_text` | `@string/see_less`
| `collapsing_less_text` | `@string/see_more`
| `collapsing_text_color` | `?attr/colorPrimary`
| `collapsing_showing_line` | `1`

# Setup
#### build.gradle
```
repositories {
    jcenter()
}
```
```
dependencies {
    // Using JCenter
    implementation 'com.tcqq.android:collapsingtextview:1.0.0'
}
```

# Screenshots

![See more](/screenshots/see_more.png)
![See less](/screenshots/see_less.png)

License
-------

Copyright 2019 Alan Perry.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
