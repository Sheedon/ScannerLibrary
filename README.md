# ScannerLibrary

​		微光二维码扫描器串口通讯读卡器封装库



### Gradle

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```
dependencies {
	      implementation 'com.github.Sheedon:ScannerLibrary:1.0'
}
```



### Maven

**Step 1.** Add the JitPack repository to your build file

```xml
<repositories>
	<repository>
		   <id>jitpack.io</id>
		   <url>https://jitpack.io</url>
	</repository>
</repositories>
```

**Step 2.** Add the dependency

```xml
	<dependency>
	    <groupId>com.github.Sheedon</groupId>
	    <artifactId>ScannerLibrary</artifactId>
	    <version>1.0</version>
	</dependency>
```



### Use Library

#### 1. Init Library

```java
ScannerFactory.init(this);
```



#### 2. Hold a card reader and monitor

```java
// 获取读卡控制中心
ScannerCenter center = ScannerFactory.getScannerCenter();

center.addListener(new OnScannerListener() {
    /**
     * 扫码枪反馈内容
     * @param code 读卡信息 
     */
     @Override
     public void onScanResult(String code) {
                
     }

     /**
      * 扫码枪是否故障
      * @param isAlarm 是否故障 
      */
     @Override
     public void onScanAlarm(boolean isAlarm) {

     }
});

// 离开页面
center.removeListener(this);
```

