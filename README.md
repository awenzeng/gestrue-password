# GesturePassword

[![JitPack](https://jitpack.io/v/awenzeng/gestrue-password.svg)](https://jitpack.io/#awenzeng/gestrue-password)
[![Downloads](https://jitpack.io/v/awenzeng/gestrue-password/month.svg)](https://jitpack.io/#awenzeng/gestrue-password)

A simple GestruePassword app.Please feel free to use this. (Welcome to Star and Fork)


# Demo

# Download
You can download the latest version from GitHub's [releases page](https://github.com/awenzeng/gestrue-password/releases).

Or use Gradle.
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  ```java
  	dependencies {
	        compile 'com.github.awenzeng:gestrue-password:1.0.0'
	}

```
Or Maven:
```java
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  ```
  ```java
  	<dependency>
	    <groupId>com.github.awenzeng</groupId>
	    <artifactId>gestrue-password</artifactId>
	    <version>1.0.0</version>
	</dependency>
```
For info on using the bleeding edge, see the [Snapshots](https://jitpack.io/#awenzeng/gestrue-password) wiki page.

# How do I use gestrue-password?
Simple use cases with gestrue-password's generated API will look something like this:

in your xml：
```xml
        <com.awen.gesturelib.view.GesturePasswordView
            android:id="@+id/gestureContainer"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@id/gLoginErroTv"
            app:centerHorizontal="true"
            android:layout_marginTop="40dp"
            >
        </com.awen.gesturelib.view.GesturePasswordView>
```


In your Activity:
```java
    public class MainActivity extends AppCompatActivity {
    
    @BindView(R.id.gestureContainer)
    GesturePasswordView gestureContainer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        
        gestureContainer.setGesturePasswordCallback(new GesturePasswordCallback() {
            @Override
            public void onGesturePassword(String password) {
                if(password.length()<6){                  
                    gestureContainer.clearDrawStatus(1500);
                }else{              
                    gestureContainer.clearDrawStatus(0);
                }
            }
        });
    }
}
```
# Thanks

# License
```java
Copyright 2017 AwenZeng

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



